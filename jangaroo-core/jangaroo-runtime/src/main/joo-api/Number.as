/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {
/**
 * A data type representing an IEEE-754 double-precision floating-point number.
 * You can manipulate primitive numeric values by using the methods and properties associated with the Number class.
 * This class is identical to the JavaScript Number class.
 * <p>The properties of the Number class are static, which means you do not need an object to use them,
 * so you do not need to use the constructor.
 * <p>The Number data type adheres to the double-precision IEEE-754 standard.
 * <p>The Number data type is useful when you need to use floating-point values.
 * Flash Player handles int and uint data types more efficiently than Number, but Number is useful in situations
 * where the range of values required exceeds the valid range of the int and uint data types. The Number class can be
 * used to represent integer values well beyond the valid range of the int and uint data types. The Number data type
 * can use up to 53 bits to represent integer values, compared to the 32 bits available to int and uint. The default
 * value of a variable typed as Number is NaN (Not a Number).
 *
 * @see int
 * @see uint
 */
public final class Number extends Object {

  /**
   * Specifies the IEEE-754 value representing positive infinity.
   * The value of this property is the same as that of the constant Infinity.
   * <p>Positive infinity is a special numeric value that is returned when a
   * mathematical operation or function returns a value larger than can be
   * represented. 
   */
  public static const POSITIVE_INFINITY : Number = Infinity;
  /**
   * Specifies the IEEE-754 value representing negative infinity.
   * The value of this property is the same as that of the constant -Infinity.
   * <p>Negative infinity is a special numeric value that is returned when a
   * mathematical operation or function returns a negative value larger than
   * can be represented. 
   */
  public static const NEGATIVE_INFINITY : Number = -Infinity;

  /**
   * The largest representable number (double-precision IEEE-754).
   * This number is approximately 1.79e+308. 
   */
  public static native function get MAX_VALUE() : Number;

  /**
   * The smallest representable non-negative, non-zero, number (double-precision IEEE-754).
   * This number is approximately 5e-324.
   * The smallest representable number overall is actually -Number.MAX_VALUE.
   */
  public static native function get MIN_VALUE() : Number;

  /**
   * The IEEE-754 value representing Not a Number (NaN).
   * @see #isNaN()
   */
  public static native function get NaN() : Number;

  /**
   * Returns a string representation of the number in exponential notation.
   * The string contains one digit before the decimal point and up to 20 digits after the decimal point,
   * as specified by the fractionDigits parameter.
   * @throws RangeError Throws an exception if the fractionDigits argument is outside the range 0 to 20.
   * @example
   * The following example shows how toExponential(2) returns a string in exponential notation.
   * <pre>
   * var num:Number = 315003;
   * trace(num.toExponential(2)); // 3.15e+5
   * </pre>
   * @param fractionalDigits An integer between 0 and 20, inclusive, that represents the desired number of decimal places.
   * @return String a string representation of the number in exponential notation.
   */
  public native function toExponential(fractionalDigits : uint) : String;

  /**
   * Returns a string representation of the number in fixed-point notation.
   * Fixed-point notation means that the string will contain a specific number of digits after the decimal point,
   * as specified in the fractionDigits parameter.
   * The valid range for the fractionDigits parameter is from 0 to 20.
   * Specifying a value outside this range throws an exception.
   * @throws RangeError Throws an exception if the fractionDigits argument is outside the range 0 to 20.
   * @example
   * The following example shows how toFixed(3) returns a string that rounds to three decimal places.
   * <pre>
   * var num:Number = 7.31343;
   * trace(num.toFixed(3)); // 7.313
   * </pre>
   * The following example shows how toFixed(2) returns a string that adds trailing zeroes.
   * <pre>
   * var num:Number = 4;
   * trace(num.toFixed(2)); // 4.00
   * </pre>
   * @param fractionalDigits An integer between 0 and 20, inclusive, that represents the desired number of decimal places.
   * @return String a string representation of the number in fixed-point notation.
   */
  public native function toFixed(fractionalDigits : uint) : String;

  /**
   * Returns a string representation of the number either in exponential notation or in fixed-point notation.
   * The string will contain the number of digits specified in the precision parameter.
   * @throws RangeError Throws an exception if the precision argument is outside the range 1 to 21.
   * @example
   * The following example shows how toPrecision(3) returns a string with only three digits.
   * The string is in fixed-point notation because exponential notation is not required.
   * <pre>
   * var num:Number = 31.570;
   * trace(num.toPrecision(3)); // 31.6
   * </pre>
   * The following example shows how toPrecision(3) returns a string with only three digits.
   * The string is in exponential notation because the resulting number does not contain enough digits for fixed-point notation.
   * <pre>
   * var num:Number = 4000;
   * trace(num.toPrecision(3)); // 4.00e+3
   * </pre>
   * @param precision An integer between 1 and 21, inclusive, that represents the desired number of digits to represent in the resulting string.
   * @return String a string representation of the number either in exponential notation or in fixed-point notation.
   */
  public native function toPrecision(precision : uint) : String;


  /**
   * Returns the primitive value type of the specified Number object.
   * @return Number The primitive type value of the Number object.
   */
  public native function valueOf() : Number;

  /**
   * Returns the string representation of the specified Number object (myNumber).
   * If the value of the Number object is a decimal number without a leading zero (such as .4),
   * Number.toString() adds a leading zero (0.4).
   * @param radix Specifies the numeric base (from 2 to 36) to use for the number-to-string conversion.
   * If you do not specify the radix parameter, the default value is 10.
   * @return String The numeric representation of the Number object as a string.
   */
  public native function toString(radix : Number = 10) : String;

}

}