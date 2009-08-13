/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * Converts a string to a floating-point number. The function reads, or parses, and returns the numbers in a string
 * until it reaches a character that is not a part of the initial number. If the string does not begin with a number
 * that can be parsed, parseFloat() returns NaN. White space preceding valid integers is ignored, as are trailing
 * non-numeric characters.
 * @param str The string to read and convert to a floating-point number.
 * @return Number A number or NaN (not a number). 
 */
public native function parseFloat(str : String) : Number;

}