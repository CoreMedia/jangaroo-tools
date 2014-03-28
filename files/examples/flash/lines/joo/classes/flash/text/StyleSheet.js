joo.classLoader.prepare("package flash.text",/* {
import flash.events.EventDispatcher*/

/**
 * The StyleSheet class lets you create a StyleSheet object that contains text formatting rules for font size, color, and other styles. You can then apply styles defined by a style sheet to a TextField object that contains HTML- or XML-formatted text. The text in the TextField object is automatically formatted according to the tag styles defined by the StyleSheet object. You can use text styles to define new formatting tags, redefine built-in HTML tags, or create style classes that you can apply to certain HTML tags.
 * <p>To apply styles to a TextField object, assign the StyleSheet object to a TextField object's <code>styleSheet</code> property.</p>
 * <p><b>Note:</b> A text field with a style sheet is not editable. In other words, a text field with the <code>type</code> property set to <code>TextFieldType.INPUT</code> applies the StyleSheet to the default text for the text field, but the content will no longer be editable by the user. Consider using the TextFormat class to assign styles to input text fields.</p>
 * <p>Flash Player supports a subset of properties in the original CSS1 specification (<a href="http://www.w3.org/TR/REC-CSS1">www.w3.org/TR/REC-CSS1</a>). The following table shows the supported Cascading Style Sheet (CSS) properties and values, as well as their corresponding ActionScript property names. (Each ActionScript property name is derived from the corresponding CSS property name; if the name contains a hyphen, the hyphen is omitted and the subsequent character is capitalized.)</p>
 * <table>
 * <tr><th>CSS property</th><th>ActionScript property</th><th>Usage and supported values</th></tr>
 * <tr>
 * <td><code>color</code> </td>
 * <td><code>color</code> </td>
 * <td>Only hexadecimal color values are supported. Named colors (such as <code>blue</code>) are not supported. Colors are written in the following format: <code>#FF0000</code>.</td></tr>
 * <tr>
 * <td><code>display</code> </td>
 * <td><code>display</code> </td>
 * <td>Supported values are <code>inline</code>, <code>block</code>, and <code>none</code>.</td></tr>
 * <tr>
 * <td><code>font-family</code> </td>
 * <td><code>fontFamily</code> </td>
 * <td>A comma-separated list of fonts to use, in descending order of desirability. Any font family name can be used. If you specify a generic font name, it is converted to an appropriate device font. The following font conversions are available: <code>mono</code> is converted to <code>_typewriter</code>, <code>sans-serif</code> is converted to <code>_sans</code>, and <code>serif</code> is converted to <code>_serif</code>.</td></tr>
 * <tr>
 * <td><code>font-size</code> </td>
 * <td><code>fontSize</code> </td>
 * <td>Only the numeric part of the value is used. Units (px, pt) are not parsed; pixels and points are equivalent.</td></tr>
 * <tr>
 * <td><code>font-style</code> </td>
 * <td><code>fontStyle</code> </td>
 * <td>Recognized values are <code>normal</code> and <code>italic</code>.</td></tr>
 * <tr>
 * <td><code>font-weight</code> </td>
 * <td><code>fontWeight</code> </td>
 * <td>Recognized values are <code>normal</code> and <code>bold</code>.</td></tr>
 * <tr>
 * <td><code>kerning</code> </td>
 * <td><code>kerning</code> </td>
 * <td>Recognized values are <code>true</code> and <code>false</code>. Kerning is supported for embedded fonts only. Certain fonts, such as Courier New, do not support kerning. The kerning property is only supported in SWF files created in Windows, not in SWF files created on the Macintosh. However, these SWF files can be played in non-Windows versions of Flash Player and the kerning still applies.</td></tr>
 * <tr>
 * <td><code>leading</code> </td>
 * <td><code>leading</code> </td>
 * <td>The amount of space that is uniformly distributed between lines. The value specifies the number of pixels that are added after each line. A negative value condenses the space between lines. Only the numeric part of the value is used. Units (px, pt) are not parsed; pixels and points are equivalent.</td></tr>
 * <tr>
 * <td><code>letter-spacing</code> </td>
 * <td><code>letterSpacing</code> </td>
 * <td>The amount of space that is uniformly distributed between characters. The value specifies the number of pixels that are added after each character. A negative value condenses the space between characters. Only the numeric part of the value is used. Units (px, pt) are not parsed; pixels and points are equivalent.</td></tr>
 * <tr>
 * <td><code>margin-left</code> </td>
 * <td><code>marginLeft</code> </td>
 * <td>Only the numeric part of the value is used. Units (px, pt) are not parsed; pixels and points are equivalent.</td></tr>
 * <tr>
 * <td><code>margin-right</code> </td>
 * <td><code>marginRight</code> </td>
 * <td>Only the numeric part of the value is used. Units (px, pt) are not parsed; pixels and points are equivalent.</td></tr>
 * <tr>
 * <td><code>text-align</code> </td>
 * <td><code>textAlign</code> </td>
 * <td>Recognized values are <code>left</code>, <code>center</code>, <code>right</code>, and <code>justify</code>.</td></tr>
 * <tr>
 * <td><code>text-decoration</code> </td>
 * <td><code>textDecoration</code> </td>
 * <td>Recognized values are <code>none</code> and <code>underline</code>.</td></tr>
 * <tr>
 * <td><code>text-indent</code> </td>
 * <td><code>textIndent</code> </td>
 * <td>Only the numeric part of the value is used. Units (px, pt) are not parsed; pixels and points are equivalent.</td></tr></table>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/text/StyleSheet.html#includeExamplesSummary">View the examples</a></p>
 * @see TextField
 *
 */
"public dynamic class StyleSheet extends flash.events.EventDispatcher",2,function($$private){;return[ 
  /**
   * An array that contains the names (as strings) of all of the styles registered in this style sheet.
   */
  "public function get styleNames",function styleNames$get()/*:Array*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Creates a new StyleSheet object.
   * @see #getStyle()
   *
   */
  "public function StyleSheet",function StyleSheet$() {this.super$2();
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Removes all styles from the style sheet object.
   */
  "public function clear",function clear()/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns a copy of the style object associated with the style named <code>styleName</code>. If there is no style object associated with <code>styleName</code>, <code>null</code> is returned.
   * @param styleName A string that specifies the name of the style to retrieve.
   *
   * @return An object.
   *
   * @see #setStyle()
   *
   * @example <a href="http://www.adobe.com/go/learn_as3_usingexamples_en">How to use this example</a>Please see the <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/text/StyleSheet.html#parseCSS()">parseCSS()</a> or <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/text/StyleSheet.html#transform()">transform()</a> method's example for illustrations of how to use the <code>getStyle()</code> method.
   */
  "public function getStyle",function getStyle(styleName/*:String*/)/*:Object*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Parses the CSS in <code>CSSText</code> and loads the style sheet with it. If a style in <code>CSSText</code> is already in <code>styleSheet</code>, the properties in <code>styleSheet</code> are retained, and only the ones in <code>CSSText</code> are added or changed in <code>styleSheet</code>.
   * <p>To extend the native CSS parsing capability, you can override this method by creating a subclass of the StyleSheet class.</p>
   * @param CSSText The CSS text to parse (a string).
   *
   * @example In the following example, when a user clicks on the text file, CSS styles, loaded from a file, are applied to the content.
   * <p>In the constructor, a multiline text field is created and its content is set to an HTML-formatted string. (The HTML heading and span tags are not rendered before CSS style is applied.) A <code>URLRequest</code> object is created to identify the location of the CSS file; for this example, the CSS file is in the same directory as the SWF file. The file is loaded with a <code>URLLoader</code> object. There are two event listeners added for the <code>loader</code> URLLoader object. If an IO error occurs, the <code>errorHandler()</code> method is invoked, which displays an error message in the text field. After all the data is received and placed in the data property of the <code>loader</code> URLLoader object, the <code>loaderCompleteHandler()</code> method is invoked. This method parses the CSS styles from the data loaded from the file and fills the <code>sheet</code> StyleSheet object with the style definitions.</p>
   * <p>When the user clicks on the text field, the <code>clickHandler()</code> method is called. The if statement in the <code>clickHandler()</code> method checks to make sure the file loading was finished before applying the style sheet to the text field. In order for the style sheet to take effect, the <code>htmlText</code> property must be reassigned with the content after the style sheet is assigned to the text field. The CSS <code>font-family</code> and the <code>color</code> property values for the heading tag also are appended to the content of the text field. (The values of these properties will be "undefined" if style sheet values are not in effect.)</p>
   * <p>The following is an example of a content of the CSS file that can be used with this example. Before running this example, create a text file, copy the following CSS content into it, then save it with the file name <code>test.css</code> and place it in the same directory as the SWF file.</p>
   * <pre>   p {
   font-family: Times New Roman, Times, _serif;
   font-size: 14;
   font-Style: italic;
   margin-left: 10;
   }
   h1 {
   font-family: Arial, Helvetica, _sans;
   font-size: 20;
   font-weight: bold;
   }
   .bluetext {
   color: #0000CC;
   }
   </pre>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.net.URLLoader;
   *     import flash.net.URLRequest;
   *     import flash.text.StyleSheet;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldAutoSize;
   *     import flash.events.IOErrorEvent;
   *     import flash.events.Event;
   *     import flash.events.MouseEvent;
   *
   *     public class StyleSheet_parseCSSExample extends Sprite {
   *         private var loader:URLLoader = new URLLoader();
   *         private var field:TextField = new TextField();
   *         private var exampleText:String = "<h1>This is a headline</h1>"
   *                     + "<p>This is a line of text. <span class='bluetext'>"
   *                     + "This line of text is colored blue.</span></p>";
   *         private var sheet:StyleSheet = new StyleSheet();
   *         private var cssReady:Boolean = false;
   *
   *         public function StyleSheet_parseCSSExample() {
   *             field.x = 10;
   *             field.y = 10;
   *             field.background = true;
   *             field.multiline = true;
   *             field.autoSize = TextFieldAutoSize.LEFT;
   *             field.htmlText = exampleText;
   *
   *             field.addEventListener(MouseEvent.CLICK, clickHandler);
   *
   *             addChild(field);
   *
   *             var req:URLRequest = new URLRequest("test.css");
   *             loader.load(req);
   *
   *             loader.addEventListener(IOErrorEvent.IO_ERROR, errorHandler);
   *             loader.addEventListener(Event.COMPLETE, loaderCompleteHandler);
   *         }
   *
   *         public function errorHandler(e:IOErrorEvent):void {
   *             field.htmlText = "Couldn't load the style sheet file.";
   *         }
   *
   *         public function loaderCompleteHandler(event:Event):void {
   *             sheet.parseCSS(loader.data);
   *             cssReady = true;
   *         }
   *
   *         public function clickHandler(e:MouseEvent):void {
   *
   *             if (cssReady) {
   *                 field.styleSheet = sheet;
   *                 field.htmlText = exampleText;
   *
   *                 var style:Object = sheet.getStyle("h1");
   *                 field.htmlText += "<p>Headline font-family is: " + style.fontFamily + "</p>";
   *                 field.htmlText += "<p>Headline color is: " + style.color + "</p>";
   *
   *             } else {
   *                 field.htmlText = "Couldn't apply the CSS styles.";
   *             }
   *         }
   *     }
   * }
   * </listing>
   */
  "public function parseCSS",function parseCSS(CSSText/*:String*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Adds a new style with the specified name to the style sheet object. If the named style does not already exist in the style sheet, it is added. If the named style already exists in the style sheet, it is replaced. If the <code>styleObject</code> parameter is <code>null</code>, the named style is removed.
   * <p>Flash Player creates a copy of the style object that you pass to this method.</p>
   * <p>For a list of supported styles, see the table in the description for the StyleSheet class.</p>
   * @param styleName A string that specifies the name of the style to add to the style sheet.
   * @param styleObject An object that describes the style, or <code>null</code>.
   *
   */
  "public function setStyle",function setStyle(styleName/*:String*/, styleObject/*:Object*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Extends the CSS parsing capability. Advanced developers can override this method by extending the StyleSheet class.
   * @param formatObject An object that describes the style, containing style rules as properties of the object, or <code>null</code>.
   *
   * @return A TextFormat object containing the result of the mapping of CSS rules to text format properties.
   *
   * @see TextFormat
   *
   * @example This example uses the <code>transform()</code> method to apply a style from a CSS file to a TextFormat object for a text field.
   * <p>CSS styles are used usually to format HTML content. However, by using <code>transform()</code> method of a StyleSheet object, specific CSS styles can be assigned to a TextFormat object and then applied to any text field.</p>
   * <p>The <code>URLRequest</code> and <code>URLLoader</code> objects are used to load the CSS file. An event listener is added for the <code>Event.COMPLETE</code> event, which occurs after all the data is received and placed in the data property of the <code>loader</code> URLLoader object. The <code>loaderCompleteHandler()</code> method then parses the CSS from the data loaded from the file and fills the <code>sheet</code> StyleSheet object with the styles. The <code>getStyle()</code> method of the style sheet retrieves the HTML paragraph styles, which are then assigned to the <code>cssFormat</code> TextFormat object by using style sheet's <code>transform()</code> method. Finally, the default text format of the <code>inputField</code> text field is set to the new <code>cssFormat</code> text format.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.net.URLLoader;
   *     import flash.net.URLRequest;
   *     import flash.text.StyleSheet;
   *     import flash.text.TextField;
   *     import flash.text.TextFormat;
   *     import flash.text.TextFieldType;
   *     import flash.events.IOErrorEvent;
   *     import flash.events.Event;
   *
   *     public class StyleSheet_transformExample extends Sprite {
   *         private var loader:URLLoader = new URLLoader();
   *         private var inputField:TextField = new TextField();
   *         private var sheet:StyleSheet = new StyleSheet();
   *
   *         public function StyleSheet_transformExample() {
   *             inputField.x = 10;
   *             inputField.y = 10;
   *             inputField.background = true;
   *             inputField.width = 300;
   *             inputField.height = 200;
   *             inputField.wordWrap = true;
   *             inputField.multiline = true;
   *             inputField.type = TextFieldType.INPUT;
   *
   *             addChild(inputField);
   *
   *             var req:URLRequest = new URLRequest("test.css");
   *             loader.load(req);
   *
   *             loader.addEventListener(IOErrorEvent.IO_ERROR, errorHandler);
   *             loader.addEventListener(Event.COMPLETE, loaderCompleteHandler);
   *         }
   *
   *         public function errorHandler(e:IOErrorEvent):void {
   *             inputField.htmlText = "Couldn't load the style sheet file.";
   *         }
   *
   *         public function loaderCompleteHandler(event:Event):void {
   *             var cssFormat:TextFormat = new TextFormat();
   *             sheet.parseCSS(loader.data);
   *             var style:Object = sheet.getStyle("p");
   *             cssFormat = sheet.transform(style);
   *             inputField.defaultTextFormat = cssFormat;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function transform",function transform(formatObject/*:Object*/)/*:TextFormat*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.1"
);