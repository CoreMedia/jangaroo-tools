/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.exml.mojo;

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
   * @parameter expression="${project.build.directory}/generated-test-sources/joo"
   */
  private File generatedTestSourcesDirectory;

  /**
   * The XSD Schema that will be generated for this test component suite
   *
   * @parameter expression="${project.artifactId}-test.xsd"
   */
  private String testXsd;

  /**
   * The folder where the XSD Schema for this test component suite will be generated
   *
   * @parameter expression="${project.build.directory}/generated-test-resources"
   */
  private File generatedTestResourcesDirectory;

  @Override
  public String getXsd() {
    return testXsd;
  }

  @Override
  public File getGeneratedResourcesDirectory() {
    return generatedTestResourcesDirectory;
  }

  @Override
  public File getGeneratedSourcesDirectory() {
    return generatedTestSourcesDirectory;
  }

  @Override
  protected List<File> getActionScriptClassPath() {
    final List<File> classPath = new ArrayList<File>(super.getActionScriptClassPath());
    classPath.add(0, getSourceDirectory());
    classPath.add(0, getGeneratedSourcesDirectory());
    return classPath;
  }

  protected List<File> getSourcePath() {
    return Collections.singletonList(testSourceDirectory);
  }
}
