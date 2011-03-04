/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * Decodes an encoded URI into a string. Returns a string in which all characters previously encoded by the <code>encodeURI</code> function are restored to their unencoded representation.
 * <p>The following table shows the set of escape sequences that are <i>not</i> decoded to characters by the <code>decodeURI</code> function. Use <code>decodeURIComponent()</code> to decode the escape sequences in this table.</p>
 * <table>
 * <tr><th>Escape sequences not decoded</th><th>Character equivalents</th></tr>
 * <tr>
 * <td><code>%23</code></td>
 * <td><code>#</code></td></tr>
 * <tr>
 * <td><code>%24</code></td>
 * <td><code>$</code></td></tr>
 * <tr>
 * <td><code>%26</code></td>
 * <td><code>&</code></td></tr>
 * <tr>
 * <td><code>%2B</code></td>
 * <td><code>+</code></td></tr>
 * <tr>
 * <td><code>%2C</code></td>
 * <td><code>,</code></td></tr>
 * <tr>
 * <td><code>%2F</code></td>
 * <td><code>/</code></td></tr>
 * <tr>
 * <td><code>%3A</code></td>
 * <td><code>:</code></td></tr>
 * <tr>
 * <td><code>%3B</code></td>
 * <td><code>;</code></td></tr>
 * <tr>
 * <td><code>%3D</code></td>
 * <td><code>=</code></td></tr>
 * <tr>
 * <td><code>%3F</code></td>
 * <td><code>?</code></td></tr>
 * <tr>
 * <td><code>%40</code></td>
 * <td><code>&#64;</code></td></tr></table>
 * @param uri A string encoded with the <code>encodeURI</code> function.
 *
 * @return A string in which all characters previously escaped by the <code>encodeURI</code> function are restored to their unescaped representation.
 *
 * @see #decodeURIComponent()
 * @see #encodeURI()
 * @see #encodeURIComponent()
 *
 * @example
 * <pre>
 * package {
 *     import flash.display.Sprite;
 *
 *     public class DecodeURIExample extends Sprite {
 *         public function DecodeURIExample() {
 *             var uri:String = "http://www.example.com/application.jsp?user=<user name='some user'></user>";
 *             var encoded:String = encodeURI(uri);
 *             var decoded:String = decodeURI(encoded);
 *             trace(uri);        // http://www.example.com/application.jsp?user=<user name='some user'></user>
 *             trace(encoded);    // http://www.example.com/application.jsp?user=%3Cuser%20name='some%20user'%3E%3C/user%3E
 *             trace(decoded);    // http://www.example.com/application.jsp?user=<user name='some user'></user>
 *         }
 *     }
 * }
 * </pre>
 */
public native function decodeURI(uri : String) : String;

}