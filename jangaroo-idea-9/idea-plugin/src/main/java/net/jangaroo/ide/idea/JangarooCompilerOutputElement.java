package net.jangaroo.ide.idea;

import com.intellij.compiler.ant.Generator;
import com.intellij.facet.pointers.FacetPointer;
import com.intellij.facet.pointers.FacetPointersManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.packaging.artifacts.ArtifactType;
import com.intellij.packaging.elements.*;
import com.intellij.packaging.impl.ui.DelegatedPackagingElementPresentation;
import com.intellij.packaging.ui.ArtifactEditorContext;
import com.intellij.packaging.ui.PackagingElementPresentation;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class JangarooCompilerOutputElement extends PackagingElement<JangarooCompilerOutputElement.JangarooCompilerOutputElementState> {

  public JangarooCompilerOutputElement(Project project, JangarooFacet facet) {
    super(JangarooCompilerOutputElementType.getInstance());
    myProject = project;
    myFacetPointer = facet == null ? null : FacetPointersManager.getInstance(project).create(facet);
  }


  public PackagingElementPresentation createPresentation(@NotNull ArtifactEditorContext context) {
    return new DelegatedPackagingElementPresentation(new JangarooCompilerOutputNodePresentation(myFacetPointer));
  }


  public List<? extends Generator> computeAntInstructions(@NotNull PackagingElementResolvingContext resolvingContext,
                                     @NotNull AntCopyInstructionCreator creator,
                                     @NotNull ArtifactAntGenerationContext generationContext,
                                     @NotNull ArtifactType artifactType) {
    return Collections.emptyList();
  }


  public void computeIncrementalCompilerInstructions(@NotNull IncrementalCompilerInstructionCreator creator,
                                                     @NotNull PackagingElementResolvingContext resolvingContext,
                                                     @NotNull ArtifactIncrementalCompilerContext compilerContext,
                                                     @NotNull ArtifactType artifactType) {
    JangarooFacet facet = (JangarooFacet)myFacetPointer.getFacet();
    if (facet != null) {
      File outputDirectory = facet.getConfiguration().getState().getOutputDirectory().getParentFile();
      if (outputDirectory.exists()) {
        VirtualFile outputRoot = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(outputDirectory);
        assert outputRoot != null;
        creator.addDirectoryCopyInstructions(outputRoot);
      }
    }
  }


  public String toString() {
    return (new StringBuilder()).append("jangaroo-compiler-output:").append(myFacetPointer.getFacetName()).append("(")
      .append(myFacetPointer.getModuleName()).append(")").toString();
  }

  public boolean isEqualTo(@NotNull PackagingElement element) {
    return (element instanceof JangarooCompilerOutputElement) &&
      myFacetPointer != null &&
      myFacetPointer.equals(((JangarooCompilerOutputElement)element).myFacetPointer);
  }

  public JangarooCompilerOutputElementState getState() {
    JangarooCompilerOutputElementState state = new JangarooCompilerOutputElementState();
    state.myFacetPointer = myFacetPointer == null ? null : myFacetPointer.getId();
    return state;
  }


  public JangarooFacet getFacet() {
    return (JangarooFacet)myFacetPointer.getFacet();
  }

  public void loadState(JangarooCompilerOutputElementState state) {
    String pointer = state.myFacetPointer;
    myFacetPointer = pointer == null ? null : FacetPointersManager.getInstance(myProject).create(pointer);
  }

  private final Project myProject;
  private FacetPointer myFacetPointer;

  public static class JangarooCompilerOutputElementState {
    public String myFacetPointer;
  }

}
