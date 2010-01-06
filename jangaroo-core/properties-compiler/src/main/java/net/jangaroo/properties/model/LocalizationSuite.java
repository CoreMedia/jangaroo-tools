/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.properties.model;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LocalizationSuite {

  private File rootDir;

  private File outputDir;

  private Map<String, ResourceBundleClass> resourceBundlesByFullClassName = new HashMap<String, ResourceBundleClass>();

  public LocalizationSuite(File rootDir, File outputDir) {
    this.rootDir = rootDir;
    this.outputDir = outputDir;
  }

  public File getOutputDir() {
    return outputDir;
  }

  public File getRootDir() {
    return rootDir;
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
}
