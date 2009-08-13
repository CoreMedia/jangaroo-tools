/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * Encodes a string into a valid URI (Uniform Resource Identifier).
 * Converts a complete URI into a string in which all characters are encoded as UTF-8 escape sequences unless a
 * character belongs to a small group of basic characters.
 * <p>The following table shows the entire set of basic characters that are not converted to UTF-8 escape sequences by
 * the encodeURI function.
 * <p>Characters not encoded
 * <pre>
 * 0 1 2 3 4 5 6 7 8 9
 * a b c d e f g h i j k l m n o p q r s t u v w x y z
 * A B C D E F G H I J K L M N O P Q R S T U V W X Y Z
 * ; / ? : @ & = + $ , #
 * - _ . ! ~ * ' ( )
 * </pre>
 * @example
 * <pre>
 * var uri:String = "http://www.example.com/application.jsp?user=<user name='some user'></user>";
 * var encoded:String = encodeURI(uri);
 * var decoded:String = decodeURI(encoded);
 * trace(uri);        // http://www.example.com/application.jsp?user=<user name='some user'></user>
 * trace(encoded);    // http://www.example.com/application.jsp?user=%3Cuser%20name='some%20user'%3E%3C/user%3E
 * trace(decoded);    // http://www.example.com/application.jsp?user=<user name='some user'></user>
 * </pre>
 * @param uri A string representing a complete URI.
 * @return String A string with certain characters encoded as UTF-8 escape sequences.
 * @see decodeURI()
 * @see decodeURIComponent()
 * @see encodeURIComponent()
 */
public native function encodeURI(uri : String) : String;

}