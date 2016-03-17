package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.config.JoocConfiguration;
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

  /**
   * Output directory into whose META-INF/resources/joo/classes sub-directory compiled classes are generated.
   * This property is used for <code>jangaroo</code> packaging type as {@link #getOutputDirectory}.
   */
  @Parameter(defaultValue = "${project.build.outputDirectory}")
  private File outputDirectory;

  /**
   * Location of Jangaroo resources of this module (including compiler output, usually under "joo/") to be added
   * to the webapp. This property is used for <code>war</code> packaging type (actually, all packaging types
   * but <code>jangaroo</code>) as {@link #getOutputDirectory}.
   * Defaults to ${project.build.directory}/jangaroo-output/
   */
  @Parameter(defaultValue = "${project.build.directory}/jangaroo-output/")
  private File packageSourceDirectory;

  /**
   * Temporary output directory for compiled classes to be packaged into a single *.js file.
   */
  @Parameter(defaultValue = "${project.build.directory}/temp/jangaroo-output/classes")
  private File tempClassesOutputDirectory;

  /**
   * Output directory for compilation reports like the cyclic classes report.
   */
  @Parameter(defaultValue = "${project.build.directory}/temp")
  private File reportOutputDirectory;

  /**
   * A list of inclusion filters for the compiler.
   */
  @Parameter
  private Set<String> includes = new HashSet<String>();

  /**
   * A list of exclusion filters for the compiler.
   */
  @Parameter
  private Set<String> excludes = new HashSet<String>();

  /**
   * This parameter specifies the path and name of the output file containing all
   * compiled classes, relative to the outputDirectory.
   */
  @Parameter(defaultValue = "joo/${project.groupId}.${project.artifactId}.classes.js")
  private String moduleClassesJsFile;

  /**
   * Output directory for generated API stubs, relative to the outputDirectory.
   */
  @Parameter(defaultValue = "${project.build.outputDirectory}/META-INF/joo-api")
  private String apiOutputDirectory;

  public File getApiOutputDirectory() {
    return Type.containsJangarooSources(getProject()) ? new File(apiOutputDirectory) : null;
  }

  @Override
  protected List<File> getActionScriptClassPath() {
    return getMavenPluginHelper().getActionScriptClassPath(false);
  }

  protected List<File> getCompileSourceRoots() {
    return Arrays.asList(getSourceDirectory(), getGeneratedSourcesDirectory());
  }

  protected File getOutputDirectory() {
    return Type.containsJangarooSources(getProject()) ? new File(outputDirectory, "META-INF/resources") : packageSourceDirectory;
  }

  protected File getTempClassesOutputDirectory() {
    return tempClassesOutputDirectory;
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

  public String getModuleClassesJsFileName() {
    return moduleClassesJsFile;
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

}
