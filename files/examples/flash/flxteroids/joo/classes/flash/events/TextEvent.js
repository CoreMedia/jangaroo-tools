joo.classLoader.prepare("package flash.events",/* {*/



/**
 * An object dispatches a TextEvent object when a user enters text in a text field or clicks a hyperlink in an HTML-enabled text field. There are two types of text events: <code>TextEvent.LINK</code> and <code>TextEvent.TEXT_INPUT</code>.
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/TextEvent.html#includeExamplesSummary">View the examples</a></p>
 * @see flash.text.TextField
 *
 */
"public class TextEvent extends flash.events.Event",2,function($$private){;return[ 
  /**
   * For a <code>textInput</code> event, the character or sequence of characters entered by the user. For a <code>link</code> event, the text of the <code>event</code> attribute of the <code>href</code> attribute of the <code><a></code> tag.
   * @example The following code shows that the <code>link</code> event is dispatched when a user clicks the hypertext link:
   * <listing>
   *     import flash.text.TextField;
   *     import flash.events.TextEvent;
   *
   *     var tf:TextField = new TextField();
   *     tf.htmlText = "<a href='event:myEvent'>Click Me.</a>";
   *     tf.addEventListener("link", clickHandler);
   *     addChild(tf);
   *
   *     function clickHandler(e:TextEvent):void {
   *         trace(e.type); // link
   *         trace(e.text); // myEvent
   *     }
   *    </listing>
   */
  "public native function get text"/*():String;*/,

  /**
   * @private
   */
  "public native function set text"/*(value:String):void;*/,

  /**
   * Creates an Event object that contains information about text events. Event objects are passed as parameters to event listeners.
   * @param type The type of the event. Event listeners can access this information through the inherited <code>type</code> property. Possible values are: <code>TextEvent.LINK</code> and <code>TextEvent.TEXT_INPUT</code>.
   * @param bubbles Determines whether the Event object participates in the bubbling phase of the event flow. Event listeners can access this information through the inherited <code>bubbles</code> property.
   * @param cancelable Determines whether the Event object can be canceled. Event listeners can access this information through the inherited <code>cancelable</code> property.
   * @param text One or more characters of text entered by the user. Event listeners can access this information through the <code>text</code> property.
   *
   * @see flash.text.TextField
   *
   */
  "public function TextEvent",function TextEvent$(type/*:String*/, bubbles/*:Boolean = false*/, cancelable/*:Boolean = false*/, text/*:String = ""*/) {if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){bubbles = false;}cancelable = false;}text = "";}
    this.super$2(type, bubbles, cancelable);
    this.text = text;
  },

  /**
   * Creates a copy of the TextEvent object and sets the value of each property to match that of the original.
   * @return A new TextEvent object with property values that match those of the original.
   *
   */
  "override public function clone",function clone()/*:Event*/ {
    return new flash.events.TextEvent(this.type, this.bubbles, this.cancelable, this.text);
  },

  /**
   * Returns a string that contains all the properties of the TextEvent object. The string is in the following format:
   * <p><code>[TextEvent type=<i>value</i> bubbles=<i>value</i> cancelable=<i>value</i> text=<i>value</i>]</code></p>
   * @return A string that contains all the properties of the TextEvent object.
   *
   */
  "override public function toString",function toString()/*:String*/ {
    return this.formatToString("TextEvent", "type", "bubbles", "cancelable", "text");
  },

  /**
   * Defines the value of the <code>type</code> property of a <code>link</code> event object.
   * <p>This event has the following properties:</p>
   * <table>
   * <tr><th>Property</th><th>Value</th></tr>
   * <tr>
   * <td><code>bubbles</code></td>
   * <td><code>true</code></td></tr>
   * <tr>
   * <td><code>cancelable</code></td>
   * <td><code>false</code>; there is no default behavior to cancel.</td></tr>
   * <tr>
   * <td><code>currentTarget</code></td>
   * <td>The object that is actively processing the Event object with an event listener.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The text field containing the hyperlink that has been clicked. The <code>target</code> is not always the object in the display list that registered the event listener. Use the <code>currentTarget</code> property to access the object in the display list that is currently processing the event.</td></tr>
   * <tr>
   * <td><code>text</code></td>
   * <td>The remainder of the URL after "event:"</td></tr></table>
   * @see #text
   * @see flash.text.TextField#event:link
   *
   * @example In this example, when a user clicks a hyperlink in HTML text, it triggers a text event. Depending on the link, the user is sent to a designated website based on the system's operating system, or a circle is drawn based on the user's selected radius.
   * <p>A text field is created and its content is set to an HTML-formatted string by using the <code>htmlText</code> property. The links are underlined for easier identification by the user. (Adobe Flash Player changes the mouse pointer only after the pointer is over the link.) To make sure that the user's click invokes an ActionScript method, the URL of the link begins with the <code>"event:"</code> string and a listener is added for the <code>TextEvent.LINK</code> event.</p>
   * <p>The <code>linkHandler()</code> method that is triggered after the user clicks a link manages all the link events for the text field. The first if statement checks the <code>text</code> property of the event, which holds the remainder of the URL after the <code>"event:"</code> string. If the user clicked the link for the operating system, the name of the user's current operating system, taken from the system's <code>Capabilities.os</code> property, is used to send the user to the designated website. Otherwise, the selected radius size, passed by the event's <code>text</code> property, is used to draw a circle below the text field. Each time the user clicks the radius link, the previously drawn circle is cleared and a new red circle with the selected radius size is drawn.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.events.TextEvent;
   *     import flash.errors.IOError;
   *     import flash.events.IOErrorEvent;
   *     import flash.system.Capabilities;
   *     import flash.net.navigateToURL;
   *     import flash.net.URLRequest;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldAutoSize;
   *     import flash.display.Shape;
   *     import flash.display.Graphics;
   *
   *     public class TextEvent_LINKExample extends Sprite {
   *         private  var myCircle:Shape = new Shape();
   *
   *         public function TextEvent_LINKExample() {
   *             var myTextField:TextField = new TextField();
   *             myTextField.autoSize = TextFieldAutoSize.LEFT;
   *             myTextField.multiline = true;
   *             myTextField.background = true;
   *             myTextField.htmlText = "Draw a circle with the radius of <u><a href=\"event:20\">20 pixels</a></u>.<br>"
   *                          +  "Draw a circle with the radius of <u><a href=\"event:50\">50 pixels</a></u>.<br><br>"
   *                          +  "<u><a href=\"event:os\">Learn about your operating system.</a></u><br>";
   *
   *             myTextField.addEventListener(TextEvent.LINK, linkHandler);
   *
   *             this.addChild(myTextField);
   *             this.addChild(myCircle);
   *         }
   *
   *         private function linkHandler(e:TextEvent):void {
   *             var osString:String = Capabilities.os;
   *
   *             if(e.text == "os") {
   *
   *                 if (osString.search(/Windows/) != -1 ){
   *                     navigateToURL(new URLRequest("http://www.microsoft.com/"), "_self");
   *                 }else if (osString.search(/Mac/) != -1 ) {
   *                     navigateToURL(new URLRequest("http://www.apple.com/"), "_self");
   *                 } else if (osString.search(/linux/i)!= -1) {
   *                     navigateToURL(new URLRequest("http://www.tldp.org/"), "_self");
   *                 }
   *
   *             } else {
   *                 myCircle.graphics.clear();
   *                 myCircle.graphics.beginFill(0xFF0000);
   *                 myCircle.graphics.drawCircle(100, 150, Number(e.text));
   *                 myCircle.graphics.endFill();
   *             }
   *         }
   *     }
   * }
   * </listing>
   */
  "public static const",{ LINK/*:String*/ : "link"},
  /**
   * Defines the value of the <code>type</code> property of a <code>textInput</code> event object.
   * <p><b>Note:</b> This event is not dispatched for the Delete or Backspace keys.</p>
   * <p>This event has the following properties:</p>
   * <table>
   * <tr><th>Property</th><th>Value</th></tr>
   * <tr>
   * <td><code>bubbles</code></td>
   * <td><code>true</code></td></tr>
   * <tr>
   * <td><code>cancelable</code></td>
   * <td><code>true</code>; call the <code>preventDefault()</code> method to cancel default behavior.</td></tr>
   * <tr>
   * <td><code>currentTarget</code></td>
   * <td>The object that is actively processing the Event object with an event listener.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The text field into which characters are being entered. The target is not always the object in the display list that registered the event listener. Use the <code>currentTarget</code> property to access the object in the display list that is currently processing the event.</td></tr>
   * <tr>
   * <td><code>text</code></td>
   * <td>The character or sequence of characters entered by the user.</td></tr></table>
   * @see flash.text.TextField#event:textInput
   * @see #text
   *
   * @example The following example guides the user in generating a special combination key (similar to a password). This combination key has seven alphanumeric characters, where the second and fifth characters are numeric.
   * <p>Three text fields for the preliminary instructions, the user input, and the warning (error) messages are created. An event listener is added to respond to the user's text input by triggering the <code>textInputHandler()</code> method. (Every time the user enters text, a <code>TextEvent.TEXT_INPUT</code> event is dispatched.</p>
   * <p><b>Note:</b> The text events are dispatched when a user enters characters and not as a response to any keyboard input, such as backspace. To catch all keyboard events, use a listener for the <code>KeyboardEvent</code> event.)</p>
   * <p>The <code>textInputHandler()</code> method controls and manages the user input. The <code>preventDefault()</code> method is used to prevent Adobe Flash Player from immediately displaying the text in the input text field. The application is responsible for updating the field. To undo the user's deletion or modification to the characters already entered (the <code>result</code> string), the content of the input text field is reassigned to the <code>result</code> string when a user enters new characters. Also, to produce a consistent user experience, the <code>setSelection()</code> method places the insertion point (a caret) after the last selected character in the text field.</p>
   * <p>The first if statement in the <code>textInputHandler()</code> method checks the input for the second and fifth character positions of the combination key, which must be numbers. If the user input is correct, the <code>updateCombination()</code> method is called and the (<code>result</code>) combination key string is appended with the user input. The <code>updateCombination()</code> method also moves the insertion point after the selected character. After the seven characters are entered, the last if statement in the <code>textInputHandler()</code> method changes type of the <code>inputTextField</code> text field from <code>INPUT</code> to <code>DYNAMIC</code>, which means that the user can no longer enter or change any characters.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldType;
   *     import flash.text.TextFieldAutoSize;
   *     import flash.events.TextEvent;
   *
   *     public class TextEvent_TEXT_INPUTExample extends Sprite {
   *         private var instructionTextField:TextField = new TextField();
   *         private var inputTextField:TextField = new TextField();
   *         private var warningTextField:TextField = new TextField();
   *         private var result:String = "";
   *
   *         public function TextEvent_TEXT_INPUTExample() {
   *             instructionTextField.x = 10;
   *             instructionTextField.y = 10;
   *             instructionTextField.background = true;
   *             instructionTextField.autoSize = TextFieldAutoSize.LEFT;
   *             instructionTextField.text = "Please enter a value in the format A#AA#AA,\n"
   *                                         + "where 'A' represents a letter and '#' represents a number.\n" +
   *                                         "(Note that once you input a character you can't change it.)" ;
   *
   *             inputTextField.x = 10;
   *             inputTextField.y = 70;
   *             inputTextField.height = 20;
   *             inputTextField.width = 75;
   *             inputTextField.background = true;
   *             inputTextField.border = true;
   *             inputTextField.type = TextFieldType.INPUT;
   *
   *             warningTextField.x = 10;
   *             warningTextField.y = 100;
   *             warningTextField.autoSize = TextFieldAutoSize.LEFT;
   *
   *             inputTextField.addEventListener(TextEvent.TEXT_INPUT, textInputHandler);
   *
   *             this.addChild(instructionTextField);
   *             this.addChild(inputTextField);
   *             this.addChild(warningTextField);
   *         }
   *
   *         private function textInputHandler(event:TextEvent):void {
   *             var charExp:RegExp = /[a-zA-z]/;
   *             var numExp:RegExp = /[0-9]/;
   *
   *             event.preventDefault();
   *
   *             inputTextField.text = result;
   *             inputTextField.setSelection(result.length + 1, result.length + 1);
   *
   *             if (inputTextField.text.length == 1 || inputTextField.text.length == 4) {
   *
   *                 if(numExp.test(event.text) == true) {
   *                     updateCombination(event.text);
   *                 } else {
   *                     warningTextField.text = "You need a single digit number.";
   *                 }
   *
   *             }else {
   *
   *                 if(charExp.test(event.text) == true) {
   *                     updateCombination(event.text);
   *                 } else {
   *                     warningTextField.text = "You need an alphabet character.";
   *                 }
   *             }
   *
   *             if(inputTextField.text.length == 7) {
   *                 inputTextField.type = TextFieldType.DYNAMIC;
   *                 instructionTextField.text = "CONGRATULATIONS. You've done.";
   *             }
   *         }
   *
   *         private function updateCombination(s:String):void {
   *                     warningTextField.text = "";
   *                     result += s;
   *                     inputTextField.text = result;
   *                     inputTextField.setSelection(result.length + 1, result.length + 1);
   *         }
   *     }
   * }
   * </listing>
   */
  "public static const",{ TEXT_INPUT/*:String*/ : "textInput"},
];},[],["flash.events.Event"], "0.8.0", "0.8.1"
);