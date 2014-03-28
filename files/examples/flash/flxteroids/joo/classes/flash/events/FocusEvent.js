joo.classLoader.prepare("package flash.events",/* {
import flash.display.InteractiveObject*/

/**
 * An object dispatches a FocusEvent object when the user changes the focus from one object in the display list to another. There are four types of focus events:
 * <ul>
 * <li><code>FocusEvent.FOCUS_IN</code></li>
 * <li><code>FocusEvent.FOCUS_OUT</code></li>
 * <li><code>FocusEvent.KEY_FOCUS_CHANGE</code></li>
 * <li><code>FocusEvent.MOUSE_FOCUS_CHANGE</code></li></ul>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/FocusEvent.html#includeExamplesSummary">View the examples</a></p>
 */
"public class FocusEvent extends flash.events.Event",2,function($$private){;return[ 
  /**
   * The key code value of the key pressed to trigger a <code>keyFocusChange</code> event.
   */
  "public native function get keyCode"/*():uint;*/,

  /**
   * @private
   */
  "public native function set keyCode"/*(value:uint):void;*/,

  /**
   * A reference to the complementary InteractiveObject instance that is affected by the change in focus. For example, when a <code>focusOut</code> event occurs, the <code>relatedObject</code> represents the InteractiveObject instance that has gained focus.
   * <p>The value of this property can be <code>null</code> in two circumstances: if there no related object, or there is a related object, but it is in a security sandbox to which you don't have access. Use the <code>isRelatedObjectInaccessible()</code> property to determine which of these reasons applies.</p>
   * @see #isRelatedObjectInaccessible
   *
   */
  "public native function get relatedObject"/*():InteractiveObject;*/,

  /**
   * @private
   */
  "public native function set relatedObject"/*(value:InteractiveObject):void;*/,

  /**
   * Indicates whether the Shift key modifier is activated, in which case the value is <code>true</code>. Otherwise, the value is <code>false</code>. This property is used only if the FocusEvent is of type <code>keyFocusChange</code>.
   */
  "public native function get shiftKey"/*():Boolean;*/,

  /**
   * @private
   */
  "public native function set shiftKey"/*(value:Boolean):void;*/,

  /**
   * Creates an Event object with specific information relevant to focus events. Event objects are passed as parameters to event listeners.
   * @param type The type of the event. Possible values are: <code>FocusEvent.FOCUS_IN</code>, <code>FocusEvent.FOCUS_OUT</code>, <code>FocusEvent.KEY_FOCUS_CHANGE</code>, and <code>FocusEvent.MOUSE_FOCUS_CHANGE</code>.
   * @param bubbles Determines whether the Event object participates in the bubbling stage of the event flow.
   * @param cancelable Determines whether the Event object can be canceled.
   * @param relatedObject Indicates the complementary InteractiveObject instance that is affected by the change in focus. For example, when a <code>focusIn</code> event occurs, <code>relatedObject</code> represents the InteractiveObject that has lost focus.
   * @param shiftKey Indicates whether the Shift key modifier is activated.
   * @param keyCode Indicates the code of the key pressed to trigger a <code>keyFocusChange</code> event.
   * @param direction Indicates from which direction the target interactive object is being activated. Set to <code>FocusDirection.NONE</code> (the default value) for all events other than <code>focusIn</code>.
   *
   * @see #FOCUS_IN
   * @see #FOCUS_OUT
   * @see #KEY_FOCUS_CHANGE
   * @see #MOUSE_FOCUS_CHANGE
   * @see flash.display.FocusDirection
   *
   */
  "public function FocusEvent",function FocusEvent$(type/*:String*/, bubbles/*:Boolean = true*/, cancelable/*:Boolean = false*/, relatedObject/*:InteractiveObject = null*/, shiftKey/*:Boolean = false*/, keyCode/*:uint = 0*/, direction/*:String = "none"*/) {if(arguments.length<7){if(arguments.length<6){if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){bubbles = true;}cancelable = false;}relatedObject = null;}shiftKey = false;}keyCode = 0;}direction = "none";}
    this.super$2(type, bubbles, cancelable);
    this.relatedObject = relatedObject;
    this.shiftKey = shiftKey;
    this.keyCode = keyCode;
    // TODO: no getter for "direction" - what is it for at all?
  },

  /**
   * Creates a copy of the FocusEvent object and sets the value of each property to match that of the original.
   * @return A new FocusEvent object with property values that match those of the original.
   *
   */
  "override public function clone",function clone()/*:Event*/ {
    return new flash.events.FocusEvent(this.type, this.bubbles, this.cancelable, this.relatedObject, this.shiftKey, this.keyCode);
  },

  /**
   * Returns a string that contains all the properties of the FocusEvent object. The string is in the following format:
   * <p><code>[FocusEvent type=<i>value</i> bubbles=<i>value</i> cancelable=<i>value</i> relatedObject=<i>value</i> shiftKey=<i>value</i>] keyCode=<i>value</i>]</code></p>
   * @return A string that contains all the properties of the FocusEvent object.
   *
   */
  "override public function toString",function toString()/*:String*/ {
    return this.formatToString("FocusEvent", "type", "bubbles", "cancelable", "relatedObject", "shiftKey", "keyCode");
  },

  /**
   * Defines the value of the <code>type</code> property of a <code>focusIn</code> event object.
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
   * <td><code>keyCode</code></td>
   * <td>0; applies only to <code>keyFocusChange</code> events.</td></tr>
   * <tr>
   * <td><code>relatedObject</code></td>
   * <td>The complementary InteractiveObject instance that is affected by the change in focus.</td></tr>
   * <tr>
   * <td><code>shiftKey</code></td>
   * <td><code>false</code>; applies only to <code>keyFocusChange</code> events.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The InteractiveObject instance that has just received focus. The <code>target</code> is not always the object in the display list that registered the event listener. Use the <code>currentTarget</code> property to access the object in the display list that is currently processing the event.</td></tr>
   * <tr>
   * <td><code>direction</code></td>
   * <td>The direction from which focus was assigned. This property reports the value of the <code>direction</code> parameter of the <code>assignFocus()</code> method of the stage. If the focus changed through some other means, the value will always be <code>FocusDirection.NONE</code>. Applies only to <code>focusIn</code> events. For all other focus events the value will be <code>FocusDirection.NONE</code>.</td></tr></table>
   * @see flash.display.InteractiveObject#event:focusIn
   *
   */
  "public static const",{ FOCUS_IN/*:String*/ : "focusIn"},
  /**
   * Defines the value of the <code>type</code> property of a <code>focusOut</code> event object.
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
   * <td><code>keyCode</code></td>
   * <td>0; applies only to <code>keyFocusChange</code> events.</td></tr>
   * <tr>
   * <td><code>relatedObject</code></td>
   * <td>The complementary InteractiveObject instance that is affected by the change in focus.</td></tr>
   * <tr>
   * <td><code>shiftKey</code></td>
   * <td><code>false</code>; applies only to <code>keyFocusChange</code> events.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The InteractiveObject instance that has just lost focus. The <code>target</code> is not always the object in the display list that registered the event listener. Use the <code>currentTarget</code> property to access the object in the display list that is currently processing the event.</td></tr></table>
   * @see flash.display.InteractiveObject#event:focusOut
   *
   */
  "public static const",{ FOCUS_OUT/*:String*/ : "focusOut"},
  /**
   * Defines the value of the <code>type</code> property of a <code>keyFocusChange</code> event object.
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
   * <td><code>keyCode</code></td>
   * <td>The key code value of the key pressed to trigger a <code>keyFocusChange</code> event.</td></tr>
   * <tr>
   * <td><code>relatedObject</code></td>
   * <td>The complementary InteractiveObject instance that is affected by the change in focus.</td></tr>
   * <tr>
   * <td><code>shiftKey</code></td>
   * <td><code>true</code> if the Shift key modifier is activated; <code>false</code> otherwise.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The InteractiveObject instance that currently has focus. The <code>target</code> is not always the object in the display list that registered the event listener. Use the <code>currentTarget</code> property to access the object in the display list that is currently processing the event.</td></tr></table>
   * @see flash.display.InteractiveObject#event:keyFocusChange
   *
   */
  "public static const",{ KEY_FOCUS_CHANGE/*:String*/ : "keyFocusChange"},
  /**
   * Defines the value of the <code>type</code> property of a <code>mouseFocusChange</code> event object.
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
   * <td><code>keyCode</code></td>
   * <td>0; applies only to <code>keyFocusChange</code> events.</td></tr>
   * <tr>
   * <td><code>relatedObject</code></td>
   * <td>The complementary InteractiveObject instance that is affected by the change in focus.</td></tr>
   * <tr>
   * <td><code>shiftKey</code></td>
   * <td><code>false</code>; applies only to <code>keyFocusChange</code> events.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The InteractiveObject instance that currently has focus. The <code>target</code> is not always the object in the display list that registered the event listener. Use the <code>currentTarget</code> property to access the object in the display list that is currently processing the event.</td></tr></table>
   * @see flash.display.InteractiveObject#event:mouseFocusChange
   *
   */
  "public static const",{ MOUSE_FOCUS_CHANGE/*:String*/ : "mouseFocusChange"},
];},[],["flash.events.Event"], "0.8.0", "0.8.1"
);