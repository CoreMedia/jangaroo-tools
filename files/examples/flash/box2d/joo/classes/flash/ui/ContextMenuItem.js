joo.classLoader.prepare("package flash.ui",/* {

import flash.events.EventDispatcher*/

/**
 * Dispatched when a user selects an item from a context menu. The user generates the context menu by clicking the secondary button of the user's pointing device.
 * @eventType flash.events.ContextMenuEvent.MENU_ITEM_SELECT
 */
{Event:{name:"menuItemSelect", type:"flash.events.ContextMenuEvent"}},

/**
 * The ContextMenuItem class represents an item in the context menu. Each ContextMenuItem object has a caption (text) that is displayed in the context menu. To add a new item to a context menu, you add it to the <code>customItems</code> array of a ContextMenu object.
 * <p>With the properties of the ContextMenuItem class you can enable or disable specific menu items, and you can make items visible or invisible.</p>You write an event handler for the <code>menuItemSelect</code> event to add functionality to the menu item when the user selects it.
 * <p>Custom menu items appear at the top of the context menu, above any built-in items. A separator bar divides custom menu items from built-in items. In AIR, there are no built-in items and the following restrictions do not apply to content in the AIR application sandbox.</p>
 * <p>Restrictions:</p>
 * <ul>
 * <li>You can add no more than 15 custom items to a context menu.</li>
 * <li>Each caption must contain at least one visible character.</li>
 * <li>Control characters, newlines, and other white space characters are ignored.</li>
 * <li>No caption can be more than 100 characters long.</li>
 * <li>Captions that are identical to any built-in menu item, or to another custom item, are ignored, whether the matching item is visible or not. Menu captions are compared to built-in captions or existing custom captions without regard to case, punctuation, or white space.</li>
 * <li>The following captions are not allowed, but the words may be used in conjunction with other words to form a custom caption (for example, although "Paste" is not allowed, "Paste tastes great" is allowed):
 * <pre> Save
 Zoom In
 Zoom Out
 100%
 Show All
 Quality
 Play
 Loop
 Rewind
 Forward
 Back
 Movie not loaded
 About
 Print
 Show Redraw Regions
 Debugger
 Undo
 Cut
 Copy
 Paste
 Delete
 Select All
 Open
 Open in new window
 Copy link
 </pre></li>
 * <li>None of the following words can appear in a custom caption on their own or in conjunction with other words:
 * <pre> Adobe
 Macromedia
 Flash Player
 Settings
 </pre></li></ul>
 * <p><b>Note:</b> When the player is running on a non-English system, the caption strings are compared to both the English list and the localized equivalents.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/ui/ContextMenuItem.html#includeExamplesSummary">View the examples</a></p>
 * @see ContextMenu
 * @see ContextMenuBuiltInItems
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118676a48d0-8000.html Working with menus
 *
 */
"public final class ContextMenuItem extends flash.events.EventDispatcher",2,function($$private){;return[ 
  /**
   * Specifies the menu item caption (text) displayed in the context menu. See the ContextMenuItem class overview for <code>caption</code> value restrictions.
   */
  "public function get caption",function caption$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set caption",function caption$set(value/*:String*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Indicates whether a separator bar should appear above the specified menu item.
   * <p><b>Note:</b> A separator bar always appears between any custom menu items and the built-in menu items.</p>
   * <p>The default value is <code>false.</code></p>
   */
  "public function get separatorBefore",function separatorBefore$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set separatorBefore",function separatorBefore$set(value/*:Boolean*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Indicates whether the specified menu item is visible when the Flash Player context menu is displayed.
   * <p>The default value is <code>true.</code></p>
   */
  "public function get visible",function visible$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set visible",function visible$set(value/*:Boolean*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Creates a new ContextMenuItem object that can be added to the <code>ContextMenu.customItems</code> array.
   * @param caption Specifies the text associated with the menu item. See the ContextMenuItem class overview for <code>caption</code> value restrictions.
   * @param separatorBefore Specifies whether a separator bar appears above the menu item in the context menu. The default value is <code>false</code>.
   * @param enabled Specifies whether the menu item is enabled or disabled in the context menu. The default value is <code>true</code> (enabled). This parameter is optional.
   * @param visible Specifies whether the menu item is visible or invisible. The default value is <code>true</code> (visible).
   *
   */
  "public function ContextMenuItem",function ContextMenuItem$(caption/*:String*/, separatorBefore/*:Boolean = false*/, enabled/*:Boolean = true*/, visible/*:Boolean = true*/) {if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){separatorBefore = false;}enabled = true;}visible = true;}this.super$2();
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.1"
);