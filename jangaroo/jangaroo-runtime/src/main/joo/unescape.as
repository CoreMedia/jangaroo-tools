package {


/**
 * Evaluates the parameter <code>str</code> as a string, decodes the string from URL-encoded format (converting all hexadecimal sequences to ASCII characters), and returns the string.
 * @param str A string with hexadecimal sequences to escape.
 *
 * @return A string decoded from a URL-encoded parameter.
 *
 */
[Native]
public native function unescape(str:String):String;

}
