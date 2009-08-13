/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * Decodes an encoded URI into a string.
 * Returns a string in which all characters previously encoded by the encodeURI function are restored to their
 * unencoded representation.
 * <p>The following table shows the set of escape sequences that are not decoded to characters by the decodeURI function.
 * Use decodeURIComponent() to decode the escape sequences in this table.
 * <p>Escape sequences not decoded / Character equivalents
 * <pre>
 * %23 / #
 * %24 / $
 * %26 / &
 * %2B / +
 * %2C / ,
 * %2F / /
 * %3A / :
 * %3B / ;
 * %3D / =
 * %3F / ?
 * %40 / @
 * 
 * @example
 * <pre>
 * var uri:String = "http://www.example.com/application.jsp?user=<user name='some user'></user>";
 * var encoded:String = encodeURI(uri);
 * var decoded:String = decodeURI(encoded);
 * trace(uri);        // http://www.example.com/application.jsp?user=<user name='some user'></user>
 * trace(encoded);    // http://www.example.com/application.jsp?user=%3Cuser%20name='some%20user'%3E%3C/user%3E
 * trace(decoded);    // http://www.example.com/application.jsp?user=<user name='some user'></user>
 * </pre>
 * @param uri A string encoded with the encodeURI function.
 * @return String A string in which all characters previously escaped by the encodeURI function are restored to their
 * unescaped representation.
 * @see decodeURIComponent()
 * @see encodeURI()
 * @see encodeURIComponent()
 */
public native function decodeURI(uri : String) : String;

}