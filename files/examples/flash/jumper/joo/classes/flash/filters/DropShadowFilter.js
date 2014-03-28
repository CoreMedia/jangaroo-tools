joo.classLoader.prepare("package flash.filters",/* {*/


/**
 * The DropShadowFilter class lets you add a drop shadow to display objects. The shadow algorithm is based on the same box filter that the blur filter uses. You have several options for the style of the drop shadow, including inner or outer shadow and knockout mode. You can apply the filter to any display object (that is, objects that inherit from the DisplayObject class), such as MovieClip, SimpleButton, TextField, and Video objects, as well as to BitmapData objects.
 * <p>The use of filters depends on the object to which you apply the filter:</p>
 * <ul>
 * <li>To apply filters to display objects use the <code>filters</code> property (inherited from DisplayObject). Setting the <code>filters</code> property of an object does not modify the object, and you can remove the filter by clearing the <code>filters</code> property.</li>
 * <li>To apply filters to BitmapData objects, use the <code>BitmapData.applyFilter()</code> method. Calling <code>applyFilter()</code> on a BitmapData object takes the source BitmapData object and the filter object and generates a filtered image as a result.</li></ul>
 * <p>If you apply a filter to a display object, the value of the <code>cacheAsBitmap</code> property of the display object is set to <code>true</code>. If you clear all filters, the original value of <code>cacheAsBitmap</code> is restored.</p>
 * <p>This filter supports Stage scaling. However, it does not support general scaling, rotation, and skewing. If the object itself is scaled (if <code>scaleX</code> and <code>scaleY</code> are set to a value other than 1.0), the filter is not scaled. It is scaled only when the user zooms in on the Stage.</p>
 * <p>A filter is not applied if the resulting image exceeds the maximum dimensions. In AIR 1.5 and Flash Player 10, the maximum is 8,191 pixels in width or height, and the total number of pixels cannot exceed 16,777,215 pixels. (So, if an image is 8,191 pixels wide, it can only be 2,048 pixels high.) In Flash Player 9 and earlier and AIR 1.1 and earlier, the limitation is 2,880 pixels in height and 2,880 pixels in width. If, for example, you zoom in on a large movie clip with a filter applied, the filter is turned off if the resulting image exceeds the maximum dimensions.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/filters/DropShadowFilter.html#includeExamplesSummary">View the examples</a></p>
 * @see flash.display.BitmapData#applyFilter()
 * @see flash.display.DisplayObject#filters
 * @see flash.display.DisplayObject#cacheAsBitmap
 *
 */
"public final class DropShadowFilter extends flash.filters.BitmapFilter",2,function($$private){;return[ 
  /**
   * The alpha transparency value for the shadow color. Valid values are 0.0 to 1.0. For example, .25 sets a transparency value of 25%. The default value is 1.0.
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
   * The angle of the shadow. Valid values are 0 to 360 degrees (floating point). The default value is 45.
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
   * The amount of horizontal blur. Valid values are 0 to 255.0 (floating point). The default value is 4.0.
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
   * The amount of vertical blur. Valid values are 0 to 255.0 (floating point). The default value is 4.0.
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
   * The color of the shadow. Valid values are in hexadecimal format <i>0xRRGGBB</i>. The default value is 0x000000.
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
   * The offset distance for the shadow, in pixels. The default value is 4.0 (floating point).
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
   * Indicates whether or not the object is hidden. The value <code>true</code> indicates that the object itself is not drawn; only the shadow is visible. The default is <code>false</code> (the object is shown).
   */
  "public function get hideObject",function hideObject$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set hideObject",function hideObject$set(value/*:Boolean*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Indicates whether or not the shadow is an inner shadow. The value <code>true</code> indicates an inner shadow. The default is <code>false</code>, an outer shadow (a shadow around the outer edges of the object).
   */
  "public function get inner",function inner$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set inner",function inner$set(value/*:Boolean*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Applies a knockout effect (<code>true</code>), which effectively makes the object's fill transparent and reveals the background color of the document. The default is <code>false</code> (no knockout).
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
   * <p>For most applications, a quality value of low, medium, or high is sufficient. Although you can use additional numeric values up to 15 to achieve different effects, higher values are rendered more slowly. Instead of increasing the value of <code>quality</code>, you can often get a similar effect, and with faster rendering, by simply increasing the values of the <code>blurX</code> and <code>blurY</code> properties.</p>
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
   * The strength of the imprint or spread. The higher the value, the more color is imprinted and the stronger the contrast between the shadow and the background. Valid values are from 0 to 255.0. The default is 1.0.
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
   * Creates a new DropShadowFilter instance with the specified parameters.
   * @param distance Offset distance for the shadow, in pixels.
   * @param angle Angle of the shadow, 0 to 360 degrees (floating point).
   * @param color Color of the shadow, in hexadecimal format <i>0xRRGGBB</i>. The default value is 0x000000.
   * @param alpha Alpha transparency value for the shadow color. Valid values are 0.0 to 1.0. For example, .25 sets a transparency value of 25%.
   * @param blurX Amount of horizontal blur. Valid values are 0 to 255.0 (floating point).
   * @param blurY Amount of vertical blur. Valid values are 0 to 255.0 (floating point).
   * @param strength The strength of the imprint or spread. The higher the value, the more color is imprinted and the stronger the contrast between the shadow and the background. Valid values are 0 to 255.0.
   * @param quality The number of times to apply the filter. Use the BitmapFilterQuality constants:
   * <ul>
   * <li><code>BitmapFilterQuality.LOW</code></li>
   * <li><code>BitmapFilterQuality.MEDIUM</code></li>
   * <li><code>BitmapFilterQuality.HIGH</code></li></ul>
   * <p>For more information about these values, see the <code>quality</code> property description.</p>
   * @param inner Indicates whether or not the shadow is an inner shadow. A value of <code>true</code> specifies an inner shadow. A value of <code>false</code> specifies an outer shadow (a shadow around the outer edges of the object).
   * @param knockout Applies a knockout effect (<code>true</code>), which effectively makes the object's fill transparent and reveals the background color of the document.
   * @param hideObject Indicates whether or not the object is hidden. A value of <code>true</code> indicates that the object itself is not drawn; only the shadow is visible.
   *
   * @see BitmapFilterQuality
   *
   */
  "public function DropShadowFilter",function DropShadowFilter$(distance/*:Number = 4.0*/, angle/*:Number = 45*/, color/*:uint = 0*/, alpha/*:Number = 1.0*/, blurX/*:Number = 4.0*/, blurY/*:Number = 4.0*/, strength/*:Number = 1.0*/, quality/*:int = 1*/, inner/*:Boolean = false*/, knockout/*:Boolean = false*/, hideObject/*:Boolean = false*/) {switch(arguments.length){case 0:distance = 4.0;case 1:angle = 45;case 2:color = 0;case 3:alpha = 1.0;case 4:blurX = 4.0;case 5:blurY = 4.0;case 6:strength = 1.0;case 7:quality = 1;case 8:inner = false;case 9:knockout = false;case 10:hideObject = false;}this.super$2();
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns a copy of this filter object.
   * @return A new DropShadowFilter instance with all the properties of the original DropShadowFilter instance.
   *
   */
  "override public function clone",function clone()/*:BitmapFilter*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["flash.filters.BitmapFilter","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);