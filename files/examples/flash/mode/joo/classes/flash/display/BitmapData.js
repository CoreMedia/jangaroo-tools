joo.classLoader.prepare("package flash.display",/* {
import flash.filters.BitmapFilter
import flash.geom.ColorTransform
import flash.geom.Matrix
import flash.geom.Point
import flash.geom.Rectangle
import flash.utils.ByteArray

import js.CanvasRenderingContext2D
import js.HTMLCanvasElement
import js.HTMLElement
import js.HTMLImageElement
import js.Image
import js.ImageData*/

/**
 * The BitmapData class lets you work with the data (pixels) of a Bitmap object. You can use the methods of the BitmapData class to create arbitrarily sized transparent or opaque bitmap images and manipulate them in various ways at runtime. You can also access the BitmapData for a bitmap image that you load with the <code>flash.display.Loader</code> class.
 * <p>This class lets you separate bitmap rendering operations from the internal display updating routines of Flash Player. By manipulating a BitmapData object directly, you can create complex images without incurring the per-frame overhead of constantly redrawing the content from vector data.</p>
 * <p>The methods of the BitmapData class support effects that are not available through the filters available to non-bitmap display objects.</p>
 * <p>A BitmapData object contains an array of pixel data. This data can represent either a fully opaque bitmap or a transparent bitmap that contains alpha channel data. Either type of BitmapData object is stored as a buffer of 32-bit integers. Each 32-bit integer determines the properties of a single pixel in the bitmap.</p>
 * <p>Each 32-bit integer is a combination of four 8-bit channel values (from 0 to 255) that describe the alpha transparency and the red, green, and blue (ARGB) values of the pixel. (For ARGB values, the most significant byte represents the alpha channel value, followed by red, green, and blue.)</p>
 * <p>The four channels (alpha, red, green, and blue) are represented as numbers when you use them with the <code>BitmapData.copyChannel()</code> method or the <code>DisplacementMapFilter.componentX</code> and <code>DisplacementMapFilter.componentY</code> properties, and these numbers are represented by the following constants in the BitmapDataChannel class:</p>
 * <ul>
 * <li><code>BitmapDataChannel.ALPHA</code></li>
 * <li><code>BitmapDataChannel.RED</code></li>
 * <li><code>BitmapDataChannel.GREEN</code></li>
 * <li><code>BitmapDataChannel.BLUE</code></li></ul>
 * <p>You can attach BitmapData objects to a Bitmap object by using the <code>bitmapData</code> property of the Bitmap object.</p>
 * <p>You can use a BitmapData object to fill a Graphics object by using the <code>Graphics.beginBitmapFill()</code> method.</p>
 * <p>In the AIR runtime, the DockIcon, Icon, InteractiveIcon, and SystemTrayIcon classes each include a <code>bitmaps</code> property that is an array of BitmapData objects that define the bitmap images for an icon.</p>
 * <p>In AIR 1.5 and Flash Player 10, the maximum size for a BitmapData object is 8,191 pixels in width or height, and the total number of pixels cannot exceed 16,777,215 pixels. (So, if a BitmapData object is 8,191 pixels wide, it can only be 2,048 pixels high.) In Flash Player 9 and earlier and AIR 1.1 and earlier, the limitation is 2,880 pixels in height and 2,880 in width.</p>
 * <p>Calls to any method or property of a BitmapData object throw an ArgumentError error if the BitmapData object is invalid (for example, if it has <code>height == 0</code> and <code>width == 0</code>) or it has been disposed of via dispose().</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/display/BitmapData.html#includeExamplesSummary">View the examples</a></p>
 * @see Bitmap#bitmapData
 * @see flash.desktop.DockIcon#bitmaps
 * @see Graphics#beginBitmapFill()
 * @see flash.desktop.Icon#bitmaps
 * @see flash.desktop.InteractiveIcon#bitmaps
 * @see Loader
 * @see flash.desktop.SystemTrayIcon#bitmaps
 *
 */
"public class BitmapData implements flash.display.IBitmapDrawable",1,function($$private){var is=joo.is,as=joo.as;return[ 
  /**
   * The height of the bitmap image in pixels.
   */
  "public native function get height"/*():int;*/,

  /**
   * The rectangle that defines the size and location of the bitmap image. The top and left of the rectangle are 0; the width and height are equal to the width and height in pixels of the BitmapData object.
   */
  "public function get rect",function rect$get()/*:Rectangle*/ {
    return new flash.geom.Rectangle(0, 0, this.width, this.height);
  },

  /**
   * Defines whether the bitmap image supports per-pixel transparency. You can set this value only when you construct a BitmapData object by passing in <code>true</code> for the <code>transparent</code> parameter of the constructor. Then, after you create a BitmapData object, you can check whether it supports per-pixel transparency by determining if the value of the <code>transparent</code> property is <code>true</code>.
   */
  "public native function get transparent"/*():Boolean;*/,

  /**
   * The width of the bitmap image in pixels.
   */
  "public native function get width"/*():int;*/,

  /**
   * Creates a BitmapData object with a specified width and height. If you specify a value for the <code>fillColor</code> parameter, every pixel in the bitmap is set to that color.
   * <p>By default, the bitmap is created as transparent, unless you pass the value <code>false</code> for the <code>transparent</code> parameter. After you create an opaque bitmap, you cannot change it to a transparent bitmap. Every pixel in an opaque bitmap uses only 24 bits of color channel information. If you define the bitmap as transparent, every pixel uses 32 bits of color channel information, including an alpha transparency channel.</p>
   * <p>In AIR 1.5 and Flash Player 10, the maximum size for a BitmapData object is 8,191 pixels in width or height, and the total number of pixels cannot exceed 16,777,215 pixels. (So, if a BitmapData object is 8,191 pixels wide, it can only be 2,048 pixels high.) In Flash Player 9 and earlier and AIR 1.1 and earlier, the limitation is 2,880 pixels in height and 2,880 pixels in width. If you specify a width or height value that is greater than 2880, a new instance is not created.</p>
   * @param width The width of the bitmap image in pixels.
   * @param height The height of the bitmap image in pixels.
   * @param transparent Specifies whether the bitmap image supports per-pixel transparency. The default value is <code>true</code> (transparent). To create a fully transparent bitmap, set the value of the <code>transparent</code> parameter to <code>true</code> and the value of the <code>fillColor</code> parameter to 0x00000000 (or to 0). Setting the <code>transparent</code> property to <code>false</code> can result in minor improvements in rendering performance.
   * @param fillColor A 32-bit ARGB color value that you use to fill the bitmap image area. The default value is 0xFFFFFFFF (solid white).
   *
   * @throws ArgumentError width and/or height exceed the maximum dimensions.
   *
   */
  "public function BitmapData",function BitmapData$(width/*:int*/, height/*:int*/, transparent/*:Boolean = true*/, fillColor/*:uint = 0xFFFFFFFF*/) {switch(arguments.length){case 0:case 1:case 2:transparent = true;case 3:fillColor = 0xFFFFFFFF;}this.elementChangeListeners$1=this.elementChangeListeners$1();
    this.transparent = transparent;
    this.width = width;
    this.height = height;
    this._alpha$1 = transparent ? (fillColor >>> 24) / 0xFF : 1;
    this._fillColor$1 = fillColor & 0xFFFFFF;
  },

  /**
   * Takes a source image and a filter object and generates the filtered image.
   * <p>This method relies on the behavior of built-in filter objects, which determine the destination rectangle that is affected by an input source rectangle.</p>
   * <p>After a filter is applied, the resulting image can be larger than the input image. For example, if you use a BlurFilter class to blur a source rectangle of (50,50,100,100) and a destination point of (10,10), the area that changes in the destination image is larger than (10,10,60,60) because of the blurring. This happens internally during the <code>applyFilter()</code> call.</p>
   * <p>If the <code>sourceRect</code> parameter of the <code>sourceBitmapData</code> parameter is an interior region, such as (50,50,100,100) in a 200 x 200 image, the filter uses the source pixels outside the <code>sourceRect</code> parameter to generate the destination rectangle.</p>
   * <p>If the BitmapData object and the object specified as the <code>sourceBitmapData</code> parameter are the same object, the application uses a temporary copy of the object to perform the filter. For best performance, avoid this situation.</p>
   * @param sourceBitmapData The input bitmap image to use. The source image can be a different BitmapData object or it can refer to the current BitmapData instance.
   * @param sourceRect A rectangle that defines the area of the source image to use as input.
   * @param destPoint The point within the destination image (the current BitmapData instance) that corresponds to the upper-left corner of the source rectangle.
   * @param filter The filter object that you use to perform the filtering operation. Each type of filter has certain requirements, as follows:
   * <ul>
   * <li><b>BlurFilter</b> � This filter can use source and destination images that are either opaque or transparent. If the formats of the images do not match, the copy of the source image that is made during the filtering matches the format of the destination image.</li>
   * <li><b>BevelFilter, DropShadowFilter, GlowFilter, ChromeFilter</b> � The destination image of these filters must be a transparent image. Calling DropShadowFilter or GlowFilter creates an image that contains the alpha channel data of the drop shadow or glow. It does not create the drop shadow onto the destination image. If you use any of these filters with an opaque destination image, an exception is thrown.</li>
   * <li><b>ConvolutionFilter</b> � This filter can use source and destination images that are either opaque or transparent.</li>
   * <li><b>ColorMatrixFilter</b> � This filter can use source and destination images that are either opaque or transparent.</li>
   * <li><b>DisplacementMapFilter</b> � This filter can use source and destination images that are either opaque or transparent, but the source and destination image formats must be the same.</li></ul>
   *
   * @throws TypeError The sourceBitmapData, sourceRect, destPoint or filter are null.
   * @throws flash.errors.IllegalOperationError The transparency of the BitmapData objects are not compatible with the filter operation.
   *
   * @see flash.filters.BevelFilter
   * @see flash.filters.BlurFilter
   * @see flash.filters.ColorMatrixFilter
   * @see flash.filters.ConvolutionFilter
   * @see flash.filters.DisplacementMapFilter
   * @see flash.filters.DropShadowFilter
   * @see flash.filters.GlowFilter
   * @see DisplayObject#filters
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7dbb.html Creating and applying filters
   *
   * @example The following example shows how to apply a blur filter to a BitmapData instance:
   * <listing>
   * import flash.display.Bitmap;
   * import flash.display.BitmapData;
   * import flash.geom.Point;
   * import flash.geom.Rectangle;
   * import flash.filters.BlurFilter;
   *
   * var bmd:BitmapData = new BitmapData(80, 30, false, 0xFFCC00);
   * var rect:Rectangle = new Rectangle(10, 10, 40, 10);
   * bmd.fillRect(rect, 0xFF0000);
   *
   * var pt:Point = new Point(10, 10);
   * var filter:BlurFilter = new BlurFilter();
   * bmd.applyFilter(bmd, rect, pt, filter);
   *
   * var bm:Bitmap = new Bitmap(bmd);
   * addChild(bm);
   * </listing>
   */
  "public function applyFilter",function applyFilter(sourceBitmapData/*:BitmapData*/, sourceRect/*:Rectangle*/, destPoint/*:Point*/, filter/*:BitmapFilter*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns a new BitmapData object that is a clone of the original instance with an exact copy of the contained bitmap.
   * @return A new BitmapData object that is identical to the original.
   *
   * @example The following example shows how to clone a BitmapData instance, and it shows that when you modify the cloned BitmapData instance, the original remains unmodified:
   * <listing>
   * import flash.display.Bitmap;
   * import flash.display.BitmapData;
   *
   * var bmd1:BitmapData = new BitmapData(100, 80, false, 0x00000000);
   * var bmd2:BitmapData = bmd1.clone();
   *
   * bmd1.setPixel32(1, 1, 0xFFFFFFFF);
   *
   * trace(bmd1.getPixel32(1, 1).toString(16)); // ffffffff
   * trace(bmd2.getPixel32(1, 1).toString(16)); // ff000000
   *
   * var bm1:Bitmap = new Bitmap(bmd1);
   * this.addChild(bm1);
   *
   * var bm2:Bitmap = new Bitmap(bmd2);
   * bm2.x = 110;
   * this.addChild(bm2);
   * </listing>
   */
  "public function clone",function clone()/*:BitmapData*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Adjusts the color values in a specified area of a bitmap image by using a <code>ColorTransform</code> object. If the rectangle matches the boundaries of the bitmap image, this method transforms the color values of the entire image.
   * @param rect A Rectangle object that defines the area of the image in which the ColorTransform object is applied.
   * @param colorTransform A ColorTransform object that describes the color transformation values to apply.
   *
   * @throws TypeError The rect or colorTransform are null.
   *
   * @see flash.geom.ColorTransform
   * @see flash.geom.Rectangle
   *
   * @example The following example shows how to apply a color transform to the left half (rectangle) of a BitmapData object:
   * <listing>
   * import flash.display.Bitmap;
   * import flash.display.BitmapData;
   * import flash.geom.Rectangle;
   * import flash.geom.ColorTransform;
   *
   * var bmd:BitmapData = new BitmapData(80, 30, false, 0xFF0000);
   *
   * var cTransform:ColorTransform = new ColorTransform();
   * cTransform.alphaMultiplier = 0.5
   * var rect:Rectangle = new Rectangle(0, 0, 40, 30);
   * bmd.colorTransform(rect, cTransform);
   *
   * var bm:Bitmap = new Bitmap(bmd);
   * addChild(bm);
   * </listing>
   */
  "public function colorTransform",function colorTransform(rect/*:Rectangle*/, colorTransform/*:ColorTransform*/)/*:void*/ {
    var context/*:CanvasRenderingContext2D*/ = this.getContext$1();
    // check for all known faster methods to map colorTransform directly to canvas APIs:
    if (colorTransform.alphaOffset==0
      && colorTransform.redMultiplier>=0 && colorTransform.redMultiplier<=1
      && colorTransform.redMultiplier==colorTransform.greenMultiplier
      && colorTransform.redMultiplier==colorTransform.blueMultiplier
      && colorTransform.redMultiplier==colorTransform.alphaMultiplier) {
      if (colorTransform.redOffset>=0 && colorTransform.greenOffset>=0 && colorTransform.blueOffset>=0) {
        context.save();
        context.setTransform(1, 0, 0, 1, 0, 0);
        // TODO: which other context attributes to reset?
        var alpha/* : Number*/ = 1;
        if (colorTransform.redMultiplier==1) {
          context.globalCompositeOperation = "lighter";
        } else {
          context.globalCompositeOperation = "source-over";
          alpha -= colorTransform.alphaMultiplier;
        }
        context.fillStyle = "rgba("+
                            [colorTransform.redOffset, colorTransform.greenOffset, colorTransform.blueOffset,
                              alpha]
                              .join(",")+")";
        context.fillRect(rect.x,rect.y, rect.width,rect.height);
        context.restore();
        return;
      //} else {
      //   TODO: "destination-out" for alphaMultiplier within 0..1 only
      //   TODO: negative offsets: "darker" does not work in Firefox :-(
      }
    }
    // generic, but very slow solution:
    // get the image data to manipulate
    var input/* : ImageData*/ = context.getImageData(rect.x, rect.y, rect.width, rect.height);
    var inputData/* : Array*/ = input.data;

    // color transformation:
    var maps/* : Array*/ = colorTransform.getComponentMaps();
    var i/* : uint*/;
    for (var m/*:uint*/ =0; m<4; ++m) {
      var map/* : Array*/ = maps[m];
      if (map) {
        for (i = inputData.length-4 + m; i >= 0; i -= 4) {
          inputData[i] = map[inputData[i]];
        }
      }
    }
    // put the image data back after manipulation
    context.putImageData(input, rect.x, rect.y);
  },

  /**
   * Compares two BitmapData objects. If the two BitmapData objects have the same dimensions (width and height), the method returns a new BitmapData object, in which each pixel is the "difference" between the pixels in the two source objects:
   * <ul>
   * <li>If two pixels are equal, the difference pixel is 0x00000000.</li>
   * <li>If two pixels have different RGB values (ignoring the alpha value), the difference pixel is 0xRRGGBB where RR/GG/BB are the individual difference values between red, green, and blue channels (the pixel value in the source object minus the pixel value in the <code>otherBitmapData</code> object). Alpha channel differences are ignored in this case.</li>
   * <li>If only the alpha channel value is different, the pixel value is 0x<i>ZZ</i>FFFFFF, where <i>ZZ</i> is the difference in the alpha values (the alpha value in the source object minus the alpha value in the <code>otherBitmapData</code> object).</li></ul>
   * <p>For example, consider the following two BitmapData objects:</p>
   * <listing>
   *      var bmd1:BitmapData = new BitmapData(50, 50, true, 0xFFFF8800);
   *      var bmd2:BitmapData = new BitmapData(50, 50, true, 0xCCCC6600);
   *      var diffBmpData:BitmapData = bmd1.compare(bmd2) as BitmapData;
   *      trace ("0x" + diffBmpData.getPixel(0,0).toString(16); // 0x332200
   *     </listing>
   * <p><b>Note:</b> The colors used to fill the two BitmapData objects have slightly different RGB values (0xFF0000 and 0xFFAA00). The result of the <code>compare()</code> method is a new BitmapData object with each pixel showing the difference in the RGB values between the two bitmaps.</p>
   * <p>Consider the following two BitmapData objects, in which the RGB colors are the same, but the alpha values are different:</p>
   * <listing>
   *      var bmd1:BitmapData = new BitmapData(50, 50, true, 0xFFFFAA00);
   *      var bmd2:BitmapData = new BitmapData(50, 50, true, 0xCCFFAA00);
   *      var diffBmpData:BitmapData = bmd1.compare(bmd2) as BitmapData;
   *      trace ("0x" + diffBmpData.getPixel32(0,0).toString(16); // 0x33ffffff
   *     </listing>
   * <p>The result of the <code>compare()</code> method is a new BitmapData object with each pixel showing the difference in the alpha values between the two bitmaps.</p>
   * <p>If the BitmapData objects are equivalent (with the same width, height, and identical pixel values), the method returns the number 0.</p>
   * <p>If the widths of the BitmapData objects are not equal, the method returns the number -3.</p>
   * <p>If the heights of the BitmapData objects are not equal, but the widths are the same, the method returns the number -4.</p>
   * <p>The following example compares two Bitmap objects with different widths (50 and 60):</p>
   * <listing>
   *      var bmd1:BitmapData = new BitmapData(100, 50, false, 0xFFFF0000);
   *      var bmd2:BitmapData = new BitmapData(100, 60, false, 0xFFFFAA00);
   *      trace(bmd1.compare(bmd2)); // -4
   *     </listing>
   * @param otherBitmapData The BitmapData object to compare with the source BitmapData object.
   *
   * @return If the two BitmapData objects have the same dimensions (width and height), the method returns a new BitmapData object that has the difference between the two objects (see the main discussion). If the BitmapData objects are equivalent, the method returns the number 0. If the widths of the BitmapData objects are not equal, the method returns the number -3. If the heights of the BitmapData objects are not equal, the method returns the number -4.
   *
   * @throws TypeError The otherBitmapData is null.
   *
   * @example The following example shows the value of a pixel in the BitmapData object that results from comparing two BitmapData objects of the same dimensions:
   * <listing>
   * import flash.display.Bitmap;
   * import flash.display.BitmapData;
   *
   * var bmd1:BitmapData = new BitmapData(50, 50, true, 0xFFFFAA00);
   * var bmd2:BitmapData = new BitmapData(50, 50, true, 0xCCFFAA00);
   * var diffBmpData:BitmapData = BitmapData(bmd1.compare(bmd2));
   * var diffValue:String = diffBmpData.getPixel32(1, 1).toString(16);
   * trace (diffValue); // 33ffffff
   *
   * var bm1:Bitmap = new Bitmap(bmd1);
   * addChild(bm1);
   * var bm2:Bitmap = new Bitmap(bmd2);
   * addChild(bm2);
   * bm2.x = 60;
   * </listing>
   */
  "public function compare",function compare(otherBitmapData/*:BitmapData*/)/*:Object*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Transfers data from one channel of another BitmapData object or the current BitmapData object into a channel of the current BitmapData object. All of the data in the other channels in the destination BitmapData object are preserved.
   * <p>The source channel value and destination channel value can be one of following values:</p>
   * <ul>
   * <li><code>BitmapDataChannel.RED</code></li>
   * <li><code>BitmapDataChannel.GREEN</code></li>
   * <li><code>BitmapDataChannel.BLUE</code></li>
   * <li><code>BitmapDataChannel.ALPHA</code></li></ul>
   * @param sourceBitmapData The input bitmap image to use. The source image can be a different BitmapData object or it can refer to the current BitmapData object.
   * @param sourceRect The source Rectangle object. To copy only channel data from a smaller area within the bitmap, specify a source rectangle that is smaller than the overall size of the BitmapData object.
   * @param destPoint The destination Point object that represents the upper-left corner of the rectangular area where the new channel data is placed. To copy only channel data from one area to a different area in the destination image, specify a point other than (0,0).
   * @param sourceChannel The source channel. Use a value from the BitmapDataChannel class (<code>BitmapDataChannel.RED</code>, <code>BitmapDataChannel.BLUE</code>, <code>BitmapDataChannel.GREEN</code>, <code>BitmapDataChannel.ALPHA</code>).
   * @param destChannel The destination channel. Use a value from the BitmapDataChannel class (<code>BitmapDataChannel.RED</code>, <code>BitmapDataChannel.BLUE</code>, <code>BitmapDataChannel.GREEN</code>, <code>BitmapDataChannel.ALPHA</code>).
   *
   * @throws TypeError The sourceBitmapData, sourceRect or destPoint are null.
   *
   * @see flash.geom.Rectangle
   *
   * @example The following example shows how to copy the red channel in a BitmapData object to its own blue channel in a 20 x 20 pixel region:
   * <listing>
   * import flash.display.Bitmap;
   * import flash.display.BitmapData;
   * import flash.geom.Rectangle;
   * import flash.geom.Point;
   *
   * var bmd:BitmapData = new BitmapData(100, 80, false, 0x00FF0000);
   *
   * var rect:Rectangle = new Rectangle(0, 0, 20, 20);
   * var pt:Point = new Point(10, 10);
   * bmd.copyChannel(bmd, rect, pt, BitmapDataChannel.RED, BitmapDataChannel.BLUE);
   *
   * var bm:Bitmap = new Bitmap(bmd);
   * this.addChild(bm);
   * </listing>
   */
  "public function copyChannel",function copyChannel(sourceBitmapData/*:BitmapData*/, sourceRect/*:Rectangle*/, destPoint/*:Point*/, sourceChannel/*:uint*/, destChannel/*:uint*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Provides a fast routine to perform pixel manipulation between images with no stretching, rotation, or color effects. This method copies a rectangular area of a source image to a rectangular area of the same size at the destination point of the destination BitmapData object.
   * <p>If you include the <code>alphaBitmap</code> and <code>alphaPoint</code> parameters, you can use a secondary image as an alpha source for the source image. If the source image has alpha data, both sets of alpha data are used to composite pixels from the source image to the destination image. The <code>alphaPoint</code> parameter is the point in the alpha image that corresponds to the upper-left corner of the source rectangle. Any pixels outside the intersection of the source image and alpha image are not copied to the destination image.</p>
   * <p>The <code>mergeAlpha</code> property controls whether or not the alpha channel is used when a transparent image is copied onto another transparent image. To copy pixels with the alpha channel data, set the <code>mergeAlpha</code> property to <code>true</code>. By default, the <code>mergeAlpha</code> property is <code>false</code>.</p>
   * @param sourceBitmapData The input bitmap image from which to copy pixels. The source image can be a different BitmapData instance, or it can refer to the current BitmapData instance.
   * @param sourceRect A rectangle that defines the area of the source image to use as input.
   * @param destPoint The destination point that represents the upper-left corner of the rectangular area where the new pixels are placed.
   * @param alphaBitmapData A secondary, alpha BitmapData object source.
   * @param alphaPoint The point in the alpha BitmapData object source that corresponds to the upper-left corner of the <code>sourceRect</code> parameter.
   * @param mergeAlpha To use the alpha channel, set the value to <code>true</code>. To copy pixels with no alpha channel, set the value to <code>false</code>.
   *
   * @throws TypeError The sourceBitmapData, sourceRect, destPoint are null.
   *
   * @example The following example shows how to copy pixels from a 20 x 20 pixel region in one BitmapData object to another BitmapData object:
   * <listing>
   * import flash.display.Bitmap;
   * import flash.display.BitmapData;
   * import flash.geom.Rectangle;
   * import flash.geom.Point;
   *
   * var bmd1:BitmapData = new BitmapData(40, 40, false, 0x000000FF);
   * var bmd2:BitmapData = new BitmapData(80, 40, false, 0x0000CC44);
   *
   * var rect:Rectangle = new Rectangle(0, 0, 20, 20);
   * var pt:Point = new Point(10, 10);
   * bmd2.copyPixels(bmd1, rect, pt);
   *
   * var bm1:Bitmap = new Bitmap(bmd1);
   * this.addChild(bm1);
   * var bm2:Bitmap = new Bitmap(bmd2);
   * this.addChild(bm2);
   * bm2.x = 50;
   * </listing>
   */
  "public function copyPixels",function copyPixels(sourceBitmapData/*:BitmapData*/, sourceRect/*:Rectangle*/, destPoint/*:Point*/,
                             alphaBitmapData/*:BitmapData = null*/, alphaPoint/*:Point = null*/, mergeAlpha/*:Boolean = false*/)/*:void*/ {switch(arguments.length){case 0:case 1:case 2:case 3:alphaBitmapData = null;case 4:alphaPoint = null;case 5:mergeAlpha = false;}
    var context/*:CanvasRenderingContext2D*/;
    var destRect/*:Rectangle*/ = new flash.geom.Rectangle(destPoint.x, destPoint.y, sourceRect.width, sourceRect.height);
    destRect = destRect.intersection(this.rect);
    destRect.width = Math.floor(destRect.width);
    destRect.height = Math.floor(destRect.height);
    if (destRect.width > 0 && destRect.height > 0) {
      var sx/*:Number*/ = sourceRect.x + (destRect.left - destPoint.x);
      var sy/*:Number*/ = sourceRect.y + (destRect.top - destPoint.y);
      if (!sourceBitmapData.isCanvas$1) {
        if (destRect.equals(this.rect) && (!this.isCanvas$1 || !mergeAlpha)) {
          // the whole Bitmap is to become a copy of (a clipping of) the source bitmap
          this._fillColor$1 = sourceBitmapData._fillColor$1;
          this._alpha$1 = sourceBitmapData._alpha$1;
          this.image$1 = sourceBitmapData.image$1;
          this.imageOffsetX$1 = sx + sourceBitmapData.imageOffsetX$1;
          this.imageOffsetY$1 = sy + sourceBitmapData.imageOffsetY$1;
          if (this.elem$1) {
            this.asDiv(); // updates existing div
          }
        } else {
          // only part of this BitmapData is painted from the source, or we paint transparently onto an existing canvas:
          context = this.getContext$1(); // if not already one, become a canvas
          // clear destination rectangle with source background color first:
          if (sourceBitmapData._alpha$1 > 0) {
            context.fillStyle = flash.display.Graphics.toRGBA(sourceBitmapData._fillColor$1, mergeAlpha ? sourceBitmapData._alpha$1 : 1);
            context.fillRect(destRect.x, destRect.y, destRect.width, destRect.height);
          }
          if (sourceBitmapData.image$1) {
            // then, draw source image onto destination rectangle:
            context.drawImage(sourceBitmapData.image$1, sx + sourceBitmapData.imageOffsetX$1, sy + sourceBitmapData.imageOffsetY$1,
              destRect.width, destRect.height,
              destRect.left, destRect.top, destRect.width, destRect.height);
          }
        }
      } else {
        context = this.getContext$1();
        if (mergeAlpha) {
          // putImageData() does not support alpha channel, so use drawImage():
          context.drawImage(sourceBitmapData.asCanvas$1(), sx, sy, destRect.width, destRect.height,
            destRect.x, destRect.y, destRect.width, destRect.height);
        } else {
          var imageData/*:ImageData*/ = sourceBitmapData.getContext$1().getImageData(sx, sy, destRect.width, destRect.height);
          context.putImageData(imageData, destRect.x, destRect.y);
        }
      }
    }
  },

  /**
   * Frees memory that is used to store the BitmapData object.
   * <p>When the <code>dispose()</code> method is called on an image, the width and height of the image are set to 0. All subsequent calls to methods or properties of this BitmapData instance fail, and an exception is thrown.</p>
   * <p><code>BitmapData.dispose()</code> releases the memory occupied by the actual bitmap data, immediately (a bitmap can consume up to 64 MB of memory). After using <code>BitmapData.dispose()</code>, the BitmapData object is no longer usable and the Flash runtime throws an exception if you call functions on the BitmapData object. However, <code>BitmapData.dispose()</code> does not garbage collect the BitmapData object (approximately 128 bytes); the memory occupied by the actual BitmapData object is released at the time the BitmapData object is collected by the garbage collector.</p>
   * @see flash.system.System#gc()
   *
   * @example The following example shows the effect of calling a method of a BitmapData object after a call to the <code>dispose()</code> method (an exception is thrown):
   * <listing>
   * import flash.display.BitmapData;
   *
   * var myBitmapData:BitmapData = new BitmapData(100, 80, false, 0x000000FF);
   * trace(myBitmapData.getPixel(1, 1)); // 255 == 0xFF
   *
   * myBitmapData.dispose();
   * try {
   *     trace(myBitmapData.getPixel(1, 1));
   * } catch (error:Error) {
   *     trace(error); // ArgumentError
   * }
   * </listing>
   */
  "public function dispose",function dispose()/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Draws the <code>source</code> display object onto the bitmap image, using the Flash runtime vector renderer. You can specify <code>matrix</code>, <code>colorTransform</code>, <code>blendMode</code>, and a destination <code>clipRect</code> parameter to control how the rendering performs. Optionally, you can specify whether the bitmap should be smoothed when scaled (this works only if the source object is a BitmapData object).
   * <p>This method directly corresponds to how objects are drawn with the standard vector renderer for objects in the authoring tool interface.</p>
   * <p>The source display object does not use any of its applied transformations for this call. It is treated as it exists in the library or file, with no matrix transform, no color transform, and no blend mode. To draw a display object (such as a movie clip) by using its own transform properties, you can copy its <code>transform</code> property object to the <code>transform</code> property of the Bitmap object that uses the BitmapData object.</p>
   * <p>This method is supported over RTMP in Flash Player 9.0.115.0 and later and in Adobe AIR. You can control access to streams on Flash Media Server in a server-side script. For more information, see the <code>Client.audioSampleAccess</code> and <code>Client.videoSampleAccess</code> properties in <a href="http://www.adobe.com/go/documentation"><i>Server-Side ActionScript Language Reference for Adobe Flash Media Server</i></a>.</p>
   * <p>If the source object and (in the case of a Sprite or MovieClip object) all of its child objects do not come from the same domain as the caller, or are not in a content that is accessible to the caller by having called the <code>Security.allowDomain()</code> method, a call to the <code>draw()</code> throws a SecurityError exception. This restriction does not apply to AIR content in the application security sandbox.</p>
   * <p>There are also restrictions on using a loaded bitmap image as the <code>source</code>. A call to the <code>draw()</code> method is successful if the loaded image comes from the same domain as the caller. Also, a cross-domain policy file on the image's server can grant permission to the domain of the SWF content calling the <code>draw()</code> method. In this case, you must set the <code>checkPolicyFile</code> property of a LoaderContext object, and use this object as the <code>context</code> parameter when calling the <code>load()</code> method of the Loader object used to load the image. These restrictions do not apply to AIR content in the application security sandbox.</p>
   * <p>On Windows, the <code>draw()</code> method cannot capture SWF content embedded in an HTML page in an HTMLLoader object in Adobe AIR.</p>
   * <p>The <code>draw()</code> method cannot capture PDF content in Adobe AIR. Nor can it capture or SWF content embedded in HTML in which the <code>wmode</code> attribute is set to <code>"window"</code> in Adobe AIR.</p>
   * @param source The display object or BitmapData object to draw to the BitmapData object. (The DisplayObject and BitmapData classes implement the IBitmapDrawable interface.)
   * @param matrix A Matrix object used to scale, rotate, or translate the coordinates of the bitmap. If you do not want to apply a matrix transformation to the image, set this parameter to an identity matrix, created with the default <code>new Matrix()</code> constructor, or pass a <code>null</code> value.
   * @param colorTransform A ColorTransform object that you use to adjust the color values of the bitmap. If no object is supplied, the bitmap image's colors are not transformed. If you must pass this parameter but you do not want to transform the image, set this parameter to a ColorTransform object created with the default <code>new ColorTransform()</code> constructor.
   * @param blendMode A string value, from the flash.display.BlendMode class, specifying the blend mode to be applied to the resulting bitmap.
   * @param clipRect A Rectangle object that defines the area of the source object to draw. If you do not supply this value, no clipping occurs and the entire source object is drawn.
   * @param smoothing A Boolean value that determines whether a BitmapData object is smoothed when scaled or rotated, due to a scaling or rotation in the <code>matrix</code> parameter. The <code>smoothing</code> parameter only applies if the <code>source</code> parameter is a BitmapData object. With <code>smoothing</code> set to <code>false</code>, the rotated or scaled BitmapData image can appear pixelated or jagged. For example, the following two images use the same BitmapData object for the <code>source</code> parameter, but the <code>smoothing</code> parameter is set to <code>true</code> on the left and <code>false</code> on the right:
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/bitmapData_draw_smoothing.jpg" /></p>
   * <p>Drawing a bitmap with <code>smoothing</code> set to <code>true</code> takes longer than doing so with <code>smoothing</code> set to <code>false</code>.</p>
   *
   * @throws ArgumentError The <code>source</code> parameter is not a BitmapData or DisplayObject object.
   * @throws SecurityError The <code>source</code> object and (in the case of a Sprite or MovieClip object) all of its child objects do not come from the same domain as the caller, or are not in a content that is accessible to the caller by having called the <code>Security.allowDomain()</code> method. This restriction does not apply to AIR content in the application security sandbox.
   * @throws ArgumentError The source is null or not a valid IBitmapDrawable object.
   *
   * @see BlendMode
   * @see flash.geom.ColorTransform
   * @see flash.geom.Matrix
   * @see flash.system.JPEGLoaderContext
   *
   * @example The following example shows how to draw a TextField object to a BitmapData object:
   * <listing>
   * import flash.display.Bitmap;
   * import flash.display.BitmapData;
   * import flash.text.TextField;
   *
   * var tf:TextField = new TextField();
   * tf.text = "bitmap text";
   *
   * var myBitmapData:BitmapData = new BitmapData(80, 20);
   * myBitmapData.draw(tf);
   * var bmp:Bitmap = new Bitmap(myBitmapData);
   * this.addChild(bmp);
   * </listing>
   */
  "public function draw",function draw(source/*:IBitmapDrawable*/, matrix/*:Matrix = null*/, colorTransform/*:ColorTransform = null*/,
                       blendMode/*:String = null*/, clipRect/*:Rectangle = null*/, smoothing/*:Boolean = false*/)/*:void*/ {switch(arguments.length){case 0:case 1:matrix = null;case 2:colorTransform = null;case 3:blendMode = null;case 4:clipRect = null;case 5:smoothing = false;}
    var bitmapData/*:BitmapData*/;
    if (is(source,  flash.display.Bitmap)) {
      bitmapData =/* flash.display.Bitmap*/(source).bitmapData;
    } else {
      bitmapData =as( source,  flash.display.BitmapData);
    }
    var element/*:HTMLElement*/ = bitmapData ?
      bitmapData.image$1 || bitmapData.elem$1 || bitmapData.asDiv() :/*
      flash.display.DisplayObject*/(source).getElement();
    var context/*:CanvasRenderingContext2D*/ = this.getContext$1();
    if (matrix) {
      context.save();
      context.setTransform(matrix.a, matrix.b, matrix.c, matrix.d, matrix.tx, matrix.ty);
    }
    if (is(element,  js.HTMLImageElement) ||is( element,  js.HTMLCanvasElement)) {
      context.drawImage(element, 0, 0);
    } else {
      if (element.style.backgroundColor) {
        context.fillStyle = element.style.backgroundColor;
        context.fillRect(0, 0, source['width'], source['height']);
      }
      var text/*:String*/ = element['textContent'];
      if (text) {
        context.fillStyle = element.style.color;
        context.font = element.style.font;
        context.textBaseline = "top";
        context.fillText(text, 0, 0);
      }
    }
    if (matrix) {
      context.restore();
    }
  },

  /**
   * Fills a rectangular area of pixels with a specified ARGB color.
   * @param rect The rectangular area to fill.
   * @param color The ARGB color value that fills the area. ARGB colors are often specified in hexadecimal format; for example, 0xFF336699.
   *
   * @throws TypeError The rect is null.
   *
   * @see flash.geom.Rectangle
   *
   * @example The following example shows how to fill a rectangular region of a BitmapData object with blue:
   * <listing>
   * import flash.display.Bitmap;
   * import flash.display.BitmapData;
   * import flash.geom.Rectangle;
   *
   * var myBitmapData:BitmapData = new BitmapData(40, 40, false, 0x0000FF00);
   *
   * var rect:Rectangle = new Rectangle(0, 0, 20, 20);
   * myBitmapData.fillRect(rect, 0x0000FF);
   *
   * var bm:Bitmap = new Bitmap(myBitmapData);
   * addChild(bm);
   * </listing>
   */
  "public function fillRect",function fillRect(rect/*:Rectangle*/, color/*:uint*/)/*:void*/ {
    var alpha/*:uint*/ = (color >> 24 & 0xFF) / 0xFF;
    color = color & 0xFFFFFF;
    if (!this.isCanvas$1 && rect.equals(this.rect)) { // TODO: what about alpha != 1?
      this._fillColor$1 = color;
      this._alpha$1 = alpha;
      this.image$1 = null;
      if (this.elem$1) {
        this.asDiv();
      }
      return;
    }
    var context/*:CanvasRenderingContext2D*/ = this.getContext$1();
    context.save();
    context.setTransform(1, 0, 0, 1, 0, 0);
    // TODO: which other context attributes to reset?
    if (alpha == 0) {
      // IE9 does not (yet?) support globalCompositeOperation, but at least we can clear:
      context.clearRect(rect.x, rect.y, rect.width, rect.height);
    } else {
      context.fillStyle = "rgba("+
        [color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, alpha]
          .join(",")+")";
      context.globalCompositeOperation = "copy";
      context.fillRect(rect.x, rect.y, rect.width, rect.height);
    }
    context.restore();
    context.globalCompositeOperation = "source-over";
  },

  /**
   * Performs a flood fill operation on an image starting at an (<i>x</i>, <i>y</i>) coordinate and filling with a certain color. The <code>floodFill()</code> method is similar to the paint bucket tool in various paint programs. The color is an ARGB color that contains alpha information and color information.
   * @param x The <i>x</i> coordinate of the image.
   * @param y The <i>y</i> coordinate of the image.
   * @param color The ARGB color to use as a fill.
   *
   * @example The following example shows how to fill a region of a BitmapData object � that is, the region surrounding the pixel defined by the point <code>(10, 10)</code> iin which all colors march the color at that point � with red
   * <listing>
   * import flash.display.Bitmap;
   * import flash.display.BitmapData;
   * import flash.geom.Rectangle;
   *
   * var myBitmapData:BitmapData = new BitmapData(40, 40, false, 0x0000FF00);
   *
   * var rect:Rectangle = new Rectangle(0, 0, 20, 20);
   * myBitmapData.fillRect(rect, 0x000000FF);
   * rect = new Rectangle(15, 15, 25, 25);
   * myBitmapData.fillRect(rect, 0x000000FF);
   *
   * myBitmapData.floodFill(10, 10, 0x00FF0000);
   *
   * var bm:Bitmap = new Bitmap(myBitmapData);
   * addChild(bm);
   * </listing>
   */
  "public function floodFill",function floodFill(x/*:int*/, y/*:int*/, color/*:uint*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Determines the destination rectangle that the <code>applyFilter()</code> method call affects, given a BitmapData object, a source rectangle, and a filter object.
   * <p>For example, a blur filter normally affects an area larger than the size of the original image. A 100 x 200 pixel image that is being filtered by a default BlurFilter instance, where <code>blurX = blurY = 4</code> generates a destination rectangle of <code>(-2,-2,104,204)</code>. The <code>generateFilterRect()</code> method lets you find out the size of this destination rectangle in advance so that you can size the destination image appropriately before you perform a filter operation.</p>
   * <p>Some filters clip their destination rectangle based on the source image size. For example, an inner <code>DropShadow</code> does not generate a larger result than its source image. In this API, the BitmapData object is used as the source bounds and not the source <code>rect</code> parameter.</p>
   * @param sourceRect A rectangle defining the area of the source image to use as input.
   * @param filter A filter object that you use to calculate the destination rectangle.
   *
   * @return A destination rectangle computed by using an image, the <code>sourceRect</code> parameter, and a filter.
   *
   * @throws TypeError The sourceRect or filter are null.
   *
   * @example The following example shows how you can use the <code>generateFilterRect()</code> method to determine the rectangular area that the result of a blur filter will occupy. The results of the <code>generateFilterRect()</code> method are output by the <code>trace()</code> function:
   * <listing>
   * import flash.display.Bitmap;
   * import flash.display.BitmapData;
   * import flash.geom.Point;
   * import flash.geom.Rectangle;
   * import flash.filters.BlurFilter;
   *
   * var bmd:BitmapData = new BitmapData(80, 30, false, 0xFFCC00);
   * var rect:Rectangle = new Rectangle(10, 10, 40, 10);
   * bmd.fillRect(rect, 0xFF0000);
   *
   * var pt:Point = new Point(10, 10);
   * var filter:BlurFilter = new BlurFilter();
   *
   * trace(bmd.generateFilterRect(rect, filter));
   * // (x=8, y=8, w=44, h=14)
   *
   * bmd.applyFilter(bmd, rect, pt, filter);
   * var bm:Bitmap = new Bitmap(bmd);
   * addChild(bm);
   * </listing>Note that the <code>generateFilterRect()</code> method does not apply the filter. Call the <code>applyFilter()</code> method to apply the filter.
   */
  "public function generateFilterRect",function generateFilterRect(sourceRect/*:Rectangle*/, filter/*:BitmapFilter*/)/*:Rectangle*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Determines a rectangular region that either fully encloses all pixels of a specified color within the bitmap image (if the <code>findColor</code> parameter is set to <code>true</code>) or fully encloses all pixels that do not include the specified color (if the <code>findColor</code> parameter is set to <code>false</code>).
   * <p>For example, if you have a source image and you want to determine the rectangle of the image that contains a nonzero alpha channel, pass <code>{mask: 0xFF000000, color: 0x00000000}</code> as parameters. If the <code>findColor</code> parameter is set to <code>true</code>, the entire image is searched for the bounds of pixels for which <code>(value & mask) == color</code> (where <code>value</code> is the color value of the pixel). If the <code>findColor</code> parameter is set to <code>false</code>, the entire image is searched for the bounds of pixels for which <code>(value & mask) != color</code> (where <code>value</code> is the color value of the pixel). To determine white space around an image, pass <code>{mask: 0xFFFFFFFF, color: 0xFFFFFFFF}</code> to find the bounds of nonwhite pixels.</p>
   * @param mask A hexadecimal value, specifying the bits of the ARGB color to consider. The color value is combined with this hexadecimal value, by using the <code>&</code> (bitwise AND) operator.
   * @param color A hexadecimal value, specifying the ARGB color to match (if <code>findColor</code> is set to <code>true</code>) or <i>not</i> to match (if <code>findColor</code> is set to <code>false</code>).
   * @param findColor If the value is set to <code>true</code>, returns the bounds of a color value in an image. If the value is set to <code>false</code>, returns the bounds of where this color doesn't exist in an image.
   *
   * @return The region of the image that is the specified color.
   *
   * @example The following example creates a BitmapData object with red in the top half of its pixels. It then calls the <code>getColorBoundsRect()</code> method to determine the rectangle in which pixels are red (0xFF0000), and then it calls the same method to determine the rectangle in which pixels are not red (by setting the <code>findColor</code> parameter to <code>false</code>:
   * <listing>
   * import flash.display.Bitmap;
   * import flash.display.BitmapData;
   * import flash.geom.Rectangle;
   *
   * var bmd:BitmapData = new BitmapData(80, 40, false, 0xFFFFFF);
   * var rect:Rectangle = new Rectangle(0, 0, 80, 20);
   * bmd.fillRect(rect, 0xFF0000);
   *
   * var maskColor:uint = 0xFFFFFF;
   * var color:uint = 0xFF0000;
   * var redBounds:Rectangle = bmd.getColorBoundsRect(maskColor, color, true);
   * trace(redBounds); // (x=0, y=0, w=80, h=20)
   *
   * var notRedBounds:Rectangle = bmd.getColorBoundsRect(maskColor, color, false);
   * trace(notRedBounds); // (x=0, y=20, w=80, h=20)
   *
   * var bm:Bitmap = new Bitmap(bmd);
   * addChild(bm);
   * </listing>
   */
  "public function getColorBoundsRect",function getColorBoundsRect(mask/*:uint*/, color/*:uint*/, findColor/*:Boolean = true*/)/*:Rectangle*/ {switch(arguments.length){case 0:case 1:case 2:findColor = true;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns an integer that represents an RGB pixel value from a BitmapData object at a specific point (<i>x</i>, <i>y</i>). The <code>getPixel()</code> method returns an unmultiplied pixel value. No alpha information is returned.
   * <p>All pixels in a BitmapData object are stored as premultiplied color values. A premultiplied image pixel has the red, green, and blue color channel values already multiplied by the alpha data. For example, if the alpha value is 0, the values for the RGB channels are also 0, independent of their unmultiplied values. This loss of data can cause some problems when you perform operations. All BitmapData methods take and return unmultiplied values. The internal pixel representation is converted from premultiplied to unmultiplied before it is returned as a value. During a set operation, the pixel value is premultiplied before the raw image pixel is set.</p>
   * @param x The <i>x</i> position of the pixel.
   * @param y The <i>y</i> position of the pixel.
   *
   * @return A number that represents an RGB pixel value. If the (<i>x</i>, <i>y</i>) coordinates are outside the bounds of the image, the method returns 0.
   *
   * @see #getPixel32()
   * @see #setPixel()
   *
   * @example The following example creates a BitmapData object filled with red, then uses the <code>getPixel()</code> method to determine the color value in the upper-left pixel:
   * <listing>
   * import flash.display.BitmapData;
   *
   * var bmd:BitmapData = new BitmapData(80, 40, false, 0xFF0000);
   *
   * var pixelValue:uint = bmd.getPixel(0, 0);
   * trace(pixelValue.toString(16)); // ff0000;
   * </listing>
   */
  "public function getPixel",function getPixel(x/*:int*/, y/*:int*/)/*:uint*/ {
    if (this.rect.contains(x, y)) {
      var data/* : Array*/ = this.getContext$1().getImageData(x, y, 1, 1).data;
      return data[0] << 16 | data[1] << 8 | data[2];
    }
    return 0;
  },

  /**
   * Returns an ARGB color value that contains alpha channel data and RGB data. This method is similar to the <code>getPixel()</code> method, which returns an RGB color without alpha channel data.
   * <p>All pixels in a BitmapData object are stored as premultiplied color values. A premultiplied image pixel has the red, green, and blue color channel values already multiplied by the alpha data. For example, if the alpha value is 0, the values for the RGB channels are also 0, independent of their unmultiplied values. This loss of data can cause some problems when you perform operations. All BitmapData methods take and return unmultiplied values. The internal pixel representation is converted from premultiplied to unmultiplied before it is returned as a value. During a set operation, the pixel value is premultiplied before the raw image pixel is set.</p>
   * @param x The <i>x</i> position of the pixel.
   * @param y The <i>y</i> position of the pixel.
   *
   * @return A number representing an ARGB pixel value. If the (<i>x</i>, <i>y</i>) coordinates are outside the bounds of the image, 0 is returned.
   *
   * @see #getPixel()
   * @see #setPixel32()
   *
   * @example The following example creates a BitmapData object filled with a color, then uses the <code>getPixel32()</code> method to determine the color value in the upper-left pixel, and then determines the hexidecimal values for each color component (alpha, red, green, and blue):
   * <listing>
   * import flash.display.BitmapData;
   *
   * var bmd:BitmapData = new BitmapData(80, 40, true, 0xFF44AACC);
   *
   * var pixelValue:uint = bmd.getPixel32(0, 0);
   * var alphaValue:uint = pixelValue >> 24 & 0xFF;
   * var red:uint = pixelValue >> 16 & 0xFF;
   * var green:uint = pixelValue >> 8 & 0xFF;
   * var blue:uint = pixelValue & 0xFF;
   *
   * trace(alphaValue.toString(16)); // ff
   * trace(red.toString(16)); // 44
   * trace(green.toString(16)); // aa
   * trace(blue.toString(16)); // cc
   * </listing>
   */
  "public function getPixel32",function getPixel32(x/*:int*/, y/*:int*/)/*:uint*/ {
    if (this.rect.contains(x, y)) {
      var data/* : Array*/ = this.getContext$1().getImageData(x, y, 1, 1).data;
      return data[0] << 16 | data[1] << 8 | data[2] | data[3] << 24;
    }
    return 0;
  },

  /**
   * Generates a byte array from a rectangular region of pixel data. Writes an unsigned integer (a 32-bit unmultiplied pixel value) for each pixel into the byte array.
   * @param rect A rectangular area in the current BitmapData object.
   *
   * @return A ByteArray representing the pixels in the given Rectangle.
   *
   * @throws TypeError The rect is null.
   *
   * @see flash.utils.ByteArray
   *
   * @example The following example creates a BitmapData object filled with random noise pixels, then uses the <code>getPixels()</code> method to fill a ByteArray object with the pixel values of the BitmapData object
   * <listing>
   * import flash.display.BitmapData;
   * import flash.geom.Rectangle;
   * import flash.utils.ByteArray;
   *
   * var bmd:BitmapData = new BitmapData(80, 40, true);
   * var seed:int = int(Math.random() * int.MAX_VALUE);
   * bmd.noise(seed);
   *
   * var bounds:Rectangle = new Rectangle(0, 0, bmd.width, bmd.height);
   * var pixels:ByteArray = bmd.getPixels(bounds);
   * </listing>
   */
  "public function getPixels",function getPixels(rect/*:Rectangle*/)/*:ByteArray*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Performs pixel-level hit detection between one bitmap image and a point, rectangle, or other bitmap image. A hit is defined as an overlap of a point or rectangle over an opaque pixel, or two overlapping opaque pixels. No stretching, rotation, or other transformation of either object is considered when the hit test is performed.
   * <p>If an image is an opaque image, it is considered a fully opaque rectangle for this method. Both images must be transparent images to perform pixel-level hit testing that considers transparency. When you are testing two transparent images, the alpha threshold parameters control what alpha channel values, from 0 to 255, are considered opaque.</p>
   * @param firstPoint A position of the upper-left corner of the BitmapData image in an arbitrary coordinate space. The same coordinate space is used in defining the <code>secondBitmapPoint</code> parameter.
   * @param firstAlphaThreshold The smallest alpha channel value that is considered opaque for this hit test.
   * @param secondObject A Rectangle, Point, Bitmap, or BitmapData object.
   * @param secondBitmapDataPoint A point that defines a pixel location in the second BitmapData object. Use this parameter only when the value of <code>secondObject</code> is a BitmapData object.
   * @param secondAlphaThreshold The smallest alpha channel value that is considered opaque in the second BitmapData object. Use this parameter only when the value of <code>secondObject</code> is a BitmapData object and both BitmapData objects are transparent.
   *
   * @return A value of <code>true</code> if a hit occurs; otherwise, <code>false</code>.
   *
   * @throws ArgumentError The <code>secondObject</code> parameter is not a Point, Rectangle, Bitmap, or BitmapData object.
   * @throws TypeError The firstPoint is null.
   *
   * @example The following example creates a BitmapData object that is only opaque in a rectangular region (20, 20, 40, 40) and calls the <code>hitTest()</code> method with a Point object as the <code>secondObject</code>. In the first call, the Point object defines the upper-left corner of the BitmapData object, which is not opaque, and in the second call, the Point object defines the center of the BitmapData object, which is opaque.
   * <listing>
   * import flash.display.BitmapData;
   * import flash.geom.Rectangle;
   * import flash.geom.Point;
   *
   * var bmd1:BitmapData = new BitmapData(80, 80, true, 0x00000000);
   * var rect:Rectangle = new Rectangle(20, 20, 40, 40);
   * bmd1.fillRect(rect, 0xFF0000FF);
   *
   * var pt1:Point = new Point(1, 1);
   * trace(bmd1.hitTest(pt1, 0xFF, pt1)); // false
   * var pt2:Point = new Point(40, 40);
   * trace(bmd1.hitTest(pt1, 0xFF, pt2)); // true
   * </listing>
   */
  "public function hitTest",function hitTest(firstPoint/*:Point*/, firstAlphaThreshold/*:uint*/, secondObject/*:Object*/, secondBitmapDataPoint/*:Point = null*/, secondAlphaThreshold/*:uint = 1*/)/*:Boolean*/ {switch(arguments.length){case 0:case 1:case 2:case 3:secondBitmapDataPoint = null;case 4:secondAlphaThreshold = 1;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Locks an image so that any objects that reference the BitmapData object, such as Bitmap objects, are not updated when this BitmapData object changes. To improve performance, use this method along with the <code>unlock()</code> method before and after numerous calls to the <code>setPixel()</code> or <code>setPixel32()</code> method.
   * @see #setPixel()
   * @see #setPixel32()
   * @see #unlock()
   *
   * @example The following example creates a BitmapData object based on the <code>bitmapData</code> property of a Bitmap object, <code>picture</code>. It then calls the <code>lock()</code> method before calling a complicated custom function, <code>complexTransformation()</code>, that modifies the BitmapData object. (The <code>picture</code> object and the <code>complexTransformation()</code> function are not defined in this example.) Even if the <code>complexTransformation()</code> function updates the <code>bitmapData</code> property of the <code>picture</code> object, changes are not reflected until the code calls the <code>unlock()</code> method on the <code>bitmapData</code> object:
   * <listing>
   * import flash.display.BitmapData;
   *
   * var bitmapData:BitmapData = picture.bitmapData;
   * bitmapData.lock();
   * bitmapData = complexTransformation(bitmapData);
   * bitmapData.unlock();
   * picture.bitmapData = bitmapData;
   * </listing>
   */
  "public function lock",function lock()/*:void*/ {
    // TODO: anything we can do here? Maybe create a reusable ImageData object and destroy it on unlock()?
  },

  /**
   * Performs per-channel blending from a source image to a destination image. For each channel and each pixel, a new value is computed based on the channel values of the source and destination pixels. For example, in the red channel, the new value is computed as follows (where <code>redSrc</code> is the red channel value for a pixel in the source image and <code>redDest</code> is the red channel value at the corresponding pixel of the destination image):
   * <p><code>new redDest = [(redSrc * redMultiplier) + (redDest * (256 - redMultiplier))] / 256;</code></p>
   * <p>The <code>redMultiplier</code>, <code>greenMultiplier</code>, <code>blueMultiplier</code>, and <code>alphaMultiplier</code> values are the multipliers used for each color channel. Use a hexadecimal value ranging from <code>0</code> to <code>0x100</code> (256) where <code>0</code> specifies the full value from the destination is used in the result, <code>0x100</code> specifies the full value from the source is used, and numbers in between specify a blend is used (such as <code>0x80</code> for 50%).</p>
   * @param sourceBitmapData The input bitmap image to use. The source image can be a different BitmapData object, or it can refer to the current BitmapData object.
   * @param sourceRect A rectangle that defines the area of the source image to use as input.
   * @param destPoint The point within the destination image (the current BitmapData instance) that corresponds to the upper-left corner of the source rectangle.
   * @param redMultiplier A hexadecimal uint value by which to multiply the red channel value.
   * @param greenMultiplier A hexadecimal uint value by which to multiply the green channel value.
   * @param blueMultiplier A hexadecimal uint value by which to multiply the blue channel value.
   * @param alphaMultiplier A hexadecimal uint value by which to multiply the alpha transparency value.
   *
   * @throws TypeError The sourceBitmapData, sourceRect or destPoint are null.
   *
   * @example The following example creates two BitmapData objects. Both are 100 x 80 pixels in size. The first is filled with green and the second is filled with red. The code calls the <code>merge()</code> method, merging the second BitmapData pixels into the first BitmapData object, but only on a specified rectangular area:
   * <listing>
   * import flash.display.Bitmap;
   * import flash.display.BitmapData;
   * import flash.geom.Rectangle;
   * import flash.geom.Point;
   *
   * var bmd1:BitmapData = new BitmapData(100, 80, true, 0xFF00FF00);
   * var bmd2:BitmapData = new BitmapData(100, 80, true, 0xFFFF0000);
   * var rect:Rectangle = new Rectangle(0, 0, 20, 20);
   * var pt:Point = new Point(20, 20);
   * var mult:uint = 0x80; // 50%
   * bmd1.merge(bmd2, rect, pt, mult, mult, mult, mult);
   *
   * var bm1:Bitmap = new Bitmap(bmd1);
   * addChild(bm1);
   * var bm2:Bitmap = new Bitmap(bmd2);
   * addChild(bm2);
   * bm2.x = 110;
   * </listing>
   */
  "public function merge",function merge(sourceBitmapData/*:BitmapData*/, sourceRect/*:Rectangle*/, destPoint/*:Point*/, redMultiplier/*:uint*/, greenMultiplier/*:uint*/, blueMultiplier/*:uint*/, alphaMultiplier/*:uint*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Fills an image with pixels representing random noise.
   * @param randomSeed The random seed number to use. If you keep all other parameters the same, you can generate different pseudo-random results by varying the random seed value. The noise function is a mapping function, not a true random-number generation function, so it creates the same results each time from the same random seed.
   * @param low The lowest value to generate for each channel (0 to 255).
   * @param high The highest value to generate for each channel (0 to 255).
   * @param channelOptions A number that can be a combination of any of the four color channel values (<code>BitmapDataChannel.RED</code>, <code>BitmapDataChannel.BLUE</code>, <code>BitmapDataChannel.GREEN</code>, and <code>BitmapDataChannel.ALPHA</code>). You can use the logical OR operator (<code>|</code>) to combine channel values.
   * @param grayScale A Boolean value. If the value is <code>true</code>, a grayscale image is created by setting all of the color channels to the same value. The alpha channel selection is not affected by setting this parameter to <code>true</code>.
   *
   * @see BitmapDataChannel#RED
   * @see BitmapDataChannel#BLUE
   * @see BitmapDataChannel#GREEN
   * @see BitmapDataChannel#ALPHA
   *
   * @example The following example creates two BitmapData objects and calls the <code>noise()</code> method on both. However, the <code>grayscale</code> parameter is set to <code>false</code> for the call to the <code>noise()</code> method of the first object, and it is set to <code>true</code> for the call to the <code>noise()</code> method of the second object:
   * <listing>
   * import flash.display.Bitmap;
   * import flash.display.BitmapData;
   * import flash.display.BitmapDataChannel;
   *
   * var bmd1:BitmapData = new BitmapData(80, 80);
   * var bmd2:BitmapData = new BitmapData(80, 80);
   *
   * var seed:int = int(Math.random() * int.MAX_VALUE);
   * bmd1.noise(seed, 0, 0xFF, BitmapDataChannel.RED, false);
   * bmd2.noise(seed, 0, 0xFF, BitmapDataChannel.RED, true);
   *
   * var bm1:Bitmap = new Bitmap(bmd1);
   * this.addChild(bm1);
   * var bm2:Bitmap = new Bitmap(bmd2);
   * this.addChild(bm2);
   * bm2.x = 90;
   * </listing>
   */
  "public function noise",function noise(randomSeed/*:int*/, low/*:uint = 0*/, high/*:uint = 255*/, channelOptions/*:uint = 7*/, grayScale/*:Boolean = false*/)/*:void*/ {switch(arguments.length){case 0:case 1:low = 0;case 2:high = 255;case 3:channelOptions = 7;case 4:grayScale = false;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Remaps the color channel values in an image that has up to four arrays of color palette data, one for each channel.
   * <p>Flash runtimes use the following steps to generate the resulting image:</p><ol>
   * <li>After the red, green, blue, and alpha values are computed, they are added together using standard 32-bit-integer arithmetic.</li>
   * <li>The red, green, blue, and alpha channel values of each pixel are extracted into separate 0 to 255 values. These values are used to look up new color values in the appropriate array: <code>redArray</code>, <code>greenArray</code>, <code>blueArray</code>, and <code>alphaArray</code>. Each of these four arrays should contain 256 values.</li>
   * <li>After all four of the new channel values are retrieved, they are combined into a standard ARGB value that is applied to the pixel.</li></ol>
   * <p>Cross-channel effects can be supported with this method. Each input array can contain full 32-bit values, and no shifting occurs when the values are added together. This routine does not support per-channel clamping.</p>
   * <p>If no array is specified for a channel, the color channel is copied from the source image to the destination image.</p>
   * <p>You can use this method for a variety of effects such as general palette mapping (taking one channel and converting it to a false color image). You can also use this method for a variety of advanced color manipulation algorithms, such as gamma, curves, levels, and quantizing.</p>
   * @param sourceBitmapData The input bitmap image to use. The source image can be a different BitmapData object, or it can refer to the current BitmapData instance.
   * @param sourceRect A rectangle that defines the area of the source image to use as input.
   * @param destPoint The point within the destination image (the current BitmapData object) that corresponds to the upper-left corner of the source rectangle.
   * @param redArray If <code>redArray</code> is not <code>null</code>, <code>red = redArray[source red value] else red = source rect value</code>.
   * @param greenArray If <code>greenArray</code> is not <code>null</code>, <code>green = greenArray[source green value] else green = source green value.</code>
   * @param blueArray If <code>blueArray</code> is not <code>null</code>, <code>blue = blueArray[source blue value] else blue = source blue value</code>.
   * @param alphaArray If <code>alphaArray</code> is not <code>null</code>, <code>alpha = alphaArray[source alpha value] else alpha = source alpha value</code>.
   *
   * @throws TypeError The sourceBitmapData, sourceRect or destPoint are null.
   *
   * @example The following example creates a green BitmapData object with a red center square, and then uses the <code>paletteMap()</code> method to swap red with green in the bottom rectangular half of the BitmapData object:
   * <listing>
   * import flash.display.Bitmap;
   * import flash.display.BitmapData;
   * import flash.geom.Rectangle;
   * import flash.geom.Point;
   *
   * var myBitmapData:BitmapData = new BitmapData(80, 80, false, 0x00FF0000);
   * myBitmapData.fillRect(new Rectangle(20, 20, 40, 40), 0x0000FF00);
   *
   * var redArray:Array = new Array(256);
   * var greenArray:Array = new Array(256);
   *
   * for(var i:uint = 0; i < 255; i++) {
   *     redArray[i] = 0x00000000;
   *     greenArray[i] = 0x00000000;
   * }
   *
   * redArray[0xFF] = 0x0000FF00;
   * greenArray[0xFF] = 0x00FF0000;
   *
   * var bottomHalf:Rectangle = new Rectangle(0, 0, 100, 40);
   * var pt:Point = new Point(0, 0);
   * myBitmapData.paletteMap(myBitmapData, bottomHalf, pt, redArray, greenArray);
   *
   * var bm1:Bitmap = new Bitmap(myBitmapData);
   * addChild(bm1);
   * </listing>
   */
  "public function paletteMap",function paletteMap(sourceBitmapData/*:BitmapData*/, sourceRect/*:Rectangle*/, destPoint/*:Point*/, redArray/*:Array = null*/, greenArray/*:Array = null*/, blueArray/*:Array = null*/, alphaArray/*:Array = null*/)/*:void*/ {switch(arguments.length){case 0:case 1:case 2:case 3:redArray = null;case 4:greenArray = null;case 5:blueArray = null;case 6:alphaArray = null;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Generates a Perlin noise image.
   * <p>The Perlin noise generation algorithm interpolates and combines individual random noise functions (called octaves) into a single function that generates more natural-seeming random noise. Like musical octaves, each octave function is twice the frequency of the one before it. Perlin noise has been described as a "fractal sum of noise" because it combines multiple sets of noise data with different levels of detail.</p>
   * <p>You can use Perlin noise functions to simulate natural phenomena and landscapes, such as wood grain, clouds, and mountain ranges. In most cases, the output of a Perlin noise function is not displayed directly but is used to enhance other images and give them pseudo-random variations.</p>
   * <p>Simple digital random noise functions often produce images with harsh, contrasting points. This kind of harsh contrast is not often found in nature. The Perlin noise algorithm blends multiple noise functions that operate at different levels of detail. This algorithm results in smaller variations among neighboring pixel values.</p>
   * @param baseX Frequency to use in the <i>x</i> direction. For example, to generate a noise that is sized for a 64 x 128 image, pass 64 for the <code>baseX</code> value.
   * @param baseY Frequency to use in the <i>y</i> direction. For example, to generate a noise that is sized for a 64 x 128 image, pass 128 for the <code>baseY</code> value.
   * @param numOctaves Number of octaves or individual noise functions to combine to create this noise. Larger numbers of octaves create images with greater detail. Larger numbers of octaves also require more processing time.
   * @param randomSeed The random seed number to use. If you keep all other parameters the same, you can generate different pseudo-random results by varying the random seed value. The Perlin noise function is a mapping function, not a true random-number generation function, so it creates the same results each time from the same random seed.
   * @param stitch A Boolean value. If the value is <code>true</code>, the method attempts to smooth the transition edges of the image to create seamless textures for tiling as a bitmap fill.
   * @param fractalNoise A Boolean value. If the value is <code>true</code>, the method generates fractal noise; otherwise, it generates turbulence. An image with turbulence has visible discontinuities in the gradient that can make it better approximate sharper visual effects like flames and ocean waves.
   * @param channelOptions A number that can be a combination of any of the four color channel values (<code>BitmapDataChannel.RED</code>, <code>BitmapDataChannel.BLUE</code>, <code>BitmapDataChannel.GREEN</code>, and <code>BitmapDataChannel.ALPHA</code>). You can use the logical OR operator (<code>|</code>) to combine channel values.
   * @param grayScale A Boolean value. If the value is <code>true</code>, a grayscale image is created by setting each of the red, green, and blue color channels to identical values. The alpha channel value is not affected if this value is set to <code>true</code>.
   * @param offsets An array of points that correspond to <i>x</i> and <i>y</i> offsets for each octave. By manipulating the offset values you can smoothly scroll the layers of a perlinNoise image. Each point in the offset array affects a specific octave noise function.
   *
   * @example The following example creates a 200 x 200 pixel BitmapData object that calls the <code>perlinNoise()</code> method to generate a red and blue watercolor effect:
   * <listing>
   * import flash.display.Bitmap;
   * import flash.display.BitmapData;
   *
   * var bmd:BitmapData = new BitmapData(200, 200, false, 0x00CCCCCC);
   *
   * var seed:Number = Math.floor(Math.random() * 10);
   * var channels:uint = BitmapDataChannel.RED | BitmapDataChannel.BLUE;
   * bmd.perlinNoise(100, 80, 6, seed, false, true, channels, false, null);
   *
   * var bm:Bitmap = new Bitmap(bmd);
   * addChild(bm);
   * </listing>
   */
  "public function perlinNoise",function perlinNoise(baseX/*:Number*/, baseY/*:Number*/, numOctaves/*:uint*/, randomSeed/*:int*/, stitch/*:Boolean*/, fractalNoise/*:Boolean*/, channelOptions/*:uint = 7*/, grayScale/*:Boolean = false*/, offsets/*:Array = null*/)/*:void*/ {switch(arguments.length){case 0:case 1:case 2:case 3:case 4:case 5:case 6:channelOptions = 7;case 7:grayScale = false;case 8:offsets = null;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Performs a pixel dissolve either from a source image to a destination image or by using the same image. Flash runtimes use a <code>randomSeed</code> value to generate a random pixel dissolve. The return value of the function must be passed in on subsequent calls to continue the pixel dissolve until it is finished.
   * <p>If the source image does not equal the destination image, pixels are copied from the source to the destination by using all of the properties. This process allows dissolving from a blank image into a fully populated image.</p>
   * <p>If the source and destination images are equal, pixels are filled with the <code>color</code> parameter. This process allows dissolving away from a fully populated image. In this mode, the destination <code>point</code> parameter is ignored.</p>
   * @param sourceBitmapData The input bitmap image to use. The source image can be a different BitmapData object, or it can refer to the current BitmapData instance.
   * @param sourceRect A rectangle that defines the area of the source image to use as input.
   * @param destPoint The point within the destination image (the current BitmapData instance) that corresponds to the upper-left corner of the source rectangle.
   * @param randomSeed The random seed to use to start the pixel dissolve.
   * @param numPixels The default is 1/30 of the source area (width x height).
   * @param fillColor An ARGB color value that you use to fill pixels whose source value equals its destination value.
   *
   * @return The new random seed value to use for subsequent calls.
   *
   * @throws TypeError The sourceBitmapData, sourceRect or destPoint are null.
   * @throws TypeError The numPixels value is negative
   *
   * @example The following example uses the <code>pixelDissolve()</code> method to convert a grey BitmapData object to a red one by dissolving 40 pixels at a time until all pixels have changed colors:
   * <listing>
   * import flash.display.BitmapData;
   * import flash.display.Bitmap;
   * import flash.geom.Point;
   * import flash.geom.Rectangle;
   * import flash.utils.Timer;
   * import flash.events.TimerEvent;
   *
   * var bmd:BitmapData = new BitmapData(100, 80, false, 0x00CCCCCC);
   * var bitmap:Bitmap = new Bitmap(bmd);
   * addChild(bitmap);
   *
   * var tim:Timer = new Timer(20);
   * tim.start();
   * tim.addEventListener(TimerEvent.TIMER, timerHandler);
   *
   * function timerHandler(event:TimerEvent):void {
   *     var randomNum:Number = Math.floor(Math.random() * int.MAX_VALUE);
   *     dissolve(randomNum);
   * }
   *
   * function dissolve(randomNum:Number):void {
   *     var rect:Rectangle = bmd.rect;
   *     var pt:Point = new Point(0, 0);
   *     var numberOfPixels:uint = 100;
   *     var red:uint = 0x00FF0000;
   *     bmd.pixelDissolve(bmd, rect, pt, randomNum, numberOfPixels, red);
   *     var grayRegion:Rectangle = bmd.getColorBoundsRect(0xFFFFFFFF, 0x00CCCCCC, true);
   *     if(grayRegion.width == 0 && grayRegion.height == 0 ) {
   *         tim.stop();
   *     }
   * }
   * </listing>
   */
  "public function pixelDissolve",function pixelDissolve(sourceBitmapData/*:BitmapData*/, sourceRect/*:Rectangle*/, destPoint/*:Point*/, randomSeed/*:int = 0*/, numPixels/*:int = 0*/, fillColor/*:uint = 0*/)/*:int*/ {switch(arguments.length){case 0:case 1:case 2:case 3:randomSeed = 0;case 4:numPixels = 0;case 5:fillColor = 0;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Scrolls an image by a certain (<i>x</i>, <i>y</i>) pixel amount. Edge regions outside the scrolling area are left unchanged.
   * @param x The amount by which to scroll horizontally.
   * @param y The amount by which to scroll vertically.
   *
   * @example The following example shows the effect of scrolling a Bitmap data object 40 pixels to the right:
   * <listing>
   * import flash.display.Bitmap;
   * import flash.display.BitmapData;
   * import flash.geom.Rectangle;
   *
   * var bmd:BitmapData = new BitmapData(80, 80, true, 0xFFCCCCCC);
   * var rect:Rectangle = new Rectangle(0, 0, 40, 40);
   * bmd.fillRect(rect, 0xFFFF0000);
   *
   * var bm:Bitmap = new Bitmap(bmd);
   * addChild(bm);
   *
   * trace (bmd.getPixel32(50, 20).toString(16)); // ffcccccccc
   *
   * bmd.scroll(30, 0);
   *
   * trace (bmd.getPixel32(50, 20).toString(16)); // ffff0000
   * </listing>
   */
  "public function scroll",function scroll(x/*:int*/, y/*:int*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Sets a single pixel of a BitmapData object. The current alpha channel value of the image pixel is preserved during this operation. The value of the RGB color parameter is treated as an unmultiplied color value.
   * <p><b>Note:</b> To increase performance, when you use the <code>setPixel()</code> or <code>setPixel32()</code> method repeatedly, call the <code>lock()</code> method before you call the <code>setPixel()</code> or <code>setPixel32()</code> method, and then call the <code>unlock()</code> method when you have made all pixel changes. This process prevents objects that reference this BitmapData instance from updating until you finish making the pixel changes.</p>
   * @param x The <i>x</i> position of the pixel whose value changes.
   * @param y The <i>y</i> position of the pixel whose value changes.
   * @param color The resulting RGB color for the pixel.
   *
   * @see #getPixel()
   * @see #setPixel32()
   * @see #lock()
   * @see #unlock()
   *
   * @example The following example uses the <code>setPixel()</code> method to draw a red line in a BitmapData object:
   * <listing>
   * import flash.display.Bitmap;
   * import flash.display.BitmapData;
   *
   * var bmd:BitmapData = new BitmapData(80, 80, false, 0xCCCCCC);
   *
   * for (var i:uint = 0; i < 80; i++) {
   *     var red:uint = 0xFF0000;
   *     bmd.setPixel(i, 40, red);
   * }
   *
   * var bm:Bitmap = new Bitmap(bmd);
   * addChild(bm);
   * </listing>
   */
  "public function setPixel",function setPixel(x/*:int*/, y/*:int*/, color/*:uint*/)/*:void*/ {
    if (this.rect.contains(x, y)) {
      var context/*:CanvasRenderingContext2D*/ = this.getContext$1();
      var imageData/*:ImageData*/ = context.createImageData(1, 1);
      imageData.data[0] = color >> 16 & 0xFF;
      imageData.data[1] = color >>  8 & 0xFF;
      imageData.data[2] = color       & 0xFF;
      imageData.data[3] = 0xFF;
      context.putImageData(imageData, x, y);
    }
  },

  /**
   * Sets the color and alpha transparency values of a single pixel of a BitmapData object. This method is similar to the <code>setPixel()</code> method; the main difference is that the <code>setPixel32()</code> method takes an ARGB color value that contains alpha channel information.
   * <p>All pixels in a BitmapData object are stored as premultiplied color values. A premultiplied image pixel has the red, green, and blue color channel values already multiplied by the alpha data. For example, if the alpha value is 0, the values for the RGB channels are also 0, independent of their unmultiplied values. This loss of data can cause some problems when you perform operations. All BitmapData methods take and return unmultiplied values. The internal pixel representation is converted from premultiplied to unmultiplied before it is returned as a value. During a set operation, the pixel value is premultiplied before the raw image pixel is set.</p>
   * <p><b>Note:</b> To increase performance, when you use the <code>setPixel()</code> or <code>setPixel32()</code> method repeatedly, call the <code>lock()</code> method before you call the <code>setPixel()</code> or <code>setPixel32()</code> method, and then call the <code>unlock()</code> method when you have made all pixel changes. This process prevents objects that reference this BitmapData instance from updating until you finish making the pixel changes.</p>
   * @param x The <i>x</i> position of the pixel whose value changes.
   * @param y The <i>y</i> position of the pixel whose value changes.
   * @param color The resulting ARGB color for the pixel. If the bitmap is opaque (not transparent), the alpha transparency portion of this color value is ignored.
   *
   * @see #setPixel()
   * @see #getPixel32()
   * @see #lock()
   * @see #unlock()
   *
   * @example The following example uses the <code>setPixel32()</code> method to draw a transparent (alpha == 0x60) red line in a BitmapData object:
   * <listing>
   * import flash.display.Bitmap;
   * import flash.display.BitmapData;
   *
   * var bmd:BitmapData = new BitmapData(80, 80, true, 0xFFCCCCCC);
   *
   * for (var i:uint = 0; i < 80; i++) {
   *     var red:uint = 0x60FF0000;
   *     bmd.setPixel32(i, 40, red);
   * }
   *
   * var bm:Bitmap = new Bitmap(bmd);
   * addChild(bm);
   * </listing>
   */
  "public function setPixel32",function setPixel32(x/*:int*/, y/*:int*/, color/*:uint*/)/*:void*/ {
    if (this.rect.contains(x, y)) {
      var context/*:CanvasRenderingContext2D*/ = this.getContext$1();
      var imageData/*:ImageData*/ = context.createImageData(1, 1);
      imageData.data[0] = color >> 16 & 0xFF;
      imageData.data[1] = color >>  8 & 0xFF;
      imageData.data[2] = color       & 0xFF;
      imageData.data[3] = color >> 24 & 0xFF;
      context.putImageData(imageData, x, y);
    }
  },

  /**
   * Converts a byte array into a rectangular region of pixel data. For each pixel, the <code>ByteArray.readUnsignedInt()</code> method is called and the return value is written into the pixel. If the byte array ends before the full rectangle is written, the function returns. The data in the byte array is expected to be 32-bit ARGB pixel values. No seeking is performed on the byte array before or after the pixels are read.
   * @param rect Specifies the rectangular region of the BitmapData object.
   * @param inputByteArray A ByteArray object that consists of 32-bit unmultiplied pixel values to be used in the rectangular region.
   *
   * @throws flash.errors.EOFError The <code>inputByteArray</code> object does not include enough data to fill the area of the <code>rect</code> rectangle. The method fills as many pixels as possible before throwing the exception.
   * @throws TypeError The rect or inputByteArray are null.
   *
   * @see flash.utils.ByteArray#readUnsignedInt()
   *
   * @example The following example uses the <code>getPixels()</code> and <code>setPixels()</code> methods to copy pixels from one BitmapData object to another:
   * <listing>
   * import flash.display.Bitmap;
   * import flash.display.BitmapData;
   * import flash.utils.ByteArray;
   * import flash.geom.Rectangle;
   *
   * var bmd1:BitmapData = new BitmapData(100, 100, true, 0xFFCCCCCC);
   * var bmd2:BitmapData = new BitmapData(100, 100, true, 0xFFFF0000);
   *
   * var rect:Rectangle = new Rectangle(0, 0, 100, 100);
   * var bytes:ByteArray = bmd1.getPixels(rect);
   *
   * bytes.position = 0;
   * bmd2.setPixels(rect, bytes);
   *
   * var bm1:Bitmap = new Bitmap(bmd1);
   * addChild(bm1);
   * var bm2:Bitmap = new Bitmap(bmd2);
   * addChild(bm2);
   * bm2.x = 110;
   * </listing>
   */
  "public function setPixels",function setPixels(rect/*:Rectangle*/, inputByteArray/*:ByteArray*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Tests pixel values in an image against a specified threshold and sets pixels that pass the test to new color values. Using the <code>threshold()</code> method, you can isolate and replace color ranges in an image and perform other logical operations on image pixels.
   * <p>The <code>threshold()</code> method's test logic is as follows:</p><ol>
   * <li>If <code>((pixelValue & mask) operation (threshold & mask))</code>, then set the pixel to <code>color</code>;</li>
   * <li>Otherwise, if <code>copySource == true</code>, then set the pixel to corresponding pixel value from <code>sourceBitmap</code>.</li></ol>
   * <p>The <code>operation</code> parameter specifies the comparison operator to use for the threshold test. For example, by using "==" as the <code>operation</code> parameter, you can isolate a specific color value in an image. Or by using <code>{operation: "<", mask: 0xFF000000, threshold: 0x7F000000, color: 0x00000000}</code>, you can set all destination pixels to be fully transparent when the source image pixel's alpha is less than 0x7F. You can use this technique for animated transitions and other effects.</p>
   * @param sourceBitmapData The input bitmap image to use. The source image can be a different BitmapData object or it can refer to the current BitmapData instance.
   * @param sourceRect A rectangle that defines the area of the source image to use as input.
   * @param destPoint The point within the destination image (the current BitmapData instance) that corresponds to the upper-left corner of the source rectangle.
   * @param operation One of the following comparison operators, passed as a String: "<", "<=", ">", ">=", "==", "!="
   * @param threshold The value that each pixel is tested against to see if it meets or exceeds the threshhold.
   * @param color The color value that a pixel is set to if the threshold test succeeds. The default value is 0x00000000.
   * @param mask The mask to use to isolate a color component.
   * @param copySource If the value is <code>true</code>, pixel values from the source image are copied to the destination when the threshold test fails. If the value is <code>false</code>, the source image is not copied when the threshold test fails.
   *
   * @return The number of pixels that were changed.
   *
   * @throws TypeError The sourceBitmapData, sourceRect destPoint or operation are null.
   * @throws ArgumentError The operation string is not a valid operation
   *
   * @example The following example uses the <code>perlinNoise()</code> method to add a blue and red pattern to one BitmapData object, and then uses the <code>threshold()</code> method to copy those pixels from the first BitmapData object to a second one, replacing those pixels in which the red value is greater than 0x80 (50%) with a pixel set to transparent red (0x20FF0000):
   * <listing>
   * import flash.display.Bitmap;
   * import flash.display.BitmapData;
   * import flash.display.BitmapDataChannel;
   * import flash.geom.Point;
   * import flash.geom.Rectangle;
   *
   * var bmd1:BitmapData = new BitmapData(200, 200, true, 0xFFCCCCCC);
   *
   * var seed:int = int(Math.random() * int.MAX_VALUE);
   * var channels:uint = BitmapDataChannel.RED | BitmapDataChannel.BLUE;
   * bmd1.perlinNoise(100, 80, 12, seed, false, true, channels, false, null);
   *
   * var bitmap1:Bitmap = new Bitmap(bmd1);
   * addChild(bitmap1);
   *
   * var bmd2:BitmapData = new BitmapData(200, 200, true, 0xFFCCCCCC);
   * var pt:Point = new Point(0, 0);
   * var rect:Rectangle = new Rectangle(0, 0, 200, 200);
   * var threshold:uint =  0x00800000;
   * var color:uint = 0x20FF0000;
   * var maskColor:uint = 0x00FF0000;
   * bmd2.threshold(bmd1, rect, pt, ">", threshold, color, maskColor, true);
   *
   * var bitmap2:Bitmap = new Bitmap(bmd2);
   * bitmap2.x = bitmap1.x + bitmap1.width + 10;
   * addChild(bitmap2);
   * </listing>
   */
  "public function threshold",function threshold(sourceBitmapData/*:BitmapData*/, sourceRect/*:Rectangle*/, destPoint/*:Point*/, operation/*:String*/, threshold/*:uint*/, color/*:uint = 0*/, mask/*:uint = 0xFFFFFFFF*/, copySource/*:Boolean = false*/)/*:uint*/ {switch(arguments.length){case 0:case 1:case 2:case 3:case 4:case 5:color = 0;case 6:mask = 0xFFFFFFFF;case 7:copySource = false;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Unlocks an image so that any objects that reference the BitmapData object, such as Bitmap objects, are updated when this BitmapData object changes. To improve performance, use this method along with the <code>lock()</code> method before and after numerous calls to the <code>setPixel()</code> or <code>setPixel32()</code> method.
   * @param changeRect The area of the BitmapData object that has changed. If you do not specify a value for this parameter, the entire area of the BitmapData object is considered changed. This parameter requires Flash Player version 9.0.115.0 or later.
   *
   * @see #lock()
   * @see #setPixel()
   * @see #setPixel32()
   *
   * @example The following example creates a BitmapData object based on the <code>bitmapData</code> property of a Bitmap object, <code>picture</code>. It then calls the <code>lock()</code> method before calling a complicated custom function, <code>complexTransformation()</code>, that modifies the BitmapData object. (The <code>picture</code> object and the <code>complexTransformation()</code> function are not defined in this example.) Even if the <code>complexTransformation()</code> function updates the <code>bitmapData</code> property of the <code>picture</code> object, changes are not reflected until the code calls the <code>unlock()</code> method on the <code>bitmapData</code> object:
   * <listing>
   * import flash.display.BitmapData;
   *
   * var bitmapData:BitmapData = picture.bitmapData;
   * bitmapData.lock();
   * bitmapData = complexTransformation(bitmapData);
   * bitmapData.unlock();
   * picture.bitmapData = bitmapData;
   * </listing>
   */
  "public function unlock",function unlock(changeRect/*:Rectangle = null*/)/*:void*/ {switch(arguments.length){case 0:changeRect = null;}
    // TODO: see lock()
  },

  /**
   * @private
   */
  "public native function set transparent"/*(value:Boolean):void;*/,

  /**
   * @private
   */
  "public native function set width"/*(value:int):void;*/,

  /**
   * @private
   */
  "public native function set height"/*(value:int):void;*/,

  "private const",{ elementChangeListeners/*:Array*/ :function(){return( []);}},

  "internal function getElement",function getElement()/*:HTMLElement*/ {
    if (!this.elem$1) {
      return this.asDiv();
    }
    return this.elem$1;
  },

  "public static function fromImg",function fromImg(img/*:HTMLImageElement*/)/*:BitmapData*/ {
    var bitmapData/*:BitmapData*/ = new flash.display.BitmapData(img.width, img.height, true, 0);
    bitmapData.image$1 = img;
    return bitmapData;
  },

  "internal function getImage",function getImage()/*:HTMLImageElement*/ {
    if (this.image$1)
      return this.image$1;
    var img/*:HTMLImageElement*/ = new js.Image();
    if (this.isCanvas$1) {
      img.src =/* js.HTMLCanvasElement*/(this.elem$1).toDataURL();
    } else {
      return null;
    }
    return img;
  },

  "public function asDiv",function asDiv()/*:HTMLElement*/ {
    var url/*:String*/;
    if (!this.elem$1 || this.isCanvas$1) {
      if (this.isCanvas$1) {
        url = this.asCanvas$1().toDataURL();
      }
      this.isCanvas$1 = false;
      var div/*:HTMLElement*/ =/* js.HTMLElement*/(window.document.createElement("DIV"));
      div.style.position = "absolute";
      div.style.width = this.width + "px";
      div.style.height = this.height + "px";
      this.changeElement$1(div);
    }
    if (this.image$1) {
      url = this.image$1.src;
    }
    this.elem$1.style.backgroundColor = flash.display.Graphics.toRGBA(this._fillColor$1, this._alpha$1);
    this.elem$1.style.backgroundImage = url ? "url('" + url + "')" : "none";
    return this.elem$1;
  },

  "private function getContext",function getContext()/*:CanvasRenderingContext2D*/ {
    return/* js.CanvasRenderingContext2D*/(this.asCanvas$1().getContext("2d"));
  },

  "private function asCanvas",function asCanvas()/*:HTMLCanvasElement*/ {
    if (!this.isCanvas$1) {
      this.isCanvas$1 = true;
      var canvas/*:HTMLCanvasElement*/ =/* js.HTMLCanvasElement*/(window.document.createElement("canvas"));
      canvas.width = this.width;
      canvas.height = this.height;
      canvas.style.position = "absolute";
      var context/* : CanvasRenderingContext2D*/ =/* js.CanvasRenderingContext2D*/(canvas.getContext("2d"));
      if (this._alpha$1 > 0 || !this.transparent) {
        context.save();
        context.fillStyle = flash.display.Graphics.toRGBA(this._fillColor$1, this._alpha$1);
        context.fillRect(0, 0, this.width, this.height);
        context.restore();
      }
      if (this.image$1) {
        context.drawImage(this.image$1, this.imageOffsetX$1, this.imageOffsetY$1, this.width, this.height, 0, 0, this.width, this.height);
        this.image$1 = null;
      }
      this.changeElement$1(canvas);
    }
    return/* js.HTMLCanvasElement*/(this.elem$1);
  },

  "internal function addElementChangeListener",function addElementChangeListener(listener/*:Function*/)/*:void*/ {
    this.elementChangeListeners$1.push(listener);
  },

  "internal function removeElementChangeListener",function removeElementChangeListener(listener/*:Function*/)/*:void*/ {
    var listenerIndex/*:int*/ = this.elementChangeListeners$1.indexOf(listener);
    if (listenerIndex !== -1) {
      this.elementChangeListeners$1.slice(listenerIndex, 1);
    }
  },

  "private function changeElement",function changeElement(elem/*:HTMLElement*/)/*:void*/ {
    this.elem$1 = elem;
    for (var i/*:int*/ = 0; i < this.elementChangeListeners$1.length; i++) {
      this.elementChangeListeners$1[i](elem);
    }
  },

  "private var",{ _fillColor/* : uint*/:0},
  "private var",{ _alpha/* : Number*/:NaN},
  "private var",{ elem/* : HTMLElement*/:null}, // either div or canvas
  "private var",{ isCanvas/* : Boolean*/:false}, // whether elem is a canvas
  "private var",{ image/* : HTMLImageElement*/:null}, // only set if BitmapData if created from and image
  "private var",{ imageOffsetX/* : int*/:0}, // left offset in the image
  "private var",{ imageOffsetY/* : int*/:0}, // top offset in the image
];},["fromImg"],["flash.display.IBitmapDrawable","flash.geom.Rectangle","Error","Math","flash.display.Graphics","flash.display.Bitmap","flash.display.DisplayObject","js.HTMLImageElement","js.HTMLCanvasElement","js.Image","js.HTMLElement","js.CanvasRenderingContext2D"], "0.8.0", "0.8.2-SNAPSHOT"
);