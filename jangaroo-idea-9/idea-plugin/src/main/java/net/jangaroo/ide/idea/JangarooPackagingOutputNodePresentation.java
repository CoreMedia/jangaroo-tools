package net.jangaroo.ide.idea;

import com.intellij.facet.pointers.FacetPointer;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.openapi.util.IconLoader;
import com.intellij.packaging.ui.TreeNodePresentation;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.NotNull;

public class JangarooPackagingOutputNodePresentation extends TreeNodePresentation {

  public JangarooPackagingOutputNodePresentation(FacetPointer facetPointer) {
    myFacetPointer = facetPointer;
  }


  public String getPresentableName() {
    String moduleName = myFacetPointer == null ? "<unknown>" : myFacetPointer.getModuleName();
    return (new StringBuilder()).append("'").append(moduleName).append("' Jangaroo packaging output").toString();
  }

  public void render(@NotNull PresentationData presentationData, SimpleTextAttributes mainAttributes,
                     SimpleTextAttributes commentAttributes) {
    presentationData.setIcons(IconLoader.getIcon(JangarooFacetType.JANGAROO_FACET_ICON_URL));
    presentationData.addText(getPresentableName(), mainAttributes);
  }


  public int getWeight() {
    return 10;
  }

  private FacetPointer myFacetPointer;
}
