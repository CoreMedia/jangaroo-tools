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
  XML("xml");

  ComponentType(String extension) {
    this.extension = extension;
  }

  public String extension;
  
}
