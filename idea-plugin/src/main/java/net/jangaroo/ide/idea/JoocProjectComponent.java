package net.jangaroo.ide.idea;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * Jangaroo Project Component.
 */
public class JoocProjectComponent implements ProjectComponent {
  private Project project;
  private JangarooCompiler jooc;

  public JoocProjectComponent(Project project) {
    this.project = project;
  }

  public void initComponent() {
    jooc = new JangarooCompiler();
  }

  public void disposeComponent() {
    jooc = null;
  }

  @NotNull
  public String getComponentName() {
    return "JoocProjectComponent";
  }

  public void projectOpened() {
    CompilerManager compilerManager = CompilerManager.getInstance(project);
    FileType actionscript = FileTypeManager.getInstance().getFileTypeByExtension("as");
    compilerManager.addCompilableFileType(actionscript);
    FileType javascript = FileTypeManager.getInstance().getFileTypeByExtension("js");
    compilerManager.addTranslatingCompiler(jooc,
      Collections.<FileType>singleton(actionscript),
      Collections.<FileType>singleton(javascript));
  }

  public void projectClosed() {
    CompilerManager compilerManager = CompilerManager.getInstance(project);
    compilerManager.removeCompiler(jooc);
  }
}
