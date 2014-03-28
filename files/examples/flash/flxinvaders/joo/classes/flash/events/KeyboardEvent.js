joo.classLoader.prepare("package flash.events",/* {*/

/**
 * A KeyboardEvent object id dispatched in response to user input through a keyboard. There are two types of keyboard events: <code>KeyboardEvent.KEY_DOWN</code> and <code>KeyboardEvent.KEY_UP</code>
 * <p>Because mappings between keys and specific characters vary by device and operating system, use the TextEvent event type for processing character input.</p>
 * <p>To listen globally for key events, listen on the Stage for the capture and target or bubble phase.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/KeyboardEvent.html#includeExamplesSummary">View the examples</a></p>
 * @see #KEY_DOWN
 * @see #KEY_UP
 * @see flash.ui.KeyLocation
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf64a29-7fdb.html About keyboard events
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf64a29-7fef.html Handling keyboard events
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf64a29-7fed.html Understanding the keyCode and charCode properties
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf64a29-7fe8.html Understanding KeyboardEvent precedence
 *
 */
"public class KeyboardEvent extends flash.events.Event",2,function($$private){;return[ 
  /**
   * Indicates whether the Alt key is active (<code>true</code>) or inactive (<code>false</code>) on Windows; indicates whether the Option key is active on Mac OS.
   */
  "public native function get altKey"/*():Boolean;*/,

  /**
   * @private
   */
  "public native function set altKey"/*(value:Boolean):void;*/,

  /**
   * Contains the character code value of the key pressed or released. The character code values are English keyboard values. For example, if you press Shift+3, <code>charCode</code> is # on a Japanese keyboard, just as it is on an English keyboard.
   * <p><b>Note:</b> When an input method editor (IME) is running, <code>charCode</code> does not report accurate character codes.</p>
   * @see flash.system.IME
   *
   */
  "public native function get charCode"/*():uint;*/,

  /**
   * @private
   */
  "public native function set charCode"/*(value:uint):void;*/,

  /**
   * On Windows and Linux, indicates whether the Ctrl key is active (<code>true</code>) or inactive (<code>false</code>); On Mac OS, indicates whether either the Ctrl key or the Command key is active.
   */
  "public native function get ctrlKey"/*():Boolean;*/,

  /**
   * @private
   */
  "public native function set ctrlKey"/*(value:Boolean):void;*/,

  /**
   * The key code value of the key pressed or released.
   * <p><b>Note:</b> When an input method editor (IME) is running, <code>keyCode</code> does not report accurate key codes.</p>
   * @see flash.system.IME
   *
   */
  "public native function get keyCode"/*():uint;*/,

  /**
   * @private
   */
  "public native function set keyCode"/*(value:uint):void;*/,

  /**
   * Indicates the location of the key on the keyboard. This is useful for differentiating keys that appear more than once on a keyboard. For example, you can differentiate between the left and right Shift keys by the value of this property: <code>KeyLocation.LEFT</code> for the left and <code>KeyLocation.RIGHT</code> for the right. Another example is differentiating between number keys pressed on the standard keyboard (<code>KeyLocation.STANDARD</code>) versus the numeric keypad (<code>KeyLocation.NUM_PAD</code>).
   */
  "public native function get keyLocation"/*():uint;*/,

  /**
   * @private
   */
  "public native function set keyLocation"/*(value:uint):void;*/,

  /**
   * Indicates whether the Shift key modifier is active (<code>true</code>) or inactive (<code>false</code>).
   */
  "public native function get shiftKey"/*():Boolean;*/,

  /**
   * @private
   */
  "public native function set shiftKey"/*(value:Boolean):void;*/,

  /**
   * Creates an Event object that contains specific information about keyboard events. Event objects are passed as parameters to event listeners.
   * @param type The type of the event. Possible values are: <code>KeyboardEvent.KEY_DOWN</code> and <code>KeyboardEvent.KEY_UP</code>
   * @param bubbles Determines whether the Event object participates in the bubbling stage of the event flow.
   * @param cancelable Determines whether the Event object can be canceled.
   * @param charCodeValue The character code value of the key pressed or released. The character code values returned are English keyboard values. For example, if you press Shift+3, the <code>Keyboard.charCode()</code> property returns # on a Japanese keyboard, just as it does on an English keyboard.
   * @param keyCodeValue The key code value of the key pressed or released.
   * @param keyLocationValue The location of the key on the keyboard.
   * @param ctrlKeyValue On Windows, indicates whether the Ctrl key is activated. On Mac, indicates whether either the Ctrl key or the Command key is activated.
   * @param altKeyValue Indicates whether the Alt key modifier is activated (Windows only).
   * @param shiftKeyValue Indicates whether the Shift key modifier is activated.
   * @param controlKeyValue Indicates whether the Control key is activated on Mac, and whether the Control or Ctrl keys are activated on WIndows and Linux.
   * @param commandKeyValue Indicates whether the Command key is activated (Mac only).
   *
   * @see #KEY_DOWN
   * @see #KEY_UP
   * @see #charCode
   *
   */
  "public function KeyboardEvent",function KeyboardEvent$(type/*:String*/, bubbles/*:Boolean = true*/, cancelable/*:Boolean = false*/, charCodeValue/*:uint = 0*/, keyCodeValue/*:uint = 0*/, keyLocationValue/*:uint = 0*/, ctrlKeyValue/*:Boolean = false*/, altKeyValue/*:Boolean = false*/, shiftKeyValue/*:Boolean = false*/, controlKeyValue/*:Boolean = false*/, commandKeyValue/*:Boolean = false*/) {if(arguments.length<11){if(arguments.length<10){if(arguments.length<9){if(arguments.length<8){if(arguments.length<7){if(arguments.length<6){if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){bubbles = true;}cancelable = false;}charCodeValue = 0;}keyCodeValue = 0;}keyLocationValue = 0;}ctrlKeyValue = false;}altKeyValue = false;}shiftKeyValue = false;}controlKeyValue = false;}commandKeyValue = false;}
    this.super$2(type, bubbles, cancelable);
    this.charCode = charCodeValue;
    this.keyCode = keyCodeValue;
    this.keyLocation = keyLocationValue;
    this.ctrlKey = ctrlKeyValue || controlKeyValue || commandKeyValue; // TODO: Is this the intended semantics?
    this.altKey = altKeyValue;
    this.shiftKey = shiftKeyValue;
  },

  /**
   * Creates a copy of the KeyboardEvent object and sets the value of each property to match that of the original.
   * @return A new KeyboardEvent object with property values that match those of the original.
   *
   */
  "override public function clone",function clone()/*:Event*/ {
    return new flash.events.KeyboardEvent(this.type, this.bubbles, this.cancelable, this.charCode, this.keyCode, this.keyLocation, this.ctrlKey, this.altKey,
      this.shiftKey, this.ctrlKey, this.ctrlKey);
  },

  /**
   * Returns a string that contains all the properties of the KeyboardEvent object. The string is in the following format:
   * <p><code>[KeyboardEvent type=<i>value</i> bubbles=<i>value</i> cancelable=<i>value</i> ... shiftKey=<i>value</i>]</code></p>
   * @return A string that contains all the properties of the KeyboardEvent object.
   *
   */
  "override public function toString",function toString()/*:String*/ {
    return this.formatToString("KeyboardEvent", "type", "bubbles", "cancelable",
      "charCode", "keyCode", "keyLocation", "ctrlKey", "altKey", "shiftKey", "controlKey", "commandKey");
  },

  /**
   * Indicates that the display should be rendered after processing of this event completes, if the display list has been modified
   */
  "public function updateAfterEvent",function updateAfterEvent()/*:void*/ {
    // TODO: implement!
  },

  /**
   * The <code>KeyboardEvent.KEY_DOWN</code> constant defines the value of the <code>type</code> property of a <code>keyDown</code> event object.
   * <p>This event has the following properties:</p>
   * <table>
   * <tr><th>Property</th><th>Value</th></tr>
   * <tr>
   * <td><code>bubbles</code></td>
   * <td><code>true</code></td></tr>
   * <tr>
   * <td><code>cancelable</code></td>
   * <td><code>true</code> in AIR, <code>false</code> in Flash Player; in AIR, canceling this event prevents the character from being entered into a text field.</td></tr>
   * <tr>
   * <td><code>charCode</code></td>
   * <td>The character code value of the key pressed or released.</td></tr>
   * <tr>
   * <td><code>commandKey</code></td>
   * <td><code>true</code> on Mac if the Command key is active. Otherwise, <code>false</code></td></tr>
   * <tr>
   * <td><code>controlKey</code></td>
   * <td><code>true</code> on Windows and Linux if the Ctrl key is active. <code>true</code> on Mac if either the Control key is active. Otherwise, <code>false</code></td></tr>
   * <tr>
   * <td><code>ctrlKey</code></td>
   * <td><code>true</code> on Windows and Linux if the Ctrl key is active. <code>true</code> on Mac if either the Ctrl key or the Command key is active. Otherwise, <code>false</code>.</td></tr>
   * <tr>
   * <td><code>currentTarget</code></td>
   * <td>The object that is actively processing the Event object with an event listener.</td></tr>
   * <tr>
   * <td><code>keyCode</code></td>
   * <td>The key code value of the key pressed or released.</td></tr>
   * <tr>
   * <td><code>keyLocation</code></td>
   * <td>The location of the key on the keyboard.</td></tr>
   * <tr>
   * <td><code>shiftKey</code></td>
   * <td><code>true</code> if the Shift key is active; <code>false</code> if it is inactive.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The InteractiveObject instance with focus. The <code>target</code> is not always the object in the display list that registered the event listener. Use the <code>currentTarget</code> property to access the object in the display list that is currently processing the event.</td></tr></table>
   * @see flash.display.InteractiveObject#event:keyDown
   *
   */
  "public static const",{ KEY_DOWN/*:String*/ : "keyDown"},
  /**
   * The <code>KeyboardEvent.KEY_UP</code> constant defines the value of the <code>type</code> property of a <code>keyUp</code> event object.
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
   * <td><code>charCode</code></td>
   * <td>Contains the character code value of the key pressed or released.</td></tr>
   * <tr>
   * <td><code>commandKey</code></td>
   * <td><code>true</code> on Mac if the Command key is active. Otherwise, <code>false</code></td></tr>
   * <tr>
   * <td><code>controlKey</code></td>
   * <td><code>true</code> on Windows and Linux if the Ctrl key is active. <code>true</code> on Mac if either the Control key is active. Otherwise, <code>false</code></td></tr>
   * <tr>
   * <td><code>ctrlKey</code></td>
   * <td><code>true</code> on Windows if the Ctrl key is active. <code>true</code> on Mac if either the Ctrl key or the Command key is active. Otherwise, <code>false</code>.</td></tr>
   * <tr>
   * <td><code>currentTarget</code></td>
   * <td>The object that is actively processing the Event object with an event listener.</td></tr>
   * <tr>
   * <td><code>keyCode</code></td>
   * <td>The key code value of the key pressed or released.</td></tr>
   * <tr>
   * <td><code>keyLocation</code></td>
   * <td>The location of the key on the keyboard.</td></tr>
   * <tr>
   * <td><code>shiftKey</code></td>
   * <td><code>true</code> if the Shift key is active; <code>false</code> if it is inactive.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The InteractiveObject instance with focus. The <code>target</code> is not always the object in the display list that registered the event listener. Use the <code>currentTarget</code> property to access the object in the display list that is currently processing the event.</td></tr></table>
   * @see flash.display.InteractiveObject#event:keyUp
   *
   */
  "public static const",{ KEY_UP/*:String*/ : "keyUp"},

];},[],["flash.events.Event"], "0.8.0", "0.8.1"
);