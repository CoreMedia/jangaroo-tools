joo.classLoader.prepare("package flash.filters",/* {
import flash.display.BitmapData
import flash.geom.Point*/

/**
 * The DisplacementMapFilter class uses the pixel values from the specified BitmapData object (called the <i>displacement map image</i>) to perform a displacement of an object. You can use this filter to apply a warped or mottled effect to any object that inherits from the DisplayObject class, such as MovieClip, SimpleButton, TextField, and Video objects, as well as to BitmapData objects.
 * <p>The use of filters depends on the object to which you apply the filter:</p>
 * <ul>
 * <li>To apply filters to a display object, use the <code>filters</code> property of the display object. Setting the <code>filters</code> property of an object does not modify the object, and you can remove the filter by clearing the <code>filters</code> property.</li>
 * <li>To apply filters to BitmapData objects, use the <code>BitmapData.applyFilter()</code> method. Calling <code>applyFilter()</code> on a BitmapData object takes the source BitmapData object and the filter object and generates a filtered image.</li></ul>
 * <p>If you apply a filter to a display object, the value of the <code>cacheAsBitmap</code> property of the display object is set to <code>true</code>. If you clear all filters, the original value of <code>cacheAsBitmap</code> is restored.</p>
 * <p>The filter uses the following formula:</p>
 * <listing>
 * dstPixel[x, y] = srcPixel[x + ((componentX(x, y) - 128) * scaleX) / 256, y + ((componentY(x, y) - 128) *scaleY) / 256)
 * </listing>
 * <p>where <code>componentX(x, y)</code> gets the <code>componentX</code> property color value from the <code>mapBitmap</code> property at <code>(x - mapPoint.x ,y - mapPoint.y)</code>.</p>
 * <p>The map image used by the filter is scaled to match the Stage scaling. It is not scaled when the object itself is scaled.</p>
 * <p>This filter supports Stage scaling. However, general scaling, rotation, and skewing are not supported. If the object itself is scaled (if the <code>scaleX</code> and <code>scaleY</code> properties are set to a value other than 1.0), the filter effect is not scaled. It is scaled only when the user zooms in on the Stage.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/filters/DisplacementMapFilter.html#includeExamplesSummary">View the examples</a></p>
 * @see flash.display.BitmapData#applyFilter()
 * @see flash.display.DisplayObject#filters
 * @see flash.display.DisplayObject#cacheAsBitmap
 *
 */
"public final class DisplacementMapFilter extends flash.filters.BitmapFilter",2,function($$private){;return[ 
  /**
   * Specifies the alpha transparency value to use for out-of-bounds displacements. It is specified as a normalized value from 0.0 to 1.0. For example, .25 sets a transparency value of 25%. The default value is 0. Use this property if the <code>mode</code> property is set to <code>DisplacementMapFilterMode.COLOR</code>.
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
   * Specifies what color to use for out-of-bounds displacements. The valid range of displacements is 0.0 to 1.0. Values are in hexadecimal format. The default value for <code>color</code> is 0. Use this property if the <code>mode</code> property is set to <code>DisplacementMapFilterMode.COLOR</code>.
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
   * Describes which color channel to use in the map image to displace the <i>x</i> result. Possible values are BitmapDataChannel constants:
   * <ul>
   * <li><code>BitmapDataChannel.ALPHA</code></li>
   * <li><code>BitmapDataChannel.BLUE</code></li>
   * <li><code>BitmapDataChannel.GREEN</code></li>
   * <li><code>BitmapDataChannel.RED</code></li></ul>
   * @see flash.display.BitmapDataChannel
   *
   */
  "public function get componentX",function componentX$get()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set componentX",function componentX$set(value/*:uint*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Describes which color channel to use in the map image to displace the <i>y</i> result. Possible values are BitmapDataChannel constants:
   * <ul>
   * <li><code>BitmapDataChannel.ALPHA</code></li>
   * <li><code>BitmapDataChannel.BLUE</code></li>
   * <li><code>BitmapDataChannel.GREEN</code></li>
   * <li><code>BitmapDataChannel.RED</code></li></ul>
   * @see flash.display.BitmapDataChannel
   *
   */
  "public function get componentY",function componentY$get()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set componentY",function componentY$set(value/*:uint*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * A BitmapData object containing the displacement map data.
   * @throws TypeError The BitmapData is null when being set
   *
   * @see flash.display.BitmapData
   *
   */
  "public function get mapBitmap",function mapBitmap$get()/*:BitmapData*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set mapBitmap",function mapBitmap$set(value/*:BitmapData*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * A value that contains the offset of the upper-left corner of the target display object from the upper-left corner of the map image.
   * @throws TypeError The Point is null when being set
   *
   * @see flash.geom.Point
   *
   */
  "public function get mapPoint",function mapPoint$get()/*:Point*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set mapPoint",function mapPoint$set(value/*:Point*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The mode for the filter. Possible values are DisplacementMapFilterMode constants:
   * <ul>
   * <li><code>DisplacementMapFilterMode.WRAP</code> — Wraps the displacement value to the other side of the source image.</li>
   * <li><code>DisplacementMapFilterMode.CLAMP</code> — Clamps the displacement value to the edge of the source image.</li>
   * <li><code>DisplacementMapFilterMode.IGNORE</code> — If the displacement value is out of range, ignores the displacement and uses the source pixel.</li>
   * <li><code>DisplacementMapFilterMode.COLOR</code> — If the displacement value is outside the image, substitutes the values in the <code>color</code> and <code>alpha</code> properties.</li></ul>
   * @throws TypeError The String is null when being set
   * @throws ArgumentError The mode string is not one of the valid types
   *
   * @see DisplacementMapFilterMode
   *
   */
  "public function get mode",function mode$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set mode",function mode$set(value/*:String*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The multiplier to use to scale the <i>x</i> displacement result from the map calculation.
   */
  "public function get scaleX",function scaleX$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set scaleX",function scaleX$set(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The multiplier to use to scale the <i>y</i> displacement result from the map calculation.
   */
  "public function get scaleY",function scaleY$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set scaleY",function scaleY$set(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Initializes a DisplacementMapFilter instance with the specified parameters.
   * @param mapBitmap A BitmapData object containing the displacement map data.
   * @param mapPoint A value that contains the offset of the upper-left corner of the target display object from the upper-left corner of the map image.
   * @param componentX Describes which color channel to use in the map image to displace the <i>x</i> result. Possible values are the BitmapDataChannel constants.
   * @param componentY Describes which color channel to use in the map image to displace the <i>y</i> result. Possible values are the BitmapDataChannel constants.
   * @param scaleX The multiplier to use to scale the <i>x</i> displacement result from the map calculation.
   * @param scaleY The multiplier to use to scale the <i>y</i> displacement result from the map calculation.
   * @param mode The mode of the filter. Possible values are the DisplacementMapFilterMode constants.
   * @param color Specifies the color to use for out-of-bounds displacements. The valid range of displacements is 0.0 to 1.0. Use this parameter if <code>mode</code> is set to <code>DisplacementMapFilterMode.COLOR</code>.
   * @param alpha Specifies what alpha value to use for out-of-bounds displacements. It is specified as a normalized value from 0.0 to 1.0. For example, .25 sets a transparency value of 25%. Use this parameter if <code>mode</code> is set to <code>DisplacementMapFilterMode.COLOR</code>.
   *
   * @see flash.display.BitmapDataChannel
   * @see DisplacementMapFilterMode
   *
   */
  "public function DisplacementMapFilter",function DisplacementMapFilter$(mapBitmap/*:BitmapData = null*/, mapPoint/*:Point = null*/, componentX/*:uint = 0*/, componentY/*:uint = 0*/, scaleX/*:Number = 0.0*/, scaleY/*:Number = 0.0*/, mode/*:String = "wrap"*/, color/*:uint = 0*/, alpha/*:Number = 0.0*/) {if(arguments.length<9){if(arguments.length<8){if(arguments.length<7){if(arguments.length<6){if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){mapBitmap = null;}mapPoint = null;}componentX = 0;}componentY = 0;}scaleX = 0.0;}scaleY = 0.0;}mode = "wrap";}color = 0;}alpha = 0.0;}this.super$2();
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns a copy of this filter object.
   * @return A new DisplacementMapFilter instance with all the same properties as the original one.
   *
   */
  "override public function clone",function clone()/*:BitmapFilter*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["flash.filters.BitmapFilter","Error"], "0.8.0", "0.8.3"
);