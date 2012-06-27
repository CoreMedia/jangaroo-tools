package {

/**
 * Converts the parameter to a string and encodes it in a URL-encoded format, where most nonalphanumeric characters are replaced with <code>%</code> hexadecimal sequences. When used in a URL-encoded string, the percentage symbol (<code>%</code>) is used to introduce escape characters, and is not equivalent to the modulo operator (<code>%</code>).
 * <p>The following table shows all characters that are <i>not</i> converted to escape sequences by the <code>escape()</code> function.</p>
 * <table>
 * <tr><th>Characters not encoded</th></tr>
 * <tr>
 * <td><code>0 1 2 3 4 5 6 7 8 9</code></td></tr>
 * <tr>
 * <td><code>a b c d e f g h i j k l m n o p q r s t u v w x y z</code></td></tr>
 * <tr>
 * <td><code>A B C D E F G H I J K L M N O P Q R S T U V W X Y Z</code></td></tr>
 * <tr>
 * <td><code>&#64; - _ . * + /</code></td></tr></table>
 * <p><b>Note:</b> Use the <code>encodeURIComponent()</code> function, instead of the <code>escape()</code> function, to treat special separator characters (<code>&#64; + /</code>) as regular text to encode.</p>
 * @param str The expression to convert into a string and encode in a URL-encoded format.
 *
 * @return A URL-encoded string.
 *
 * @see #unescape()
 * @see #encodeURIComponent()
 *
 */
[Native]
public native function escape(str:String):String;

}
