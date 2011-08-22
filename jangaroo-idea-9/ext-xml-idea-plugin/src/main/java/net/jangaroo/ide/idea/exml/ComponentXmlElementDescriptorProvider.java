package net.jangaroo.ide.idea.exml;

import com.intellij.lang.javascript.index.JavaScriptIndex;
import com.intellij.lang.javascript.psi.resolve.JSResolveUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.xml.XmlElementDescriptorProvider;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.xml.XmlElementDescriptor;
import com.intellij.xml.XmlNSDescriptor;
import com.intellij.xml.impl.schema.XmlElementDescriptorImpl;
import com.intellij.xml.util.XmlUtil;
import net.jangaroo.exml.model.ComponentType;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A custom XmlElementDescriptorProvider to support navigation from component/attribute elements to
 * the corresponding EXML, AS, or JS files.
 */
public class ComponentXmlElementDescriptorProvider implements XmlElementDescriptorProvider {
  public XmlElementDescriptor getDescriptor(XmlTag xmltag) {
    String namespace = xmltag.getNamespace();
    if (xmltag.getContainingFile().getName().endsWith("." + ComponentType.EXML.getExtension())
        && !ExmlResourceProvider.EXML_NAMESPACE_URI.equals(namespace)) {
      XmlNSDescriptor xmlNSDescriptor = xmltag.getNSDescriptor(namespace, false);
      XmlElementDescriptor xmlElementDescriptor = xmlNSDescriptor != null ? xmlNSDescriptor.getElementDescriptor(xmltag) : null;
      if (xmlElementDescriptor == null) {
        xmlElementDescriptor = XmlUtil.findXmlDescriptorByType(xmltag);
      }
      return xmlElementDescriptor instanceof XmlElementDescriptorImpl ? new ComponentXmlElementDescriptor((XmlElementDescriptorImpl)xmlElementDescriptor) : null;
    }
    return null;
  }

  public static class ComponentXmlElementDescriptor extends XmlElementDescriptorImpl {

    public ComponentXmlElementDescriptor() {
      super();
    }

    public ComponentXmlElementDescriptor(@NotNull XmlElementDescriptorImpl xmlElementDescriptor) {
      super((XmlTag)xmlElementDescriptor.getDeclaration());
      NSDescriptor = xmlElementDescriptor.getNSDescriptor();
    }

    public PsiElement getDeclaration() {
      XmlTag declaration = (XmlTag)super.getDeclaration();
      if (declaration != null) {
        String className = declaration.getAttributeValue("type");
        if (className != null) {
          className = XmlUtil.findLocalNameByQualifiedName(className);
          Project project = declaration.getProject();
          VirtualFile exmlFile = findExmlFile(project, className);
          if (exmlFile == null) {
            PsiElement asClass = JSResolveUtil.findClassByQName(className, JavaScriptIndex.getInstance(project), null);
            if (asClass != null) {
              return asClass;
            }
          } else {
            return PsiManager.getInstance(project).findFile(exmlFile);
          }
        }
      }
      return declaration;
    }

    private static VirtualFile findExmlFile(Project project, String className) {
      String exmlFileName = className.replace('.', '/') + "." + ComponentType.EXML.getExtension();
      Module[] modules = ModuleManager.getInstance(project).getModules();
      for (Module module : modules) {
        VirtualFile[] contentRoots = ModuleRootManager.getInstance(module).getSourceRoots();
        for (VirtualFile contentRoot : contentRoots) {
          VirtualFile exmlFile = contentRoot.findFileByRelativePath(exmlFileName);
          if (exmlFile != null) {
            return exmlFile;
          }
        }
      }
      LibraryTable table = LibraryTablesRegistrar.getInstance().getLibraryTableByLevel(LibraryTablesRegistrar.PROJECT_LEVEL, project);
      if (table != null) {
        for (Library library : table.getLibraries()) {
          if (library.getName().contains(":jangaroo:")) {
            VirtualFile[] files = library.getFiles(OrderRootType.SOURCES);
            for (VirtualFile file : files) {
              if (file.getPath().endsWith(".jar!/")) {
                try {
                  VirtualFile fileInJar = VfsUtil.findFileByURL(new URL(VfsUtil.fixIDEAUrl(file.getUrl() + exmlFileName)));
                  if (fileInJar != null) {
                    return fileInJar;
                  }
                } catch (MalformedURLException e) {
                  Logger.getInstance(ComponentXmlElementDescriptorProvider.class.getName()).error("Error while peeking into library jar file " + file.getPath() + ": " + e);
                }
              }
            }
          }
        }
      }
      return null;
    }

    public XmlNSDescriptor getNSDescriptor() {
      if (NSDescriptor == null || !NSDescriptor.getDeclaration().isValid()) {
        XmlFile xmlfile = XmlUtil.getContainingFile(super.getDeclaration()); // use old getDeclaration() behavior!
        if (xmlfile != null) {
          XmlDocument xmldocument = xmlfile.getDocument();
          if (xmldocument != null) {
            return NSDescriptor = (XmlNSDescriptor)xmldocument.getMetaData();
          }
        }
        return null;
      }
      return NSDescriptor;
    }

  }
}
