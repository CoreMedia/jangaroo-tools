joo.classLoader.prepare("package flash.accessibility",/* {
import flash.display.DisplayObject
import flash.errors.IllegalOperationError*/


/**
 * The Accessibility class manages communication with screen readers. Screen readers are a type of assistive technology for visually impaired users that provides an audio version of screen content. The methods of the Accessibility class are static—that is, you don't have to create an instance of the class to use its methods.
 * <p><b>Mobile Browser Support:</b> This class is not supported in mobile browsers.</p>
 * <p><i>AIR profile support:</i> This feature is supported on all desktop operating systems, but is not supported on mobile devices or on AIR for TV devices. See <a href="http://help.adobe.com/en_US/air/build/WS144092a96ffef7cc16ddeea2126bb46b82f-8000.html">AIR Profile Support</a> for more information regarding API support across multiple profiles.</p>
 * <p>To get and set accessible properties for a specific object, such as a button, movie clip, or text field, use the <code>DisplayObject.accessibilityProperties</code> property. To determine whether the player or runtime is running in an environment that supports accessibility aids, use the <code>Capabilities.hasAccessibility</code> property.</p>
 * <p><b>Note:</b> AIR 2 supports the JAWS 11 (or higher) screen reader software. For additional information, please see http://www.adobe.com/accessibility/.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/accessibility/Accessibility.html#includeExamplesSummary">View the examples</a></p>
 * @see flash.display.DisplayObject#accessibilityProperties
 * @see flash.system.Capabilities#hasAccessibility
 * @see flash.net.Socket
 * @see http://www.adobe.com/accessibility/ http://www.adobe.com/accessibility/
 *
 */
"public final class Accessibility",1,function($$private){;return[ 
  /**
   * Indicates whether a screen reader is active and the application is communicating with it. Use this method when you want your application to behave differently in the presence of a screen reader.
   * <p>Once this property is set to <code>true</code>, it remains <code>true</code> for the duration of the application. (It is unusual for a user to turn off the screen reader once it is started.)</p>
   * <p><b>Note:</b> Before calling this method, wait 1 or 2 seconds after launching your AIR application or after the first appearance of the Flash<sup>®</sup> Player window in which your document is playing. Otherwise, you might get a return value of <code>false</code> even if there is an active accessibility client. This happens because of an asynchronous communication mechanism between accessibility clients and Flash Player or AIR.</p>To determine whether the player is running in an environment that supports screen readers, use the <code>Capabilities.hasAccessibility</code> property.
   * @see flash.system.Capabilities#hasAccessibility
   * @see #updateProperties()
   *
   */
  "public static function get active",function active$get()/*:Boolean*/ {
    return false; // TODO: can we implement this?
  },

  /**
   * Sends an event to the Microsoft Active Accessibility API. Microsoft Active
   * Accessibility handles that event and sends the event to any active screen reader
   * application, which in turn reports the change to the user.  For example, when a
   * user toggles a RadioButton instance, the RadioButton's Accessibility Implementation
   * calls Accessibility.sendEvent() with the eventType EVENT_OBJECT_STATECHANGE.
   *
   * @param source The DisplayObject from which the accessibility event is being sent.
   * @param childID The child id of the accessibility interface element to which the event applies (for example, an individual list item in a list box). Use 0 to indicate that the event applies to the DisplayObject supplied in the <code>source</code> parameter.
   * @param eventType A constant indicating the event type. Event names and values are a subset of the <a href="constants.html#events">MSAA event constants</a>.
   * @param nonHTML A Boolean indication of whether or not the event is one of the standard event types that can be generated from an HTML form. When set to <code>true</code>, this parameter helps prevent some problems that may occur with screen readers that interperet Flash content as part of the HTML page.  The default value is <code>false</code>.
   *
   * @see AccessibilityImplementation
   * @see mx.accessibility.AccImpl
   *
   */
  "public static function sendEvent",function sendEvent(source/*:DisplayObject*/, childID/*:uint*/, eventType/*:uint*/, nonHTML/*:Boolean = false*/)/*:void*/ {if(arguments.length<4){nonHTML = false;}
    throw new flash.errors.IllegalOperationError();
  },

  /**
   * Tells Flash Player to apply any accessibility changes made by using the <code>DisplayObject.accessibilityProperties</code> property. You need to call this method for your changes to take effect.
   * <p>If you modify the accessibility properties for multiple objects, only one call to the <code>Accessibility.updateProperties()</code> method is necessary; multiple calls can result in reduced performance and erroneous screen reader output.</p>
   * @throws flash.errors.IllegalOperationError Accessibility is not supported in this version of Flash Player. Do not call the <code>Accessibility.updateProperties()</code> method if the <code>flash.system.Capabilities.hasAccessibility</code> property is <code>false</code>.
   *
   * @see #active
   * @see flash.display.DisplayObject#accessibilityProperties
   * @see flash.system.Capabilities#hasAccessibility
   *
   */
  "public static function updateProperties",function updateProperties()/*:void*/ {
    throw new flash.errors.IllegalOperationError();
  },
];},["active","sendEvent","updateProperties"],["flash.errors.IllegalOperationError"], "0.8.0", "0.8.3"
);