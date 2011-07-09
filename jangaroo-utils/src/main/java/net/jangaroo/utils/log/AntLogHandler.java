package net.jangaroo.utils.log;

import org.apache.tools.ant.Project;

public class AntLogHandler extends AbstractLogHandler {

  private Project project;

  public AntLogHandler(final Project project) {
    this.project = project;
  }

  public void error(String message, Exception exception) {
    error(message + ": " + exception);
  }

  public void error(String message) {
    project.log(message, Project.MSG_ERR);
  }

  public void warning(String message) {
    project.log(message, Project.MSG_WARN);
  }

  public void info(String message) {
    project.log(message, Project.MSG_INFO);
  }

  public void debug(String message) {
    project.log(message, Project.MSG_DEBUG);
  }

}
