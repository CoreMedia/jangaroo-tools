joo.classLoader.prepare("package flash.text",/* {*/


/**
 * The TextRenderer class provides functionality for the advanced anti-aliasing capability of embedded fonts. Advanced anti-aliasing allows font faces to render at very high quality at small sizes. Use advanced anti-aliasing with applications that have a lot of small text. Adobe does not recommend using advanced anti-aliasing for very large fonts (larger than 48 points). Advanced anti-aliasing is available in Flash Player 8 and later only.
 * <p>To set advanced anti-aliasing on a text field, set the <code>antiAliasType</code> property of the TextField instance.</p>
 * <p>Advanced anti-aliasing provides continuous stroke modulation (CSM), which is continuous modulation of both stroke weight and edge sharpness. As an advanced feature, you can use the <code>setAdvancedAntiAliasingTable()</code> method to define settings for specific typefaces and font sizes.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/text/TextRenderer.html#includeExamplesSummary">View the examples</a></p>
 * @see TextField#antiAliasType
 *
 */
"public final class TextRenderer",1,function($$private){;return[ 
  /**
   * Controls the rendering of advanced anti-aliased text. The visual quality of text is very subjective, and while Flash Player tries to use the best settings for various conditions, designers may choose a different look or feel for their text. Also, using <code>displayMode</code> allows a designer to override Flash Player's subpixel choice and create visual consistency independent of the user's hardware. Use the values in the TextDisplayMode class to set this property.
   * <p>The default value is <code>"default".</code></p>
   * @see TextDisplayMode
   *
   */
  "public static function get displayMode",function displayMode$get()/*:String*/ {
    return $$private._displayMode;
  },

  /**
   * @private
   */
  "public static function set displayMode",function displayMode$set(value/*:String*/)/*:void*/ {
    $$private._displayMode = value;
  },

  /**
   * The adaptively sampled distance fields (ADFs) quality level for advanced anti-aliasing. The only acceptable values are 3, 4, and 7.
   * <p>Advanced anti-aliasing uses ADFs to represent the outlines that determine a glyph. The higher the quality, the more cache space is required for ADF structures. A value of <code>3</code> takes the least amount of memory and provides the lowest quality. Larger fonts require more cache space; at a font size of 64 pixels, the quality level increases from <code>3</code> to <code>4</code> or from <code>4</code> to <code>7</code> unless, the level is already set to <code>7</code>.</p>
   * <p>The default value is <code>4.</code></p>
   */
  "public static function get maxLevel",function maxLevel$get()/*:int*/ {
    return $$private._maxLevel;
  },

  /**
   * @private
   */
  "public static function set maxLevel",function maxLevel$set(value/*:int*/)/*:void*/ {
    $$private._maxLevel = value;
  },

  /**
   * Sets a custom continuous stroke modulation (CSM) lookup table for a font. Flash Player attempts to detect the best CSM for your font. If you are not satisfied with the CSM that the Flash Player provides, you can customize your own CSM by using the <code>setAdvancedAntiAliasingTable()</code> method.
   * @param fontName The name of the font for which you are applying settings.
   * @param fontStyle The font style indicated by using one of the values from the flash.text.FontStyle class.
   * @param colorType This value determines whether the stroke is dark or whether it is light. Use one of the values from the flash.text.TextColorType class.
   * @param advancedAntiAliasingTable An array of one or more CSMSettings objects for the specified font. Each object contains the following properties:
   * <ul>
   * <li><code>fontSize</code></li>
   * <li><code>insideCutOff</code></li>
   * <li><code>outsideCutOff</code></li></ul>
   * <p>The <code>advancedAntiAliasingTable</code> array can contain multiple entries that specify CSM settings for different font sizes.</p>
   * <p>The <code>fontSize</code> is the size, in pixels, for which the settings apply.</p>
   * <p>Advanced anti-aliasing uses adaptively sampled distance fields (ADFs) to represent the outlines that determine a glyph. Flash Player uses an outside cutoff value (<code>outsideCutOff</code>), below which densities are set to zero, and an inside cutoff value (<code>insideCutOff</code>), above which densities are set to a maximum density value (such as 255). Between these two cutoff values, the mapping function is a linear curve ranging from zero at the outside cutoff to the maximum density at the inside cutoff.</p>
   * <p>Adjusting the outside and inside cutoff values affects stroke weight and edge sharpness. The spacing between these two parameters is comparable to twice the filter radius of classic anti-aliasing methods; a narrow spacing provides a sharper edge, while a wider spacing provides a softer, more filtered edge. When the spacing is zero, the resulting density image is a bi-level bitmap. When the spacing is very wide, the resulting density image has a watercolor-like edge.</p>
   * <p>Typically, users prefer sharp, high-contrast edges at small point sizes, and softer edges for animated text and larger point sizes.</p>
   * <p>The outside cutoff typically has a negative value, and the inside cutoff typically has a positive value, and their midpoint typically lies near zero. Adjusting these parameters to shift the midpoint toward negative infinity increases the stroke weight; shifting the midpoint toward positive infinity decreases the stroke weight. Make sure that the outside cutoff value is always less than or equal to the inside cutoff value.</p>
   *
   * @see FontStyle
   * @see TextColorType
   * @see CSMSettings
   *
   */
  "public static function setAdvancedAntiAliasingTable",function setAdvancedAntiAliasingTable(fontName/*:String*/, fontStyle/*:String*/, colorType/*:String*/, advancedAntiAliasingTable/*:Array*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  "private static var",{ _displayMode/*:String*/:null},
  "private static var",{ _maxLevel/*:int*/:0},
];},["displayMode","maxLevel","setAdvancedAntiAliasingTable"],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);