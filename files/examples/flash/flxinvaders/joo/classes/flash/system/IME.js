joo.classLoader.prepare("package flash.system",/* {
import flash.events.EventDispatcher*/

/**
 * property IMEEvent.type =
 * @eventType flash.events.IMEEvent.IME_COMPOSITION
 */
{Event:{name:"imeComposition", type:"flash.events.IMEEvent"}},

/**
 * The IME class lets you directly manipulate the operating system's input method editor (IME) in the Flash runtime application that is running on a client computer. You can determine whether an IME is installed, whether or not the IME is currently enabled, and which IME is enabled. You can disable or enable the IME in the application, and you can perform other limited functions, depending on the operating system.
 * <p><i>AIR profile support:</i> This feature is supported on desktop operating systems, but it is not supported on all mobile devices. It is also not supported on AIR for TV devices. You can test for support at run time using the <code>IME.isSupported</code> property. See <a href="http://help.adobe.com/en_US/air/build/WS144092a96ffef7cc16ddeea2126bb46b82f-8000.html">AIR Profile Support</a> for more information regarding API support across multiple profiles.</p>
 * <p>IMEs let users type non-ASCII text characters in multibyte languages such as Chinese, Japanese, and Korean. For more information on working with IMEs, see the documentation for the operating system for which you are developing applications. For additional resources, see the following websites:</p>
 * <ul>
 * <li><a href="http://www.microsoft.com/globaldev/default.mspx">http://www.microsoft.com/globaldev/default.mspx</a></li>
 * <li><a href="http://developer.apple.com/documentation/">http://developer.apple.com/documentation/</a></li>
 * <li><a href="http://java.sun.com">http://java.sun.com</a></li></ul>
 * <p>If an IME is not active on the user's computer, calls to IME methods or properties, other than <code>Capabilities.hasIME</code>, will fail. Once you manually activate an IME, subsequent ActionScript calls to IME methods and properties will work as expected. For example, if you are using a Japanese IME, it must be activated before any IME method or property is called.</p>
 * <p>The following table shows the platform coverage of this class:</p>
 * <table>
 * <tr><th>Capability</th><th>Windows</th><th>Mac OSX</th><th>Linux</th></tr>
 * <tr>
 * <td>Determine whether the IME is installed: <code>Capabilities.hasIME</code> </td>
 * <td>Yes</td>
 * <td>Yes</td>
 * <td>Yes</td></tr>
 * <tr>
 * <td>Set IME on or off: <code>IME.enabled</code> </td>
 * <td>Yes</td>
 * <td>Yes</td>
 * <td>Yes</td></tr>
 * <tr>
 * <td>Find out whether IME is on or off: <code>IME.enabled</code> </td>
 * <td>Yes</td>
 * <td>Yes</td>
 * <td>Yes</td></tr>
 * <tr>
 * <td>Get or set IME conversion mode: <code>IME.conversionMode</code> </td>
 * <td>Yes</td>
 * <td>Yes **</td>
 * <td>No</td></tr>
 * <tr>
 * <td>Send IME the string to be converted: <code>IME.setCompositionString()</code> </td>
 * <td>Yes *</td>
 * <td>No</td>
 * <td>No</td></tr>
 * <tr>
 * <td>Get from IME the original string before conversion: <code>System.ime.addEventListener()</code> </td>
 * <td>Yes *</td>
 * <td>No</td>
 * <td>No</td></tr>
 * <tr>
 * <td>Send request to convert to IME: <code>IME.doConversion()</code> </td>
 * <td>Yes *</td>
 * <td>No</td>
 * <td>No</td></tr></table>
 * <p>* Not all Windows IMEs support all of these operations. The only IME that supports them all is the Japanese IME.</p>
 * <p>** On the Macintosh, only the Japanese IME supports these methods, and third-party IMEs do not support them.</p>
 * <p>The ActionScript 3.0 version of this class does not support Macintosh Classic.</p>
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cd5.html Using the IME class
 *
 */
"public final class IME extends flash.events.EventDispatcher",2,function($$private){;return[ 
  /**
   * The conversion mode of the current IME. Possible values are IME mode string constants that indicate the conversion mode:
   * <ul>
   * <li><code>ALPHANUMERIC_FULL</code></li>
   * <li><code>ALPHANUMERIC_HALF</code></li>
   * <li><code>CHINESE</code></li>
   * <li><code>JAPANESE_HIRAGANA</code></li>
   * <li><code>JAPANESE_KATAKANA_FULL</code></li>
   * <li><code>JAPANESE_KATAKANA_HALF</code></li>
   * <li><code>KOREAN</code></li>
   * <li><code>UNKNOWN</code> (read-only value; this value cannot be set)</li></ul>
   * @throws Error A set attempt was not successful.
   *
   * @see IMEConversionMode#ALPHANUMERIC_FULL
   * @see IMEConversionMode#ALPHANUMERIC_HALF
   * @see IMEConversionMode#CHINESE
   * @see IMEConversionMode#JAPANESE_HIRAGANA
   * @see IMEConversionMode#JAPANESE_KATAKANA_FULL
   * @see IMEConversionMode#JAPANESE_KATAKANA_HALF
   * @see IMEConversionMode#KOREAN
   * @see IMEConversionMode#UNKNOWN
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cd5.html Using the IME class
   *
   */
  "public static function get conversionMode",function conversionMode$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public static function set conversionMode",function conversionMode$set(value/*:String*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Indicates whether the system IME is enabled (<code>true</code>) or disabled (<code>false</code>). An enabled IME performs multibyte input; a disabled IME performs alphanumeric input.
   * @throws Error A set attempt was not successful.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cd5.html Using the IME class
   *
   */
  "public static function get enabled",function enabled$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public static function set enabled",function enabled$set(value/*:Boolean*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Instructs the IME to select the first candidate for the current composition string.
   * @throws Error The call was not successful.
   *
   */
  "public static function doConversion",function doConversion()/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Sets the IME composition string. When this string is set, the user can select IME candidates before committing the result to the text field that currently has focus.
   * <p>If no text field has focus, this method fails and throws an error.</p>
   * @param composition The string to send to the IME.
   *
   * @throws Error The call is not successful.
   *
   */
  "public static function setCompositionString",function setCompositionString(composition/*:String*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},["conversionMode","enabled","doConversion","setCompositionString"],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.1"
);