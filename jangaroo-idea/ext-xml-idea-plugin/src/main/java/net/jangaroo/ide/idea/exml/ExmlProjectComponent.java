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

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiManager;
import com.intellij.psi.LanguageInjector;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.InjectedLanguagePlaces;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.lang.Language;
import com.intellij.lang.javascript.JavaScriptSupportLoader;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * Jangaroo Project Component.
 */
public class ExmlProjectComponent implements ProjectComponent {
  private Project project;
  private ExmlCompiler exmlc;

  public ExmlProjectComponent(Project project) {
    this.project = project;
  }

  public void initComponent() {
    exmlc = new ExmlCompiler();
    // language injection: see http://www.jetbrains.net/devnet/message/5208687
    PsiManager.getInstance(project).registerLanguageInjector(new LanguageInjector() {
      public void getLanguagesToInject(@NotNull PsiLanguageInjectionHost psiLanguageInjectionHost, @NotNull InjectedLanguagePlaces injectedLanguagePlaces) {
        System.out.println("psiLanguageInjectionHost: "+psiLanguageInjectionHost);
        if (psiLanguageInjectionHost.getContainingFile().getName().endsWith(".exml") && psiLanguageInjectionHost instanceof XmlAttributeValue) {
          XmlAttributeValue attributeValue = (XmlAttributeValue)psiLanguageInjectionHost;
          if (isImportClassAttribute(attributeValue)) {
            injectedLanguagePlaces.addPlace(JavaScriptSupportLoader.ECMA_SCRIPT_L4, new TextRange(1, attributeValue.getTextRange().getLength()-1), "import ", ";");
          } else {
            String text = attributeValue.getText();
            if (text.startsWith("\"{") && text.endsWith("}\"")) {
              TextRange innerRange = new TextRange(2, attributeValue.getTextRange().getLength() - 2);
              injectedLanguagePlaces.addPlace(getAS3(), innerRange, "function(config){return (", ");}");
            }
          }
        }
      }
    });
  }

  private static Language getAS3() {
    return JavaScriptSupportLoader.JAVASCRIPT.getLanguage();
    //return JavaScriptSupportLoader.ECMA_SCRIPT_L4;
    //return ((LanguageFileType)FileTypeManager.getInstance().getFileTypeByExtension("as")).getLanguage();
  }

  private static boolean isImportClassAttribute(XmlAttributeValue attributeValue) {
    if (attributeValue.getParent() instanceof XmlAttribute &&
      "class".equals(((XmlAttribute)attributeValue.getParent()).getName())) {
      XmlTag element = (XmlTag)attributeValue.getParent().getParent();
      return "import".equals(element.getLocalName()) &&
        ExmlApplicationComponent.EXML_NAMESPACE_URI.equals(element.getNamespace());
    }
    return false;
  }

  public void disposeComponent() {
    exmlc = null;
  }

  @NotNull
  public String getComponentName() {
    return "ExmlProjectComponent";
  }

  public void projectOpened() {
    CompilerManager compilerManager = CompilerManager.getInstance(project);
    FileType exml = FileTypeManager.getInstance().getFileTypeByExtension("exml");
    compilerManager.addCompilableFileType(exml);
    FileType javascript = FileTypeManager.getInstance().getFileTypeByExtension("as");
    compilerManager.addTranslatingCompiler(exmlc,
      Collections.<FileType>singleton(exml),
      Collections.<FileType>singleton(javascript));
  }

  public void projectClosed() {
    CompilerManager compilerManager = CompilerManager.getInstance(project);
    compilerManager.removeCompiler(exmlc);
  }
}
