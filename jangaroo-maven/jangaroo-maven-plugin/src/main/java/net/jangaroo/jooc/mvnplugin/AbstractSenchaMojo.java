package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaConfigBuilder;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;


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

  protected static void configureDefaults(SenchaConfigBuilder configBuilder, String defaultsFileName) throws MojoExecutionException {
    try {
      configBuilder.defaults(defaultsFileName);
    } catch (IOException e) {
      throw new MojoExecutionException("Cannot load " + defaultsFileName, e);
    }
  }

  protected void writeFile(@Nonnull SenchaConfigBuilder configBuilder,
                           @Nonnull String destinationFileName,
                           @Nullable String comment)
          throws MojoExecutionException {

    configBuilder.destFile(destinationFileName);
    if (comment != null ) {
      configBuilder.destFileComment(comment);
    }

    try {
      configBuilder.buildFile();
    } catch (IOException io) {
      throw new MojoExecutionException(String.format("Writing %s failed", destinationFileName), io);
    }
  }
}
