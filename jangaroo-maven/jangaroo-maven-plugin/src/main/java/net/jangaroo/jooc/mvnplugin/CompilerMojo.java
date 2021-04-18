package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.config.JoocConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Mojo to compile Jangaroo sources during the compile phase.
 */
@SuppressWarnings({"UnusedDeclaration", "UnusedPrivateField"})
@Mojo(name = "compile",
        defaultPhase = LifecyclePhase.COMPILE,
        requiresDependencyResolution = ResolutionScope.COMPILE,
        threadSafe = true)
public class CompilerMojo extends AbstractCompilerMojo {

  @Parameter(defaultValue = "${project.build.directory}")
  private String buildDirectoryPath;

  /**
   * Sencha package output directory into whose 'src' sub-directory compiled classes are generated.
   * This property is used for <code>pkg</code> packaging type as {@link #getOutputDirectory}.
   */
  private File packageOutputDirectory;

  /**
   * Sencha app output directory into whose 'app' sub-directory compiled classes are generated.
   * This property is used for <code>jangaroo-app</code> packaging type as {@link #getOutputDirectory}.
   */
  @Parameter(defaultValue = "${project.build.directory}/app")
  private File appOutputDirectory;

  /**
   * Output directory for compilation reports like the cyclic classes report.
   */
  @Parameter(defaultValue = "${project.build.directory}/temp")
  private File reportOutputDirectory;

  /**
   * A list of inclusion filters for the compiler.
   */
  @Parameter
  private Set<String> includes = new HashSet<>();

  /**
   * A list of exclusion filters for the compiler.
   */
  @Parameter
  private Set<String> excludes = new HashSet<>();

  /**
   * Output directory for generated API stubs, relative to the outputDirectory.
   */
  @Parameter(defaultValue = "${project.build.outputDirectory}/META-INF/joo-api")
  private File apiOutputDirectory;

  @Override
  public File getApiOutputDirectory() {
    return Type.containsJangarooSources(getProject()) ? apiOutputDirectory : null;
  }

  @Override
  protected List<File> getActionScriptCompilePath() {
    return getMavenPluginHelper().getActionScriptCompilePath(false);
  }

  @Override
  protected List<File> getActionScriptClassPath() {
    return getMavenPluginHelper().getActionScriptClassPath(false);
  }

  @Override
  protected List<File> getCompileSourceRoots() {
    return Arrays.asList(getSourceDirectory(), getGeneratedSourcesDirectory());
  }

  @Override
  protected File getOutputDirectory() {
    return Type.JANGAROO_APP_PACKAGING.equals(getProject().getPackaging()) ? appOutputDirectory : getPackageOutputDirectory();
  }

  public File getReportOutputDirectory() {
    return reportOutputDirectory;
  }

  @Override
  protected Set<String> getIncludes() {
    return includes;
  }

  @Override
  protected Set<String> getExcludes() {
    return excludes;
  }

  @Override
  protected JoocConfiguration createJoocConfiguration(Log log) throws MojoExecutionException, MojoFailureException {
    JoocConfiguration joocConfiguration = super.createJoocConfiguration(log);
    if (joocConfiguration != null) {
      joocConfiguration.setCatalogOutputDirectory(getCatalogOutputDirectory());
      joocConfiguration.setReportOutputDirectory(getReportOutputDirectory());
    }
    return joocConfiguration;
  }

  @Override
  protected boolean findUnusedDependencies() {
    return true;
  }

  @Override
  protected boolean isTestRun() {
    return false;
  }

  private File getPackageOutputDirectory() {
    if (packageOutputDirectory == null) {
      packageOutputDirectory = new File(buildDirectoryPath + SenchaUtils.getPackagesPath(getProject()));
    }
    return packageOutputDirectory;
  }
}
