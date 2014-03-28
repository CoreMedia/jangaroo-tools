joo.classLoader.prepare("package flash.events",/* {
import flash.display.InteractiveObject*/

/**
 * An InteractiveObject dispatches a ContextMenuEvent object when the user opens or interacts with the context menu. There are two types of ContextMenuEvent objects:
 * <ul>
 * <li><code>ContextMenuEvent.MENU_ITEM_SELECT</code></li>
 * <li><code>ContextMenuEvent.MENU_SELECT</code></li></ul>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/ContextMenuEvent.html#includeExamplesSummary">View the examples</a></p>
 * @see flash.ui.ContextMenu
 * @see flash.ui.ContextMenuItem
 *
 */
"public class ContextMenuEvent extends flash.events.Event",2,function($$private){;return[ 
  /**
   * The display list object to which the menu is attached. This could be the mouse target (<code>mouseTarget</code>) or one of its ancestors in the display list.
   */
  "public native function get contextMenuOwner"/*():InteractiveObject;*/,

  /**
   * @private
   */
  "public native function set contextMenuOwner"/*(value:InteractiveObject):void;*/,

  /**
   * The display list object on which the user right-clicked to display the context menu. This could be the display list object to which the menu is attached (<code>contextMenuOwner</code>) or one of its display list descendants.
   * <p>The value of this property can be <code>null</code> in two circumstances: if there no mouse target, for example when you mouse over something from the background; or there is a mouse target, but it is in a security sandbox to which you don't have access. Use the <code>isMouseTargetInaccessible()</code> property to determine which of these reasons applies.</p>
   * @see #isMouseTargetInaccessible
   *
   */
  "public native function get mouseTarget"/*():InteractiveObject;*/,

  /**
   * @private
   */
  "public native function set mouseTarget"/*(value:InteractiveObject):void;*/,

  /**
   * Creates an Event object that contains specific information about menu events. Event objects are passed as parameters to event listeners.
   * @param type The type of the event. Possible values are:
   * <ul>
   * <li><code>ContextMenuEvent.MENU_ITEM_SELECT</code></li>
   * <li><code>ContextMenuEvent.MENU_SELECT</code></li></ul>
   * @param bubbles Determines whether the Event object participates in the bubbling stage of the event flow. Event listeners can access this information through the inherited <code>bubbles</code> property.
   * @param cancelable Determines whether the Event object can be canceled. Event listeners can access this information through the inherited <code>cancelable</code> property.
   * @param mouseTarget The display list object on which the user right-clicked to display the context menu. This could be the <code>contextMenuOwner</code> or one of its display list descendants.
   * @param contextMenuOwner The display list object to which the menu is attached. This could be the <code>mouseTarget</code> or one of its ancestors in the display list.
   *
   * @see #MENU_ITEM_SELECT
   * @see #MENU_SELECT
   *
   */
  "public function ContextMenuEvent",function ContextMenuEvent$(type/*:String*/, bubbles/*:Boolean = false*/, cancelable/*:Boolean = false*/, mouseTarget/*:InteractiveObject = null*/, contextMenuOwner/*:InteractiveObject = null*/) {if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){bubbles = false;}cancelable = false;}mouseTarget = null;}contextMenuOwner = null;}
    this.super$2(type, bubbles, cancelable);
    this.mouseTarget = mouseTarget;
    this.contextMenuOwner = contextMenuOwner;
  },

  /**
   * Creates a copy of the ContextMenuEvent object and sets the value of each property to match that of the original.
   * @return A new ContextMenuEvent object with property values that match those of the original.
   *
   */
  "override public function clone",function clone()/*:Event*/ {
    return new flash.events.ContextMenuEvent(this.type, this.bubbles, this.cancelable, this.mouseTarget, this.contextMenuOwner);
  },

  /**
   * Returns a string that contains all the properties of the ContextMenuEvent object. The string is in the following format:
   * <p><code>[ContextMenuEvent type=<i>value</i> bubbles=<i>value</i> cancelable=<i>value</i> ... contextMenuOwner=<i>value</i>]</code></p>
   * @return A string that contains all the properties of the ContextMenuEvent object.
   *
   */
  "override public function toString",function toString()/*:String*/ {
    return this.formatToString("ContextMenuEvent", "type", "bubbles", "cancelable", "mouseTarget", "contextMenuOwner");
  },

  /**
   * Defines the value of the <code>type</code> property of a <code>menuItemSelect</code> event object.
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
   * <td><code>contextMenuOwner</code></td>
   * <td>The display list object to which the menu is attached.</td></tr>
   * <tr>
   * <td><code>currentTarget</code></td>
   * <td>The object that is actively processing the Event object with an event listener.</td></tr>
   * <tr>
   * <td><code>mouseTarget</code></td>
   * <td>The display list object on which the user right-clicked to display the context menu.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The ContextMenuItem object that has been selected. The target is not always the object in the display list that registered the event listener. Use the <code>currentTarget</code> property to access the object in the display list that is currently processing the event.</td></tr></table>
   * @see flash.ui.ContextMenuItem#event:menuItemSelect
   *
   */
  "public static const",{ MENU_ITEM_SELECT/*:String*/ : "menuItemSelect"},
  /**
   * Defines the value of the <code>type</code> property of a <code>menuSelect</code> event object.
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
   * <td><code>contextMenuOwner</code></td>
   * <td>The display list object to which the menu is attached.</td></tr>
   * <tr>
   * <td><code>currentTarget</code></td>
   * <td>The object that is actively processing the Event object with an event listener.</td></tr>
   * <tr>
   * <td><code>mouseTarget</code></td>
   * <td>The display list object on which the user right-clicked to display the context menu.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The ContextMenu object that is about to be displayed. The target is not always the object in the display list that registered the event listener. Use the <code>currentTarget</code> property to access the object in the display list that is currently processing the event.</td></tr></table>
   * @see flash.ui.ContextMenu#event:menuSelect
   *
   */
  "public static const",{ MENU_SELECT/*:String*/ : "menuSelect"},
];},[],["flash.events.Event"], "0.8.0", "0.8.3"
);