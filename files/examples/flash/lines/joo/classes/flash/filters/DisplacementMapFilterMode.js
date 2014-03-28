joo.classLoader.prepare("package flash.filters",/* {*/


/**
 * The DisplacementMapFilterMode class provides values for the <code>mode</code> property of the DisplacementMapFilter class.
 */
"public final class DisplacementMapFilterMode",1,function($$private){;return[ 
  /**
   * Clamps the displacement value to the edge of the source image. Use with the <code>DisplacementMapFilter.mode</code> property.
   * @see DisplacementMapFilter#mode
   *
   */
  "public static const",{ CLAMP/*:String*/ : "clamp"},
  /**
   * If the displacement value is outside the image, substitutes the values in the <code>color</code> and <code>alpha</code> properties. Use with the <code>DisplacementMapFilter.mode</code> property.
   * @see DisplacementMapFilter#mode
   *
   */
  "public static const",{ COLOR/*:String*/ : "color"},
  /**
   * If the displacement value is out of range, ignores the displacement and uses the source pixel. Use with the <code>DisplacementMapFilter.mode</code> property.
   * @see DisplacementMapFilter#mode
   *
   */
  "public static const",{ IGNORE/*:String*/ : "ignore"},
  /**
   * Wraps the displacement value to the other side of the source image. Use with the <code>DisplacementMapFilter.mode</code> property.
   * @see DisplacementMapFilter#mode
   *
   */
  "public static const",{ WRAP/*:String*/ : "wrap"},
];},[],[], "0.8.0", "0.8.1"
);