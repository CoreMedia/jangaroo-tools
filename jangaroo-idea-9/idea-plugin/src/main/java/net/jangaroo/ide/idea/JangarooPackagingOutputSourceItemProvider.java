package net.jangaroo.ide.idea;

import com.intellij.facet.pointers.FacetPointersManager;
import com.intellij.openapi.roots.ui.configuration.artifacts.sourceItems.FacetBasedPackagingSourceItemsProvider;
import com.intellij.packaging.ui.ArtifactEditorContext;

public class JangarooPackagingOutputSourceItemProvider extends FacetBasedPackagingSourceItemsProvider<JangarooFacet, JangarooPackagingOutputElement> {
  public JangarooPackagingOutputSourceItemProvider() {
    super(JangarooFacetType.ID, JangarooPackagingOutputElementType.getInstance());
  }


  protected JangarooPackagingOutputNodePresentation createPresentation(JangarooFacet facet) {
    return new JangarooPackagingOutputNodePresentation(FacetPointersManager.getInstance(facet.getModule().getProject()).create(facet));
  }


  protected JangarooPackagingOutputElement createElement(ArtifactEditorContext context, JangarooFacet facet) {
    return new JangarooPackagingOutputElement(context.getProject(), facet);
  }


  protected JangarooFacet getFacet(JangarooPackagingOutputElement element) {
    return element.getFacet();
  }

}
