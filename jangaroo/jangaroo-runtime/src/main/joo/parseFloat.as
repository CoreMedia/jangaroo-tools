/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * Converts a string to a floating-point number. The function reads, or <i>parses</i>, and returns the numbers in a string until it reaches a character that is not a part of the initial number. If the string does not begin with a number that can be parsed, <code>parseFloat()</code> returns <code>NaN</code>. White space preceding valid integers is ignored, as are trailing nonnumeric characters.
 * @param str The string to read and convert to a floating-point number.
 *
 * @return A number or <code>NaN</code> (not a number).
 *
 */
public native function parseFloat(str:String):Number;

}
