joo.classLoader.prepare("package flash.display",/* {

import flash.geom.Matrix
import flash.geom.Point

import js.CanvasGradient
import js.CanvasRenderingContext2D
import js.HTMLCanvasElement
import js.ImageData*/

/**
 * The Graphics class contains a set of methods that you can use to create a vector shape. Display objects that support drawing include Sprite and Shape objects. Each of these classes includes a <code>graphics</code> property that is a Graphics object. The following are among those helper functions provided for ease of use: <code>drawRect()</code>, <code>drawRoundRect()</code>, <code>drawCircle()</code>, and <code>drawEllipse()</code>.
 * <p>You cannot create a Graphics object directly from ActionScript code. If you call <code>new Graphics()</code>, an exception is thrown.</p>
 * <p>The Graphics class is final; it cannot be subclassed.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/display/Graphics.html#includeExamplesSummary">View the examples</a></p>
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7dce.html Basics of the drawing API
 *
 */
"public final class Graphics",1,function($$private){var as=joo.as;return[function(){joo.classLoader.init(flash.geom.Matrix,flash.display.CapsStyle,Math,flash.display.GradientType,flash.display.JointStyle);}, 
  /**
   * Fills a drawing area with a bitmap image. The bitmap can be repeated or tiled to fill the area. The fill remains in effect until you call the <code>beginFill()</code>, <code>beginBitmapFill()</code>, <code>beginGradientFill()</code>, or <code>beginShaderFill()</code> method. Calling the <code>clear()</code> method clears the fill.
   * <p>The application renders the fill whenever three or more points are drawn, or when the <code>endFill()</code> method is called.</p>
   * @param bitmap A transparent or opaque bitmap image that contains the bits to be displayed.
   * @param matrix A matrix object (of the flash.geom.Matrix class), which you can use to define transformations on the bitmap. For example, you can use the following matrix to rotate a bitmap by 45 degrees (pi/4 radians):
   * <listing>
   *      matrix = new flash.geom.Matrix();
   *      matrix.rotate(Math.PI / 4);
   *     </listing>
   * @param repeat If <code>true</code>, the bitmap image repeats in a tiled pattern. If <code>false</code>, the bitmap image does not repeat, and the edges of the bitmap are used for any fill area that extends beyond the bitmap.
   * <p>For example, consider the following bitmap (a 20 x 20-pixel checkerboard pattern):</p>
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/movieClip_beginBitmapFill_repeat_1.jpg" /></p>
   * <p>When <code>repeat</code> is set to <code>true</code> (as in the following example), the bitmap fill repeats the bitmap:</p>
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/movieClip_beginBitmapFill_repeat_2.jpg" /></p>
   * <p>When <code>repeat</code> is set to <code>false</code>, the bitmap fill uses the edge pixels for the fill area outside the bitmap:</p>
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/movieClip_beginBitmapFill_repeat_3.jpg" /></p>
   * @param smooth If <code>false</code>, upscaled bitmap images are rendered by using a nearest-neighbor algorithm and look pixelated. If <code>true</code>, upscaled bitmap images are rendered by using a bilinear algorithm. Rendering by using the nearest neighbor algorithm is faster.
   *
   * @see #endFill()
   * @see #beginFill()
   * @see #beginGradientFill()
   *
   * @example The following example uses an image (<code>image1.jpg</code>) that is rotated and repeated to fill in a rectangle. <ol>
   * <li>The image file (<code>image1.jpg</code>) is loaded using the <code>Loader</code> and <code>URLRequest</code> objects. Here the file is in the same directory as the SWF file. The SWF file needs to be compiled with Local Playback Security set to Access Local Files Only.</li>
   * <li>When the image is loaded (<code>Event</code> is complete), the <code>drawImage()</code> method is called. The <code>ioErrorHandler()</code> method writes a trace comment if the image was not loaded properly.</li>
   * <li>In <code>drawImage()</code> method, a <code>BitmapData</code> object is instantiated and its width and height are set to the image (<code>image1.jpg</code>). Then the source image is drawn into the BitmapData object. Next, a rectangle is drawn in the <code>mySprite</code> Sprite object and the BitmapData object is used to fill it. Using a <code>Matrix</code> object, the <code>beginBitmapFill()</code> method rotates the image 45 degrees, then it begins filling the rectangle with the image until it is finished.</li></ol>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.display.BitmapData;
   *     import flash.display.Loader;
   *     import flash.net.URLRequest;
   *     import flash.events.Event;
   *     import flash.events.IOErrorEvent;
   *     import flash.geom.Matrix;
   *
   *     public class Graphics_beginBitmapFillExample extends Sprite {
   *
   *         private var url:String = "image1.jpg";
   *         private var loader:Loader = new Loader();
   *
   *         public function Graphics_beginBitmapFillExample() {
   *
   *             var request:URLRequest = new URLRequest(url);
   *
   *             loader.load(request);
   *             loader.contentLoaderInfo.addEventListener(Event.COMPLETE, drawImage);
   *             loader.contentLoaderInfo.addEventListener(IOErrorEvent.IO_ERROR, ioErrorHandler);
   *         }
   *
   *         private function drawImage(event:Event):void {
   *
   *             var mySprite:Sprite = new Sprite();
   *             var myBitmap:BitmapData = new BitmapData(loader.width, loader.height, false);
   *
   *             myBitmap.draw(loader, new Matrix());
   *
   *             var matrix:Matrix = new Matrix();
   *             matrix.rotate(Math.PI/4);
   *
   *             mySprite.graphics.beginBitmapFill(myBitmap, matrix, true);
   *             mySprite.graphics.drawRect(100, 50, 200, 90);
   *             mySprite.graphics.endFill();
   *
   *             addChild(mySprite);
   *         }
   *
   *          private function ioErrorHandler(event:IOErrorEvent):void {
   *             trace("Unable to load image: " + url);
   *         }
   *     }
   * }
   * </listing>
   */
  "public function beginBitmapFill",function beginBitmapFill(bitmap/*:BitmapData*/, matrix/*:Matrix = null*/, repeat/*:Boolean = true*/, smooth/*:Boolean = false*/)/*:void*/ {if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){matrix = null;}repeat = true;}smooth = false;}
    this._beginFill$1(this.createPattern$1(bitmap, matrix, repeat, smooth));
  },

  "private function createPattern",function createPattern(bitmap/*:BitmapData*/, matrix/*:Matrix*/, repeat/*:Boolean*/, smooth/*:Boolean*/)/*:Object*/ {
    // TODO: matrix, smooth
    return this.context$1.createPattern(bitmap.getElement(), repeat ? "repeat" : "no-repeat");
  },

  /**
   * Specifies a simple one-color fill that subsequent calls to other Graphics methods (such as <code>lineTo()</code> or <code>drawCircle()</code>) use when drawing. The fill remains in effect until you call the <code>beginFill()</code>, <code>beginBitmapFill()</code>, <code>beginGradientFill()</code>, or <code>beginShaderFill()</code> method. Calling the <code>clear()</code> method clears the fill.
   * <p>The application renders the fill whenever three or more points are drawn, or when the <code>endFill()</code> method is called.</p>
   * @param color The color of the fill (0xRRGGBB).
   * @param alpha The alpha value of the fill (0.0 to 1.0).
   *
   * @see #endFill()
   * @see #beginBitmapFill()
   * @see #beginGradientFill()
   *
   * @example <a href="http://www.adobe.com/go/learn_as3_usingexamples_en">How to use this example</a>Please see the <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/display/Graphics.html#includeExamplesSummary">example</a> at the end of this class for an illustration of how to use this method.
   */
  "public function beginFill",function beginFill(color/*:uint*/, alpha/*:Number = 1.0*/)/*:void*/ {if(arguments.length<2){alpha = 1.0;}
    this._beginFill$1(flash.display.Graphics.toRGBA(color, alpha));
  },

  /**
   * Specifies a gradient fill used by subsequent calls to other Graphics methods (such as <code>lineTo()</code> or <code>drawCircle()</code>) for the object. The fill remains in effect until you call the <code>beginFill()</code>, <code>beginBitmapFill()</code>, <code>beginGradientFill()</code>, or <code>beginShaderFill()</code> method. Calling the <code>clear()</code> method clears the fill.
   * <p>The application renders the fill whenever three or more points are drawn, or when the <code>endFill()</code> method is called.</p>
   * @param type A value from the GradientType class that specifies which gradient type to use: <code>GradientType.LINEAR</code> or <code>GradientType.RADIAL</code>.
   * @param colors An array of RGB hexadecimal color values used in the gradient; for example, red is 0xFF0000, blue is 0x0000FF, and so on. You can specify up to 15 colors. For each color, specify a corresponding value in the alphas and ratios parameters.
   * @param alphas An array of alpha values for the corresponding colors in the colors array; valid values are 0 to 1. If the value is less than 0, the default is 0. If the value is greater than 1, the default is 1.
   * @param ratios An array of color distribution ratios; valid values are 0-255. This value defines the percentage of the width where the color is sampled at 100%. The value 0 represents the left position in the gradient box, and 255 represents the right position in the gradient box.
   * <p><b>Note:</b> This value represents positions in the gradient box, not the coordinate space of the final gradient, which can be wider or thinner than the gradient box. Specify a value for each value in the <code>colors</code> parameter.</p>
   * <p>For example, for a linear gradient that includes two colors, blue and green, the following example illustrates the placement of the colors in the gradient based on different values in the <code>ratios</code> array:</p>
   * <table>
   * <tr><th><code>ratios</code></th><th>Gradient</th></tr>
   * <tr>
   * <td><code>[0, 127]</code></td>
   * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/gradient-ratios-1.jpg" /></td></tr>
   * <tr>
   * <td><code>[0, 255]</code></td>
   * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/gradient-ratios-2.jpg" /></td></tr>
   * <tr>
   * <td><code>[127, 255]</code></td>
   * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/gradient-ratios-3.jpg" /></td></tr></table>
   * <p>The values in the array must increase sequentially; for example, <code>[0, 63, 127, 190, 255]</code>.</p>
   * @param matrix A transformation matrix as defined by the flash.geom.Matrix class. The flash.geom.Matrix class includes a <code>createGradientBox()</code> method, which lets you conveniently set up the matrix for use with the <code>beginGradientFill()</code> method.
   * @param spreadMethod A value from the SpreadMethod class that specifies which spread method to use, either: <code>SpreadMethod.PAD</code>, <code>SpreadMethod.REFLECT</code>, or <code>SpreadMethod.REPEAT</code>.
   * <p>For example, consider a simple linear gradient between two colors:</p>
   * <listing>
   *      import flash.geom.*
   *      import flash.display.*
   *      var fillType:String = GradientType.LINEAR;
   *      var colors:Array = [0xFF0000, 0x0000FF];
   *      var alphas:Array = [1, 1];
   *      var ratios:Array = [0x00, 0xFF];
   *      var matr:Matrix = new Matrix();
   *      matr.createGradientBox(20, 20, 0, 0, 0);
   *      var spreadMethod:String = SpreadMethod.PAD;
   *      this.graphics.beginGradientFill(fillType, colors, alphas, ratios, matr, spreadMethod);
   *      this.graphics.drawRect(0,0,100,100);
   *     </listing>
   * <p>This example uses <code>SpreadMethod.PAD</code> for the spread method, and the gradient fill looks like the following:</p>
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/beginGradientFill_spread_pad.jpg" /></p>
   * <p>If you use <code>SpreadMethod.REFLECT</code> for the spread method, the gradient fill looks like the following:</p>
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/beginGradientFill_spread_reflect.jpg" /></p>
   * <p>If you use <code>SpreadMethod.REPEAT</code> for the spread method, the gradient fill looks like the following:</p>
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/beginGradientFill_spread_repeat.jpg" /></p>
   * @param interpolationMethod A value from the InterpolationMethod class that specifies which value to use: <code>InterpolationMethod.LINEAR_RGB</code> or <code>InterpolationMethod.RGB</code>
   * <p>For example, consider a simple linear gradient between two colors (with the <code>spreadMethod</code> parameter set to <code>SpreadMethod.REFLECT</code>). The different interpolation methods affect the appearance as follows:</p>
   * <table>
   * <tr>
   * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/beginGradientFill_interp_linearrgb.jpg" /> </td>
   * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/beginGradientFill_interp_rgb.jpg" /> </td></tr>
   * <tr>
   * <td><code>InterpolationMethod.LINEAR_RGB</code></td>
   * <td><code>InterpolationMethod.RGB</code></td></tr></table>
   * @param focalPointRatio A number that controls the location of the focal point of the gradient. 0 means that the focal point is in the center. 1 means that the focal point is at one border of the gradient circle. -1 means that the focal point is at the other border of the gradient circle. A value less than -1 or greater than 1 is rounded to -1 or 1. For example, the following example shows a <code>focalPointRatio</code> set to 0.75:
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/radial_sketch.jpg" /></p>
   *
   * @throws ArgumentError If the <code>type</code> parameter is not valid.
   *
   * @see #endFill()
   * @see #beginFill()
   * @see #beginBitmapFill()
   * @see flash.geom.Matrix#createGradientBox()
   * @see GradientType
   * @see SpreadMethod
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ddb.html Using Matrix objects
   *
   */
  "public function beginGradientFill",function beginGradientFill(type/*:String*/, colors/*:Array*/, alphas/*:Array*/, ratios/*:Array*/, matrix/*:Matrix = null*/, spreadMethod/*:String = "pad"*/, interpolationMethod/*:String = "rgb"*/, focalPointRatio/*:Number = 0*/)/*:void*/ {if(arguments.length<8){if(arguments.length<7){if(arguments.length<6){if(arguments.length<5){matrix = null;}spreadMethod = "pad";}interpolationMethod = "rgb";}focalPointRatio = 0;}
    this._beginFill$1(this.createGradientStyle$1(type, colors, alphas, ratios,
      matrix, spreadMethod, interpolationMethod, focalPointRatio));
  },

  /**
   * Clears the graphics that were drawn to this Graphics object, and resets fill and line style settings.
   */
  "public function clear",function clear()/*:void*/ {
    this.lineStyle();
    this.context$1.save();
    this.context$1.setTransform(1,0,0,1,0,0);
    this.context$1.fillStyle = "#000000";
    this.context$1.clearRect(0,0,this.context$1.canvas.width, this.context$1.canvas.height);
    this.context$1.restore();
    this.insideFill$1 = false;
    this.context$1.moveTo(0, 0);
    this.width = this.height = 0;
  },

  /**
   * Draws a curve using the current line style from the current drawing position to (anchorX, anchorY) and using the control point that (<code>controlX</code>, <code>controlY</code>) specifies. The current drawing position is then set to (<code>anchorX</code>, <code>anchorY</code>). If the movie clip in which you are drawing contains content created with the Flash drawing tools, calls to the <code>curveTo()</code> method are drawn underneath this content. If you call the <code>curveTo()</code> method before any calls to the <code>moveTo()</code> method, the default of the current drawing position is (0, 0). If any of the parameters are missing, this method fails and the current drawing position is not changed.
   * <p>The curve drawn is a quadratic Bezier curve. Quadratic Bezier curves consist of two anchor points and one control point. The curve interpolates the two anchor points and curves toward the control point.</p>
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/quad_bezier.jpg" /></p>
   * @param controlX A number that specifies the horizontal position of the control point relative to the registration point of the parent display object.
   * @param controlY A number that specifies the vertical position of the control point relative to the registration point of the parent display object.
   * @param anchorX A number that specifies the horizontal position of the next anchor point relative to the registration point of the parent display object.
   * @param anchorY A number that specifies the vertical position of the next anchor point relative to the registration point of the parent display object.
   *
   * @example The following example draws a green circular object with a width and height of 100 pixels, 250 pixels to the right from the registration point (0, 0) of Sprite display object.
   * <p>Draw four curves to produce a circle and fill it green.</p>
   * <p>Note that due to the nature of the quadratic Bezier equation, this is not a perfect circle. The best way to draw a circle is to use the Graphics class's <code>drawCircle()</code> method.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.display.Shape;
   *
   *     public class Graphics_curveToExample1 extends Sprite
   *     {
   *         public function Graphics_curveToExample1():void
   *         {
   *             var roundObject:Shape = new Shape();
   *
   *             roundObject.graphics.beginFill(0x00FF00);
   *             roundObject.graphics.moveTo(250, 0);
   *             roundObject.graphics.curveTo(300, 0, 300, 50);
   *             roundObject.graphics.curveTo(300, 100, 250, 100);
   *             roundObject.graphics.curveTo(200, 100, 200, 50);
   *             roundObject.graphics.curveTo(200, 0, 250, 0);
   *             roundObject.graphics.endFill();
   *
   *             this.addChild(roundObject);
   *         }
   *     }
   * }
   * </listing>
   * <div>The following example draws a new moon using <code>curveTo()</code> method.
   * <p>Two curve lines of 1 pixel are drawn and the space in between is filled white. The <code>moveTo()</code> method is used to position the current drawing position to coordinates (100, 100). The first curve moves the drawing position to (100, 200), its destination point. The second curve returns the position back to the starting position (100, 100), its destination point. The horizontal control points determine the different curve sizes.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.display.Shape;
   *
   *     public class Graphics_curveToExample2 extends Sprite
   *     {
   *         public function Graphics_curveToExample2() {
   *             var newMoon:Shape = new Shape();
   *
   *             newMoon.graphics.lineStyle(1, 0);
   *             newMoon.graphics.beginFill(0xFFFFFF);
   *             newMoon.graphics.moveTo(100, 100);
   *             newMoon.graphics.curveTo(30, 150, 100, 200);
   *             newMoon.graphics.curveTo(50, 150, 100, 100);
   *             graphics.endFill();
   *
   *             this.addChild(newMoon);
   *         }
   *     }
   * }
   * </listing></div>
   */
  "public function curveTo",function curveTo(controlX/*:Number*/, controlY/*:Number*/, anchorX/*:Number*/, anchorY/*:Number*/)/*:void*/ {
    // TODO: more accurate computation of maximum x and y coordinate occupied by this curve!
    this.createSpace$1(Math.max(this.x$1, controlX, anchorX), Math.max(this.y$1, controlY, anchorY));
    this.x$1 = anchorX;
    this.y$1 = anchorY;
    this.context$1.quadraticCurveTo(controlX, controlY, anchorX, anchorY);
    if (!this.insideFill$1) {
      this.context$1.stroke();
    }
  },

  /**
   * Draws a circle. Set the line style, fill, or both before you call the <code>drawCircle()</code> method, by calling the <code>linestyle()</code>, <code>lineGradientStyle()</code>, <code>beginFill()</code>, <code>beginGradientFill()</code>, or <code>beginBitmapFill()</code> method.
   * @param x The <i>x</i> location of the center of the circle relative to the registration point of the parent display object (in pixels).
   * @param y The <i>y</i> location of the center of the circle relative to the registration point of the parent display object (in pixels).
   * @param radius The radius of the circle (in pixels).
   *
   * @see #drawEllipse()
   * @see #lineStyle()
   * @see #lineGradientStyle()
   * @see #beginFill()
   * @see #beginGradientFill()
   * @see #beginBitmapFill()
   *
   * @example <a href="http://www.adobe.com/go/learn_as3_usingexamples_en">How to use this example</a>Please see the <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/display/Graphics.html#includeExamplesSummary">example</a> at the end of this class for an illustration of how to use this method.
   */
  "public function drawCircle",function drawCircle(x/*:Number*/, y/*:Number*/, radius/*:Number*/)/*:void*/ {
    this.createSpace$1(x + radius, y + radius);
    this.context$1.moveTo(x + radius, y);
    this.context$1.arc(x, y, radius, 0 , 2*Math.PI, false);
    if (this.insideFill$1) {
      this.context$1.fill();
    }
    this.context$1.stroke();
    this.context$1.beginPath();
    this.context$1.moveTo(x, y);
  },

  /**
   * Draws an ellipse. Set the line style, fill, or both before you call the <code>drawEllipse()</code> method, by calling the <code>linestyle()</code>, <code>lineGradientStyle()</code>, <code>beginFill()</code>, <code>beginGradientFill()</code>, or <code>beginBitmapFill()</code> method.
   * @param x The <i>x</i> location of the top-left of the bounding-box of the ellipse relative to the registration point of the parent display object (in pixels).
   * @param y The <i>y</i> location of the top left of the bounding-box of the ellipse relative to the registration point of the parent display object (in pixels).
   * @param width The width of the ellipse (in pixels).
   * @param height The height of the ellipse (in pixels).
   *
   * @see #drawCircle()
   * @see #lineStyle()
   * @see #lineGradientStyle()
   * @see #beginFill()
   * @see #beginGradientFill()
   * @see #beginBitmapFill()
   *
   * @example The following example uses the function <code>drawEgg()</code> to draw three different sized eggs (three sizes of ellipses), depending on the <code>eggSize</code> parameter. <ol>
   * <li>The constructor calls the function <code>drawEgg()</code> and passes the horizontal and vertical parameters for where the egg should be drawn, plus the type of egg (<code>eggSize</code>). (The height and width of the eggs (the ellipses) can be used to decide where to display them.)</li>
   * <li>Function <code>drawEgg()</code> draws the different size ellipses and fills them white using <code>beginFill()</code> method. There is no advance error handling written for his function.</li></ol>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.display.Shape;
   *
   *     public class Graphics_drawEllipseExample extends Sprite
   *     {
   *         public static const SMALL:uint = 0;
   *         public static const MEDIUM:uint = 1;
   *         public static const LARGE:uint = 2;
   *
   *         public function Graphics_drawEllipseExample()
   *         {
   *             drawEgg(SMALL, 0, 100);
   *             drawEgg(MEDIUM, 100, 60);
   *             drawEgg(LARGE, 250, 35);
   *         }
   *
   *         public function drawEgg(eggSize:uint, x:Number, y:Number):void  {
   *
   *             var myEgg:Shape = new Shape();
   *
   *             myEgg.graphics.beginFill(0xFFFFFF);
   *             myEgg.graphics.lineStyle(1);
   *
   *             switch(eggSize) {
   *                 case SMALL:
   *                     myEgg.graphics.drawEllipse(x, y, 60, 70);
   *                     break;
   *                 case MEDIUM:
   *                     myEgg.graphics.drawEllipse(x, y, 120, 150);
   *                     break;
   *                 case LARGE:
   *                     myEgg.graphics.drawEllipse(x, y, 150, 200);
   *                     break;
   *                 default:
   *                     trace ("Wrong size! There is no egg.");
   *                 break;
   *             }
   *
   *             myEgg.graphics.endFill();
   *
   *             this.addChild(myEgg);
   *         }
   *     }
   * }
   * </listing>
   */
  "public function drawEllipse",function drawEllipse(x/*:Number*/, y/*:Number*/, width/*:Number*/, height/*:Number*/)/*:void*/ {
    var rx/*:Number*/ = width / 2;
    var ry/*:Number*/ = height / 2;

    var cx/*:Number*/ = x + rx;
    var cy/*:Number*/ = y + ry;

    this.createSpace$1(x + width, y + height);

    this.context$1.beginPath();
    this.context$1.moveTo(cx, cy - ry);
    this.context$1.bezierCurveTo(cx + ($$private.KAPPA * rx), cy - ry,  cx + rx, cy - ($$private.KAPPA * ry), cx + rx, cy);
    this.context$1.bezierCurveTo(cx + rx, cy + ($$private.KAPPA * ry), cx + ($$private.KAPPA * rx), cy + ry, cx, cy + ry);
    this.context$1.bezierCurveTo(cx - ($$private.KAPPA * rx), cy + ry, cx - rx, cy + ($$private.KAPPA * ry), cx - rx, cy);
    this.context$1.bezierCurveTo(cx - rx, cy - ($$private.KAPPA * ry), cx - ($$private.KAPPA * rx), cy - ry, cx, cy - ry);

    if (this.insideFill$1) {
      this.context$1.fill();
    }
    this.context$1.stroke();
    this.context$1.beginPath();
    this.context$1.moveTo(x, y);
  },

  /**
   * Draws a rectangle. Set the line style, fill, or both before you call the <code>drawRect()</code> method, by calling the <code>linestyle()</code>, <code>lineGradientStyle()</code>, <code>beginFill()</code>, <code>beginGradientFill()</code>, or <code>beginBitmapFill()</code> method.
   * @param x A number indicating the horizontal position relative to the registration point of the parent display object (in pixels).
   * @param y A number indicating the vertical position relative to the registration point of the parent display object (in pixels).
   * @param width The width of the rectangle (in pixels).
   * @param height The height of the rectangle (in pixels).
   *
   * @throws ArgumentError If the <code>width</code> or <code>height</code> parameters are not a number (<code>Number.NaN</code>).
   *
   * @see #lineStyle()
   * @see #lineGradientStyle()
   * @see #beginFill()
   * @see #beginGradientFill()
   * @see #beginBitmapFill()
   * @see #drawRoundRect()
   *
   * @example The following example shows how you can draw shapes in ActionScript 3.0. Example provided by <a href="http://actionscriptexamples.com/2008/12/07/drawing-shapes-using-the-drawing-api-in-actionscript-30-and-actionscript-20/">ActionScriptExamples.com</a>.
   * <listing>
   * var movieClip:MovieClip = new MovieClip();
   * movieClip.graphics.beginFill(0xFF0000);
   * movieClip.graphics.drawRect(0, 0, 100, 80);
   * movieClip.graphics.endFill();
   * movieClip.x = 10;
   * movieClip.y = 10;
   * addChild(movieClip);
   * </listing>
   */
  "public function drawRect",function drawRect(x/*:Number*/, y/*:Number*/, width/*:Number*/, height/*:Number*/)/*:void*/ {
    this.createSpace$1(x + width, y + height);
    if (this.insideFill$1) {
      this.context$1.fillRect(x, y, width, height);
    }
    this.context$1.strokeRect(x, y, width, height);
  },

  /**
   * Draws a rounded rectangle. Set the line style, fill, or both before you call the <code>drawRoundRect()</code> method, by calling the <code>linestyle()</code>, <code>lineGradientStyle()</code>, <code>beginFill()</code>, <code>beginGradientFill()</code>, or <code>beginBitmapFill()</code> method.
   * @param x A number indicating the horizontal position relative to the registration point of the parent display object (in pixels).
   * @param y A number indicating the vertical position relative to the registration point of the parent display object (in pixels).
   * @param width The width of the round rectangle (in pixels).
   * @param height The height of the round rectangle (in pixels).
   * @param ellipseWidth The width of the ellipse used to draw the rounded corners (in pixels).
   * @param ellipseHeight The height of the ellipse used to draw the rounded corners (in pixels). Optional; if no value is specified, the default value matches that provided for the <code>ellipseWidth</code> parameter.
   *
   * @throws ArgumentError If the <code>width</code>, <code>height</code>, <code>ellipseWidth</code> or <code>ellipseHeight</code> parameters are not a number (<code>Number.NaN</code>).
   *
   * @see #lineStyle()
   * @see #lineGradientStyle()
   * @see #beginFill()
   * @see #beginGradientFill()
   * @see #beginBitmapFill()
   * @see #drawRect()
   *
   * @example <a href="http://www.adobe.com/go/learn_as3_usingexamples_en">How to use this example</a>Please see the <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/display/Graphics.html#includeExamplesSummary">example</a> at the end of this class for an illustration of how to use this method.
   */
  "public function drawRoundRect",function drawRoundRect(x/*:Number*/, y/*:Number*/, width/*:Number*/, height/*:Number*/, ellipseWidth/*:Number*/, ellipseHeight/*:Number = NaN*/)/*:void*/ {if(arguments.length<6){ellipseHeight = NaN;}
    this.createSpace$1(x + width, y + height);
    if (ellipseHeight==0 || ellipseWidth==0) {
      this.drawRect(x, y, width, height);
      return;
    }
    if (isNaN(ellipseHeight)) {
      ellipseHeight = ellipseWidth;
    }
    var x_lw/* : Number*/ = x + ellipseWidth;
    var x_r/*  : Number*/ = x + width;
    var x_rw/* : Number*/ = x_r - ellipseWidth;
    var y_tw/* : Number*/ = y + ellipseHeight;
    var y_b/*  : Number*/ = y + height;
    var y_bw/* : Number*/ = y_b - ellipseHeight;
    this.context$1.beginPath();
    this.context$1.moveTo(x_lw, y);
    this.context$1.lineTo(x_rw, y);
    this.context$1.quadraticCurveTo(x_r, y, x_r, y_tw);
    this.context$1.lineTo(x_r, y_bw);
    this.context$1.quadraticCurveTo(x_r, y_b, x_rw, y_b);
    this.context$1.lineTo(x_lw, y_b);
    this.context$1.quadraticCurveTo(x, y_b, x, y_bw);
    this.context$1.lineTo(x, y_tw);
    this.context$1.quadraticCurveTo(x, y, x_lw, y);
    this.context$1.closePath();
    if (this.insideFill$1) {
      this.context$1.fill();
    }
    this.context$1.stroke();
  },

  /**
   * Applies a fill to the lines and curves that were added since the last call to the <code>beginFill()</code>, <code>beginGradientFill()</code>, or <code>beginBitmapFill()</code> method. Flash uses the fill that was specified in the previous call to the <code>beginFill()</code>, <code>beginGradientFill()</code>, or <code>beginBitmapFill()</code> method. If the current drawing position does not equal the previous position specified in a <code>moveTo()</code> method and a fill is defined, the path is closed with a line and then filled.
   * @see #beginFill()
   * @see #beginBitmapFill()
   * @see #beginGradientFill()
   *
   */
  "public function endFill",function endFill()/*:void*/ {
    this.context$1.closePath();
    this.context$1.fill();
    this.context$1.stroke();
    this.insideFill$1 = false;
  },

  /**
   * Specifies a gradient to use for the stroke when drawing lines.
   * <p>The gradient line style is used for subsequent calls to Graphics methods such as the <code>lineTo()</code> methods or the <code>drawCircle()</code> method. The line style remains in effect until you call the <code>lineStyle()</code> or <code>lineBitmapStyle()</code> methods, or the <code>lineGradientStyle()</code> method again with different parameters.</p>
   * <p>You can call the <code>lineGradientStyle()</code> method in the middle of drawing a path to specify different styles for different line segments within a path.</p>
   * <p>Call the <code>lineStyle()</code> method before you call the <code>lineGradientStyle()</code> method to enable a stroke, or else the value of the line style is <code>undefined</code>.</p>
   * <p>Calls to the <code>clear()</code> method set the line style back to <code>undefined</code>.</p>
   * @param type A value from the GradientType class that specifies which gradient type to use, either GradientType.LINEAR or GradientType.RADIAL.
   * @param colors An array of RGB hex color values to be used in the gradient (for example, red is 0xFF0000, blue is 0x0000FF, and so on).
   * @param alphas An array of alpha values for the corresponding colors in the colors array; valid values are 0 to 1. If the value is less than 0, the default is 0. If the value is greater than 1, the default is 1.
   * @param ratios An array of color distribution ratios; valid values are from 0 to 255. This value defines the percentage of the width where the color is sampled at 100%. The value 0 represents the left position in the gradient box, and 255 represents the right position in the gradient box. This value represents positions in the gradient box, not the coordinate space of the final gradient, which can be wider or thinner than the gradient box. Specify a value for each value in the <code>colors</code> parameter.
   * <p>For example, for a linear gradient that includes two colors, blue and green, the following figure illustrates the placement of the colors in the gradient based on different values in the <code>ratios</code> array:</p>
   * <table>
   * <tr><th><code>ratios</code></th><th>Gradient</th></tr>
   * <tr>
   * <td><code>[0, 127]</code></td>
   * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/gradient-ratios-1.jpg" /></td></tr>
   * <tr>
   * <td><code>[0, 255]</code></td>
   * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/gradient-ratios-2.jpg" /></td></tr>
   * <tr>
   * <td><code>[127, 255]</code></td>
   * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/gradient-ratios-3.jpg" /></td></tr></table>
   * <p>The values in the array must increase, sequentially; for example, <code>[0, 63, 127, 190, 255]</code>.</p>
   * @param matrix A transformation matrix as defined by the flash.geom.Matrix class. The flash.geom.Matrix class includes a <code>createGradientBox()</code> method, which lets you conveniently set up the matrix for use with the <code>lineGradientStyle()</code> method.
   * @param spreadMethod A value from the SpreadMethod class that specifies which spread method to use:
   * <table>
   * <tr>
   * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/beginGradientFill_spread_pad.jpg" /></td>
   * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/beginGradientFill_spread_reflect.jpg" /></td>
   * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/beginGradientFill_spread_repeat.jpg" /></td></tr>
   * <tr>
   * <td><code>SpreadMethod.PAD</code></td>
   * <td><code>SpreadMethod.REFLECT</code></td>
   * <td><code>SpreadMethod.REPEAT</code></td></tr></table>
   * @param interpolationMethod A value from the InterpolationMethod class that specifies which value to use. For example, consider a simple linear gradient between two colors (with the <code>spreadMethod</code> parameter set to <code>SpreadMethod.REFLECT</code>). The different interpolation methods affect the appearance as follows:
   * <table>
   * <tr>
   * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/beginGradientFill_interp_linearrgb.jpg" /></td>
   * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/beginGradientFill_interp_rgb.jpg" /></td></tr>
   * <tr>
   * <td><code>InterpolationMethod.LINEAR_RGB</code></td>
   * <td><code>InterpolationMethod.RGB</code></td></tr></table>
   * @param focalPointRatio A number that controls the location of the focal point of the gradient. The value 0 means the focal point is in the center. The value 1 means the focal point is at one border of the gradient circle. The value -1 means that the focal point is at the other border of the gradient circle. Values less than -1 or greater than 1 are rounded to -1 or 1. The following image shows a gradient with a <code>focalPointRatio</code> of -0.75:
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/radial_sketch.jpg" /></p>
   *
   * @see #lineStyle()
   * @see #lineBitmapStyle()
   * @see flash.geom.Matrix#createGradientBox()
   * @see GradientType
   * @see SpreadMethod
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ddb.html Using Matrix objects
   *
   * @example The following example draws a rectangle and a circle that use a gradient stroke from red to green to blue.
   * <p>The method <code>createGradientBox()</code> from the <code>Matrix</code> class is used to define the gradient box to 200 width and 40 height. The thickness of line is set to 5 pixels. Thickness of the stroke must be defined for <code>lineGradientStyle()</code> method. The gradient is set to linear. Colors for the gradient are set to red, green, and blue. Transparency (alpha value) for the colors is set to 1 (opaque). The distribution of gradient is even, where the colors are sampled at 100% at 0 (left-hand position in the gradient box), 128 (middle in the box) and 255 (right-hand position in the box). The width of the rectangle encompasses all the spectrum of the gradient, while the circle encompasses 50% from the middle of the spectrum.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.display.Shape;
   *     import flash.geom.Matrix;
   *     import flash.display.GradientType;
   *
   *     public class Graphics_lineGradientStyleExample extends Sprite
   *     {
   *         public function Graphics_lineGradientStyleExample()
   *         {
   *             var myShape:Shape = new Shape();
   *             var gradientBoxMatrix:Matrix = new Matrix();
   *
   *             gradientBoxMatrix.createGradientBox(200, 40, 0, 0, 0);
   *
   *             myShape.graphics.lineStyle(5);
   *
   *             myShape.graphics.lineGradientStyle(GradientType.LINEAR, [0xFF0000,
   *             0x00FF00, 0x0000FF], [1, 1, 1], [0, 128, 255], gradientBoxMatrix);
   *
   *             myShape.graphics.drawRect(0, 0, 200, 40);
   *             myShape.graphics.drawCircle(100, 120, 50);
   *
   *             this.addChild(myShape);
   *
   *         }
   *     }
   * }
   * </listing>
   */
  "public function lineGradientStyle",function lineGradientStyle(type/*:String*/, colors/*:Array*/, alphas/*:Array*/, ratios/*:Array*/, matrix/*:Matrix = null*/, spreadMethod/*:String = "pad"*/, interpolationMethod/*:String = "rgb"*/, focalPointRatio/*:Number = 0*/)/*:void*/ {if(arguments.length<8){if(arguments.length<7){if(arguments.length<6){if(arguments.length<5){matrix = null;}spreadMethod = "pad";}interpolationMethod = "rgb";}focalPointRatio = 0;}
    this.context$1.strokeStyle = this.createGradientStyle$1(type, colors, alphas, ratios,
      matrix, spreadMethod, interpolationMethod, focalPointRatio);
  },

  /**
   * Specifies a line style used for subsequent calls to Graphics methods such as the <code>lineTo()</code> method or the <code>drawCircle()</code> method. The line style remains in effect until you call the <code>lineGradientStyle()</code> method, the <code>lineBitmapStyle()</code> method, or the <code>lineStyle()</code> method with different parameters.
   * <p>You can call the <code>lineStyle()</code> method in the middle of drawing a path to specify different styles for different line segments within the path.</p>
   * <p><b>Note:</b> Calls to the <code>clear()</code> method set the line style back to <code>undefined</code>.</p>
   * <p><b>Note:</b> Flash Lite 4 supports only the first three parameters (<code>thickness</code>, <code>color</code>, and <code>alpha</code>).</p>
   * @param thickness An integer that indicates the thickness of the line in points; valid values are 0-255. If a number is not specified, or if the parameter is undefined, a line is not drawn. If a value of less than 0 is passed, the default is 0. The value 0 indicates hairline thickness; the maximum thickness is 255. If a value greater than 255 is passed, the default is 255.
   * @param color A hexadecimal color value of the line; for example, red is 0xFF0000, blue is 0x0000FF, and so on. If a value is not indicated, the default is 0x000000 (black). Optional.
   * @param alpha A number that indicates the alpha value of the color of the line; valid values are 0 to 1. If a value is not indicated, the default is 1 (solid). If the value is less than 0, the default is 0. If the value is greater than 1, the default is 1.
   * @param pixelHinting (Not supported in Flash Lite 4) A Boolean value that specifies whether to hint strokes to full pixels. This affects both the position of anchors of a curve and the line stroke size itself. With <code>pixelHinting</code> set to <code>true</code>, line widths are adjusted to full pixel widths. With <code>pixelHinting</code> set to <code>false</code>, disjoints can appear for curves and straight lines. For example, the following illustrations show how Flash Player or Adobe AIR renders two rounded rectangles that are identical, except that the <code>pixelHinting</code> parameter used in the <code>lineStyle()</code> method is set differently (the images are scaled by 200%, to emphasize the difference):
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/lineStyle_pixelHinting.jpg" /></p>
   * <p>If a value is not supplied, the line does not use pixel hinting.</p>
   * @param scaleMode (Not supported in Flash Lite 4) A value from the LineScaleMode class that specifies which scale mode to use:
   * <ul>
   * <li><code>LineScaleMode.NORMAL</code>�Always scale the line thickness when the object is scaled (the default).</li>
   * <li><code>LineScaleMode.NONE</code>�Never scale the line thickness.</li>
   * <li><code>LineScaleMode.VERTICAL</code>�Do not scale the line thickness if the object is scaled vertically <i>only</i>. For example, consider the following circles, drawn with a one-pixel line, and each with the <code>scaleMode</code> parameter set to <code>LineScaleMode.VERTICAL</code>. The circle on the left is scaled vertically only, and the circle on the right is scaled both vertically and horizontally:
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/LineScaleMode_VERTICAL.jpg" /></p></li>
   * <li><code>LineScaleMode.HORIZONTAL</code>�Do not scale the line thickness if the object is scaled horizontally <i>only</i>. For example, consider the following circles, drawn with a one-pixel line, and each with the <code>scaleMode</code> parameter set to <code>LineScaleMode.HORIZONTAL</code>. The circle on the left is scaled horizontally only, and the circle on the right is scaled both vertically and horizontally:
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/LineScaleMode_HORIZONTAL.jpg" /></p></li></ul>
   * @param caps (Not supported in Flash Lite 4) A value from the CapsStyle class that specifies the type of caps at the end of lines. Valid values are: <code>CapsStyle.NONE</code>, <code>CapsStyle.ROUND</code>, and <code>CapsStyle.SQUARE</code>. If a value is not indicated, Flash uses round caps.
   * <p>For example, the following illustrations show the different <code>capsStyle</code> settings. For each setting, the illustration shows a blue line with a thickness of 30 (for which the <code>capsStyle</code> applies), and a superimposed black line with a thickness of 1 (for which no <code>capsStyle</code> applies):</p>
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/linecap.jpg" /></p>
   * @param joints (Not supported in Flash Lite 4) A value from the JointStyle class that specifies the type of joint appearance used at angles. Valid values are: <code>JointStyle.BEVEL</code>, <code>JointStyle.MITER</code>, and <code>JointStyle.ROUND</code>. If a value is not indicated, Flash uses round joints.
   * <p>For example, the following illustrations show the different <code>joints</code> settings. For each setting, the illustration shows an angled blue line with a thickness of 30 (for which the <code>jointStyle</code> applies), and a superimposed angled black line with a thickness of 1 (for which no <code>jointStyle</code> applies):</p>
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/linejoin.jpg" /></p>
   * <p><b>Note:</b> For <code>joints</code> set to <code>JointStyle.MITER</code>, you can use the <code>miterLimit</code> parameter to limit the length of the miter.</p>
   * @param miterLimit (Not supported in Flash Lite 4) A number that indicates the limit at which a miter is cut off. Valid values range from 1 to 255 (and values outside that range are rounded to 1 or 255). This value is only used if the <code>jointStyle</code> is set to <code>"miter"</code>. The <code>miterLimit</code> value represents the length that a miter can extend beyond the point at which the lines meet to form a joint. The value expresses a factor of the line <code>thickness</code>. For example, with a <code>miterLimit</code> factor of 2.5 and a <code>thickness</code> of 10 pixels, the miter is cut off at 25 pixels.
   * <p>For example, consider the following angled lines, each drawn with a <code>thickness</code> of 20, but with <code>miterLimit</code> set to 1, 2, and 4. Superimposed are black reference lines showing the meeting points of the joints:</p>
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/miterLimit.jpg" /></p>
   * <p>Notice that a given <code>miterLimit</code> value has a specific maximum angle for which the miter is cut off. The following table lists some examples:</p>
   * <table>
   * <tr><th><code>miterLimit</code> value:</th><th>Angles smaller than this are cut off:</th></tr>
   * <tr>
   * <td>1.414</td>
   * <td>90 degrees</td></tr>
   * <tr>
   * <td>2</td>
   * <td>60 degrees</td></tr>
   * <tr>
   * <td>4</td>
   * <td>30 degrees</td></tr>
   * <tr>
   * <td>8</td>
   * <td>15 degrees</td></tr></table>
   *
   * @see #lineBitmapStyle()
   * @see #lineGradientStyle()
   * @see LineScaleMode
   * @see CapsStyle
   * @see JointStyle
   *
   * @example <a href="http://www.adobe.com/go/learn_as3_usingexamples_en">How to use this example</a>Please see the <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/display/Graphics.html#lineTo()">lineTo()</a> or <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/display/Graphics.html#moveTo()">moveTo()</a> method's example for illustrations of how to use the <code>getStyle()</code> method.
   */
  "public function lineStyle",function lineStyle(thickness/*:Number = NaN*/, color/*:uint = 0*/, alpha/*:Number = 1.0*/, pixelHinting/*:Boolean = false*/, scaleMode/*:String = "normal"*/, caps/*:String = null*/, joints/*:String = null*/, miterLimit/*:Number = 3*/)/*:void*/ {if(arguments.length<8){if(arguments.length<7){if(arguments.length<6){if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){thickness = NaN;}color = 0;}alpha = 1.0;}pixelHinting = false;}scaleMode = "normal";}caps = null;}joints = null;}miterLimit = 3;}
    if (!isNaN(thickness)) {
      this.context$1.lineWidth = thickness || 1;
    }
    this.context$1.strokeStyle = flash.display.Graphics.toRGBA(color, alpha);
    this.context$1.lineCap = caps || flash.display.CapsStyle.ROUND;
    this.context$1.lineJoin = joints || flash.display.JointStyle.ROUND;
    this.context$1.miterLimit = miterLimit;
  },

  /**
   * Draws a line using the current line style from the current drawing position to (<code>x</code>, <code>y</code>); the current drawing position is then set to (<code>x</code>, <code>y</code>). If the display object in which you are drawing contains content that was created with the Flash drawing tools, calls to the <code>lineTo()</code> method are drawn underneath the content. If you call <code>lineTo()</code> before any calls to the <code>moveTo()</code> method, the default position for the current drawing is (<i>0, 0</i>). If any of the parameters are missing, this method fails and the current drawing position is not changed.
   * @param x A number that indicates the horizontal position relative to the registration point of the parent display object (in pixels).
   * @param y A number that indicates the vertical position relative to the registration point of the parent display object (in pixels).
   *
   * @example The following example draws a trapezoid using <code>lineTo()</code> method, starting at pixels (100, 100).
   * <p>The line thickness is set to 10 pixels, color is gold and opaque, caps at the end of lines is set to none (since all lines are jointed), and the joint between the lines is set to <code>MITER</code> with miter limit set to 10, to have sharp, pointed corners.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.display.LineScaleMode;
   *     import flash.display.CapsStyle;
   *     import flash.display.JointStyle;
   *     import flash.display.Shape;
   *
   *
   *     public class Graphics_lineToExample extends Sprite {
   *
   *         public function Graphics_lineToExample() {
   *
   *             var trapezoid:Shape = new Shape();
   *
   *             trapezoid.graphics.lineStyle(10, 0xFFD700, 1, false, LineScaleMode.VERTICAL,
   *                                CapsStyle.NONE, JointStyle.MITER, 10);
   *
   *             trapezoid.graphics.moveTo(100, 100);
   *
   *             trapezoid.graphics.lineTo(120, 50);
   *             trapezoid.graphics.lineTo(200, 50);
   *             trapezoid.graphics.lineTo(220, 100);
   *             trapezoid.graphics.lineTo(100, 100);
   *
   *             this.addChild(trapezoid);
   *         }
   *     }
   * }
   * </listing>
   */
  "public function lineTo",function lineTo(x/*:Number*/, y/*:Number*/)/*:void*/ {
    this.createSpace$1(Math.max(this.x$1, x), Math.max(this.y$1, y));
    this.x$1 = x;
    this.y$1 = y;
    this.context$1.lineTo(x, y);
    if (!this.insideFill$1) {
      this.context$1.stroke();
      this.context$1.beginPath();
      this.context$1.moveTo(x, y);
    }
  },

  /**
   * Moves the current drawing position to (<code>x</code>, <code>y</code>). If any of the parameters are missing, this method fails and the current drawing position is not changed.
   * @param x A number that indicates the horizontal position relative to the registration point of the parent display object (in pixels).
   * @param y A number that indicates the vertical position relative to the registration point of the parent display object (in pixels).
   *
   * @example The following example draws a dashed line of three pixels thickness using <code>moveTo()</code> and <code>lineTo()</code> methods.
   * <p>Using the <code>lineStyle()</code> method, the line thickness is set to 3 pixels. It is also set not to scale. Color is set to red with 25 percent opacity. The <code>CapsStyle</code> property is set to square (the default is round).</p>
   * <p>Since <code>Graphics_moveToExample</code> is an instance of the <code>Sprite</code> class, it has access to all the Graphics class methods. The Graphics class methods can be used to directly draw on the <code>Graphic_moveToExample</code> Sprite object. However, not putting the vector drawing object in a <code>Shape</code> limits the way they can be managed, moved, or changed.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.display.CapsStyle;
   *     import flash.display.LineScaleMode;
   *
   *     public class Graphics_moveToExample extends Sprite
   *     {
   *         public function Graphics_moveToExample() {
   *
   *             graphics.lineStyle(3, 0x990000, 0.25, false,
   *                             LineScaleMode.NONE, CapsStyle.SQUARE);
   *
   *             graphics.moveTo(10, 20);
   *             graphics.lineTo(20, 20);
   *             graphics.moveTo(30, 20);
   *             graphics.lineTo(50, 20);
   *             graphics.moveTo(60, 20);
   *             graphics.lineTo(80, 20);
   *             graphics.moveTo(90, 20);
   *             graphics.lineTo(110, 20);
   *             graphics.moveTo(120, 20);
   *             graphics.lineTo(130, 20);
   *         }
   *     }
   * }
   * </listing>
   */
  "public function moveTo",function moveTo(x/*:Number*/, y/*:Number*/)/*:void*/ {
    this.context$1.beginPath();
    this.context$1.moveTo(x, y);
    this.x$1 = x;
    this.y$1 = y;
  },

  // ************************** Jangaroo part **************************

  "private static const",{ PIXEL_CHUNK_SIZE/*:int*/ : 100},

  "private var",{ context/* : CanvasRenderingContext2D*/:null},
  "private var",{ insideFill/* : Boolean*/ : false},
  "private var",{ x/*:Number*/ : 0},
  "private var",{ y/*:Number*/ : 0},
  "internal var",{ width/*:Number*/ : 0},
  "internal var",{ height/*:Number*/ : 0},

  /**
   * @private
   */
  "public function Graphics",function Graphics$() {
    var canvas/* : HTMLCanvasElement*/ =as( window.document.createElement("canvas"),  js.HTMLCanvasElement);
    canvas.width = $$private.PIXEL_CHUNK_SIZE;
    canvas.height = $$private.PIXEL_CHUNK_SIZE;
    canvas.style.position = "absolute";

    this.context$1 =/* js.CanvasRenderingContext2D*/(canvas.getContext("2d"));
    // switch to Flash defaults:
    this.context$1.beginPath();
    this.context$1.moveTo(0, 0);
    this.context$1.lineCap = flash.display.CapsStyle.ROUND;
    this.context$1.lineJoin = flash.display.JointStyle.ROUND;
    this.context$1.miterLimit = 3;
  },

  "internal function get canvas",function canvas$get()/* : HTMLCanvasElement*/ {
    return this.context$1.canvas;
  },

  "internal function get renderingContext",function renderingContext$get()/* : CanvasRenderingContext2D*/ {
    return this.context$1;
  },

  "private function createSpace",function createSpace(width/*:Number*/, height/*:Number*/)/*:void*/ {
    if (width > this.width || height > this.height) {
      this.width = Math.max(this.width, width);
      this.height = Math.max(this.height, height);
      var canvas/*:HTMLCanvasElement*/ = this.canvas;
      if (width > canvas.width || height > canvas.height) {
        // backup all properties that will be reset by setting width / height:
        var backupStyle/*:Object*/ = {
          fillStyle  : this.context$1.fillStyle,
          lineWidth  : this.context$1.lineWidth,
          strokeStyle: this.context$1.strokeStyle,
          lineCap    : this.context$1.lineCap,
          lineJoin   : this.context$1.lineJoin,
          miterLimit : this.context$1.miterLimit
        };

        if (canvas.width > 0 && canvas.height > 0) {
          var imageData/*:ImageData*/ = this.context$1.getImageData(0, 0, canvas.width, canvas.height);
        }
        canvas.width = Math.max(canvas.width, width + $$private.PIXEL_CHUNK_SIZE);
        canvas.height = Math.max(canvas.height, height + $$private.PIXEL_CHUNK_SIZE);

        //trace("[INFO] enlarged canvas to " + canvas.width + " x " + canvas.height);
        // restore context properties:
        for (var m/*:String*/ in backupStyle) {
          this.context$1[m] = backupStyle[m];
        }
        if (imageData) {
          this.context$1.putImageData(imageData, 0, 0);
        }
        this.context$1.beginPath();
        this.context$1.moveTo(this.x$1, this.y$1);
      }
    }
  },

  "private function _beginFill",function _beginFill(fillStyle/* : Object*/)/* : void*/ {
    this.context$1.beginPath();
    this.context$1.fillStyle = fillStyle;
    this.insideFill$1 = true;
  },

  "private function createGradientStyle",function createGradientStyle(type/*:String*/, colors/*:Array*/, alphas/*:Array*/, ratios/*:Array*/,
                                       matrix/*:Matrix = null*/, spreadMethod/*:String = "pad"*/,
                                       interpolationMethod/*:String = "rgb"*/, focalPointRatio/*:Number = 0*/)/* : CanvasGradient*/ {if(arguments.length<8){if(arguments.length<7){if(arguments.length<6){if(arguments.length<5){matrix = null;}spreadMethod = "pad";}interpolationMethod = "rgb";}focalPointRatio = 0;}
    // TODO: support spreadMethod != "pad" (medium), interpolationMethod == "rgb_linear" (hard)
    // TODO: check enumeration-typed parameters: throw new ArgumentError("<param-name>","2002");
    var gradient/* : CanvasGradient*/;
    var p0/* : Point*/ = new flash.geom.Point(0, 0);
    var p1/* : Point*/ = new flash.geom.Point(-flash.geom.Matrix.MAGIC_GRADIENT_FACTOR/2,0);
    var p2/* : Point*/ = type == flash.display.GradientType.LINEAR
      ? new flash.geom.Point(0,-flash.geom.Matrix.MAGIC_GRADIENT_FACTOR/2)
      : new flash.geom.Point(flash.geom.Matrix.MAGIC_GRADIENT_FACTOR/2 * focalPointRatio, 0);
    if (matrix) {
      p0 = matrix.transformPoint(p0);
      p1 = matrix.transformPoint(p1);
      p2 = matrix.transformPoint(p2);
    }
    if (type == flash.display.GradientType.LINEAR) {
      var x1/* : Number*/;
      var y1/* : Number*/;
      if (p2.x==p0.x) {
        x1 = p1.x;
        y1 = p1.y;
      } else if (p2.y==p0.y) {
        x1 = p1.x;
        y1 = p2.x;
      } else {
        var d/* : Number*/ = -(p2.x - p0.x) / (p2.y - p0.y);
        // d*(x1 - pm.x) + pm.y = -1/d*(x1 - px.x) + px.y =>
        x1 = (p1.x/d + p1.y + d*p0.x - p0.y) / (d + 1/d);
        y1 = d*(x1 - p0.x) + p0.y;
      }
      var x2/* : Number*/ = p0.x + (p0.x-x1);
      var y2/* : Number*/ = p0.y + (p0.y-y1);
      gradient = this.context$1.createLinearGradient(x1, y1, x2, y2);
    } else { // type == GradientType.RADIAL
      // TODO: support squashed box, i.e. ellipse, not circle! But how? Somehow delegate transform to fill...
      var rx/* : Number*/ = p1.x - p0.x;
      var ry/* : Number*/ = p1.y - p0.y;
      // point distance with optimizations for two typical special cases:
      var r/* : Number*/ = rx==0 ? Math.abs(ry) : ry==0 ? Math.abs(rx) : Math.sqrt(rx*rx+ry*ry);
      gradient = this.context$1.createRadialGradient(p2.x, p2.y, 0, p0.x, p0.y, r);
    }
    for (var i/*:uint*/ = 0; i < colors.length; ++i) {
      gradient.addColorStop(ratios[i] / 255, flash.display.Graphics.toRGBA(colors[i], alphas[i]));
    }
    return gradient;
  },

  /**
   * @private
   */
  "public static function toRGBA",function toRGBA(color/* : uint*/, alpha/* : Number = undefined*/)/* : String*/ {
    var params/*:String*/ = [color >>> 16 & 0xFF, color >>> 8 & 0xFF, color & 0xFF].join(",");
    return alpha < 1 ? ["rgba(", params, ",", alpha, ")"].join("")
                     :  "rgb(" + params + ")";
    
  },

  "private static const",{ KAPPA/*:Number*/ :function(){return( 4 * ((Math.sqrt(2) -1) / 3));}},

];},["toRGBA"],["Math","flash.display.CapsStyle","flash.display.JointStyle","js.HTMLCanvasElement","js.CanvasRenderingContext2D","flash.geom.Point","flash.geom.Matrix","flash.display.GradientType"], "0.8.0", "0.8.1"
);