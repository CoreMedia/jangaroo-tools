package net.jangaroo.jooc.mvnplugin;

import java.io.File;
import java.util.*;

/**
 * Mojo to compile Jangaroo sources during the compile phase.
 *
 * @goal compile
 * @phase compile
 * @requiresDependencyResolution compile
 */
@SuppressWarnings({"UnusedDeclaration", "UnusedPrivateField"})
public class CompilerMojo extends AbstractCompilerMojo {

  /**
   * Output directory into whose META-INF/resources/joo/classes sub-directory compiled classes are generated.
   * This property is used for <code>jangaroo</code> packaging type as {@link #getOutputDirectory}.
   *
   * @parameter expression="${project.build.outputDirectory}"
   */
  private File outputDirectory;

  /**
   * Location of Jangaroo resources of this module (including compiler output, usually under "joo/") to be added
   * to the webapp. This property is used for <code>war</code> packaging type (actually, all packaging types
   * but <code>jangaroo</code>) as {@link #getOutputDirectory}.
   * Defaults to ${project.build.directory}/jangaroo-output/
   *
   * @parameter expression="${project.build.directory}/jangaroo-output/"
   */
  private File packageSourceDirectory;

  /**
   * Temporary output directory for compiled classes to be packaged into a single *.js file.
   *
   * @parameter expression="${project.build.directory}/temp/jangaroo-output/classes"
   */
  private File tempClassesOutputDirectory;

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
   * This parameter specifies the path and name of the output file containing all
   * compiled classes, relative to the outputDirectory.
   *
   * @parameter expression="joo/${project.groupId}.${project.artifactId}.classes.js"
   */
  private String moduleClassesJsFile;

  /**
   * Output directory for generated API stubs, relative to the outputDirectory.
   *
   * @parameter expression="${project.build.outputDirectory}/META-INF/joo-api"
   */
  private String apiOutputDirectory;

  public File getApiOutputDirectory() {
    return isJangarooPackaging() ? new File(apiOutputDirectory) : null;
  }

  @Override
  protected List<File> getActionScriptClassPath() {
    return getMavenPluginHelper().getActionScriptClassPath(false);
  }

  protected List<File> getCompileSourceRoots() {
    return Arrays.asList(sourceDirectory,getGeneratedSourcesDirectory());
  }

  protected File getOutputDirectory() {
    return isJangarooPackaging() ? new File(outputDirectory, "META-INF/resources") : packageSourceDirectory;
  }

  protected File getTempClassesOutputDirectory() {
    return tempClassesOutputDirectory;
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

}
