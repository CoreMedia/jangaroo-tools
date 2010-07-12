/*
 * Copyright 2009 CoreMedia AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package net.jangaroo.ide.idea.exml;

import com.intellij.lang.ASTNode;
import com.intellij.lang.javascript.JavaScriptSupportLoader;
import com.intellij.lang.javascript.psi.ecmal4.JSClass;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlElementDescriptor;
import net.jangaroo.ide.idea.properties.PropertiesCompiler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Jangaroo Project Component.
 */
public class ExmlProjectComponent implements ProjectComponent {
  private Project project;
  private ExmlCompiler exmlc;
  private PropertiesCompiler propc;

  public ExmlProjectComponent(Project project) {
    this.project = project;
  }

  private String getModuleRelativePath(VirtualFile file) {
    ProjectFileIndex projectFileIndex = ProjectRootManager.getInstance(project).getFileIndex();
    final Module module = projectFileIndex.getModuleForFile(file);
    if (module != null) {
      for (VirtualFile sourceRoot : ModuleRootManager.getInstance(module).getSourceRoots()) {
        if (VfsUtil.isAncestor(sourceRoot, file, false)) {
          return VfsUtil.getRelativePath(file, sourceRoot, '.');
        }
      }
    }
    return "";
  }

  public void initComponent() {
    exmlc = new ExmlCompiler();
    propc = new PropertiesCompiler();
    // language injection: see http://www.jetbrains.net/devnet/message/5208687
    PsiManager.getInstance(project).registerLanguageInjector(new LanguageInjector() {
      public void getLanguagesToInject(@NotNull PsiLanguageInjectionHost psiLanguageInjectionHost, @NotNull InjectedLanguagePlaces injectedLanguagePlaces) {
        //System.out.println("psiLanguageInjectionHost: "+psiLanguageInjectionHost);
        PsiFile psiFile = psiLanguageInjectionHost.getContainingFile();
        if (psiFile.getName().endsWith(".exml") && psiLanguageInjectionHost instanceof XmlAttributeValue) {
          VirtualFile exmlFile = psiFile.getOriginalFile().getVirtualFile();
          if (exmlFile == null) {
            return;
          }
          XmlAttributeValue attributeValue = (XmlAttributeValue)psiLanguageInjectionHost;
          if (isImportClassAttribute(attributeValue)) {
            injectedLanguagePlaces.addPlace(JavaScriptSupportLoader.ECMA_SCRIPT_L4, new TextRange(1, attributeValue.getTextRange().getLength()-1), "import ", ";");
          } else {
            String text = attributeValue.getText();
            if (text.startsWith("\"{") && text.endsWith("}\"")) {

              ASTNode node = attributeValue.getParent().getNode();
              XmlTag xmlTag = findTopLevelTag(node);
              if (xmlTag != null) {

                // find relative path to source root to determine package name:
                VirtualFile packageDir = exmlFile.getParent();
                String packageName = packageDir == null ? "" : getModuleRelativePath(packageDir);
                String className = exmlFile.getNameWithoutExtension();

                StringBuilder codePrefix = new StringBuilder("package ").append(packageName).append("{\n");

                String superClassName = findSuperClass(xmlTag);

                // find and append imports:
                List<String> imports = findImports(xmlTag);
                if (superClassName != null) {
                  imports.add(superClassName);
                }
                for (String importName : imports) {
                  codePrefix.append("import ").append(importName).append(";\n");
                }

                codePrefix.append("public class ").append(className);
                if (superClassName != null) {
                  codePrefix.append(" extends ").append(superClassName);
                }
                codePrefix.append("{\n");

                codePrefix.append("public function ").append(className).append("(config:*){\n  super({x:(");
                String codeSuffix = ")});\n}\n}\n}\n";

                TextRange innerRange = new TextRange(2, attributeValue.getTextRange().getLength() - 2);
                injectedLanguagePlaces.addPlace(JavaScriptSupportLoader.ECMA_SCRIPT_L4, innerRange, codePrefix.toString(), codeSuffix);
                // to inject JavaScript, we'd use
                //injectedLanguagePlaces.addPlace(JavaScriptSupportLoader.JAVASCRIPT.getLanguage(), innerRange, "", "");
              }
            }
          }
        }
      }
    });
  }

  private static XmlTag findTopLevelTag(ASTNode node) {
    while (node != null) {
      ASTNode parent = node.getTreeParent();
      if (node instanceof XmlTag && parent instanceof XmlTag) {
        XmlTag tag = (XmlTag)parent;
        if ("component".equals(tag.getLocalName()) && ExmlResourceProvider.EXML_NAMESPACE_URI.equals(tag.getNamespace())) {
          return (XmlTag)node;
        }
      }
      node = parent;
    }
    return null;
  }

  private static String findSuperClass(XmlTag xmlTag) {
    XmlElementDescriptor descriptor = xmlTag.getDescriptor();
    String superClassName = null;
    if (descriptor != null && descriptor.getDeclaration() instanceof JSClass) {
      JSClass jsClass = (JSClass)descriptor.getDeclaration();
      superClassName = jsClass.getQualifiedName();
    }
    return superClassName;
  }

  private static List<String> findImports(XmlTag xmlTag) {
    List<String> imports = new ArrayList<String>();
    PsiElement sibling = xmlTag.getParent().getFirstChild();
    while (sibling != null) {
      if (sibling instanceof XmlTag) {
        XmlTag topLevelXmlTag = (XmlTag)sibling;
        if ("import".equals(topLevelXmlTag.getLocalName())) {
          imports.add(topLevelXmlTag .getAttributeValue("class"));
        }
      }
      sibling = sibling.getNextSibling();
    }
    return imports;
  }

  private static boolean isImportClassAttribute(XmlAttributeValue attributeValue) {
    if (attributeValue.getParent() instanceof XmlAttribute &&
      "class".equals(((XmlAttribute)attributeValue.getParent()).getName())) {
      XmlTag element = (XmlTag)attributeValue.getParent().getParent();
      return "import".equals(element.getLocalName()) &&
        ExmlResourceProvider.EXML_NAMESPACE_URI.equals(element.getNamespace());
    }
    return false;
  }

  public void disposeComponent() {
    exmlc = null;
    propc = null;
  }

  @NotNull
  public String getComponentName() {
    return "ExmlProjectComponent";
  }

  public void projectOpened() {
    CompilerManager compilerManager = CompilerManager.getInstance(project);
    FileType exml = FileTypeManager.getInstance().getFileTypeByExtension("exml");
    FileType properties = FileTypeManager.getInstance().getFileTypeByExtension("properties");
    FileType actionscript = FileTypeManager.getInstance().getFileTypeByExtension("as");

    compilerManager.addCompilableFileType(exml);
    compilerManager.addTranslatingCompiler(exmlc,
      Collections.<FileType>singleton(exml),
      Collections.<FileType>singleton(actionscript));

    compilerManager.addCompilableFileType(properties);
    compilerManager.addTranslatingCompiler(propc,
      Collections.<FileType>singleton(properties),
      Collections.<FileType>singleton(actionscript));
  }

  public void projectClosed() {
    CompilerManager compilerManager = CompilerManager.getInstance(project);
    compilerManager.removeCompiler(exmlc);
    compilerManager.removeCompiler(propc);
  }
}
