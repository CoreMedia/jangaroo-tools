/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.exml.mojo;

import net.jangaroo.exml.compiler.Exmlc;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * A Mojo to compile EXML sources to AS3 sources into target/generated-sources/joo in phase generate-sources.
 *
 * @goal convert-to-mxml
 * @phase generate-sources
 * @requiresDependencyResolution
 * @threadSafe
 */
public class ExmlToMxmlMojo extends AbstractExmlMojo {

  @Override
  public File getGeneratedSourcesDirectory() {
    return super.getSourceDirectory();
  }

  @Override
  protected void executeExmlc(Exmlc exmlc) {
    // Convert all EXML files to MXML:
    exmlc.convertAllExmlToMxml();
  }

  protected List<File> getSourcePath() {
    return Collections.singletonList(getSourceDirectory());
  }
}
