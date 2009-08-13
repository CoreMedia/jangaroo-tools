/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * Converts a string to an integer. If the specified string in the parameters cannot be converted to a number, the
 * function returns NaN. Strings beginning with 0x are interpreted as hexadecimal numbers. Unlike in previous versions
 * of ActionScript, integers beginning with 0 are not interpreted as octal numbers. You must specify a radix of 8 for
 * octal numbers. White space and zeroes preceding valid integers are ignored, as are trailing nonnumeric characters. 
 * @param str A string to convert to an integer.
 * @param radix An integer representing the radix (base) of the number to parse. Legal values are from 2 to 36.
 * @return Number A number or NaN (not a number).
 */
public native function parseInt(str : String, radix : uint = 0) : Number;

}