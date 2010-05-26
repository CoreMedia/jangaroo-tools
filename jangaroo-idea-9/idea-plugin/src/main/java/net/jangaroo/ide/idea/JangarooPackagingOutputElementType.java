package net.jangaroo.ide.idea;

import com.intellij.openapi.project.Project;
import com.intellij.packaging.impl.elements.FacetBasedPackagingElementType;
import org.jetbrains.annotations.NotNull;

public class JangarooPackagingOutputElementType extends FacetBasedPackagingElementType<JangarooPackagingOutputElement, JangarooFacet> {

  public JangarooPackagingOutputElementType() {
    super("jangaroo-compiler-output", "Jangaroo Compiler Output", JangarooFacetType.ID);
  }


  protected String getDialogTitle() {
    return "Select Module";
  }


  protected String getDialogDescription() {
    return "Select Module with Jangaroo Facet";
  }


  protected String getItemText(JangarooFacet item) {
    return item.getModule().getName();
  }


  @NotNull
  public JangarooPackagingOutputElement createEmpty(@NotNull Project project) {
    return new JangarooPackagingOutputElement(project, null);
  }

  public static JangarooPackagingOutputElementType getInstance() {
    return getInstance(JangarooPackagingOutputElementType.class);
  }

  protected JangarooPackagingOutputElement createElement(Project project, JangarooFacet facet) {
    return new JangarooPackagingOutputElement(project, facet);
  }

}
