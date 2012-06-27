package {

/**
 * Encodes a string into a valid URI component. Converts a substring of a URI into a string in which all characters are encoded as UTF-8 escape sequences unless a character belongs to a very small group of basic characters.
 * <p>The <code>encodeURIComponent()</code> function differs from the <code>encodeURI()</code> function in that it is intended for use only with a part of a URI string, called a URI component. A URI component is any text that appears between special characters called <i>component separators</i> (<code>: / ; and ?</code> ). Common examples of a URI component are "http" and "www.adobe.com".</p>
 * <p>Another important difference between this function and <code>encodeURI()</code> is that because this function assumes that it is processing a URI component it treats the special separator characters (<code>; / ? : @ & = + $ , #</code>) as regular text that should be encoded.</p>
 * <p>The following table shows all characters that are <i>not</i> converted to UTF-8 escape sequences by the <code>encodeURIComponent</code> function.</p>
 * <table>
 * <tr><th>Characters not encoded</th></tr>
 * <tr>
 * <td><code>0 1 2 3 4 5 6 7 8 9</code></td></tr>
 * <tr>
 * <td><code>a b c d e f g h i j k l m n o p q r s t u v w x y z</code></td></tr>
 * <tr>
 * <td><code>A B C D E F G H I J K L M N O P Q R S T U V W X Y Z</code></td></tr>
 * <tr>
 * <td><code>- _ . ! ~ * ' ( )</code></td></tr></table>
 * @param uri A substring of a URI that is to be encoded into a valid URI component.
 * @return String A string with certain characters encoded as UTF-8 escape sequences.
 * @see #decodeURI()
 * @see #decodeURIComponent()
 * @see #encodeURI()
 *
 */
[Native]
public native function encodeURIComponent(uri : String) : String;

}
