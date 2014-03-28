joo.classLoader.prepare("package flash.net",/* {*/


/**
 * The ObjectEncoding class is used in defining serialization settings in classes that serialize objects (such as FileStream, NetStream, NetConnection, SharedObject, and ByteArray) to work with prior versions of ActionScript.
 * <p>Object encoding controls how objects are represented in Action Message Format (AMF). Flash Player uses AMF to enable efficient communication between an application and a remote server. AMF encodes remote procedure calls into a compact binary representation that can be transferred over HTTP/HTTPS or the RTMP/RTMPS protocol used by Flash Media Server. Objects and data values are serialized into this binary format, which is generally more compact than other representations, such as XML.</p>
 * <p>Adobe AIR and Flash Player 9 can serialize in two different formats: AMF3 and AMF0. AMF3, the default serialization developed for ActionScript 3.0, provides various advantages over AMF0, which is used for ActionScript 1.0 and 2.0. AMF3 sends data over the network more efficiently than AMF0. AMF3 supports sending <code>int</code> and <code>uint</code> objects as integers and supports data types that are available only in ActionScript 3.0, such as ByteArray, XML, and IExternalizable. It is available only in ActionScript 3.0 and with servers that use AMF3 encoding, such as Flex 2.</p>
 * <p>The ByteArray, FileStream, NetConnection, NetStream, SharedObject, Socket, and URLStream classes contain an <code>objectEncoding</code> property that is assigned a constant from the ObjectEncoding class. The behavior of the <code>objectEncoding</code> property differs depending on the object; each class's <code>objectEncoding</code> property description explains the behavior more thoroughly.</p>
 */
"public final class ObjectEncoding",1,function($$private){;return[ 
  /**
   * Allows greater control over the serialization of dynamic properties of dynamic objects. When this property is set to <code>null</code>, the default value, dynamic properties are serialized using native code, which writes all dynamic properties excluding those whose value is a function.
   * <p>This value is called only for properties of a dynamic object (objects declared within a dynamic class) or for objects declared using the <code>new</code> operator.</p>
   * <p>You can use this property to exclude properties of dynamic objects from serialization; to write values to properties of dynamic objects; or to create new properties for dynamic objects. To do so, set this property to an object that implements the IDynamicPropertyWriter interface. For more information, see the IDynamicPropertyWriter interface.</p>
   * @see IDynamicPropertyWriter
   *
   */
  "public static function get dynamicPropertyWriter",function dynamicPropertyWriter$get()/*:IDynamicPropertyWriter*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public static function set dynamicPropertyWriter",function dynamicPropertyWriter$set(value/*:IDynamicPropertyWriter*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies that objects are serialized using the Action Message Format for ActionScript 1.0 and 2.0.
   */
  "public static const",{ AMF0/*:uint*/ : 0},
  /**
   * Specifies that objects are serialized using the Action Message Format for ActionScript 3.0.
   */
  "public static const",{ AMF3/*:uint*/ : 3},
  /**
   * Specifies the default (latest) format for the current runtime (either Flash Player or AIR). Because object encoding control is only available in Flash Player 9 and later and Adobe AIR, the earliest format used will be the Action Message Format for ActionScript 3.0.
   * <p>For example, if an object has the <code>objectEncoding</code> property set to <code>ObjectEncoding.DEFAULT</code>, AMF3 encoding is used. If, in the future, a later version of Flash Player or Adobe AIR introduces a new AMF version and you republish your content, the application will use that new AMF version. You can use this constant only if you're not concerned at all about interoperability with previous versions.</p>
   */
  "public static const",{ DEFAULT/*:uint*/ : 3},
];},["dynamicPropertyWriter"],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);