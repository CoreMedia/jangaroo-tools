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

import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.openapi.options.ConfigurationException;
import net.jangaroo.ide.idea.exml.ui.ExmlFacetEditorTabUI;
import org.jetbrains.annotations.Nls;

import javax.swing.*;

/**
 * Jangaroo Facet settings (Editor Tab).
 */
public class ExmlFacetEditorTab extends FacetEditorTab {
  private ExmlcConfigurationBean exmlcConfigurationBean;
  private ExmlFacetEditorTabUI form;


  public ExmlFacetEditorTab(ExmlcConfigurationBean exmlcConfigurationBean) {
    this.exmlcConfigurationBean = exmlcConfigurationBean;
  }

  @Nls
  public String getDisplayName() {
    return "EXML";
  }

  public Icon getIcon() {
    return ExmlFacetType.INSTANCE.getIcon();
  }

  public String getHelpTopic() {
    return null;
  }

  public JComponent createComponent() {
    if (form==null) {
      form = new ExmlFacetEditorTabUI();
    }
    return form.getRootComponent();
  }

  public boolean isModified() {
    return form!=null && form.isModified(exmlcConfigurationBean);
  }

  public void apply() throws ConfigurationException {
    if (form!=null) {
      form.getData(exmlcConfigurationBean);
    }
  }

  public void reset() {
    if (form!=null) {
      form.setData(exmlcConfigurationBean);
    }
  }

  public void disposeUIResources() {
    //Disposer.dispose((Disposable)form);
    form = null;
  }

  public ExmlcConfigurationBean getExmlcConfigurationBean() {
    return exmlcConfigurationBean;
  }
}