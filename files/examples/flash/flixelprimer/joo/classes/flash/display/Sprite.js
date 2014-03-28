joo.classLoader.prepare("package flash.display",/* {
import flash.geom.Rectangle
import flash.media.SoundTransform

import js.Element
import js.HTMLCanvasElement
import flash.geom.Transform
import flash.geom.Matrix*/

/**
 * The Sprite class is a basic display list building block: a display list node that can display graphics and can also contain children.
 * <p>A Sprite object is similar to a movie clip, but does not have a timeline. Sprite is an appropriate base class for objects that do not require timelines. For example, Sprite would be a logical base class for user interface (UI) components that typically do not use the timeline.</p>
 * <p>The Sprite class is new in ActionScript 3.0. It provides an alternative to the functionality of the MovieClip class, which retains all the functionality of previous ActionScript releases to provide backward compatibility.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/display/Sprite.html#includeExamplesSummary">View the examples</a></p>
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf62d75-7feb.html Creating a subclass of Sprite as a download progress bar
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e3c.html Core display classes
 *
 */
"public class Sprite extends flash.display.DisplayObjectContainer",5,function($$private){;return[ 
  /**
   * Specifies the button mode of this sprite. If <code>true</code>, this sprite behaves as a button, which means that it triggers the display of the hand cursor when the pointer passes over the sprite and can receive a <code>click</code> event if the enter or space keys are pressed when the sprite has focus. You can suppress the display of the hand cursor by setting the <code>useHandCursor</code> property to <code>false</code>, in which case the pointer is displayed.
   * <p>Although it is better to use the SimpleButton class to create buttons, you can use the <code>buttonMode</code> property to give a sprite some button-like functionality. To include a sprite in the tab order, set the <code>tabEnabled</code> property (inherited from the InteractiveObject class and <code>false</code> by default) to <code>true</code>. Additionally, consider whether you want the children of your sprite to be user input enabled. Most buttons do not enable user input interactivity for their child objects because it confuses the event flow. To disable user input interactivity for all child objects, you must set the <code>mouseChildren</code> property (inherited from the DisplayObjectContainer class) to <code>false</code>.</p>
   * <p>If you use the <code>buttonMode</code> property with the MovieClip class (which is a subclass of the Sprite class), your button might have some added functionality. If you include frames labeled _up, _over, and _down, Flash Player provides automatic state changes (functionality similar to that provided in previous versions of ActionScript for movie clips used as buttons). These automatic state changes are not available for sprites, which have no timeline, and thus no frames to label.</p>
   * @see SimpleButton
   * @see #useHandCursor
   * @see InteractiveObject#tabEnabled
   * @see DisplayObjectContainer#mouseChildren
   *
   * @example The following example creates two sprites and sets the <code>buttonMode</code> property to <code>true</code> for one and <code>false</code> for the other. When you compile and run the application, both sprites respond to mouse events, but only the one in which <code>buttonMode</code> is set to <code>true</code> uses the hand cursor and is included in the tab order:
   * <listing>
   * import flash.display.Sprite;
   * import flash.events.MouseEvent;
   *
   * var circle1:Sprite = new Sprite();
   * circle1.graphics.beginFill(0xFFCC00);
   * circle1.graphics.drawCircle(40, 40, 40);
   * circle1.buttonMode = true;
   * circle1.addEventListener(MouseEvent.CLICK, clicked);
   *
   * var circle2:Sprite = new Sprite();
   * circle2.graphics.beginFill(0xFFCC00);
   * circle2.graphics.drawCircle(120, 40, 40);
   * circle2.buttonMode = false;
   * circle2.addEventListener(MouseEvent.CLICK, clicked);
   *
   * function clicked(event:MouseEvent):void {
   *     trace ("Click!");
   * }
   *
   * addChild(circle1);
   * addChild(circle2);
   * </listing>
   */
  "public function get buttonMode",function buttonMode$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set buttonMode",function buttonMode$set(value/*:Boolean*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies the display object over which the sprite is being dragged, or on which the sprite was dropped.
   * @see #startDrag()
   * @see #stopDrag()
   *
   * @example The following example creates a <code>circle</code> sprite and two <code>target</code> sprites. The <code>startDrag()</code> method is called on the <code>circle</code> sprite when the user positions the cursor over the sprite and presses the mouse button, and the <code>stopDrag()</code> method is called when the user releases the mouse button. This lets the user drag the sprite. On release of the mouse button, the <code>mouseRelease()</code> method is called, which in turn traces the <code>name</code> of the <code>dropTarget</code> object � the one to which the user dragged the <code>circle</code> sprite:
   * <listing>
   * import flash.display.Sprite;
   * import flash.events.MouseEvent;
   *
   * var circle:Sprite = new Sprite();
   * circle.graphics.beginFill(0xFFCC00);
   * circle.graphics.drawCircle(0, 0, 40);
   *
   * var target1:Sprite = new Sprite();
   * target1.graphics.beginFill(0xCCFF00);
   * target1.graphics.drawRect(0, 0, 100, 100);
   * target1.name = "target1";
   *
   * var target2:Sprite = new Sprite();
   * target2.graphics.beginFill(0xCCFF00);
   * target2.graphics.drawRect(0, 200, 100, 100);
   * target2.name = "target2";
   *
   * addChild(target1);
   * addChild(target2);
   * addChild(circle);
   *
   * circle.addEventListener(MouseEvent.MOUSE_DOWN, mouseDown)
   *
   * function mouseDown(event:MouseEvent):void {
   *     circle.startDrag();
   * }
   * circle.addEventListener(MouseEvent.MOUSE_UP, mouseReleased);
   *
   * function mouseReleased(event:MouseEvent):void {
   *     circle.stopDrag();
   *     trace(circle.dropTarget.name);
   * }
   * </listing>
   */
  "public function get dropTarget",function dropTarget$get()/*:DisplayObject*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies the Graphics object that belongs to this sprite where vector drawing commands can occur.
   * @example The following example creates a <code>circle</code> sprite and uses its <code>graphics</code> property to draw a circle with a yellow (0xFFCC00) fill:
   * <listing>
   * import flash.display.Sprite;
   *
   * var circle:Sprite = new Sprite();
   * circle.graphics.beginFill(0xFFCC00);
   * circle.graphics.drawCircle(40, 40, 40);
   * addChild(circle);
   * </listing>
   */
  "public function get graphics",function graphics$get()/*:Graphics*/ {
    if (!this._graphics$5) {
      this._graphics$5 = new flash.display.Graphics();
      var canvas/* : HTMLCanvasElement*/ = this._graphics$5.canvas;
      var element/* : Element*/ = this.getElement();
      if (element.firstChild) {
        element.insertBefore(canvas, element.firstChild);
      } else {
        element.appendChild(canvas);
      }
    }
    return this._graphics$5;
  },

  /**
   * Designates another sprite to serve as the hit area for a sprite. If the <code>hitArea</code> property does not exist or the value is <code>null</code> or <code>undefined</code>, the sprite itself is used as the hit area. The value of the <code>hitArea</code> property can be a reference to a Sprite object.
   * <p>You can change the <code>hitArea</code> property at any time; the modified sprite immediately uses the new hit area behavior. The sprite designated as the hit area does not need to be visible; its graphical shape, although not visible, is still detected as the hit area.</p>
   * <p><b>Note:</b> You must set to <code>false</code> the <code>mouseEnabled</code> property of the sprite designated as the hit area. Otherwise, your sprite button might not work because the sprite designated as the hit area receives the user input events instead of your sprite button.</p>
   * @example The following example creates a <code>circle</code> sprite and a <code>square</code> sprite. The <code>square</code> sprite is the <code>hitArea</code> for the <code>circle</code> sprite. So when the user clicks the <code>square</code> sprite, the <code>circle</code> sprite dispatches a <code>click</code> event:
   * <listing>
   * import flash.display.Sprite;
   * import flash.events.MouseEvent;
   *
   * var circle:Sprite = new Sprite();
   * circle.graphics.beginFill(0xFFCC00);
   * circle.graphics.drawCircle(0, 0, 40);
   *
   * var square:Sprite = new Sprite();
   * square.graphics.beginFill(0xCCFF00);
   * square.graphics.drawRect(200, 0, 100, 100);
   *
   * circle.hitArea = square;
   * square.mouseEnabled = false;
   *
   * circle.addEventListener(MouseEvent.CLICK, clicked);
   *
   * function clicked(event:MouseEvent):void{
   *     trace(event.target == circle); // true
   *     trace(event.target == square); // false
   * }
   *
   * addChild(circle);
   * addChild(square);
   * </listing>
   */
  "public function get hitArea",function hitArea$get()/*:Sprite*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set hitArea",function hitArea$set(value/*:Sprite*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Controls sound within this sprite.
   * <p><b>Note:</b> This property does not affect HTML content in an HTMLControl object (in Adobe AIR).</p>
   * @see flash.media.SoundTransform
   *
   * @example The following example creates a sprite named <code>container</code> and adds a Loader object to its child list. The Loader object loads a SWF file. When the user clicks the link in the <code>tf</code> text field <code>true</code>, the <code>mute()</code> method sets the <code>volume</code> property of the <code>soundTransform</code> property of the <code>container</code> sprite:
   * <listing>
   * import flash.display.Sprite;
   * import flash.display.Loader;
   * import flash.events.IOErrorEvent;
   * import flash.events.MouseEvent;
   * import flash.net.URLRequest;
   * import flash.text.TextField;
   * import flash.media.SoundTransform;
   *
   * var container:Sprite = new Sprite();
   * addChild(container);
   *
   * var ldr:Loader = new Loader;
   * var urlReq:URLRequest = new URLRequest("SoundPlayer.swf");
   * ldr.load(urlReq);
   *
   * container.addChild(ldr);
   * ldr.contentLoaderInfo.addEventListener(IOErrorEvent.IO_ERROR, urlNotFound);
   *
   * var tf:TextField = new TextField();
   * tf.htmlText = "<a href = 'event:Mute'>Mute / Unmute</a>";
   * addChild(tf);
   *
   * var mySoundTransform:SoundTransform = new SoundTransform();
   * mySoundTransform.volume = 1;
   *
   * tf.addEventListener(MouseEvent.CLICK, mute);
   *
   * function mute(event:MouseEvent):void {
   *     if (mySoundTransform.volume == 0) {
   *         mySoundTransform.volume = 1;
   *     } else {
   *         mySoundTransform.volume = 0;
   *     }
   *     container.soundTransform = mySoundTransform;
   * }
   *
   * function urlNotFound(event:IOErrorEvent):void {
   *     trace("The URL was not found.");
   * }
   * </listing>
   */
  "public function get soundTransform",function soundTransform$get()/*:SoundTransform*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set soundTransform",function soundTransform$set(value/*:SoundTransform*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * A Boolean value that indicates whether the pointing hand (hand cursor) appears when the pointer rolls over a sprite in which the <code>buttonMode</code> property is set to <code>true</code>. The default value of the <code>useHandCursor</code> property is <code>true</code>. If <code>useHandCursor</code> is set to <code>true</code>, the pointing hand used for buttons appears when the pointer rolls over a button sprite. If <code>useHandCursor</code> is <code>false</code>, the arrow pointer is used instead.
   * <p>You can change the <code>useHandCursor</code> property at any time; the modified sprite immediately takes on the new cursor appearance.</p>
   * <p><b>Note:</b> In Flex or Flash Builder, if your sprite has child sprites, you might want to set the <code>mouseChildren</code> property to <code>false</code>. For example, if you want a hand cursor to appear over a Flex <mx:Label> control, set the <code>useHandCursor</code> and <code>buttonMode</code> properties to <code>true</code>, and the <code>mouseChildren</code> property to <code>false</code>.</p>
   * @see #buttonMode
   * @see DisplayObjectContainer#mouseChildren
   *
   * @example The following example creates two sprites and sets the <code>buttonMode</code> property to <code>true</code> for both, yet it sets the <code>useHandCursor</code> property to <code>true</code> for one and <code>false</code> for the other. When you compile and run the application, both sprites respond as buttons (and are included in the tab order), but only the one in which <code>useHandCursor</code> is set to <code>true</code> uses the hand cursor:
   * <listing>
   * import flash.display.Sprite;
   * import flash.events.MouseEvent;
   *
   * var circle1:Sprite = new Sprite();
   * circle1.graphics.beginFill(0xFFCC00);
   * circle1.graphics.drawCircle(40, 40, 40);
   * circle1.buttonMode = true;
   * circle1.useHandCursor = true;
   * circle1.addEventListener(MouseEvent.CLICK, clicked);
   *
   * var circle2:Sprite = new Sprite();
   * circle2.graphics.beginFill(0xFFCC00);
   * circle2.graphics.drawCircle(120, 40, 40);
   * circle2.buttonMode = true;
   * circle2.useHandCursor = false;
   * circle2.addEventListener(MouseEvent.CLICK, clicked);
   *
   * function clicked(event:MouseEvent):void {
   *     trace ("Click!");
   * }
   *
   * addChild(circle1);
   * addChild(circle2);
   * </listing>
   */
  "public function get useHandCursor",function useHandCursor$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set useHandCursor",function useHandCursor$set(value/*:Boolean*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Creates a new Sprite instance. After you create the Sprite instance, call the <code>DisplayObjectContainer.addChild()</code> or <code>DisplayObjectContainer.addChildAt()</code> method to add the Sprite to a parent DisplayObjectContainer.
   */
  "public function Sprite",function Sprite$() {
    this.super$5();
  },

  /**
   * Lets the user drag the specified sprite. The sprite remains draggable until explicitly stopped through a call to the <code>Sprite.stopDrag()</code> method, or until another sprite is made draggable. Only one sprite is draggable at a time.
   * <p>Three-dimensional display objects follow the pointer and <code>Sprite.startDrag()</code> moves the object within the three-dimensional plane defined by the display object. Or, if the display object is a two-dimensional object and the child of a three-dimensional object, the two-dimensional object moves within the three dimensional plane defined by the three-dimensional parent object.</p>
   * @param lockCenter Specifies whether the draggable sprite is locked to the center of the pointer position (<code>true</code>), or locked to the point where the user first clicked the sprite (<code>false</code>).
   * @param bounds Value relative to the coordinates of the Sprite's parent that specify a constraint rectangle for the Sprite.
   *
   * @see #dropTarget
   * @see #stopDrag()
   *
   * @example The following example creates a <code>circle</code> sprite and two <code>target</code> sprites. The <code>startDrag()</code> method is called on the <code>circle</code> sprite when the user positions the cursor over the sprite and presses the mouse button, and the <code>stopDrag()</code> method is called when the user releases the mouse button. This lets the user drag the sprite. On release of the mouse button, the <code>mouseRelease()</code> method is called, which in turn traces the <code>name</code> of the <code>dropTarget</code> object � the one to which the user dragged the <code>circle</code> sprite:
   * <listing>
   * import flash.display.Sprite;
   * import flash.events.MouseEvent;
   *
   * var circle:Sprite = new Sprite();
   * circle.graphics.beginFill(0xFFCC00);
   * circle.graphics.drawCircle(0, 0, 40);
   *
   * var target1:Sprite = new Sprite();
   * target1.graphics.beginFill(0xCCFF00);
   * target1.graphics.drawRect(0, 0, 100, 100);
   * target1.name = "target1";
   *
   * var target2:Sprite = new Sprite();
   * target2.graphics.beginFill(0xCCFF00);
   * target2.graphics.drawRect(0, 200, 100, 100);
   * target2.name = "target2";
   *
   * addChild(target1);
   * addChild(target2);
   * addChild(circle);
   *
   * circle.addEventListener(MouseEvent.MOUSE_DOWN, mouseDown)
   *
   * function mouseDown(event:MouseEvent):void {
   *     circle.startDrag();
   * }
   * circle.addEventListener(MouseEvent.MOUSE_UP, mouseReleased);
   *
   * function mouseReleased(event:MouseEvent):void {
   *     circle.stopDrag();
   *     trace(circle.dropTarget.name);
   * }
   * </listing>
   */
  "public function startDrag",function startDrag(lockCenter/*:Boolean = false*/, bounds/*:Rectangle = null*/)/*:void*/ {if(arguments.length<2){if(arguments.length<1){lockCenter = false;}bounds = null;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Ends the <code>startDrag()</code> method. A sprite that was made draggable with the <code>startDrag()</code> method remains draggable until a <code>stopDrag()</code> method is added, or until another sprite becomes draggable. Only one sprite is draggable at a time.
   * @see #dropTarget
   * @see #startDrag()
   *
   * @example The following example creates a <code>circle</code> sprite and two <code>target</code> sprites. The <code>startDrag()</code> method is called on the <code>circle</code> sprite when the user positions the cursor over the sprite and presses the mouse button, and the <code>stopDrag()</code> method is called when the user releases the mouse button. This lets the user drag the sprite. On release of the mouse button, the <code>mouseRelease()</code> method is called, which in turn traces the <code>name</code> of the <code>dropTarget</code> object � the one to which the user dragged the <code>circle</code> sprite:
   * <listing>
   * import flash.display.Sprite;
   * import flash.events.MouseEvent;
   *
   * var circle:Sprite = new Sprite();
   * circle.graphics.beginFill(0xFFCC00);
   * circle.graphics.drawCircle(0, 0, 40);
   *
   * var target1:Sprite = new Sprite();
   * target1.graphics.beginFill(0xCCFF00);
   * target1.graphics.drawRect(0, 0, 100, 100);
   * target1.name = "target1";
   *
   * var target2:Sprite = new Sprite();
   * target2.graphics.beginFill(0xCCFF00);
   * target2.graphics.drawRect(0, 200, 100, 100);
   * target2.name = "target2";
   *
   * addChild(target1);
   * addChild(target2);
   * addChild(circle);
   *
   * circle.addEventListener(MouseEvent.MOUSE_DOWN, mouseDown)
   *
   * function mouseDown(event:MouseEvent):void {
   *     circle.startDrag();
   * }
   * circle.addEventListener(MouseEvent.MOUSE_UP, mouseReleased);
   *
   * function mouseReleased(event:MouseEvent):void {
   *     circle.stopDrag();
   *     trace(circle.dropTarget.name);
   * }
   * </listing>
   */
  "public function stopDrag",function stopDrag()/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @inheritDoc
   */
  "override public function set transform",function transform$set(value/*:Transform*/)/*:void*/ {
    this.transform$5 = value;
    var m/* : Matrix*/ = value.matrix;
    if (m) {
      this.graphics.renderingContext.setTransform(m.a, m.b, m.c, m.d, m.tx, m.ty);
    }
  },

  // ************************** Jangaroo part **************************
  "private var",{ _graphics/* : Graphics*/:null},
];},[],["flash.display.DisplayObjectContainer","Error","flash.display.Graphics"], "0.8.0", "0.8.3"
);