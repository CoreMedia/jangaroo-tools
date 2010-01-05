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

import com.intellij.facet.impl.ui.FacetTypeFrameworkSupportProvider;
import com.intellij.openapi.roots.ModifiableRootModel;

/**
 * Plug Jangaroo Facet into IDEA.
 */
public class JangarooFacetTypeFrameworkSupportProvider extends FacetTypeFrameworkSupportProvider<JangarooFacet> {

  public JangarooFacetTypeFrameworkSupportProvider() {
    super(JangarooFacetType.INSTANCE);
  }

  @Override
  public String getTitle() {
    return "Jangaroo AS3 to JS Compiler";
  }

  protected void setupConfiguration(JangarooFacet jangarooFacet, ModifiableRootModel modifiableRootModel, String s) {
    // TODO
  }
}
