joo.classLoader.prepare("package flash.events",/* {*/

/**
 * An object dispatches a StatusEvent object when a device, such as a camera or microphone, or an object such as a LocalConnection object reports its status. There is only one type of status event: <code>StatusEvent.STATUS</code>.
 * @see flash.media.Camera
 * @see flash.media.Microphone
 * @see flash.net.LocalConnection
 * @see flash.sensors.Accelerometer
 * @see flash.sensors.Geolocation
 * @see air.net.ServiceMonitor
 *
 */
"public class StatusEvent extends flash.events.Event",2,function($$private){;return[ 
  /**
   * A description of the object's status.
   * @see flash.media.Camera
   * @see flash.media.Microphone
   * @see flash.net.LocalConnection
   *
   */
  "public native function get code"/*():String;*/,

  /**
   * @private
   */
  "public native function set code"/*(value:String):void;*/,

  /**
   * The category of the message, such as <code>"status"</code>, <code>"warning"</code> or <code>"error"</code>.
   * @see flash.media.Camera
   * @see flash.media.Microphone
   * @see flash.net.LocalConnection
   *
   */
  "public native function get level"/*():String;*/,

  /**
   * @private
   */
  "public native function set level"/*(value:String):void;*/,

  /**
   * Creates an Event object that contains information about status events. Event objects are passed as parameters to event listeners.
   * @param type The type of the event. Event listeners can access this information through the inherited <code>type</code> property. There is only one type of status event: <code>StatusEvent.STATUS</code>.
   * @param bubbles Determines whether the Event object participates in the bubbling stage of the event flow. Event listeners can access this information through the inherited <code>bubbles</code> property.
   * @param cancelable Determines whether the Event object can be canceled. Event listeners can access this information through the inherited <code>cancelable</code> property.
   * @param code A description of the object's status. Event listeners can access this information through the <code>code</code> property.
   * @param level The category of the message, such as <code>"status"</code>, <code>"warning"</code> or <code>"error"</code>. Event listeners can access this information through the <code>level</code> property.
   *
   * @see #STATUS
   *
   */
  "public function StatusEvent",function StatusEvent$(type/*:String*/, bubbles/*:Boolean = false*/, cancelable/*:Boolean = false*/, code/*:String = ""*/, level/*:String = ""*/) {if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){bubbles = false;}cancelable = false;}code = "";}level = "";}
    this.super$2(type, bubbles, cancelable);
    this.code = code;
    this.level = level;
  },

  /**
   * Creates a copy of the StatusEvent object and sets the value of each property to match that of the original.
   * @return A new StatusEvent object with property values that match those of the original.
   *
   */
  "override public function clone",function clone()/*:Event*/ {
    return new flash.events.StatusEvent(this.type, this.bubbles, this.cancelable, this.code, this.level);
  },

  /**
   * Returns a string that contains all the properties of the StatusEvent object. The string is in the following format:
   * <p><code>[StatusEvent type=<i>value</i> bubbles=<i>value</i> cancelable=<i>value</i> code=<i>value</i> level=<i>value</i>]</code></p>
   * @return A string that contains all the properties of the StatusEvent object.
   *
   */
  "override public function toString",function toString()/*:String*/ {
    return this.formatToString("StatusEvent", "type", "bubbles", "cancelable", "code", "level");
  },

  /**
   * Defines the value of the <code>type</code> property of a <code>status</code> event object.
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
   * <td><code>code</code></td>
   * <td>A description of the object's status.</td></tr>
   * <tr>
   * <td><code>currentTarget</code></td>
   * <td>The object that is actively processing the Event object with an event listener.</td></tr>
   * <tr>
   * <td><code>level</code></td>
   * <td>The category of the message, such as <code>"status"</code>, <code>"warning"</code> or <code>"error"</code>.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The object reporting its status.</td></tr></table>
   * @see flash.media.Camera#event:status
   * @see flash.media.Microphone#event:status
   * @see flash.net.LocalConnection#event:status
   * @see flash.net.NetStream#event:status
   * @see flash.sensors.Geolocation#event:status
   * @see flash.sensors.Accelerometer#event:status
   *
   */
  "public static const",{ STATUS/*:String*/ : "status"},

];},[],["flash.events.Event"], "0.8.0", "0.8.1"
);