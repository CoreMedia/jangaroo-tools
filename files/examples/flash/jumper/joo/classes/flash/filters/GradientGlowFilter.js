joo.classLoader.prepare("package flash.filters",/* {*/



/**
 * The GradientGlowFilter class lets you apply a gradient glow effect to display objects. A gradient glow is a realistic-looking glow with a color gradient that you can control. You can apply a gradient glow around the inner or outer edge of an object or on top of an object. You can apply the filter to any display object (objects that inherit from the DisplayObject class), such as MovieClip, SimpleButton, TextField, and Video objects, as well as to BitmapData objects.
 * <p>The use of filters depends on the object to which you apply the filter:</p>
 * <ul>
 * <li>To apply filters to display objects, use the <code>filters</code> property. Setting the <code>filters</code> property of an object does not modify the object, and you can remove the filter by clearing the <code>filters</code> property.</li>
 * <li>To apply filters to BitmapData objects, use the <code>BitmapData.applyFilter()</code> method. Calling <code>applyFilter()</code> on a BitmapData object takes the source BitmapData object and the filter object and generates a filtered image as a result.</li></ul>
 * <p>If you apply a filter to a display object, the <code>cacheAsBitmap</code> property of the display object is set to <code>true</code>. If you clear all filters, the original value of <code>cacheAsBitmap</code> is restored.</p>
 * <p>This filter supports Stage scaling. However, it does not support general scaling, rotation, and skewing; if the object itself is scaled (if <code>scaleX</code> and <code>scaleY</code> are set to a value other than 1.0), the filter effect is not scaled. It is scaled only when the user zooms in on the Stage.</p>
 * <p>A filter is not applied if the resulting image exceeds the maximum dimensions. In AIR 1.5 and Flash Player 10, the maximum is 8,191 pixels in width or height, and the total number of pixels cannot exceed 16,777,215 pixels. (So, if an image is 8,191 pixels wide, it can only be 2,048 pixels high.) In Flash Player 9 and earlier and AIR 1.1 and earlier, the limitation is 2,880 pixels in height and 2,880 pixels in width. For example, if you zoom in on a large movie clip with a filter applied, the filter is turned off if the resulting image exceeds the maximum dimensions.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/filters/GradientGlowFilter.html#includeExamplesSummary">View the examples</a></p>
 * @see #ratios
 * @see flash.display.BitmapData#applyFilter()
 * @see flash.display.DisplayObject#cacheAsBitmap
 * @see flash.display.DisplayObject#filters
 * @see GlowFilter
 *
 */
"public final class GradientGlowFilter extends flash.filters.BitmapFilter",2,function($$private){;return[ 
  /**
   * An array of alpha transparency values for the corresponding colors in the <code>colors</code> array. Valid values for each element in the array are 0 to 1. For example, .25 sets the alpha transparency value to 25%.
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
   * <p>The angle value represents the angle of the theoretical light source falling on the object and determines the placement of the effect relative to the object. If <code>distance</code> is set to 0, the effect is not offset from the object, and therefore the <code>angle</code> property has no effect.</p>
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
   * The amount of horizontal blur. Valid values are 0 to 255. A blur of 1 or less means that the original image is copied as is. The default value is 4. Values that are a power of 2 (such as 2, 4, 8, 16, and 32) are optimized to render more quickly than other values.
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
   * The amount of vertical blur. Valid values are 0 to 255. A blur of 1 or less means that the original image is copied as is. The default value is 4. Values that are a power of 2 (such as 2, 4, 8, 16, and 32) are optimized to render more quickly than other values.
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
   * An array of colors that defines a gradient. For example, red is 0xFF0000, blue is 0x0000FF, and so on.
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
   * The offset distance of the glow. The default value is 4.
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
   * Specifies whether the object has a knockout effect. A knockout effect makes the object's fill transparent and reveals the background color of the document. The value <code>true</code> specifies a knockout effect; the default value is <code>false</code> (no knockout effect).
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
   * An array of color distribution ratios for the corresponding colors in the <code>colors</code> array. Valid values are 0 to 255.
   * <p>The <code>ratios</code> property cannot be changed by directly modifying its values. Instead, you must get a reference to <code>ratios</code>, make the change to the reference, and then set <code>ratios</code> to the reference.</p>
   * <p>The <code>colors</code>, <code>alphas</code>, and <code>ratios</code> properties are related. The first element in the <code>colors</code> array corresponds to the first element in the <code>alphas</code> array and in the <code>ratios</code> array, and so on.</p>
   * <p>Think of the gradient glow filter as a glow that emanates from the center of the object (if the <code>distance</code> value is set to 0), with gradients that are stripes of color blending into each other. The first color in the <code>colors</code> array is the outermost color of the glow. The last color is the innermost color of the glow.</p>
   * <p>Each value in the <code>ratios</code> array sets the position of the color on the radius of the gradient, where 0 represents the outermost point of the gradient and 255 represents the innermost point of the gradient. The ratio values can range from 0 to 255 pixels, in increasing value; for example [0, 64, 128, 200, 255]. Values from 0 to 128 appear on the outer edges of the glow. Values from 129 to 255 appear in the inner area of the glow. Depending on the ratio values of the colors and the <code>type</code> value of the filter, the filter colors might be obscured by the object to which the filter is applied.</p>
   * <p>In the following code and image, a filter is applied to a black circle movie clip, with the type set to <code>"full"</code>. For instructional purposes, the first color in the <code>colors</code> array, pink, has an <code>alpha</code> value of 1, so it shows against the white document background. (In practice, you probably would not want the first color showing in this way.) The last color in the array, yellow, obscures the black circle to which the filter is applied:</p>
   * <pre>    var colors:Array = [0xFFCCFF, 0x0000FF, 0x9900FF, 0xFF0000, 0xFFFF00];
   var alphas:Array = [1, 1, 1, 1, 1];
   var ratios:Array = [0, 32, 64, 128, 225];
   var myGGF:GradientGlowFilter = new GradientGlowFilter(0, 0, colors, alphas, ratios, 50, 50, 1, 2, "full", false);
   </pre>
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/gradientGlowDiagram.jpg" /></p>
   * <p>To achieve a seamless effect with your document background when you set the <code>type</code> value to <code>"outer"</code> or <code>"full"</code>, set the first color in the array to the same color as the document background, or set the alpha value of the first color to 0; either technique makes the filter blend in with the background.</p>
   * <p>If you make two small changes in the code, the effect of the glow can be very different, even with the same <code>ratios</code> and <code>colors</code> arrays. Set the alpha value of the first color in the array to 0, to make the filter blend in with the document's white background; and set the <code>type</code> property to <code>"outer"</code> or <code>"inner"</code>. Observe the results, as shown in the following images.</p>
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/gradientGlowOuter.jpg" /> <img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/gradientGlowInner.jpg" /></p>
   * <p>Keep in mind that the spread of the colors in the gradient varies based on the values of the <code>blurX</code>, <code>blurY</code>, <code>strength</code>, and <code>quality</code> properties, as well as the <code>ratios</code> values.</p>
   * @throws TypeError The Array is null when being set
   *
   * @see #colors
   * @see #alphas
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
   * The strength of the imprint or spread. The higher the value, the more color is imprinted and the stronger the contrast between the glow and the background. Valid values are 0 to 255. A value of 0 means that the filter is not applied. The default value is 1.
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
   * The placement of the filter effect. Possible values are flash.filters.BitmapFilterType constants:
   * <ul>
   * <li><code>BitmapFilterType.OUTER</code> — Glow on the outer edge of the object</li>
   * <li><code>BitmapFilterType.INNER</code> — Glow on the inner edge of the object; the default.</li>
   * <li><code>BitmapFilterType.FULL</code> — Glow on top of the object</li></ul>
   * @throws TypeError The string is null when being set
   *
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
   * @param distance The offset distance of the glow.
   * @param angle The angle, in degrees. Valid values are 0 to 360.
   * @param colors An array of colors that defines a gradient. For example, red is 0xFF0000, blue is 0x0000FF, and so on.
   * @param alphas An array of alpha transparency values for the corresponding colors in the <code>colors</code> array. Valid values for each element in the array are 0 to 1. For example, a value of .25 sets the alpha transparency value to 25%.
   * @param ratios An array of color distribution ratios. Valid values are 0 to 255. This value defines the percentage of the width where the color is sampled at 100 percent.
   * @param blurX The amount of horizontal blur. Valid values are 0 to 255. A blur of 1 or less means that the original image is copied as is. Values that are a power of 2 (such as 2, 4, 8, 16 and 32) are optimized to render more quickly than other values.
   * @param blurY The amount of vertical blur. Valid values are 0 to 255. A blur of 1 or less means that the original image is copied as is. Values that are a power of 2 (such as 2, 4, 8, 16 and 32) are optimized to render more quickly than other values.
   * @param strength The strength of the imprint or spread. The higher the value, the more color is imprinted and the stronger the contrast between the glow and the background. Valid values are 0 to 255. The larger the value, the stronger the imprint. A value of 0 means the filter is not applied.
   * @param quality The number of times to apply the filter. Use the BitmapFilterQuality constants:
   * <ul>
   * <li><code>BitmapFilterQuality.LOW</code></li>
   * <li><code>BitmapFilterQuality.MEDIUM</code></li>
   * <li><code>BitmapFilterQuality.HIGH</code></li></ul>
   * <p>For more information, see the description of the <code>quality</code> property.</p>
   * @param type The placement of the filter effect. Possible values are the flash.filters.BitmapFilterType constants:
   * <ul>
   * <li><code>BitmapFilterType.OUTER</code> — Glow on the outer edge of the object</li>
   * <li><code>BitmapFilterType.INNER</code> — Glow on the inner edge of the object; the default.</li>
   * <li><code>BitmapFilterType.FULL</code> — Glow on top of the object</li></ul>
   * @param knockout Specifies whether the object has a knockout effect. A knockout effect makes the object's fill transparent and reveals the background color of the document. The value <code>true</code> specifies a knockout effect; the default is <code>false</code> (no knockout effect).
   *
   */
  "public function GradientGlowFilter",function GradientGlowFilter$(distance/*:Number = 4.0*/, angle/*:Number = 45*/, colors/*:Array = null*/, alphas/*:Array = null*/, ratios/*:Array = null*/, blurX/*:Number = 4.0*/, blurY/*:Number = 4.0*/, strength/*:Number = 1*/, quality/*:int = 1*/, type/*:String = "inner"*/, knockout/*:Boolean = false*/) {switch(arguments.length){case 0:distance = 4.0;case 1:angle = 45;case 2:colors = null;case 3:alphas = null;case 4:ratios = null;case 5:blurX = 4.0;case 6:blurY = 4.0;case 7:strength = 1;case 8:quality = 1;case 9:type = "inner";case 10:knockout = false;}this.super$2();
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns a copy of this filter object.
   * @return A new GradientGlowFilter instance with all the same properties as the original GradientGlowFilter instance.
   *
   */
  "override public function clone",function clone()/*:BitmapFilter*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["flash.filters.BitmapFilter","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);