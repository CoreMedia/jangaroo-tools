/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * The Math class contains methods and constants that represent common mathematical functions and values.
 * <p>Use the methods and properties of this class to access and manipulate mathematical constants and functions. All the properties and methods of the Math class are static and must be called using the syntax <code>Math.method(</code> <code><i>parameter</i></code> <code>)</code> or <code>Math.constant</code>. In ActionScript, constants are defined with the maximum precision of double-precision IEEE-754 floating-point numbers.</p>
 * <p>Several Math class methods use the measure of an angle in radians as a parameter. You can use the following equation to calculate radian values before calling the method and then provide the calculated value as the parameter, or you can provide the entire right side of the equation (with the angle's measure in degrees in place of <code>degrees</code>) as the radian parameter.</p>
 * <p>To calculate a radian value, use the following formula:</p>
 * <pre> radians = degrees * Math.PI/180
 </pre>
 * <p>To calculate degrees from radians, use the following formula:</p>
 * <pre> degrees = radians * 180/Math.PI
 </pre>
 * <p>The following is an example of passing the equation as a parameter to calculate the sine of a 45° angle:</p>
 * <p><code>Math.sin(45 * Math.PI/180)</code> is the same as <code>Math.sin(.7854)</code></p>
 * <p><b>Note:</b> The Math functions acos, asin, atan, atan2, cos, exp, log, pow, sin, and sqrt may result in slightly different values depending on the algorithms used by the CPU or operating system. Flash runtimes call on the CPU (or operating system if the CPU doesn't support floating point calculations) when performing the calculations for the listed functions, and results have shown slight variations depending upon the CPU or operating system in use.</p>
 */
public final class Math {
  /**
   * Computes and returns an absolute value for the number specified by the parameter <code>val</code>.
   * @param val The number whose absolute value is returned.
   *
   * @return The absolute value of the specified paramater.
   *
   */
  public native static function abs(val : Number) : Number;

  /**
   * Computes and returns the arc cosine of the number specified in the parameter <code>val</code>, in radians.
   * @param val A number from -1.0 to 1.0.
   *
   * @return The arc cosine of the parameter <code>val</code>.
   *
   */
  public native static function acos(val : Number) : Number;

  /**
   * Computes and returns the arc sine for the number specified in the parameter <code>val</code>, in radians.
   * @param val A number from -1.0 to 1.0.
   *
   * @return A number between negative pi divided by 2 and positive pi divided by 2.
   *
   */
  public native static function asin(val : Number) : Number;

  /**
   * Computes and returns the value, in radians, of the angle whose tangent is specified in the parameter <code>val</code>. The return value is between negative pi divided by 2 and positive pi divided by 2.
   * @param val A number that represents the tangent of an angle.
   *
   * @return A number between negative pi divided by 2 and positive pi divided by 2.
   *
   */
  public native static function atan(val : Number) : Number;

  /**
   * Computes and returns the angle of the point <code>y</code>/<code>x</code> in radians, when measured counterclockwise from a circle's <i>x</i> axis (where 0,0 represents the center of the circle). The return value is between positive pi and negative pi. Note that the first parameter to atan2 is always the <i>y</i> coordinate.
   * @param y The <i>y</i> coordinate of the point.
   * @param x The <i>x</i> coordinate of the point.
   *
   * @return A number.
   *
   * @see #acos()
   * @see #asin()
   * @see #atan()
   * @see #cos()
   * @see #sin()
   * @see #tan()
   *
   */
  public native static function atan2(y : Number, x : Number) : Number;

  /**
   * Returns the ceiling of the specified number or expression. The ceiling of a number is the closest integer that is greater than or equal to the number.
   * @param val A number or expression.
   *
   * @return An integer that is both closest to, and greater than or equal to, the parameter <code>val</code>.
   *
   * @see #floor()
   * @see #round()
   *
   */
  public native static function ceil(val : Number) : Number;

  /**
   * Computes and returns the cosine of the specified angle in radians. To calculate a radian, see the overview of the Math class.
   * @param angleRadians A number that represents an angle measured in radians.
   *
   * @return A number from -1.0 to 1.0.
   *
   * @see #acos()
   * @see #asin()
   * @see #atan()
   * @see #atan2()
   * @see #sin()
   * @see #tan()
   *
   */
  public native static function cos(angleRadians : Number) : Number;

  /**
   * Returns the value of the base of the natural logarithm (<i>e</i>), to the power of the exponent specified in the parameter <code>x</code>. The constant <code>Math.E</code> can provide the value of <i>e</i>.
   * @param val The exponent; a number or expression.
   *
   * @return <i>e</i> to the power of the parameter <code>val</code>.
   *
   * @see #E
   *
   */
  public native static function exp(val : Number) : Number;

  /**
   * Returns the floor of the number or expression specified in the parameter <code>val</code>. The floor is the closest integer that is less than or equal to the specified number or expression.
   * @param val A number or expression.
   *
   * @return The integer that is both closest to, and less than or equal to, the parameter <code>val</code>.
   *
   */
  public native static function floor(val : Number) : Number;

  /**
   * Returns the natural logarithm of the parameter <code>val</code>.
   * @param val A number or expression with a value greater than 0.
   *
   * @return The natural logarithm of parameter <code>val</code>.
   *
   */
  public native static function log(val : Number) : Number;

  /**
   * Evaluates all given values and returns the largest value.
   * @param values Math.max() can accept multiple numbers or number expressions.
   *
   * @return Number The largest of the parameter values or -Infinity when called with no parameters.
   *
   * @see #min()
   *
   */
  public native static function max(... values) : Number;

  /**
   * Evaluates all given values and returns the smallest value.
   * @param values Math.min() can accept multiple numbers or number expressions.
   *
   * @return Number The smallest of the parameter values or Infinity when called with no parameters.
   *
   * @see #max()
   *
   */
  public native static function min(... values : Array) : Number;

  /**
   * Computes and returns <code>base</code> to the power of <code>pow</code>.
   * @param base A number to be raised by the power of the parameter <code>pow</code>.
   * @param pow A number specifying the power that the parameter <code>base</code> is raised by.
   *
   * @return The value of <code>base</code> raised to the power of <code>pow</code>.
   *
   */
  public static native function pow(base : Number, pow : Number) : Number;

  /**
   * Returns a pseudo-random number n, where 0 <= n < 1. The number returned is calculated in an undisclosed manner, and is "pseudo-random" because the calculation inevitably contains some element of non-randomness.
   * @return A pseudo-random number.
   *
   */
  public native static function random() : Number;

  /**
   * Rounds the value of the parameter <code>val</code> up or down to the nearest integer and returns the value. If <code>val</code> is equidistant from its two nearest integers (that is, if the number ends in .5), the value is rounded up to the next higher integer.
   * @param val The number to round.
   *
   * @return The parameter <code>val</code> rounded to the nearest whole number.
   *
   * @see #ceil()
   * @see #floor()
   *
   */
  public native static function round(val : Number) : Number;

  /**
   * Computes and returns the sine of the specified angle in radians. To calculate a radian, see the overview of the Math class.
   * @param angleRadians A number that represents an angle measured in radians.
   *
   * @return A number; the sine of the specified angle (between -1.0 and 1.0).
   *
   * @see #acos()
   * @see #asin()
   * @see #atan()
   * @see #atan2()
   * @see #cos()
   * @see #tan()
   *
   */
  public static native function sin(angleRadians : Number) : Number;

  /**
   * Computes and returns the square root of the specified number.
   * @param val A number or expression greater than or equal to 0.
   *
   * @return If the parameter <code>val</code> is greater than or equal to zero, a number; otherwise <code>NaN</code> (not a number).
   *
   */
  public native static function sqrt(val : Number) : Number;

  /**
   * Computes and returns the tangent of the specified angle. To calculate a radian, see the overview of the Math class.
   * @param angleRadians A number that represents an angle measured in radians.
   *
   * @return The tangent of the parameter <code>angleRadians</code>.
   *
   * @see #acos()
   * @see #asin()
   * @see #atan()
   * @see #atan2()
   * @see #cos()
   * @see #sin()
   *
   */
  public native static function tan(angleRadians : Number) : Number;

  /**
   * A mathematical constant for the base of natural logarithms, expressed as <i>e</i>. The approximate value of <i>e</i> is 2.71828182845905.
   */
  public native static function get E() : Number;

  /**
   * A mathematical constant for the natural logarithm of 10, expressed as log10, with an approximate value of 2.302585092994046.
   */
  public native static function get LN10() : Number;

  /**
   * A mathematical constant for the natural logarithm of 2, expressed as log2, with an approximate value of 0.6931471805599453.
   */
  public native static function get LN2() : Number;

  /**
   * A mathematical constant for the base-10 logarithm of the constant <i>e</i> (<code>Math.E</code>), expressed as loge, with an approximate value of 0.4342944819032518.
   * <p>The <code>Math.log()</code> method computes the natural logarithm of a number. Multiply the result of <code>Math.log()</code> by <code>Math.LOG10E</code> to obtain the base-10 logarithm.</p>
   */
  public native static function get LOG10E() : Number;

  /**
   * A mathematical constant for the base-2 logarithm of the constant <i>e</i>, expressed as log2e, with an approximate value of 1.442695040888963387.
   * <p>The <code>Math.log</code> method computes the natural logarithm of a number. Multiply the result of <code>Math.log()</code> by <code>Math.LOG2E</code> to obtain the base-2 logarithm.</p>
   */
  public native static function get LOG2E() : Number;

  /**
   * A mathematical constant for the ratio of the circumference of a circle to its diameter, expressed as pi, with a value of 3.141592653589793.
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

}
}
