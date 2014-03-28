joo.classLoader.prepare("package flash.ui",/* {*/


/**
 * The KeyLocation class contains constants that indicate the location of a key pressed on the keyboard or keyboard-like input device.
 * <p>The KeyLocation constants are used in the <code>KeyboardEvent.keyLocation</code> property.</p>
 * @see flash.events.KeyboardEvent#keyLocation
 *
 */
"public final class KeyLocation",1,function($$private){;return[ 
  /**
   * Indicates the key activated is in the left key location (there is more than one possible location for this key). Example: The left Shift key on a PC 101 Key US keyboard.
   */
  "public static const",{ LEFT/*:uint*/ : 1},
  /**
   * Indicates the key activation originated on the numeric keypad or with a virtual key corresponding to the numeric keypad. Example: The 1 key on a PC 101 Key US keyboard located on the numeric pad.
   */
  "public static const",{ NUM_PAD/*:uint*/ : 3},
  /**
   * Indicates the key activated is in the right key location (there is more than one possible location for this key). Example: The right Shift key on a PC 101 Key US keyboard.
   */
  "public static const",{ RIGHT/*:uint*/ : 2},
  /**
   * Indicates the key activation is not distinguished as the left or right version of the key, and did not originate on the numeric keypad (or did not originate with a virtual key corresponding to the numeric keypad). Example: The Q key on a PC 101 Key US keyboard.
   */
  "public static const",{ STANDARD/*:uint*/ : 0},
];},[],[], "0.8.0", "0.8.1"
);