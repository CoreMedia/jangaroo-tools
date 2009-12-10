/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public enum ComponentType {
  JavaScript("js"),
  ActionScript("as"),
  EXML("exml");

  private static Map<String, ComponentType> types = new HashMap<String, ComponentType>();

  static {
    for (ComponentType type : ComponentType.values()) {
      types.put(type.extension, type);
    }
  }

  public static ComponentType from(String extension) {
    return types.get(extension);
  }


  ComponentType(String extension) {
    this.extension = extension;
  }

  private String extension;

  public String getExtension() {
    return extension;
  }
}
