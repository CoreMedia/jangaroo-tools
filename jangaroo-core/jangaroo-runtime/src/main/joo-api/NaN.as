/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * A special member of the Number data type that represents a value that is "not a number" (NaN). When a mathematical
 * expression results in a value that cannot be expressed as a number, the result is NaN. The following list describes
 * common expressions that result in NaN.
 * <ul>
 * <li>Division by 0 results in NaN only if the divisor is also 0. If the divisor is greater than 0, division by 0 results in Infinity. If the divisor is less than 0, division by 0 results in -Infinity;
 * <li>Square root of a negative number;
 * <li>The arcsine of a number outside the valid range of 0 to 1;
 * <li>Infinity subtracted from Infinity;
 * <li>Infinity or -Infinity divided by Infinity or -Infinity;
 * <li>Infinity or -Infinity multiplied by 0;
 * </ul>
 * The NaN value is not a member of the int or uint data types.
 * <p>The NaN value is not considered equal to any other value, including NaN, which makes it impossible to use the
 * equality operator to test whether an expression is NaN. To determine whether a number is the NaN function, use
 * isNaN().
 * @see isNaN()
 * @see Number.NaN
 */
public const NaN : Number = NaN; 
}
