package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaProfileConfiguration;
import org.apache.maven.plugins.annotations.Parameter;

@SuppressWarnings("FieldCanBeLocal")
public class MavenSenchaConfiguration extends MavenSenchaProfileConfiguration implements SenchaConfiguration {

  /**
   * @see SenchaConfiguration#isEnabled()
   */
  @Parameter(defaultValue = "true")
  private boolean enabled = true;

  /**
   * @see SenchaConfiguration#getType()
   */
  @Parameter(defaultValue = "CODE")
  private Type type = Type.CODE;

  /**
   * @see SenchaConfiguration#getToolkit()
   */
  @Parameter(defaultValue = "classic")
  private String toolkit = "classic";

  /**
   * @see SenchaConfiguration#getTheme()
   */
  @Parameter(defaultValue = "")
  private String theme = "";

  /**
   * @see SenchaConfiguration#getProduction()
   */
  @Parameter(defaultValue = "${production}")
  private MavenSenchaProfileConfigurationProduction production;

  /**
   * @see SenchaConfiguration#getTesting()
   */
  @Parameter(defaultValue = "${testing}")
  private MavenSenchaProfileConfigurationTesting testing;

  /**
   * @see SenchaConfiguration#getDevelopment()
   */
  @Parameter(defaultValue = "${development}")
  private MavenSenchaProfileConfigurationDevelopment development;

  /**
   * @see SenchaConfiguration#getExtFrameworkDir()
   */
  @Parameter(defaultValue = "${project.build.directory}/ext")
  private String extFrameworkDir = "target/ext";

  /**
   * @see SenchaConfiguration#getBuildDir()
   */
  @Parameter(property = "project.build.directory", readonly = true)
  private String buildDir = "target/sencha/build";

  /**
   * @see SenchaConfiguration#getPackagesDir()
   */
  @Parameter(defaultValue = "${project.build.directory}/packages", readonly = true)
  private String packagesDir = "target/packages";

  /**
   * @see SenchaConfiguration#isSkipBuild()
   */
  @Parameter(defaultValue = "false")
  private boolean skipBuild;

  /**
   * @see SenchaConfiguration#isScssFromSrc()
   */
  @Parameter(defaultValue = "false")
  private boolean scssFromSrc;

  /**
   * Defines the coordinates of the local remote-packages Maven module. E.g.,
   * <pre>
   * &lt;remotePackagesArtifact>
   *    &lt;groupdId>com.coremedia.blueprint&lt;/groupId>
   *    &lt;artifactId>remote-packages&lt;/artifactId>
   * &lt;/remotePackagesArtifact>
   * </pre>
   */
  @Parameter(property = "remotePackagesArtifact")
  private ArtifactItem remotePackagesArtifact;

  @Parameter(property = "project.packaging", readonly = true)
  private String packaging;

  @Parameter(property = "project.basedir")
  private String baseDir;

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public Type getType() {
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
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public void setType(Type type) {
    this.type = type;
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
  public void setBuildDir(String buildDir) {
    this.buildDir = buildDir;
  }

  @Override
  public void setPackagesDir(String packagesDir) {
    this.packagesDir = packagesDir;
  }

  @Override
  public void setSkipBuild(boolean skipBuild) {
    this.skipBuild = skipBuild;
  }

  @Override
  public void setScssFromSrc(boolean scssFromSrc) {
    this.scssFromSrc = scssFromSrc;
  }

  /**
   * @return the Maven project's base directory
   */
  public String getBaseDir() {
    return baseDir;
  }

  /**
   * @return the coordinates of the remote packages artifact
   */
  public ArtifactItem getRemotePackagesArtifact() {
    return remotePackagesArtifact;
  }

  /**
   * @param remotePackagesArtifact Defines the coordinates of the remote-packages artifact
   */
  public void setRemotePackagesArtifact(ArtifactItem remotePackagesArtifact) {
    this.remotePackagesArtifact = remotePackagesArtifact;
  }

  public static boolean isSenchaPackaging(String packaging) {
    return Types.JANGAROO_TYPE.equals(packaging) || Type.isSenchaPackaging(packaging);
  }

  /**
   * Holds the basic coordinates of the Maven artifact
   */
  public static final class ArtifactItem {

    @Parameter
    private String groupId;

    @Parameter
    private String artifactId;

    /**
     * @param groupId Sets the artifact's groupId
     */
    public void setGroupId(String groupId) {
      this.groupId = groupId;
    }

    /**
     * @param artifactId Sets the artifact's artifactId
     */
    public void setArtifactId(String artifactId) {
      this.artifactId = artifactId;
    }

    /**
     * @return the artifact's groupId
     */
    public String getGroupId() {
      return groupId;
    }

    /**
     * @return the artifact's artifactId
     */
    public String getArtifactId() {
      return artifactId;
    }
  }
}
