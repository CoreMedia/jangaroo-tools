joo.classLoader.prepare("package flash.geom",/* {*/


/**
 * The Point object represents a location in a two-dimensional coordinate system, where <i>x</i> represents the horizontal axis and <i>y</i> represents the vertical axis.
 * <p>The following code creates a point at (0,0):</p>
 * <listing>
 * var myPoint:Point = new Point();</listing>
 * <p>Methods and properties of the following classes use Point objects:</p>
 * <ul>
 * <li>BitmapData</li>
 * <li>DisplayObject</li>
 * <li>DisplayObjectContainer</li>
 * <li>DisplacementMapFilter</li>
 * <li>NativeWindow</li>
 * <li>Matrix</li>
 * <li>Rectangle</li></ul>
 * <p>You can use the <code>new Point()</code> constructor to create a Point object.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/geom/Point.html#includeExamplesSummary">View the examples</a></p>
 * @see flash.display.BitmapData
 * @see flash.display.DisplayObject
 * @see flash.display.DisplayObjectContainer
 * @see flash.filters.DisplacementMapFilter
 * @see Matrix
 * @see flash.display.NativeWindow
 * @see Rectangle
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7dca.html Using Point objects
 *
 */
"public class Point",1,function($$private){;return[ 
  /**
   * The length of the line segment from (0,0) to this point.
   * @see #polar()
   *
   */
  "public function get length",function length$get()/*:Number*/ {
    return Math.sqrt(this.x^2 + this.y^2);
  },

  /**
   * The horizontal coordinate of the point. The default value is 0.
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7dca.html Using Point objects
   *
   */
  "public var",{ x/*:Number*/:NaN},
  /**
   * The vertical coordinate of the point. The default value is 0.
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7dca.html Using Point objects
   *
   */
  "public var",{ y/*:Number*/:NaN},

  /**
   * Creates a new point. If you pass no parameters to this method, a point is created at (0,0).
   * @param x The horizontal coordinate.
   * @param y The vertical coordinate.
   *
   */
  "public function Point",function Point$(x/*:Number = 0*/, y/*:Number = 0*/) {if(arguments.length<2){if(arguments.length<1){x = 0;}y = 0;}
    this.x = x;
    this.y = y;
  },

  /**
   * Adds the coordinates of another point to the coordinates of this point to create a new point.
   * @param v The point to be added.
   *
   * @return The new point.
   *
   */
  "public function add",function add(v/*:Point*/)/*:Point*/ {
    return new flash.geom.Point(this.x+v.x, this.y+v.y);
  },

  /**
   * Creates a copy of this Point object.
   * @return The new Point object.
   *
   */
  "public function clone",function clone()/*:Point*/ {
    return new flash.geom.Point(this.x, this.y);
  },

  /**
   * Returns the distance between <code>pt1</code> and <code>pt2</code>.
   * @param pt1 The first point.
   * @param pt2 The second point.
   *
   * @return The distance between the first and second points.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7dca.html Using Point objects
   *
   */
  "public static function distance",function distance(pt1/*:Point*/, pt2/*:Point*/)/*:Number*/ {
    return Math.sqrt((pt2.x-pt1.x)^2 + (pt2.y-pt2.y)^2);
  },

  /**
   * Determines whether two points are equal. Two points are equal if they have the same <i>x</i> and <i>y</i> values.
   * @param toCompare The point to be compared.
   *
   * @return A value of <code>true</code> if the object is equal to this Point object; <code>false</code> if it is not equal.
   *
   */
  "public function equals",function equals(toCompare/*:Point*/)/*:Boolean*/ {
    return this.x == toCompare.x && this.y == toCompare.y;
  },

  /**
   * Determines a point between two specified points. The parameter <code>f</code> determines where the new interpolated point is located relative to the two end points specified by parameters <code>pt1</code> and <code>pt2</code>. The closer the value of the parameter <code>f</code> is to <code>1.0</code>, the closer the interpolated point is to the first point (parameter <code>pt1</code>). The closer the value of the parameter <code>f</code> is to 0, the closer the interpolated point is to the second point (parameter <code>pt2</code>).
   * @param pt1 The first point.
   * @param pt2 The second point.
   * @param f The level of interpolation between the two points. Indicates where the new point will be, along the line between <code>pt1</code> and <code>pt2</code>. If <code>f</code>=1, <code>pt1</code> is returned; if <code>f</code>=0, <code>pt2</code> is returned.
   *
   * @return The new, interpolated point.
   *
   */
  "public static function interpolate",function interpolate(pt1/*:Point*/, pt2/*:Point*/, f/*:Number*/)/*:Point*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Scales the line segment between (0,0) and the current point to a set length.
   * @param thickness The scaling value. For example, if the current point is (0,5), and you normalize it to 1, the point returned is at (0,1).
   *
   * @see #length
   *
   */
  "public function normalize",function normalize(thickness/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Offsets the Point object by the specified amount. The value of <code>dx</code> is added to the original value of <i>x</i> to create the new <i>x</i> value. The value of <code>dy</code> is added to the original value of <i>y</i> to create the new <i>y</i> value.
   * @param dx The amount by which to offset the horizontal coordinate, <i>x</i>.
   * @param dy The amount by which to offset the vertical coordinate, <i>y</i>.
   *
   */
  "public function offset",function offset(dx/*:Number*/, dy/*:Number*/)/*:void*/ {
    this.x += dx;
    this.y += dy;
  },

  /**
   * Converts a pair of polar coordinates to a Cartesian point coordinate.
   * @param len The length coordinate of the polar pair.
   * @param angle The angle, in radians, of the polar pair.
   *
   * @return The Cartesian point.
   *
   * @see #length
   * @see Math#round()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7dca.html Using Point objects
   *
   */
  "public static function polar",function polar(len/*:Number*/, angle/*:Number*/)/*:Point*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Subtracts the coordinates of another point from the coordinates of this point to create a new point.
   * @param v The point to be subtracted.
   *
   * @return The new point.
   *
   */
  "public function subtract",function subtract(v/*:Point*/)/*:Point*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns a string that contains the values of the <i>x</i> and <i>y</i> coordinates. The string has the form <code>"(x=<i>x</i>, y=<i>y</i>)"</code>, so calling the <code>toString()</code> method for a point at 23,17 would return <code>"(x=23, y=17)"</code>.
   * @return The string representation of the coordinates.
   *
   */
  "public function toString",function toString()/*:String*/ {
    return ["(x=",this.x,", y=",this.y,")"].join("");
  },
];},["distance","interpolate","polar"],["Math","Error"], "0.8.0", "0.8.3"
);