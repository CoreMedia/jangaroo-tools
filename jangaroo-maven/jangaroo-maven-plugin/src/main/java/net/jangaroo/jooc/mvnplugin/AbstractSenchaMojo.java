package net.jangaroo.jooc.mvnplugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;


public abstract class AbstractSenchaMojo extends AbstractMojo {

  @Parameter(defaultValue = "classic")
  private String toolkit;

  @Parameter(defaultValue = "${project.build.directory}/ext", readonly = true)
  private String extFrameworkDir;

  @Parameter(defaultValue = "${project.build.directory}/packages", readonly = true)
  private String packagesDir;

  @Parameter(defaultValue = "${project.groupId}:${project.artifactId}")
  private String remotePackagesArtifact;

  @Parameter(defaultValue = "net.jangaroo.com.sencha:ext-js")
  private String extFrameworkArtifact;

  /**
   * The log level to use for Sencha Cmd.
   * The log level for Maven is kind of the base line which determines which log entries are actually shown in the output.
   * When you Maven log level is "info", no "debug" messages for Sencha Cmd are logged.
   * If no log level is given, the Maven log level will be used.
   */
  @Parameter(property = "senchaLogLevel")
  private String senchaLogLevel;

  // ***********************************************************************
  // ************************* GETTERS *************************************
  // ***********************************************************************

  public String getToolkit() {
    return toolkit;
  }

  public String getExtFrameworkDir() {
    return extFrameworkDir;
  }

  public String getExtFrameworkArtifact() {
    return extFrameworkArtifact;
  }


  public String getPackagesDir() {
    return packagesDir;
  }

  public String getRemotePackagesArtifact() {
    return remotePackagesArtifact;
  }

  // ***********************************************************************
  // ************************* SETTERS *************************************
  // ***********************************************************************

  public void setExtFrameworkDir(String extFrameworkDir) {
    this.extFrameworkDir = extFrameworkDir;
  }

  public void setPackagesDir(String packagesDir) {
    this.packagesDir = packagesDir;
  }

  public String getSenchaLogLevel() {
    return senchaLogLevel;
  }
}
