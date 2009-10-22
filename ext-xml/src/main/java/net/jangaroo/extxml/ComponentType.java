/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

/**
 *
 */
public enum ComponentType {
  JavaScript("js"),
  ActionScript("as"),
  EXML("exml");

  ComponentType(String extension) {
    this.extension = extension;
  }

  private String extension;

  public String getExtension() {
    return extension;
  }
}
