package net.jangaroo.ide.idea;

import com.intellij.facet.pointers.FacetPointersManager;
import com.intellij.openapi.roots.ui.configuration.artifacts.sourceItems.FacetBasedPackagingSourceItemsProvider;
import com.intellij.packaging.ui.ArtifactEditorContext;

public class JangarooCompilerOutputSourceItemProvider extends FacetBasedPackagingSourceItemsProvider<JangarooFacet, JangarooCompilerOutputElement> {
  public JangarooCompilerOutputSourceItemProvider() {
    super(JangarooFacetType.ID, JangarooCompilerOutputElementType.getInstance());
  }


  protected JangarooCompilerOutputNodePresentation createPresentation(JangarooFacet facet) {
    return new JangarooCompilerOutputNodePresentation(FacetPointersManager.getInstance(facet.getModule().getProject()).create(facet));
  }


  protected JangarooCompilerOutputElement createElement(ArtifactEditorContext context, JangarooFacet facet) {
    return new JangarooCompilerOutputElement(context.getProject(), facet);
  }


  protected JangarooFacet getFacet(JangarooCompilerOutputElement element) {
    return element.getFacet();
  }

}
