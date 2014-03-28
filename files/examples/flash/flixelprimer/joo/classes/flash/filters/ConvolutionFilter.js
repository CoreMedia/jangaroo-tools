joo.classLoader.prepare("package flash.filters",/* {*/


/**
 * The ConvolutionFilter class applies a matrix convolution filter effect. A convolution combines pixels in the input image with neighboring pixels to produce an image. A wide variety of image effects can be achieved through convolutions, including blurring, edge detection, sharpening, embossing, and beveling. You can apply the filter to any display object (that is, objects that inherit from the DisplayObject class), such as MovieClip, SimpleButton, TextField, and Video objects, as well as to BitmapData objects.
 * <p>To create a convolution filter, use the syntax <code>new ConvolutionFilter()</code>. The use of filters depends on the object to which you apply the filter:</p>
 * <ul>
 * <li>To apply filters to movie clips, text fields, buttons, and video, use the <code>filters</code> property (inherited from DisplayObject). Setting the <code>filters</code> property of an object does not modify the object, and you can remove the filter by clearing the <code>filters</code> property.</li>
 * <li>To apply filters to BitmapData objects, use the <code>BitmapData.applyFilter()</code> method. Calling <code>applyFilter()</code> on a BitmapData object takes the source BitmapData object and the filter object and generates a filtered image as a result.</li></ul>
 * <p>If you apply a filter to a display object, the value of the <code>cacheAsBitmap</code> property of the object is set to <code>true</code>. If you clear all filters, the original value of <code>cacheAsBitmap</code> is restored.</p>
 * <p>A filter is not applied if the resulting image exceeds the maximum dimensions. In AIR 1.5 and Flash Player 10, the maximum is 8,191 pixels in width or height, and the total number of pixels cannot exceed 16,777,215 pixels. (So, if an image is 8,191 pixels wide, it can only be 2,048 pixels high.) In Flash Player 9 and earlier and AIR 1.1 and earlier, the limitation is 2,880 pixels in height and 2,880 pixels in width. For example, if you zoom in on a large movie clip with a filter applied, the filter is turned off if the resulting image exceeds maximum dimensions.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/filters/ConvolutionFilter.html#includeExamplesSummary">View the examples</a></p>
 * @see flash.display.BitmapData#applyFilter()
 * @see flash.display.DisplayObject#filters
 * @see flash.display.DisplayObject#cacheAsBitmap
 * @see #matrix
 *
 */
"public class ConvolutionFilter extends flash.filters.BitmapFilter",2,function($$private){;return[ 
  /**
   * The alpha transparency value of the substitute color. Valid values are 0 to 1.0. The default is 0. For example, .25 sets a transparency value of 25%.
   */
  "public function get alpha",function alpha$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set alpha",function alpha$set(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The amount of bias to add to the result of the matrix transformation. The bias increases the color value of each channel, so that dark colors appear brighter. The default value is 0.
   */
  "public function get bias",function bias$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set bias",function bias$set(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Indicates whether the image should be clamped. For pixels off the source image, a value of <code>true</code> indicates that the input image is extended along each of its borders as necessary by duplicating the color values at each respective edge of the input image. A value of <code>false</code> indicates that another color should be used, as specified in the <code>color</code> and <code>alpha</code> properties. The default is <code>true</code>.
   * @example The following example creates two boxes using the <code>BitmapData</code> class, one of which is half the size of the other. When the example first loads, the larger box is drawn inside <code>mc</code> using the <code>attachBitmap()</code>. When <code>mc</code> is clicked and the <code>applyFilter()</code> method is called, the <code>largeBox</code> instance of <code>BitmapData</code> is redrawn with <code>smallBox</code> as a source bitmap. Since <code>applyFilter()</code> draws <code>smallBox</code> over a <code>Rectangle</code> whose width and height is specified as those of <code>largeBox</code>, the source bitmap is smaller than the drawing area. The <code>clamp</code> property of <code>ConvolutionFilter</code> in this case is set to <code>false</code> and the area which is not covered by the source bitmap, <code>smallBox</code>, is a solid red as determined by the <code>clampColor</code> and <code>clampAlpha</code> variables.
   * <listing>
   *  package {
   *     import flash.display.Sprite;
   *     import flash.display.BitmapData;
   *     import flash.filters.ConvolutionFilter;
   *     import flash.text.TextField;
   *     import flash.geom.Rectangle;
   *     import flash.geom.Point;
   *
   *     public class ConvolutionClampExample extends Sprite {
   *         // Variables that affect clamping:
   *         var clamp:Boolean = false;
   *         var clampColor:Number = 0xFF0000;
   *         var clampAlpha:Number = 1;
   *
   *         // For illustration, keep other ConvolutionFilter variables neutral:
   *         var bias:Number = 0;
   *         var preserveAlpha:Boolean = false;
   *         // Also, construct a neutral matrix
   *         var matrixCols:Number = 3;
   *         var matrixRows:Number = 3;
   *         var matrix:Array = [ 1,1,1,
   *                              1,1,1,
   *                              1,1,1 ];
   *
   *         var filter:ConvolutionFilter = new ConvolutionFilter(matrixCols, matrixRows, matrix, matrix.length, bias, preserveAlpha, clamp, clampColor, clampAlpha);
   *
   *         var largeBoxWidth:Number = 100;
   *         var largeBoxHeight:Number = 100;
   *         var largeBox:BitmapData = new BitmapData(largeBoxWidth, largeBoxWidth, true, 0xCC00FF00);
   *         var smallBoxWidth:Number = largeBoxWidth / 2;
   *         var smallBoxHeight:Number = largeBoxHeight / 2;
   *         var smallBox:BitmapData = new BitmapData(smallBoxWidth, smallBoxWidth, true, 0xCC0000FF);
   *
   *         var mc:MovieClip = this.createEmptyMovieClip("mc", this.getNextHighestDepth());
   *         mc.attachBitmap(largeBox, this.getNextHighestDepth());
   *
   *         mc.onPress = function() {
   *             largeBox.applyFilter(smallBox,
   *                                  new Rectangle(0,0, largeBoxWidth, largeBoxHeight),
   *                                  new Point(0,0),
   *                                  filter);
   *         }
   *     }
   * }
   * </listing>
   */
  "public function get clamp",function clamp$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set clamp",function clamp$set(value/*:Boolean*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The hexadecimal color to substitute for pixels that are off the source image. It is an RGB value with no alpha component. The default is 0.
   */
  "public function get color",function color$get()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set color",function color$set(value/*:uint*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The divisor used during matrix transformation. The default value is 1. A divisor that is the sum of all the matrix values smooths out the overall color intensity of the result. A value of 0 is ignored and the default is used instead.
   */
  "public function get divisor",function divisor$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set divisor",function divisor$set(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * An array of values used for matrix transformation. The number of items in the array must equal <code>matrixX * matrixY</code>.
   * <p>A matrix convolution is based on an <i>n</i> x <i>m</i> matrix, which describes how a given pixel value in the input image is combined with its neighboring pixel values to produce a resulting pixel value. Each result pixel is determined by applying the matrix to the corresponding source pixel and its neighboring pixels.</p>
   * <p>For a 3 x 3 matrix convolution, the following formula is used for each independent color channel:</p>
   * <pre><code>   dst (x, y) = ((src (x-1, y-1) * a0 + src(x, y-1) * a1....
   src(x, y+1) * a7 + src (x+1,y+1) * a8) / divisor) + bias
   </code></pre>
   * <p>Certain filter specifications perform faster when run by a processor that offers SSE (Streaming SIMD Extensions). The following are criteria for faster convolution operations:</p>
   * <ul>
   * <li>The filter must be a 3x3 filter.</li>
   * <li>All the filter terms must be integers between -127 and +127.</li>
   * <li>The sum of all the filter terms must not have an absolute value greater than 127.</li>
   * <li>If any filter term is negative, the divisor must be between 2.00001 and 256.</li>
   * <li>If all filter terms are positive, the divisor must be between 1.1 and 256.</li>
   * <li>The bias must be an integer.</li></ul>
   * <p><b>Note:</b> If you create a ConvolutionFilter instance using the constructor without parameters, the order you assign values to matrix properties affects the behavior of the filter. In the following case, the matrix array is assigned while the <code>matrixX</code> and <code>matrixY</code> properties are still set to <code>0</code> (the default value):</p>
   * <listing>
   *     public var myfilter:ConvolutionFilter = new ConvolutionFilter();
   *     myfilter.matrix = [0, 0, 0, 0, 1, 0, 0, 0, 0];
   *     myfilter.matrixX = 3;
   *     myfilter.matrixY = 3;
   *    </listing>
   * <p>In the following case, the matrix array is assigned while the <code>matrixX</code> and <code>matrixY</code> properties are set to <code>3</code>:</p>
   * <listing>
   *     public var myfilter:ConvolutionFilter = new ConvolutionFilter();
   *     myfilter.matrixX = 3;
   *     myfilter.matrixY = 3;
   *     myfilter.matrix = [0, 0, 0, 0, 1, 0, 0, 0, 0];
   *    </listing>
   * @throws TypeError The Array is null when being set
   *
   */
  "public function get matrix",function matrix$get()/*:Array*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set matrix",function matrix$set(value/*:Array*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The <i>x</i> dimension of the matrix (the number of columns in the matrix). The default value is 0.
   */
  "public function get matrixX",function matrixX$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set matrixX",function matrixX$set(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The <i>y</i> dimension of the matrix (the number of rows in the matrix). The default value is 0.
   */
  "public function get matrixY",function matrixY$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set matrixY",function matrixY$set(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Indicates if the alpha channel is preserved without the filter effect or if the convolution filter is applied to the alpha channel as well as the color channels. A value of <code>false</code> indicates that the convolution applies to all channels, including the alpha channel. A value of <code>true</code> indicates that the convolution applies only to the color channels. The default value is <code>true</code>.
   */
  "public function get preserveAlpha",function preserveAlpha$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set preserveAlpha",function preserveAlpha$set(value/*:Boolean*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Initializes a ConvolutionFilter instance with the specified parameters.
   * @param matrixX The <i>x</i> dimension of the matrix (the number of columns in the matrix). The default value is 0.
   * @param matrixY The <i>y</i> dimension of the matrix (the number of rows in the matrix). The default value is 0.
   * @param matrix The array of values used for matrix transformation. The number of items in the array must equal <code>matrixX * matrixY</code>.
   * @param divisor The divisor used during matrix transformation. The default value is 1. A divisor that is the sum of all the matrix values evens out the overall color intensity of the result. A value of 0 is ignored and the default is used instead.
   * @param bias The bias to add to the result of the matrix transformation. The default value is 0.
   * @param preserveAlpha A value of <code>false</code> indicates that the alpha value is not preserved and that the convolution applies to all channels, including the alpha channel. A value of <code>true</code> indicates that the convolution applies only to the color channels. The default value is <code>true</code>.
   * @param clamp For pixels that are off the source image, a value of <code>true</code> indicates that the input image is extended along each of its borders as necessary by duplicating the color values at the given edge of the input image. A value of <code>false</code> indicates that another color should be used, as specified in the <code>color</code> and <code>alpha</code> properties. The default is <code>true</code>.
   * @param color The hexadecimal color to substitute for pixels that are off the source image.
   * @param alpha The alpha of the substitute color.
   *
   */
  "public function ConvolutionFilter",function ConvolutionFilter$(matrixX/*:Number = 0*/, matrixY/*:Number = 0*/, matrix/*:Array = null*/, divisor/*:Number = 1.0*/, bias/*:Number = 0.0*/, preserveAlpha/*:Boolean = true*/, clamp/*:Boolean = true*/, color/*:uint = 0*/, alpha/*:Number = 0.0*/) {if(arguments.length<9){if(arguments.length<8){if(arguments.length<7){if(arguments.length<6){if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){matrixX = 0;}matrixY = 0;}matrix = null;}divisor = 1.0;}bias = 0.0;}preserveAlpha = true;}clamp = true;}color = 0;}alpha = 0.0;}this.super$2();
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns a copy of this filter object.
   * @return BitmapFilter A new ConvolutionFilter instance with all the same properties as the original ConvolutionMatrixFilter instance.
   *
   */
  "override public function clone",function clone()/*:BitmapFilter*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["flash.filters.BitmapFilter","Error"], "0.8.0", "0.8.3"
);