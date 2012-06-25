/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * Returns <code>true</code> if the value is <code>NaN</code>(not a number). The <code>isNaN()</code> function is useful for checking whether a mathematical expression evaluates successfully to a number. The most common use of <code>isNaN()</code> is to check the value returned from the <code>parseInt()</code>and <code>parseFloat()</code> functions. The <code>NaN</code> value is a special member of the Number data type that represents a value that is "not a number."
 * <p><b>Note</b>: The <code>NaN</code> value is not a member of the int or uint data types.</p>
 * <p>The following table describes the return value of <code>isNaN()</code> on various input types and values. (If your compiler warnings are set to Strict Mode, some of the following operations will generate compiler warnings.)</p>
 * <table>
 * <tr><th>Input Type/Value</th><th>Example</th><th>Return Value</th></tr>
 * <tr>
 * <td>0 divided by 0</td>
 * <td><code>isNaN(0/0)</code></td>
 * <td><code>true</code></td></tr>
 * <tr>
 * <td>Non-zero number divided by <code>0</code></td>
 * <td><code>isNaN(5/0)</code></td>
 * <td><code>false</code></td></tr>
 * <tr>
 * <td>Square root of a negative number</td>
 * <td><code>isNaN(Math.sqrt(-1))</code></td>
 * <td><code>true</code></td></tr>
 * <tr>
 * <td>Arcsine of number greater than 1 or less than 0</td>
 * <td><code>isNaN(Math.asin(2))</code></td>
 * <td><code>true</code></td></tr>
 * <tr>
 * <td>String that can be converted to Number</td>
 * <td><code>isNaN("5")</code></td>
 * <td><code>false</code></td></tr>
 * <tr>
 * <td>String that cannot be converted to Number</td>
 * <td><code>isNaN("5a")</code></td>
 * <td><code>true</code></td></tr></table>
 * @param num A numeric value or mathematical expression to evaluate.
 *
 * @return Returns <code>true</code> if the value is <code>NaN</code>(not a number) and <code>false</code> otherwise.
 *
 */
public native function isNaN(num : *) : Boolean;

}
