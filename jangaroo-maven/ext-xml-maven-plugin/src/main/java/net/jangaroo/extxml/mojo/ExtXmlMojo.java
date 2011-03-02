/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml.mojo;

import java.io.File;

/**
 * A Mojo to compile EXML sources to AS3 sources into target/generated-sources/joo in phase generate-sources.
 *
 * @goal extxml
 * @phase generate-sources
 * @requiresDependencyResolution
 */
public class ExtXmlMojo extends AbstractExtXmlMojo {

  /**
   * Source directory to scan for files to compile.
   *
   * @parameter expression="${basedir}/src/main/joo"
   */
  private File sourceDirectory;

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

  /**
   * Output directory for all ActionScript3 files generated out of exml components
   *
   * @parameter expression="${project.build.directory}/generated-sources/joo"
   */
  private File generatedSourcesDirectory;

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
  public File getSourceDirectory() {
    return sourceDirectory;
  }

  @Override
  public File getGeneratedSourcesDirectory() {
    return generatedSourcesDirectory;
  }

  @Override
  public File getGeneratedResourcesDirectory() {
    return generatedResourcesDirectory;
  }

}
