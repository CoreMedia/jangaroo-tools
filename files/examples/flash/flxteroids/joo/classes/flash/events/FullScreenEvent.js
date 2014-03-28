joo.classLoader.prepare("package flash.events",/* {*/

/**
 * The Stage object dispatches a FullScreenEvent object whenever the Stage enters or leaves full-screen display mode. There is only one type of <code>fullScreen</code> event: <code>FullScreenEvent.FULL_SCREEN</code>.
 * @see flash.display.Stage#displayState
 *
 */
"public class FullScreenEvent extends flash.events.ActivityEvent",3,function($$private){;return[ 
  /**
   * Indicates whether the Stage object is in full-screen mode (<code>true</code>) or not (<code>false</code>).
   */
  "public native function get fullScreen"/*():Boolean;*/,

  /**
   * Creates an event object that contains information about <code>fullScreen</code> events. Event objects are passed as parameters to event listeners.
   * @param type The type of the event. Event listeners can access this information through the inherited <code>type</code> property. There is only one type of <code>fullScreen</code> event: <code>FullScreenEvent.FULL_SCREEN</code>.
   * @param bubbles Determines whether the Event object participates in the bubbling phase of the event flow. Event listeners can access this information through the inherited <code>bubbles</code> property.
   * @param cancelable Determines whether the Event object can be canceled. Event listeners can access this information through the inherited <code>cancelable</code> property.
   * @param fullScreen Indicates whether the device is activating (<code>true</code>) or deactivating (<code>false</code>). Event listeners can access this information through the <code>activating</code> property.
   *
   * @see #FULL_SCREEN
   *
   */
  "public function FullScreenEvent",function FullScreenEvent$(type/*:String*/, bubbles/*:Boolean = false*/, cancelable/*:Boolean = false*/, fullScreen/*:Boolean = false*/) {if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){bubbles = false;}cancelable = false;}fullScreen = false;}
    this.super$3(type, bubbles, cancelable);
    this['fullScreen'] = fullScreen;
  },

  /**
   * Creates a copy of a FullScreenEvent object and sets the value of each property to match that of the original.
   * @return A new FullScreenEvent object with property values that match those of the original.
   *
   */
  "override public function clone",function clone()/*:Event*/ {
    return new flash.events.FullScreenEvent(this.type, this.bubbles, this.cancelable, this.fullScreen);
  },

  /**
   * Returns a string that contains all the properties of the FullScreenEvent object. The following format is used:
   * <p><code>[FullScreenEvent type=<i>value</i> bubbles=<i>value</i> cancelable=<i>value</i> fullScreen=<i>value</i>]</code></p>
   * @return A string that contains all the properties of the FullScreenEvent object.
   *
   */
  "override public function toString",function toString()/*:String*/ {
    return this.formatToString("FullScreenEvent", "type", "bubbles", "cancelable", "fullScreen");
  },

  /**
   * The <code>FullScreenEvent.FULL_SCREEN</code> constant defines the value of the <code>type</code> property of a <code>fullScreen</code> event object.
   * <p>This event has the following properties:</p>
   * <table>
   * <tr><th>Property</th><th>Value</th></tr>
   * <tr>
   * <td><code>fullScreen</code></td>
   * <td><code>true</code> if the display state is full screen or <code>false</code> if it is normal.</td></tr>
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
   * <td>The Stage object.</td></tr></table>
   * @see flash.display.Stage#displayState
   *
   */
  "public static const",{ FULL_SCREEN/*:String*/ : "fullScreen"},
];},[],["flash.events.ActivityEvent"], "0.8.0", "0.8.1"
);