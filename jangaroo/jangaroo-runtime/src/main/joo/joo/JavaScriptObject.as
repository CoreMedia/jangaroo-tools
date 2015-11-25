package joo {

/**
 * This is a marker class indicating that any subclass is intended to be used as a typed JSON object, i.e.
 * no enumerable "meta" properties may be defined by the Jangaroo Runtime.
 */
[Native]
public dynamic class JavaScriptObject {

  /**
   * Create a typed JavaScript object from the given JSON object.
   * @param config the JSON object to copy into this JavaScriptObject. If no JSON object is given, this JavaScriptObject
   *   is initially empty.
   */
  public native function JavaScriptObject(config:Object = null);

  /**
   * This element contains additional config objects that are mixed into
   * (merged with) this config object.
   * <br/>
   * It is neither recommended to define the same property in the base config object
   * as well as in a mixin nor to define the same property in more than one mixin.
   * However, when a property is defined multiple times, the base config object has
   * precedence over mixins, and a mixin has precedence over following mixins.
   * <br/>
   * It is recommended to use a typed mixin config object in favor of using
   * dynamic attributes.
   */
  public native function set mixins(mixins:Object):void;
}
}
