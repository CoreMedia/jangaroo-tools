joo.classLoader.prepare("package flash.filters",/* {*/



/**
 * The GradientBevelFilter class lets you apply a gradient bevel effect to display objects. A gradient bevel is a beveled edge, enhanced with gradient color, on the outside, inside, or top of an object. Beveled edges make objects look three-dimensional. You can apply the filter to any display object (that is, objects that inherit from the DisplayObject class), such as MovieClip, SimpleButton, TextField, and Video objects, as well as to BitmapData objects.
 * <p>The use of filters depends on the object to which you apply the filter:</p>
 * <ul>
 * <li>To apply filters to display objects, use the <code>filters</code> property. Setting the <code>filters</code> property of an object does not modify the object, and you can remove the filter by clearing the <code>filters</code> property.</li>
 * <li>To apply filters to BitmapData objects, use the <code>BitmapData.applyFilter()</code> method. Calling <code>applyFilter()</code> on a BitmapData object takes the source BitmapData object and the filter object and generates a filtered image as a result.</li></ul>
 * <p>If you apply a filter to a display object, the <code>cacheAsBitmap</code> property of the display object is set to <code>true</code>. If you clear all filters, the original value of <code>cacheAsBitmap</code> is restored.</p>
 * <p>This filter supports Stage scaling. However, it does not support general scaling, rotation, and skewing; if the object itself is scaled (if <code>scaleX</code> and <code>scaleY</code> are set to a value other than 1.0), the filter effect is not scaled. It is scaled only when the user zooms in on the Stage.</p>
 * <p>A filter is not applied if the resulting image exceeds the maximum dimensions. In AIR 1.5 and Flash Player 10, the maximum is 8,191 pixels in width or height, and the total number of pixels cannot exceed 16,777,215 pixels. (So, if an image is 8,191 pixels wide, it can only be 2,048 pixels high.) In Flash Player 9 and earlier and AIR 1.1 and earlier, the limitation is 2,880 pixels in height and 2,880 pixels in width. For example, if you zoom in on a large movie clip with a filter applied, the filter is turned off if the resulting image exceeds the maximum dimensions.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/filters/GradientBevelFilter.html#includeExamplesSummary">View the examples</a></p>
 * @see #ratios
 * @see flash.display.BitmapData#applyFilter()
 * @see BevelFilter
 * @see flash.display.DisplayObject#filters
 * @see flash.display.DisplayObject#cacheAsBitmap
 *
 */
"public final class GradientBevelFilter extends flash.filters.BitmapFilter",2,function($$private){;return[ 
  /**
   * An array of alpha transparency values for the corresponding colors in the <code>colors</code> array. Valid values for each element in the array are 0 to 1. For example, .25 sets a transparency value of 25%.
   * <p>The <code>alphas</code> property cannot be changed by directly modifying its values. Instead, you must get a reference to <code>alphas</code>, make the change to the reference, and then set <code>alphas</code> to the reference.</p>
   * <p>The <code>colors</code>, <code>alphas</code>, and <code>ratios</code> properties are related. The first element in the <code>colors</code> array corresponds to the first element in the <code>alphas</code> array and in the <code>ratios</code> array, and so on.</p>
   * @throws TypeError The Array is null when being set
   *
   * @see #colors
   * @see #ratios
   *
   */
  "public function get alphas",function alphas$get()/*:Array*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set alphas",function alphas$set(value/*:Array*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The angle, in degrees. Valid values are 0 to 360. The default is 45.
   * <p>The angle value represents the angle of the theoretical light source falling on the object. The value determines the angle at which the gradient colors are applied to the object: where the highlight and the shadow appear, or where the first color in the array appears. The colors are then applied in the order in which they appear in the array.</p>
   * @see #ratios
   *
   */
  "public function get angle",function angle$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set angle",function angle$set(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The amount of horizontal blur. Valid values are 0 to 255. A blur of 1 or less means that the original image is copied as is. The default value is 4. Values that are a power of 2 (such as 2, 4, 8, 16 and 32) are optimized to render more quickly than other values.
   */
  "public function get blurX",function blurX$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set blurX",function blurX$set(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The amount of vertical blur. Valid values are 0 to 255. A blur of 1 or less means that the original image is copied as is. The default value is 4. Values that are a power of 2 (such as 2, 4, 8, 16 and 32) are optimized to render more quickly than other values.
   */
  "public function get blurY",function blurY$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set blurY",function blurY$set(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * An array of RGB hexadecimal color values to use in the gradient. For example, red is 0xFF0000, blue is 0x0000FF, and so on.
   * <p>The <code>colors</code> property cannot be changed by directly modifying its values. Instead, you must get a reference to <code>colors</code>, make the change to the reference, and then set <code>colors</code> to the reference.</p>
   * <p>The <code>colors</code>, <code>alphas</code>, and <code>ratios</code> properties are related. The first element in the <code>colors</code> array corresponds to the first element in the <code>alphas</code> array and in the <code>ratios</code> array, and so on.</p>
   * @throws TypeError The Array is null when being set
   *
   * @see #alphas
   * @see #ratios
   *
   */
  "public function get colors",function colors$get()/*:Array*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set colors",function colors$set(value/*:Array*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The offset distance. Valid values are 0 to 8. The default value is 4.0.
   */
  "public function get distance",function distance$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set distance",function distance$set(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies whether the object has a knockout effect. A knockout effect makes the object's fill transparent and reveals the background color of the document. The value <code>true</code> specifies a knockout effect; the default is <code>false</code> (no knockout effect).
   */
  "public function get knockout",function knockout$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set knockout",function knockout$set(value/*:Boolean*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The number of times to apply the filter. The default value is <code>BitmapFilterQuality.LOW</code>, which is equivalent to applying the filter once. The value <code>BitmapFilterQuality.MEDIUM</code> applies the filter twice; the value <code>BitmapFilterQuality.HIGH</code> applies it three times. Filters with lower values are rendered more quickly.
   * <p>For most applications, a <code>quality</code> value of low, medium, or high is sufficient. Although you can use additional numeric values up to 15 to achieve different effects, higher values are rendered more slowly. Instead of increasing the value of <code>quality</code>, you can often get a similar effect, and with faster rendering, by simply increasing the values of the <code>blurX</code> and <code>blurY</code> properties.</p>
   * @see BitmapFilterQuality
   * @see #ratios
   *
   */
  "public function get quality",function quality$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set quality",function quality$set(value/*:int*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * An array of color distribution ratios for the corresponding colors in the <code>colors</code> array. Valid values for each element in the array are 0 to 255.
   * <p>The <code>ratios</code> property cannot be changed by directly modifying its values. Instead, you must get a reference to <code>ratios</code>, make the change to the reference, and then set <code>ratios</code> to the reference.</p>
   * <p>The <code>colors</code>, <code>alphas</code>, and <code>ratios</code> properties are related. The first element in the <code>colors</code> array corresponds to the first element in the <code>alphas</code> array and in the <code>ratios</code> array, and so on.</p>
   * <p>To understand how the colors in a gradient bevel are distributed, think first of the colors that you want in your gradient bevel. Consider that a simple bevel has a highlight color and shadow color; a gradient bevel has a highlight gradient and a shadow gradient. Assume that the highlight appears on the top-left corner, and the shadow appears on the bottom-right corner. Assume that one possible usage of the filter has four colors in the highlight and four in the shadow. In addition to the highlight and shadow, the filter uses a base fill color that appears where the edges of the highlight and shadow meet. Therefore the total number of colors is nine, and the corresponding number of elements in the ratios array is nine.</p>
   * <p>If you think of a gradient as composed of stripes of various colors, blending into each other, each ratio value sets the position of the color on the radius of the gradient, where 0 represents the outermost point of the gradient and 255 represents the innermost point of the gradient. For a typical usage, the middle value is 128, and that is the base fill value. To get the bevel effect shown in the image below, assign the ratio values as follows, using the example of nine colors:</p>
   * <ul>
   * <li>The first four colors range from 0-127, increasing in value so that each value is greater than or equal to the previous one. This is the highlight bevel edge.</li>
   * <li>The fifth color (the middle color) is the base fill, set to 128. The pixel value of 128 sets the base fill, which appears either outside the shape (and around the bevel edges) if the type is set to outer; or inside the shape, effectively covering the object's own fill, if the type is set to inner.</li>
   * <li>The last four colors range from 129-255, increasing in value so that each value is greater than or equal to the previous one. This is the shadow bevel edge.</li></ul>
   * <p>If you want an equal distribution of colors for each edge, use an odd number of colors, where the middle color is the base fill. Distribute the values between 0-127 and 129-255 equally among your colors, then adjust the value to change the width of each stripe of color in the gradient. For a gradient bevel with nine colors, a possible array is [16, 32, 64, 96, 128, 160, 192, 224, 235]. The following image depicts the gradient bevel as described:</p>
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/gradientBevelRainbow.jpg" /></p>
   * <p>Keep in mind that the spread of the colors in the gradient varies based on the values of the <code>blurX</code>, <code>blurY</code>, <code>strength</code>, and <code>quality</code> properties, as well as the <code>ratios</code> values.</p>
   * @throws TypeError The Array is null when being set
   *
   * @see #alphas
   * @see #colors
   * @see flash.display.Graphics#beginGradientFill()
   *
   */
  "public function get ratios",function ratios$get()/*:Array*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set ratios",function ratios$set(value/*:Array*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The strength of the imprint or spread. The higher the value, the more color is imprinted and the stronger the contrast between the bevel and the background. Valid values are 0 to 255. A value of 0 means that the filter is not applied. The default value is 1.
   * @see #ratios
   *
   */
  "public function get strength",function strength$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set strength",function strength$set(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The placement of the bevel effect. Possible values are BitmapFilterType constants:
   * <ul>
   * <li><code>BitmapFilterType.OUTER</code> — Bevel on the outer edge of the object</li>
   * <li><code>BitmapFilterType.INNER</code> — Bevel on the inner edge of the object</li>
   * <li><code>BitmapFilterType.FULL</code> — Bevel on top of the object</li></ul>
   */
  "public function get type",function type$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set type",function type$set(value/*:String*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Initializes the filter with the specified parameters.
   * @param distance The offset distance. Valid values are 0 to 8.
   * @param angle The angle, in degrees. Valid values are 0 to 360.
   * @param colors An array of RGB hexadecimal color values to use in the gradient. For example, red is 0xFF0000, blue is 0x0000FF, and so on.
   * @param alphas An array of alpha transparency values for the corresponding colors in the <code>colors</code> array. Valid values for each element in the array are 0 to 1. For example, .25 sets a transparency value of 25%.
   * @param ratios An array of color distribution ratios; valid values are 0 to 255.
   * @param blurX The amount of horizontal blur. Valid values are 0 to 255. A blur of 1 or less means that the original image is copied as is. The default value is 4. Values that are a power of 2 (such as 2, 4, 8, 16 and 32) are optimized to render more quickly than other values.
   * @param blurY The amount of vertical blur. Valid values are 0 to 255. A blur of 1 or less means that the original image is copied as is. Values that are a power of 2 (such as 2, 4, 8, 16 and 32) are optimized to render more quickly than other values.
   * @param strength The strength of the imprint or spread. The higher the value, the more color is imprinted and the stronger the contrast between the bevel and the background. Valid values are 0 to 255. A value of 0 means that the filter is not applied.
   * @param quality The quality of the filter. Use BitmapFilterQuality constants:
   * <ul>
   * <li><code>BitmapFilterQuality.LOW</code></li>
   * <li><code>BitmapFilterQuality.MEDIUM</code></li>
   * <li><code>BitmapFilterQuality.HIGH</code></li></ul>
   * <p>For more information, see the description of the <code>quality</code> property.</p>
   * @param type The placement of the bevel effect. Possible values are BitmapFilterType constants:
   * <ul>
   * <li><code>BitmapFilterType.OUTER</code> — Bevel on the outer edge of the object</li>
   * <li><code>BitmapFilterType.INNER</code> — Bevel on the inner edge of the object</li>
   * <li><code>BitmapFilterType.FULL</code> — Bevel on top of the object</li></ul>
   * @param knockout Specifies whether a knockout effect is applied. The value <code>true</code> makes the object's fill transparent and reveals the background color of the document.
   *
   * @see #quality
   * @see #ratios
   *
   */
  "public function GradientBevelFilter",function GradientBevelFilter$(distance/*:Number = 4.0*/, angle/*:Number = 45*/, colors/*:Array = null*/, alphas/*:Array = null*/, ratios/*:Array = null*/, blurX/*:Number = 4.0*/, blurY/*:Number = 4.0*/, strength/*:Number = 1*/, quality/*:int = 1*/, type/*:String = "inner"*/, knockout/*:Boolean = false*/) {if(arguments.length<11){if(arguments.length<10){if(arguments.length<9){if(arguments.length<8){if(arguments.length<7){if(arguments.length<6){if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){distance = 4.0;}angle = 45;}colors = null;}alphas = null;}ratios = null;}blurX = 4.0;}blurY = 4.0;}strength = 1;}quality = 1;}type = "inner";}knockout = false;}this.super$2();
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns a copy of this filter object.
   * @return A new GradientBevelFilter instance with all the same properties as the original GradientBevelFilter instance.
   *
   */
  "override public function clone",function clone()/*:BitmapFilter*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["flash.filters.BitmapFilter","Error"], "0.8.0", "0.8.1"
);