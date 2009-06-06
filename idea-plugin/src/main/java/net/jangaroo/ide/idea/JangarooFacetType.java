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
  
  public static final FacetTypeId<JangarooFacet> ID = new FacetTypeId<JangarooFacet>("jangaroo");
  public static final JangarooFacetType INSTANCE = new JangarooFacetType();
  static final String JANGAROO_FACET_ICON_URL = "/net/jangaroo/jooley-16x16.png";

  public JangarooFacetType() {
    super(ID, "jangaroo", "Jangaroo");
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
