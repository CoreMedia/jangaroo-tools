/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * The Math class contains methods and constants that represent common mathematical functions and values.
 * <p>Use the methods and properties of this class to access and manipulate mathematical constants and functions.
 * All the properties and methods of the Math class are static and must be called using the syntax
 * Math.method( parameter ) or Math.constant. In ActionScript, constants are defined with the maximum precision of
 * double-precision IEEE-754 floating-point numbers.
 * <p>Several Math class methods use the measure of an angle in radians as a parameter. You can use the following
 * equation to calculate radian values before calling the method and then provide the calculated value as the
 * parameter, or you can provide the entire right side of the equation (with the angle's measure in degrees in place of
 * degrees) as the radian parameter.
 * <p>To calculate a radian value, use the following formula:
 * <pre>
 * radians = degrees * Math.PI/180
 * </pre>
 * To calculate degrees from radians, use the following formula:
 * <pre>
 * degrees = radians * 180/Math.PI
 * </pre>
 * The following is an example of passing the equation as a parameter to calculate the sine of a 45° angle:
 * <pre>
 * Math.sin(45 * Math.PI/180) is the same as Math.sin(.7854)
 * </pre>
 * Note: The Math functions acos, asin, atan, atan2, cos, exp, log, pow, sin, and sqrt may result in slightly different
 * values depending on the algorithms used by the CPU or operating system. Flash Player calls on the CPU (or operating
 * system if the CPU doesn't support floating point calculations) when performing the calculations for the listed
 * functions, and results have shown slight variations depending upon the CPU or operating system in use.
 */
public class Math extends Object {

  /**
   * A mathematical constant for the base of natural logarithms, expressed as e. The approximate value of e is
   * 2.71828182845905.
   */
  public native static function get E() : Number;

  /**
   * A mathematical constant for the natural logarithm of 10, expressed as log10, with an approximate value of
   * 2.302585092994046.
   */
  public native static function get LN10() : Number;

  /**
   * A mathematical constant for the natural logarithm of 2, expressed as log2, with an approximate value of
   * 0.6931471805599453.
   */
  public native static function get LN2() : Number;

  /**
   * A mathematical constant for the base-10 logarithm of the constant e (Math.E), expressed as loge, with an
   * approximate value of 0.4342944819032518.
   * <p>The Math.log() method computes the natural logarithm of a number. Multiply the result of Math.log() by
   * Math.LOG10E to obtain the base-10 logarithm.
   */
  public native static function get LOG10E() : Number;

  /**
   * A mathematical constant for the base-2 logarithm of the constant e, expressed as log2e, with an approximate value
   * of 1.442695040888963387.
   * <p>The Math.log method computes the natural logarithm of a number. Multiply the result of Math.log() by Math.LOG2E
   * to obtain the base-2 logarithm.
   */
  public native static function get LOG2E() : Number;

  /**
   * A mathematical constant for the ratio of the circumference of a circle to its diameter, expressed as pi, with a
   * value of 3.141592653589793.
   */
  public native static function get PI() : Number;

  /**
   * A mathematical constant for the square root of one-half, with an approximate value of 0.7071067811865476.
   */
  public native static function get SQRT1_2() : Number;

  /**
   * A mathematical constant for the square root of 2, with an approximate value of 1.4142135623730951.
   */
  public native static function get SQRT2() : Number;

  /**
   * Computes and returns an absolute value for the number specified by the parameter val.
   * @param val The number whose absolute value is returned.
   * @return Number The absolute value of the specified paramater.
   */
  public native static function abs(val : Number) : Number;

  /**
   * Computes and returns the arc cosine of the number specified in the parameter val, in radians.
   * @param val  A number from -1.0 to 1.0.
   * @return Number The arc cosine of the parameter val.
   */
  public native static function acos(val : Number) : Number;

  /**
   * Computes and returns the arc sine for the number specified in the parameter val, in radians.
   * @param val A number from -1.0 to 1.0.
   * @return Number A number between negative pi divided by 2 and positive pi divided by 2.
   */
  public native static function asin(val : Number) : Number;

  /**
   * Computes and returns the value, in radians, of the angle whose tangent is specified in the parameter val.
   * The return value is between negative pi divided by 2 and positive pi divided by 2.
   * @param val A number that represents the tangent of an angle.
   * @return Number A number between negative pi divided by 2 and positive pi divided by 2.
   */
  public native static function atan(val : Number) : Number;

  /**
   * Computes and returns the angle of the point y/x in radians, when measured counterclockwise from a circle's x axis
   * (where 0,0 represents the center of the circle). The return value is between positive pi and negative pi.
   * Note that the first parameter to atan2 is always the y coordinate.
   * @param y The y coordinate of the point.
   * @param x The x coordinate of the point.
   * @return Number A number
   * @see Math.acos()
   * @see Math.asin()
   * @see Math.atan()
   * @see Math.cos()
   * @see Math.sin()
   * @see Math.tan()
   */
  public native static function atan2(y : Number, x : Number) : Number;

  /**
   * Returns the ceiling of the specified number or expression. The ceiling of a number is the closest integer that is
   * greater than or equal to the number.
   * @param val A number or expression.
   * @return Number An integer that is both closest to, and greater than or equal to, the parameter val.
   * @see Math.floor()
   * @see Math.round()
   */
  public native static function ceil(val : Number) : Number;

  /**
   * Computes and returns the cosine of the specified angle in radians. To calculate a radian, see the overview of the
   * Math class.
   * @param angleRadians A number that represents an angle measured in radians.
   * @return Number A number from -1.0 to 1.0.
   * @see Math.acos()
   * @see Math.asin()
   * @see Math.atan()
   * @see Math.atan2()
   * @see Math.sin()
   * @see Math.tan()
   */
  public native static function cos(angleRadians : Number) : Number;

  /**
   * Returns the value of the base of the natural logarithm (e), to the power of the exponent specified in the
   * parameter val. The constant Math.E can provide the value of e.
   * @param val The exponent; a number or expression.
   * @return Number e to the power of the parameter val.
   * @see Math.E
   */
  public native static function exp(val : Number) : Number;

  /**
   * Returns the floor of the number or expression specified in the parameter val. The floor is the closest integer
   * that is less than or equal to the specified number or expression. 
   * @param val A number or expression.
   * @return Number The integer that is both closest to, and less than or equal to, the parameter val.
   */
  public native static function floor(val : Number) : Number;

  /**
   * Returns the natural logarithm of the parameter val. 
   * @param val A number or expression with a value greater than 0.
   * @return Number The natural logarithm of parameter val.
   */
  public native static function log(val : Number) : Number;

  /**
   * Evaluates all given values and returns the largest value.
   * @param values Math.max() can accept multiple numbers or number expressions.
   * @return Number The largest of the parameter values or -Infinity when called with no parameters.
   * @see Math.min()
   */
  public native static function max(... values) : Number;

  /**
   * Evaluates all given values and returns the smallest value.
   * @param values Math.min() can accept multiple numbers or number expressions.
   * @return Number The smallest of the parameter values or Infinity when called with no parameters.
   * @see Math.max()
   */
  public native static function min(... values : Array) : Number;

  /**
   * Computes and returns base to the power of pow.
   * @param base A number to be raised by the power of the parameter pow.
   * @param pow A number specifying the power that the parameter base is raised by.
   * @return Number The value of base raised to the power of pow. 
   */
  public static native function pow(base : Number, pow : Number) : Number;

  /**
   * Returns a pseudo-random number n, where 0 <= n < 1. The number returned is calculated in an undisclosed manner,
   * and pseudo-random because the calculation inevitably contains some element of non-randomness.
   * @return A pseudo-random number.
   */
  public native static function random() : Number;

  /**
   * Rounds the value of the parameter val up or down to the nearest integer and returns the value. If val is
   * equidistant from its two nearest integers (that is, if the number ends in .5), the value is rounded up to the
   * next higher integer.
   * @param val The number to round.
   * @return Number The parameter val rounded to the nearest whole number.
   * @see Math.ceil()
   * @see Math.floor()
   */
  public native static function round(val : Number) : Number;

  /**
   * Computes and returns the sine of the specified angle in radians. To calculate a radian, see the overview of the Math class.
   * @param angleRadians A number that represents an angle measured in radians.
   * @return Number A number; the sine of the specified angle (between -1.0 and 1.0).
   * @see Math.acos()
   * @see Math.asin()
   * @see Math.atan()
   * @see Math.atan2()
   * @see Math.cos()
   * @see Math.tan()
   */
  public static native function sin(angleRadians : Number) : Number;

  /**
   * Computes and returns the square root of the specified number.
   * @param val A number or expression greater than or equal to 0.
   * @return Number If the parameter val is greater than or equal to zero, a number; otherwise NaN (not a number).
   */
  public native static function sqrt(val : Number) : Number;

  /**
   * Computes and returns the tangent of the specified angle. To calculate a radian, see the overview of the Math class.
   * @param angleRadians A number that represents an angle measured in radians.
   * @return Number The tangent of the parameter angleRadians.
   * @see Math.acos()
   * @see Math.asin()
   * @see Math.atan()
   * @see Math.atan2()
   * @see Math.cos()
   * @see Math.sin()
   */
  public native static function tan(angleRadians : Number) : Number;

}

}

