package net.jangaroo.exml.mojo;

import net.jangaroo.exml.api.ExmlcException;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.compiler.Exmlc;
import net.jangaroo.exml.config.ValidationMode;
import net.jangaroo.jooc.api.CompileLog;
import net.jangaroo.jooc.api.FilePosition;
import net.jangaroo.jooc.api.Jooc;
import net.jangaroo.jooc.mvnplugin.JangarooMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Mojo to invoke the EXML compiler.
 */
public abstract class AbstractExmlMojo extends JangarooMojo {
  /**
   * The Maven project object
   *
   * @parameter expression="${project}"
   */
  private MavenProject project;

  /**
   * Source directory to scan for files to compile.
   *
   * @parameter default-value="${basedir}/src/main/joo"
   */
  private File sourceDirectory;

  /**
   * Output directory for all ActionScript3 files generated out of exml components
   *
   * @parameter default-value="${project.build.directory}/generated-sources/joo"
   */
  private File generatedSourcesDirectory;

  /**
   * The folder where the XSD Schema for this component suite will be generated
   *
   * @parameter default-value="${project.build.directory}/generated-resources"
   */
  private File generatedResourcesDirectory;

  /**
   * The package into which config classes of EXML components are generated.
   *
   * @parameter
   * @required
   */
  private String configClassPackage;

  /**
   * A switch to control EXML validation against EXML schema and generated component suite schemas.
   * It can take the values "off" to skip validation (default), "warn" to log a warning whenever a validation
   * error occurs, and "error" to stop the build with an error whenever validation errors occur.
   *
   * @parameter default-value="off"
   */
  private String validationMode;

  /**
   * A list of inclusion filters for the compiler.
   *
   * @parameter
   */
  private Set<String> includes = new HashSet<String>();
  /**
   * A list of exclusion filters for the compiler.
   *
   * @parameter
   */
  private Set<String> excludes = new HashSet<String>();
  /**
   * Sets the granularity in milliseconds of the last modification
   * date for testing whether a source needs recompilation.
   *
   * @parameter expression="${lastModGranularityMs}" default-value="0"
   */
  private int staleMillis;

  protected void useAllSources() {
    staleMillis = -1;
  }

  @Override
  protected MavenProject getProject() {
    return project;
  }

  public File getSourceDirectory() {
    return sourceDirectory;
  }

  public File getGeneratedSourcesDirectory() {
    return generatedSourcesDirectory;
  }

  public File getGeneratedResourcesDirectory() {
    return generatedResourcesDirectory;
  }

  public void execute() throws MojoExecutionException, MojoFailureException {

    File gSourcesDirectory = getGeneratedSourcesDirectory();
    if (!gSourcesDirectory.exists()) {
      getLog().info("generating sources into: " + gSourcesDirectory.getPath());
      getLog().debug("created " + gSourcesDirectory.mkdirs());
    }

    if (!generatedResourcesDirectory.exists()) {
      getLog().info("generating resources into: " + generatedResourcesDirectory.getPath());
      getLog().debug("created " + generatedResourcesDirectory.mkdirs());
    }

    ExmlConfiguration exmlConfiguration = new ExmlConfiguration();
    exmlConfiguration.setConfigClassPackage(configClassPackage);
    exmlConfiguration.setClassPath(getActionScriptClassPath());
    exmlConfiguration.setOutputDirectory(gSourcesDirectory);
    exmlConfiguration.setResourceOutputDirectory(generatedResourcesDirectory);
    List<File> sourcePath = getSourcePath();
    try {
      exmlConfiguration.setSourcePath(sourcePath);
    } catch (IOException e) {
      throw new MojoExecutionException("could not determine source directory", e);
    }
    exmlConfiguration.setSourceFiles(getMavenPluginHelper().computeStaleSources(sourcePath, includes, excludes, gSourcesDirectory, Exmlc.EXML_SUFFIX, Jooc.AS_SUFFIX, staleMillis));
    if (StringUtils.isNotEmpty(validationMode)) {
      try {
        exmlConfiguration.setValidationMode(ValidationMode.valueOf(validationMode.toUpperCase()));
      } catch (IllegalArgumentException e) {
        throw new MojoFailureException("The specified EXML validation mode '" + validationMode + "' is unsupported. " +
                "Legal values are 'error', 'warn', and 'off'.");
      }
    }
    CompileLog compileLog = new MavenCompileLog();
    exmlConfiguration.setLog(compileLog);

    Exmlc exmlc;
    try {
      getLog().debug("Exmlc configuration: " + exmlConfiguration);
      exmlc = new Exmlc(exmlConfiguration);

      executeExmlc(exmlc);

    } catch (ExmlcException e) {
      throw new MojoFailureException(e.toString(), e);
    }

    if (compileLog.hasErrors()) {
      throw new MojoFailureException("There were EXML compiler errors, see log for details.");
    }

    getProject().addCompileSourceRoot(gSourcesDirectory.getPath());
  }

  private class MavenCompileLog implements CompileLog {
    private boolean hasErrors = false;

    @Override
    public void error(FilePosition position, String msg) {
      error(String.format("%s (%d): %s", position.getFileName(), position.getLine(), msg));
    }

    @Override
    public void error(String msg) {
      hasErrors = true;
      getLog().error(msg);
    }

    @Override
    public void warning(FilePosition position, String msg) {
      warning(String.format("%s (%d): %s", position.getFileName(), position.getLine(), msg));
    }

    @Override
    public void warning(String msg) {
      getLog().warn(msg);
    }

    @Override
    public boolean hasErrors() {
      return hasErrors;
    }
  }

  /**
   * Execute the exmlc parts that are needed by the concret mojo
   * @param exmlc the configured exmlc
   */
  protected abstract void executeExmlc(Exmlc exmlc);

  protected abstract List<File> getSourcePath();

  protected List<File> getActionScriptClassPath() {
    return getMavenPluginHelper().getActionScriptClassPath(false);
  }
}
