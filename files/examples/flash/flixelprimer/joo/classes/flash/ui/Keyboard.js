joo.classLoader.prepare("package flash.ui",/* {*/


/**
 * The Keyboard class is used to build an interface that can be controlled by a user with a standard keyboard. You can use the methods and properties of the Keyboard class without using a constructor. The properties of the Keyboard class are constants representing the keys that are most commonly used to control games.
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7d01.html Capturing keyboard input
 *
 */
"public final class Keyboard",1,function($$private){;return[ 
  /**
   * Specifies whether the Caps Lock key is activated (<code>true</code>) or not (<code>false</code>).
   */
  "public static function get capsLock",function capsLock$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies whether the Num Lock key is activated (<code>true</code>) or not (<code>false</code>).
   */
  "public static function get numLock",function numLock$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies whether the last key pressed is accessible by other SWF files. By default, security restrictions prevent code from a SWF file in one domain from accessing a keystroke generated from a SWF file in another domain.
   * @return The value <code>true</code> if the last key pressed can be accessed. If access is not permitted, this method returns <code>false</code>.
   *
   */
  "public static function isAccessible",function isAccessible()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Constant associated with the key code value for the Backspace key (8).
   */
  "public static const",{ BACKSPACE/*:uint*/ : 8},
  /**
   * Constant associated with the key code value for the Caps Lock key (20).
   */
  "public static const",{ CAPS_LOCK/*:uint*/ : 20},
  /**
   * Constant associated with the key code value for the Control key (17).
   */
  "public static const",{ CONTROL/*:uint*/ : 17},
  /**
   * Constant associated with the key code value for the Delete key (46).
   */
  "public static const",{ DELETE/*:uint*/ : 46},
  /**
   * Constant associated with the key code value for the Down Arrow key (40).
   */
  "public static const",{ DOWN/*:uint*/ : 40},
  /**
   * Constant associated with the key code value for the End key (35).
   */
  "public static const",{ END/*:uint*/ : 35},
  /**
   * Constant associated with the key code value for the Enter key (13).
   */
  "public static const",{ ENTER/*:uint*/ : 13},
  /**
   * Constant associated with the key code value for the Escape key (27).
   */
  "public static const",{ ESCAPE/*:uint*/ : 27},
  /**
   * Constant associated with the key code value for the F1 key (112).
   */
  "public static const",{ F1/*:uint*/ : 112},
  /**
   * Constant associated with the key code value for the F10 key (121).
   */
  "public static const",{ F10/*:uint*/ : 121},
  /**
   * Constant associated with the key code value for the F11 key (122).
   */
  "public static const",{ F11/*:uint*/ : 122},
  /**
   * Constant associated with the key code value for the F12 key (123).
   */
  "public static const",{ F12/*:uint*/ : 123},
  /**
   * Constant associated with the key code value for the F13 key (124).
   */
  "public static const",{ F13/*:uint*/ : 124},
  /**
   * Constant associated with the key code value for the F14 key (125).
   */
  "public static const",{ F14/*:uint*/ : 125},
  /**
   * Constant associated with the key code value for the F15 key (126).
   */
  "public static const",{ F15/*:uint*/ : 126},
  /**
   * Constant associated with the key code value for the F2 key (113).
   */
  "public static const",{ F2/*:uint*/ : 113},
  /**
   * Constant associated with the key code value for the F3 key (114).
   */
  "public static const",{ F3/*:uint*/ : 114},
  /**
   * Constant associated with the key code value for the F4 key (115).
   */
  "public static const",{ F4/*:uint*/ : 115},
  /**
   * Constant associated with the key code value for the F5 key (116).
   */
  "public static const",{ F5/*:uint*/ : 116},
  /**
   * Constant associated with the key code value for the F6 key (117).
   */
  "public static const",{ F6/*:uint*/ : 117},
  /**
   * Constant associated with the key code value for the F7 key (118).
   */
  "public static const",{ F7/*:uint*/ : 118},
  /**
   * Constant associated with the key code value for the F8 key (119).
   */
  "public static const",{ F8/*:uint*/ : 119},
  /**
   * Constant associated with the key code value for the F9 key (120).
   */
  "public static const",{ F9/*:uint*/ : 120},
  /**
   * Constant associated with the key code value for the G key (71).
   */
  "public static const",{ G/*:uint*/ : 71},
  /**
   * Constant associated with the key code value for the Home key (36).
   */
  "public static const",{ HOME/*:uint*/ : 36},
  /**
   * Constant associated with the key code value for the Insert key (45).
   */
  "public static const",{ INSERT/*:uint*/ : 45},
  /**
   * Constant associated with the key code value for the Left Arrow key (37).
   */
  "public static const",{ LEFT/*:uint*/ : 37},
  /**
   * Constant associated with the key code value for the number 0 key on the number pad (96).
   */
  "public static const",{ NUMPAD_0/*:uint*/ : 96},
  /**
   * Constant associated with the key code value for the number 1 key on the number pad (97).
   */
  "public static const",{ NUMPAD_1/*:uint*/ : 97},
  /**
   * Constant associated with the key code value for the number 2 key on the number pad (98).
   */
  "public static const",{ NUMPAD_2/*:uint*/ : 98},
  /**
   * Constant associated with the key code value for the number 3 key on the number pad (99).
   */
  "public static const",{ NUMPAD_3/*:uint*/ : 99},
  /**
   * Constant associated with the key code value for the number 4 key on the number pad (100).
   */
  "public static const",{ NUMPAD_4/*:uint*/ : 100},
  /**
   * Constant associated with the key code value for the number 5 key on the number pad (101).
   */
  "public static const",{ NUMPAD_5/*:uint*/ : 101},
  /**
   * Constant associated with the key code value for the number 6 key on the number pad (102).
   */
  "public static const",{ NUMPAD_6/*:uint*/ : 102},
  /**
   * Constant associated with the key code value for the number 7 key on the number pad (103).
   */
  "public static const",{ NUMPAD_7/*:uint*/ : 103},
  /**
   * Constant associated with the key code value for the number 8 key on the number pad (104).
   */
  "public static const",{ NUMPAD_8/*:uint*/ : 104},
  /**
   * Constant associated with the key code value for the number 9 key on the number pad (105).
   */
  "public static const",{ NUMPAD_9/*:uint*/ : 105},
  /**
   * Constant associated with the key code value for the addition key on the number pad (107).
   */
  "public static const",{ NUMPAD_ADD/*:uint*/ : 107},
  /**
   * Constant associated with the key code value for the decimal key on the number pad (110).
   */
  "public static const",{ NUMPAD_DECIMAL/*:uint*/ : 110},
  /**
   * Constant associated with the key code value for the division key on the number pad (111).
   */
  "public static const",{ NUMPAD_DIVIDE/*:uint*/ : 111},
  /**
   * Constant associated with the key code value for the Enter key on the number pad (108).
   */
  "public static const",{ NUMPAD_ENTER/*:uint*/ : 108},
  /**
   * Constant associated with the key code value for the multiplication key on the number pad (106).
   */
  "public static const",{ NUMPAD_MULTIPLY/*:uint*/ : 106},
  /**
   * Constant associated with the key code value for the subtraction key on the number pad (109).
   */
  "public static const",{ NUMPAD_SUBTRACT/*:uint*/ : 109},
  /**
   * Constant associated with the key code value for the Page Down key (34).
   */
  "public static const",{ PAGE_DOWN/*:uint*/ : 34},
  /**
   * Constant associated with the key code value for the Page Up key (33).
   */
  "public static const",{ PAGE_UP/*:uint*/ : 33},
  /**
   * Constant associated with the key code value for the Right Arrow key (39).
   */
  "public static const",{ RIGHT/*:uint*/ : 39},
  /**
   * Constant associated with the key code value for the Shift key (16).
   */
  "public static const",{ SHIFT/*:uint*/ : 16},
  /**
   * Constant associated with the key code value for the Spacebar (32).
   */
  "public static const",{ SPACE/*:uint*/ : 32},
  /**
   * Constant associated with the key code value for the Tab key (9).
   */
  "public static const",{ TAB/*:uint*/ : 9},
  /**
   * Constant associated with the key code value for the Up Arrow key (38).
   */
  "public static const",{ UP/*:uint*/ : 38},
];},["capsLock","numLock","isAccessible"],["Error"], "0.8.0", "0.8.3"
);