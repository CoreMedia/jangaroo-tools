/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {


/**
 * A function is the basic unit of code that can be invoked in ActionScript. Both user-defined and built-in functions in ActionScript are represented by Function objects, which are instances of the Function class.
 * <p>Methods of a class are slightly different than Function objects. Unlike an ordinary function object, a method is tightly linked to its associated class object. Therefore, a method or property has a definition that is shared among all instances of the same class. Methods can be extracted from an instance and treated as "bound" methods (retaining the link to the original instance). For a bound method, the <code>this</code> keyword points to the original object that implemented the method. For a function, <code>this</code> points to the associated object at the time the function is invoked.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/./Function.html#includeExamplesSummary">View the examples</a></p>
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f36.html Classes
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f30.html Methods
 *
 */
[Native]
public final class Function {
  /**
   * Specifies the value of <code>thisObject</code> to be used within any function that ActionScript calls. This method also specifies the parameters to be passed to any called function. Because <code>apply()</code> is a method of the Function class, it is also a method of every Function object in ActionScript.
   * <p>The parameters are specified as an Array object, unlike <code>Function.call()</code>, which specifies parameters as a comma-delimited list. This is often useful when the number of parameters to be passed is not known until the script actually executes.</p>
   * <p>Returns the value that the called function specifies as the return value.</p>
   * @param thisArg The object to which the function is applied.
   * @param argArray An array whose elements are passed to the function as parameters.
   *
   * @return Any value that the called function specifies.
   *
   * @see #call()
   *
   */
  public native function apply(thisArg:* = NaN, argArray:* = NaN):*;

  /**
   * Invokes the function represented by a Function object. Every function in ActionScript is represented by a Function object, so all functions support this method.
   * <p>In almost all cases, the function call (<code>()</code>) operator can be used instead of this method. The function call operator produces code that is concise and readable. This method is primarily useful when the <code>thisObject</code> parameter of the function invocation needs to be explicitly controlled. Normally, if a function is invoked as a method of an object within the body of the function, <code>thisObject</code> is set to <code>myObject</code>, as shown in the following example:</p>
   * <listing>
   *   myObject.myMethod(1, 2, 3);
   *  </listing>
   * <p>In some situations, you might want <code>thisObject</code> to point somewhere else; for example, if a function must be invoked as a method of an object, but is not actually stored as a method of that object:</p>
   * <listing>
   *   myObject.myMethod.call(myOtherObject, 1, 2, 3);
   *  </listing>
   * <p>You can pass the value <code>null</code> for the <code>thisObject</code> parameter to invoke a function as a regular function and not as a method of an object. For example, the following function invocations are equivalent:</p>
   * <listing>
   *   Math.sin(Math.PI / 4)
   *   Math.sin.call(null, Math.PI / 4)
   *  </listing>
   * <p>Returns the value that the called function specifies as the return value.</p>
   * @param thisArg An object that specifies the value of <code>thisObject</code> within the function body.
   * @param args The parameter or parameters to be passed to the function. You can specify zero or more parameters.
   *
   * @return <code><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/specialTypes.html#*">*</a></code>
   *
   * @see #apply()
   *
   */
  public native function call(thisArg:* = NaN, ...args):Object;

  /**
   * A reference to the prototype object of a class or function object. The prototype property is automatically created
   * and attached to any class or function object that you create. If you create a class, the value of the prototype property is
   * shared by all instances of the class and is accessible only as a class property. Instances of your class cannot
   * directly access the prototype property
   * <p>A class's prototype object is a special instance of that class that provides a mechanism for sharing state
   * across all instances of a class. At run time, when a property is not found on a class instance, the delegate,
   * which is the class prototype object, is checked for that property. If the prototype object does not contain the
   * property, the process continues with the prototype object's delegate checking in consecutively higher levels in
   * the hierarchy until Flash Player or the Adobe Integrated Runtime finds the property.
   * <p>Note: In ActionScript 3.0, prototype inheritance is not the primary mechanism for inheritance. Class
   * inheritance, which drives the inheritance of fixed properties in class definitions, is the primary inheritance
   * mechanism in ActionScript 3.0.
   * @return Object A reference to the prototype object of a class or function object.
   */
  public native function get prototype():Object;

  /**
   * @private
   */
  public native function set prototype(o:Object):void;

  /**
   * @private
   */
  public native function Function(name:String = null, body:String = null);

  /**
   * @private
   */
  public native function get arguments():Arguments;

  /**
   * @private
   */
  public native function get arity():Number;

}
}
