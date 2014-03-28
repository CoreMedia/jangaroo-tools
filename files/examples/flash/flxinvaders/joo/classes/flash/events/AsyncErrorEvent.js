joo.classLoader.prepare("package flash.events",/* {*/


/**
 * An object dispatches an AsyncErrorEvent when an exception is thrown from native asynchronous code, which could be from, for example, LocalConnection, NetConnection, SharedObject, or NetStream. There is only one type of asynchronous error event: <code>AsyncErrorEvent.ASYNC_ERROR</code>.
 * @see #ASYNC_ERROR
 *
 */
"public class AsyncErrorEvent extends flash.events.ErrorEvent",4,function($$private){;return[ 
  /**
   * The exception that was thrown.
   */
  "public var",{ error/*:Error*/:null},

  /**
   * Creates an AsyncErrorEvent object that contains information about asyncError events. AsyncErrorEvent objects are passed as parameters to event listeners.
   * @param type The type of the event. Event listeners can access this information through the inherited <code>type</code> property. There is only one type of error event: <code>ErrorEvent.ERROR</code>.
   * @param bubbles Determines whether the Event object bubbles. Event listeners can access this information through the inherited <code>bubbles</code> property.
   * @param cancelable Determines whether the Event object can be canceled. Event listeners can access this information through the inherited <code>cancelable</code> property.
   * @param text Text to be displayed as an error message. Event listeners can access this information through the <code>text</code> property.
   * @param error The exception that occurred. If error is non-null, the event's <code>errorId</code> property is set from the error's <code>errorId</code> property.
   *
   */
  "public function AsyncErrorEvent",function AsyncErrorEvent$(type/*:String*/, bubbles/*:Boolean = false*/, cancelable/*:Boolean = false*/, text/*:String = ""*/, error/*:Error = null*/) {if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){bubbles = false;}cancelable = false;}text = "";}error = null;}
    this.super$4(type, bubbles, cancelable, text);
    this.error = error;
  },

  /**
   * Creates a copy of the AsyncErrorEvent object and sets the value of each property to match that of the original.
   * @return A new AsyncErrorEvent object with property values that match those of the original.
   *
   */
  "override public function clone",function clone()/*:Event*/ {
    return new flash.events.AsyncErrorEvent(this.type, this.bubbles, this.cancelable, this.text, this.error);
  },

  /**
   * Returns a string that contains all the properties of the AsyncErrorEvent object. The string is in the following format:
   * <p><code>[AsyncErrorEvent type=<i>value</i> bubbles=<i>value</i> cancelable=<i>value</i> ... error=<i>value</i> errorID=<i>value</i>]</code> The <code>errorId</code> is only available in Adobe AIR</p>
   * @return A string that contains all the properties of the AsyncErrorEvent object.
   *
   */
  "override public function toString",function toString()/*:String*/ {
    return this.formatToString("AsyncErrorEvent", "type", "bubbles", "cancelable", "error");
  },

  /**
   * The <code>AsyncErrorEvent.ASYNC_ERROR</code> constant defines the value of the <code>type</code> property of an <code>asyncError</code> event object.
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
   * <td>The object dispatching the event.</td></tr>
   * <tr>
   * <td><code>error</code></td>
   * <td>The error that triggered the event.</td></tr></table>
   */
  "public static const",{ ASYNC_ERROR/*:String*/ : "asyncError"},
];},[],["flash.events.ErrorEvent"], "0.8.0", "0.8.1"
);