/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * A special member of the Number data type that represents a value that is "not a number" (<code>NaN</code>). When a mathematical expression results in a value that cannot be expressed as a number, the result is <code>NaN</code>. The following list describes common expressions that result in <code>NaN</code>.
 * <ul>
 * <li>Division by 0 results in <code>NaN</code> only if the divisor is also 0. If the divisor is greater than 0, division by 0 results in <code><code>Infinity</code></code>. If the divisor is less than 0, division by 0 results in <code><code>-Infinity</code></code>;</li>
 * <li>Square root of a negative number;</li>
 * <li>The arcsine of a number outside the valid range of 0 to 1;</li>
 * <li><code>Infinity</code> subtracted from <code>Infinity</code>;</li>
 * <li><code>Infinity</code> or <code>-Infinity</code> divided by <code>Infinity</code> or <code>-Infinity</code>;</li>
 * <li><code>Infinity</code> or <code>-Infinity</code> multiplied by 0;</li></ul>
 * <p>The <code>NaN</code> value is not a member of the int or uint data types.</p>
 * <p>The <code>NaN</code> value is not considered equal to any other value, including <code>NaN</code>, which makes it impossible to use the equality operator to test whether an expression is <code>NaN</code>. To determine whether a number is the <code>NaN</code> function, use <code>isNaN()</code>.</p>
 * @see #isNaN()
 * @see Number#NaN
 *
 */
public native function get NaN():Number;
}
