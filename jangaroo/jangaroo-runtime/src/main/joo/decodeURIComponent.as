/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * Decodes an encoded URI component into a string. Returns a string in which all characters previously escaped by the <code>encodeURIComponent</code> function are restored to their uncoded representation.
 * <p>This function differs from the <code>decodeURI()</code> function in that it is intended for use only with a part of a URI string, called a URI component. A URI component is any text that appears between special characters called <i>component separators</i> (<code>: / ; and ?</code> ). Common examples of a URI component are "http" and "www.adobe.com".</p>
 * <p>Another important difference between this function and <code>decodeURI()</code> is that because this function assumes that it is processing a URI component it treats the escape sequences that represent special separator characters (<code>; / ? : @ & = + $ , #</code>) as regular text that should be decoded.</p>
 * @param uri A string encoded with the <code>encodeURIComponent</code> function.
 *
 * @return A string in which all characters previously escaped by the <code>encodeURIComponent</code> function are restored to their unescaped representation.
 *
 * @see #decodeURI()
 * @see #encodeURI()
 * @see #encodeURIComponent()
 *
 */
public native function decodeURIComponent(uri : String) : String;

}
