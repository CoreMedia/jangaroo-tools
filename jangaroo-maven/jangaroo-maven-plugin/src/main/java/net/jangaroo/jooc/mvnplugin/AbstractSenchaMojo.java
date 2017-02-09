package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaConfigBuilder;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.regex.Pattern;


public abstract class AbstractSenchaMojo extends AbstractMojo {

  @Parameter
  private String toolkit = SenchaUtils.TOOLKIT_CLASSIC;

  @Parameter(defaultValue = "${project.build.directory}/ext", readonly = true)
  private String extFrameworkDir;

  @Parameter(defaultValue = "${project.build.directory}/packages", readonly = true)
  private String packagesDir;

  @Parameter(defaultValue = "${project.groupId}:${project.artifactId}")
  private String remotePackagesArtifact;

  /**
   * a regexp matching the group and artifact id of a sencha framewrok artifact
   */
  @Parameter(defaultValue = "((net\\.jangaroo\\.com)|(com\\.coremedia))\\.sencha:ext-js(-pkg)?(-gpl)?")
  private String extFrameworkArtifactRegexp;

  /**
   * The log level to use for Sencha Cmd.
   * The log level for Maven is kind of the base line which determines which log entries are actually shown in the output.
   * When you Maven log level is "info", no "debug" messages for Sencha Cmd are logged.
   * If no log level is given, the Maven log level will be used.
   */
  @Parameter(property = "senchaLogLevel")
  private String senchaLogLevel;

  private volatile Pattern extFrameworkArtifactPattern;

  // ***********************************************************************
  // ************************* GETTERS *************************************
  // ***********************************************************************

  public String getToolkit() {
    return toolkit;
  }

  public String getExtFrameworkDir() {
    return extFrameworkDir;
  }

  public Pattern getExtFrameworkArtifactPattern() {
    if (extFrameworkArtifactPattern == null) {
      extFrameworkArtifactPattern = Pattern.compile(getExtFrameworkArtifactRegexp());
    }
    return extFrameworkArtifactPattern;
  }

  public String getExtFrameworkArtifactRegexp() {
    return extFrameworkArtifactRegexp;
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
                           @Nonnull String destinationFileDir,
                           @Nonnull String destinationFileName,
                           @Nullable String comment)
          throws MojoExecutionException {

    String tmpDestFileName = destinationFileName + ".tmp";
    final File tmpDestFile = new File(destinationFileDir, tmpDestFileName);
    final File destFile = new File(destinationFileDir, destinationFileName);
    configBuilder.destFile(tmpDestFile);
    if (comment != null ) {
      configBuilder.destFileComment(comment);
    }
    try {
      configBuilder.buildFile();
    } catch (IOException io) {
      try {
        Files.delete(tmpDestFile.toPath());
      } catch (IOException e) {
        getLog().warn("Unable to delete temporary file " + tmpDestFile.getAbsolutePath(), e);
      }
      throw new MojoExecutionException(String.format("Writing %s failed", tmpDestFile.getName()), io);
    }
    try {
      Files.move(tmpDestFile.toPath(), destFile.toPath(),
              StandardCopyOption.REPLACE_EXISTING,
              StandardCopyOption.ATOMIC_MOVE);
    } catch (IOException e) {
      throw new MojoExecutionException(String.format("Moving %s to %s failed", tmpDestFile.getName(), destFile.getAbsolutePath()), e);
    }
  }

  protected boolean isExtFrameworkArtifact(Artifact artifact) {
    return isExtFramework(artifact.getGroupId(), artifact.getArtifactId());
  }

  protected boolean isExtFrameworkDependency(Dependency dependency) {
    return isExtFramework(dependency.getGroupId(), dependency.getArtifactId());
  }

  private boolean isExtFramework(String groupId, String artifactId) {
    String key = groupId + ":" + artifactId;
    return getExtFrameworkArtifactPattern().matcher(key).matches();
  }

}
