package joo {

/**
 * This is a marker class indicating that any subclass is intended to be used as a typed JSON object, i.e.
 * no enumerable "meta" properties may be defined by the Jangaroo Runtime.
 */
public dynamic class JavaScriptObject {

  /**
   * Create a typed JavaScript object from the given JSON object.
   * @param config the JSON object to copy into this JavaScriptObject. If no JSON object is given, this JavaScriptObject
   *   is initially empty.
   */
  public native function JavaScriptObject(config:Object = null);
}
}
