/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.properties.model;

import net.jangaroo.utils.CompilerUtils;

public final class ResourceBundleClass {

  private String fullClassName;

  public ResourceBundleClass(String fullClassName) {
    this.fullClassName = fullClassName;
  }

  public String getBundleName() {
    String className = getClassName();
    return className.substring(0, className.length() - CompilerUtils.PROPERTIES_CLASS_SUFFIX.length());
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

}
