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

}
