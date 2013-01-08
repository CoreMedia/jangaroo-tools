/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.jooc.json;

/**
 *
 */
public interface Json {
  Object get(String property);

  void set(String property, Object value);

  /**
   * Make a prettyprinted JSON text of this JSONObject.
   * <p/>
   * Warning: This method assumes that the data structure is acyclical.
   *
   * @param indentFactor The number of spaces to add to each level of
   *                     indentation.
   * @param indent       The indentation of the top level.
   * @return a printable, displayable, transmittable
   *         representation of the object, beginning
   *         with <code>{</code>&nbsp;<small>(left brace)</small> and ending
   *         with <code>}</code>&nbsp;<small>(right brace)</small>.
   */
  String toString(int indentFactor, int indent);
}
