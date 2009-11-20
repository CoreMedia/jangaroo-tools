package {

/**
 * Encodes a string into a valid URI component.
 * Converts a substring of a URI into a string in which all characters are encoded as UTF-8 escape sequences unless a
 * character belongs to a very small group of basic characters.
 * <p>The encodeURIComponent() function differs from the encodeURI() function in that it is intended for use only with
 * a part of a URI string, called a URI component. A URI component is any text that appears between special characters
 * called component separators (: / ; and ? ). Common examples of a URI component are "http" and "www.adobe.com".
 * <p>Another important difference between this function and encodeURI() is that because this function assumes that it
 * is processing a URI component it treats the special separator characters (; / ? : @ & = + $ , #) as regular text
 * that should be encoded.
 * <p>The following table shows all characters that are not converted to UTF-8 escape sequences by the
 * encodeURIComponent function.
 * <p>Characters not encoded
 * <pre>
 * 0 1 2 3 4 5 6 7 8 9
 * a b c d e f g h i j k l m n o p q r s t u v w x y z
 * A B C D E F G H I J K L M N O P Q R S T U V W X Y Z
 * ; / ? : @ & = + $ , #
 * - _ . ! ~ * ' ( )
 * </pre>
 * @param uri A substring of a URI that is to be encoded into a valid URI component.
 * @return String A string with certain characters encoded as UTF-8 escape sequences.
 * @see decodeURI()
 * @see decodeURIComponent()
 * @see encodeURIComponent()
 */
public native function encodeURIComponent(uri : String) : String;

}