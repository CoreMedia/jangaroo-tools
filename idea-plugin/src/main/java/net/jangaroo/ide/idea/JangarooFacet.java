package net.jangaroo.ide.idea;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.openapi.module.Module;
import org.jetbrains.annotations.NotNull;

/**
 * An IDEA Facet for Jangaroo.
 */
public class JangarooFacet extends Facet<JangarooFacetConfiguration> {

  public JangarooFacet(@NotNull FacetType facetType, @NotNull Module module, String s, @NotNull JangarooFacetConfiguration facetConfiguration, Facet facet) {
    super(facetType, module, s, facetConfiguration, facet);
  }
}
