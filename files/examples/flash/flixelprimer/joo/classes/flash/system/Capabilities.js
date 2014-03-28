joo.classLoader.prepare("package flash.system",/* {
import joo.debug
import joo.getQualifiedObject*/

/**
 * The Capabilities class provides properties that describe the system and runtime that are hosting the application. For example, a mobile phone's screen might be 100 square pixels, black and white, whereas a PC screen might be 1000 square pixels, color. By using the Capabilities class to determine what capabilities the client has, you can provide appropriate content to as many users as possible. When you know the device's capabilities, you can tell the server to send the appropriate SWF files or tell the SWF file to alter its presentation.
 * <p>However, some capabilities of Adobe AIR are not listed as properties in the Capabilities class. They are properties of other classes:</p>
 * <table>
 * <tr><th>Property</th><th>Description</th></tr>
 * <tr>
 * <td><code>NativeApplication.supportsDockIcon</code> </td>
 * <td>Whether the operating system supports application doc icons.</td></tr>
 * <tr>
 * <td><code>NativeApplication.supportsMenu</code> </td>
 * <td>Whether the operating system supports a global application menu bar.</td></tr>
 * <tr>
 * <td><code>NativeApplication.supportsSystemTrayIcon</code> </td>
 * <td>Whether the operating system supports system tray icons.</td></tr>
 * <tr>
 * <td><code>NativeWindow.supportsMenu</code> </td>
 * <td>Whether the operating system supports window menus.</td></tr>
 * <tr>
 * <td><code>NativeWindow.supportsTransparency</code> </td>
 * <td>Whether the operating system supports transparent windows.</td></tr></table>
 * <p>Do <i>not</i> use <code>Capabilities.os</code> or <code>Capabilities.manufacturer</code> to determine a capability based on the operating system. Basing a capability on the operating system is a bad idea, since it can lead to problems if an application does not consider all potential target operating systems. Instead, use the property corresponding to the capability for which you are testing.</p>
 * <p>You can send capabilities information, which is stored in the <code>Capabilities.serverString</code> property as a URL-encoded string, using the <code>GET</code> or <code>POST</code> HTTP method. The following example shows a server string for a computer that has MP3 support and 1600 x 1200 pixel resolution and that is running Windows XP with an input method editor (IME) installed:</p>
 * <pre>A=t&SA=t&SV=t&EV=t&MP3=t&AE=t&VE=t&ACC=f&PR=t&SP=t&
 SB=f&DEB=t&V=WIN%209%2C0%2C0%2C0&M=Adobe%20Windows&
 R=1600x1200&DP=72&COL=color&AR=1.0&OS=Windows%20XP&
 L=en&PT=External&AVD=f&LFD=f&WD=f&IME=t</pre>
 * <p>The following table lists the properties of the Capabilities class and corresponding server strings:</p>
 * <table>
 * <tr><th>Capabilities class property</th><th>Server string</th></tr>
 * <tr>
 * <td><code>avHardwareDisable</code> </td>
 * <td><code>AVD</code> </td></tr>
 * <tr>
 * <td><code>hasAccessibility</code> </td>
 * <td><code>ACC</code> </td></tr>
 * <tr>
 * <td><code>hasAudio</code> </td>
 * <td><code>A</code> </td></tr>
 * <tr>
 * <td><code>hasAudioEncoder</code> </td>
 * <td><code>AE</code> </td></tr>
 * <tr>
 * <td><code>hasEmbeddedVideo</code> </td>
 * <td><code>EV</code> </td></tr>
 * <tr>
 * <td><code>hasIME</code> </td>
 * <td><code>IME</code> </td></tr>
 * <tr>
 * <td><code>hasMP3</code> </td>
 * <td><code>MP3</code> </td></tr>
 * <tr>
 * <td><code>hasPrinting</code> </td>
 * <td><code>PR</code> </td></tr>
 * <tr>
 * <td><code>hasScreenBroadcast</code> </td>
 * <td><code>SB</code> </td></tr>
 * <tr>
 * <td><code>hasScreenPlayback</code> </td>
 * <td><code>SP</code> </td></tr>
 * <tr>
 * <td><code>hasStreamingAudio</code> </td>
 * <td><code>SA</code> </td></tr>
 * <tr>
 * <td><code>hasStreamingVideo</code> </td>
 * <td><code>SV</code> </td></tr>
 * <tr>
 * <td><code>hasTLS</code> </td>
 * <td><code>TLS</code> </td></tr>
 * <tr>
 * <td><code>hasVideoEncoder</code> </td>
 * <td><code>VE</code> </td></tr>
 * <tr>
 * <td><code>isDebugger</code> </td>
 * <td><code>DEB</code> </td></tr>
 * <tr>
 * <td><code>language</code> </td>
 * <td><code>L</code> </td></tr>
 * <tr>
 * <td><code>localFileReadDisable</code> </td>
 * <td><code>LFD</code> </td></tr>
 * <tr>
 * <td><code>manufacturer</code> </td>
 * <td><code>M</code> </td></tr>
 * <tr>
 * <td><code>maxLevelIDC</code> </td>
 * <td><code>ML</code> </td></tr>
 * <tr>
 * <td><code>os</code> </td>
 * <td><code>OS</code> </td></tr>
 * <tr>
 * <td><code>pixelAspectRatio</code> </td>
 * <td><code>AR</code> </td></tr>
 * <tr>
 * <td><code>playerType</code> </td>
 * <td><code>PT</code> </td></tr>
 * <tr>
 * <td><code>screenColor</code> </td>
 * <td><code>COL</code> </td></tr>
 * <tr>
 * <td><code>screenDPI</code> </td>
 * <td><code>DP</code> </td></tr>
 * <tr>
 * <td><code>screenResolutionX</code> </td>
 * <td><code>R</code> </td></tr>
 * <tr>
 * <td><code>screenResolutionY</code> </td>
 * <td><code>R</code> </td></tr>
 * <tr>
 * <td><code>version</code> </td>
 * <td><code>V</code> </td></tr></table>
 * <p>There is also a <code>WD</code> server string that specifies whether windowless mode is disabled. Windowless mode can be disabled in Flash Player due to incompatibility with the web browser or to a user setting in the mms.cfg file. There is no corresponding Capabilities property.</p>
 * <p>All properties of the Capabilities class are read-only.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/system/Capabilities.html#includeExamplesSummary">View the examples</a></p>
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf69084-7ebb.html Determining Flash Player version in Flex
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cd8.html Using the Capabilities class
 *
 */
"public final class Capabilities",1,function($$private){;return[ 
  /**
   * Specifies whether access to the user's camera and microphone has been administratively prohibited (<code>true</code>) or allowed (<code>false</code>). The server string is <code>AVD</code>.
   * <p>For content in Adobe AIR™, this property applies only to content in security sandboxes other than the application security sandbox. Content in the application security sandbox can always access the user's camera and microphone.</p>
   * @see flash.media.Camera#getCamera()
   * @see flash.media.Microphone#getMicrophone()
   * @see Security#showSettings()
   *
   */
  "public static function get avHardwareDisable",function avHardwareDisable$get()/*:Boolean*/ {
    return false; // JavaScript cannot access the user's camera or microphone
  },

  /**
   * Specifies whether the system supports (<code>true</code>) or does not support (<code>false</code>) communication with accessibility aids. The server string is <code>ACC</code>.
   * @see flash.accessibility.Accessibility#active
   * @see flash.accessibility.Accessibility#updateProperties()
   *
   */
  "public static function get hasAccessibility",function hasAccessibility$get()/*:Boolean*/ {
    return false;
  },

  /**
   * Specifies whether the system has audio capabilities. This property is always <code>true</code>. The server string is <code>A</code>.
   */
  "public static function get hasAudio",function hasAudio$get()/*:Boolean*/ {
    return $$private.audio !== null;
  },

  /**
   * Specifies whether the system can (<code>true</code>) or cannot (<code>false</code>) encode an audio stream, such as that coming from a microphone. The server string is <code>AE</code>.
   */
  "public static function get hasAudioEncoder",function hasAudioEncoder$get()/*:Boolean*/ {
    return false;
  },

  /**
   * Specifies whether the system supports (<code>true</code>) or does not support (<code>false</code>) embedded video. The server string is <code>EV</code>.
   */
  "public static function get hasEmbeddedVideo",function hasEmbeddedVideo$get()/*:Boolean*/ {
    return ! !joo.getQualifiedObject('Video');
  },

  /**
   * Specifies whether the system does (<code>true</code>) or does not (<code>false</code>) have an input method editor (IME) installed. The server string is <code>IME</code>.
   * @see IME
   * @see System#ime
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cd5.html Using the IME class
   *
   */
  "public static function get hasIME",function hasIME$get()/*:Boolean*/ {
    return false; // TODO: ???
  },

  /**
   * Specifies whether the system does (<code>true</code>) or does not (<code>false</code>) have an MP3 decoder. The server string is <code>MP3</code>.
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cd8.html Using the Capabilities class
   *
   */
  "public static function get hasMP3",function hasMP3$get()/*:Boolean*/ {
    return flash.system.Capabilities.hasAudio && $$private.audio['canPlayType']("audio/mp3");
  },

  /**
   * Specifies whether the system does (<code>true</code>) or does not (<code>false</code>) support printing. The server string is <code>PR</code>.
   */
  "public static function get hasPrinting",function hasPrinting$get()/*:Boolean*/ {
    return false; // TODO: implement!
  },

  /**
   * Specifies whether the system does (<code>true</code>) or does not (<code>false</code>) support the development of screen broadcast applications to be run through Flash Media Server. The server string is <code>SB</code>.
   */
  "public static function get hasScreenBroadcast",function hasScreenBroadcast$get()/*:Boolean*/ {
    return false;
  },

  /**
   * Specifies whether the system does (<code>true</code>) or does not (<code>false</code>) support the playback of screen broadcast applications that are being run through Flash Media Server. The server string is <code>SP</code>.
   */
  "public static function get hasScreenPlayback",function hasScreenPlayback$get()/*:Boolean*/ {
    return false;
  },

  /**
   * Specifies whether the system can (<code>true</code>) or cannot (<code>false</code>) play streaming audio. The server string is <code>SA</code>.
   */
  "public static function get hasStreamingAudio",function hasStreamingAudio$get()/*:Boolean*/ {
    return false; // TODO: does the audio object know?
  },

  /**
   * Specifies whether the system can (<code>true</code>) or cannot (<code>false</code>) play streaming video. The server string is <code>SV</code>.
   */
  "public static function get hasStreamingVideo",function hasStreamingVideo$get()/*:Boolean*/ {
    return flash.system.Capabilities.hasEmbeddedVideo;
  },

  /**
   * Specifies whether the system supports native SSL sockets through NetConnection (<code>true</code>) or does not (<code>false</code>). The server string is <code>TLS</code>.
   * @see flash.net.NetConnection#connectedProxyType
   * @see flash.net.NetConnection#proxyType
   * @see flash.net.NetConnection#usingTLS
   *
   */
  "public static function get hasTLS",function hasTLS$get()/*:Boolean*/ {
    return false; // TODO: implement!
  },

  /**
   * Specifies whether the system can (<code>true</code>) or cannot (<code>false</code>) encode a video stream, such as that coming from a web camera. The server string is <code>VE</code>.
   */
  "public static function get hasVideoEncoder",function hasVideoEncoder$get()/*:Boolean*/ {
    return false;
  },

  /**
   * Specifies whether the system is a special debugging version (<code>true</code>) or an officially released version (<code>false</code>). The server string is <code>DEB</code>. This property is set to <code>true</code> when running in the debug version of Flash Player or the AIR Debug Launcher (ADL).
   */
  "public static function get isDebugger",function isDebugger$get()/*:Boolean*/ {
    return joo.debug;
  },

  /**
   * Specifies whether the Flash runtime is embedded in a PDF file that is open in Acrobat 9.0 or higher (<code>true</code>) or not (<code>false</code>).
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cd8.html Using the Capabilities class
   *
   */
  "public static function get isEmbeddedInAcrobat",function isEmbeddedInAcrobat$get()/*:Boolean*/ {
    return false;
  },

  /**
   * Specifies the language code of the system on which the content is running. The language is specified as a lowercase two-letter language code from ISO 639-1. For Chinese, an additional uppercase two-letter country code from ISO 3166 distinguishes between Simplified and Traditional Chinese. The languages codes are based on the English names of the language: for example, <code>hu</code> specifies Hungarian.
   * <p>On English systems, this property returns only the language code (<code>en</code>), not the country code. On Microsoft Windows systems, this property returns the user interface (UI) language, which refers to the language used for all menus, dialog boxes, error messages, and help files. The following table lists the possible values:</p>
   * <table>
   * <tr><th>Language</th><th>Value</th></tr>
   * <tr>
   * <td>Czech</td>
   * <td><code>cs</code></td></tr>
   * <tr>
   * <td>Danish</td>
   * <td><code>da</code></td></tr>
   * <tr>
   * <td>Dutch</td>
   * <td><code>nl</code></td></tr>
   * <tr>
   * <td>English</td>
   * <td><code>en</code></td></tr>
   * <tr>
   * <td>Finnish</td>
   * <td><code>fi</code></td></tr>
   * <tr>
   * <td>French</td>
   * <td><code>fr</code></td></tr>
   * <tr>
   * <td>German</td>
   * <td><code>de</code></td></tr>
   * <tr>
   * <td>Hungarian</td>
   * <td><code>hu</code></td></tr>
   * <tr>
   * <td>Italian</td>
   * <td><code>it</code></td></tr>
   * <tr>
   * <td>Japanese</td>
   * <td><code>ja</code></td></tr>
   * <tr>
   * <td>Korean</td>
   * <td><code>ko</code></td></tr>
   * <tr>
   * <td>Norwegian</td>
   * <td><code>no</code></td></tr>
   * <tr>
   * <td>Other/unknown</td>
   * <td><code>xu</code></td></tr>
   * <tr>
   * <td>Polish</td>
   * <td><code>pl</code></td></tr>
   * <tr>
   * <td>Portuguese</td>
   * <td><code>pt</code></td></tr>
   * <tr>
   * <td>Russian</td>
   * <td><code>ru</code></td></tr>
   * <tr>
   * <td>Simplified Chinese</td>
   * <td><code>zh-CN</code></td></tr>
   * <tr>
   * <td>Spanish</td>
   * <td><code>es</code></td></tr>
   * <tr>
   * <td>Swedish</td>
   * <td><code>sv</code></td></tr>
   * <tr>
   * <td>Traditional Chinese</td>
   * <td><code>zh-TW</code></td></tr>
   * <tr>
   * <td>Turkish</td>
   * <td><code>tr</code></td></tr></table>
   * <p><i>Note:</i> The value of <code>Capabilities.language</code> property is limited to the possible values on this list. Because of this limitation, Adobe AIR applications should use the first element in the <code>Capabilities.languages</code> array to determine the primary user interface language for the system.</p>
   * <p>The server string is <code>L</code>.</p>
   * @see #languages
   * @see http://help.adobe.com/en_US/as3/dev/WS9b644acd4ebe59993a5b57f812214f2074b-7ffd.html Choosing a locale
   *
   * @example In the following example, the content that is displayed depends on the language of the user's operating system.
   * <p>The <code>Capabilities.language</code> property returns the ISO 639-1 language code (for example, "en" for English). The <code>switch</code> statement checks for the language code and sets the content of the <code>myTextField</code> text field to a greeting specific to the language. If the language code is not supported by the example, the default error string is returned.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldAutoSize;
   *     import flash.system.Capabilities;
   *
   *     public class Capabilities_languageExample extends Sprite {
   *
   *         public function Capabilities_languageExample()  {
   *             var myTextField:TextField = new TextField();
   *             myTextField.x = 10;
   *             myTextField.y = 10;
   *             myTextField.background = true;
   *             myTextField.autoSize = TextFieldAutoSize.LEFT;
   *
   *             var greetingEnglish:String = "Hello World";
   *             var greetingGerman:String = "Hallo Welt";
   *             var greetingFrench:String = "Bonjour Monde";
   *             var greetingSpanish:String = "Hola Mundo";
   *
   *             var lang:String = Capabilities.language;
   *
   *             switch (lang) {
   *                 case "en":
   *                     myTextField.text = greetingEnglish;
   *                     break;
   *                 case "de":
   *                     myTextField.text = greetingGerman;
   *                     break;
   *                 case "fr":
   *                     myTextField.text = greetingFrench;
   *                     break;
   *                 case "es":
   *                     myTextField.text = greetingSpanish;
   *                     break;
   *                 default:
   *                     myTextField.text = "Sorry your system's language is not supported at this time.";
   *             }
   *
   *             this.addChild(myTextField);
   *         }
   *     }
   * }
   * </listing>
   */
  "public static function get language",function language$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies whether read access to the user's hard disk has been administratively prohibited (<code>true</code>) or allowed (<code>false</code>). For content in Adobe AIR, this property applies only to content in security sandboxes other than the application security sandbox. (Content in the application security sandbox can always read from the file system.) If this property is <code>true</code>, Flash Player cannot read files (including the first file that Flash Player launches with) from the user's hard disk. If this property is <code>true</code>, AIR content outside of the application security sandbox cannot read files from the user's hard disk. For example, attempts to read a file on the user's hard disk using load methods will fail if this property is set to <code>true</code>.
   * <p>Reading runtime shared libraries is also blocked if this property is set to <code>true</code>, but reading local shared objects is allowed without regard to the value of this property.</p>
   * <p>The server string is <code>LFD</code>.</p>
   * @see flash.display.Loader
   *
   */
  "public static function get localFileReadDisable",function localFileReadDisable$get()/*:Boolean*/ {
    return true;
  },

  /**
   * Specifies the manufacturer of the running version of Flash Player or the AIR runtime, in the format <code>"Adobe</code> <code><i>OSName</i>"</code>. The value for <code><i>OSName</i></code> could be <code>"Windows"</code>, <code>"Macintosh"</code>, <code>"Linux"</code>, or another operating system name. The server string is <code>M</code>.
   * <p>Do <i>not</i> use <code>Capabilities.manufacturer</code> to determine a capability based on the operating system if a more specific capability property exists. Basing a capability on the operating system is a bad idea, since it can lead to problems if an application does not consider all potential target operating systems. Instead, use the property corresponding to the capability for which you are testing. For more information, see the Capabilities class description.</p>
   */
  "public static function get manufacturer",function manufacturer$get()/*:String*/ {
    return "Jangaroo";
  },

  /**
   * Specifies the current operating system. The <code>os</code> property can return the following strings:
   * <table>
   * <tr><th>Operating system</th><th>Value</th></tr>
   * <tr>
   * <td>Windows 7</td>
   * <td><code>"Windows 7"</code></td></tr>
   * <tr>
   * <td>Windows Vista</td>
   * <td><code>"Windows Vista"</code></td></tr>
   * <tr>
   * <td>Windows Server 2008 R2</td>
   * <td><code>"Windows Server 2008 R2"</code></td></tr>
   * <tr>
   * <td>Windows Server 2008</td>
   * <td><code>"Windows Server 2008"</code></td></tr>
   * <tr>
   * <td>Windows Home Server</td>
   * <td><code>"Windows Home Server"</code></td></tr>
   * <tr>
   * <td>Windows Server 2003 R2</td>
   * <td><code>"Windows Server 2003 R2"</code></td></tr>
   * <tr>
   * <td>Windows Server 2003</td>
   * <td><code>"Windows Server 2003"</code></td></tr>
   * <tr>
   * <td>Windows XP 64</td>
   * <td><code>"Windows Server XP 64"</code></td></tr>
   * <tr>
   * <td>Windows XP</td>
   * <td><code>"Windows XP"</code></td></tr>
   * <tr>
   * <td>Windows 98</td>
   * <td><code>"Windows 98"</code></td></tr>
   * <tr>
   * <td>Windows 95</td>
   * <td><code>"Windows 95"</code></td></tr>
   * <tr>
   * <td>Windows NT</td>
   * <td><code>"Windows NT"</code></td></tr>
   * <tr>
   * <td>Windows 2000</td>
   * <td><code>"Windows 2000"</code></td></tr>
   * <tr>
   * <td>Windows ME</td>
   * <td><code>"Windows ME"</code></td></tr>
   * <tr>
   * <td>Windows CE</td>
   * <td><code>"Windows CE"</code></td></tr>
   * <tr>
   * <td>Windows SmartPhone</td>
   * <td><code>"Windows SmartPhone"</code></td></tr>
   * <tr>
   * <td>Windows PocketPC</td>
   * <td><code>"Windows PocketPC"</code></td></tr>
   * <tr>
   * <td>Windows CEPC</td>
   * <td><code>"Windows CEPC"</code></td></tr>
   * <tr>
   * <td>Windows Mobile</td>
   * <td><code>"Windows Mobile"</code></td></tr>
   * <tr>
   * <td>Mac OS</td>
   * <td><code>"Mac OS X.Y.Z"</code> (where X.Y.Z is the version number, for example: <code>"Mac OS 10.5.2"</code>)</td></tr>
   * <tr>
   * <td>Linux</td>
   * <td><code>"Linux"</code> (Flash Player attaches the Linux version, such as <code>"Linux 2.6.15-1.2054_FC5smp"</code></td></tr>
   * <tr>
   * <td>iPhone OS 4.1</td>
   * <td><code>"iPhone3,1"</code></td></tr></table>
   * <p>The server string is <code>OS</code>.</p>
   * <p>Do <i>not</i> use <code>Capabilities.os</code> to determine a capability based on the operating system if a more specific capability property exists. Basing a capability on the operating system is a bad idea, since it can lead to problems if an application does not consider all potential target operating systems. Instead, use the property corresponding to the capability for which you are testing. For more information, see the Capabilities class description.</p>
   */
  "public static function get os",function os$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies the pixel aspect ratio of the screen. The server string is <code>AR</code>.
   */
  "public static function get pixelAspectRatio",function pixelAspectRatio$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies the type of runtime environment. This property can have one of the following values:
   * <ul>
   * <li><code>"ActiveX"</code> for the Flash Player ActiveX control used by Microsoft Internet Explorer</li>
   * <li><code>"Desktop"</code> for the Adobe AIR runtime (except for SWF content loaded by an HTML page, which has <code>Capabilities.playerType</code> set to <code>"PlugIn"</code>)</li>
   * <li><code>"External"</code> for the external Flash Player or in test mode</li>
   * <li><code>"PlugIn"</code> for the Flash Player browser plug-in (and for SWF content loaded by an HTML page in an AIR application)</li>
   * <li><code>"StandAlone"</code> for the stand-alone Flash Player</li></ul>
   * <p>The server string is <code>PT</code>.</p>
   */
  "public static function get playerType",function playerType$get()/*:String*/ {
    return "PlugIn";
  },

  /**
   * Specifies the screen color. This property can have the value <code>"color"</code>, <code>"gray"</code> (for grayscale), or <code>"bw"</code> (for black and white). The server string is <code>COL</code>.
   */
  "public static function get screenColor",function screenColor$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies the dots-per-inch (dpi) resolution of the screen, in pixels. The server string is <code>DP</code>.
   */
  "public static function get screenDPI",function screenDPI$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies the maximum horizontal resolution of the screen. The server string is <code>R</code> (which returns both the width and height of the screen). This property does not update with a user's screen resolution and instead only indicates the resolution at the time Flash Player or an Adobe AIR application started. Also, the value only specifies the primary screen.
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cd8.html Using the Capabilities class
   *
   */
  "public static function get screenResolutionX",function screenResolutionX$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies the maximum vertical resolution of the screen. The server string is <code>R</code> (which returns both the width and height of the screen). This property does not update with a user's screen resolution and instead only indicates the resolution at the time Flash Player or an Adobe AIR application started. Also, the value only specifies the primary screen.
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cd8.html Using the Capabilities class
   *
   * @example The following example is a simple test that indicates the current screen resolution and operating system version. When testing this example, click the text field to see the property values:
   * <listing>
   * import flash.events.~;
   * import flash.display.~;
   * import flash.system.Capabilities;
   * import flash.text.TextField;
   *
   * var screenInfoTxt:TextField = new TextField();
   * var screenInfoTxt.x = 30;
   * var screenInfoTxt.y = 50;
   * var screenInfoTxt.width = 300;
   * var screenInfoTxt.height = 100;
   * var screenInfoTxt.border = true;
   *
   * addChild(screenInfoTxt);
   *
   * addEventListener(MouseEvent.CLICK, getScreenNVersion);
   *
   * function getScreenNVersion(e:MouseEvent):void{
   *     screenInfoTxt.text= "flash.system.Capabilities.screenResolutionX is : " + String(flash.system.Capabilities.screenResolutionX) + "\n" +
   *     "flash.system.Capabilities.screenResolutionY is : " + String(flash.system.Capabilities.screenResolutionY) + "\n" +
   *     "flash.system.Capabilities.version is : " + flash.system.Capabilities.version;
   * }
   * </listing>
   */
  "public static function get screenResolutionY",function screenResolutionY$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * A URL-encoded string that specifies values for each Capabilities property.
   * <p>The following example shows a URL-encoded string:</p>
   * <pre>A=t&SA=t&SV=t&EV=t&MP3=t&AE=t&VE=t&ACC=f&PR=t&SP=t&
   SB=f&DEB=t&V=WIN%208%2C5%2C0%2C208&M=Adobe%20Windows&
   R=1600x1200&DP=72&COL=color&AR=1.0&OS=Windows%20XP&
   L=en&PT=External&AVD=f&LFD=f&WD=f</pre>
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cd8.html Using the Capabilities class
   *
   */
  "public static function get serverString",function serverString$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies the Flash Player or Adobe<sup>®</sup> AIR<sup>®</sup> platform and version information. The format of the version number is: <i>platform majorVersion,minorVersion,buildNumber,internalBuildNumber</i>. Possible values for <i>platform</i> are <code>"WIN"</code>, ` <code>"MAC"</code>, <code>"LNX"</code>, and <code>"AND"</code>. Here are some examples of version information:
   * <pre>     WIN 9,0,0,0  // Flash Player 9 for Windows
   MAC 7,0,25,0   // Flash Player 7 for Macintosh
   LNX 9,0,115,0  // Flash Player 9 for Linux
   AND 10,2,150,0 // Flash Player 10 for Android
   </pre>
   * <p>Do <i>not</i> use <code>Capabilities.version</code> to determine a capability based on the operating system if a more specific capability property exists. Basing a capability on the operating system is a bad idea, since it can lead to problems if an application does not consider all potential target operating systems. Instead, use the property corresponding to the capability for which you are testing. For more information, see the Capabilities class description.</p>
   * <p>The server string is <code>V</code>.</p>
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cd8.html Using the Capabilities class
   *
   * @example The following example is a simple test that indicates the current screen resolution and operating system version. When testing this example, click the text field to see the property values:
   * <listing>
   * import flash.events.~;
   * import flash.display.~;
   * import flash.system.Capabilities;
   * import flash.text.TextField;
   *
   * var screenInfoTxt:TextField = new TextField();
   * var screenInfoTxt.x = 30;
   * var screenInfoTxt.y = 50;
   * var screenInfoTxt.width = 300;
   * var screenInfoTxt.height = 100;
   * var screenInfoTxt.border = true;
   *
   * addChild(screenInfoTxt);
   *
   * addEventListener(MouseEvent.CLICK, getScreenNVersion);
   *
   * function getScreenNVersion(e:MouseEvent):void{
   *     screenInfoTxt.text= "flash.system.Capabilities.screenResolutionX is : " + String(flash.system.Capabilities.screenResolutionX) + "\n" +
   *     "flash.system.Capabilities.screenResolutionY is : " + String(flash.system.Capabilities.screenResolutionY) + "\n" +
   *     "flash.system.Capabilities.version is : " + flash.system.Capabilities.version;
   * }
   * </listing>
   */
  "public static function get version",function version$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  // ************************** Jangaroo part **************************

  "private static const",{ audio/*:Object*/ :function(){return( (function flash$system$Capabilities$609_40()/*:Object*/ {
    var Audio/*:Class*/ = joo.getQualifiedObject('Audio');
    return Audio ? new Audio : null;
  })());}},

];},["avHardwareDisable","hasAccessibility","hasAudio","hasAudioEncoder","hasEmbeddedVideo","hasIME","hasMP3","hasPrinting","hasScreenBroadcast","hasScreenPlayback","hasStreamingAudio","hasStreamingVideo","hasTLS","hasVideoEncoder","isDebugger","isEmbeddedInAcrobat","language","localFileReadDisable","manufacturer","os","pixelAspectRatio","playerType","screenColor","screenDPI","screenResolutionX","screenResolutionY","serverString","version"],["Error"], "0.8.0", "0.8.3"
);