joo.classLoader.prepare("package flash.events",/* {*/




/**
 * A Camera or Microphone object dispatches an ActivityEvent object whenever a camera or microphone reports that it has become active or inactive. There is only one type of activity event: <code>ActivityEvent.ACTIVITY</code>.
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/ActivityEvent.html#includeExamplesSummary">View the examples</a></p>
 * @see #ACTIVITY
 *
 */
"public class ActivityEvent extends flash.events.Event",2,function($$private){;return[ 
  /**
   * Indicates whether the device is activating (<code>true</code>) or deactivating (<code>false</code>).
   */
  "public native function get activating"/*():Boolean;*/,

  /**
   * @private
   */
  "public native function set activating"/*(value:Boolean):void;*/,

  /**
   * Creates an event object that contains information about activity events. Event objects are passed as parameters to Event listeners.
   * @param type The type of the event. Event listeners can access this information through the inherited <code>type</code> property. There is only one type of activity event: <code>ActivityEvent.ACTIVITY</code>.
   * @param bubbles Determines whether the Event object participates in the bubbling phase of the event flow. Event listeners can access this information through the inherited <code>bubbles</code> property.
   * @param cancelable Determines whether the Event object can be canceled. Event listeners can access this information through the inherited <code>cancelable</code> property.
   * @param activating Indicates whether the device is activating (<code>true</code>) or deactivating (<code>false</code>). Event listeners can access this information through the <code>activating</code> property.
   *
   * @see #ACTIVITY
   *
   */
  "public function ActivityEvent",function ActivityEvent$(type/*:String*/, bubbles/*:Boolean = false*/, cancelable/*:Boolean = false*/, activating/*:Boolean = false*/) {if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){bubbles = false;}cancelable = false;}activating = false;}
    this.super$2(flash.events.ActivityEvent.ACTIVITY, bubbles, cancelable);
    this.activating = activating;
  },

  /**
   * Creates a copy of an ActivityEvent object and sets the value of each property to match that of the original.
   * @return A new ActivityEvent object with property values that match those of the original.
   *
   */
  "override public function clone",function clone()/*:Event*/ {
    return new flash.events.ActivityEvent(this.type, this.bubbles, this.cancelable, this.activating);
  },

  /**
   * Returns a string that contains all the properties of the ActivityEvent object. The following format is used:
   * <p><code>[ActivityEvent type=<i>value</i> bubbles=<i>value</i> cancelable=<i>value</i> activating=<i>value</i>]</code></p>
   * @return A string that contains all the properties of the ActivityEvent object.
   *
   */
  "override public function toString",function toString()/*:String*/ {
    return this.formatToString("ActivityEvent", "type", "bubbles", "cancelable", "activating");
  },

  /**
   * The <code>ActivityEvent.ACTIVITY</code> constant defines the value of the <code>type</code> property of an <code>activity</code> event object.
   * <p>This event has the following properties:</p>
   * <table>
   * <tr><th>Property</th><th>Value</th></tr>
   * <tr>
   * <td><code>activating</code></td>
   * <td><code>true</code> if the device is activating or <code>false</code> if it is deactivating.</td></tr>
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
   * <td>The object beginning or ending a session, such as a Camera or Microphone object.</td></tr></table>
   * @see flash.media.Camera#event:activity
   * @see flash.media.Microphone#event:activity
   *
   */
  "public static const",{ ACTIVITY/*:String*/ : "activity"},
];},[],["flash.events.Event"], "0.8.0", "0.8.3"
);