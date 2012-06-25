/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {
/**
 * A special value representing positive <code>Infinity</code>. The value of this constant is the same as <code>Number.POSITIVE_INFINITY</code>.
 * @see Number#POSITIVE_INFINITY
 *
 * @example The result of division by 0 is <code>Infinity</code>, but only when the divisor is a positive number.
 * <listing>
 * trace(0 / 0);  // NaN
 * trace(7 / 0);  // Infinity
 * trace(-7 / 0); // -Infinity
 * </listing>
 */
public native function get Infinity() : Number;
}
