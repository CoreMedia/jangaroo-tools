/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * A special value that applies to untyped variables that have not been initialized or dynamic object properties that are not initialized. In ActionScript 3.0, only variables that are untyped can hold the value <code>undefined</code>, which is not true in ActionScript 1.0 and ActionScript 2.0. For example, both of the following variables are <code>undefined</code> because they are untyped and unitialized:
 * <ul>
 * <li><code>var foo;</code></li>
 * <li><code>var bar:*;</code></li></ul>
 * <p>The <code>undefined</code> value also applies to uninitialized or undefined properties of dynamic objects. For example, if an object is an instance of the Object class, the value of any dynamically added property is <code>undefined</code> until a value is assigned to that property.</p>
 * <p>Results vary when <code>undefined</code> is used with various functions:</p>
 * <ul>
 * <li>The value returned by <code>String(undefined)</code> is <code>"undefined"</code> (<code>undefined</code> is converted to a string).</li>
 * <li>The value returned by <code>Number(undefined)</code> is <code>NaN</code>.</li>
 * <li>The value returned by <code>int(undefined)</code> and <code>uint(undefined)</code> is 0.</li>
 * <li>The value returned by <code>Object(undefined)</code> is a new Object instance.</li>
 * <li>When the value <code>undefined</code> is assigned to a typed variable, the value is converted to the default value of the data type.</li></ul>
 * <p>Do not confuse <code>undefined</code> with <code>null</code>. When <code>null</code> and <code>undefined</code> are compared with the equality (<code>==</code>) operator, they compare as equal. However, when <code>null</code> and <code>undefined</code> are compared with the strict equality (<code>===</code>) operator, they compare as not equal.</p>
 * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/statements.html#null null
 *
 * @example In the following example, an untyped variable, <code>myVar</code> is declared but not initialized. The value of <code>myVar</code> is <code>undefined</code> because the variable is untyped. This is true whether the variable has no type annotation or uses the special (*) untyped annotation (<code>var myVar:*;</code>).
 * <listing>
 * // trace value of untyped and uninitialized variable
 * var myVar;
 * trace(myVar); // undefined
 * </listing>
 * <div>The same rule applies to uninitialized properties of a dynamic object. For example, given an instance, <code>obj</code>, of the dynamic class <code>A</code>, the value of <code>obj.propName</code>, which is an uninitialized property of the <code>obj</code> instance, is <code>undefined</code>.
 * <listing>
 * dynamic class A {}
 * var obj:A = new A()
 *
 * // trace undefined property of obj
 * trace(obj.propName); // undefined
 * </listing></div>
 */
[Native]
public const undefined: *;

}
