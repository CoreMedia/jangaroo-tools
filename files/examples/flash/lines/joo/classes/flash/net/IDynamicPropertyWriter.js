joo.classLoader.prepare("package flash.net",/* {*/


/**
 * This interface is used with the IDynamicPropertyOutput interface to control the serialization of dynamic properties of dynamic objects. To use this interface, assign an object that implements the IDynamicPropertyWriter interface to the <code>ObjectEncoding.dynamicPropertyWriter</code> property.
 * @see IDynamicPropertyOutput
 * @see ObjectEncoding#dynamicPropertyWriter
 *
 */
"public interface IDynamicPropertyWriter",1,function($$private){;return[ /*
  /**
   * Writes the name and value of an IDynamicPropertyOutput object to an object with dynamic properties. If <code>ObjectEncoding.dynamicPropertyWriter</code> is set, this method is invoked for each object with dynamic properties.
   * @param obj The object to write to.
   * @param output The IDynamicPropertyOutput object that contains the name and value to dynamically write to the object.
   *
   * @see IDynamicPropertyOutput
   * @see ObjectEncoding#dynamicPropertyWriter
   *
   * /
  function writeDynamicProperties(obj:Object, output:IDynamicPropertyOutput):void;*/,
];},[],[], "0.8.0", "0.8.1"
);