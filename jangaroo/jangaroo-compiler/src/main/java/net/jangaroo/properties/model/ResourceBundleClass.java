/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.properties.model;

import net.jangaroo.utils.CompilerUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class ResourceBundleClass {

  private String fullClassName;

  private Map<Locale, PropertiesClass> localizedProperties = new HashMap<Locale, PropertiesClass> ();

  public ResourceBundleClass(String fullClassName) {
    this.fullClassName = fullClassName;
  }

  public String getBundleName() {
    String className = getClassName();
    return className.substring(0, className.length() - "_properties".length());
  }

  public String getClassName() {
    return CompilerUtils.className(fullClassName);
  }

  public String getPackageName() {
    return CompilerUtils.packageName(fullClassName);
  }

  public String getFullClassName() {
    return fullClassName; 
  }

  public void addLocaleProperties(Locale locale, PropertiesClass p ) {
    this.localizedProperties.put(locale, p);
  }

  public Set<Locale> getLocales() {
    return this.localizedProperties.keySet();
  }

  public PropertiesClass getProperties(Locale locale) {
    return this.localizedProperties.get(locale);
  }

  public Collection<PropertiesClass> getPropertiesClasses() {
    return localizedProperties.values();
  }
}
