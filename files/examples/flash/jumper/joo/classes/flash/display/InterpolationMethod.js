joo.classLoader.prepare("package flash.display",/* {*/


/**
 * The InterpolationMethod class provides values for the <code>interpolationMethod</code> parameter in the <code>Graphics.beginGradientFill()</code> and <code>Graphics.lineGradientStyle()</code> methods. This parameter determines the RGB space to use when rendering the gradient.
 * @see Graphics#beginGradientFill()
 * @see Graphics#lineGradientStyle()
 *
 */
"public final class InterpolationMethod",1,function($$private){;return[ 
  /**
   * Specifies that the linear RGB interpolation method should be used. This means that an RGB color space based on a linear RGB color model is used.
   * @see #RGB
   *
   */
  "public static const",{ LINEAR_RGB/*:String*/ : "linearRGB"},
  /**
   * Specifies that the RGB interpolation method should be used. This means that the gradient is rendered with exponential sRGB (standard RGB) space. The sRGB space is a W3C-endorsed standard that defines a non-linear conversion between red, green, and blue component values and the actual intensity of the visible component color.
   * <p>For example, consider a simple linear gradient between two colors (with the <code>spreadMethod</code> parameter set to <code>SpreadMethod.REFLECT</code>). The different interpolation methods affect the appearance as follows:</p>
   * <table>
   * <tr>
   * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/beginGradientFill_interp_linearrgb.jpg" /></td>
   * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/beginGradientFill_interp_rgb.jpg" /></td></tr>
   * <tr>
   * <td><code>InterpolationMethod.LINEAR_RGB</code></td>
   * <td><code>InterpolationMethod.RGB</code></td></tr></table>
   * @see #LINEAR_RGB
   *
   */
  "public static const",{ RGB/*:String*/ : "rgb"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);