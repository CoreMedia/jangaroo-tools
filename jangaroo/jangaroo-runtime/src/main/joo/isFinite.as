/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * Returns <code>true</code> if the value is a finite number, or <code>false</code> if the value is <code>Infinity</code> or <code>-Infinity</code>. The presence of <code>Infinity</code> or <code>-Infinity</code> indicates a mathematical error condition such as division by 0.
 * @param num A number to evaluate as finite or infinite.
 *
 * @return Returns <code>true</code> if it is a finite number or <code>false</code> if it is infinity or negative infinity
 *
 */
public native function isFinite(num : *) : Boolean;

}
