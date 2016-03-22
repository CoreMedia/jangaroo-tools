package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaProfileConfiguration;
import org.apache.maven.plugins.annotations.Parameter;

/**
 *
 */
@SuppressWarnings("FieldCanBeLocal")
public class MavenSenchaConfiguration extends MavenSenchaProfileConfiguration implements SenchaConfiguration {

  /**
   * @see SenchaConfiguration#getType()
   */
  @Parameter
  private String type = Type.CODE;

  /**
   * @see SenchaConfiguration#getToolkit()
   */
  @Parameter
  private String toolkit = "classic";

  /**
   * @see SenchaConfiguration#getTheme()
   */
  @Parameter
  private String theme = "";

  /**
   * @see SenchaConfiguration#getProduction()
   */
  @Parameter
  private MavenSenchaProfileConfigurationProduction production;

  /**
   * @see SenchaConfiguration#getTesting()
   */
  @Parameter
  private MavenSenchaProfileConfigurationTesting testing;

  /**
   * @see SenchaConfiguration#getDevelopment()
   */
  @Parameter
  private MavenSenchaProfileConfigurationDevelopment development;

  /**
   * @see SenchaConfiguration#getExtFrameworkDir()
   */
  @Parameter
  private String extFrameworkDir = "target/ext";

  /**
   * @see SenchaConfiguration#getBuildDir()
   */
  @Parameter
  private String buildDir = "target/sencha/build";

  /**
   * @see SenchaConfiguration#getPackagesDir()
   */
  @Parameter
  private String packagesDir = "target/packages";

  /**
   * @see SenchaConfiguration#isSkipBuild()
   */
  @Parameter
  private boolean skipBuild = false;

  /**
   * @see SenchaConfiguration#isScssFromSrc()
   */
  @Parameter
  private boolean scssFromSrc = false;

  /**
   * Defines the coordinates of the local remote-packages Maven module. E.g.,
   * <pre>
   * &lt;remotePackagesArtifact>net.jangaroo:remote-packages&lt;/remotePackagesArtifact>
   * </pre>
   */
  @Parameter
  private String remotePackagesArtifact;

  @Parameter
  private String extFrameworkArtifact = "net.jangaroo.com.sencha:ext-js";

  @Override
  public String getType() {
    return type;
  }

  @Override
  public String getToolkit() {
    return toolkit;
  }

  @Override
  public String getTheme() {
    return theme;
  }

  @Override
  public SenchaProfileConfiguration getProduction() {
    return production;
  }

  @Override
  public SenchaProfileConfiguration getDevelopment() {
    return development;
  }

  @Override
  public SenchaProfileConfiguration getTesting() {
    return testing;
  }

  @Override
  public String getExtFrameworkDir() {
    return extFrameworkDir;
  }

  @Override
  public String getExtFrameworkArtifact() {
    return extFrameworkArtifact;
  }

  /**
   * @return the Maven project's build directory
   */
  @Override
  public String getBuildDir() {
    return buildDir;
  }

  @Override
  public String getPackagesDir() {
    return packagesDir;
  }

  @Override
  public boolean isSkipBuild() {
    return skipBuild;
  }

  @Override
  public boolean isScssFromSrc() {
    return scssFromSrc;
  }

  @Override
  public void setToolkit(String toolkit) {
    this.toolkit = toolkit;
  }

  @Override
  public void setTheme(String theme) {
    this.theme = theme;
  }

  @Override
  public void setExtFrameworkDir(String extFrameworkDir) {
    this.extFrameworkDir = extFrameworkDir;
  }

  @Override
  public void setPackagesDir(String packagesDir) {
    this.packagesDir = packagesDir;
  }

  @Override
  public void setScssFromSrc(boolean scssFromSrc) {
    this.scssFromSrc = scssFromSrc;
  }

  /**
   * @return the coordinates of the remote packages artifact
   */
  public String getRemotePackagesArtifact() {
    return remotePackagesArtifact;
  }

  // *******************************
  // Setters

  /**
   * Sets all configuration according the Maven project's build dir
   * @param buildDir
   */
  public void setProjectBuildDir(String buildDir) {
    this.extFrameworkDir = buildDir + "/ext";
    this.packagesDir = buildDir + "/packages";
    this.buildDir = buildDir + "/sencha/build";
  }

  public void setType(String type) {
    this.type = type;
  }

}
