/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.exml.mojo;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * A Mojo to compile EXML sources to AS3 sources into target/generated-sources/joo in phase generate-sources.
 *
 * @goal exml
 * @phase generate-sources
 * @requiresDependencyResolution
 */
public class ExmlMojo extends AbstractExmlMojo {

  /**
   * The namespace of the component suite
   *
   * @parameter expression="${project.artifactId}"
   */
  private String namespace;

  /**
   * The default namespace prefix of the component suite
   *
   * @parameter expression="${project.artifactId}"
   */
  private String namespacePrefix;

  @Override
  public String getNamespace() {
    return namespace;
  }

  @Override
  public String getNamespacePrefix() {
    return namespacePrefix;
  }

  @Override
  public String getXsd() {
    return xsd;
  }

  @Override
  public File getGeneratedResourcesDirectory() {
    return generatedResourcesDirectory;
  }

  protected List<File> getSourcePath() {
    return Collections.singletonList(getSourceDirectory());
  }
}
