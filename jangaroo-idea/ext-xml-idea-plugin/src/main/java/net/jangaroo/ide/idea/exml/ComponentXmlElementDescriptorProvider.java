package net.jangaroo.ide.idea.exml;

import com.intellij.codeInsight.daemon.Validator;
import com.intellij.lang.javascript.index.JavaScriptIndex;
import com.intellij.lang.javascript.psi.resolve.JSResolveUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.xml.XmlElementDescriptorProvider;
import com.intellij.psi.meta.PsiWritableMetaData;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.IncorrectOperationException;
import com.intellij.xml.XmlAttributeDescriptor;
import com.intellij.xml.XmlElementDescriptor;
import com.intellij.xml.XmlElementDescriptorAwareAboutChildren;
import com.intellij.xml.XmlNSDescriptor;
import com.intellij.xml.util.XmlUtil;
import net.jangaroo.extxml.model.ComponentType;
import org.jetbrains.annotations.NotNull;

/**
 * A custom XmlElementDescriptorProvider to support navigation from component/attribute elements to
 * the corresponding EXML, AS, or JS files.
 */
public class ComponentXmlElementDescriptorProvider implements XmlElementDescriptorProvider {
  public XmlElementDescriptor getDescriptor(XmlTag xmltag) {
    String namespace = xmltag.getNamespace();
    if (xmltag.getContainingFile().getName().endsWith("." + ComponentType.EXML.getExtension())
        && !ExmlApplicationComponent.EXML_NAMESPACE_URI.equals(namespace)) {
      XmlNSDescriptor xmlNSDescriptor = xmltag.getNSDescriptor(namespace, false);
      XmlElementDescriptor xmlElementDescriptor = xmlNSDescriptor != null ? xmlNSDescriptor.getElementDescriptor(xmltag) : null;
      if (xmlElementDescriptor == null) {
        xmlElementDescriptor = XmlUtil.findXmlDescriptorByType(xmltag);
      }
      return xmlElementDescriptor == null ? null : new ComponentXmlElementDescriptor(xmlElementDescriptor);
    }
    return null;
  }

  public static class ComponentXmlElementDescriptor implements XmlElementDescriptor, PsiWritableMetaData, Validator<XmlTag>, XmlElementDescriptorAwareAboutChildren {
    private final XmlElementDescriptor xmlElementDescriptor;

    public ComponentXmlElementDescriptor(@NotNull XmlElementDescriptor xmlElementDescriptor) {
      this.xmlElementDescriptor = xmlElementDescriptor;
    }

    public XmlElementDescriptor getXmlElementDescriptor() {
      return xmlElementDescriptor;
    }

    public PsiElement getDeclaration() {
      PsiElement declaration = xmlElementDescriptor.getDeclaration();
      if (declaration instanceof XmlTag) {
        String className = ((XmlTag) declaration).getAttributeValue("type");
        if (className != null) {
          className = XmlUtil.findLocalNameByQualifiedName(className);
          Project project = xmlElementDescriptor.getDeclaration().getProject();
          VirtualFile exmlFile = findExmlFile(project, className);
          if (exmlFile == null) {
            return JSResolveUtil.findClassByQName(className, JavaScriptIndex.getInstance(project), null);
          } else {
            return PsiManager.getInstance(project).findFile(exmlFile);
          }
        }
      }
      return declaration;
    }

    private static VirtualFile findExmlFile(Project project, String className) {
      String exmlFileName = className.replaceAll("\\.", "/") + "." + ComponentType.EXML.getExtension();
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
      return null;
    }

    public String getName(PsiElement psielement) {
      return xmlElementDescriptor.getName(psielement);
    }

    public String getName() {
      return xmlElementDescriptor.getName();
    }

    public void init(PsiElement psielement) {
      xmlElementDescriptor.init(psielement);
    }

    public Object[] getDependences() {
      return xmlElementDescriptor.getDependences();
    }

    public XmlNSDescriptor getNSDescriptor() {
      return xmlElementDescriptor.getNSDescriptor();
    }

    public XmlElementDescriptor[] getElementsDescriptors(XmlTag xmltag) {
      return xmlElementDescriptor.getElementsDescriptors(xmltag);
    }

    public XmlAttributeDescriptor[] getAttributesDescriptors(XmlTag xmltag) {
      return xmlElementDescriptor.getAttributesDescriptors(xmltag);
    }

    public XmlAttributeDescriptor getAttributeDescriptor(String s, XmlTag xmltag) {
      return xmlElementDescriptor.getAttributeDescriptor(s, xmltag);
    }

    public XmlAttributeDescriptor getAttributeDescriptor(XmlAttribute xmlattribute) {
      return xmlElementDescriptor.getAttributeDescriptor(xmlattribute);
    }

    public int getContentType() {
      return xmlElementDescriptor.getContentType();
    }

    public XmlElementDescriptor getElementDescriptor(XmlTag childTag, XmlTag contextTag) {
      return xmlElementDescriptor.getElementDescriptor(childTag, contextTag);
    }

    public String getQualifiedName() {
      return xmlElementDescriptor.getQualifiedName();
    }

    public String getDefaultName() {
      return xmlElementDescriptor.getDefaultName();
    }

    public void setName(String s) throws IncorrectOperationException {
      if (xmlElementDescriptor instanceof PsiWritableMetaData) {
        ((PsiWritableMetaData) xmlElementDescriptor).setName(s);
      }
    }

    public boolean allowElementsFromNamespace(String s, XmlTag xmltag) {
      return !(xmlElementDescriptor instanceof XmlElementDescriptorAwareAboutChildren)
          || ((XmlElementDescriptorAwareAboutChildren) xmlElementDescriptor).allowElementsFromNamespace(s, xmltag);
    }

    @SuppressWarnings({"unchecked"})
    public void validate(@NotNull XmlTag xmlTag, @NotNull ValidationHost validationHost) {
      if (xmlElementDescriptor instanceof Validator) {
        ((Validator) xmlElementDescriptor).validate(xmlTag, validationHost);
      }
    }
  }
}
