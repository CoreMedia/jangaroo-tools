package net.jangaroo.exml.mojo;

import net.jangaroo.exml.api.ExmlcException;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.compiler.Exmlc;
import net.jangaroo.jooc.api.Jooc;
import net.jangaroo.jooc.mvnplugin.JangarooMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

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

    Exmlc exmlc;
    try {
      getLog().debug("Exmlc configuration: " + exmlConfiguration);
      exmlc = new Exmlc(exmlConfiguration);

      executeExmlc(exmlc);

    } catch (ExmlcException e) {
      throw new MojoFailureException(e.toString(), e);
    }

    getProject().addCompileSourceRoot(gSourcesDirectory.getPath());
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
