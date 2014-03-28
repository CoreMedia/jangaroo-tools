joo.classLoader.prepare("package flash.display",/* {
import flash.media.SoundTransform*/

/**
 * The SimpleButton class lets you control all instances of button symbols in a SWF file.
 * <p>In Flash Professional, you can give a button an instance name in the Property inspector. SimpleButton instance names are displayed in the Movie Explorer and in the Insert Target Path dialog box in the Actions panel. After you create an instance of a button in Flash Professional, you can use the methods and properties of the SimpleButton class to manipulate buttons with ActionScript.</p>
 * <p>In ActionScript 3.0, you use the <code>new SimpleButton()</code> constructor to create a SimpleButton instance.</p>
 * <p>The SimpleButton class inherits from the InteractiveObject class.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/display/SimpleButton.html#includeExamplesSummary">View the examples</a></p>
 * @see InteractiveObject
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e3c.html Core display classes
 *
 */
"public class SimpleButton extends flash.display.InteractiveObject",4,function($$private){;return[ 
  /**
   * Specifies a display object that is used as the visual object for the button "Down" state —the state that the button is in when the user selects the <code>hitTestState</code> object.
   * @see #hitTestState
   * @see #overState
   * @see #upState
   *
   */
  "public native function get downState"/*():DisplayObject;*/,

  /**
   * @private
   */
  "public native function set downState"/*(value:DisplayObject):void;*/,

  /**
   * A Boolean value that specifies whether a button is enabled. When a button is disabled (the enabled property is set to <code>false</code>), the button is visible but cannot be clicked. The default value is <code>true</code>. This property is useful if you want to disable part of your navigation; for example, you might want to disable a button in the currently displayed page so that it can't be clicked and the page cannot be reloaded.
   * <p><b>Note:</b> To prevent mouseClicks on a button, set both the <code>enabled</code> and <code>mouseEnabled</code> properties to <code>false</code>.</p>
   */
  "public function get enabled",function enabled$get()/*:Boolean*/ {
    return this._enabled$4;
  },

  /**
   * @private
   */
  "public function set enabled",function enabled$set(value/*:Boolean*/)/*:void*/ {
    this._enabled$4 = value;
    this.getElement().disabled = !value;
  },

  /**
   * Specifies a display object that is used as the hit testing object for the button. For a basic button, set the <code>hitTestState</code> property to the same display object as the <code>overState</code> property. If you do not set the <code>hitTestState</code> property, the SimpleButton is inactive &mdash; it does not respond to user input events.
   * @see #downState
   * @see #overState
   * @see #upState
   *
   */
  "public native function get hitTestState"/*():DisplayObject;*/,

  /**
   * @private
   */
  "public native function set hitTestState"/*(value:DisplayObject):void;*/,

  /**
   * Specifies a display object that is used as the visual object for the button over state � the state that the button is in when the pointer is positioned over the button.
   * @see #downState
   * @see #hitTestState
   * @see #upState
   *
   */
  "public native function get overState"/*():DisplayObject;*/,

  /**
   * @private
   */
  "public native function set overState"/*(value:DisplayObject):void;*/,

  /**
   * The SoundTransform object assigned to this button. A SoundTransform object includes properties for setting volume, panning, left speaker assignment, and right speaker assignment. This SoundTransform object applies to all states of the button. This SoundTransform object affects only embedded sounds.
   * @see flash.media.SoundTransform
   *
   */
  "public native function get soundTransform"/*():SoundTransform;*/,

  /**
   * @private
   */
  "public native function set soundTransform"/*(value:SoundTransform):void;*/,

  /**
   * Indicates whether other display objects that are SimpleButton or MovieClip objects can receive user input release events. The <code>trackAsMenu</code> property lets you create menus. You can set the <code>trackAsMenu</code> property on any SimpleButton or MovieClip object. If the <code>trackAsMenu</code> property does not exist, the default behavior is <code>false</code>.
   * <p>You can change the <code>trackAsMenu</code> property at any time; the modified button immediately takes on the new behavior.</p>
   */
  "public native function get trackAsMenu"/*():Boolean;*/,

  /**
   * @private
   */
  "public native function set trackAsMenu"/*(value:Boolean):void;*/,

  /**
   * Specifies a display object that is used as the visual object for the button up state � the state that the button is in when the pointer is not positioned over the button.
   * @see #downState
   * @see #hitTestState
   * @see #overState
   *
   */
  "public native function get upState"/*():DisplayObject;*/,

  /**
   * @private
   */
  "public native function set upState"/*(value:DisplayObject):void;*/,

  /**
   * A Boolean value that, when set to <code>true</code>, indicates whether the hand cursor is shown when the pointer rolls over a button. If this property is set to <code>false</code>, the arrow pointer cursor is displayed instead. The default is <code>true</code>.
   * <p>You can change the <code>useHandCursor</code> property at any time; the modified button immediately uses the new cursor behavior.</p>
   */
  "public native function get useHandCursor"/*():Boolean;*/,

  /**
   * @private
   */
  "public native function set useHandCursor"/*(value:Boolean):void;*/,

  /**
   * Creates a new SimpleButton instance. Any or all of the display objects that represent the various button states can be set as parameters in the constructor.
   * @param upState The initial value for the SimpleButton up state.
   * @param overState The initial value for the SimpleButton over state.
   * @param downState The initial value for the SimpleButton down state.
   * @param hitTestState The initial value for the SimpleButton hitTest state.
   *
   */
  "public function SimpleButton",function SimpleButton$(upState/*:DisplayObject = null*/, overState/*:DisplayObject = null*/, downState/*:DisplayObject = null*/, hitTestState/*:DisplayObject = null*/) {if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){upState = null;}overState = null;}downState = null;}hitTestState = null;}
    this.super$4();
    this.upState = upState;
    this.overState = overState;
    this.downState = downState;
    this.hitTestState = hitTestState;
  },

  // ************************** Jangaroo part **************************

  /**
   * @private
   */
  "override protected function getElementName",function getElementName()/*:String*/ {
    return "button";
  },

  "private var",{ _enabled/* : Boolean*/ : true},
];},[],["flash.display.InteractiveObject"], "0.8.0", "0.8.3"
);