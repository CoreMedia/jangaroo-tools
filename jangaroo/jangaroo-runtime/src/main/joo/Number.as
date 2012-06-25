/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {
/**
 * A data type representing an IEEE-754 double-precision floating-point number. You can manipulate primitive numeric values by using the methods and properties associated with the Number class. This class is identical to the JavaScript Number class.
 * <p>The properties of the Number class are static, which means you do not need an object to use them, so you do not need to use the constructor.</p>
 * <p>The Number data type adheres to the double-precision IEEE-754 standard.</p>
 * <p>The Number data type is useful when you need to use floating-point values. Flash runtimes handle int and uint data types more efficiently than Number, but Number is useful in situations where the range of values required exceeds the valid range of the int and uint data types. The Number class can be used to represent integer values well beyond the valid range of the int and uint data types. The Number data type can use up to 53 bits to represent integer values, compared to the 32 bits available to int and uint. The default value of a variable typed as Number is <code>NaN</code> (Not a Number).</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/./Number.html#includeExamplesSummary">View the examples</a></p>
 * @see int
 * @see uint
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f9c.html Data types
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f88.html Data type descriptions
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f87.html Type conversions
 *
 */
[Native]
public final class Number {
  /**
   * Creates a Number object with the specified value. This constructor has the same effect as the <code>Number()</code> public native function that converts an object of a different type to a primitive numeric value.
   * @param num The numeric value of the Number instance being created or a value to be converted to a Number. The default value is 0 if <code>num</code> is not specified. Using the constructor without specifying a <code>num</code> parameter is not the same as declaring a variable of type Number with no value assigned (such as <code>var myNumber:Number</code>), which defaults to <code>NaN</code>. A number with no value assigned is undefined and the equivalent of <code>new Number(undefined)</code>.
   *
   * @see #toString()
   * @see #valueOf()
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f9c.html Data types
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f88.html Data type descriptions
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f87.html Type conversions
   *
   */
  public native function Number(num:* = 0);

  /**
   * Returns a string representation of the number in exponential notation. The string contains one digit before the decimal point and up to 20 digits after the decimal point, as specified by the <code>fractionDigits</code> parameter.
   * @param fractionDigits An integer between 0 and 20, inclusive, that represents the desired number of decimal places.
   *
   * @return a string representation of the number in exponential notation.
   *
   * @throws RangeError Throws an exception if the <code>fractionDigits</code> argument is outside the range 0 to 20.
   *
   * @example The following example shows how <code>toExponential(2)</code> returns a string in exponential notation.
   * <listing>
   * var num:Number = 315003;
   * trace(num.toExponential(2)); // 3.15e+5
   * </listing>
   */
  public native function toExponential(fractionDigits:uint = 0):String;

  /**
   * Returns a string representation of the number in fixed-point notation. Fixed-point notation means that the string will contain a specific number of digits after the decimal point, as specified in the <code>fractionDigits</code> parameter. The valid range for the <code>fractionDigits</code> parameter is from 0 to 20. Specifying a value outside this range throws an exception.
   * @param fractionDigits An integer between 0 and 20, inclusive, that represents the desired number of decimal places.
   *
   * @return a string representation of the number in fixed-point notation.
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
  public native function toFixed(fractionDigits:uint = 0):String;

  /**
   * Returns a string representation of the number either in exponential notation or in fixed-point notation. The string will contain the number of digits specified in the <code>precision</code> parameter.
   * @param precision An integer between 1 and 21, inclusive, that represents the desired number of digits to represent in the resulting string.
   *
   * @return a string representation of the number either in exponential notation or in fixed-point notation.
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
  public native function toPrecision(precision:uint = 0):String;

  /**
   * Returns the string representation of the specified Number object (<code><i>myNumber</i></code>). If the value of the Number object is a decimal number without a leading zero (such as <code>.4</code>), <code>Number.toString()</code> adds a leading zero (<code>0.4</code>).
   * @param radix Specifies the numeric base (from 2 to 36) to use for the number-to-string conversion. If you do not specify the <code>radix</code> parameter, the default value is 10.
   *
   * @return The numeric representation of the Number object as a string.
   *
   */
  public native function toString(radix:Number = 10):String;

  /**
   * Returns the primitive value type of the specified Number object.
   * @return The primitive type value of the Number object.
   *
   */
  public native function valueOf():Number;

  /**
   * The largest representable number (double-precision IEEE-754). This number is approximately 1.79e+308.
   */
  public static const MAX_VALUE:Number = 1.7976931348623157e+308;

  /**
   * The smallest representable non-negative, non-zero, number (double-precision IEEE-754). This number is approximately 5e-324. The smallest representable number overall is actually <code>-Number.MAX_VALUE</code>.
   */
  public static const MIN_VALUE:Number = 5e-324;

  /**
   * The IEEE-754 value representing Not a Number (<code>NaN</code>).
   * @see #isNaN()
   *
   */
  public static const NaN:Number = NaN;

  /**
   * Specifies the IEEE-754 value representing negative infinity. The value of this property is the same as that of the constant <code>-Infinity</code>.
   * <p>Negative infinity is a special numeric value that is returned when a mathematical operation or function returns a negative value larger than can be represented.</p>
   */
  public static const NEGATIVE_INFINITY:Number = -Infinity;

  /**
   * Specifies the IEEE-754 value representing positive infinity. The value of this property is the same as that of the constant <code>Infinity</code>.
   * <p>Positive infinity is a special numeric value that is returned when a mathematical operation or function returns a value larger than can be represented.</p>
   */
  public static const POSITIVE_INFINITY:Number = Infinity;

}
}
