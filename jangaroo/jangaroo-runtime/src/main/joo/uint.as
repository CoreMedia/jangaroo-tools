package {

/**
 * The uint class provides methods for working with a data type representing a 32-bit unsigned integer. Because an unsigned integer can only be positive, its maximum value is twice that of the int class.
 * <p>The range of values represented by the uint class is 0 to 4,294,967,295 (2^32-1).</p>
 * <p>You can create a uint object by declaring a variable of type uint and assigning the variable a literal value. The default value of a variable of type uint is <code>0</code>.</p>
 * <p>The uint class is primarily useful for pixel color values (ARGB and RGBA) and other situations where the int data type does not work well. For example, the number 0xFFFFFFFF, which represents the color value white with an alpha value of 255, can't be represented using the int data type because it is not within the valid range of the int values.</p>
 * <p>The following example creates a uint object and calls the <code>toString()</code> method:</p>
 * <pre>
 * var myuint:uint = 1234;
 * trace(myuint.toString()); // 1234
 * </pre>
 * <p>The following example assigns the value of the <code>MIN_VALUE</code> property to a variable without the use of the constructor:</p>
 * <pre>
 * var smallest:uint = uint.MIN_VALUE;
 * trace(smallest.toString()); // 0
 * </pre>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/./uint.html#includeExamplesSummary">View the examples</a></p>
 * @see int
 * @see Number
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f9c.html Data types
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f88.html Data type descriptions
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f87.html Type conversions
 *
 */
[Native("AS3.$uint", require)]
public final class uint {
  /**
   * Creates a new uint object. You can create a variable of uint type and assign it a literal value. The <code>new uint()</code> constructor is primarily used as a placeholder. A uint object is not the same as the <code>uint()</code> function, which converts a parameter to a primitive value.
   * @param num The numeric value of the uint object being created, or a value to be converted to a number. If <code>num</code> is not provided, the default value is <code>0</code>.
   *
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f9c.html Data types
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f88.html Data type descriptions
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f87.html Type conversions
   *
   * @example <a href="http://www.adobe.com/go/learn_as3_usingexamples_en">How to use this example</a>The following code constructs two new uint objects; the first by assigning a literal value, and the second by using the constructor function:
   * <pre>
   * var n1:uint = 3;
   * var n2:uint = new uint(10);
   * </pre>
   */
  public native function uint(num:Object = 0);

  /**
   * Returns a string representation of the number in exponential notation. The string contains one digit before the decimal point and up to 20 digits after the decimal point, as specified by the <code>fractionDigits</code> parameter.
   * @param fractionDigits An integer between 0 and 20, inclusive, that represents the desired number of decimal places.
   *
   * @return <code><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/String.html">String</a></code>
   *
   * @throws RangeError Throws an exception if the <code>fractionDigits</code> argument is outside the range 0 to 20.
   *
   * @example The following example shows how <code>toExponential(2)</code> returns a string in exponential notation.
   * <listing>
   * var num:Number = 315003;
   * trace(num.toExponential(2)); // 3.15e+5
   *
   * </listing>
   */
  public native function toExponential(fractionDigits:uint):String;

  /**
   * Returns a string representation of the number in fixed-point notation. Fixed-point notation means that the string will contain a specific number of digits after the decimal point, as specified in the <code>fractionDigits</code> parameter. The valid range for the <code>fractionDigits</code> parameter is from 0 to 20. Specifying a value outside this range throws an exception.
   * @param fractionDigits An integer between 0 and 20, inclusive, that represents the desired number of decimal places.
   *
   * @return <code><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/String.html">String</a></code>
   *
   * @throws RangeError Throws an exception if the <code>fractionDigits</code> argument is outside the range 0 to 20.
   *
   * @example The following example shows how <code>toFixed(3)</code> returns a string that rounds to three decimal places.
   * <listing>
   * var num:Number = 7.31343;
   * trace(num.toFixed(3)); // 7.313
   * </listing>
   * <div>The following example shows how <code>toFixed(2)</code> returns a string that adds trailing zeroes.
   * <listing>
   * var num:Number = 4;
   * trace(num.toFixed(2)); // 4.00
   * </listing></div>
   */
  public native function toFixed(fractionDigits:uint):String;

  /**
   * Returns a string representation of the number either in exponential notation or in fixed-point notation. The string will contain the number of digits specified in the <code>precision</code> parameter.
   * @param precision An integer between 1 and 21, inclusive, that represents the desired number of digits to represent in the resulting string.
   *
   * @return <code><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/String.html">String</a></code>
   *
   * @throws RangeError Throws an exception if the <code>precision</code> argument is outside the range 1 to 21.
   *
   * @example The following example shows how <code>toPrecision(3)</code> returns a string with only three digits. The string is in fixed-point notation because exponential notation is not required.
   * <listing>
   * var num:Number = 31.570;
   * trace(num.toPrecision(3)); // 31.6
   * </listing>
   * <div>The following example shows how <code>toPrecision(3)</code> returns a string with only three digits. The string is in exponential notation because the resulting number does not contain enough digits for fixed-point notation.
   * <listing>
   * var num:Number = 4000;
   * trace(num.toPrecision(3)); // 4.00e+3
   * </listing></div>
   */
  public native function toPrecision(precision:uint):String;

  /**
   * Returns the string representation of a uint object.
   * @param radix Specifies the numeric base (from 2 to 36) to use for the number-to-string conversion. If you do not specify the <code>radix</code> parameter, the default value is <code>10</code>.
   *
   * @return The string representation of the uint object.
   *
   * @example <a href="http://www.adobe.com/go/learn_as3_usingexamples_en">How to use this example</a>The following example uses 2 and 8 for the <code>radix</code> parameters and returns a string value with the corresponding representation of the number 9:
   * <pre>
   * var myuint:uint = 9;
   * trace(myuint.toString(2)); // 1001
   * trace(myuint.toString(8)); // 11
   * </pre>The following example creates hexadecimal values:
   * <pre>
   * var r:uint = 250;
   * var g:uint = 128;
   * var b:uint = 114;
   * var rgb:String = "0x" + r.toString(16) + g.toString(16) + b.toString(16);
   * trace(rgb); // 0xfa8072
   * </pre>
   */
  public native function toString(radix:uint = 10):String;

  /**
   * Returns the primitive uint type value of the specified uint object.
   * @return The primitive uint type value of this uint object.
   *
   * @example <a href="http://www.adobe.com/go/learn_as3_usingexamples_en">How to use this example</a>The following example outputs the primitive value of the <code>numSocks</code> object.
   * <pre>
   * var numSocks:uint = 2;
   * trace(numSocks.valueOf()); // 2
   * </pre>
   */
  public native function valueOf():uint;

  /**
   * The largest representable 32-bit unsigned integer, which is 4,294,967,295.
   * @example <a href="http://www.adobe.com/go/learn_as3_usingexamples_en">How to use this example</a>The following ActionScript displays the largest and smallest representable <code>uint</code> values:
   * <pre>
   * trace("uint.MIN_VALUE = " + uint.MIN_VALUE);
   * trace("uint.MAX_VALUE = " + uint.MAX_VALUE);
   * </pre>
   * <p>The values are:</p>
   * <pre>
   * uint.MIN_VALUE = 0
   * uint.MAX_VALUE = 4294967295
   * </pre>
   */
  public static const MAX_VALUE:uint = 4294967295;
  /**
   * The smallest representable unsigned integer, which is <code>0</code>.
   * @example <a href="http://www.adobe.com/go/learn_as3_usingexamples_en">How to use this example</a>The following ActionScript displays the largest and smallest representable <code>uint</code> values:
   * <pre>
   * trace("uint.MIN_VALUE = " + uint.MIN_VALUE);
   * trace("uint.MAX_VALUE = " + uint.MAX_VALUE);
   * </pre>
   * <p>The values are:</p>
   * <pre>
   * uint.MIN_VALUE = 0
   * uint.MAX_VALUE = 4294967295
   * </pre>
   */
  public static const MIN_VALUE:uint = 0;
}
}
