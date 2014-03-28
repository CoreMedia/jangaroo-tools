joo.classLoader.prepare("package flash.text",/* {
import flash.display.DisplayObject
import flash.display.Graphics
import flash.display.InteractiveObject

import flash.geom.Rectangle

import js.CanvasRenderingContext2D
import js.HTMLCanvasElement
import js.HTMLElement*/

/**
 * Dispatched after a control value is modified, unlike the <code>textInput</code> event, which is dispatched before the value is modified. Unlike the W3C DOM Event Model version of the <code>change</code> event, which dispatches the event only after the control loses focus, the ActionScript 3.0 version of the <code>change</code> event is dispatched any time the control changes. For example, if a user types text into a text field, a <code>change</code> event is dispatched after every keystroke.
 * @eventType flash.events.Event.CHANGE
 */
{Event:{name:"change", type:"flash.events.Event"}},
/**
 * Dispatched when a user clicks a hyperlink in an HTML-enabled text field, where the URL begins with "event:". The remainder of the URL after "event:" is placed in the text property of the LINK event.
 * <p><b>Note:</b> The default behavior, adding the text to the text field, occurs only when Flash Player generates the event, which in this case happens when a user attempts to input text. You cannot put text into a text field by sending it <code>textInput</code> events.</p>
 * @eventType flash.events.TextEvent.LINK
 */
{Event:{name:"link", type:"flash.events.TextEvent"}},
/**
 * Dispatched by a TextField object <i>after</i> the user scrolls.
 * @eventType flash.events.Event.SCROLL
 */
{Event:{name:"scroll", type:"flash.events.Event"}},
/**
 * Flash Player dispatches the <code>textInput</code> event when a user enters one or more characters of text. Various text input methods can generate this event, including standard keyboards, input method editors (IMEs), voice or speech recognition systems, and even the act of pasting plain text with no formatting or style information.
 * @eventType flash.events.TextEvent.TEXT_INPUT
 */
{Event:{name:"textInput", type:"flash.events.TextEvent"}},

/**
 * The TextField class is used to create display objects for text display and input. You can give a text field an instance name in the Property inspector and use the methods and properties of the TextField class to manipulate it with ActionScript. TextField instance names are displayed in the Movie Explorer and in the Insert Target Path dialog box in the Actions panel.
 * <p>To create a text field dynamically, use the <code>TextField()</code> constructor.</p>
 * <p>The methods of the TextField class let you set, select, and manipulate text in a dynamic or input text field that you create during authoring or at runtime.</p>
 * <p>ActionScript provides several ways to format your text at runtime. The TextFormat class lets you set character and paragraph formatting for TextField objects. You can apply Cascading Style Sheets (CSS) styles to text fields by using the <code>TextField.styleSheet</code> property and the StyleSheet class. You can use CSS to style built-in HTML tags, define new formatting tags, or apply styles. You can assign HTML formatted text, which optionally uses CSS styles, directly to a text field. HTML text that you assign to a text field can contain embedded media (movie clips, SWF files, GIF files, PNG files, and JPEG files). The text wraps around the embedded media in the same way that a web browser wraps text around media embedded in an HTML document.</p>
 * <p>Flash Player supports a subset of HTML tags that you can use to format text. See the list of supported HTML tags in the description of the <code>htmlText</code> property.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/text/TextField.html#includeExamplesSummary">View the examples</a></p>
 * @see TextFormat
 * @see StyleSheet
 * @see #htmlText
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e3c.html Core display classes
 * @see http://help.adobe.com/en_US/as3/dev/WSb2ba3b1aad8a27b07258e35912218ac0e60-8000.html Using the TextField class
 *
 */
"public class TextField extends flash.display.InteractiveObject",4,function($$private){;return[function(){joo.classLoader.init(flash.text.TextFormatAlign);}, 

  /**
   * When set to <code>true</code> and the text field is not in focus, Flash Player highlights the selection in the text field in gray. When set to <code>false</code> and the text field is not in focus, Flash Player does not highlight the selection in the text field.
   * <p>The default value is <code>false.</code></p>
   * @see flash.display.Stage#focus
   *
   * @example Compile and run the following file. When you run the file, drag to select text in each of the two text fields, and notice the difference in selection highlighting when you select text in the two text fields (changing focus):
   * <listing>
   *     package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldType;
   *
   *     public class TextField_alwaysShowSelection extends Sprite {
   *         public function TextField_alwaysShowSelection() {
   *             var label1:TextField = createCustomTextField(0, 20, 200, 20);
   *             label1.text = "This text is selected.";
   *             label1.setSelection(0, 9);
   *             label1.alwaysShowSelection = true;
   *
   *             var label2:TextField = createCustomTextField(0, 50, 200, 20);
   *             label2.text = "Drag to select some of this text.";
   *         }
   *
   *         private function createCustomTextField(x:Number, y:Number, width:Number, height:Number):TextField {
   *             var result:TextField = new TextField();
   *             result.x = x; result.y = y;
   *             result.width = width; result.height = height;
   *             addChild(result);
   *             return result;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function get alwaysShowSelection",function alwaysShowSelection$get()/*:Boolean*/ {
    return this._alwaysShowSelection$4;
  },

  /**
   * @private
   */
  "public function set alwaysShowSelection",function alwaysShowSelection$set(value/*:Boolean*/)/*:void*/ {
    this._alwaysShowSelection$4 = value;
  },

  /**
   * The type of anti-aliasing used for this text field. Use <code>flash.text.AntiAliasType</code> constants for this property. You can control this setting only if the font is embedded (with the <code>embedFonts</code> property set to <code>true</code>). The default setting is <code>flash.text.AntiAliasType.NORMAL</code>.
   * <p>To set values for this property, use the following string values:</p>
   * <table>
   * <tr><th>String value</th><th>Description</th></tr>
   * <tr>
   * <td><code>flash.text.AntiAliasType.NORMAL</code></td>
   * <td>Applies the regular text anti-aliasing. This value matches the type of anti-aliasing that Flash Player 7 and earlier versions used.</td></tr>
   * <tr>
   * <td><code>flash.text.AntiAliasType.ADVANCED</code></td>
   * <td>Applies advanced anti-aliasing, which makes text more legible. (This feature became available in Flash Player 8.) Advanced anti-aliasing allows for high-quality rendering of font faces at small sizes. It is best used with applications with a lot of small text. Advanced anti-aliasing is not recommended for fonts that are larger than 48 points.</td></tr></table>
   * @see AntiAliasType
   * @see #embedFonts
   *
   */
  "public function get antiAliasType",function antiAliasType$get()/*:String*/ {
    return this._antiAliasType$4;
  },

  /**
   * @private
   */
  "public function set antiAliasType",function antiAliasType$set(value/*:String*/)/*:void*/ {
    this._antiAliasType$4 = value;
  },

  /**
   * Controls automatic sizing and alignment of text fields. Acceptable values for the <code>TextFieldAutoSize</code> constants: <code>TextFieldAutoSize.NONE</code> (the default), <code>TextFieldAutoSize.LEFT</code>, <code>TextFieldAutoSize.RIGHT</code>, and <code>TextFieldAutoSize.CENTER</code>.
   * <p>If <code>autoSize</code> is set to <code>TextFieldAutoSize.NONE</code> (the default) no resizing occurs.</p>
   * <p>If <code>autoSize</code> is set to <code>TextFieldAutoSize.LEFT</code>, the text is treated as left-justified text, meaning that the left margin of the text field remains fixed and any resizing of a single line of the text field is on the right margin. If the text includes a line break (for example, <code>"\n"</code> or <code>"\r"</code>), the bottom is also resized to fit the next line of text. If <code>wordWrap</code> is also set to <code>true</code>, only the bottom of the text field is resized and the right side remains fixed.</p>
   * <p>If <code>autoSize</code> is set to <code>TextFieldAutoSize.RIGHT</code>, the text is treated as right-justified text, meaning that the right margin of the text field remains fixed and any resizing of a single line of the text field is on the left margin. If the text includes a line break (for example, <code>"\n" or "\r")</code>, the bottom is also resized to fit the next line of text. If <code>wordWrap</code> is also set to <code>true</code>, only the bottom of the text field is resized and the left side remains fixed.</p>
   * <p>If <code>autoSize</code> is set to <code>TextFieldAutoSize.CENTER</code>, the text is treated as center-justified text, meaning that any resizing of a single line of the text field is equally distributed to both the right and left margins. If the text includes a line break (for example, <code>"\n"</code> or <code>"\r"</code>), the bottom is also resized to fit the next line of text. If <code>wordWrap</code> is also set to <code>true</code>, only the bottom of the text field is resized and the left and right sides remain fixed.</p>
   * @throws ArgumentError The <code>autoSize</code> specified is not a member of flash.text.TextFieldAutoSize.
   *
   * @see TextFieldAutoSize
   * @see #autoSize
   * @see #wordWrap
   *
   */
  "public function get autoSize",function autoSize$get()/*:String*/ {
    return this._autoSize$4;
  },

  /**
   * @private
   */
  "public function set autoSize",function autoSize$set(value/*:String*/)/*:void*/ {
    this._autoSize$4 = value;
  },

  /**
   * Specifies whether the text field has a background fill. If <code>true</code>, the text field has a background fill. If <code>false</code>, the text field has no background fill. Use the <code>backgroundColor</code> property to set the background color of a text field.
   * <p>The default value is <code>false.</code></p>
   * @see #backgroundColor
   *
   */
  "public function get background",function background$get()/*:Boolean*/ {
    return this._background$4;
  },

  /**
   * @private
   */
  "public function set background",function background$set(value/*:Boolean*/)/*:void*/ {
    this._background$4 = value;
  },

  /**
   * The color of the text field background. The default value is <code>0xFFFFFF</code> (white). This property can be retrieved or set, even if there currently is no background, but the color is visible only if the text field has the <code>background</code> property set to <code>true</code>.
   * @see #background
   *
   */
  "public function get backgroundColor",function backgroundColor$get()/*:uint*/ {
    return this._backgroundColor$4;
  },

  /**
   * @private
   */
  "public function set backgroundColor",function backgroundColor$set(value/*:uint*/)/*:void*/ {
    this._backgroundColor$4 = value;
    $$private.updateElementProperty(this.getElement(), "style.backgroundColor", flash.display.Graphics.toRGBA(value));
  },

  /**
   * Specifies whether the text field has a border. If <code>true</code>, the text field has a border. If <code>false</code>, the text field has no border. Use the <code>borderColor</code> property to set the border color.
   * <p>The default value is <code>false.</code></p>
   * @see #borderColor
   *
   */
  "public function get border",function border$get()/*:Boolean*/ {
    return this._border$4;
  },

  /**
   * @private
   */
  "public function set border",function border$set(value/*:Boolean*/)/*:void*/ {
    this._border$4 = value;
    $$private.updateElementProperty(this.getElement(), "style.borderWidth", value ? "1px" : "0");
  },

  /**
   * The color of the text field border. The default value is <code>0x000000</code> (black). This property can be retrieved or set, even if there currently is no border, but the color is visible only if the text field has the <code>border</code> property set to <code>true</code>.
   * @see #border
   *
   */
  "public function get borderColor",function borderColor$get()/*:uint*/ {
    return this._borderColor$4;
  },

  /**
   * @private
   */
  "public function set borderColor",function borderColor$set(value/*:uint*/)/*:void*/ {
    this._borderColor$4 = value;
    $$private.updateElementProperty(this.getElement(), "style.borderColor", flash.display.Graphics.toRGBA(value));
  },

  /**
   * An integer (1-based index) that indicates the bottommost line that is currently visible in the specified text field. Think of the text field as a window onto a block of text. The <code>scrollV</code> property is the 1-based index of the topmost visible line in the window.
   * <p>All the text between the lines indicated by <code>scrollV</code> and <code>bottomScrollV</code> is currently visible in the text field.</p>
   * @see #scrollV
   *
   */
  "public function get bottomScrollV",function bottomScrollV$get()/*:int*/ {
    return this._bottomScrollV$4;
  },

  /**
   * The index of the insertion point (caret) position. If no insertion point is displayed, the value is the position the insertion point would be if you restored focus to the field (typically where the insertion point last was, or 0 if the field has not had focus).
   * <p>Selection span indexes are zero-based (for example, the first position is 0, the second position is 1, and so on).</p>
   * @see #selectable
   * @see #selectionBeginIndex
   * @see #selectionEndIndex
   *
   * @example In this example, a TextField instance is created and populated with text. An event listener is assigned so that when the user clicks on the TextField, the <code>printCursorPosition</code> method is called. In that case, the values of the <code>caretIndex</code>, <code>selectionBeginIndex</code>, and <code>selectionEndIndex</code> properties are output.
   * <p>Run this example and try clicking in the TextField to select text. Then click in the field without selecting text. When you click in the text without making a selection, the <code>caretIndex</code> property indicates where the insertion point occurs, and the <code>selectionBeginIndex</code> and <code>selectionEndIndex</code> properties equal the <code>caretIndex</code> property value.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.events.MouseEvent;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldType;
   *
   *     public class TextField_caretIndex extends Sprite {
   *         public function TextField_caretIndex() {
   *             var tf:TextField = createCustomTextField(10, 10, 100, 100);
   *             tf.wordWrap = true;
   *             tf.type = TextFieldType.INPUT;
   *             tf.text = "Click in this text field. Compare the difference between clicking without selecting versus clicking and selecting text.";
   *             tf.addEventListener(MouseEvent.CLICK, printCursorPosition);
   *         }
   *
   *         private function printCursorPosition(event:MouseEvent):void {
   *             var tf:TextField = TextField(event.target);
   *             trace("caretIndex:", tf.caretIndex);
   *             trace("selectionBeginIndex:", tf.selectionBeginIndex);
   *             trace("selectionEndIndex:", tf.selectionEndIndex);
   *         }
   *
   *         private function createCustomTextField(x:Number, y:Number, width:Number, height:Number):TextField {
   *             var result:TextField = new TextField();
   *             result.x = x;
   *             result.y = y;
   *             result.width = width;
   *             result.height = height;
   *             addChild(result);
   *             return result;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function get caretIndex",function caretIndex$get()/*:int*/ {
    return this._caretIndex$4;
  },

  /**
   * A Boolean value that specifies whether extra white space (spaces, line breaks, and so on) in a text field with HTML text is removed. The default value is <code>false</code>. The <code>condenseWhite</code> property only affects text set with the <code>htmlText</code> property, not the <code>text</code> property. If you set text with the <code>text</code> property, <code>condenseWhite</code> is ignored.
   * <p>If <code>condenseWhite</code> is set to <code>true</code>, use standard HTML commands such as <code><BR></code> and <code><P></code> to place line breaks in the text field.</p>
   * <p>Set the <code>condenseWhite</code> property before setting the <code>htmlText</code> property.</p>
   * @see #htmlText
   *
   * @example The following shows the difference between setting the <code>condenseWhite</code> setting to <code>false</code> and setting it to <code>true</code>:
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *
   *     public class TextField_condenseWhite extends Sprite {
   *         public function TextField_condenseWhite() {
   *             var tf1:TextField = createCustomTextField(0, 0, 200, 50);
   *             tf1.condenseWhite = false;
   *             tf1.htmlText = "keep    on\n\ttruckin'";
   *
   *             var tf2:TextField = createCustomTextField(0, 120, 200, 50);
   *             tf2.condenseWhite = true;
   *             tf2.htmlText = "keep    on\n\ttruckin'";
   *         }
   *
   *         private function createCustomTextField(x:Number, y:Number, width:Number, height:Number):TextField {
   *             var result:TextField = new TextField();
   *             result.x = x;
   *             result.y = y;
   *             result.width = width;
   *             result.height = height;
   *             result.border = true;
   *             addChild(result);
   *             return result;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function get condenseWhite",function condenseWhite$get()/*:Boolean*/ {
    return this._condenseWhite$4;
  },

  /**
   * @private
   */
  "public function set condenseWhite",function condenseWhite$set(value/*:Boolean*/)/*:void*/ {
    this._condenseWhite$4 = value;
  },

  /**
   * Specifies the format applied to newly inserted text, such as text entered by a user or text inserted with the <code>replaceSelectedText()</code> method.
   * <p><b>Note:</b> When selecting characters to be replaced with <code>setSelection()</code> and <code>replaceSelectedText()</code>, the <code>defaultTextFormat</code> will be applied only if the text has been selected up to and including the last character. Here is an example:</p>
   * <pre>     var my_txt:TextField new TextField();
   my_txt.text = "Flash Macintosh version";
   var my_fmt:TextFormat = new TextFormat();
   my_fmt.color = 0xFF0000;
   my_txt.defaultTextFormat = my_fmt;
   my_txt.setSelection(6,15); // partial text selected - defaultTextFormat not applied
   my_txt.setSelection(6,23); // text selected to end - defaultTextFormat applied
   my_txt.replaceSelectedText("Windows version");
   </pre>
   * <p>When you access the <code>defaultTextFormat</code> property, the returned TextFormat object has all of its properties defined. No property is <code>null</code>.</p>
   * <p><b>Note:</b> You can't set this property if a style sheet is applied to the text field.</p>
   * @throws Error This method cannot be used on a text field with a style sheet.
   *
   * @see #replaceSelectedText()
   * @see #getTextFormat()
   * @see #setTextFormat()
   *
   */
  "public function get defaultTextFormat",function defaultTextFormat$get()/*:TextFormat*/ {
    return this._defaultTextFormat$4;
  },

  /**
   * @private
   */
  "public function set defaultTextFormat",function defaultTextFormat$set(value/*:TextFormat*/)/*:void*/ {
    for (var property/*:String*/ in value) {
      if (value.hasOwnProperty(property)) {
        var val/*:**/ = value[property];
        if (typeof val !== "function" && val !== null && val !== "") {
          this._defaultTextFormat$4[property] = value[property];
        }
      }
    }
    if (this.hasElement()) {
      this.syncTextFormat$4(this.getElement());
    }
  },

  /**
   * Specifies whether the text field is a password text field. If the value of this property is <code>true</code>, the text field is treated as a password text field and hides the input characters using asterisks instead of the actual characters. If <code>false</code>, the text field is not treated as a password text field. When password mode is enabled, the Cut and Copy commands and their corresponding keyboard shortcuts will not function. This security mechanism prevents an unscrupulous user from using the shortcuts to discover a password on an unattended computer.
   * <p>The default value is <code>false.</code></p>
   */
  "public function get displayAsPassword",function displayAsPassword$get()/*:Boolean*/ {
    return this._displayAsPassword$4;
  },

  /**
   * @private
   */
  "public function set displayAsPassword",function displayAsPassword$set(value/*:Boolean*/)/*:void*/ {
    this._displayAsPassword$4 = value;
  },

  /**
   * Specifies whether to render by using embedded font outlines. If <code>false</code>, Flash Player renders the text field by using device fonts.
   * <p>If you set the <code>embedFonts</code> property to <code>true</code> for a text field, you must specify a font for that text by using the <code>font</code> property of a TextFormat object applied to the text field. If the specified font is not embedded in the SWF file, the text is not displayed.</p>
   * <p>The default value is <code>false.</code></p>
   * @see Font#enumerateFonts()
   *
   */
  "public function get embedFonts",function embedFonts$get()/*:Boolean*/ {
    return this._embedFonts$4;
  },

  /**
   * @private
   */
  "public function set embedFonts",function embedFonts$set(value/*:Boolean*/)/*:void*/ {
    this._embedFonts$4 = value;
  },

  /**
   * The type of grid fitting used for this text field. This property applies only if the <code>flash.text.AntiAliasType</code> property of the text field is set to <code>flash.text.AntiAliasType.ADVANCED</code>.
   * <p>The type of grid fitting used determines whether Flash Player forces strong horizontal and vertical lines to fit to a pixel or subpixel grid, or not at all.</p>
   * <p>For the <code>flash.text.GridFitType</code> property, you can use the following string values:</p>
   * <table>
   * <tr><th>String value</th><th>Description</th></tr>
   * <tr>
   * <td><code>flash.text.GridFitType.NONE</code></td>
   * <td>Specifies no grid fitting. Horizontal and vertical lines in the glyphs are not forced to the pixel grid. This setting is recommended for animation or for large font sizes.</td></tr>
   * <tr>
   * <td><code>flash.text.GridFitType.PIXEL</code></td>
   * <td>Specifies that strong horizontal and vertical lines are fit to the pixel grid. This setting works only for left-aligned text fields. To use this setting, the <code>flash.dispaly.AntiAliasType</code> property of the text field must be set to <code>flash.text.AntiAliasType.ADVANCED</code>. This setting generally provides the best legibility for left-aligned text.</td></tr>
   * <tr>
   * <td><code>flash.text.GridFitType.SUBPIXEL</code></td>
   * <td>Specifies that strong horizontal and vertical lines are fit to the subpixel grid on an LCD monitor. To use this setting, the <code>flash.text.AntiAliasType</code> property of the text field must be set to <code>flash.text.AntiAliasType.ADVANCED</code>. The <code>flash.text.GridFitType.SUBPIXEL</code> setting is often good for right-aligned or centered dynamic text, and it is sometimes a useful trade-off for animation versus text quality.</td></tr></table>
   * <p>The default value is <code>pixel.</code></p>
   * @see GridFitType
   * @see #antiAliasType
   * @see AntiAliasType
   *
   * @example The following example shows three text fields with different settings for the <code>gridFitType</code> property. When you use this example, notice the difference in legibility for the first two lines. Also note the optimal use of <code>GridFitType.PIXEL</code> for left-aligned text and <code>GridFitType.SUBPIXEL</code> for right-aligned text.
   * <listing>
   * package
   * {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.text.TextFormat;
   *     import flash.text.TextFieldAutoSize;
   *     import flash.text.AntiAliasType;
   *     import flash.text.GridFitType;
   *
   *     public class gridFitTypeExample extends Sprite
   *     {
   *         public function gridFitTypeExample()
   *         {
   *     var format1:TextFormat = new TextFormat();
   *     format1.font="Arial";
   *     format1.size=12;
   *
   *     var tf1:TextField = createCustomTextField(0,0,format1,"NONE",TextFieldAutoSize.LEFT,GridFitType.NONE);
   *
   *     var tf2:TextField = createCustomTextField(0,30,format1,"PIXEL",TextFieldAutoSize.LEFT,GridFitType.PIXEL);
   *
   *     var tf3:TextField = createCustomTextField(300,60,format1,"SUBPIXEL",TextFieldAutoSize.RIGHT,GridFitType.SUBPIXEL);
   *
   *         }
   *         private function createCustomTextField(x:Number,y:Number,fm:TextFormat,tl:String,tfs:String,gft:String):TextField
   *         {
   *             var result:TextField = new TextField();
   *             result.x=x;
   *             result.y=y;
   *             result.embedFonts=true;
   *             result.antiAliasType=AntiAliasType.ADVANCED;
   *             result.text="This text uses a gridFitType of " + tl;
   *             result.autoSize=tfs;
   *         result.gridFitType=gft;
   *             result.setTextFormat(fm);
   *             addChild(result);
   *             return result;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function get gridFitType",function gridFitType$get()/*:String*/ {
    return this._gridFitType$4;
  },

  /**
   * @private
   */
  "public function set gridFitType",function gridFitType$set(value/*:String*/)/*:void*/ {
    this._gridFitType$4 = value;
  },

  /**
   * Contains the HTML representation of the text field contents.
   * <p>Flash Player supports the following HTML tags:</p>
   * <table>
   * <tr><th>Tag</th><th>Description</th></tr>
   * <tr>
   * <td>Anchor tag</td>
   * <td>The <code><a></code> tag creates a hypertext link and supports the following attributes:
   * <ul>
   * <li><code>target</code>: Specifies the name of the target window where you load the page. Options include <code>_self</code>, <code>_blank</code>, <code>_parent</code>, and <code>_top</code>. The <code>_self</code> option specifies the current frame in the current window, <code>_blank</code> specifies a new window, <code>_parent</code> specifies the parent of the current frame, and <code>_top</code> specifies the top-level frame in the current window.</li>
   * <li><code>href</code>: Specifies a URL or an ActionScript <code>link</code> event.The URL can be either absolute or relative to the location of the SWF file that is loading the page. An example of an absolute reference to a URL is <code>http://www.adobe.com</code>; an example of a relative reference is <code>/index.html</code>. Absolute URLs must be prefixed with http://; otherwise, Flash Player or AIR treats them as relative URLs. You can use the <code>link</code> event to cause the link to execute an ActionScript function in a SWF file instead of opening a URL. To specify a <code>link</code> event, use the event scheme instead of the http scheme in your <code>href</code> attribute. An example is <code>href="event:myText"</code> instead of <code>href="http://myURL"</code>; when the user clicks a hypertext link that contains the event scheme, the text field dispatches a <code>link</code> TextEvent with its <code>text</code> property set to "<code>myText</code>". You can then create an ActionScript function that executes whenever the link TextEvent is dispatched. You can also define <code>a:link</code>, <code>a:hover</code>, and <code>a:active</code> styles for anchor tags by using style sheets.</li></ul></td></tr>
   * <tr>
   * <td>Bold tag</td>
   * <td>The <code><b></code> tag renders text as bold. A bold typeface must be available for the font used.</td></tr>
   * <tr>
   * <td>Break tag</td>
   * <td>The <code><br></code> tag creates a line break in the text field. Set the text field to be a multiline text field to use this tag.</td></tr>
   * <tr>
   * <td>Font tag</td>
   * <td>The <code><font></code> tag specifies a font or list of fonts to display the text.The font tag supports the following attributes:
   * <ul>
   * <li><code>color</code>: Only hexadecimal color (<code>#FFFFFF</code>) values are supported.</li>
   * <li><code>face</code>: Specifies the name of the font to use. As shown in the following example, you can specify a list of comma-delimited font names, in which case Flash Player selects the first available font. If the specified font is not installed on the local computer system or isn't embedded in the SWF file, Flash Player selects a substitute font.</li>
   * <li><code>size</code>: Specifies the size of the font. You can use absolute pixel sizes, such as 16 or 18, or relative point sizes, such as +2 or -4.</li></ul></td></tr>
   * <tr>
   * <td>Image tag</td>
   * <td>The <code><img></code> tag lets you embed external image files (JPEG, GIF, PNG), SWF files, and movie clips inside text fields. Text automatically flows around images you embed in text fields. You must set the text field to be multiline to wrap text around an image.
   * <p>The <code><img></code> tag supports the following attributes:</p>
   * <ul>
   * <li><code>src</code>: Specifies the URL to an image or SWF file, or the linkage identifier for a movie clip symbol in the library. This attribute is required; all other attributes are optional. External files (JPEG, GIF, PNG, and SWF files) do not show until they are downloaded completely.</li>
   * <li><code>width</code>: The width of the image, SWF file, or movie clip being inserted, in pixels.</li>
   * <li><code>height</code>: The height of the image, SWF file, or movie clip being inserted, in pixels.</li>
   * <li><code>align</code>: Specifies the horizontal alignment of the embedded image within the text field. Valid values are <code>left</code> and <code>right</code>. The default value is <code>left</code>.</li>
   * <li><code>hspace</code>: Specifies the amount of horizontal space that surrounds the image where no text appears. The default value is 8.</li>
   * <li><code>vspace</code>: Specifies the amount of vertical space that surrounds the image where no text appears. The default value is 8.</li>
   * <li><code>id</code>: Specifies the name for the movie clip instance (created by Flash Player) that contains the embedded image file, SWF file, or movie clip. This approach is used to control the embedded content with ActionScript.</li>
   * <li><code>checkPolicyFile</code>: Specifies that Flash Player checks for a URL policy file on the server associated with the image domain. If a policy file exists, SWF files in the domains listed in the file can access the data of the loaded image, for example, by calling the <code>BitmapData.draw()</code> method with this image as the <code>source</code> parameter. For more information related to security, see the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</li></ul>
   * <p>Flash displays media embedded in a text field at full size. To specify the dimensions of the media you are embedding, use the <code><img></code> tag <code>height</code> and <code>width</code> attributes.</p>
   * <p>In general, an image embedded in a text field appears on the line following the <code><img></code> tag. However, when the <code><img></code> tag is the first character in the text field, the image appears on the first line of the text field.</p>
   * <p>For AIR content in the application security sandbox, AIR ignores <code>img</code> tags in HTML content in ActionScript TextField objects. This is to prevent possible phishing attacks,</p></td></tr>
   * <tr>
   * <td>Italic tag</td>
   * <td>The <code><i></code> tag displays the tagged text in italics. An italic typeface must be available for the font used.</td></tr>
   * <tr>
   * <td>List item tag</td>
   * <td>The <code><li></code> tag places a bullet in front of the text that it encloses. <b>Note:</b> Because Flash Player and AIR do not recognize ordered and unordered list tags (<code><ol></code> and <code><ul></code>, they do not modify how your list is rendered. All lists are unordered and all list items use bullets.</td></tr>
   * <tr>
   * <td>Paragraph tag</td>
   * <td>The <code><p></code> tag creates a new paragraph. The text field must be set to be a multiline text field to use this tag. The <code><p></code> tag supports the following attributes:
   * <ul>
   * <li>align: Specifies alignment of text within the paragraph; valid values are <code>left</code>, <code>right</code>, <code>justify</code>, and <code>center</code>.</li>
   * <li>class: Specifies a CSS style class defined by a flash.text.StyleSheet object.</li></ul></td></tr>
   * <tr>
   * <td>Span tag</td>
   * <td>The <code><span></code> tag is available only for use with CSS text styles. It supports the following attribute:
   * <ul>
   * <li>class: Specifies a CSS style class defined by a flash.text.StyleSheet object.</li></ul></td></tr>
   * <tr>
   * <td>Text format tag</td>
   * <td>
   * <p>The <code><textformat></code> tag lets you use a subset of paragraph formatting properties of the TextFormat class within text fields, including line leading, indentation, margins, and tab stops. You can combine <code><textformat></code> tags with the built-in HTML tags.</p>
   * <p>The <code><textformat></code> tag has the following attributes:</p>
   * <ul>
   * <li><code>blockindent</code>: Specifies the block indentation in points; corresponds to <code>TextFormat.blockIndent</code>.</li>
   * <li><code>indent</code>: Specifies the indentation from the left margin to the first character in the paragraph; corresponds to <code>TextFormat.indent</code>. Both positive and negative numbers are acceptable.</li>
   * <li><code>leading</code>: Specifies the amount of leading (vertical space) between lines; corresponds to <code>TextFormat.leading</code>. Both positive and negative numbers are acceptable.</li>
   * <li><code>leftmargin</code>: Specifies the left margin of the paragraph, in points; corresponds to <code>TextFormat.leftMargin</code>.</li>
   * <li><code>rightmargin</code>: Specifies the right margin of the paragraph, in points; corresponds to <code>TextFormat.rightMargin</code>.</li>
   * <li><code>tabstops</code>: Specifies custom tab stops as an array of non-negative integers; corresponds to <code>TextFormat.tabStops</code>.</li></ul></td></tr>
   * <tr>
   * <td>Underline tag</td>
   * <td>The <code><u></code> tag underlines the tagged text.</td></tr></table>
   * <p>Flash Player and AIR support the following HTML entities:</p>
   * <table>
   * <tr><th>Entity</th><th>Description</th></tr>
   * <tr>
   * <td>&lt;</td>
   * <td>< (less than)</td></tr>
   * <tr>
   * <td>&gt;</td>
   * <td>> (greater than)</td></tr>
   * <tr>
   * <td>&amp;</td>
   * <td>& (ampersand)</td></tr>
   * <tr>
   * <td>&quot;</td>
   * <td>" (double quotes)</td></tr>
   * <tr>
   * <td>&apos;</td>
   * <td>' (apostrophe, single quote)</td></tr></table>
   * <p>Flash Player and AIR also support explicit character codes, such as &#38; (ASCII ampersand) and &#x20AC; (Unicode â‚¬ symbol).</p>
   * @see #text
   * @see StyleSheet
   * @see flash.events.TextEvent
   *
   * @example The following example creates a TextField called <code>tf1</code>, and assigns an HTML-formatted String to its <code>text</code> property. When its <code>htmlText</code> property is traced, the output is the HTML-formatted String, with additional tags (such as <P> and <FONT>) automatically added by Flash Player. When the value of the <code>text</code> property is traced, the unformatted string without HTML tags is displayed.
   * <p>By way of comparison, the same steps are performed on another TextField object named <code>tf2</code>, with the addition that a StyleSheet object is assigned to <code>tf2</code>'s <code>styleSheet</code> property before its <code>htmlText</code> property is set. In that case, when the <code>htmlText</code> property is traced, it only includes the exact HTML text that was originally assigned to the <code>htmlText</code> property, showing that no additional tags were added by Flash Player.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.StyleSheet;
   *     import flash.text.TextField;
   *
   *     public class TextField_text extends Sprite {
   *         public function TextField_text() {
   *             var tf1:TextField = createCustomTextField(10, 10, 400, 22);
   *             tf1.htmlText = "<b>Lorem ipsum dolor sit amet.</b>";
   *
   *             // htmlText: <P ALIGN="LEFT"><FONT FACE="Times New Roman" SIZE="12" COLOR="#000000" LETTERSPACING="0" KERNING="0"><b>Lorem ipsum dolor sit amet.</b></FONT></P>
   *             trace("htmlText: " + tf1.htmlText);
   *             // text: Lorem ipsum dolor sit amet.
   *             trace("text: " + tf1.text);
   *
   *             var tf2:TextField = createCustomTextField(10, 50, 400, 22);
   *             tf2.styleSheet = new StyleSheet();
   *             tf2.htmlText = "<b>Lorem ipsum dolor sit amet.</b>";
   *             // htmlText: <b>Lorem ipsum dolor sit amet.</b>
   *             trace("htmlText: " + tf2.htmlText);
   *             // text: Lorem ipsum dolor sit amet.
   *             trace("text: " + tf2.text);
   *         }
   *
   *         private function createCustomTextField(x:Number, y:Number, width:Number, height:Number):TextField {
   *             var result:TextField = new TextField();
   *             result.x = x;
   *             result.y = y;
   *             result.width = width;
   *             result.height = height;
   *             addChild(result);
   *             return result;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function get htmlText",function htmlText$get()/*:String*/ {
    return this._htmlText$4;
  },

  /**
   * @private
   */
  "public function set htmlText",function htmlText$set(value/*:String*/)/*:void*/ {
    this._htmlText$4 = value;
    $$private.updateElementProperty(this.getElement(), "innerHTML", value);
  },

  /**
   * The number of characters in a text field. A character such as tab (<code>\t</code>) counts as one character.
   */
  "public function get length",function length$get()/*:int*/ {
    return this._length$4;
  },

  /**
   * The maximum number of characters that the text field can contain, as entered by a user. A script can insert more text than <code>maxChars</code> allows; the <code>maxChars</code> property indicates only how much text a user can enter. If the value of this property is <code>0</code>, a user can enter an unlimited amount of text.
   * <p>The default value is <code>0.</code></p>
   */
  "public function get maxChars",function maxChars$get()/*:int*/ {
    return this._maxChars$4;
  },

  /**
   * @private
   */
  "public function set maxChars",function maxChars$set(value/*:int*/)/*:void*/ {
    this._maxChars$4 = value;
  },

  /**
   * The maximum value of <code>scrollH</code>.
   * @see #scrollH
   *
   */
  "public function get maxScrollH",function maxScrollH$get()/*:int*/ {
    return this._maxScrollH$4;
  },

  /**
   * The maximum value of <code>scrollV</code>.
   * @see #scrollV
   *
   */
  "public function get maxScrollV",function maxScrollV$get()/*:int*/ {
    return this._maxScrollV$4;
  },

  /**
   * A Boolean value that indicates whether Flash Player automatically scrolls multiline text fields when the user clicks a text field and rolls the mouse wheel. By default, this value is <code>true</code>. This property is useful if you want to prevent mouse wheel scrolling of text fields, or implement your own text field scrolling.
   */
  "public function get mouseWheelEnabled",function mouseWheelEnabled$get()/*:Boolean*/ {
    return this._mouseWheelEnabled$4;
  },

  /**
   * @private
   */
  "public function set mouseWheelEnabled",function mouseWheelEnabled$set(value/*:Boolean*/)/*:void*/ {
    this._mouseWheelEnabled$4 = value;
  },

  /**
   * Indicates whether field is a multiline text field. If the value is <code>true</code>, the text field is multiline; if the value is <code>false</code>, the text field is a single-line text field. In a field of type <code>TextFieldType.INPUT</code>, the <code>multiline</code> value determines whether the <code>Enter</code> key creates a new line (a value of <code>false</code>, and the <code>Enter</code> key is ignored). If you paste text into a <code>TextField</code> with a <code>multiline</code> value of <code>false</code>, newlines are stripped out of the text.
   * <p>The default value is <code>false.</code></p>
   * @see #numLines
   *
   */
  "public function get multiline",function multiline$get()/*:Boolean*/ {
    return this._multiline$4;
  },

  /**
   * @private
   */
  "public function set multiline",function multiline$set(value/*:Boolean*/)/*:void*/ {
    this._multiline$4 = value;
  },

  /**
   * Defines the number of text lines in a multiline text field. If <code>wordWrap</code> property is set to <code>true</code>, the number of lines increases when text wraps.
   * @see #multiline
   * @see #wordWrap
   *
   */
  "public function get numLines",function numLines$get()/*:int*/ {
    return this._lines$4.length;
  },

  /**
   * Indicates the set of characters that a user can enter into the text field. If the value of the <code>restrict</code> property is <code>null</code>, you can enter any character. If the value of the <code>restrict</code> property is an empty string, you cannot enter any character. If the value of the <code>restrict</code> property is a string of characters, you can enter only characters in the string into the text field. The string is scanned from left to right. You can specify a range by using the hyphen (-) character. Only user interaction is restricted; a script can put any text into the text field. This property does not synchronize with the Embed font options in the Property inspector.
   * <p>If the string begins with a caret (^) character, all characters are initially accepted and succeeding characters in the string are excluded from the set of accepted characters. If the string does not begin with a caret (^) character, no characters are initially accepted and succeeding characters in the string are included in the set of accepted characters.</p>
   * <p>The following example allows only uppercase characters, spaces, and numbers to be entered into a text field:</p>
   * <pre>     my_txt.restrict = "A-Z 0-9";
   </pre>
   * <p>The following example includes all characters, but excludes lowercase letters:</p>
   * <pre>     my_txt.restrict = "^a-z";
   </pre>
   * <p>You can use a backslash to enter a ^ or - verbatim. The accepted backslash sequences are \-, \^ or \\. The backslash must be an actual character in the string, so when specified in ActionScript, a double backslash must be used. For example, the following code includes only the dash (-) and caret (^):</p>
   * <pre>     my_txt.restrict = "\\-\\^";
   </pre>
   * <p>The ^ can be used anywhere in the string to toggle between including characters and excluding characters. The following code includes only uppercase letters, but excludes the uppercase letter Q:</p>
   * <pre>     my_txt.restrict = "A-Z^Q";
   </pre>
   * <p>You can use the <code>\u</code> escape sequence to construct <code>restrict</code> strings. The following code includes only the characters from ASCII 32 (space) to ASCII 126 (tilde).</p>
   * <pre>     my_txt.restrict = "\u0020-\u007E";
   </pre>
   * <p>The default value is <code>null.</code></p>
   */
  "public function get restrict",function restrict$get()/*:String*/ {
    return this._restrict$4;
  },

  /**
   * @private
   */
  "public function set restrict",function restrict$set(value/*:String*/)/*:void*/ {
    this._restrict$4 = value;
  },

  /**
   * The current horizontal scrolling position. If the <code>scrollH</code> property is 0, the text is not horizontally scrolled. This property value is an integer that represents the horizontal position in pixels.
   * <p>The units of horizontal scrolling are pixels, whereas the units of vertical scrolling are lines. Horizontal scrolling is measured in pixels because most fonts you typically use are proportionally spaced; that is, the characters can have different widths. Flash Player performs vertical scrolling by line because users usually want to see a complete line of text rather than a partial line. Even if a line uses multiple fonts, the height of the line adjusts to fit the largest font in use.</p>
   * <p><b>Note:</b> The <code>scrollH</code> property is zero-based, not 1-based like the <code>scrollV</code> vertical scrolling property.</p>
   * @see #maxScrollH
   * @see #scrollV
   *
   */
  "public function get scrollH",function scrollH$get()/*:int*/ {
    return this._scrollH$4;
  },

  /**
   * @private
   */
  "public function set scrollH",function scrollH$set(value/*:int*/)/*:void*/ {
    this._scrollH$4 = value;
  },

  /**
   * The vertical position of text in a text field. The <code>scrollV</code> property is useful for directing users to a specific paragraph in a long passage, or creating scrolling text fields.
   * <p>The units of vertical scrolling are lines, whereas the units of horizontal scrolling are pixels. If the first line displayed is the first line in the text field, scrollV is set to 1 (not 0). Horizontal scrolling is measured in pixels because most fonts are proportionally spaced; that is, the characters can have different widths. Flash performs vertical scrolling by line because users usually want to see a complete line of text rather than a partial line. Even if there are multiple fonts on a line, the height of the line adjusts to fit the largest font in use.</p>
   * @see #scrollH
   * @see #maxScrollV
   *
   */
  "public function get scrollV",function scrollV$get()/*:int*/ {
    return this._scrollV$4;
  },

  /**
   * @private
   */
  "public function set scrollV",function scrollV$set(value/*:int*/)/*:void*/ {
    this._scrollV$4 = value;
  },

  /**
   * A Boolean value that indicates whether the text field is selectable. The value <code>true</code> indicates that the text is selectable. The <code>selectable</code> property controls whether a text field is selectable, not whether a text field is editable. A dynamic text field can be selectable even if it is not editable. If a dynamic text field is not selectable, the user cannot select its text.
   * <p>If <code>selectable</code> is set to <code>false</code>, the text in the text field does not respond to selection commands from the mouse or keyboard, and the text cannot be copied with the Copy command. If <code>selectable</code> is set to <code>true</code>, the text in the text field can be selected with the mouse or keyboard, and the text can be copied with the Copy command. You can select text this way even if the text field is a dynamic text field instead of an input text field.</p>
   * <p>The default value is <code>true.</code></p>
   * @see #setSelection()
   * @see #selectionBeginIndex
   * @see #selectionEndIndex
   * @see #setSelection()
   * @see #caretIndex
   *
   * @example The following example creates two dynamic text fields: one text field with the <code>selectable</code> property set to <code>true</code>, and the other text field with the <code>selectable</code> property set to <code>false</code>. When you use this example, try to select the text in these fields with the mouse or the keyboard.
   * <listing>
   * package
   * {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldAutoSize;
   *
   *     public class selectableExample extends Sprite
   *     {
   *         public function selectableExample()
   *         {
   *     var tf1:TextField = createCustomTextField(10, 10);
   *     tf1.text="This text can be selected";
   *     tf1.selectable=true;
   *
   *     var tf2:TextField = createCustomTextField(10, 30);
   *     tf2.text="This text cannot be selected";
   *     tf2.selectable=false;
   *         }
   *
   *         private function createCustomTextField(x:Number, y:Number):TextField
   *        {
   *             var result:TextField = new TextField();
   *             result.x = x;
   *             result.y = y;
   *             result.autoSize=TextFieldAutoSize.LEFT;
   *             addChild(result);
   *             return result;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function get selectable",function selectable$get()/*:Boolean*/ {
    return this._selectable$4;
  },

  /**
   * @private
   */
  "public function set selectable",function selectable$set(value/*:Boolean*/)/*:void*/ {
    this._selectable$4 = value;
  },

  /**
   * The zero-based character index value of the first character in the current selection. For example, the first character is 0, the second character is 1, and so on. If no text is selected, this property is the value of <code>caretIndex</code>.
   * @see #selectable
   * @see #selectionEndIndex
   * @see #setSelection()
   * @see #caretIndex
   *
   * @example In this example, a TextField instance is created and populated with text. An event listener is assigned so that when the user clicks on the TextField, the <code>printCursorPosition</code> method is called. In that case, the values of the <code>caretIndex</code>, <code>selectionBeginIndex</code>, and <code>selectionEndIndex</code> properties are output.
   * <p>Run this example and try clicking in the TextField to select text. Then click in the field without selecting text. When you click in the text without making a selection, the <code>caretIndex</code> property indicates where the insertion point occurs, and the <code>selectionBeginIndex</code> and <code>selectionEndIndex</code> properties equal the <code>caretIndex</code> property value.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.events.MouseEvent;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldType;
   *
   *     public class TextField_caretIndex extends Sprite {
   *         public function TextField_caretIndex() {
   *             var tf:TextField = createCustomTextField(10, 10, 100, 100);
   *             tf.wordWrap = true;
   *             tf.type = TextFieldType.INPUT;
   *             tf.text = "Click in this text field. Compare the difference between clicking without selecting versus clicking and selecting text.";
   *             tf.addEventListener(MouseEvent.CLICK, printCursorPosition);
   *         }
   *
   *         private function printCursorPosition(event:MouseEvent):void {
   *             var tf:TextField = TextField(event.target);
   *             trace("caretIndex:", tf.caretIndex);
   *             trace("selectionBeginIndex:", tf.selectionBeginIndex);
   *             trace("selectionEndIndex:", tf.selectionEndIndex);
   *         }
   *
   *         private function createCustomTextField(x:Number, y:Number, width:Number, height:Number):TextField {
   *             var result:TextField = new TextField();
   *             result.x = x;
   *             result.y = y;
   *             result.width = width;
   *             result.height = height;
   *             addChild(result);
   *             return result;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function get selectionBeginIndex",function selectionBeginIndex$get()/*:int*/ {
    return this._selectionBeginIndex$4;
  },

  /**
   * The zero-based character index value of the last character in the current selection. For example, the first character is 0, the second character is 1, and so on. If no text is selected, this property is the value of <code>caretIndex</code>.
   * @see #selectable
   * @see #selectionBeginIndex
   * @see #setSelection()
   * @see #caretIndex
   *
   * @example In this example, a TextField instance is created and populated with text. An event listener is assigned so that when the user clicks on the TextField, the <code>printCursorPosition</code> method is called. In that case, the values of the <code>caretIndex</code>, <code>selectionBeginIndex</code>, and <code>selectionEndIndex</code> properties are output.
   * <p>Run this example and try clicking in the TextField to select text. Then click in the field without selecting text. When you click in the text without making a selection, the <code>caretIndex</code> property indicates where the insertion point occurs, and the <code>selectionBeginIndex</code> and <code>selectionEndIndex</code> properties equal the <code>caretIndex</code> property value.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.events.MouseEvent;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldType;
   *
   *     public class TextField_caretIndex extends Sprite {
   *         public function TextField_caretIndex() {
   *             var tf:TextField = createCustomTextField(10, 10, 100, 100);
   *             tf.wordWrap = true;
   *             tf.type = TextFieldType.INPUT;
   *             tf.text = "Click in this text field. Compare the difference between clicking without selecting versus clicking and selecting text.";
   *             tf.addEventListener(MouseEvent.CLICK, printCursorPosition);
   *         }
   *
   *         private function printCursorPosition(event:MouseEvent):void {
   *             var tf:TextField = TextField(event.target);
   *             trace("caretIndex:", tf.caretIndex);
   *             trace("selectionBeginIndex:", tf.selectionBeginIndex);
   *             trace("selectionEndIndex:", tf.selectionEndIndex);
   *         }
   *
   *         private function createCustomTextField(x:Number, y:Number, width:Number, height:Number):TextField {
   *             var result:TextField = new TextField();
   *             result.x = x;
   *             result.y = y;
   *             result.width = width;
   *             result.height = height;
   *             addChild(result);
   *             return result;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function get selectionEndIndex",function selectionEndIndex$get()/*:int*/ {
    return this._selectionEndIndex$4;
  },

  /**
   * The sharpness of the glyph edges in this text field. This property applies only if the <code>flash.text.AntiAliasType</code> property of the text field is set to <code>flash.text.AntiAliasType.ADVANCED</code>. The range for <code>sharpness</code> is a number from -400 to 400. If you attempt to set <code>sharpness</code> to a value outside that range, Flash sets the property to the nearest value in the range (either -400 or 400).
   * <p>The default value is <code>0.</code></p>
   * @see #antiAliasType
   * @see AntiAliasType
   *
   * @example The following example shows the effect of changing the <code>sharpness</code> property for a TextField object. You need to embed the font, and set the <code>antiAliasType</code> property to <code>ADVANCED</code>.
   * <listing>
   * package
   * {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldAutoSize;
   *     import flash.text.AntiAliasType;
   *     import flash.text.GridFitType;
   *     import flash.text.TextFormat;
   *
   *     public class sharpnessExample extends Sprite
   *     {
   *         public function sharpnessExample()
   *         {
   *     var format1:TextFormat = new TextFormat();
   *     format1.font="Arial";
   *     format1.size=24;
   *     var lTxt:String = "The quick brown fox";
   *
   *     var tf1:TextField=createCustomTextField(0,lTxt,format1,-400);
   *     var tf2:TextField=createCustomTextField(30,lTxt,format1,0);
   *     var tf3:TextField=createCustomTextField(60,lTxt,format1,400);
   *         }
   *
   *         private function createCustomTextField(y:Number,fldTxt:String,format:TextFormat,fldSharpness:Number):TextField
   *        {
   *             var result:TextField = new TextField();
   *             result.y=y;
   *             result.text=fldTxt;
   *             result.embedFonts=true;
   *             result.autoSize=TextFieldAutoSize.LEFT;
   *             result.antiAliasType=AntiAliasType.ADVANCED;
   *             result.gridFitType=GridFitType.PIXEL;
   *             result.sharpness=fldSharpness;
   *             result..setTextFormat(format);
   *             addChild(result);
   *             return result;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function get sharpness",function sharpness$get()/*:Number*/ {
    return this._sharpness$4;
  },

  /**
   * @private
   */
  "public function set sharpness",function sharpness$set(value/*:Number*/)/*:void*/ {
    this._sharpness$4 = value;
  },

  /**
   * Attaches a style sheet to the text field. For information on creating style sheets, see the StyleSheet class and the <i>ActionScript 3.0 Developer's Guide</i>.
   * <p>You can change the style sheet associated with a text field at any time. If you change the style sheet in use, the text field is redrawn with the new style sheet. You can set the style sheet to <code>null</code> or <code>undefined</code> to remove the style sheet. If the style sheet in use is removed, the text field is redrawn without a style sheet.</p>
   * <p><b>Note:</b> If the style sheet is removed, the contents of both <code>TextField.text</code> and <code>TextField.htmlText</code> change to incorporate the formatting previously applied by the style sheet. To preserve the original <code>TextField.htmlText</code> contents without the formatting, save the value in a variable before removing the style sheet.</p>
   * @see StyleSheet
   *
   * @example The following example defines a simple StyleSheet object and assigns it to a text field with HTML content. Set the <code>stylesheet</code> property before setting the content.
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.text.StyleSheet;
   *
   *     public class TextStylesheetExample extends Sprite {
   *         var myLabel:TextField = new TextField();
   *         var labelText:String = "Hello world.";
   *         var newStyle:StyleSheet = new StyleSheet();
   *
   *         public function TextStylesheetExample()
   *        {
   *             var styleObj:Object = new Object();
   *             styleObj.fontWeight = "bold";
   *             styleObj.color = "#660066";
   *             newStyle.setStyle(".defStyle", styleObj);
   *
   *             myLabel.styleSheet=newStyle;
   *             myLabel.htmlText=labelText;
   *             addChild(myLabel);
   *         }
   *     }
   * }
   * </listing>
   */
  "public function get styleSheet",function styleSheet$get()/*:StyleSheet*/ {
    return this._styleSheet$4;
  },

  /**
   * @private
   */
  "public function set styleSheet",function styleSheet$set(value/*:StyleSheet*/)/*:void*/ {
    this._styleSheet$4 = value;
  },

  /**
   * A string that is the current text in the text field. Lines are separated by the carriage return character (<code>'\r'</code>, ASCII 13). This property contains unformatted text in the text field, without HTML tags.
   * <p>To get the text in HTML form, use the <code>htmlText</code> property.</p>
   * @see #htmlText
   *
   * @example The following example creates a TextField called <code>tf1</code>, and assigns an HTML-formatted String to its <code>text</code> property. When its <code>htmlText</code> property is traced, the output is the HTML-formatted String, with additional tags (such as <P> and <FONT>) automatically added by Flash Player. When the value of the <code>text</code> property is traced, the unformatted string without HTML tags is displayed.
   * <p>By way of comparison, the same steps are performed on another TextField object named <code>tf2</code>, with the addition that a StyleSheet object is assigned to <code>tf2</code>'s <code>styleSheet</code> property before its <code>htmlText</code> property is set. In that case, when the <code>htmlText</code> property is traced, it only includes the exact HTML text that was originally assigned to the <code>htmlText</code> property, showing that no additional tags were added by Flash Player.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.StyleSheet;
   *     import flash.text.TextField;
   *
   *     public class TextField_text extends Sprite {
   *         public function TextField_text() {
   *             var tf1:TextField = createCustomTextField(10, 10, 400, 22);
   *             tf1.htmlText = "<b>Lorem ipsum dolor sit amet.</b>";
   *
   *             // htmlText: <P ALIGN="LEFT"><FONT FACE="Times New Roman" SIZE="12" COLOR="#000000" LETTERSPACING="0" KERNING="0"><b>Lorem ipsum dolor sit amet.</b></FONT></P>
   *             trace("htmlText: " + tf1.htmlText);
   *             // text: Lorem ipsum dolor sit amet.
   *             trace("text: " + tf1.text);
   *
   *             var tf2:TextField = createCustomTextField(10, 50, 400, 22);
   *             tf2.styleSheet = new StyleSheet();
   *             tf2.htmlText = "<b>Lorem ipsum dolor sit amet.</b>";
   *             // htmlText: <b>Lorem ipsum dolor sit amet.</b>
   *             trace("htmlText: " + tf2.htmlText);
   *             // text: Lorem ipsum dolor sit amet.
   *             trace("text: " + tf2.text);
   *         }
   *
   *         private function createCustomTextField(x:Number, y:Number, width:Number, height:Number):TextField {
   *             var result:TextField = new TextField();
   *             result.x = x;
   *             result.y = y;
   *             result.width = width;
   *             result.height = height;
   *             addChild(result);
   *             return result;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function get text",function text$get()/*:String*/ {
    return this._lines$4.join('\n');
  },

  /**
   * @private
   */
  "public function set text",function text$set(value/*:String*/)/*:void*/ {
    this._lines$4 = value.split('\n');
    $$private.updateElementProperty(this.getElement(), "innerHTML", this._lines$4.join('<br />'));
  },

  /**
   * The color of the text in a text field, in hexadecimal format. The hexadecimal color system uses six digits to represent color values. Each digit has 16 possible values or characters. The characters range from 0-9 and then A-F. For example, black is <code>0x000000</code>; white is <code>0xFFFFFF</code>.
   * <p>The default value is <code>0 (0x000000).</code></p>
   * @example The following ActionScript creates a TextField object and changes its <code>textColor</code> property to red (<code>0xFF0000</code>).
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *
   *     public class TextField_textColor extends Sprite {
   *         public function TextField_textColor() {
   *             var tf:TextField = createCustomTextField(10, 10, 100, 300);
   *             tf.text = "This will be red text";
   *             tf.textColor = 0xFF0000;
   *         }
   *
   *         private function createCustomTextField(x:Number, y:Number, width:Number, height:Number):TextField {
   *             var result:TextField = new TextField();
   *             result.x = x;
   *             result.y = y;
   *             result.width = width;
   *             result.height = height;
   *             addChild(result);
   *             return result;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function get textColor",function textColor$get()/*:uint*/ {
    return $$uint(this._textFormat$4.color !== null ? this._textFormat$4.color : this._defaultTextFormat$4.color);
  },

  /**
   * @private
   */
  "public function set textColor",function textColor$set(value/*:uint*/)/*:void*/ {
    this._defaultTextFormat$4.color = this._textFormat$4.color = value;
    if (this.hasElement()) {
      $$private.updateElementProperty(this.getElement(), "style.color", flash.display.Graphics.toRGBA(value));
    }
  },

  /**
   * The height of the text in pixels.
   * @see #textWidth
   *
   * @example The following example creates a TextField object and assigns text to it. The <code>trace</code> statements display the values of the <code>textWidth</code> and <code>textHeight</code> properties. For comparison, the <code>width</code> and <code>height</code> properties are also displayed. (Note that the values you see for <code>textHeight</code> and <code>textWidth</code> might vary depending on the font that is used on your machine).
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *
   *     public class TextField_textHeight extends Sprite {
   *         public function TextField_textHeight() {
   *             var tf:TextField = createCustomTextField(10, 10, 100, 150);
   *             tf.text = "Sample text";
   *
   *             trace("textWidth: " + tf.textWidth); // textWidth: 55.75
   *             trace("textHeight: " + tf.textHeight); // textHeight: 13.450000000000001
   *             trace("width: " + tf.width); // width: 100
   *             trace("height: " + tf.height); // height: 150
   *         }
   *
   *         private function createCustomTextField(x:Number, y:Number, width:Number, height:Number):TextField {
   *             var result:TextField = new TextField();
   *             result.x = x;
   *             result.y = y;
   *             result.width = width;
   *             result.height = height;
   *             result.border = true;
   *             result.background = true;
   *             addChild(result);
   *             return result;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function get textHeight",function textHeight$get()/*:Number*/ {
    return this._textHeight$4;
  },

  /**
   * The width of the text in pixels.
   * @see #textHeight
   *
   * @example The following example creates a TextField object and assigns text to it. The <code>trace</code> statements display the values of the <code>textWidth</code> and <code>textHeight</code> properties. For comparison, the <code>width</code> and <code>height</code> properties are also displayed. (Note that the values you see for <code>textHeight</code> and <code>textWidth</code> might vary depending on the font that is used on your machine).
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *
   *     public class TextField_textHeight extends Sprite {
   *         public function TextField_textHeight() {
   *             var tf:TextField = createCustomTextField(10, 10, 100, 150);
   *             tf.text = "Sample text";
   *
   *             trace("textWidth: " + tf.textWidth); // textWidth: 55.75
   *             trace("textHeight: " + tf.textHeight); // textHeight: 13.450000000000001
   *             trace("width: " + tf.width); // width: 100
   *             trace("height: " + tf.height); // height: 150
   *         }
   *
   *         private function createCustomTextField(x:Number, y:Number, width:Number, height:Number):TextField {
   *             var result:TextField = new TextField();
   *             result.x = x;
   *             result.y = y;
   *             result.width = width;
   *             result.height = height;
   *             result.border = true;
   *             result.background = true;
   *             addChild(result);
   *             return result;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function get textWidth",function textWidth$get()/*:Number*/ {
    return this._textWidth$4;
  },

  /**
   * The thickness of the glyph edges in this text field. This property applies only when <code>flash.text.AntiAliasType</code> is set to <code>flash.text.AntiAliasType.ADVANCED</code>.
   * <p>The range for <code>thickness</code> is a number from -200 to 200. If you attempt to set <code>thickness</code> to a value outside that range, the property is set to the nearest value in the range (either -200 or 200).</p>
   * <p>The default value is <code>0.</code></p>
   * @see #antiAliasType
   * @see AntiAliasType
   *
   * @example The following example shows the effect of changing the <code>thickness</code> property for a TextField object. You need to embed the font, and set the <code>antiAliasType</code> property to <code>ADVANCED</code>.
   * <listing>
   * package
   * {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldAutoSize;
   *     import flash.text.AntiAliasType;
   *     import flash.text.GridFitType;
   *     import flash.text.TextFormat;
   *
   *     public class thicknessExample extends Sprite
   *     {
   *         public function thicknessExample()
   *         {
   *     var format1:TextFormat = new TextFormat();
   *     format1.font="Arial";
   *     format1.size=24;
   *     var lTxt:String = "The quick brown fox";
   *
   *     var tf1:TextField=createCustomTextField(0,lTxt,format1,-200);
   *     var tf2:TextField=createCustomTextField(30,lTxt,format1,0);
   *     var tf3:TextField=createCustomTextField(60,lTxt,format1,200);
   *         }
   *
   *         private function createCustomTextField(y:Number,fldTxt:String,format:TextFormat,fldThickness:Number):TextField
   *        {
   *             var result:TextField = new TextField();
   *             result.y=y;
   *             result.text=fldTxt;
   *             result.embedFonts=true;
   *             result.autoSize=TextFieldAutoSize.LEFT;
   *             result.antiAliasType=AntiAliasType.ADVANCED;
   *             result.gridFitType=GridFitType.PIXEL;
   *             result.thickness=fldThickness;
   *             result.setTextFormat(format);
   *             addChild(result);
   *             return result;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function get thickness",function thickness$get()/*:Number*/ {
    return this._thickness$4;
  },

  /**
   * @private
   */
  "public function set thickness",function thickness$set(value/*:Number*/)/*:void*/ {
    this._thickness$4 = value;
  },

  /**
   * The type of the text field. Either one of the following TextFieldType constants: <code>TextFieldType.DYNAMIC</code>, which specifies a dynamic text field, which a user cannot edit, or <code>TextFieldType.INPUT</code>, which specifies an input text field, which a user can edit.
   * <p>The default value is <code>dynamic.</code></p>
   * @throws ArgumentError The <code>type</code> specified is not a member of flash.text.TextFieldType.
   *
   * @see TextFieldType
   *
   * @example The following example creates two text fields: <code>tfDynamic</code> and <code>tfInput</code>. Text is entered into both text fields. However, <code>tfDynamic</code> has its <code>type</code> property set to <code>TextFieldType.DYNAMIC</code>, and <code>tfInput</code> has its <code>type</code> property set to <code>TextFieldType.INPUT</code>, so the user can modify the text in <code>tfInput</code> but can only view the text in <code>tfDynamic</code>.
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldType;
   *
   *     public class TextField_type extends Sprite {
   *         public function TextField_type() {
   *             var tfDynamic:TextField = createCustomTextField(10, 10, 100, 20);
   *             tfDynamic.type = TextFieldType.DYNAMIC;
   *             tfDynamic.text = "hello";
   *
   *             var tfInput:TextField = createCustomTextField(10, 45, 100, 20);
   *             tfInput.type = TextFieldType.INPUT;
   *             tfInput.text = "world";
   *         }
   *
   *         private function createCustomTextField(x:Number, y:Number, width:Number, height:Number):TextField {
   *             var result:TextField = new TextField();
   *             result.x = x;
   *             result.y = y;
   *             result.width = width;
   *             result.height = height;
   *             result.background = true;
   *             result.border = true;
   *             addChild(result);
   *             return result;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function get type",function type$get()/*:String*/ {
    return this._type$4;
  },

  /**
   * @private
   */
  "public function set type",function type$set(value/*:String*/)/*:void*/ {
    this._type$4 = value;
  },

  /**
   * Specifies whether to copy and paste the text formatting along with the text. When set to <code>true</code>, Flash Player copies and pastes formatting (such as alignment, bold, and italics) when you copy and paste between text fields. Both the origin and destination text fields for the copy and paste procedure must have <code>useRichTextClipboard</code> set to <code>true</code>. The default value is <code>false</code>.
   * @example This example creates an input text field (<code>tf1</code>) and two dynamic text fields (<code>tf2</code> and <code>tf3</code>). The code assigns each dynamic text field a TextFormat object (Courier Bold font). The <code>tf2</code> text field has <code>useRichTextClipboard</code> property set to <code>false</code>. The <code>tf3</code> text field has the <code>useRichTextClipboard</code> property set to <code>true</code>. When you copy the text from the <code>tf2</code> text field and paste it into the <code>tf1</code> text field, the pasted text does not include the formatting. When you copy the text from the <code>tf3</code> text field (which has <code>useRichTextClipboard</code> set to <code>true</code>) and paste it into the <code>tf1</code> text field, the pasted text includes the formatting.
   * <listing>
   * package
   * {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldType;
   *     import flash.text.TextFormat;
   *
   *     public class useRichTextClipboard extends Sprite
   *     {
   *         public function useRichTextClipboard()
   *         {
   *     var format1:TextFormat = new TextFormat();
   *     format1.font="Courier";
   *     format1.bold=true;
   *
   *     var tf1:TextField = createCustomTextField(10, 10, 200, 20);
   *     tf1.type=TextFieldType.INPUT;
   *     tf1.useRichTextClipboard=true;
   *
   *     var tf2:TextField = createCustomTextField(220, 10, 200, 20);
   *     tf2.text="1.Text loses format";
   *     tf2.setTextFormat(format1);
   *     tf2.useRichTextClipboard=false;
   *
   *     var tf3:TextField = createCustomTextField(220, 50, 200, 20);
   *     tf3.text="2.Text includes format";
   *     tf3.setTextFormat(format1);
   *     tf3.useRichTextClipboard=true;
   *         }
   *
   *         private function createCustomTextField(x:Number, y:Number, width:Number, height:Number):TextField
   *        {
   *             var result:TextField = new TextField();
   *             result.x = x;
   *             result.y = y;
   *             result.width = width;
   *             result.height = height;
   *             result.background = true;
   *             result.border = true;
   *             addChild(result);
   *             return result;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function get useRichTextClipboard",function useRichTextClipboard$get()/*:Boolean*/ {
    return this._useRichTextClipboard$4;
  },

  /**
   * @private
   */
  "public function set useRichTextClipboard",function useRichTextClipboard$set(value/*:Boolean*/)/*:void*/ {
    this._useRichTextClipboard$4 = value;
  },

  /**
   * A Boolean value that indicates whether the text field has word wrap. If the value of <code>wordWrap</code> is <code>true</code>, the text field has word wrap; if the value is <code>false</code>, the text field does not have word wrap. The default value is <code>false</code>.
   * @example This example demonstrates the difference between setting the <code>wordWrap</code> property to <code>true</code> and setting it to <code>false</code>. Two TextField instances are created whose contents are too large for their widths. The <code>wordWrap</code> property of the first (named <code>tfWrap</code>) is set to <code>true</code>; it is set to <code>false</code> for the second (<code>tfNoWrap</code>).
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *
   *     public class TextField_wordWrap extends Sprite {
   *         public function TextField_wordWrap() {
   *             var tfWrap:TextField = createCustomTextField(10, 10, 100, 100);
   *             tfWrap.wordWrap = true;
   *             tfWrap.text = "(wordWrap = true):\nThis is very long text that will certainly extend beyond the width of this text field";
   *
   *             var tfNoWrap:TextField = createCustomTextField(10, 150, 100, 100);
   *             tfNoWrap.wordWrap = false;
   *             tfNoWrap.text = "(wordWrap = false):\nThis is very long text that will certainly extend beyond the width of this text field";
   *         }
   *
   *         private function createCustomTextField(x:Number, y:Number, width:Number, height:Number):TextField {
   *             var result:TextField = new TextField();
   *             result.x = x;
   *             result.y = y;
   *             result.width = width;
   *             result.height = height;
   *             result.background = true;
   *             result.border = true;
   *             addChild(result);
   *             return result;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function get wordWrap",function wordWrap$get()/*:Boolean*/ {
    return this._wordWrap$4;
  },

  /**
   * @private
   */
  "public function set wordWrap",function wordWrap$set(value/*:Boolean*/)/*:void*/ {
    this._wordWrap$4 = value;
  },

  /**
   * Creates a new TextField instance. After you create the TextField instance, call the <code>addChild()</code> or <code>addChildAt()</code> method of the parent DisplayObjectContainer object to add the TextField instance to the display list.
   * <p>The default size for a text field is 100 x 100 pixels.</p>
   * @example The following example shows how you can dynamically create an input TextField object in ActionScript 3.0 by setting the text field object's type property to the TextFieldType.INPUT constant. Example provided by <a href="http://actionscriptexamples.com/2008/12/02/dynamically-creating-an-input-text-field-in-actionscript-30/">ActionScriptExamples.com</a>.
   * <listing>
   * var theTextField:TextField = new TextField();
   * theTextField.type = TextFieldType.INPUT;
   * theTextField.border = true;
   * theTextField.x = 10;
   * theTextField.y = 10;
   * theTextField.multiline = true;
   * theTextField.wordWrap = true;
   * addChild(theTextField);
   * </listing>
   */
  "public function TextField",function TextField$() {
    this.super$4();this._defaultTextFormat$4=this._defaultTextFormat$4();this._textFormat$4=this._textFormat$4();
    this._lines$4 = [""];
  },

  /**
   * Appends the string specified by the <code>newText</code> parameter to the end of the text of the text field. This method is more efficient than an addition assignment (<code>+=</code>) on a <code>text</code> property (such as <code>someTextField.text += moreText</code>), particularly for a text field that contains a significant amount of content.
   * @param newText The string to append to the existing text.
   *
   * @example The following example displays the time if it's not the weekend or the text, "It's the weekend," if it is. It also counts the number of characters up to a certain position and the number of lines in the text field.
   * <p>The <code>outputText</code> text field is set to automatically fit the text and to resize as a left-justified text using <code>autoSize</code> property. The <code>outputText.text</code> property writes the first line of the content and the method <code>appendText()</code> appends the rest of the content. (It is not necessary to start with the <code>text</code> property. The <code>appendText()</code> method could also be used to append text from the outset.) Setting the <code>text</code> property a second time will overwrite the original text. Use <code>+=</code> operator to append content with the <code>text</code> property.</p>
   * <p>The <code>if</code> statement checks if the date is Saturday (6) or Sunday (0). If it's not, the <code>toLocaleTimeString()</code> method returns the local time, which is appended to the text field's content.</p>
   * <p>The text field's <code>length</code> property is used to read the number of characters until right before the function is called, and the property <code>numLines</code> is used to count the number of lines in the text field. Note that the empty lines are counted in the number of lines and the empty spaces and line breaks (\n) are counted in determining the content length.</p>
   * <listing>
   *   package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldAutoSize;
   *
   *     public class TextField_appendTextExample extends Sprite {
   *
   *         public function TextField_appendTextExample() {
   *             var outputText:TextField = new TextField();
   *             var today:Date = new Date();
   *
   *             outputText.x = 10;
   *             outputText.y = 10;
   *             outputText.background = true;
   *             outputText.autoSize = TextFieldAutoSize.LEFT;
   *
   *             outputText.text = "WHAT TIME IS IT?" + "\n\n";
   *
   *             if((today.day == 0) || (today.day == 6)) {
   *                 outputText.appendText("It's the weekend.");
   *                 outputText.appendText("\n\n");
   *
   *             } else {
   *                 outputText.appendText("The time is: ");
   *                 outputText.appendText(today.toLocaleTimeString() + ".\n\n");
   *             }
   *
   *             outputText.appendText("Number of characters including line breaks and spaces so far: ");
   *             outputText.appendText(outputText.length.toString() + "\n");
   *             outputText.appendText("Number of lines in the outputText: ");
   *             outputText.appendText(outputText.numLines.toString());
   *
   *             this.addChild(outputText);
   *         }
   *     }
   * }
   * </listing>
   */
  "public function appendText",function appendText(newText/*:String*/)/*:void*/ {
    this.text = this.text + newText;
  },

  /**
   * Returns a rectangle that is the bounding box of the character.
   * @param charIndex The zero-based index value for the character (for example, the first position is 0, the second position is 1, and so on).
   *
   * @return A rectangle with <code>x</code> and <code>y</code> minimum and maximum values defining the bounding box of the character.
   *
   * @see flash.geom.Rectangle
   *
   * @example In the following example the <code>getCharBoundaries()</code> method is used to mark (put a spotlight on) a character that is selected by the user.
   * <p>The class defines the <code>spotlight</code> Shape object that will be used to draw a rectangle around each character that is selected. When the user clicks on the <code>myTextField</code> text field, the <code>clickHandler()</code> method is invoked.</p>
   * <p>In the <code>clickHandler()</code> method, the <code>getCharIndexAtPoint()</code> method gets the clicked character's index based on the <code>localX</code> and <code>localY</code> coordinates of the mouse click, which is relative to the containing <code>Sprite</code>. The <code>getCharIndexAtPoint()</code> method returns <code>-1</code> if the point (mouse click) was not over any character. Since the text field could be larger than the text, the returned integer (<code>index</code>) is checked to make sure the user has clicked on a character. The <code>index</code> integer is also used by <code>getCharBoundaries()</code> to get a <code>Rectangle</code> object that holds the boundary of the character. The <code>clear()</code> method clears any previously displayed <code>spotlight</code> Shape object. A new rectangle the size of the character's width and height boundaries is produced at the location of the character (offset from the (10, 10) coordinates) using the returned <code>frame</code> rectangle's x and y coordinates. To put the spotlight on the character, the <code>spotlight</code> Shape object is filled with color yellow and the opacity is set to 35 percent, so the character can be seen. Note that spaces are also considered a character.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.events.MouseEvent;
   *     import flash.text.TextField;
   *     import flash.geom.Rectangle;
   *     import flash.events.MouseEvent;
   *     import flash.text.TextFieldAutoSize;
   *     import flash.display.Shape;
   *
   *     public class TextField_getCharBoundariesExample extends Sprite
   *     {
   *         private var myTextField:TextField = new TextField();
   *         private var spotlight:Shape = new Shape();
   *
   *         public function TextField_getCharBoundariesExample() {
   *
   *             myTextField.x = 10;
   *             myTextField.y = 10;
   *             myTextField.border = true;
   *             myTextField.selectable = false;
   *             myTextField.autoSize = TextFieldAutoSize.LEFT;
   *
   *             myTextField.text = "Selected a character from this text by clicking on it."
   *
   *             myTextField.addEventListener(MouseEvent.CLICK, clickHandler);
   *
   *             this.addChild(myTextField);
   *             this.addChild(spotlight);
   *          }
   *
   *         private function clickHandler (e:MouseEvent):void {
   *             var index:int = myTextField.getCharIndexAtPoint(e.localX, e.localY);
   *
   *             if (index != -1) {
   *                  var frame:Rectangle = myTextField.getCharBoundaries(index);
   *
   *                 spotlight.graphics.clear();
   *                 spotlight.graphics.beginFill(0xFFFF00, .35);
   *                 spotlight.graphics.drawRect((frame.x + 10), (frame.y + 10), frame.width, frame.height);
   *                 spotlight.graphics.endFill();
   *             }
   *         }
   *     }
   * }
   * </listing>
   */
  "public function getCharBoundaries",function getCharBoundaries(charIndex/*:int*/)/*:Rectangle*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns the zero-based index value of the character at the point specified by the <code>x</code> and <code>y</code> parameters.
   * @param x The <i>x</i> coordinate of the character.
   * @param y The <i>y</i> coordinate of the character.
   *
   * @return The zero-based index value of the character (for example, the first position is 0, the second position is 1, and so on). Returns -1 if the point is not over any character.
   *
   * @example In the following example, when a user clicked on a character, the character is echoed in another text field above the text.
   * <p>The first text field holds the text the user is going to select. In order to make sure the text is clicked but not selected, <code>selectable</code> property is set to false. When the user clicks on the <code>firstTextField</code> text field, the <code>clickHandler()</code> method is invoked.</p>
   * <p>In the <code>clickHandler()</code> method, the <code>getCharIndexAtPoint()</code> method returns the character's index based on the <code>localX</code> and <code>localY</code> coordinates of the mouse click. Since the text field could be larger than the text, the return integer (<code>index</code>) is checked to make sure the user has clicked on a character. (The <code>getCharIndexAtPoint()</code> method returns <code>-1</code>, if the point (mouse click) was not over a character.) The mouse coordinates is used to set the coordinates of the new text field where the echoed character will appear. The color of the character in the second text field is set to red. Finally the text of the second field is set to the selected character, which is retrieved using the <code>charAt()</code> method. Note that using the <code>text</code> property instead of the <code>appendText()</code> method will overwrite the character in the second text field, instead of appending it.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.events.MouseEvent;
   *     import flash.text.TextField;
   *     import flash.geom.Rectangle;
   *     import flash.events.MouseEvent;
   *     import flash.text.TextFieldAutoSize;
   *
   *     public class TextField_getCharIndexAtPointExample extends Sprite {
   *         private var firstTextField:TextField = new TextField();
   *         private var secondTextField:TextField = new TextField();
   *
   *         public function TextField_getCharIndexAtPointExample() {
   *
   *             firstTextField.x = 100;
   *             firstTextField.y = 100;
   *             firstTextField.width = 260;
   *             firstTextField.height = 20;
   *             firstTextField.border = true;
   *             firstTextField.background = true;
   *             firstTextField.selectable = false;
   *
   *             firstTextField.text = "Selected a character from this text by clicking on it."
   *
   *             firstTextField.addEventListener(MouseEvent.CLICK, clickHandler);
   *
   *             this.addChild(firstTextField);
   *             this.addChild(secondTextField);
   *          }
   *
   *         private function clickHandler (e:MouseEvent):void {
   *             var index:int = firstTextField.getCharIndexAtPoint(e.localX, e.localY);
   *
   *             if (index != -1) {
   *                 secondTextField.x = mouseX;
   *                 secondTextField.y =  70;
   *                 secondTextField.border = true;
   *                 secondTextField.selectable = false;
   *                 secondTextField.background = true;
   *                 secondTextField.textColor = 0xFF0000;
   *                 secondTextField.autoSize = TextFieldAutoSize.LEFT;
   *                 secondTextField.text = firstTextField.text.charAt(index);
   *             }
   *         }
   *     }
   * }
   * </listing>
   */
  "public function getCharIndexAtPoint",function getCharIndexAtPoint(x/*:Number*/, y/*:Number*/)/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Given a character index, returns the index of the first character in the same paragraph.
   * @param charIndex The zero-based index value of the character (for example, the first character is 0, the second character is 1, and so on).
   *
   * @return The zero-based index value of the first character in the same paragraph.
   *
   * @throws RangeError The character index specified is out of range.
   *
   * @example In the following example, paragraph formatting is applied to the text field content. When the user clicks on a paragraph, the text of the paragraph will be aligned right and when the user clicks on the paragraph again, it will return to the original (default) format (left-align).
   * <p>In the constructor, the <code>myTextField</code> text field is set to text wrap. The <code>getTextFormat</code> method returns the original format of the first character of the content of the text field, which is placed in the <code>originalFormat</code> TextFormat object. A new TextFormat object (<code>newFormat</code>) is also defined and its <code>align</code> property is assigned to right-justified. When the user clicks on the text field, the <code>clickHandler()</code> method is invoked.</p>
   * <p>In the <code>clickHandler()</code> method, the <code>getCharIndexAtPoint()</code> method returns the character's index based on the <code>localX</code> and <code>localY</code> coordinates of the mouse click. The first <code>if</code> statement checks to see if the use has clicked on a character. Using the <code>clickIndex</code> integer returned by the <code>getCharIndexAtPoint()</code> method, the <code>getFirstCharInParagraph()</code> method returns the index of the first character in the paragraph the user has clicked. The index of the last character in the paragraph is determined by adding the length of the paragraph (using <code>getParagraphLength()</code> method) to the index of the first character in the paragraph, minus the last character (<code>\n</code>). The second <code>if</code> statement checks the format of the first character in the paragraph. If its alignment value is the same as the original format (left-justified), the new format is applied to all the characters in the paragraph. Otherwise, the format of the paragraph is set back to the original format. Alignment, along with formatting like indent, bullet, tab stop, left and right margin are formats that are meant for paragraphs. Note that once word wrap or line break is used, the formatting will only apply to the first line of the paragraph if <code>endIndex</code> argument is not defined for the <code>setTextFormat()</code> method.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.events.MouseEvent;
   *     import flash.text.TextFormat;
   *     import flash.text.TextFormatAlign;
   *
   *     public class TextField_getFirstCharInParagraphExample extends Sprite
   *     {
   *         private var myTextField:TextField = new TextField();
   *         private var originalFormat:TextFormat = new TextFormat();
   *         private var newFormat:TextFormat = new TextFormat();
   *
   *         public function TextField_getFirstCharInParagraphExample() {
   *             myTextField.x = 10;
   *             myTextField.y = 10;
   *             myTextField.border = true;
   *             myTextField.wordWrap = true;
   *             myTextField.width = 300;
   *             myTextField.height = 300;
   *             myTextField.background = true;
   *
   *             myTextField.appendText("The TextField class is used to create display objects for "
   *                         + "text display and input. All dynamic and input text fields in a SWF file "
   *                         + "are instances of the TextField class. You can use the TextField class "
   *                         + "to perform low-level text rendering. However, in Flex, you typically use "
   *                         + "the Label, Text, TextArea, and TextInput controls to process text. "
   *                         + "You can give a text field an instance name in the Property inspector "
   *                         + "and use the methods and properties of the TextField class to manipulate it with ActionScript. "
   *                         + "TextField instance names are displayed in the Movie Explorer and in the Insert "
   *                         + "Target Path dialog box in the Actions panel.\n\n"
   *                         + "To create a text field dynamically, use the TextField constructor.\n\n"
   *                         + "The methods of the TextField class let you set, select, and manipulate "
   *                         + "text in a dynamic or input text field that you create during authoring or at runtime.\n\n");
   *
   *             originalFormat = myTextField.getTextFormat(0);
   *
   *             newFormat.align = TextFormatAlign.RIGHT;
   *
   *             myTextField.addEventListener(MouseEvent.CLICK, clickHandler);
   *
   *             this.addChild(myTextField);
   *         }
   *
   *         private function clickHandler(e:MouseEvent):void {
   *             var clickIndex:int = myTextField.getCharIndexAtPoint(e.localX, e.localY);
   *
   *             if(clickIndex != -1) {
   *                 var paragraphFirstIndex:int = myTextField.getFirstCharInParagraph(clickIndex);
   *                 var paragraphEndIndex:int = paragraphFirstIndex + ((myTextField.getParagraphLength(clickIndex) - 1));
   *
   *                 if (myTextField.getTextFormat(paragraphFirstIndex).align == originalFormat.align) {
   *                      myTextField.setTextFormat(newFormat, paragraphFirstIndex, paragraphEndIndex);
   *                 }else {
   *                      myTextField.setTextFormat(originalFormat, paragraphFirstIndex, paragraphEndIndex);
   *                 }
   *             }
   *         }
   *     }
   * }
   *
   * </listing>
   */
  "public function getFirstCharInParagraph",function getFirstCharInParagraph(charIndex/*:int*/)/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns a DisplayObject reference for the given <code>id</code>, for an image or SWF file that has been added to an HTML-formatted text field by using an <code><img></code> tag. The <code><img></code> tag is in the following format:
   * <pre><code>  <img src = 'filename.jpg' id = 'instanceName' ></code></pre>
   * @param id The <code>id</code> to match (in the <code>id</code> attribute of the <code><img></code> tag).
   *
   * @return The display object corresponding to the image or SWF file with the matching <code>id</code> attribute in the <code><img></code> tag of the text field. For media loaded from an external source, this object is a Loader object, and, once loaded, the media object is a child of that Loader object. For media embedded in the SWF file, it is the loaded object. If no <code><img></code> tag with the matching <code>id</code> exists, the method returns <code>null</code>.
   *
   * @see #htmlText
   *
   * @example In the following example, when the text field is clicked, the image in the field is set to 25 percent opacity and it rotates 90 degrees from its original rotation. The image will continue to rotate with each subsequent click.
   * <p>The image (<code>image.jpg</code>) is included via the HTML. (Here it is assumed that an image file is in the same directory as the SWF file.) An <code>id</code> attribute needs to be defined for the <code>img</code> tag in order to access the image using <code>getImageReference()</code> method. The <code>htmlText</code> property is used to include HTML-formatted string content. When the user clicks on the <code>myTextField</code> text field, the <code>clickHandler()</code> method is invoked.</p>
   * <p>In the <code>clickHandler()</code> method, the <code>getImageReference()</code> method returns a reference to the image as a <code>DisplayObject</code>. This reference can be used to manipulate the image, like any <code>DisplayObject</code> object. Here, the <code>alpha</code> (transparency) and <code>rotation</code> properties are set. The <code>transform</code> property can also be used to access the display object's matrix, color transform, and pixel bounds. Note also that <code>flash.display.DisplayObject</code> needs to be imported.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.events.Event;
   *     import flash.events.MouseEvent;
   *     import flash.display.DisplayObject;
   *
   *     import flash.text.TextFieldAutoSize;
   *
   *     public class TextField_getImageReferenceExample extends Sprite
   *     {
   *         private var myTextField:TextField = new TextField();
   *
   *         public function TextField_getImageReferenceExample()
   *         {
   *             var myText1:String = "<p>Here is an image we want to mainpulate: <img src='image.jpg' id='testimage'></p>";
   *
   *             myTextField.x = 10;
   *             myTextField.y = 10;
   *             myTextField.width = 250;
   *             myTextField.height = 250;
   *             myTextField.background = true;
   *             myTextField.border = true;
   *             myTextField.border = true;
   *             myTextField.multiline = true;
   *
   *             myTextField.htmlText = myText1;
   *
   *             myTextField.addEventListener(MouseEvent.CLICK, clickHandler);
   *
   *             this.addChild(myTextField);
   *         }
   *
   *         private function clickHandler(e:MouseEvent):void {
   *             var imageRef:DisplayObject = myTextField.getImageReference("testimage");
   *
   *             imageRef.rotation += 90;
   *             imageRef.x = 125;
   *             imageRef.y = 125;
   *             imageRef.alpha = 0.25;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function getImageReference",function getImageReference(id/*:String*/)/*:DisplayObject*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns the zero-based index value of the line at the point specified by the <code>x</code> and <code>y</code> parameters.
   * @param x The <i>x</i> coordinate of the line.
   * @param y The <i>y</i> coordinate of the line.
   *
   * @return The zero-based index value of the line (for example, the first line is 0, the second line is 1, and so on). Returns -1 if the point is not over any line.
   *
   * @example In the following example, when a user selects a line from the Shakespeare's sonnet, it is copied (appended) into a new text field.
   * <p>In the constructor, the <code>poem</code> text field is set not to wrap (since it's a poem). The <code>autoSize</code> property also is used to set the text to automatically fit and to have it resize as a left-justified text. The <code>poemCopy</code> text field is placed under the <code>poem</code> text field. When a user clicks on some line of the poem, the <code>clickHandler()</code> method is invoked.</p>
   * <p>In <code>clickHandler()</code> method, the <code>getLineIndexAtPoint()</code> method returns the line index of where the user has clicked based on the <code>localX</code> and <code>localY</code> coordinates of the mouse click. (Since the original poem fits the size of the text field here, it is not necessary to check for out of range error (<code>RangeError</code>) thrown by <code>getCharIndexAtPoint()</code> method.) The line index is then used to get the content of the line as a string with the <code>getLineText()</code> method, which is then appended to the <code>poemCopy</code> text field content. The copying can go on continuously but after a point, the text will be outside of the range of the viewable <code>poemCopy</code> text field.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.events.MouseEvent;
   *     import flash.text.TextFormat;
   *     import flash.text.TextFieldAutoSize;
   *
   *     public class TextField_getLineIndexAtPointExample extends Sprite {
   *         private var poem:TextField = new TextField();
   *         private var poemCopy:TextField = new TextField();
   *
   *         public function TextField_getLineIndexAtPointExample() {
   *             poem.border = true;
   *             poem.autoSize = TextFieldAutoSize.LEFT;
   *             poem.x = 10;
   *             poem.wordWrap = false;
   *
   *             poemCopy.height = 250;
   *             poemCopy.width = 270;
   *             poemCopy.y = 230;
   *             poemCopy.x = 10;
   *             poemCopy.background = true;
   *             poemCopy.border = true;
   *             poemCopy.wordWrap = false;
   *
   *             poem.appendText("Let me not to the marriage of true minds\n"
   *                               + "Admit impediments. love is not love\n"
   *                               + "Which alters when it alteration finds\n"
   *                               + "Or bends with the remover to remove:\n"
   *                               + "O no! it is an ever-fixed mark\n"
   *                               + "That looks on tempests and is never shaken;\n"
   *                               + "It is the star to every wandering bark,\n"
   *                               + "Whose worth's unknown, although his height be taken.\n"
   *                               + "Love's not Time's fool, though rosy lips and cheeks\n"
   *                               + "Within his bending sickle's compass come:\n"
   *                               + "Love alters not with his brief hours and weeks,\n"
   *                               + "But bears it out even to the edge of doom.\n"
   *                               + "If this be error and upon me proved,\n"
   *                               + "I never writ, nor no man ever loved.");
   *
   *            poem.addEventListener(MouseEvent.CLICK, clickHandler);
   *
   *            this.addChild(poem);
   *            this.addChild(poemCopy);
   *         }
   *
   *         private function clickHandler(e:MouseEvent):void {
   *                 var index:int = poem.getLineIndexAtPoint(e.localX, e.localY);
   *                 var s:String;
   *
   *                 s = poem.getLineText(index);
   *                 poemCopy.appendText(s + "\n");
   *         }
   *     }
   * }
   * </listing>
   */
  "public function getLineIndexAtPoint",function getLineIndexAtPoint(x/*:Number*/, y/*:Number*/)/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns the zero-based index value of the line containing the character specified by the <code>charIndex</code> parameter.
   * @param charIndex The zero-based index value of the character (for example, the first character is 0, the second character is 1, and so on).
   *
   * @return The zero-based index value of the line.
   *
   * @throws RangeError The character index specified is out of range.
   *
   * @example In the following example, the <code>getLineIndexOfChar()</code> method returns the line numbers for the 100th and 500th characters in the text field.
   * <p>The <code>myTextField</code> text field is defined to wrap and resize as a left-justified text. The <code>getLineIndexOfChar()</code> method returns the line index for the specified character indexes (100 and 500). This information is then appended after the paragraph. Note that since line index begins with 0, the line index (<code>index</code>) is increased by 1 to get the line number. Also if the display is resized the line number may change but the information here will stay the same since the method is only invoked once.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldAutoSize;
   *
   *     public class TextField_getLineIndexOfCharExample extends Sprite
   *     {
   *         public function TextField_getLineIndexOfCharExample()
   *         {
   *             var myTextField:TextField = new TextField();
   *
   *             myTextField.x = 10;
   *             myTextField.y = 10;
   *             myTextField.width = 200;
   *             myTextField.background = true;
   *             myTextField.border = true;
   *             myTextField.wordWrap = true;
   *             myTextField.autoSize = TextFieldAutoSize.LEFT;
   *
   *             myTextField.appendText("The TextField class is used to create display objects for "
   *                 + "text display and input. All dynamic and input text fields in a SWF file"
   *                 + "are instances of the TextField class. You can use the TextField class "
   *                 + "to perform low-level text rendering. However, in Flex, you typically use "
   *                 + "the Label, Text, TextArea, and TextInput controls to process text. "
   *                 + "You can give a text field an instance name in the Property inspector "
   *                 + "and use the methods and properties of the TextField class to manipulate it with ActionScript. "
   *                 + "TextField instance names are displayed in the Movie Explorer and in the Insert "
   *                 + "Target Path dialog box in the Actions panel.\n\n");
   *
   *             var index:int = myTextField.getLineIndexOfChar(100);
   *             myTextField.appendText("100th character is in line: " +  (index + 1) + "\n");
   *             index = myTextField.getLineIndexOfChar(500);
   *             myTextField.appendText("500th character is in line: " + (index + 1));
   *
   *             this.addChild(myTextField);
   *         }
   *     }
   * }
   * </listing>
   */
  "public function getLineIndexOfChar",function getLineIndexOfChar(charIndex/*:int*/)/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns the number of characters in a specific text line.
   * @param lineIndex The line number for which you want the length.
   *
   * @return The number of characters in the line.
   *
   * @throws RangeError The line number specified is out of range.
   *
   * @example In the following example, once the user selects a line, its line length (number of characters) is displayed in a separate text field.
   * <p>As an illustration, <code>myTextField</code> text field, which displays the text that will be counted, is set to <code>INPUT</code>, meaning users can actually change the lines or add lines between the lines or at the end. (There is an empty line created by using line break (<code>\n</code>) at the end of the last line.) The <code>countLines</code> text field, where the result of counting the line length is displayed, is set below <code>myTextField</code> text field and its text is not selectable. When the user clicks on a line in the <code>myTextField</code> text field, the <code>clickHandler()</code> method is invoked.</p>
   * <p>In the <code>clickHandler()</code> method, the <code>getLineIndexAtPoint()</code> method returns the line index of where the user clicked, by using the <code>localX</code> and <code>localY</code> coordinates of the mouse click. The <code>if</code> statement checks to see if the use has clicked on a character. If so, the <code>getLineLength()</code> method, using the index of line, returns the number of characters in the line. Note that the empty lines between the lines include the second line break (<code>\n</code>) and have a count of 1 character, while the line after the last line has a 0 count. Spaces also count as one character. The users can write a new line or changes a line and get the character count of the line by clicking on it. If text wrap is used and the screen is resized, the line index could change.</p>
   * <listing>
   *  package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldType;
   *     import flash.events.Event;
   *     import flash.events.MouseEvent;
   *
   *     public class TextField_getLineLengthExample extends Sprite {
   *         private var myTextField:TextField = new TextField();
   *         private var countLines:TextField = new TextField();
   *
   *         public function TextField_getLineLengthExample() {
   *             myTextField.x = 10;
   *             myTextField.y = 10;
   *             myTextField.width = 350;
   *             myTextField.height = 150;
   *             myTextField.background = true;
   *             myTextField.border = true;
   *             myTextField.type = TextFieldType.INPUT;
   *
   *             myTextField.appendText("Click on the lines to count its number of characters:\n\n");
   *             myTextField.appendText("This is a short line.\n");
   *             myTextField.appendText("This is a longer line than the last line.\n\n");
   *             myTextField.appendText("This one is even longer than the one before. It has two sentences.\n");
   *
   *             this.addChild(myTextField);
   *
   *             countLines.border = true;
   *             countLines.x = 10;
   *             countLines.y = 180;
   *             countLines.height = 30;
   *             countLines.width = 200;
   *             countLines.background = true;
   *             countLines.selectable = false;
   *
   *            this.addChild(countLines);
   *
   *             myTextField.addEventListener(MouseEvent.CLICK, clickHandler);
   *         }
   *
   *         private function clickHandler(e:MouseEvent):void {
   *             var index:int = myTextField.getLineIndexAtPoint(e.localX, e.localY);
   *
   *             if (index != -1) {
   *             var lenght:int = myTextField.getLineLength(index);
   *
   *             countLines.text = "Number of characters in the line is: " + lenght.toString();
   *             }
   *         }
   *     }
   * }
   * </listing>
   */
  "public function getLineLength",function getLineLength(lineIndex/*:int*/)/*:int*/ {
    return this._lines$4[lineIndex].length;
  },

  /**
   * Returns metrics information about a given text line.
   * @param lineIndex The line number for which you want metrics information.
   *
   * @return A TextLineMetrics object.
   *
   * @throws RangeError The line number specified is out of range.
   *
   * @see TextLineMetrics
   * @see TextLineMetrics
   *
   * @example The following example displays some line metrics values for two differently formatted lines of text.
   * <p>The text appended is two lines from the <i>Song of Myself</i> by Walt Whitman. A new TextFormat object (<code>newFormat</code>) is used to set the format of the second line. The first line holds the default format. The <code>getLineMetrics()</code> method returns a <code>TextLineMetrics</code> object for a specific line. (Line index begins with 0.) Using <code>metrics1</code> and <code>metrics2</code> TextLineMetrics objects for the line one and two, respectively, the ascent, descent, height, and weight value of the line are retrieved and displayed. The result numbers are converted to string but not rounded. Note that this value is for the line and not a specific character. It reflects the range of characters for a line. For example, if a line has different characters with different height formats, the character with the highest height will determine the value. This also means that if one of the character's format is changes, some of the metrics values could also change.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.text.TextLineMetrics;
   *     import flash.text.TextFieldAutoSize;
   *     import flash.text.AntiAliasType;
   *     import flash.text.TextFormat;
   *
   *     public class TextField_getLineMetricsExample extends Sprite {
   *
   *         public function TextField_getLineMetricsExample() {
   *             var myTextField:TextField = new TextField();
   *             var newFormat:TextFormat = new TextFormat();
   *
   *             myTextField.x = 10;
   *             myTextField.y = 10;
   *             myTextField.background = true;
   *             myTextField.wordWrap = false;
   *             myTextField.autoSize = TextFieldAutoSize.LEFT;
   *
   *             myTextField.appendText("A child said What is the grass? fetching it to me with full hands;\n");
   *             myTextField.appendText("How could I answer the child? I do not know what it is any more than he.\n\n");
   *
   *             newFormat.size = 14;
   *             newFormat.font = "Arial";
   *             newFormat.italic = true;
   *             myTextField.setTextFormat(newFormat, 67, 139);
   *
   *             var metrics1:TextLineMetrics = myTextField.getLineMetrics(0);
   *
   *             myTextField.appendText("Metrics ascent for the line 1 is: " + metrics1.ascent.toString() + "\n");
   *             myTextField.appendText("Metrics descent is: " + metrics1.descent.toString() + "\n");
   *             myTextField.appendText("Metrics height is: " + metrics1.height.toString() + "\n");
   *             myTextField.appendText("Metrics width is: " + metrics1.width.toString() + "\n\n");
   *
   *             var metrics2:TextLineMetrics = myTextField.getLineMetrics(1);
   *
   *             myTextField.appendText("Metrics ascent for the line 2 is: " + metrics2.ascent.toString() + "\n");
   *             myTextField.appendText("Metrics descent is: " + metrics2.descent.toString() + "\n");
   *             myTextField.appendText("Metrics height is: " + metrics2.height.toString() + "\n");
   *             myTextField.appendText("Metrics width is: " + metrics2.width.toString() + "\n");
   *
   *             addChild(myTextField);
   *         }
   *     }
   * }
   * </listing>
   */
  "public function getLineMetrics",function getLineMetrics(lineIndex/*:int*/)/*:TextLineMetrics*/ {
    if (!$$private.lineMetricsContext) {
      $$private.lineMetricsContext =/* js.CanvasRenderingContext2D*/(/*js.HTMLCanvasElement*/(window.document.createElement("CANVAS")).getContext("2d"));
    }
    $$private.lineMetricsContext.font = this.asWebFont$4();
    var width/*:int*/ = $$private.lineMetricsContext.measureText(this._lines$4[lineIndex]).width;
    return new flash.text.TextLineMetrics(0, width, this.getSize$4(), 0, 0, 0);
  },

  "private function getSize",function getSize()/*:int*/ {
    return $$int(this._textFormat$4.size !== null ? this._textFormat$4.size : this._defaultTextFormat$4.size);
  },

  /**
   * Returns the character index of the first character in the line that the <code>lineIndex</code> parameter specifies.
   * @param lineIndex The zero-based index value of the line (for example, the first line is 0, the second line is 1, and so on).
   *
   * @return The zero-based index value of the first character in the line.
   *
   * @throws RangeError The line number specified is out of range.
   *
   * @example The following example checks for the first character of the line 4, which will change if the screen (and the text field) is resized.
   * <p>The <code>myTextField</code> text field is set to word wrap. The <code>countField</code> text field will display the first character of line 4. When the user clicks on the <code>myTextField</code> text field, the <code>clickHandler()</code> method is invoked.</p>
   * <p>In the <code>clickHandler()</code> method, the <code>getLineOffset()</code> method returns the index of the first character in the line index 3, which is the fourth line of the text. (First line has a 0 index.) The <code>charAt()</code> method is used to get the character using the index of the first character of the fourth line. The <code>countField</code> text field content is updated with this information using the <code>text</code> property of the <code>countField</code> text field. Using the <code>countField.text</code> property means that each time after the click the content of the <code>countField</code> text field will be overwritten. If the user resizes the display, the content will wrap and the first character of the line 4 could change. By clicking again on the <code>myTextField</code> field, the content of <code>countField</code> text field is updated with the new first character for the fourth line.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.events.MouseEvent;
   *
   *     public class TextField_getLineOffsetExample extends Sprite {
   *         private var myTextField:TextField = new TextField();
   *         private var countField:TextField = new TextField();
   *
   *         public function TextField_getLineOffsetExample() {
   *             myTextField.x = 10;
   *             myTextField.y = 10;
   *             myTextField.width = 150;
   *             myTextField.height = 300;
   *             myTextField.background = true;
   *             myTextField.border = true;
   *             myTextField.wordWrap = true;
   *
   *             countField.height = 20;
   *             countField.width = 200;
   *             countField.x = 10;
   *             countField.y = 320;
   *             countField.selectable = false;
   *
   *             myTextField.appendText("The TextField class is used to create display objects for "
   *                         + "text display and input. All dynamic and input text fields in a SWF file "
   *                         + "are instances of the TextField class. You can use the TextField class "
   *                         + "to perform low-level text rendering. However, in Flex, you typically use "
   *                         + "the Label, Text, TextArea, and TextInput controls to process text. "
   *                         + "You can give a text field an instance name in the Property inspector "
   *                         + "and use the methods and properties of the TextField class to manipulate it with ActionScript.");
   *
   *             myTextField.addEventListener(MouseEvent.CLICK, clickHandler);
   *
   *             this.addChild(myTextField);
   *             this.addChild(countField);
   *         }
   *
   *             private function clickHandler(e:MouseEvent):void {
   *                 var c:String;
   *                 var index:int;
   *
   *                 index = myTextField.getLineOffset(3);
   *                 c = myTextField.text.charAt(index);
   *                 countField.text = "The first character of line 4 is: " + c;
   *             }
   *     }
   * }
   * </listing>
   */
  "public function getLineOffset",function getLineOffset(lineIndex/*:int*/)/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns the text of the line specified by the <code>lineIndex</code> parameter.
   * @param lineIndex The zero-based index value of the line (for example, the first line is 0, the second line is 1, and so on).
   *
   * @return The text string contained in the specified line.
   *
   * @throws RangeError The line number specified is out of range.
   *
   * @example In the following example, the line numbers of all the instances of the word "love" used in Shakespeare's sonnet are found and displayed.
   * <p>The <code>poem</code> text field is set to fit automatically the text and to resize as a left-justified text. The <code>wordWrap</code> property is set to <code>false</code>, so the lines of the poem would not wrap, though normally when using the <code>autoSize</code> property, this should not be a problem. The <code>for</code> loop iterates through the lines of the sonnet using the property <code>numLines</code> of the text field. The <code>getLineText()</code> method returns the content of the line as a string. (Note that the <code>numLines</code> property returns the number of lines starting with line 1, while for the <code>getLineText()</code> method the line number begins with 0.) Using the regular expression pattern (<code>/love/i</code>), the <code>if</code> statement looks for any substring of the word in upper or lowercase. If the pattern is found, the <code>search</code> method returns the index of the first matching substring, otherwise it returns <code>-1</code> (if there is no match). The line number where "love" was found (<code>(i + 1)</code>) is then placed in the string <code>lineResult</code>. The string method converts the number argument (<code>(i + 1)</code>) to a string as long as there is another argument that is a string (" "). The line result of the search will include lines with the words "loved" or "Love's." If the string "Love was found in lines:" was appended before the <code>for</code> loop, the word "Love" in this line would also have been included.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldAutoSize;
   *     import flash.utils.Timer;
   *     import flash.events.TimerEvent;
   *
   *     public class TextField_getLineTextExample extends Sprite {
   *
   *         public function TextField_getLineTextExample() {
   *            var poem:TextField = new TextField();
   *            var lineResult:String = "";
   *            var pattern:RegExp = /love/i;
   *
   *             poem.x = 10;
   *             poem.y = 10;
   *             poem.background = true;
   *             poem.wordWrap = false;
   *             poem.autoSize = TextFieldAutoSize.LEFT;
   *
   *             poem.text = "Let me not to the marriage of true minds\n"
   *                               + "Admit impediments. love is not love\n"
   *                               + "Which alters when it alteration finds\n"
   *                               + "Or bends with the remover to remove:\n"
   *                               + "O no! it is an ever-fixed mark\n"
   *                               + "That looks on tempests and is never shaken;\n"
   *                               + "It is the star to every wandering bark,\n"
   *                               + "Whose worth's unknown, although his height be taken.\n"
   *                               + "Love's not Time's fool, though rosy lips and cheeks\n"
   *                               + "Within his bending sickle's compass come:\n"
   *                               + "Love alters not with his brief hours and weeks,\n"
   *                               + "But bears it out even to the edge of doom.\n"
   *                               + "If this be error and upon me proved,\n"
   *                               + "I never writ, nor no man ever loved.\n\n";
   *
   *             for (var i:int = 0; i < poem.numLines; i++) {
   *
   *                 var s:String = poem.getLineText(i);
   *
   *                 if(s.search(pattern) != -1) {
   *                     lineResult += (i + 1) + " ";
   *                 }
   *             }
   *
   *             poem.appendText("Love was found in lines: " + lineResult);
   *
   *             this.addChild(poem);
   *         }
   *     }
   * }
   * </listing>
   */
  "public function getLineText",function getLineText(lineIndex/*:int*/)/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Given a character index, returns the length of the paragraph containing the given character. The length is relative to the first character in the paragraph (as returned by <code>getFirstCharInParagraph()</code>), not to the character index passed in.
   * @param charIndex The zero-based index value of the character (for example, the first character is 0, the second character is 1, and so on).
   *
   * @return Returns the number of characters in the paragraph.
   *
   * @throws RangeError The character index specified is out of range.
   *
   * @see #getFirstCharInParagraph()
   *
   * @example In the following example, when a user selects a paragraph, the paragraph's length and number of "s" characters in the paragraph are displayed in a separate text field.
   * <p>The <code>myTextField</code> text field displays the paragraphs that the user will select. When the user click on the text field, the <code>MouseEvent.CLICK</code> event is dispatched, and the <code>clickHandler()</code> method is called. The paragraph length and number of "s" characters will appear in <code>countField</code> text field, which is placed below <code>myTextField</code> text field.</p>
   * <p>In the <code>clickHandler()</code> method, the <code>getCharIndexAtPoint()</code> method returns the character's index based on the <code>localX</code> and <code>localY</code> coordinates of the mouse click. The first <code>if</code> statement checks to see if the use has clicked on a character. The <code>getFirstCharInParagraph()</code> method, uses this index to return the index of the first character in the same paragraph. The paragraph length returned by <code>getParagraphLength()</code> method is used with the index of the first character in the paragraph to determine the index for the end of the paragraph. A <code>for</code> loop iterates through the paragraph looking for the number of "s" characters.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.events.MouseEvent;
   *
   *     public class TextField_getParagraphLengthExample extends Sprite {
   *         private var myTextField:TextField = new TextField();
   *         private var countField:TextField = new TextField();
   *
   *         public function TextField_getParagraphLengthExample() {
   *             myTextField.x = 10;
   *             myTextField.y = 10;
   *             myTextField.background = true;
   *             myTextField.border = true;
   *             myTextField.wordWrap = true;
   *             myTextField.width = 300;
   *             myTextField.height = 280;
   *
   *             myTextField.appendText("The TextField class is used to create display objects for "
   *                         + "text display and input. All dynamic and input text fields in a SWF file"
   *                         + "are instances of the TextField class. You can use the TextField class "
   *                         + "to perform low-level text rendering. However, in Flex, you typically use "
   *                         + "the Label, Text, TextArea, and TextInput controls to process text. "
   *                         + "You can give a text field an instance name in the Property inspector "
   *                         + "and use the methods and properties of the TextField class to manipulate it with ActionScript. "
   *                         + "TextField instance names are displayed in the Movie Explorer and in the Insert "
   *                         + "Target Path dialog box in the Actions panel.\n\n"
   *                         + "To create a text field dynamically, use the TextField() constructor.\n\n"
   *                         + "The methods of the TextField class let you set, select, and manipulate "
   *                         + "text in a dynamic or input text field that you create during authoring or at runtime.");
   *
   *             myTextField.addEventListener(MouseEvent.CLICK, clickHandler);
   *
   *             countField.x = 10;
   *             countField.y = 300;
   *             countField.height = 50;
   *             countField.width = 250;
   *             countField.background = true;
   *             countField.selectable = false;
   *
   *             this.addChild(myTextField);
   *             this.addChild(countField);
   *         }
   *
   *         private function clickHandler(e:MouseEvent):void {
   *             var index:int = myTextField.getCharIndexAtPoint(e.localX, e.localY);
   *
   *             if(index != -1) {
   *                 var beginParag:int = myTextField.getFirstCharInParagraph(index);
   *                 var paragLength:int = myTextField.getParagraphLength(index);
   *                 var endParag:int = beginParag + paragLength;
   *                 var sCount:uint = 0;
   *
   *                 for (var i:int = beginParag; i <= endParag; i++) {
   *                     if ((myTextField.text.charAt(i) == "s") || (myTextField.text.charAt(i) == "S")) {
   *                         sCount++;
   *                     }
   *
   *                 countField.text = "Paragraph length is: " + paragLength.toString() + "\n"
   *                         + "Number of 's' characters in the paragraph: " + sCount.toString();
   *                 }
   *             }
   *         }
   *     }
   * }
   * </listing>
   */
  "public function getParagraphLength",function getParagraphLength(charIndex/*:int*/)/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns a TextFormat object that contains formatting information for the range of text that the <code>beginIndex</code> and <code>endIndex</code> parameters specify. Only properties that are common to the entire text specified are set in the resulting TextFormat object. Any property that is <i>mixed</i>, meaning that it has different values at different points in the text, has a value of <code>null</code>.
   * <p>If you do not specify values for these parameters, this method is applied to all the text in the text field.</p>
   * <p>The following table describes three possible usages:</p>
   * <table>
   * <tr><th>Usage</th><th>Description</th></tr>
   * <tr>
   * <td><code>my_textField.getTextFormat()</code></td>
   * <td>Returns a TextFormat object containing formatting information for all text in a text field. Only properties that are common to all text in the text field are set in the resulting TextFormat object. Any property that is <i>mixed</i>, meaning that it has different values at different points in the text, has a value of <code>null</code>.</td></tr>
   * <tr>
   * <td><code>my_textField.getTextFormat(beginIndex:Number)</code></td>
   * <td>Returns a TextFormat object containing a copy of the text format of the character at the <code>beginIndex</code> position.</td></tr>
   * <tr>
   * <td><code>my_textField.getTextFormat(beginIndex:Number,endIndex:Number)</code></td>
   * <td>Returns a TextFormat object containing formatting information for the span of text from <code>beginIndex</code> to <code>endIndex-1</code>. Only properties that are common to all of the text in the specified range are set in the resulting TextFormat object. Any property that is mixed (that is, has different values at different points in the range) has its value set to <code>null</code>.</td></tr></table>
   * @param beginIndex Optional; an integer that specifies the starting location of a range of text within the text field.
   * @param endIndex Optional; an integer that specifies the position of the first character after the desired text span. As designed, if you specify <code>beginIndex</code> and <code>endIndex</code> values, the text from <code>beginIndex</code> to <code>endIndex-1</code> is read.
   *
   * @return The TextFormat object that represents the formatting properties for the specified text.
   *
   * @throws RangeError The <code>beginIndex</code> or <code>endIndex</code> specified is out of range.
   *
   * @see TextFormat
   * @see #defaultTextFormat
   * @see #setTextFormat()
   *
   * @example <a href="http://www.adobe.com/go/learn_as3_usingexamples_en">How to use this example</a>Please see the <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/text/TextField.html#getFirstCharInParagraph()">getFirstCharInParagraph()</a> or <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/text/TextField.html#setTextFormat()">setTextFormat()</a> method example for illustrations of how to use the <code>getTextFormat()</code> method.
   */
  "public function getTextFormat",function getTextFormat(beginIndex/*:int = -1*/, endIndex/*:int = -1*/)/*:TextFormat*/ {if(arguments.length<2){if(arguments.length<1){beginIndex = -1;}endIndex = -1;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Replaces the current selection with the contents of the <code>value</code> parameter. The text is inserted at the position of the current selection, using the current default character format and default paragraph format. The text is not treated as HTML.
   * <p>You can use the <code>replaceSelectedText()</code> method to insert and delete text without disrupting the character and paragraph formatting of the rest of the text.</p>
   * <p><b>Note:</b> This method does not work if a style sheet is applied to the text field.</p>
   * @param value The string to replace the currently selected text.
   *
   * @throws Error This method cannot be used on a text field with a style sheet.
   *
   * @see flash.display.Stage#focus
   *
   * @example In the following example, the user erases some text from the first text field by selecting it and replaces a selected text in the second text field with "NEW TEXT" string.
   * <p>Two different TextField objects are created and event listeners are added for the <code>MouseEvent.MOUSE_UP</code> events. Mouse up occurs when the user releases the mouse, an event that normally happens after a selection of text is made. Note that the default setting for a text field is for its text to be selected.</p>
   * <p>In the <code>mouseHandler1()</code> method, when a user release a mouse in the <code>myTextField1</code> text field, the text is erased by replacing it with an empty string. This can continue until all the text is erased. In the <code>mouseHandler2()</code> method, when a user selects some text in <code>myTextField2</code> text field, properties <code>selectionBeginIndex</code> and <code>selectionEndIndex</code> are checked to see if any character was selected. (The <code>selectionBeginIndex</code> and <code>selectionEndIndex</code> properties don't have the same value if some text were selected.) The selected text is then replaced with "NEW TEXT" string. This can continue until all the original text of the second text field is replaced with the "NEW TEXT" string.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.events.MouseEvent;
   *
   *     public class TextField_replaceSelectedTextExample extends Sprite {
   *         private var myTextField1:TextField = new TextField();
   *         private var myTextField2:TextField = new TextField();
   *
   *         public function TextField_replaceSelectedTextExample() {
   *             myTextField1.x = 10;
   *             myTextField1.width = 300;
   *             myTextField1.height = 50;
   *             myTextField1.background = true;
   *             myTextField1.border = true;
   *             myTextField1.text = "Select the text you want to remove from the line.";
   *
   *             myTextField2.x = 10;
   *             myTextField2.y = 60;
   *             myTextField2.width = 300;
   *             myTextField2.height = 50;
   *             myTextField2.background = true;
   *             myTextField2.border = true;
   *             myTextField2.text = "Select the text you want to replace with NEW TEXT.";
   *
   *             myTextField1.addEventListener(MouseEvent.MOUSE_UP, mouseHandler1);
   *             myTextField2.addEventListener(MouseEvent.MOUSE_UP, mouseHandler2);
   *
   *             this.addChild(myTextField1);
   *             this.addChild(myTextField2);
   *         }
   *
   *         private function mouseHandler1(e:MouseEvent):void {
   *             myTextField1.replaceSelectedText("");
   *         }
   *
   *         private function mouseHandler2(e:MouseEvent):void {
   *             if(myTextField2.selectionBeginIndex != myTextField2.selectionEndIndex) {
   *                 myTextField2.replaceSelectedText("NEW TEXT");
   *             }
   *         }
   *     }
   * }
   * </listing>
   */
  "public function replaceSelectedText",function replaceSelectedText(value/*:String*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Replaces the range of characters that the <code>beginIndex</code> and <code>endIndex</code> parameters specify with the contents of the <code>newText</code> parameter. As designed, the text from <code>beginIndex</code> to <code>endIndex-1</code> is replaced.
   * <p><b>Note:</b> This method does not work if a style sheet is applied to the text field.</p>
   * @param beginIndex The zero-based index value for the start position of the replacement range.
   * @param endIndex The zero-based index position of the first character after the desired text span.
   * @param newText The text to use to replace the specified range of characters.
   *
   * @throws Error This method cannot be used on a text field with a style sheet.
   *
   * @example The following example uses the <code>replaceText()</code> method to delete, replace and insert some text into a text field.
   * <p>The <code>outputText</code> text field is set to automatically fit the text and to resize as a left-justified text. With the first <code>replaceText()</code> method call, the first line ("This is the wrong heading") is replaced with "THIS IS THE HEADING FOR EVERYONE." With the second method call, the text "CORRECT" is inserted between "THE" and "HEADING." With the third method call, the words "FOR EVERYONE" are deleted. Note that with each call to the method <code>appendText()</code>, the current text's begin and end index are changed. Here, only the final text (after the changes have been made) will display.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldAutoSize;
   *
   *     public class TextField_replaceTextExample extends Sprite {
   *
   *         public function TextField_replaceTextExample() {
   *             var outputText:TextField = new TextField();
   *
   *             outputText.x = 10;
   *             outputText.y = 10;
   *             outputText.background = true;
   *             outputText.autoSize = TextFieldAutoSize.LEFT;
   *
   *             outputText.appendText("This is the wrong heading");
   *             outputText.appendText("\n\n");
   *             outputText.appendText("This is the body of the text.");
   *
   *             outputText.replaceText(0, 25, "THIS IS THE HEADING FOR EVERYONE");
   *
   *             outputText.replaceText(12, 12, "CORRECT ");
   *
   *             outputText.replaceText(27, 40, "");
   *
   *            this.addChild(outputText);
   *          }
   *     }
   * }
   * </listing>
   */
  "public function replaceText",function replaceText(beginIndex/*:int*/, endIndex/*:int*/, newText/*:String*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Sets as selected the text designated by the index values of the first and last characters, which are specified with the <code>beginIndex</code> and <code>endIndex</code> parameters. If the two parameter values are the same, this method sets the insertion point, as if you set the <code>caretIndex</code> property.
   * @param beginIndex The zero-based index value of the first character in the selection (for example, the first character is 0, the second character is 1, and so on).
   * @param endIndex The zero-based index value of the last character in the selection.
   *
   * @see #selectable
   * @see #selectionBeginIndex
   * @see #selectionEndIndex
   * @see #caretIndex
   *
   * @example In the following example, when the user clicks anywhere in the text field a predefined range of text will be selected (highlighting the words "TEXT IN ALL CAPS").
   * <p>Two event listeners for the <code>myTextField</code> text field respond to the user's mouse clicks or mouse up events. Mouse up will occur when the user releases the mouse, an event that normally happens after a selection of text is made. Note that the default setting for a text field is for its text to be selected. When some text is clicked, <code>clickHandler()</code> method is invoked. When some text is selected and the mouse is released, <code>mouseUpHandler()</code> method is invoked.</p>
   * <p>In both <code>clickHandler()</code> and <code>mouseUpHandler()</code> methods, the <code>setSelection()</code> method sets only the characters between indexes 54 and 70 (TEXT IN ALL CAPS) to be selected.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.events.MouseEvent;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldAutoSize;
   *
   *     public class TextField_setSelectionExample extends Sprite
   *     {
   *         private var myTextField:TextField = new TextField();
   *
   *         public function TextField_setSelectionExample() {
   *             myTextField.autoSize = TextFieldAutoSize.LEFT;
   *             myTextField.text = "No matter where you click on this text field only the TEXT IN ALL CAPS is selected.";
   *
   *             myTextField.addEventListener(MouseEvent.CLICK, clickHandler);
   *             myTextField.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
   *
   *             this.addChild(myTextField);
   *         }
   *
   *         private function clickHandler(event:MouseEvent):void {
   *             myTextField.setSelection(54, 70);
   *         }
   *
   *         private function mouseUpHandler(event:MouseEvent):void {
   *             myTextField.setSelection(54, 70);
   *         }
   *
   *     }
   * }
   * </listing>
   */
  "public function setSelection",function setSelection(beginIndex/*:int*/, endIndex/*:int*/)/*:void*/ {
    this._selectionBeginIndex$4 = beginIndex;
    this._selectionEndIndex$4 = endIndex;
    // TODO: implement actual selection!
  },

  /**
   * Applies the text formatting that the <code>format</code> parameter specifies to the specified text in a text field. The value of <code>format</code> must be a TextFormat object that specifies the desired text formatting changes. Only the non-null properties of <code>format</code> are applied to the text field. Any property of <code>format</code> that is set to <code>null</code> is not applied. By default, all of the properties of a newly created TextFormat object are set to <code>null</code>.
   * <p><b>Note:</b> This method does not work if a style sheet is applied to the text field.</p>
   * <p>The <code>setTextFormat()</code> method changes the text formatting applied to a range of characters or to the entire body of text in a text field. To apply the properties of format to all text in the text field, do not specify values for <code>beginIndex</code> and <code>endIndex</code>. To apply the properties of the format to a range of text, specify values for the <code>beginIndex</code> and the <code>endIndex</code> parameters. You can use the <code>length</code> property to determine the index values.</p>
   * <p>The two types of formatting information in a TextFormat object are character level formatting and paragraph level formatting. Each character in a text field can have its own character formatting settings, such as font name, font size, bold, and italic.</p>
   * <p>For paragraphs, the first character of the paragraph is examined for the paragraph formatting settings for the entire paragraph. Examples of paragraph formatting settings are left margin, right margin, and indentation.</p>
   * <p>Any text inserted manually by the user, or replaced by the <code>replaceSelectedText()</code> method, receives the default text field formatting for new text, and not the formatting specified for the text insertion point. To set the default formatting for new text, use <code>defaultTextFormat</code>.</p>
   * @param format A TextFormat object that contains character and paragraph formatting information.
   * @param beginIndex Optional; an integer that specifies the zero-based index position specifying the first character of the desired range of text.
   * @param endIndex Optional; an integer that specifies the first character after the desired text span. As designed, if you specify <code>beginIndex</code> and <code>endIndex</code> values, the text from <code>beginIndex</code> to <code>endIndex-1</code> is updated.
   * <table>
   * <tr><th>Usage</th><th>Description</th></tr>
   * <tr>
   * <td><code>my_textField.setTextFormat(textFormat:TextFormat)</code></td>
   * <td>Applies the properties of <code>textFormat</code> to all text in the text field.</td></tr>
   * <tr>
   * <td><code>my_textField.setTextFormat(textFormat:TextFormat, beginIndex:int)</code></td>
   * <td>Applies the properties of <code>textFormat</code> to the text starting with the <code>beginIndex</code> position.</td></tr>
   * <tr>
   * <td><code>my_textField.setTextFormat(textFormat:TextFormat, beginIndex:int, endIndex:int)</code></td>
   * <td>Applies the properties of the <code>textFormat</code> parameter to the span of text from the <code>beginIndex</code> position to the <code>endIndex-1</code> position.</td></tr></table>
   * <p>Notice that any text inserted manually by the user, or replaced by the <code>replaceSelectedText()</code> method, receives the default text field formatting for new text, and not the formatting specified for the text insertion point. To set a text field's default formatting for new text, use the <code>defaultTextFormat</code> property.</p>
   *
   * @throws Error This method cannot be used on a text field with a style sheet.
   * @throws RangeError The <code>beginIndex</code> or <code>endIndex</code> specified is out of range.
   *
   * @see TextFormat
   * @see #defaultTextFormat
   *
   * @example In the following example, when the text is clicked, a defined range of text, "TEXT IN ALL CAPS," switches format between the default text format and the new format.
   * <p>An event listener for the <code>myTextField</code> text field is added to respond to the mouse clicks by invoking the <code>clickHandler()</code> method. In the <code>clickHandler()</code> method, the <code>getTextFormat()</code> method returns the current format of a character (index 55) from the intended range of the text, which is then placed in the <code>currentTextFormat</code> TextFormat object. The <code>if</code> statement checks the <code>currentTextFormat</code> text format to see if the character in the range is using the new format (font point is set to 18). If not, the new format changes the size to 18 point, color to red, and applies underline and italics to the range of text between 54-70 (TEXT IN ALL CAPS). If the character in the range is using the new format, the format of the range is set back to the default (original) format of the text field.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.text.TextFormat;
   *     import flash.text.TextFieldAutoSize;
   *     import flash.events.MouseEvent;
   *
   *     public class TextField_setTextFormatExample extends Sprite {
   *         private var myTextField:TextField = new TextField();
   *         private var newFormat:TextFormat = new TextFormat();
   *
   *         public function TextField_setTextFormatExample() {
   *             myTextField.autoSize = TextFieldAutoSize.LEFT;
   *             myTextField.selectable = false;
   *             myTextField.background = true;
   *             myTextField.text = "No matter where you click on this text field only the TEXT IN ALL CAPS changes format.";
   *
   *             myTextField.addEventListener(MouseEvent.CLICK, clickHandler);
   *
   *             newFormat.color = 0xFF0000;
   *             newFormat.size = 18;
   *             newFormat.underline = true;
   *             newFormat.italic = true;
   *
   *             this.addChild(myTextField);
   *         }
   *
   *         private function clickHandler(event:MouseEvent):void {
   *             var currentTextFormat:TextFormat = myTextField.getTextFormat(55);
   *
   *             if(currentTextFormat.size != 18) {
   *                 myTextField.setTextFormat(newFormat, 54, 70);
   *             }
   *             else {
   *                 myTextField.setTextFormat(myTextField.defaultTextFormat);
   *             }
   *         }
   *     }
   * }
   * </listing>
   */
  "public function setTextFormat",function setTextFormat(format/*:TextFormat*/, beginIndex/*:int = -1*/, endIndex/*:int = -1*/)/*:void*/ {if(arguments.length<3){if(arguments.length<2){beginIndex = -1;}endIndex = -1;}
    // TODO: beginIndex, endIndex
    this._textFormat$4 = format;
    if (this.hasElement()) {
      this.syncTextFormat$4(this.getElement());
    }
  },

  // ************************** Jangaroo part **************************

  "private function asWebFont",function asWebFont()/*:String*/ {
    var webFont/*:String*/ = this.getSize$4() + "px ";
    switch (this._textFormat$4.font !== null ? this._textFormat$4.font : this._defaultTextFormat$4.font) {
      case "Times New Roman":
        webFont += "Times New Roman,serif"; break;
      case "Arial":
      case "Helvetica":
        webFont += "Helvetica,Arial,sans-serif"; break;
      case "system":
        // system font cannot be resized when drawing into canvas, so use console or monospace instead:
        webFont += "console,monospace"; break;
    }
    return webFont;
  },

  /**
   * @private
   */
  "override protected function createElement",function createElement()/*:HTMLElement*/ {
    var elem/*:HTMLElement*/ = this.createElement$4();
    elem.style.padding = "2px";
    this.syncTextFormat$4(elem);
    return elem;
  },

  "private function syncTextFormat",function syncTextFormat(element/*:HTMLElement*/)/*:void*/ {
    $$private.updateElementProperty(element, "style.font", this.asWebFont$4());
    $$private.updateElementProperty(element, "style.color", flash.display.Graphics.toRGBA(this.textColor));
    var bold/*:Boolean*/ = this._textFormat$4.bold !== null ? this._textFormat$4.bold : this._defaultTextFormat$4.bold;
    $$private.updateElementProperty(element, "style.fontWeight", bold ? "bold" : "normal");
    var italic/*:Boolean*/ = this._textFormat$4.italic !== null ? this._textFormat$4.italic : this._defaultTextFormat$4.italic;
    $$private.updateElementProperty(element, "style.fontStyle", italic ? "italic" : "normal");
    var align/*:Boolean*/ = this._textFormat$4.align !== null ? this._textFormat$4.align : this._defaultTextFormat$4.align;
    $$private.updateElementProperty(element, "style.textAlign", align);
  },

  /**
   * @private
   */
  "override protected function getElementName",function getElementName()/*:String*/ {
    return "span";
  },

  "private static function updateElementProperty",function updateElementProperty(element/* : HTMLElement*/, propertyPath/* : String*/, value/* : Object*/)/* : void*/ {
    var current/* : Object*/ = element;
    var propertyPathArcs/* : Array*/ = propertyPath.split(".");
    var lastIndex/* : uint*/ = propertyPathArcs.length - 1;
    for (var i/*:uint*/ =0; i<lastIndex; ++i) {
      current = current[propertyPathArcs[i]];
    }
    current[propertyPathArcs[lastIndex]] = value;
  },

  "private var",{ _alwaysShowSelection/*:Boolean*/:false},
  "private var",{ _antiAliasType/*:String*/:null},
  "private var",{ _autoSize/*:String*/:null},
  "private var",{ _background/*:Boolean*/:false},
  "private var",{ _backgroundColor/*:uint*/:0},
  "private var",{ _border/*:Boolean*/:false},
  "private var",{ _borderColor/*:uint*/:0},
  "private var",{ _bottomScrollV/*:int*/:0},
  "private var",{ _caretIndex/*:int*/:0},
  "private var",{ _condenseWhite/*:Boolean*/:false},
  "private var",{ _displayAsPassword/*:Boolean*/:false},
  "private var",{ _embedFonts/*:Boolean*/:false},
  "private var",{ _gridFitType/*:String*/:null},
  "private var",{ _htmlText/*:String*/:null},
  "private var",{ _length/*:int*/:0},
  "private var",{ _maxChars/*:int*/:0},
  "private var",{ _maxScrollH/*:int*/:0},
  "private var",{ _maxScrollV/*:int*/:0},
  "private var",{ _mouseWheelEnabled/*:Boolean*/:false},
  "private var",{ _multiline/*:Boolean*/ : false},
  "private var",{ _restrict/*:String*/:null},
  "private var",{ _scrollH/*:int*/:0},
  "private var",{ _scrollV/*:int*/:0},
  "private var",{ _selectable/*:Boolean*/:false},
  "private var",{ _selectionBeginIndex/*:int*/:0},
  "private var",{ _selectionEndIndex/*:int*/:0},
  "private var",{ _sharpness/*:Number*/:NaN},
  "private var",{ _defaultTextFormat/*:TextFormat*/ :function(){return( new flash.text.TextFormat("Times New Roman", 12, 0, false, false, false, "", "", flash.text.TextFormatAlign.LEFT, 0, 0, 0, 0));}},
  "private var",{ _textFormat/*:TextFormat*/ :function(){return( new flash.text.TextFormat());}},
  "private var",{ _lines/*:Array*/:null}/*String*/,
  "private var",{ _styleSheet/*:StyleSheet*/:null},
  "private var",{ _textHeight/*:Number*/:NaN},
  "private var",{ _textWidth/*:Number*/:NaN},
  "private var",{ _thickness/*:Number*/:NaN},
  "private var",{ _type/*:String*/:null},
  "private var",{ _useRichTextClipboard/*:Boolean*/:false},
  "private var",{ _wordWrap/*:Boolean*/:false},

  "private static var",{ lineMetricsContext/*:CanvasRenderingContext2D*/:null},
];},[],["flash.display.InteractiveObject","flash.display.Graphics","uint","Error","js.CanvasRenderingContext2D","js.HTMLCanvasElement","flash.text.TextLineMetrics","int","flash.text.TextFormat","flash.text.TextFormatAlign"], "0.8.0", "0.8.3"
);