joo.classLoader.prepare("package flash.events",/* {*/




/**
 * An IMEEvent object is dispatched when the user enters text using an input method editor (IME). IMEs are generally used to enter text from languages that have ideographs instead of letters, such as Japanese, Chinese, and Korean. There are two IME events: <code>IMEEvent.IME_COMPOSITION</code> and <code>IMEEvent.IME_START_COMPOSITION</code>.
 * @see flash.system.IME
 * @see #IME_COMPOSITION
 * @see #IME_START_COMPOSITION
 *
 */
"public class IMEEvent extends flash.events.TextEvent",3,function($$private){;return[ 
  /**
   * Creates an Event object with specific information relevant to IME events. Event objects are passed as parameters to event listeners.
   * @param type The type of the event. Event listeners can access this information through the inherited <code>type</code> property. There is only one IME event: <code>IMEEvent.IME_COMPOSITION</code>.
   * @param bubbles Determines whether the Event object participates in the bubbling stage of the event flow. Event listeners can access this information through the inherited <code>bubbles</code> property.
   * @param cancelable Determines whether the Event object can be canceled. Event listeners can access this information through the inherited <code>cancelable</code> property.
   * @param text The reading string from the IME. This is the initial string as typed by the user, before selection of any candidates. The final composition string is delivered to the object with keyboard focus in a <code>TextEvent.TEXT_INPUT</code> event. Event listeners can access this information through the <code>text</code> property.
   * @param imeClient A set of callbacks used by the text engine to communicate with the IME. Useful if your code has its own text engine and is rendering lines of text itself, rather than using TextField objects or the TextLayoutFramework.
   *
   * @see flash.system.IME
   * @see #IME_COMPOSITION
   * @see #IME_START_COMPOSITION
   *
   */
  "public function IMEEvent",function IMEEvent$(type/*:String*/, bubbles/*:Boolean = false*/, cancelable/*:Boolean = false*/, text/*:String = ""*/, imeClient/*:Object/*flash.text.ime.IIMEClient* / = null*/) {if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){bubbles = false;}cancelable = false;}text = "";}imeClient/*flash.text.ime.IIMEClient*/ = null;}
    this.super$3(type, bubbles, cancelable, text);
  },

  /**
   * Creates a copy of the IMEEvent object and sets the value of each property to match that of the original.
   * @return A new IMEEvent object with property values that match those of the original.
   *
   */
  "override public function clone",function clone()/*:Event*/ {
    return new flash.events.IMEEvent(this.type, this.bubbles, this.cancelable, this.text);
  },

  /**
   * Returns a string that contains all the properties of the IMEEvent object. The string is in the following format:
   * <p><code>[IMEEvent type=<i>value</i> bubbles=<i>value</i> cancelable=<i>value</i> text=<i>value</i>]</code></p>
   * @return A string that contains all the properties of the IMEEvent object.
   *
   */
  "override public function toString",function toString()/*:String*/ {
    return this.formatToString("IMEEvent", "type", "bubbles", "cancelable", "text");
  },

  /**
   * Defines the value of the <code>type</code> property of an <code>imeComposition</code> event object.
   * <p>This event has the following properties:</p>
   * <table>
   * <tr><th>Property</th><th>Value</th></tr>
   * <tr>
   * <td><code>bubbles</code></td>
   * <td><code>false</code></td></tr>
   * <tr>
   * <td><code>cancelable</code></td>
   * <td><code>false</code>; there is no default behavior to cancel.</td></tr>
   * <tr>
   * <td><code>currentTarget</code></td>
   * <td>The object that is actively processing the Event object with an event listener.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The IME object.</td></tr></table>
   * @see flash.system.IME#event:imeComposition
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cd5.html Using the IME class
   *
   */
  "public static const",{ IME_COMPOSITION/*:String*/ : "imeComposition"},
];},[],["flash.events.TextEvent"], "0.8.0", "0.8.3"
);