package net.jangaroo.jooc.ant;

import net.jangaroo.jooc.AbstractCompileLog;
import net.jangaroo.jooc.CompileLog;
import net.jangaroo.jooc.StdOutCompileLog;
import org.apache.tools.ant.Project;

public class AntCompileLog extends AbstractCompileLog {

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
  protected void doLogError(String msg) {
    getProject().log(msg, Project.MSG_ERR);
  }

  @Override
  public void warning(final String msg) {
    // MSG_WARN does not appear in the output when run with maven-antrun-plugin
    getProject().log(msg, Project.MSG_ERR);
  }
}
