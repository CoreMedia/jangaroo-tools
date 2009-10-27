/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml.json;

/**
 *
 */
public interface Json {
  Object get(String property);

  void set(String property, Object value);
}
