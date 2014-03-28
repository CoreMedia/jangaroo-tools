joo.classLoader.prepare("package flash.events",/* {*/

/**
 * A ProgressEvent object is dispatched when a load operation has begun or a socket has received data. These events are usually generated when SWF files, images or data are loaded into an application. There are two types of progress events: <code>ProgressEvent.PROGRESS</code> and <code>ProgressEvent.SOCKET_DATA</code>. Additionally, in AIR ProgressEvent objects are dispatched when a data is sent to or from a child process using the NativeProcess class.
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/ProgressEvent.html#includeExamplesSummary">View the examples</a></p>
 * @see flash.filesystem
 * @see flash.display.LoaderInfo
 * @see flash.net.Socket
 *
 */
"public class ProgressEvent extends flash.events.Event",2,function($$private){;return[ 
  /**
   * The number of items or bytes loaded when the listener processes the event.
   */
  "public native function get bytesLoaded"/*():Number;*/,

  /**
   * @private
   */
  "public native function set bytesLoaded"/*(value:Number):void;*/,

  /**
   * The total number of items or bytes that will be loaded if the loading process succeeds. If the progress event is dispatched/attached to a Socket object, the bytesTotal will always be 0 unless a value is specified in the bytesTotal parameter of the constructor. The actual number of bytes sent back or forth is not set and is up to the application developer.
   */
  "public native function get bytesTotal"/*():Number;*/,

  /**
   * @private
   */
  "public native function set bytesTotal"/*(value:Number):void;*/,

  /**
   * Creates an Event object that contains information about progress events. Event objects are passed as parameters to event listeners.
   * @param type The type of the event. Possible values are:<code>ProgressEvent.PROGRESS</code>, <code>ProgressEvent.SOCKET_DATA</code>, <code>ProgressEvent.STANDARD_ERROR_DATA</code>, <code>ProgressEvent.STANDARD_INPUT_PROGRESS</code>, and <code>ProgressEvent.STANDARD_OUTPUT_DATA</code>.
   * @param bubbles Determines whether the Event object participates in the bubbling stage of the event flow.
   * @param cancelable Determines whether the Event object can be canceled.
   * @param bytesLoaded The number of items or bytes loaded at the time the listener processes the event.
   * @param bytesTotal The total number of items or bytes that will be loaded if the loading process succeeds.
   *
   */
  "public function ProgressEvent",function ProgressEvent$(type/*:String*/, bubbles/*:Boolean = false*/, cancelable/*:Boolean = false*/, bytesLoaded/*:Number = 0*/, bytesTotal/*:Number = 0*/) {if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){bubbles = false;}cancelable = false;}bytesLoaded = 0;}bytesTotal = 0;}
    this.super$2(type, bubbles, cancelable);
    this.bytesLoaded = bytesLoaded;
    this.bytesTotal = bytesTotal;
  },

  /**
   * Creates a copy of the ProgressEvent object and sets each property's value to match that of the original.
   * @return A new ProgressEvent object with property values that match those of the original.
   *
   */
  "override public function clone",function clone()/*:Event*/ {
    return new flash.events.ProgressEvent(this.type, this.bubbles, this.cancelable, this.bytesLoaded, this.bytesTotal);
  },

  /**
   * Returns a string that contains all the properties of the ProgressEvent object. The string is in the following format:
   * <p><code>[ProgressEvent type=<i>value</i> bubbles=<i>value</i> cancelable=<i>value</i> bytesLoaded=<i>value</i> bytesTotal=<i>value</i>]</code></p>
   * @return A string that contains all the properties of the <code>ProgressEvent</code> object.
   *
   */
  "override public function toString",function toString()/*:String*/ {
    return this.formatToString("TimerEvent", "type", "bubbles", "cancelable", "bytesLoaded", "bytesTotal");
  },

  /**
   * Defines the value of the <code>type</code> property of a <code>progress</code> event object.
   * <p>This event has the following properties:</p>
   * <table>
   * <tr><th>Property</th><th>Value</th></tr>
   * <tr>
   * <td><code>bubbles</code></td>
   * <td><code>false</code></td></tr>
   * <tr>
   * <td><code>bytesLoaded</code></td>
   * <td>The number of items or bytes loaded at the time the listener processes the event.</td></tr>
   * <tr>
   * <td><code>bytesTotal</code></td>
   * <td>The total number of items or bytes that ultimately will be loaded if the loading process succeeds.</td></tr>
   * <tr>
   * <td><code>cancelable</code></td>
   * <td><code>false</code>; there is no default behavior to cancel.</td></tr>
   * <tr>
   * <td><code>currentTarget</code></td>
   * <td>The object that is actively processing the Event object with an event listener.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The object reporting progress.</td></tr></table>
   * @see flash.display.LoaderInfo#event:progress
   * @see flash.media.Sound#event:progress
   * @see flash.net.FileReference#event:progress
   * @see flash.net.URLLoader#event:progress
   * @see flash.net.URLStream#event:progress
   *
   */
  "public static const",{ PROGRESS/*:String*/ : "progress"},
  /**
   * Defines the value of the <code>type</code> property of a <code>socketData</code> event object.
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
   * <td>The object that is actively processing the Event.</td></tr>
   * <tr>
   * <td><code>bytesLoaded</code></td>
   * <td>The number of items or bytes loaded at the time the listener processes the event.</td></tr>
   * <tr>
   * <td><code>bytesTotal</code></td>
   * <td>0; this property is not used by <code>socketData</code> event objects.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The socket reporting progress.</td></tr></table>
   * @see flash.net.Socket#event:socketData
   *
   */
  "public static const",{ SOCKET_DATA/*:String*/ : "socketData"},

];},[],["flash.events.Event"], "0.8.0", "0.8.1"
);