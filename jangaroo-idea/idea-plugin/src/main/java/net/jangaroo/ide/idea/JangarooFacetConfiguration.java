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
package net.jangaroo.ide.idea;

import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ModuleRootModel;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.module.Module;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jdom.Element;

/**
 * Jangaroo IDEA Facet configuration.
 */
public class JangarooFacetConfiguration implements FacetConfiguration, PersistentStateComponent<JoocConfigurationBean> {

  private JoocConfigurationBean joocConfigurationBean;

  public FacetEditorTab[] createEditorTabs(FacetEditorContext facetEditorContext, FacetValidatorsManager facetValidatorsManager) {
    return new FacetEditorTab[]{ new JangarooFacetEditorTab(getJoocConfigurationBean(facetEditorContext))};
  }

  private synchronized JoocConfigurationBean getJoocConfigurationBean(FacetEditorContext facetEditorContext) {
    if (joocConfigurationBean==null) {
      String outputPrefix = null, moduleName = null;
      if (facetEditorContext!=null) {
        Module module = facetEditorContext.getModule();
        if (module != null) {
          ModuleRootModel rootModel = facetEditorContext.getRootModel();
          if (rootModel != null) {
            VirtualFile[] contentRoots = rootModel.getContentRoots();
            if (contentRoots.length > 0) {
              outputPrefix = contentRoots[0].getPath();
              moduleName = module.getName();
            }
          }
        }
      }
      joocConfigurationBean = new JoocConfigurationBean(outputPrefix, moduleName);
    }
    return joocConfigurationBean;
  }

  public void readExternal(Element element) throws InvalidDataException {
    // ignore
  }

  public void writeExternal(Element element) throws WriteExternalException {
    // ignore
  }

  public JoocConfigurationBean getState() {
    return getJoocConfigurationBean(null);
  }

  public void loadState(JoocConfigurationBean state) {
    if (joocConfigurationBean==null) {
      joocConfigurationBean = state;
    } else {
      // copy into existing instance, as it may already be used by some JangarooFacetEditorTab:
      XmlSerializerUtil.copyBean(state, joocConfigurationBean);
    }
  }
}
