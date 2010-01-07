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

import com.intellij.facet.FacetType;
import com.intellij.facet.FacetTypeId;
import com.intellij.facet.Facet;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.util.IconLoader;
//import com.intellij.openapi.module.StdModuleTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Jangaroo FacetType.
 */
public class JangarooFacetType extends FacetType<JangarooFacet, JangarooFacetConfiguration> {

  private static final String JANGAROO_STRING_ID = "jangaroo";
  public static final FacetTypeId<JangarooFacet> ID = new FacetTypeId<JangarooFacet>(JANGAROO_STRING_ID);
  public static final JangarooFacetType INSTANCE = new JangarooFacetType();
  public static final String JANGAROO_FACET_ICON_URL = "/net/jangaroo/jooley-16x16.png";

  public JangarooFacetType() {
    super(ID, JANGAROO_STRING_ID, "Jangaroo");
  }

  public JangarooFacetConfiguration createDefaultConfiguration() {
    return new JangarooFacetConfiguration();
  }

  public JangarooFacet createFacet(@NotNull Module module, String name,
                                   @NotNull JangarooFacetConfiguration jangarooFacetConfiguration,
                                   @Nullable Facet underlyingFacet) {
    return new JangarooFacet(this, module, name, jangarooFacetConfiguration, underlyingFacet);
  }

  public boolean isSuitableModuleType(ModuleType moduleType) {
    return true;
  }

  @Override
  public Icon getIcon() {
    return IconLoader.getIcon(JangarooFacetType.JANGAROO_FACET_ICON_URL);
  }

  @Override
  public boolean isOnlyOneFacetAllowed() {
    return true;
  }

}
