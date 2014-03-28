joo.classLoader.prepare("package flash.display",/* {
import flash.geom.Matrix
import flash.geom.Transform

import js.HTMLElement*/


/**
 * This class is used to create lightweight shapes using the ActionScript drawing application program interface (API). The Shape class includes a <code>graphics</code> property, which lets you access methods from the Graphics class.
 * <p>The Sprite class also includes a <code>graphics</code>property, and it includes other features not available to the Shape class. For example, a Sprite object is a display object container, whereas a Shape object is not (and cannot contain child display objects). For this reason, Shape objects consume less memory than Sprite objects that contain the same graphics. However, a Sprite object supports user input events, while a Shape object does not.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/display/Shape.html#includeExamplesSummary">View the examples</a></p>
 * @see Graphics
 * @see Sprite
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e3c.html Core display classes
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7dce.html Basics of the drawing API
 *
 */
"public class Shape extends flash.display.DisplayObject",3,function($$private){;return[ 
  /**
   * Specifies the Graphics object belonging to this Shape object, where vector drawing commands can occur.
   */
  "public function get graphics",function graphics$get()/*:Graphics*/ {
    return this._graphics$3;
  },

  /**
   * Creates a new Shape object.
   */
  "public function Shape",function Shape$() {
    this.super$3();
    this._graphics$3 = new flash.display.Graphics();
  },

  /**
   * @inheritDoc
   */
  "override public function set transform",function transform$set(value/*:Transform*/)/*:void*/ {
    this.transform$3 = value;
    var m/* : Matrix*/ = value.matrix;
    if (m) {
      this.graphics.renderingContext.setTransform(m.a, m.b, m.c, m.d, m.tx, m.ty);
    }
  },

  /**
   * @inheritDoc
   */
  "override public function get width",function width$get()/*:Number*/ {
    return this._graphics$3.width;
  },

  /**
   * @inheritDoc
   */
  "override public function get height",function height$get()/*:Number*/ {
    return this._graphics$3.height;
  },

  // ************************** Jangaroo part **************************

  /**
   * @private
   */
  "override protected function createElement",function createElement()/* : HTMLElement*/ {
    return this.graphics.canvas;
  },

  "private var",{ _graphics/* : Graphics*/:null},
];},[],["flash.display.DisplayObject","flash.display.Graphics"], "0.8.0", "0.8.2-SNAPSHOT"
);