joo.classLoader.prepare("package flash.display",/* {*/


/**
 * The BitmapDataChannel class is an enumeration of constant values that indicate which channel to use: red, blue, green, or alpha transparency.
 * <p>When you call some methods, you can use the bitwise OR operator (<code>|</code>) to combine BitmapDataChannel constants to indicate multiple color channels.</p>
 * <p>The BitmapDataChannel constants are provided for use as values in the following:</p>
 * <ul>
 * <li>The <code>sourceChannel</code> and <code>destChannel</code> parameters of the <code>flash.display.BitmapData.copyChannel()</code> method</li>
 * <li>The <code>channelOptions</code> parameter of the <code>flash.display.BitmapData.noise()</code> method</li>
 * <li>The <code>flash.filters.DisplacementMapFilter.componentX</code> and <code>flash.filters.DisplacementMapFilter.componentY</code> properties</li></ul>
 * @see BitmapData#copyChannel()
 * @see BitmapData#noise()
 * @see flash.filters.DisplacementMapFilter#componentX
 * @see flash.filters.DisplacementMapFilter#componentY
 *
 */
"public final class BitmapDataChannel",1,function($$private){;return[ 
  /**
   * The alpha channel.
   */
  "public static const",{ ALPHA/*:uint*/ : 8},
  /**
   * The blue channel.
   */
  "public static const",{ BLUE/*:uint*/ : 4},
  /**
   * The green channel.
   */
  "public static const",{ GREEN/*:uint*/ : 2},
  /**
   * The red channel.
   */
  "public static const",{ RED/*:uint*/ : 1},
];},[],[], "0.8.0", "0.8.3"
);