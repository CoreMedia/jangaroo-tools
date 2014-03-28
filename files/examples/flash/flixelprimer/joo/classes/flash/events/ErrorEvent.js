joo.classLoader.prepare("package flash.events",/* {*/


/**
 * An object dispatches an ErrorEvent object when an error causes an asynchronous operation to fail.
 * <p>The ErrorEvent class defines only one type of <code>error</code> event: <code>ErrorEvent.ERROR</code>. The ErrorEvent class also serves as the base class for several other error event classes, including the AsyncErrorEvent, IOErrorEvent, SecurityErrorEvent, SQLErrorEvent, and UncaughtErrorEvent classes.</p>
 * <p>You can check for <code>error</code> events that do not have any listeners by registering a listener for the <code>uncaughtError</code> (UncaughtErrorEvent.UNCAUGHT_ERROR) event.</p>
 * <p>An uncaught error also causes an error dialog box displaying the error event to appear when content is running in the debugger version of Flash Player or the AIR Debug Launcher (ADL) application.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/ErrorEvent.html#includeExamplesSummary">View the examples</a></p>
 * @see UncaughtErrorEvent
 *
 */
"public class ErrorEvent extends flash.events.TextEvent",3,function($$private){;return[ 
  /**
   * Creates an Event object that contains information about error events. Event objects are passed as parameters to event listeners.
   * @param type The type of the event. Event listeners can access this information through the inherited <code>type</code> property. There is only one type of error event: <code>ErrorEvent.ERROR</code>.
   * @param bubbles Determines whether the Event object bubbles. Event listeners can access this information through the inherited <code>bubbles</code> property.
   * @param cancelable Determines whether the Event object can be canceled. Event listeners can access this information through the inherited <code>cancelable</code> property.
   * @param text Text to be displayed as an error message. Event listeners can access this information through the <code>text</code> property.
   * @param id A reference number to associate with the specific error (supported in Adobe AIR only).
   *
   */
  "public function ErrorEvent",function ErrorEvent$(type/*:String*/, bubbles/*:Boolean = false*/, cancelable/*:Boolean = false*/, text/*:String = ""*/, id/*:int = 0*/) {if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){bubbles = false;}cancelable = false;}text = "";}id = 0;}
    this.super$3(type, bubbles, cancelable, text);
    this['id'] = id;
  },

  /**
   * Creates a copy of the ErrorEvent object and sets the value of each property to match that of the original.
   * @return A new ErrorEvent object with property values that match those of the original.
   *
   */
  "override public function clone",function clone()/*:Event*/ {
    return new flash.events.ErrorEvent(this.type, this.bubbles, this.cancelable, this.text, this.id);
  },

  /**
   * Returns a string that contains all the properties of the ErrorEvent object. The string is in the following format:
   * <p><code>[ErrorEvent type=<i>value</i> bubbles=<i>value</i> cancelable=<i>value</i> text=<i>value</i> errorID=<i>value</i>]</code></p>
   * <p><b>Note:</b> The <code>errorId</code> value returned by the <code>toString()</code> method is only available for Adobe AIR. While Flash Player 10.1 supports the <code>errorID</code> property, calling <code>toString()</code> on the ErrorEvent object does not provide the <code>errorId</code> value in Flash Player.</p>
   * @return A string that contains all the properties of the ErrorEvent object.
   *
   */
  "override public function toString",function toString()/*:String*/ {
    return this.formatToString("ErrorEvent", "type", "bubbles", "cancelable", "text");
  },

  /**
   * Defines the value of the <code>type</code> property of an <code>error</code> event object.
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
   * <td>The object experiencing a network operation failure.</td></tr>
   * <tr>
   * <td><code>text</code></td>
   * <td>Text to be displayed as an error message.</td></tr></table>
   */
  "public static const",{ ERROR/*:String*/ : "error"},

  /**
   * @private
   */
  "protected native function get id"/*():int;*/,
];},[],["flash.events.TextEvent"], "0.8.0", "0.8.3"
);