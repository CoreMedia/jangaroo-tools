/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.properties.model;

import org.apache.maven.shared.model.fileset.FileSet;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LocalizationSuite {

  private FileSet properties;

  private File outputDir;

  private Map<String, ResourceBundleClass> resourceBundlesByFullClassName = new HashMap<String, ResourceBundleClass>();

  public LocalizationSuite(FileSet properties, File outputDir) {
    this.properties = properties;
    this.outputDir = outputDir;
  }

  public File getOutputDir() {
    return outputDir;
  }

  public FileSet getProperties() {
    return properties;
  }



  public void addResourceBundleClass(ResourceBundleClass rbc) {
    this.resourceBundlesByFullClassName.put(rbc.getFullClassName(), rbc);
  }

  public ResourceBundleClass getClassByFullName(String fullClassName) {
    return this.resourceBundlesByFullClassName.get(fullClassName);
  }

  public Collection<ResourceBundleClass> getResourceBundles() {
    return this.resourceBundlesByFullClassName.values();
  }

  public File getRootDir() {
    return new File(properties.getDirectory());
  }
}
