package net.jangaroo.ide.idea;

import com.intellij.openapi.project.Project;
import com.intellij.packaging.impl.elements.FacetBasedPackagingElementType;
import org.jetbrains.annotations.NotNull;

public class JangarooCompilerOutputElementType extends FacetBasedPackagingElementType<JangarooCompilerOutputElement, JangarooFacet> {

  public JangarooCompilerOutputElementType() {
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
  public JangarooCompilerOutputElement createEmpty(@NotNull Project project) {
    return new JangarooCompilerOutputElement(project, null);
  }

  public static JangarooCompilerOutputElementType getInstance() {
    return getInstance(JangarooCompilerOutputElementType.class);
  }

  protected JangarooCompilerOutputElement createElement(Project project, JangarooFacet facet) {
    return new JangarooCompilerOutputElement(project, facet);
  }

}
