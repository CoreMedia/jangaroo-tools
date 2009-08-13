/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package{

/**
 * A function is the basic unit of code that can be invoked in ActionScript.
 * Both user-defined and built-in functions in ActionScript are represented by Function objects, which are instances
 * of the Function class.
 * <p>Methods of a class are slightly different than Function objects. Unlike an ordinary function object, a method is
 * tightly linked to its associated class object. Therefore, a method or property has a definition that is shared among
 * all instances of the same class. Methods can be extracted from an instance and treated as "bound" methods (retaining
 * the link to the original instance). For a bound method, the this keyword points to the original object that
 * implemented the method. For a function, this points to the associated object at the time the function is invoked.
 */
public class Function extends Object {

  public native function Function(name : String = null, body : String = null);



  public native function get arguments():Arguments;



  public native function get arity():Number;


  /**
   * Specifies the value of thisObject to be used within any function that ActionScript calls.
   * This method also specifies the parameters to be passed to any called function.
   * Because apply() is a method of the Function class, it is also a method of every Function object in ActionScript.
   * <p>The parameters are specified as an Array object, unlike Function.call(), which specifies parameters as a
   * comma-delimited list. This is often useful when the number of parameters to be passed is not known until the
   * script actually executes.
   * 
   * @param thisArg The object to which the function is applied.
   * @param argArray An array whose elements are passed to the function as parameters.
   * @return * Any value that the called function specifies.
   * @see #call()
   */
  public native function apply(thisArg : * = NaN, argArray : * = NaN):*;


  /**
   * Invokes the function represented by a Function object.
   * Every function in ActionScript is represented by a Function object, so all functions support this method.
   * <p>In almost all cases, the function call (()) operator can be used instead of this method. The function call
   * operator produces code that is concise and readable. This method is primarily useful when the thisObject parameter
   * of the function invocation needs to be explicitly controlled. Normally, if a function is invoked as a method of an
   * object within the body of the function, thisObject is set to myObject, as shown in the following example:
   * <pre>
   * myObject.myMethod(1, 2, 3);
   * </pre>
   * In some situations, you might want thisObject to point somewhere else; for example, if a function must be invoked
   * as a method of an object, but is not actually stored as a method of that object:
   * <pre>
   * myObject.myMethod.call(myOtherObject, 1, 2, 3);
   * </pre>
   * You can pass the value null for the thisObject parameter to invoke a function as a regular function and not as a
   * method of an object. For example, the following function invocations are equivalent:
   * <pre>
   * Math.sin(Math.PI / 4)
   * Math.sin.call(null, Math.PI / 4)
   * </pre>
   * Returns the value that the called function specifies as the return value.
   * @param thisArg An object that specifies the value of thisObject within the function body.
   * @param args The parameter or parameters to be passed to the function. You can specify zero or more parameters.
   * @return * the value that the called function specifies as the return value.
   * @see #apply()
   */
  public native function call(thisArg : * = NaN, ... args):Object;



  public native function bind(thisArg:Object):Function;



}

}

class Arguments extends Object {

  public native function get length():uint;



  public native function get callee():Function;



  public native function get caller():Function;

}

