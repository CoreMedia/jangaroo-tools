joo.classLoader.prepare("package flash.text",/* {*/

/**
 * The TextLineMetrics class contains information about the text position and measurements of a <i>line of text</i> within a text field. All measurements are in pixels. Objects of this class are returned by the <code>flash.text.TextField.getLineMetrics()</code> method.
 * <p>For measurements related to the text field containing the line of text (for example, the "Text Field height" measurement in the diagram), see flash.text.TextField.</p>
 * <p>The following diagram indicates the points and measurements of a text field and the line of text the field contains:</p>
 * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/text-metrics.jpg" /></p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/text/TextLineMetrics.html#includeExamplesSummary">View the examples</a></p>
 * @see TextField
 *
 */
"public class TextLineMetrics",1,function($$private){;return[ 
  /**
   * The ascent value of the text is the length from the baseline to the top of the line height in pixels. See the "Ascent" measurement in the overview diagram for this class.
   * @see TextLineMetrics
   *
   */
  "public var",{ ascent/*:Number*/:NaN},
  /**
   * The descent value of the text is the length from the baseline to the bottom depth of the line in pixels. See the "Descent" measurement in the overview diagram for this class.
   * @see TextLineMetrics
   *
   */
  "public var",{ descent/*:Number*/:NaN},
  /**
   * The height value of the text of the selected lines (not necessarily the complete text) in pixels. The height of the text line does not include the gutter height. See the "Line height" measurement in the overview diagram for this class.
   * @see TextLineMetrics
   *
   */
  "public var",{ height/*:Number*/:NaN},
  /**
   * The leading value is the measurement of the vertical distance between the lines of text. See the "Leading" measurement in the overview diagram for this class.
   * @see TextLineMetrics
   *
   */
  "public var",{ leading/*:Number*/:NaN},
  /**
   * The width value is the width of the text of the selected lines (not necessarily the complete text) in pixels. The width of the text line is not the same as the width of the text field. The width of the text line is relative to the text field width, minus the gutter width of 4 pixels (2 pixels on each side). See the "Text Line width" measurement in the overview diagram for this class.
   * @see TextLineMetrics
   *
   */
  "public var",{ width/*:Number*/:NaN},
  /**
   * The x value is the left position of the first character in pixels. This value includes the margin, indent (if any), and gutter widths. See the "Text Line x-position" in the overview diagram for this class.
   * @see TextLineMetrics
   *
   */
  "public var",{ x/*:Number*/:NaN},

  /**
   * Creates a TextLineMetrics object. The TextLineMetrics object contains information about the text metrics of a line of text in a text field. Objects of this class are returned by the <code>flash.text.TextField.getLineMetrics()</code> method.
   * <p>See the diagram in the overview for this class for the properties in context.</p>
   * @param x The left position of the first character in pixels.
   * @param width The width of the text of the selected lines (not necessarily the complete text) in pixels.
   * @param height The height of the text of the selected lines (not necessarily the complete text) in pixels.
   * @param ascent The length from the baseline to the top of the line height in pixels.
   * @param descent The length from the baseline to the bottom depth of the line in pixels.
   * @param leading The measurement of the vertical distance between the lines of text.
   *
   * @see TextLineMetrics
   * @see TextField#getLineMetrics()
   *
   */
  "public function TextLineMetrics",function TextLineMetrics$(x/*:Number*/, width/*:Number*/, height/*:Number*/, ascent/*:Number*/, descent/*:Number*/, leading/*:Number*/) {
    this.x = x;
    this.width = width;
    this.height = height;
    this.ascent = ascent;
    this.descent = descent;
    this.leading = leading;
  },
];},[],[], "0.8.0", "0.8.3"
);