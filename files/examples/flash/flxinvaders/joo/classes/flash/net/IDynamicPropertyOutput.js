joo.classLoader.prepare("package flash.net",/* {*/


/**
 * This interface controls the serialization of dynamic properties of dynamic objects. You use this interface with the IDynamicPropertyWriter interface and the <code>ObjectEncoding.dynamicPropertyWriter</code> property.
 * @see IDynamicPropertyWriter
 * @see ObjectEncoding#dynamicPropertyWriter
 *
 */
"public interface IDynamicPropertyOutput",1,function($$private){;return[ /*
  /**
   * Adds a dynamic property to the binary output of a serialized object. When the object is subsequently read (using a method such as <code>readObject</code>), it contains the new property. You can use this method to exclude properties of dynamic objects from serialization; to write values to properties of dynamic objects; or to create new properties for dynamic objects.
   * @param name The name of the property. You can use this parameter either to specify the name of an existing property of the dynamic object or to create a new property.
   * @param value The value to write to the specified property.
   *
   * @see IDynamicPropertyWriter
   * @see ObjectEncoding#dynamicPropertyWriter
   *
   * /
  function writeDynamicProperty(name:String, value:*):void;*/,
];},[],[], "0.8.0", "0.8.1"
);