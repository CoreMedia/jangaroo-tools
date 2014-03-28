joo.classLoader.prepare("package flash.display",/* {
import js.HTMLElement*/

/**
 * The Bitmap class represents display objects that represent bitmap images. These can be images that you load with the flash.display.Loader class, or they can be images that you create with the <code>Bitmap()</code> constructor.
 * <p>The <code>Bitmap()</code> constructor allows you to create a Bitmap object that contains a reference to a BitmapData object. After you create a Bitmap object, use the <code>addChild()</code> or <code>addChildAt()</code> method of the parent DisplayObjectContainer instance to place the bitmap on the display list.</p>
 * <p>A Bitmap object can share its BitmapData reference among several Bitmap objects, independent of translation or rotation properties. Because you can create multiple Bitmap objects that reference the same BitmapData object, multiple display objects can use the same complex BitmapData object without incurring the memory overhead of a BitmapData object for each display object instance.</p>
 * <p>A BitmapData object can be drawn to the screen by a Bitmap object in one of two ways: by using the vector renderer as a fill-bitmap shape, or by using a faster pixel-copying routine. The pixel-copying routine is substantially faster than the vector renderer, but the Bitmap object must meet certain conditions to use it:</p>
 * <ul>
 * <li>No stretching, rotation, or skewing can be applied to the Bitmap object.</li>
 * <li>No color transform can be applied to the Bitmap object.</li>
 * <li>No blend mode can be applied to the Bitmap object.</li>
 * <li>No clipping can be done through mask layers or <code>setMask()</code> methods.</li>
 * <li>The image itself cannot be a mask.</li>
 * <li>The destination coordinates must be on a whole pixel boundary.</li></ul>
 * <p>If you load a Bitmap object from a domain other than that of the Loader object used to load the image, and there is no URL policy file that permits access to the domain of the Loader object, then a script in that domain cannot access the Bitmap object or its properties and methods. For more information, see the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</p>
 * <p><b>Note:</b> The Bitmap class is not a subclass of the InteractiveObject class, so it cannot dispatch mouse events. However, you can use the <code>addEventListener()</code> method of the display object container that contains the Bitmap object.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/display/Bitmap.html#includeExamplesSummary">View the examples</a></p>
 * @see Loader
 * @see BitmapData
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e3c.html Core display classes
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7d66.html Basics of working with bitmaps
 *
 */
"public class Bitmap extends flash.display.DisplayObject",3,function($$private){var $$bound=joo.boundMethod;return[ 
  /**
   * The BitmapData object being referenced.
   */
  "public function get bitmapData",function bitmapData$get()/*:BitmapData*/ {
    return this._bitmapData$3;
  },

  /**
   * @private
   */
  "public function set bitmapData",function bitmapData$set(value/*:BitmapData*/)/*:void*/ {
    if (this.listenerAdded$3) {
      this._bitmapData$3.removeElementChangeListener($$bound(this,"setElement"));
    }
    this._bitmapData$3 = value;
    if (this.listenerAdded$3) {
      this._bitmapData$3.addElementChangeListener($$bound(this,"setElement"));
    }
  },

  /**
   * Controls whether or not the Bitmap object is snapped to the nearest pixel. The PixelSnapping class includes possible values:
   * <ul>
   * <li><code>PixelSnapping.NEVER</code>—No pixel snapping occurs.</li>
   * <li><code>PixelSnapping.ALWAYS</code>—The image is always snapped to the nearest pixel, independent of transformation.</li>
   * <li><code>PixelSnapping.AUTO</code>—The image is snapped to the nearest pixel if it is drawn with no rotation or skew and it is drawn at a scale factor of 99.9% to 100.1%. If these conditions are satisfied, the bitmap image is drawn at 100% scale, snapped to the nearest pixel. Internally, this value allows the image to be drawn as fast as possible using the vector renderer.</li></ul>
   */
  "public function get pixelSnapping",function pixelSnapping$get()/*:String*/ {
    return this._pixelSnapping$3;
  },

  /**
   * @private
   */
  "public function set pixelSnapping",function pixelSnapping$set(value/*:String*/)/*:void*/ {
    this._pixelSnapping$3 = value;
  },

  /**
   * Controls whether or not the bitmap is smoothed when scaled. If <code>true</code>, the bitmap is smoothed when scaled. If <code>false</code>, the bitmap is not smoothed when scaled.
   */
  "public function get smoothing",function smoothing$get()/*:Boolean*/ {
    return this._smoothing$3;
  },

  /**
   * @private
   */
  "public function set smoothing",function smoothing$set(value/*:Boolean*/)/*:void*/ {
    this._smoothing$3 = value;
    // TODO: can we use canvas.context.mozImageSmoothingEnabled = true / false to achieve smoothing?
  },

  /**
   * Initializes a Bitmap object to refer to the specified BitmapData object.
   * @param bitmapData The BitmapData object being referenced.
   * @param pixelSnapping Whether or not the Bitmap object is snapped to the nearest pixel.
   * @param smoothing Whether or not the bitmap is smoothed when scaled. For example, the following examples show the same bitmap scaled by a factor of 3, with <code>smoothing</code> set to <code>false</code> (left) and <code>true</code> (right):
   * <table>
   * <tr>
   * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/bitmap_smoothing_off.jpg" /></td>
   * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/bitmap_smoothing_on.jpg" /></td></tr></table>
   *
   * @example The following example shows how you can dynamically load an image at runtime using the ActionScript 3.0 Loader class and then copy the image's pixels into four separate Bitmap object instances on the display list by using the Loader instance's content property and bitmapData properties. Example provided by <a href="http://actionscriptexamples.com/2008/11/24/copying-a-dynamically-loaded-images-pixels-to-a-new-bitmap-instance/">ActionScriptExamples.com</a>.
   * <listing>
   * const IMAGE_URL:String = "http://www.helpexamples.com/flash/images/logo.png";
   *
   * var ldr:Loader = new Loader();
   * ldr.contentLoaderInfo.addEventListener(Event.COMPLETE, ldr_complete);
   * ldr.load(new URLRequest(IMAGE_URL));
   *
   * var bitmap1:Bitmap;
   * var bitmap2:Bitmap;
   * var bitmap3:Bitmap;
   * var bitmap4:Bitmap;
   *
   * function ldr_complete(evt:Event):void {
   *     var bmp:Bitmap = ldr.content as Bitmap;
   *
   *     bitmap1 = new Bitmap(bmp.bitmapData);
   *     bitmap1.x = 100;
   *     bitmap1.y = 100;
   *     bitmap1.rotation = 0;
   *     addChild(bitmap1);
   *
   *     bitmap2 = new Bitmap(bmp.bitmapData);
   *     bitmap2.x = 200;
   *     bitmap2.y = 100;
   *     bitmap2.rotation = 90;
   *     addChild(bitmap2);
   *
   *     bitmap3 = new Bitmap(bmp.bitmapData);
   *     bitmap3.x = 300;
   *     bitmap3.y = 100;
   *     bitmap3.rotation = 180;
   *     addChild(bitmap3);
   *
   *     bitmap4 = new Bitmap(bmp.bitmapData);
   *     bitmap4.x = 400;
   *     bitmap4.y = 100;
   *     bitmap4.rotation = 270;
   *     addChild(bitmap4);
   * }
   * </listing>
   */
  "public function Bitmap",function Bitmap$(bitmapData/*:BitmapData = null*/, pixelSnapping/*:String = "auto"*/, smoothing/*:Boolean = false*/) {switch(arguments.length){case 0:bitmapData = null;case 1:pixelSnapping = "auto";case 2:smoothing = false;}
    this._bitmapData$3 = bitmapData;
    this.super$3();
    this._pixelSnapping$3 = pixelSnapping;
    this._smoothing$3 = smoothing;
  },

  "override protected function createElement",function createElement()/*:HTMLElement*/ {
    if (!this.listenerAdded$3) {
      this._bitmapData$3.addElementChangeListener($$bound(this,"setElement"));
    }
    return this._bitmapData$3.getElement();
  },

  "override public function get height",function height$get()/*:Number*/ {
    return this._bitmapData$3.height * this.scaleY;
  },

  "override public function get width",function width$get()/*:Number*/ {
    return this._bitmapData$3.width * this.scaleX;
  },

  "private var",{ _bitmapData/*:BitmapData*/:null},
  "private var",{ listenerAdded/*:Boolean*/ : false},
  "private var",{ _pixelSnapping/*:String*/:null},
  "private var",{ _smoothing/*:Boolean*/:false},

];},[],["flash.display.DisplayObject"], "0.8.0", "0.8.2-SNAPSHOT"
);