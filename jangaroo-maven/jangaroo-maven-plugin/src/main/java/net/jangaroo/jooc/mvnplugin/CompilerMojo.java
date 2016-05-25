package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.config.JoocConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.io.IOException;
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

  private static final OrFileFilter PROPERTIES_FILE_FILTER = new OrFileFilter(DirectoryFileFilter.DIRECTORY, new RegexFileFilter("[^_]*\\.properties"));

  /**
   * Sencha package output directory into whose 'src' sub-directory compiled classes are generated.
   * This property is used for <code>jangaroo-pkg</code> packaging type as {@link #getOutputDirectory}.
   */
  @Parameter(defaultValue = "${project.build.directory}/packages/local/package")
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
  private String apiOutputDirectory;

  @Override
  public File getApiOutputDirectory() {
    return Type.containsJangarooSources(getProject()) ? new File(apiOutputDirectory) : null;
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
    return Type.JANGAROO_PKG_PACKAGING.equals(getProject().getPackaging()) ? packageOutputDirectory : appOutputDirectory;
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
  public void execute() throws MojoExecutionException, MojoFailureException {
    super.execute();
    if (getSourceDirectory() != null && getApiOutputDirectory() != null && getSourceDirectory().exists()) {
      try {
        getLog().info(String.format("Copying properties source files from %s to API output directory %s...", getSourceDirectory().getPath(), getApiOutputDirectory().getPath()));
        FileUtils.copyDirectory(getSourceDirectory(), getApiOutputDirectory(), PROPERTIES_FILE_FILTER);
      } catch (IOException e) {
        throw new MojoExecutionException("Failed to copy properties source files to API output directory.", e);
      }
    }
  }
}
