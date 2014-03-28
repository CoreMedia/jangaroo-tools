joo.classLoader.prepare("package flash.filters",/* {*/


/**
 * The BevelFilter class lets you add a bevel effect to display objects. A bevel effect gives objects such as buttons a three-dimensional look. You can customize the look of the bevel with different highlight and shadow colors, the amount of blur on the bevel, the angle of the bevel, the placement of the bevel, and a knockout effect. You can apply the filter to any display object (that is, objects that inherit from the DisplayObject class), such as MovieClip, SimpleButton, TextField, and Video objects, as well as to BitmapData objects.
 * <p>To create a new filter, use the constructor <code>new BevelFilter()</code>. The use of filters depends on the object to which you apply the filter:</p>
 * <ul>
 * <li>To apply filters to movie clips, text fields, buttons, and video, use the <code>filters</code> property (inherited from DisplayObject). Setting the <code>filters</code> property of an object does not modify the object, and you can remove the filter by clearing the <code>filters</code> property.</li>
 * <li>To apply filters to BitmapData objects, use the <code>BitmapData.applyFilter()</code> method. Calling <code>applyFilter()</code> on a BitmapData object takes the source BitmapData object and the filter object and generates a filtered image as a result.</li></ul>
 * <p>If you apply a filter to a display object, the value of the <code>cacheAsBitmap</code> property of the object is set to <code>true</code>. If you remove all filters, the original value of <code>cacheAsBitmap</code> is restored.</p>
 * <p>This filter supports Stage scaling. However, it does not support general scaling, rotation, and skewing. If the object itself is scaled (if the <code>scaleX</code> and <code>scaleY</code> properties are not set to 100%), the filter is not scaled. It is scaled only when the user zooms in on the Stage.</p>
 * <p>A filter is not applied if the resulting image exceeds the maximum dimensions. In AIR 1.5 and Flash Player 10, the maximum is 8,191 pixels in width or height, and the total number of pixels cannot exceed 16,777,215 pixels. (So, if an image is 8,191 pixels wide, it can only be 2,048 pixels high.) In Flash Player 9 and earlier and AIR 1.1 and earlier, the limitation is 2,880 pixels in height and 2,880 pixels in width. If, for example, you zoom in on a large movie clip with a filter applied, the filter is turned off if the resulting image exceeds the maximum dimensions.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/filters/BevelFilter.html#includeExamplesSummary">View the examples</a></p>
 * @see flash.display.DisplayObject#filters
 * @see flash.display.DisplayObject#cacheAsBitmap
 * @see flash.display.BitmapData#applyFilter()
 *
 */
"public final class BevelFilter extends flash.filters.BitmapFilter",2,function($$private){;return[ 
  /**
   * The angle of the bevel. Valid values are from 0 to 360°. The default value is 45°.
   * <p>The angle value represents the angle of the theoretical light source falling on the object and determines the placement of the effect relative to the object. If the <code>distance</code> property is set to 0, the effect is not offset from the object and, therefore, the <code>angle</code> property has no effect.</p>
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
   * The amount of horizontal blur, in pixels. Valid values are from 0 to 255 (floating point). The default value is 4. Values that are a power of 2 (such as 2, 4, 8, 16, and 32) are optimized to render more quickly than other values.
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
   * The amount of vertical blur, in pixels. Valid values are from 0 to 255 (floating point). The default value is 4. Values that are a power of 2 (such as 2, 4, 8, 16, and 32) are optimized to render more quickly than other values.
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
   * The offset distance of the bevel. Valid values are in pixels (floating point). The default is 4.
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
   * The alpha transparency value of the highlight color. The value is specified as a normalized value from 0 to 1. For example, .25 sets a transparency value of 25%. The default value is 1.
   */
  "public function get highlightAlpha",function highlightAlpha$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set highlightAlpha",function highlightAlpha$set(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The highlight color of the bevel. Valid values are in hexadecimal format, <i>0xRRGGBB</i>. The default is 0xFFFFFF.
   */
  "public function get highlightColor",function highlightColor$get()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set highlightColor",function highlightColor$set(value/*:uint*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Applies a knockout effect (<code>true</code>), which effectively makes the object's fill transparent and reveals the background color of the document. The default value is <code>false</code> (no knockout).
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
   * <p>You can use the following <code>BitmapFilterQuality</code> constants to specify values of the <code>quality</code> property:</p>
   * <ul>
   * <li><code>BitmapFilterQuality.LOW</code></li>
   * <li><code>BitmapFilterQuality.MEDIUM</code></li>
   * <li><code>BitmapFilterQuality.HIGH</code></li></ul>
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
   * The alpha transparency value of the shadow color. This value is specified as a normalized value from 0 to 1. For example, .25 sets a transparency value of 25%. The default is 1.
   */
  "public function get shadowAlpha",function shadowAlpha$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set shadowAlpha",function shadowAlpha$set(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The shadow color of the bevel. Valid values are in hexadecimal format, <i>0xRRGGBB</i>. The default is 0x000000.
   */
  "public function get shadowColor",function shadowColor$get()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set shadowColor",function shadowColor$set(value/*:uint*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The strength of the imprint or spread. Valid values are from 0 to 255. The larger the value, the more color is imprinted and the stronger the contrast between the bevel and the background. The default value is 1.
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
   * The placement of the bevel on the object. Inner and outer bevels are placed on the inner or outer edge; a full bevel is placed on the entire object. Valid values are the <code>BitmapFilterType</code> constants:
   * <ul>
   * <li><code>BitmapFilterType.INNER</code></li>
   * <li><code>BitmapFilterType.OUTER</code></li>
   * <li><code>BitmapFilterType.FULL</code></li></ul>
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
   * Initializes a new BevelFilter instance with the specified parameters.
   * @param distance The offset distance of the bevel, in pixels (floating point).
   * @param angle The angle of the bevel, from 0 to 360 degrees.
   * @param highlightColor The highlight color of the bevel, <i>0xRRGGBB</i>.
   * @param highlightAlpha The alpha transparency value of the highlight color. Valid values are 0.0 to 1.0. For example, .25 sets a transparency value of 25%.
   * @param shadowColor The shadow color of the bevel, <i>0xRRGGBB</i>.
   * @param shadowAlpha The alpha transparency value of the shadow color. Valid values are 0.0 to 1.0. For example, .25 sets a transparency value of 25%.
   * @param blurX The amount of horizontal blur in pixels. Valid values are 0 to 255.0 (floating point).
   * @param blurY The amount of vertical blur in pixels. Valid values are 0 to 255.0 (floating point).
   * @param strength The strength of the imprint or spread. The higher the value, the more color is imprinted and the stronger the contrast between the bevel and the background. Valid values are 0 to 255.0.
   * @param quality The quality of the bevel. Valid values are 0 to 15, but for most applications, you can use <code>BitmapFilterQuality</code> constants:
   * <ul>
   * <li><code>BitmapFilterQuality.LOW</code></li>
   * <li><code>BitmapFilterQuality.MEDIUM</code></li>
   * <li><code>BitmapFilterQuality.HIGH</code></li></ul>
   * <p>Filters with lower values render faster. You can use the other available numeric values to achieve different effects.</p>
   * @param type The type of bevel. Valid values are <code>BitmapFilterType</code> constants: <code>BitmapFilterType.INNER</code>, <code>BitmapFilterType.OUTER</code>, or <code>BitmapFilterType.FULL</code>.
   * @param knockout Applies a knockout effect (<code>true</code>), which effectively makes the object's fill transparent and reveals the background color of the document.
   *
   * @see BitmapFilterQuality
   * @see BitmapFilterType
   *
   */
  "public function BevelFilter",function BevelFilter$(distance/*:Number = 4.0*/, angle/*:Number = 45*/, highlightColor/*:uint = 0xFFFFFF*/, highlightAlpha/*:Number = 1.0*/, shadowColor/*:uint = 0x000000*/, shadowAlpha/*:Number = 1.0*/, blurX/*:Number = 4.0*/, blurY/*:Number = 4.0*/, strength/*:Number = 1*/, quality/*:int = 1*/, type/*:String = "inner"*/, knockout/*:Boolean = false*/) {if(arguments.length<12){if(arguments.length<11){if(arguments.length<10){if(arguments.length<9){if(arguments.length<8){if(arguments.length<7){if(arguments.length<6){if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){distance = 4.0;}angle = 45;}highlightColor = 0xFFFFFF;}highlightAlpha = 1.0;}shadowColor = 0x000000;}shadowAlpha = 1.0;}blurX = 4.0;}blurY = 4.0;}strength = 1;}quality = 1;}type = "inner";}knockout = false;}this.super$2();
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns a copy of this filter object.
   * @return A new BevelFilter instance with all the same properties as the original BevelFilter instance.
   *
   */
  "override public function clone",function clone()/*:BitmapFilter*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["flash.filters.BitmapFilter","Error"], "0.8.0", "0.8.3"
);