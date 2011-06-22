package net.jangaroo.jooc.ant;

import net.jangaroo.jooc.CompileLog;
import net.jangaroo.jooc.StdOutCompileLog;
import org.apache.tools.ant.Project;

public class AntCompileLog extends StdOutCompileLog implements CompileLog {

  private Project project;

  public AntCompileLog() {
  }

  public AntCompileLog(final Project project) {
    this.project = project;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(final Project project) {
    this.project = project;
  }

  @Override
  public void error(final String msg) {
    getProject().log(msg, Project.MSG_ERR);
    errors = true;
  }

  @Override
  public void warning(final String msg) {
    // MSG_WARN does not appear in the output when run with maven-antrun-plugin
    getProject().log(msg, Project.MSG_ERR);
    warnings = true;
  }
}
