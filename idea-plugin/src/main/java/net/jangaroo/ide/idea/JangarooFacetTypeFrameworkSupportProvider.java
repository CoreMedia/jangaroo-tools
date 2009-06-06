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
