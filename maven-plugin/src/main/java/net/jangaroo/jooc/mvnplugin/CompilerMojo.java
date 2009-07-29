package net.jangaroo.jooc.mvnplugin;

import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Mojo to compile Jangaroo sources during the compile phase.
 *
 * @goal compile
 * @phase compile
 */
public class CompilerMojo extends AbstractCompilerMojo {

  /**
   * Output directory for compiled classes.
   * @parameter expression="${project.build.directory}/joo/classes"
   */
  private File outputDirectory;

  /**
   * Source directory to scan for files to compile.
   * @parameter expression="${basedir}/src/main/joo"
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
   * When "mergeOutput" is "true", this parameter specifies the name of the output file containing all
   * compiled classes. Otherwise, this parameter will be ignored.
   * @parameter expression="${project.build.finalName}.js"
   */
  private String outputFileName;

  protected List getCompileSourceRoots() {
    return Collections.singletonList(sourceDirectory);
  }

  protected File getOutputDirectory() {
    return outputDirectory;
  }


  protected SourceInclusionScanner getSourceInclusionScanner(int staleMillis) {
    return getSourceInclusionScanner(includes, excludes, staleMillis);
  }

  protected SourceInclusionScanner getSourceInclusionScanner(String inputFileEnding) {
    return getSourceInclusionScanner(includes, excludes, inputFileEnding);
  }

  public String getOutputFileName() {
    return outputFileName;
  }
}
