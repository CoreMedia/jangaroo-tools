/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * Returns true if the value is a finite number, or false if the value is Infinity or -Infinity. The presence of
 * Infinity or -Infinity indicates a mathematical error condition such as division by 0.
 * @param num A number to evaluate as finite or infinite.
 * @return Boolean Returns true if it is a finite number or false if it is infinity or negative infinity
 */
public native function isFinite(num : *) : Boolean;

}