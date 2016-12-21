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
 * @goal test-exml
 * @phase generate-test-sources
 * @requiresDependencyResolution
 * @threadSafe
 */
public class ExmlTestCompileMojo extends AbstractExmlCompileMojo {

  /**
   * Set this to 'true' to bypass unit tests entirely. Its use is NOT RECOMMENDED, especially if you
   * enable it using the "maven.test.skip" property, because maven.test.skip disables both running the
   * tests and compiling the tests. Consider using the skipTests parameter instead.
   * @parameter default-value="${maven.test.skip}"
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
    return getGeneratedTestSourcesDirectory();
  }

  @Override
  protected List<File> getActionScriptClassPath() {
    final List<File> classPath = new ArrayList<File>(getMavenPluginHelper().getActionScriptClassPath(true));
    classPath.add(0, getSourceDirectory());
    //generated sources of compilation run also needed
    classPath.add(0, super.getGeneratedSourcesDirectory());
    return classPath;
  }

  protected List<File> getSourcePath() {
    return Collections.singletonList(getTestSourceDirectory());
  }
}
