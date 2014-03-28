joo.classLoader.prepare("package flash.geom",/* {*/



/**
 * The Matrix class represents a transformation matrix that determines how to map points from one coordinate space to another. You can perform various graphical transformations on a display object by setting the properties of a Matrix object, applying that Matrix object to the <code>matrix</code> property of a Transform object, and then applying that Transform object as the <code>transform</code> property of the display object. These transformation functions include translation (<i>x</i> and <i>y</i> repositioning), rotation, scaling, and skewing.
 * <p>Together these types of transformations are known as <i>affine transformations</i>. Affine transformations preserve the straightness of lines while transforming, so that parallel lines stay parallel.</p>
 * <p>To apply a transformation matrix to a display object, you create a Transform object, set its <code>matrix</code> property to the transformation matrix, and then set the <code>transform</code> property of the display object to the Transform object. Matrix objects are also used as parameters of some methods, such as the following:</p>
 * <ul>
 * <li>The <code>draw()</code> method of a BitmapData object</li>
 * <li>The <code>beginBitmapFill()</code> method, <code>beginGradientFill()</code> method, or <code>lineGradientStyle()</code> method of a Graphics object</li></ul>
 * <p>A transformation matrix object is a 3 x 3 matrix with the following contents:</p>
 * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/matrix_props1.jpg" /></p>
 * <p>In traditional transformation matrixes, the <code>u</code>, <code>v</code>, and <code>w</code> properties provide extra capabilities. The Matrix class can only operate in two-dimensional space, so it always assumes that the property values <code>u</code> and <code>v</code> are 0.0, and that the property value <code>w</code> is 1.0. The effective values of the matrix are as follows:</p>
 * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/matrix_props2.jpg" /></p>
 * <p>You can get and set the values of all six of the other properties in a Matrix object: <code>a</code>, <code>b</code>, <code>c</code>, <code>d</code>, <code>tx</code>, and <code>ty</code>.</p>
 * <p>The Matrix class supports the four major types of transformations: translation, scaling, rotation, and skewing. You can set three of these transformations by using specialized methods, as described in the following table:</p>
 * <table>
 * <tr><th>Transformation</th><th>Method</th><th>Matrix values</th><th>Display result</th><th>Description</th></tr>
 * <tr>
 * <td>Translation (displacement)</td>
 * <td><code>translate(tx, ty)</code> </td>
 * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/matrix_translate.jpg" /> </td>
 * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/matrix_translate_image.jpg" /> </td>
 * <td>Moves the image <code>tx</code> pixels to the right and <code>ty</code> pixels down.</td></tr>
 * <tr>
 * <td>Scaling</td>
 * <td><code>scale(sx, sy)</code> </td>
 * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/matrix_scale.jpg" /> </td>
 * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/matrix_scale_image.jpg" /> </td>
 * <td>Resizes the image, multiplying the location of each pixel by <code>sx</code> on the <i>x</i> axis and <code>sy</code> on the <i>y</i> axis.</td></tr>
 * <tr>
 * <td>Rotation</td>
 * <td><code>rotate(q)</code> </td>
 * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/matrix_rotate.jpg" /> </td>
 * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/matrix_rotate_image.jpg" /> </td>
 * <td>Rotates the image by an angle <code>q</code>, which is measured in radians.</td></tr>
 * <tr>
 * <td>Skewing or shearing</td>
 * <td>None; must set the properties <code>b</code> and <code>c</code> </td>
 * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/matrix_skew.jpg" /> </td>
 * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/matrix_skew_image.jpg" /> </td>
 * <td>Progressively slides the image in a direction parallel to the <i>x</i> or <i>y</i> axis. The <code>b</code> property of the Matrix object represents the tangent of the skew angle along the <i>y</i> axis; the <code>c</code> property of the Matrix object represents the tangent of the skew angle along the <i>x</i> axis.</td></tr></table>
 * <p>Each transformation function alters the current matrix properties so that you can effectively combine multiple transformations. To do this, you call more than one transformation function before applying the matrix to its display object target (by using the <code>transform</code> property of that display object).</p>
 * <p>Use the <code>new Matrix()</code> constructor to create a Matrix object before you can call the methods of the Matrix object.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/geom/Matrix.html#includeExamplesSummary">View the examples</a></p>
 * @see flash.display.DisplayObject#transform
 * @see Transform
 * @see flash.display.BitmapData#draw()
 * @see flash.display.Graphics#beginBitmapFill()
 * @see flash.display.Graphics#beginGradientFill()
 * @see flash.display.Graphics#lineGradientStyle()
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WSda78ed3a750d6b8fee1b36612357de97a3-8000.html Using MXML graphics
 *
 */
"public class Matrix",1,function($$private){;return[ 
  /**
   * The value that affects the positioning of pixels along the <i>x</i> axis when scaling or rotating an image.
   * @example The following example creates the Matrix object <code>myMatrix</code> and sets its <code>a</code> value.
   * <listing>
   * import flash.geom.Matrix;
   *
   * var myMatrix:Matrix = new Matrix();
   * trace(myMatrix.a);  // 1
   *
   * myMatrix.a = 2;
   * trace(myMatrix.a);  // 2
   * </listing>
   */
  "public var",{ a/*:Number*/:NaN},
  /**
   * The value that affects the positioning of pixels along the <i>y</i> axis when rotating or skewing an image.
   * @example The following example creates the Matrix object <code>myMatrix</code> and sets its <code>b</code> value.
   * <listing>
   * import flash.geom.Matrix;
   *
   * var myMatrix:Matrix = new Matrix();
   * trace(myMatrix.b);  // 0
   *
   * var degrees:Number = 30;
   * var radians:Number = (degrees/180) * Math.PI;
   * myMatrix.b = Math.tan(radians);
   * trace(myMatrix.b);  // 0.5773502691896257
   * </listing>
   */
  "public var",{ b/*:Number*/:NaN},
  /**
   * The value that affects the positioning of pixels along the <i>x</i> axis when rotating or skewing an image.
   * @example The following example creates the Matrix object <code>myMatrix</code> and sets its <code>c</code> value.
   * <listing>
   * import flash.geom.Matrix;
   *
   * var myMatrix:Matrix = new Matrix();
   * trace(myMatrix.c);  // 0
   *
   * var degrees:Number = 30;
   * var radians:Number = (degrees/180) * Math.PI;
   * myMatrix.c = Math.tan(radians);
   * trace(myMatrix.c);  // 0.5773502691896257
   * </listing>
   */
  "public var",{ c/*:Number*/:NaN},
  /**
   * The value that affects the positioning of pixels along the <i>y</i> axis when scaling or rotating an image.
   * @example The following example creates the Matrix object <code>myMatrix</code> and sets its <code>d</code> value.
   * <listing>
   * import flash.geom.Matrix;
   *
   * var myMatrix:Matrix = new Matrix();
   * trace(myMatrix.d);  // 1
   *
   * myMatrix.d = 2;
   * trace(myMatrix.d);  // 2
   * </listing>
   */
  "public var",{ d/*:Number*/:NaN},
  /**
   * The distance by which to translate each point along the <i>x</i> axis.
   * @example The following example creates the Matrix object <code>myMatrix</code> and sets its <code>tx</code> value.
   * <listing>
   * import flash.geom.Matrix;
   *
   * var myMatrix:Matrix = new Matrix();
   * trace(myMatrix.tx);  // 0
   *
   * myMatrix.tx = 50;  // 50
   * trace(myMatrix.tx);
   * </listing>
   */
  "public var",{ tx/*:Number*/:NaN},
  /**
   * The distance by which to translate each point along the <i>y</i> axis.
   * @example The following example creates the Matrix object <code>myMatrix</code> and sets its <code>ty</code> value.
   * <listing>
   * import flash.geom.Matrix;
   *
   * var myMatrix:Matrix = new Matrix();
   * trace(myMatrix.ty);  // 0
   *
   * myMatrix.ty = 50;
   * trace(myMatrix.ty);  // 50
   * </listing>
   */
  "public var",{ ty/*:Number*/:NaN},

  /**
   * Creates a new Matrix object with the specified parameters. In matrix notation, the properties are organized like this:
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/matrix_props2.jpg" /></p>
   * <p>If you do not provide any parameters to the <code>new Matrix()</code> constructor, it creates an <i>identity matrix</i> with the following values:</p>a = 1
   * <pre>b = 0</pre>
   * <pre>c = 0</pre>
   * <pre>d = 1</pre>
   * <pre>tx = 0</pre>
   * <pre>ty = 0</pre>
   * <p>In matrix notation, the identity matrix looks like this:</p>
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/matrix_identity.jpg" /></p>
   * @param a The value that affects the positioning of pixels along the <i>x</i> axis when scaling or rotating an image.
   * @param b The value that affects the positioning of pixels along the <i>y</i> axis when rotating or skewing an image.
   * @param c The value that affects the positioning of pixels along the <i>x</i> axis when rotating or skewing an image.
   * @param d The value that affects the positioning of pixels along the <i>y</i> axis when scaling or rotating an image..
   * @param tx The distance by which to translate each point along the <i>x</i> axis.
   * @param ty The distance by which to translate each point along the <i>y</i> axis.
   *
   * @example The following example creates <code>matrix_1</code> by sending no parameters to the <code>Matrix()</code> constructor and <code>matrix_2</code> by sending parameters to it. Notice that <code>matrix_1</code>, which was created with no parameters, results in an identity matrix with the values <code>a</code>=1, <code>b</code>=0, <code>c</code>=0, <code>d</code>=1, <code>tx</code>=0, <code>ty</code>=0.
   * <listing>
   * import flash.geom.Matrix;
   *
   * var matrix_1:Matrix = new Matrix();
   * trace(matrix_1);  // (a=1, b=0, c=0, d=1, tx=0, ty=0)
   *
   * var matrix_2:Matrix = new Matrix(1, 2, 3, 4, 5, 6);
   * trace(matrix_2);  // (a=1, b=2, c=3, d=4, tx=5, ty=6)
   * </listing>
   */
  "public function Matrix",function Matrix$(a/*:Number = 1*/, b/*:Number = 0*/, c/*:Number = 0*/, d/*:Number = 1*/, tx/*:Number = 0*/, ty/*:Number = 0*/) {if(arguments.length<6){if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){a = 1;}b = 0;}c = 0;}d = 1;}tx = 0;}ty = 0;}
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
    this.tx = tx;
    this.ty = ty;
  },

  /**
   * Returns a new Matrix object that is a clone of this matrix, with an exact copy of the contained object.
   * @return A Matrix object.
   *
   */
  "public function clone",function clone()/*:Matrix*/ {
    return new flash.geom.Matrix(this.a, this.b, this.c, this.d, this.tx, this.ty);
  },

  /**
   * Concatenates a matrix with the current matrix, effectively combining the geometric effects of the two. In mathematical terms, concatenating two matrixes is the same as combining them using matrix multiplication.
   * <p>For example, if matrix <code>m1</code> scales an object by a factor of four, and matrix <code>m2</code> rotates an object by 1.5707963267949 radians (<code>Math.PI/2</code>), then <code>m1.concat(m2)</code> transforms <code>m1</code> into a matrix that scales an object by a factor of four and rotates the object by <code>Math.PI/2</code> radians.</p>
   * <p>This method replaces the source matrix with the concatenated matrix. If you want to concatenate two matrixes without altering either of the two source matrixes, first copy the source matrix by using the <code>clone()</code> method, as shown in the Class Examples section.</p>
   * @param m The matrix to be concatenated to the source matrix.
   *
   */
  "public function concat",function concat(m/*:Matrix*/)/*:void*/ {
    var a/* : Number*/ = this.a;
    var b/* : Number*/ = this.b;
    var c/* : Number*/ = this.c;
    var d/* : Number*/ = this.d;
    var tx/* : Number*/ = this.tx;
    var ty/* : Number*/ = this.ty;
    this.a  = m.a*a  + m.c*b;
    this.b  = m.b*a  + m.d*b;
    this.c  = m.a*c  + m.c*d;
    this.d  = m.b*c  + m.d*d;
    this.tx = m.a*tx + m.c*ty + m.tx;
    this.ty = m.b*tx + m.d*ty+m.ty;
  },

  /**
   * Includes parameters for scaling, rotation, and translation. When applied to a matrix it sets the matrix's values based on those parameters.
   * <p>Using the <code>createBox()</code> method lets you obtain the same matrix as you would if you applied the <code>identity()</code>, <code>rotate()</code>, <code>scale()</code>, and <code>translate()</code> methods in succession. For example, <code>mat1.createBox(2,2,Math.PI/4, 100, 100)</code> has the same effect as the following:</p>
   * <listing>
   *      import flash.geom.Matrix;
   *
   *      var mat1:Matrix = new Matrix();
   *      mat1.identity();
   *      mat1.rotate(Math.PI/4);
   *      mat1.scale(2,2);
   *      mat1.translate(10,20);
   *     </listing>
   * @param scaleX The factor by which to scale horizontally.
   * @param scaleY The factor by which scale vertically.
   * @param rotation The amount to rotate, in radians.
   * @param tx The number of pixels to translate (move) to the right along the <i>x</i> axis.
   * @param ty The number of pixels to translate (move) down along the <i>y</i> axis.
   *
   * @see flash.display.Graphics#beginBitmapFill()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ddb.html Using Matrix objects
   *
   * @example The following example sets the x scale, y scale, rotation, x location, and y location of <code>myMatrix</code> by calling its <code>createBox()</code> method.
   * <listing>
   * package
   * {
   *     import flash.display.Shape;
   *     import flash.display.Sprite;
   *     import flash.geom.Matrix;
   *     import flash.geom.Transform;
   *
   *     public class Matrix_createBox extends Sprite
   *     {
   *         public function Matrix_createBox()
   *         {
   *              var myMatrix:Matrix = new Matrix();
   *              trace(myMatrix.toString());  // (a=1, b=0, c=0, d=1, tx=0, ty=0)
   *
   *              myMatrix.createBox(1, 2, Math.PI/4, 50, 100);
   *              trace(myMatrix.toString());
   *              // (a=0.7071067811865476, b=1.414213562373095, c=-0.7071067811865475,
   *              //  d=1.4142135623730951, tx=100, ty=200)
   *
   *              var rectangleShape:Shape = createRectangle(20, 80, 0xFF0000);
   *              addChild(rectangleShape);
   *
   *              var rectangleTrans:Transform = new Transform(rectangleShape);
   *              rectangleTrans.matrix = myMatrix;
   *         }
   *
   *         public function createRectangle(w:Number, h:Number, color:Number):Shape
   *         {
   *             var rect:Shape = new Shape();
   *             rect.graphics.beginFill(color);
   *             rect.graphics.drawRect(0, 0, w, h);
   *             addChild(rect);
   *             return rect;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function createBox",function createBox(scaleX/*:Number*/, scaleY/*:Number*/, rotation/*:Number = 0*/, tx/*:Number = 0*/, ty/*:Number = 0*/)/*:void*/ {if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){rotation = 0;}tx = 0;}ty = 0;}
    // all inlined for higher performance:
    if (rotation == 0) {
      this.a = this.d = 1;
      this.b = this.c = 0;
    } else {
      this.a = Math.cos(rotation);
      this.b = Math.sin(rotation);
      this.c = -this.b;
      this.d = this.a;
    }
    if (scaleX != 1) {
      this.a *= scaleX;
      this.c *= scaleY;
    }
    if (scaleY != 1) {
      this.b *= scaleY;
      this.d *= scaleY;
    }
    this.tx = tx;
    this.ty = ty;
  },

  /**
   * Creates the specific style of matrix expected by the <code>beginGradientFill()</code> and <code>lineGradientStyle()</code> methods of the Graphics class. Width and height are scaled to a <code>scaleX</code>/<code>scaleY</code> pair and the <code>tx</code>/<code>ty</code> values are offset by half the width and height.
   * <p>For example, consider a gradient with the following characteristics:</p>
   * <ul>
   * <li><code>GradientType.LINEAR</code></li>
   * <li>Two colors, green and blue, with the ratios array set to <code>[0, 255]</code></li>
   * <li><code>SpreadMethod.PAD</code></li>
   * <li><code>InterpolationMethod.LINEAR_RGB</code></li></ul>
   * <p>The following illustrations show gradients in which the matrix was defined using the <code>createGradientBox()</code> method with different parameter settings:</p>
   * <table>
   * <tr><th><code>createGradientBox()</code> settings</th><th>Resulting gradient</th></tr>
   * <tr>
   * <td>
   * <pre>width = 25;
   height = 25;
   rotation = 0;
   tx = 0;
   ty = 0;</pre></td>
   * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/createGradientBox-1.jpg" /></td></tr>
   * <tr>
   * <td>
   * <pre>width = 25;
   height = 25;
   rotation = 0;
   tx = 25;
   ty = 0;</pre></td>
   * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/createGradientBox-2.jpg" /></td></tr>
   * <tr>
   * <td>
   * <pre>width = 50;
   height = 50;
   rotation = 0;
   tx = 0;
   ty = 0;</pre></td>
   * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/createGradientBox-3.jpg" /></td></tr>
   * <tr>
   * <td>
   * <pre>width = 50;
   height = 50;
   rotation = Math.PI / 4; // 45 degrees
   tx = 0;
   ty = 0;</pre></td>
   * <td><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/createGradientBox-4.jpg" /></td></tr></table>
   * @param width The width of the gradient box.
   * @param height The height of the gradient box.
   * @param rotation The amount to rotate, in radians.
   * @param tx The distance, in pixels, to translate to the right along the <i>x</i> axis. This value is offset by half of the <code>width</code> parameter.
   * @param ty The distance, in pixels, to translate down along the <i>y</i> axis. This value is offset by half of the <code>height</code> parameter.
   *
   * @see flash.display.Graphics#beginGradientFill()
   * @see flash.display.Graphics#lineGradientStyle()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ddb.html Using Matrix objects
   *
   * @example The following example sets the x scale, y scale, rotation, x location, and y location of <code>myMatrix</code> by calling its <code>createBox()</code> method.
   * <listing>
   * package
   * {
   *     import flash.display.GradientType;
   *     import flash.display.Sprite;
   *     import flash.geom.Matrix;
   *
   *     public class Matrix_createGradientBox extends Sprite
   *     {
   *         public function Matrix_createGradientBox()
   *         {
   *              var myMatrix:Matrix = new Matrix();
   *              trace(myMatrix.toString());          // (a=1, b=0, c=0, d=1, tx=0, ty=0)
   *
   *              myMatrix.createGradientBox(200, 200, 0, 50, 50);
   *              trace(myMatrix.toString());          // (a=0.1220703125, b=0, c=0, d=0.1220703125, tx=150, ty=150)
   *
   *              var colors:Array = [0xFF0000, 0x0000FF];
   *              var alphas:Array = [100, 100];
   *              var ratios:Array = [0, 0xFF];
   *
   *              this.graphics.beginGradientFill(GradientType.LINEAR, colors, alphas, ratios, myMatrix);
   *              this.graphics.drawRect(0, 0, 300, 200);
   *         }
   *     }
   * }
   * </listing>
   */
  "public function createGradientBox",function createGradientBox(width/*:Number*/, height/*:Number*/, rotation/*:Number = 0*/, tx/*:Number = 0*/, ty/*:Number = 0*/)/*:void*/ {if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){rotation = 0;}tx = 0;}ty = 0;}
    this.createBox(width/flash.geom.Matrix.MAGIC_GRADIENT_FACTOR, height/flash.geom.Matrix.MAGIC_GRADIENT_FACTOR, rotation, tx + width/2, ty + height/2);
  },

  /**
   * Given a point in the pretransform coordinate space, returns the coordinates of that point after the transformation occurs. Unlike the standard transformation applied using the <code>transformPoint()</code> method, the <code>deltaTransformPoint()</code> method's transformation does not consider the translation parameters <code>tx</code> and <code>ty</code>.
   * @param point The point for which you want to get the result of the matrix transformation.
   *
   * @return The point resulting from applying the matrix transformation.
   *
   */
  "public function deltaTransformPoint",function deltaTransformPoint(point/*:Point*/)/*:Point*/ {
    return new flash.geom.Point(this.a*point.x + this.c*point.y, this.b*point.x + this.d*point.y);
  },

  /**
   * Sets each matrix property to a value that causes a null transformation. An object transformed by applying an identity matrix will be identical to the original.
   * <p>After calling the <code>identity()</code> method, the resulting matrix has the following properties: <code>a</code>=1, <code>b</code>=0, <code>c</code>=0, <code>d</code>=1, <code>tx</code>=0, <code>ty</code>=0.</p>
   * <p>In matrix notation, the identity matrix looks like this:</p>
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/matrix_identity.jpg" /></p>
   */
  "public function identity",function identity()/*:void*/ {
    this.a = this.d = 1;
    this.b = this.c = this.tx = this.ty = 0;
  },

  /**
   * Performs the opposite transformation of the original matrix. You can apply an inverted matrix to an object to undo the transformation performed when applying the original matrix.
   * @example The following example creates a <code>halfScaleMatrix</code> by calling the <code>invert()</code> method of <code>doubleScaleMatrix</code>. It then demonstrates that the two are Matrix inverses of one another -- matrices that undo any transformations performed by the other -- by creating <code>originalAndInverseMatrix</code> which is equal to <code>noScaleMatrix</code>.
   * <listing>
   * package
   * {
   *     import flash.display.Shape;
   *     import flash.display.Sprite;
   *     import flash.geom.Matrix;
   *     import flash.geom.Transform;
   *
   *     public class Matrix_invert extends Sprite
   *     {
   *         public function Matrix_invert()
   *         {
   *             var rect0:Shape = createRectangle(20, 80, 0xFF0000);
   *             var rect1:Shape = createRectangle(20, 80, 0x00FF00);
   *             var rect2:Shape = createRectangle(20, 80, 0x0000FF);
   *             var rect3:Shape = createRectangle(20, 80, 0x000000);
   *
   *             var trans0:Transform = new Transform(rect0);
   *             var trans1:Transform = new Transform(rect1);
   *             var trans2:Transform = new Transform(rect2);
   *             var trans3:Transform = new Transform(rect3);
   *
   *             var doubleScaleMatrix:Matrix = new Matrix(2, 0, 0, 2, 0, 0);
   *             trans0.matrix = doubleScaleMatrix;
   *             trace(doubleScaleMatrix.toString());  // (a=2, b=0, c=0, d=2, tx=0, ty=0)
   *
   *             var noScaleMatrix:Matrix = new Matrix(1, 0, 0, 1, 0, 0);
   *             trans1.matrix = noScaleMatrix;
   *             rect1.x = 50;
   *             trace(noScaleMatrix.toString());  // (a=1, b=0, c=0, d=1, tx=0, ty=0)
   *
   *             var halfScaleMatrix:Matrix = doubleScaleMatrix.clone();
   *             halfScaleMatrix.invert();
   *             trans2.matrix = halfScaleMatrix;
   *             rect2.x = 100;
   *             trace(halfScaleMatrix.toString());  // (a=0.5, b=0, c=0, d=0.5, tx=0, ty=0)
   *
   *             var originalAndInverseMatrix:Matrix = doubleScaleMatrix.clone();
   *             originalAndInverseMatrix.concat(halfScaleMatrix);
   *             trans3.matrix = originalAndInverseMatrix;
   *             rect3.x = 150;
   *             trace(originalAndInverseMatrix.toString());  // (a=1, b=0, c=0, d=1, tx=0, ty=0)
   *         }
   *
   *         public function createRectangle(w:Number, h:Number, color:Number):Shape
   *         {
   *             var rect:Shape = new Shape();
   *             rect.graphics.beginFill(color);
   *             rect.graphics.drawRect(0, 0, w, h);
   *             addChild(rect);
   *             return rect;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function invert",function invert()/*:void*/ {
    var a/* : Number*/ = this.a;
    var b/* : Number*/ = this.b;
    var c/* : Number*/ = this.c;
    var d/* : Number*/ = this.d;
    var tx/* : Number*/ = this.tx;
    var ty/* : Number*/ = this.ty;
    // Cremer's rule: inverse = adjugate / determinant
    // A-1 = adj(A) / det(A)
    var det/* : Number*/ = a*d - c*b;
    //     [a11 a12 a13]
    // A = [a21 a22 a23]
    //     [a31 a32 a33]
    // according to http://de.wikipedia.org/wiki/Inverse_Matrix#Formel_f.C3.BCr_3x3-Matrizen (sorry, German):
    //          [a22*a33-a32*a23 a13*a32-a12*a33 a12*a23-a13*a22]
    // adj(A) = [a23*a31-a21*a33 a11*a33-a13*a31 a13*a21-a11*a23]
    //          [a21*a32-a22*a31 a12*a31-a11*a32 a11*a22-a12*a21]
    // with a11 = a, a12 = c, a13 = tx,
    //      a21 = b, a22 = d, a23 = ty,
    //      a31 = 0, a32 = 0, a33 = 1:
    //          [d *1-0*ty  tx*0-c *1  c *ty-tx*d ]
    // adj(A) = [ty*0-b* 1  a *1-tx*0  tx* b-a *ty]
    //          [b *0-d* 0  c *0-a *0  a * d-c *b ]
    //          [ d -c  c*ty-tx*d]
    //        = [-b  a  tx*b-a*ty]
    //          [ 0  0  a*d -c*b ]
    this.a = d/det;
    this.b = -b/det;
    this.c = -c/det;
    this.d = a/det;
    this.tx = (c*ty-tx*d)/det;
    this.ty = (tx*b-a*ty)/det;
  },

  /**
   * Applies a rotation transformation to the Matrix object.
   * <p>The <code>rotate()</code> method alters the <code>a</code>, <code>b</code>, <code>c</code>, and <code>d</code> properties of the Matrix object. In matrix notation, this is the same as concatenating the current matrix with the following:</p>
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/matrix_rotate.jpg" /></p>
   * @param angle The rotation angle in radians.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ddb.html Using Matrix objects
   *
   */
  "public function rotate",function rotate(angle/*:Number*/)/*:void*/ {
    if (angle!=0) {
      var cos/* : Number*/ = Math.cos(angle);
      var sin/* : Number*/ = Math.sin(angle);
      var a/* : Number*/ = this.a;
      var b/* : Number*/ = this.b;
      var c/* : Number*/ = this.c;
      var d/* : Number*/ = this.d;
      var tx/* : Number*/ = this.tx;
      var ty/* : Number*/ = this.ty;
      this.a   = a*cos  - c*sin;
      this.b   = a*sin  + c*cos;
      this.c   = b*cos  - d*sin;
      this.d   = b*sin  + d*cos;
      this.tx  = tx*cos - ty*sin;
      this.ty  = tx*sin + ty*cos;
    }
  },

  /**
   * Applies a scaling transformation to the matrix. The <i>x</i> axis is multiplied by <code>sx</code>, and the <i>y</i> axis it is multiplied by <code>sy</code>.
   * <p>The <code>scale()</code> method alters the <code>a</code> and <code>d</code> properties of the Matrix object. In matrix notation, this is the same as concatenating the current matrix with the following matrix:</p>
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/matrix_scale.jpg" /></p>
   * @param sx A multiplier used to scale the object along the <i>x</i> axis.
   * @param sy A multiplier used to scale the object along the <i>y</i> axis.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ddb.html Using Matrix objects
   *
   */
  "public function scale",function scale(sx/*:Number*/, sy/*:Number*/)/*:void*/ {
    if (sx != 1) {
      this.a *= sx;
      this.tx *= sx;
    }
    if (sy != 1) {
      this.d *= sy;
      this.ty *= sy;
    }
  },

  /**
   * Returns a text value listing the properties of the Matrix object.
   * @return A string containing the values of the properties of the Matrix object: <code>a</code>, <code>b</code>, <code>c</code>, <code>d</code>, <code>tx</code>, and <code>ty</code>.
   *
   */
  "public function toString",function toString()/*:String*/ {
    return "("+["a="+this.a,"b="+this.b,"c="+this.c,"d="+this.d,"tx="+this.tx,"ty="+this.ty].join(", ")+")";
  },

  /**
   * Returns the result of applying the geometric transformation represented by the Matrix object to the specified point.
   * @param point The point for which you want to get the result of the Matrix transformation.
   *
   * @return The point resulting from applying the Matrix transformation.
   *
   */
  "public function transformPoint",function transformPoint(point/*:Point*/)/*:Point*/ {
    return new flash.geom.Point(this.a*point.x + this.c*point.y + this.tx, this.b*point.x + this.d*point.y + this.ty);
  },

  /**
   * Translates the matrix along the <i>x</i> and <i>y</i> axes, as specified by the <code>dx</code> and <code>dy</code> parameters.
   * @param dx The amount of movement along the <i>x</i> axis to the right, in pixels.
   * @param dy The amount of movement down along the <i>y</i> axis, in pixels.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ddb.html Using Matrix objects
   *
   */
  "public function translate",function translate(dx/*:Number*/, dy/*:Number*/)/*:void*/ {
    this.tx += dx;
    this.ty += dy;
  },

  // ************************** Jangaroo part **************************

  /**
   * @private
   */
  "public static const",{ MAGIC_GRADIENT_FACTOR/*:Number*/ : 16384/10},

  // TODO: Adobe's documentation of this class seems quite incomplete.
  // I used http://www.senocular.com/flash/tutorials/transformmatrix/ to find out the implementation
  // that matches Flash 9.
];},[],["Math","flash.geom.Point"], "0.8.0", "0.8.3"
);