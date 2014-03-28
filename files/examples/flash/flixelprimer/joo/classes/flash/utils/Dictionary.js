joo.classLoader.prepare("package flash.utils",/* {*/


/**
 * The Dictionary class lets you create a dynamic collection of properties, which uses strict equality (<code>===</code>) for key comparison. When an object is used as a key, the object's identity is used to look up the object, and not the value returned from calling <code>toString()</code> on it.
 * <p>The following statements show the relationship between a Dictionary object and a key object:</p>
 * <pre> var dict = new Dictionary();
 var obj = new Object();
 var key:Object = new Object();
 key.toString = function() { return "key" }

 dict[key] = "Letters";
 obj["key"] = "Letters";

 dict[key] == "Letters"; // true
 obj["key"] == "Letters"; // true
 obj[key] == "Letters"; // true because key == "key" is true b/c key.toString == "key"
 dict["key"] == "Letters"; // false because "key" === key is false
 delete dict[key]; //removes the key
 </pre>
 * <p><b>Note:</b> Objects of the type QName are not valid values for keys in Dictionary object instances. Using a QName data type for a key generates a Reference error.</p>
 * @see operators#strict_equality
 *
 */
"public dynamic class Dictionary",1,function($$private){var trace=joo.trace;return[ 
  /**
   * Creates a new Dictionary object. To remove a key from a Dictionary object, use the <code>delete</code> operator.
   * @param weakKeys Instructs the Dictionary object to use "weak" references on object keys. If the only reference to an object is in the specified Dictionary object, the key is eligible for garbage collection and is removed from the table when the object is collected.
   *
   */
  "public function Dictionary",function Dictionary$(weakKeys/*:Boolean = false*/) {if(arguments.length<1){weakKeys = false;}
    if (weakKeys) {
      trace("[WARN]", "Dictionary with weakKeys not supported by Jangaroo.");
    }
  },
];},[],[], "0.8.0", "0.8.3"
);