joo.classLoader.prepare("package flash.text",/* {*/


/**
 * The CSMSettings class contains properties for use with the <code>TextRenderer.setAdvancedAntiAliasingTable()</code> method to provide continuous stroke modulation (CSM). CSM is the continuous modulation of both stroke weight and edge sharpness.
 * @see TextRenderer#setAdvancedAntiAliasingTable()
 *
 */
"public final class CSMSettings",1,function($$private){;return[ 
  /**
   * The size, in pixels, for which the settings apply.
   * <p>The <code>advancedAntiAliasingTable</code> array passed to the <code>setAdvancedAntiAliasingTable()</code> method can contain multiple entries that specify CSM settings for different font sizes. Using this property, you can specify the font size to which the other settings apply.</p>
   * @see TextRenderer#setAdvancedAntiAliasingTable()
   *
   */
  "public var",{ fontSize/*:Number*/:NaN},
  /**
   * The inside cutoff value, above which densities are set to a maximum density value (such as 255).
   * @see TextRenderer#setAdvancedAntiAliasingTable()
   *
   */
  "public var",{ insideCutoff/*:Number*/:NaN},
  /**
   * The outside cutoff value, below which densities are set to zero.
   * @see TextRenderer#setAdvancedAntiAliasingTable()
   *
   */
  "public var",{ outsideCutoff/*:Number*/:NaN},

  /**
   * Creates a new CSMSettings object which stores stroke values for custom anti-aliasing settings.
   * @param fontSize The size, in pixels, for which the settings apply.
   * @param insideCutoff The inside cutoff value, above which densities are set to a maximum density value (such as 255).
   * @param outsideCutoff The outside cutoff value, below which densities are set to zero.
   *
   */
  "public function CSMSettings",function CSMSettings$(fontSize/*:Number*/, insideCutoff/*:Number*/, outsideCutoff/*:Number*/) {
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["Error"], "0.8.0", "0.8.1"
);