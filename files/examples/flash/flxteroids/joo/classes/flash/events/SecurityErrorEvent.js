joo.classLoader.prepare("package flash.events",/* {*/

/**
 * An object dispatches a SecurityErrorEvent object to report the occurrence of a security error. Security errors reported through this class are generally from asynchronous operations, such as loading data, in which security violations may not manifest immediately. Your event listener can access the object's <code>text</code> property to determine what operation was attempted and any URLs that were involved. If there are no event listeners, the debugger version of Flash Player or the AIR Debug Launcher (ADL) application automatically displays an error message that contains the contents of the <code>text</code> property. There is one type of security error event: <code>SecurityErrorEvent.SECURITY_ERROR</code>.
 * <p>Security error events are the final events dispatched for any target object. This means that any other events, including generic error events, are not dispatched for a target object that experiences a security error.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/SecurityErrorEvent.html#includeExamplesSummary">View the examples</a></p>
 * @see flash.system.Security
 * @see #SECURITY_ERROR
 *
 */
"public class SecurityErrorEvent extends flash.events.ErrorEvent",4,function($$private){;return[ 
  /**
   * Creates an Event object that contains information about security error events. Event objects are passed as parameters to event listeners.
   * @param type The type of the event. Event listeners can access this information through the inherited <code>type</code> property. There is only one type of error event: <code>SecurityErrorEvent.SECURITY_ERROR</code>.
   * @param bubbles Determines whether the Event object participates in the bubbling stage of the event flow. Event listeners can access this information through the inherited <code>bubbles</code> property.
   * @param cancelable Determines whether the Event object can be canceled. Event listeners can access this information through the inherited <code>cancelable</code> property.
   * @param text Text to be displayed as an error message. Event listeners can access this information through the <code>text</code> property.
   * @param id A reference number to associate with the specific error.
   *
   * @see #SECURITY_ERROR
   *
   */
  "public function SecurityErrorEvent",function SecurityErrorEvent$(type/*:String*/, bubbles/*:Boolean = false*/, cancelable/*:Boolean = false*/, text/*:String = ""*/, id/*:int = 0*/) {if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){bubbles = false;}cancelable = false;}text = "";}id = 0;}
    this.super$4(type, bubbles, cancelable, text, id);
  },

  /**
   * Creates a copy of the SecurityErrorEvent object and sets the value of each property to match that of the original.
   * @return A new securityErrorEvent object with property values that match those of the original.
   *
   */
  "override public function clone",function clone()/*:Event*/ {
    return new flash.events.SecurityErrorEvent(this.type, this.bubbles, this.cancelable, this.text, this.id);
  },

  /**
   * Returns a string that contains all the properties of the SecurityErrorEvent object. The string is in the following format:
   * <p><code>[SecurityErrorEvent type=<i>value</i> bubbles=<i>value</i> cancelable=<i>value</i> text=<i>value</i> errorID=<i>value</i>]</code> The <code>errorId</code> is only available in Adobe AIR</p>
   * @return A string that contains all the properties of the SecurityErrorEvent object.
   *
   */
  "override public function toString",function toString()/*:String*/ {
    return this.formatToString("SecurityErrorEvent", "type", "bubbles", "cancelable", "text");
  },

  /**
   * The <code>SecurityErrorEvent.SECURITY_ERROR</code> constant defines the value of the <code>type</code> property of a <code>securityError</code> event object.
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
   * <td>The network object reporting the security error.</td></tr>
   * <tr>
   * <td><code>text</code></td>
   * <td>Text to be displayed as an error message.</td></tr></table>
   * @see flash.net.FileReference#event:securityError
   * @see flash.net.LocalConnection#event:securityError
   * @see flash.net.NetConnection#event:securityError
   * @see flash.net.Socket#event:securityError
   * @see flash.net.URLLoader#event:securityError
   * @see flash.net.URLStream#event:securityError
   * @see flash.net.XMLSocket#event:securityError
   *
   */
  "public static const",{ SECURITY_ERROR/*:String*/ : "securityError"},
];},[],["flash.events.ErrorEvent"], "0.8.0", "0.8.1"
);