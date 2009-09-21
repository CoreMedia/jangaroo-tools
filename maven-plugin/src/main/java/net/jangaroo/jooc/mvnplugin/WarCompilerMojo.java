package net.jangaroo.jooc.mvnplugin;

import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;

import java.io.File;
import java.util.*;

/**
 * The <code>war-compile</code> goal compiles the Actionscript sources to javascript. The output directory
 * is set to the web application script directory to have them accessible during web application execution.<br/>
 * This goal is NOT bound to the jangaroo lifecycle. It should be used in conjunction with
 * the <code>war-package</code> goal in the <code>war</code> lifecycle by defining its execution
 * as shown in the following sniplet<br/>
 * <pre>
 * ...
 * &lt;plugin>
 *  &lt;groupId>net.jangaroo&lt;/groupId>
 *  &lt;artifactId>jangaroo-maven-plugin&lt;/artifactId>
 *  &lt;extensions>true&lt;/extensions>
 *  &lt;executions>
 *   &lt;execution>
 *     &lt;id>compile-as-sources&lt;/id>
 *     &lt;phase>compile&lt;/phase>
 *     &lt;goals>
 *      &lt;goal>war-compile&lt;/goal>
 *     &lt;/goals>
 *   &lt;/execution>
 *   &lt;execution>
 *    &lt;id>war-package&lt;/id>
 *    &lt;goals>
 *     &lt;goal>war-package&lt;/goal>
 *    &lt;/goals>
 *   &lt;/execution>
 *  &lt;/executions>
 * &lt;/plugin>
 * ...
 * </pre>
 *
 * @goal war-compile
 * @phase compile
 */
public class WarCompilerMojo extends AbstractCompilerMojo {

  /**
   * Output directory for compiled classes.
   *
   * @parameter expression="${scripts}" default-value="${project.build.directory}/${project.build.finalName}/scripts/classes"
   */
  private File outputDirectory;

  /**
   * Source directory to scan for files to compile.
   *
   * @parameter expression="src/main/joo"
   */
  private File sourceDirectory;
  /**
   * A list of inclusion filters for the compiler.
   *
   * @parameter
   */
  private Set includes = new HashSet();
  /**
   * A list of exclusion filters for the compiler.
   *
   * @parameter
   */
  private Set excludes = new HashSet();

  /**
   * This parameter specifies the name of the output file containing all
   * compiled classes.
   *
   * @parameter default-value="${project.build.directory}/${project.build.finalName}/scripts/${project.artifactId}.js"
   */
  private String outputFileName;

  /**
   * Output directory for all ActionScript3 files generated out of exml components
   *
   * @parameter expression="${project.build.directory}/generated-sources/joo"
   */
  private File generatedSourcesDirectory;


  protected List<File> getCompileSourceRoots() {
    return Arrays.asList(sourceDirectory, generatedSourcesDirectory);
  }

  protected File getOutputDirectory() {
    return outputDirectory;
  }


  protected SourceInclusionScanner getSourceInclusionScanner(int staleMillis) {
    return getSourceInclusionScanner(includes, excludes, staleMillis);
  }


  public String getOutputFileName() {
    return outputFileName;
  }

}
