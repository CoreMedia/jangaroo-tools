joo.classLoader.prepare("package flash.utils",/* {*/


/**
 * The Proxy class lets you override the default behavior of ActionScript operations (such as retrieving and modifying properties) on an object.
 * <p>The Proxy class has no constructor, and you should not attempt to instantiate Proxy. Instead, subclass the Proxy class to override methods such as <code>getProperty</code> and provide custom behavior. If you try to use a method of the Proxy class without overriding the method, an exception is thrown.</p>
 * <p>And, keep in mind, your own code overriding the methods of the Proxy class can throw exceptions unintentionally. Throwing exceptions when using these methods causes problems because the calling code (using operators like <code>in</code>, <code>is</code>, <code>delete</code> and others) does not expect exceptions. Unless you're already sure your overriding method does not throw exceptions, Adobe recommends using <code>try..catch</code> statements around your implementation of the Proxy class to avoid fatal errors when operators call your methods. For example:</p>
 * <listing>
 *  dynamic class MyProxy extends Proxy {
 *      flash_proxy override function callProperty(name:*, ...rest):* {
 *        try {
 *          // custom code here
 *        }
 *        catch (e:Error) {
 *          // respond to error here
 *        }
 *  }
 * </listing>
 * <p>The Proxy class is a replacement for the <code>Object.__resolve</code> and <code>Object.addProperty</code> features of ActionScript 2.0, which are no longer available in ActionScript 3.0. The <code>Object.addProperty()</code> feature allowed you to dynamically create get and set methods in ActionScript 2.0. Although ActionScript 3.0 provides get and set methods at compile time, you cannot dynamically assign one to an object unless you use the Proxy class.</p>
 * <p>To avoid collisions with the <code>public</code> namespace, the methods of the Proxy class are in the <code>flash_proxy</code> namespace.</p>
 * <p>Where methods of the Proxy class take a <code>name</code> argument, <code>name</code> can be either a String or a QName object (if namespaces are being used).</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/utils/Proxy.html#includeExamplesSummary">View the examples</a></p>
 */
"public class Proxy",1,function($$private){;return[ 
  /**
   * Overrides the behavior of an object property that can be called as a function. When a method of the object is invoked, this method is called. While some objects can be called as functions, some object properties can also be called as functions.
   * @param name The name of the method being invoked.
   * @param rest An array specifying the arguments to the called method.
   *
   * @return The return value of the called method.
   *
   * @see Function#call()
   * @see http://www.ecma-international.org/publications/standards/Ecma-262.htm ECMA-262 Language Specification, 3rd Edition, section 15
   *
   */
  "flash_proxy function callProperty",function callProperty(name/*:*, ...rest*/)/*:**/ {var rest=Array.prototype.slice.call(arguments,1);
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Overrides the request to delete a property. When a property is deleted with the <code>delete</code> operator, this method is called to perform the deletion.
   * @param name The name of the property to delete.
   *
   * @return If the property was deleted, <code>true</code>; otherwise <code>false</code>.
   *
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#delete
   * @see http://www.ecma-international.org/publications/standards/Ecma-262.htm ECMA-262 Language Specification, 3rd Edition, 8.6.2.5
   *
   */
  "flash_proxy function deleteProperty",function deleteProperty(name/*:**/)/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Overrides the use of the <code>descendant</code> operator. When the <code>descendant</code> operator is used, this method is invoked.
   * @param name The name of the property to descend into the object and search for.
   *
   * @return The results of the <code>descendant</code> operator.
   *
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#descendant_accessor
   * @see http://www.ecma-international.org/publications/standards/Ecma-357.htm E4X Specification
   *
   */
  "flash_proxy function getDescendants",function getDescendants(name/*:**/)/*:**/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Overrides any request for a property's value. If the property can't be found, the method returns <code>undefined</code>. For more information on this behavior, see the ECMA-262 Language Specification, 3rd Edition, section 8.6.2.1.
   * @param name The name of the property to retrieve.
   *
   * @return The specified property or <code>undefined</code> if the property is not found.
   *
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/statements.html#get get statement
   * @see http://www.ecma-international.org/publications/standards/Ecma-262.htm ECMA-262 Language Specification, 3rd Edition, section 8.6.2.1
   *
   */
  "flash_proxy function getProperty",function getProperty(name/*:**/)/*:**/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Overrides a request to check whether an object has a particular property by name.
   * @param name The name of the property to check for.
   *
   * @return If the property exists, <code>true</code>; otherwise <code>false</code>.
   *
   * @see Object#hasOwnProperty()
   * @see http://www.ecma-international.org/publications/standards/Ecma-262.htm ECMA-262 Language Specification, 3rd Edition, section 8.6.2.4
   *
   */
  "flash_proxy function hasProperty",function hasProperty(name/*:**/)/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Checks whether a supplied QName is also marked as an attribute.
   * @param name The name of the property to check.
   *
   * @return Returns <code>true</code> if the argument for <code>name</code> is a QName that is also marked as an attribute.
   *
   * @see QName
   *
   */
  "flash_proxy function isAttribute",function isAttribute(name/*:**/)/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Allows enumeration of the proxied object's properties by index number to retrieve property names. However, you cannot enumerate the properties of the Proxy class themselves. This function supports implementing <code>for...in</code> and <code>for each..in</code> loops on the object to retrieve the desired names.
   * <p>For example (with code from <code>Proxy.nextNameIndex()</code>):</p>
   * <listing>
   *      protected var _item:Array; // array of object's properties
   *      override flash_proxy function nextNameIndex (index:int):int {
   *          // initial call
   *          if (index == 0) {
   *              _item = new Array();
   *              for (var x:* in _target) {
   *                 _item.push(x);
   *              }
   *          }
   *
   *          if (index < _item.length) {
   *              return index + 1;
   *          } else {
   *              return 0;
   *          }
   *      }
   *      override flash_proxy function nextName(index:int):String {
   *          return _item[index - 1];
   *      }
   *     </listing>
   * @param index The zero-based index value of the object's property.
   *
   * @return The property's name.
   *
   * @see #nextNameIndex()
   * @see #nextValue()
   *
   */
  "flash_proxy function nextName",function nextName(index/*:int*/)/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Allows enumeration of the proxied object's properties by index number. However, you cannot enumerate the properties of the Proxy class themselves. This function supports implementing <code>for...in</code> and <code>for each..in</code> loops on the object to retrieve property index values.
   * <p>For example:</p>
   * <listing>
   *      protected var _item:Array; // array of object's properties
   *      override flash_proxy function nextNameIndex (index:int):int {
   *          // initial call
   *          if (index == 0) {
   *              _item = new Array();
   *              for (var x:* in _target) {
   *                 _item.push(x);
   *              }
   *          }
   *
   *          if (index < _item.length) {
   *              return index + 1;
   *          } else {
   *              return 0;
   *          }
   *      }
   *      override flash_proxy function nextName(index:int):String {
   *          return _item[index - 1];
   *      }
   *     </listing>
   * @param index The zero-based index value where the enumeration begins.
   *
   * @return The property's index value.
   *
   * @see #nextName()
   * @see #nextValue()
   *
   */
  "flash_proxy function nextNameIndex",function nextNameIndex(index/*:int*/)/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Allows enumeration of the proxied object's properties by index number to retrieve property values. However, you cannot enumerate the properties of the Proxy class themselves. This function supports implementing <code>for...in</code> and <code>for each..in</code> loops on the object to retrieve the desired values.
   * <p>For example (with code from <code>Proxy.nextNameIndex()</code>):</p>
   * <listing>
   *      protected var _item:Array; // array of object's properties
   *      override flash_proxy function nextNameIndex (index:int):int {
   *          // initial call
   *          if (index == 0) {
   *              _item = new Array();
   *              for (var x:* in _target) {
   *                 _item.push(x);
   *              }
   *          }
   *
   *          if (index < _item.length) {
   *              return index + 1;
   *          } else {
   *              return 0;
   *          }
   *      }
   *      override flash_proxy function nextName(index:int):String {
   *          return _item[index - 1];
   *      }
   *     </listing>
   * @param index The zero-based index value of the object's property.
   *
   * @return The property's value.
   *
   * @see #nextNameIndex()
   * @see #nextName()
   *
   */
  "flash_proxy function nextValue",function nextValue(index/*:int*/)/*:**/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Overrides a call to change a property's value. If the property can't be found, this method creates a property with the specified name and value.
   * @param name The name of the property to modify.
   * @param value The value to set the property to.
   *
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/statements.html#set set statement
   * @see http://www.ecma-international.org/publications/standards/Ecma-262.htm ECMA-262 Language Specification, 3rd Edition, section 8.6.2.2
   *
   */
  "flash_proxy function setProperty",function setProperty(name/*:**/, value/*:**/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["Error"], "0.8.0", "0.8.1"
);