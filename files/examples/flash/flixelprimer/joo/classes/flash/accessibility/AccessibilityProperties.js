joo.classLoader.prepare("package flash.accessibility",/* {*/


/**
 * The AccessibilityProperties class lets you control the presentation of Flash objects to accessibility aids, such as screen readers.
 * <p>You can attach an AccessibilityProperties object to any display object, but Flash Player will read your AccessibilityProperties object only for certain kinds of objects: entire SWF files (as represented by <code>DisplayObject.root</code>), container objects (<code>DisplayObjectContainer</code> and subclasses), buttons (<code>SimpleButton</code> and subclasses), and text (<code>TextField</code> and subclasses).</p>
 * <p>The <code>name</code> property of these objects is the most important property to specify because accessibility aids provide the names of objects to users as a basic means of navigation. Do not confuse <code>AccessibilityProperties.name</code> with <code>DisplayObject.name</code>; these are separate and unrelated. The <code>AccessibilityProperties.name</code> property is a name that is read aloud by the accessibility aids, whereas <code>DisplayObject.name</code> is essentially a variable name visible only to ActionScript code.</p>
 * <p>In Flash Professional, the properties of <code>AccessibilityProperties</code> objects override the corresponding settings available in the Accessibility panel during authoring.</p>
 * <p>To determine whether Flash Player is running in an environment that supports accessibility aids, use the <code>Capabilities.hasAccessibility</code> property. If you modify AccessibilityProperties objects, you need to call the <code>Accessibility.updateProperties()</code> method for the changes to take effect.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/accessibility/AccessibilityProperties.html#includeExamplesSummary">View the examples</a></p>
 * @see Accessibility#updateProperties()
 * @see flash.display.DisplayObject#accessibilityProperties
 * @see flash.display.InteractiveObject#tabIndex
 * @see flash.system.Capabilities#hasAccessibility
 *
 */
"public class AccessibilityProperties",1,function($$private){;return[ 
  /**
   * Provides a description for this display object in the accessible presentation. If you have a lot of information to present about the object, it is best to choose a concise name and put most of your content in the <code>description</code> property. Applies to whole SWF files, containers, buttons, and text. The default value is an empty string.
   * <p>In Flash Professional, this property corresponds to the Description field in the Accessibility panel.</p>
   */
  "public var",{ description/*:String*/:null},
  /**
   * If <code>true</code>, causes Flash Player to exclude child objects within this display object from the accessible presentation. The default is <code>false</code>. Applies to whole SWF files and containers.
   */
  "public var",{ forceSimple/*:Boolean*/:false},
  /**
   * Provides a name for this display object in the accessible presentation. Applies to whole SWF files, containers, buttons, and text. Do not confuse with <code>DisplayObject.name</code>, which is unrelated. The default value is an empty string.
   * <p>In Flash Professional, this property corresponds to the Name field in the Accessibility panel.</p>
   */
  "public var",{ name/*:String*/:null},
  /**
   * If <code>true</code>, disables the Flash Player default auto-labeling system. Auto-labeling causes text objects inside buttons to be treated as button names, and text objects near text fields to be treated as text field names. The default is <code>false</code>. Applies only to whole SWF files.
   * <p>The <code>noAutoLabeling</code> property value is ignored unless you specify it before the first time an accessibility aid examines your SWF file. If you plan to set <code>noAutoLabeling</code> to <code>true</code>, you should do so as early as possible in your code.</p>
   */
  "public var",{ noAutoLabeling/*:Boolean*/:false},
  /**
   * Indicates a keyboard shortcut associated with this display object. Supply this string only for UI controls that you have associated with a shortcut key. Applies to containers, buttons, and text. The default value is an empty string.
   * <p><b>Note</b>: Assigning this property does not automatically assign the specified key combination to this object; you must do that yourself, for example, by listening for a <code>KeyboardEvent</code>.</p>
   * <p>The syntax for this string uses long names for modifier keys, and the plus(+) character to indicate key combination. Examples of valid strings are "Ctrl+F", "Ctrl+Shift+Z", and so on.</p>
   */
  "public var",{ shortcut/*:String*/:null},
  /**
   * If <code>true</code>, excludes this display object from accessible presentation. The default is <code>false</code>. Applies to whole SWF files, containers, buttons, and text.
   */
  "public var",{ silent/*:Boolean*/:false},

  /**
   * Creates a new AccessibilityProperties object.
   */
  "public function AccessibilityProperties",function AccessibilityProperties$() {
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["Error"], "0.8.0", "0.8.3"
);