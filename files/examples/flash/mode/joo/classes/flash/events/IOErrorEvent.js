joo.classLoader.prepare("package flash.events",/* {*/

/**
 * An IOErrorEvent object is dispatched when an error causes input or output operations to fail.
 * <p>You can check for error events that do not have any listeners by using the debugger version of Flash Player or the AIR Debug Launcher (ADL). The string defined by the <code>text</code> parameter of the IOErrorEvent constructor is displayed.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/IOErrorEvent.html#includeExamplesSummary">View the examples</a></p>
 * @see #IO_ERROR
 *
 */
"public class IOErrorEvent extends flash.events.ErrorEvent",4,function($$private){;return[ 
  /**
   * Creates an Event object that contains specific information about <code>ioError</code> events. Event objects are passed as parameters to Event listeners.
   * @param type The type of the event. Event listeners can access this information through the inherited <code>type</code> property. There is only one type of input/output error event: <code>IOErrorEvent.IO_ERROR</code>.
   * @param bubbles Determines whether the Event object participates in the bubbling stage of the event flow. Event listeners can access this information through the inherited <code>bubbles</code> property.
   * @param cancelable Determines whether the Event object can be canceled. Event listeners can access this information through the inherited <code>cancelable</code> property.
   * @param text Text to be displayed as an error message. Event listeners can access this information through the <code>text</code> property.
   * @param id A reference number to associate with the specific error (supported in Adobe AIR only).
   *
   * @see #IO_ERROR
   *
   */
  "public function IOErrorEvent",function IOErrorEvent$(type/*:String*/, bubbles/*:Boolean = false*/, cancelable/*:Boolean = false*/, text/*:String = ""*/, id/*:int = 0*/) {switch(arguments.length){case 0:case 1:bubbles = false;case 2:cancelable = false;case 3:text = "";case 4:id = 0;}
    this.super$4(type, bubbles, cancelable, text, id);
  },

  /**
   * Creates a copy of the IOErrorEvent object and sets the value of each property to match that of the original.
   * @return A new IOErrorEvent object with property values that match those of the original.
   *
   */
  "override public function clone",function clone()/*:Event*/ {
    return new flash.events.IOErrorEvent(this.type, this.bubbles, this.cancelable, this.text, this.id);
  },

  /**
   * Returns a string that contains all the properties of the IOErrorEvent object. The string is in the following format:
   * <p><code>[IOErrorEvent type=<i>value</i> bubbles=<i>value</i> cancelable=<i>value</i> text=<i>value</i> errorID=<i>value</i>]</code> The <code>errorId</code> is only available in Adobe AIR</p>
   * @return A string that contains all the properties of the IOErrorEvent object.
   *
   */
  "override public function toString",function toString()/*:String*/ {
    return this.formatToString("IOErrorEvent", "type", "bubbles", "cancelable", "text");
  },

  /**
   * Defines the value of the <code>type</code> property of an <code>ioError</code> event object.
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
   * <td><code>errorID</code></td>
   * <td>A reference number associated with the specific error (AIR only).</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The network object experiencing the input/output error.</td></tr>
   * <tr>
   * <td><code>text</code></td>
   * <td>Text to be displayed as an error message.</td></tr></table>
   * @see flash.display.LoaderInfo#event:ioError
   * @see flash.media.Sound#event:ioError
   * @see flash.net.SecureSocket#event:ioError
   * @see flash.net.Socket#event:ioError
   * @see flash.net.FileReference#event:ioError
   * @see flash.net.NetConnection#event:ioError
   * @see flash.net.NetStream#event:ioError
   * @see flash.net.URLLoader#event:ioError
   * @see flash.net.URLStream#event:ioError
   * @see flash.net.XMLSocket#event:ioError
   *
   */
  "public static const",{ IO_ERROR/*:String*/ : "ioError"},
];},[],["flash.events.ErrorEvent"], "0.8.0", "0.8.2-SNAPSHOT"
);