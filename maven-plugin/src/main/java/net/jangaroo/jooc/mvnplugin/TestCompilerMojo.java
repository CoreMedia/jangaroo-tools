package net.jangaroo.jooc.mvnplugin;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Mojo to compile Jangaroo sources from during the test-compile phase.
 *
 * @goal testCompile
 * @phase test-compile
 */
public class TestCompilerMojo extends AbstractCompilerMojo {

  /**
   * Output directory for compiled classes.
   * @parameter expression="${project.build.directory}/test-js-classes"
   */
  private File outputDirectory;

  /**
   * Source directory to scan for files to compile.
   * @parameter expression="${basedir}/src/test/joo"
   */
  private File sourceDirectory;

  protected List getCompileSourceRoots() {
    return Collections.singletonList(sourceDirectory);
  }

  protected File getOutputDirectory() {
    return outputDirectory;
  }


}