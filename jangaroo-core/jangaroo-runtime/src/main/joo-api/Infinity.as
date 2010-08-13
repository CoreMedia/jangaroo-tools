/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {
/**
 * A special value representing positive Infinity.
 * The value of this constant is the same as Number.POSITIVE_INFINITY.
 * @see Number#POSITIVE_INFINITY
 * @example
 * The result of division by 0 is Infinity, but only when the divisor is a positive number.
 * <pre>
 * trace(0 / 0);  // NaN
 * trace(7 / 0);  // Infinity
 * trace(-7 / 0); // -Infinity
 * </pre>
 */
public native function get Infinity() : Number;
}