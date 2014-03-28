joo.classLoader.prepare("package flash.display",/* {*/


/**
 * The SpreadMethod class provides values for the <code>spreadMethod</code> parameter in the <code>beginGradientFill()</code> and <code>lineGradientStyle()</code> methods of the Graphics class.
 * <p>The following example shows the same gradient fill using various spread methods:</p>
 * <table>
 * <tr>
 * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/beginGradientFill_spread_pad.jpg" /> </td>
 * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/beginGradientFill_spread_reflect.jpg" /> </td>
 * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/beginGradientFill_spread_repeat.jpg" /> </td></tr>
 * <tr>
 * <td><code>SpreadMethod.PAD</code> </td>
 * <td><code>SpreadMethod.REFLECT</code> </td>
 * <td><code>SpreadMethod.REPEAT</code> </td></tr></table>
 * @see Graphics#beginGradientFill()
 * @see Graphics#lineGradientStyle()
 *
 */
"public final class SpreadMethod",1,function($$private){;return[ 
  /**
   * Specifies that the gradient use the <i>pad</i> spread method.
   */
  "public static const",{ PAD/*:String*/ : "pad"},
  /**
   * Specifies that the gradient use the <i>reflect</i> spread method.
   */
  "public static const",{ REFLECT/*:String*/ : "reflect"},
  /**
   * Specifies that the gradient use the <i>repeat</i> spread method.
   */
  "public static const",{ REPEAT/*:String*/ : "repeat"},
];},[],[], "0.8.0", "0.8.1"
);