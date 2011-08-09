/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.exml.mojo;

import net.jangaroo.exml.compiler.Exmlc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Mojo to compile test EXML sources to test AS3 sources into target/generated-test-sources/joo in phase generate-test-sources.
 *
 * @goal test-exml
 * @phase generate-test-sources
 * @requiresDependencyResolution
 */
public class TestExmlMojo extends AbstractExmlMojo {

  /**
   * Source directory to scan for test files to compile.
   *
   * @parameter expression="${project.build.testSourceDirectory}"
   */
  private File testSourceDirectory;

  /**
   * Output directory for all test ActionScript3 files generated out of exml components
   *
   * @parameter default-value="${project.build.directory}/generated-test-sources/joo"
   */
  private File generatedTestSourcesDirectory;

  /**
   * Set this to 'true' to bypass unit tests entirely. Its use is NOT RECOMMENDED, especially if you
   * enable it using the "maven.test.skip" property, because maven.test.skip disables both running the
   * tests and compiling the tests. Consider using the skipTests parameter instead.
   *
   * @parameter expression="${maven.test.skip}"
   */
  private boolean skip;


  @Override
  protected void executeExmlc(Exmlc exmlc) {
    if(!skip) {
      // Generate all config classes from EXML files:
      exmlc.generateAllConfigClasses();
      exmlc.generateAllComponentClasses();
    }
  }

  @Override
  public File getGeneratedSourcesDirectory() {
    return generatedTestSourcesDirectory;
  }

  @Override
  protected List<File> getActionScriptClassPath() {
    final List<File> classPath = new ArrayList<File>(super.getActionScriptClassPath());
    classPath.add(0, getSourceDirectory());
    //generated sources also needed
    classPath.add(0, generatedSourcesDirectory);
    return classPath;
  }

  protected List<File> getSourcePath() {
    return Collections.singletonList(testSourceDirectory);
  }
}
