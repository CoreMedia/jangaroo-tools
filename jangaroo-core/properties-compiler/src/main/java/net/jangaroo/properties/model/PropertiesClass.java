/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.properties.model;

import java.io.File;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

public class PropertiesClass {

  private ResourceBundleClass resourceBundle;
  private Locale locale;
  private Properties properties;
  private File srcFile;

  public PropertiesClass(ResourceBundleClass resourceBundle, Locale locale, Properties properties, File srcFile) {
    this.resourceBundle = resourceBundle;
    resourceBundle.addLocaleProperties(locale, this);

    this.locale = locale;
    this.properties = properties;
    this.srcFile = srcFile;
  }

  public ResourceBundleClass getResourceBundle() {
    return resourceBundle;
  }

  public Locale getLocale() {
    return locale;
  }

  public Properties getProperties() {
    return properties;
  }

  public Set getProps() {
    return properties.entrySet();
  }

  public File getSrcFile() {
    return srcFile;
  }
}
