/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * A special value that applies to untyped variables that have not been initialized or dynamic object properties that
 * are not initialized. In ActionScript 3.0, only variables that are untyped can hold the value undefined, which is not
 * true in ActionScript 1.0 and ActionScript 2.0. For example, both of the following variables are undefined because
 * they are untyped and unitialized:
 * <pre>
 * var foo;
 * var bar:*;
 * </pre>
 * The undefined value also applies to uninitialized or undefined properties of dynamic objects. For example, if an
 * object is an instance of the Object class, the value of any dynamically added property is undefined until a value
 * is assigned to that property.
 * <p>Results vary when undefined is used with various functions:
 * <ul>
 * <li>The value returned by String(undefined) is "undefined" (undefined is converted to a string).
 * <li>The value returned by Number(undefined) is NaN.
 * <li>The value returned by int(undefined) and uint(undefined) is 0.
 * <li>The value returned by Object(undefined) is a new Object instance.
 * <li>When the value undefined is assigned to a typed variable, the value is converted to the default value of the
 * data type.
 * </ul>
 * Do not confuse undefined with null. When null and undefined are compared with the equality (==) operator, they
 * compare as equal. However, when null and undefined are compared with the strict equality (===) operator, they
 * compare as not equal.
 * @example
 * In the following example, an untyped variable, myVar is declared but not initialized. The value of myVar is
 * undefined because the variable is untyped. This is true whether the variable has no type annotation or uses the
 * special (*) untyped annotation (var myVar:*;).
 * <pre>
 * // trace value of untyped and uninitialized variable
 * var myVar;
 * trace(myVar); // undefined
 * </pre>
 * The same rule applies to uninitialized properties of a dynamic object. For example, given an instance, obj, of the
 * dynamic class A, the value of obj.propName, which is an uninitialized property of the obj instance, is undefined.
 * <pre>
 * dynamic class A {}
 * var obj:A = new A()
 * 
 * // trace undefined property of obj
 * trace(obj.propName); // undefined
 * </pre>
 */
public const undefined: *;

}