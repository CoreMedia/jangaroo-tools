joo.classLoader.prepare("package flash.filters",/* {*/


/**
 * The ColorMatrixFilter class lets you apply a 4 x 5 matrix transformation on the RGBA color and alpha values of every pixel in the input image to produce a result with a new set of RGBA color and alpha values. It allows saturation changes, hue rotation, luminance to alpha, and various other effects. You can apply the filter to any display object (that is, objects that inherit from the DisplayObject class), such as MovieClip, SimpleButton, TextField, and Video objects, as well as to BitmapData objects.
 * <p><b>Note:</b> For RGBA values, the most significant byte represents the red channel value, followed by green, blue, and then alpha.</p>
 * <p>To create a new color matrix filter, use the syntax <code>new ColorMatrixFilter()</code>. The use of filters depends on the object to which you apply the filter:</p>
 * <ul>
 * <li>To apply filters to movie clips, text fields, buttons, and video, use the <code>filters</code> property (inherited from DisplayObject). Setting the <code>filters</code> property of an object does not modify the object, and you can remove the filter by clearing the <code>filters</code> property.</li>
 * <li>To apply filters to BitmapData objects, use the <code>BitmapData.applyFilter()</code> method. Calling <code>applyFilter()</code> on a BitmapData object takes the source BitmapData object and the filter object and generates a filtered image as a result.</li></ul>
 * <p>If you apply a filter to a display object, the <code>cacheAsBitmap</code> property of the display object is set to <code>true</code>. If you remove all filters, the original value of <code>cacheAsBitmap</code> is restored.</p>
 * <p>A filter is not applied if the resulting image exceeds the maximum dimensions. In AIR 1.5 and Flash Player 10, the maximum is 8,191 pixels in width or height, and the total number of pixels cannot exceed 16,777,215 pixels. (So, if an image is 8,191 pixels wide, it can only be 2,048 pixels high.) In Flash Player 9 and earlier and AIR 1.1 and earlier, the limitation is 2,880 pixels in height and 2,880 pixels in width. For example, if you zoom in on a large movie clip with a filter applied, the filter is turned off if the resulting image reaches the maximum dimensions.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/filters/ColorMatrixFilter.html#includeExamplesSummary">View the examples</a></p>
 * @see flash.display.BitmapData#getPixel()
 * @see flash.display.BitmapData#applyFilter()
 * @see flash.display.DisplayObject#filters
 * @see flash.display.DisplayObject#cacheAsBitmap
 *
 */
"public final class ColorMatrixFilter extends flash.filters.BitmapFilter",2,function($$private){;return[ 
  /**
   * An array of 20 items for 4 x 5 color transform. The <code>matrix</code> property cannot be changed by directly modifying its value (for example, <code>myFilter.matrix[2] = 1;</code>). Instead, you must get a reference to the array, make the change to the reference, and reset the value.
   * <p>The color matrix filter separates each source pixel into its red, green, blue, and alpha components as srcR, srcG, srcB, srcA. To calculate the result of each of the four channels, the value of each pixel in the image is multiplied by the values in the transformation matrix. An offset, between -255 and 255, can optionally be added to each result (the fifth item in each row of the matrix). The filter combines each color component back into a single pixel and writes out the result. In the following formula, a[0] through a[19] correspond to entries 0 through 19 in the 20-item array that is passed to the <code>matrix</code> property:</p>
   * <pre>    redResult   = (a[0]  * srcR) + (a[1]  * srcG) + (a[2]  * srcB) + (a[3]  * srcA) + a[4]
   greenResult = (a[5]  * srcR) + (a[6]  * srcG) + (a[7]  * srcB) + (a[8]  * srcA) + a[9]
   blueResult  = (a[10] * srcR) + (a[11] * srcG) + (a[12] * srcB) + (a[13] * srcA) + a[14]
   alphaResult = (a[15] * srcR) + (a[16] * srcG) + (a[17] * srcB) + (a[18] * srcA) + a[19]
   </pre>
   * <p>For each color value in the array, a value of 1 is equal to 100% of that channel being sent to the output, preserving the value of the color channel.</p>
   * <p>The calculations are performed on unmultiplied color values. If the input graphic consists of premultiplied color values, those values are automatically converted into unmultiplied color values for this operation.</p>
   * <p>Two optimized modes are available:</p>
   * <p><b>Alpha only.</b> When you pass to the filter a matrix that adjusts only the alpha component, as shown here, the filter optimizes its performance:</p>
   * <pre>        1 0 0 0 0
   0 1 0 0 0
   0 0 1 0 0
   0 0 0 N 0  (where N is between 0.0 and 1.0)
   </pre>
   * <p><b>Faster version</b>. Available only with SSE/AltiVec accelerator-enabled processors, such as Intel<sup>®</sup> Pentium<sup>®</sup> 3 and later and Apple<sup>®</sup> G4 and later. The accelerator is used when the multiplier terms are in the range -15.99 to 15.99 and the adder terms a[4], a[9], a[14], and a[19] are in the range -8000 to 8000.</p>
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
   * Initializes a new ColorMatrixFilter instance with the specified parameters.
   * @param matrix An array of 20 items arranged as a 4 x 5 matrix.
   *
   */
  "public function ColorMatrixFilter",function ColorMatrixFilter$(matrix/*:Array = null*/) {if(arguments.length<1){matrix = null;}this.super$2();
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns a copy of this filter object.
   * @return A new ColorMatrixFilter instance with all of the same properties as the original one.
   *
   */
  "override public function clone",function clone()/*:BitmapFilter*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["flash.filters.BitmapFilter","Error"], "0.8.0", "0.8.3"
);