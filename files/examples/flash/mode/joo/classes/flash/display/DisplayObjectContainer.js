joo.classLoader.prepare("package flash.display",/* {
import flash.events.Event
import flash.geom.Point
import flash.text.TextSnapshot

import js.Element
import js.HTMLElement*/

/**
 * The DisplayObjectContainer class is the base class for all objects that can serve as display object containers on the display list. The display list manages all objects displayed in the Flash runtimes. Use the DisplayObjectContainer class to arrange the display objects in the display list. Each DisplayObjectContainer object has its own child list for organizing the z-order of the objects. The z-order is the front-to-back order that determines which object is drawn in front, which is behind, and so on.
 * <p>DisplayObject is an abstract base class; therefore, you cannot call DisplayObject directly. Invoking <code>new DisplayObject()</code> throws an <code>ArgumentError</code> exception.</p>The DisplayObjectContainer class is an abstract base class for all objects that can contain child objects. It cannot be instantiated directly; calling the <code>new DisplayObjectContainer()</code> constructor throws an <code>ArgumentError</code> exception.
 * <p>For more information, see the "Display Programming" chapter of the <i>ActionScript 3.0 Developer's Guide</i>.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/display/DisplayObjectContainer.html#includeExamplesSummary">View the examples</a></p>
 * @see DisplayObject
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e3c.html Core display classes
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e3d.html Working with display objects
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e36.html Working with display object containers
 *
 */
"public class DisplayObjectContainer extends flash.display.InteractiveObject",4,function($$private){var as=joo.as,assert=joo.assert;return[function(){joo.classLoader.init(flash.events.Event);}, 

  /**
   * Determines whether or not the children of the object are mouse, or user input device, enabled. If an object is enabled, a user can interact with it by using a mouse or user input device. The default is <code>true</code>.
   * <p>This property is useful when you create a button with an instance of the Sprite class (instead of using the SimpleButton class). When you use a Sprite instance to create a button, you can choose to decorate the button by using the <code>addChild()</code> method to add additional Sprite instances. This process can cause unexpected behavior with mouse events because the Sprite instances you add as children can become the target object of a mouse event when you expect the parent instance to be the target object. To ensure that the parent instance serves as the target objects for mouse events, you can set the <code>mouseChildren</code> property of the parent instance to <code>false</code>.</p>
   * <p>No event is dispatched by setting this property. You must use the <code>addEventListener()</code> method to create interactive functionality.</p>
   * @see Sprite#buttonMode
   * @see flash.events.EventDispatcher#addEventListener()
   *
   * @example The following example sets up a Sprite object (a type of display object container) named <code>container</code> and shows that when you set its <code>mouseChildren</code> property to <code>false</code>, the target of a <code>mouseClick</code> event is the <code>container</code> object, not any one of its child objects:
   * <listing>
   * import flash.display.Sprite;
   * import flash.events.MouseEvent;
   *
   * var container:Sprite = new Sprite();
   * container.name = "container";
   * addChild(container);
   *
   * var circle:Sprite = new Sprite();
   * circle.name = "circle";
   * circle.graphics.beginFill(0xFFCC00);
   * circle.graphics.drawCircle(40, 40, 40);
   *
   * container.addChild(circle);
   *
   * container.mouseChildren = false;
   *
   * container.addEventListener(MouseEvent.CLICK, clicked);
   *
   * function clicked(event:MouseEvent):void {
   *     trace(event.target.name); // container
   * }
   * </listing>
   */
  "public function get mouseChildren",function mouseChildren$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set mouseChildren",function mouseChildren$set(value/*:Boolean*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns the number of children of this object.
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e40.html Advantages of the display list approach
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e04.html Improved depth management
   *
   * @example The following example sets up two Sprite objects named <code>container1</code> and <code>container2</code>. A Sprite is a type of display object container. The example calls the <code>addChild()</code> method to set up the display hierarchy: <code>container1</code> is a child of <code>container2</code>, and two other display objects, <code>circle1</code> and <code>circle2</code>, are children of <code>container1</code>. The calls to the <code>trace()</code> method show the number of children of each object. Note that grandchildren are not included in the <code>numChildren</code> count:
   * <listing>
   * import flash.display.Sprite;
   *
   * var container1:Sprite = new Sprite();
   * var container2:Sprite = new Sprite();
   *
   * var circle1:Sprite = new Sprite();
   * circle1.graphics.beginFill(0xFFCC00);
   * circle1.graphics.drawCircle(40, 40, 40);
   *
   * var circle2:Sprite = new Sprite();
   * circle2.graphics.beginFill(0x00CCFF);
   * circle2.graphics.drawCircle(80, 40, 40);
   *
   * container2.addChild(container1);
   * container1.addChild(circle1);
   * container1.addChild(circle2);
   *
   * trace(container1.numChildren); // 2
   * trace(container2.numChildren); // 1
   * trace(circle1.numChildren); // 0
   * trace(circle2.numChildren); // 0
   * </listing>
   */
  "public function get numChildren",function numChildren$get()/*:int*/ {
    return this.children$4.length;
  },

  /**
   * Determines whether the children of the object are tab enabled. Enables or disables tabbing for the children of the object. The default is <code>true</code>.
   * <p><b>Note:</b> Do not use the <code>tabChildren</code> property with Flex. Instead, use the <code>mx.core.UIComponent.hasFocusableChildren</code> property.</p>
   * @throws flash.errors.IllegalOperationError Calling this property of the Stage object throws an exception. The Stage object does not implement this property.
   *
   * @example The following example creates a <code>container1</code> display object container and adds two display objects, <code>circle1</code> and <code>circle2</code>, to its child list. The example sets tabChildren to <code>false</code> for the children so it can manage its own tab order using <code>tabIndex</code>:
   * <listing>
   * import flash.display.Sprite;
   *
   * var container:Sprite = new Sprite();
   * container.tabChildren = false;
   *
   * var circle1:Sprite = new Sprite();
   * circle1.graphics.beginFill(0xFFCC00);
   * circle1.graphics.drawCircle(40, 40, 40);
   * circle1.tabIndex = 1;
   *
   * var circle2:Sprite = new Sprite();
   * circle2.graphics.beginFill(0x00CCFF);
   * circle2.graphics.drawCircle(120, 40, 40);
   * circle2.tabIndex = 0;
   *
   * container.addChild(circle1);
   * container.addChild(circle2);
   * </listing>To see the results of this example, compile and run the file. When you select one of the circles, you can press the TAB key to switch the display object that has focus (indicated by a yellow highlight rectangle).
   */
  "public function get tabChildren",function tabChildren$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set tabChildren",function tabChildren$set(value/*:Boolean*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns a TextSnapshot object for this DisplayObjectContainer instance.
   * @see flash.text.TextSnapshot
   *
   * @example The following example works only in the Flash authoring environment. Flex does not include any ways of adding static text to a file. To prepare the Flash file for this example, add one or more static text fields in the first frame of a movie. Then insert the following script into the first frame and run the file. The output will be the static text that you added:
   * <listing>
   * trace(this.textSnapshot.getText(0, this.textSnapshot.charCount));
   * </listing>
   */
  "public function get textSnapshot",function textSnapshot$get()/*:TextSnapshot*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Calling the <code>new DisplayObjectContainer()</code> constructor throws an <code>ArgumentError</code> exception. You <i>can</i>, however, call constructors for the following subclasses of DisplayObjectContainer:
   * <ul>
   * <li><code>new Loader()</code></li>
   * <li><code>new Sprite()</code></li>
   * <li><code>new MovieClip()</code></li></ul>
   */
  "public function DisplayObjectContainer",function DisplayObjectContainer$() {
    this.children$4 = [];
    this.super$4();
  },

  /**
   * Adds a child DisplayObject instance to this DisplayObjectContainer instance. The child is added to the front (top) of all other children in this DisplayObjectContainer instance. (To add a child to a specific index position, use the <code>addChildAt()</code> method.)
   * <p>If you add a child object that already has a different display object container as a parent, the object is removed from the child list of the other display object container.</p>
   * <p><b>Note:</b> The command <code>stage.addChild()</code> can cause problems with a published SWF file, including security problems and conflicts with other loaded SWF files. There is only one Stage within a Flash runtime instance, no matter how many SWF files you load into the runtime. So, generally, objects should not be added to the Stage, directly, at all. The only object the Stage should contain is the root object. Create a DisplayObjectContainer to contain all of the items on the display list. Then, if necessary, add that DisplayObjectContainer instance to the Stage.</p>
   * @param child The DisplayObject instance to add as a child of this DisplayObjectContainer instance.
   *
   * @return The DisplayObject instance that you pass in the <code>child</code> parameter.
   * Events
   * <table>
   * <tr>
   * <td><code><b>added</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/Event.html"><code>Event</code></a></code> � Dispatched when a display object is added to the display list.</td></tr></table>
   * @throws ArgumentError Throws if the child is the same as the parent. Also throws if the caller is a child (or grandchild etc.) of the child being added.
   *
   * @see #addChildAt()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e40.html Advantages of the display list approach
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e02.html Off-list display objects
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e3d.html Working with display objects
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7dff.html Adding display objects to the display list
   *
   * @example The following example sets up two Sprite objects named <code>container1</code> and <code>container2</code>. A Sprite is a type of display object container. The example calls the <code>addChild()</code> method to set up the display hierarchy: <code>container1</code> is a child of <code>container2</code>, and two other display objects, <code>circle1</code> and <code>circle2</code>, are children of <code>container1</code>. The calls to the <code>trace()</code> method show the number of children of each object. Note that grandchildren are not included in the <code>numChildren</code> count:
   * <listing>
   * import flash.display.Sprite;
   *
   * var container1:Sprite = new Sprite();
   * var container2:Sprite = new Sprite();
   *
   * var circle1:Sprite = new Sprite();
   * circle1.graphics.beginFill(0xFFCC00);
   * circle1.graphics.drawCircle(40, 40, 40);
   *
   * var circle2:Sprite = new Sprite();
   * circle2.graphics.beginFill(0x00CCFF);
   * circle2.graphics.drawCircle(80, 40, 40);
   *
   * container2.addChild(container1);
   * container1.addChild(circle1);
   * container1.addChild(circle2);
   *
   * trace(container1.numChildren); // 2
   * trace(container2.numChildren); // 1
   * trace(circle1.numChildren); // 0
   * trace(circle2.numChildren); // 0
   * </listing>
   */
  "public function addChild",function addChild(child/*:DisplayObject*/)/*:DisplayObject*/ {
    return this.addChildAt(child, this.children$4.length);
  },

  /**
   * Adds a child DisplayObject instance to this DisplayObjectContainer instance. The child is added at the index position specified. An index of 0 represents the back (bottom) of the display list for this DisplayObjectContainer object.
   * <p>For example, the following example shows three display objects, labeled a, b, and c, at index positions 0, 2, and 1, respectively:</p>
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/DisplayObjectContainer_layers.jpg" /></p>
   * <p>If you add a child object that already has a different display object container as a parent, the object is removed from the child list of the other display object container.</p>
   * @param child The DisplayObject instance to add as a child of this DisplayObjectContainer instance.
   * @param index The index position to which the child is added. If you specify a currently occupied index position, the child object that exists at that position and all higher positions are moved up one position in the child list.
   *
   * @return The DisplayObject instance that you pass in the <code>child</code> parameter.
   * Events
   * <table>
   * <tr>
   * <td><code><b>added</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/Event.html"><code>Event</code></a></code> � Dispatched when a display object is added to the display list.</td></tr></table>
   * @throws RangeError Throws if the index position does not exist in the child list.
   * @throws ArgumentError Throws if the child is the same as the parent. Also throws if the caller is a child (or grandchild etc.) of the child being added.
   *
   * @see #addChild()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e40.html Advantages of the display list approach
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e02.html Off-list display objects
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e3d.html Working with display objects
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7dff.html Adding display objects to the display list
   *
   * @example The following example creates a <code>container</code> display object container and adds a display objects <code>circle1</code> to its display list. Then, by calling <code>container.addChildAt(circle2, 0)</code>, it adds the <code>circle2</code> object to index position zero (the back), and moves the <code>circle1</code> object to index position 1:
   * <listing>
   * import flash.display.Sprite;
   *
   * var container:Sprite = new Sprite();
   *
   * var circle1:Sprite = new Sprite();
   * var circle2:Sprite = new Sprite();
   *
   * container.addChild(circle1);
   * container.addChildAt(circle2, 0);
   *
   * trace(container.getChildAt(0) == circle2); // true
   * trace(container.getChildAt(1) == circle1); // true
   * </listing>
   */
  "public function addChildAt",function addChildAt(child/*:DisplayObject*/, index/*:int*/)/*:DisplayObject*/ {
    var wasInStage/*:Boolean*/ = ! !child.stage;
    this.internalAddChildAt(child, index);
    var isInStage/*:Boolean*/ = ! !this.stage;
    if (wasInStage !== isInStage) {
      child.broadcastEvent(new flash.events.Event(isInStage ? flash.events.Event.ADDED_TO_STAGE : flash.events.Event.REMOVED_FROM_STAGE, false, false));
    }
    return child;
  },

  /**
   * @private
   */
  "public function internalAddChildAt",function internalAddChildAt(child/*:DisplayObject*/, index/*:int*/)/*:void*/ {
    var containerElement/*:Element*/ = this.getElement();
    var childElement/*:Element*/ = child.getElement();/*
    assert containerElement.childNodes.length === this.children$4.length;*/
    var oldParent/*:DisplayObjectContainer*/ = child.parent;
    if (oldParent) {
      oldParent.removeChild(child);
    } else {/*
      assert!childElement.parentNode;*/
    }
    var refChild/*:DisplayObject*/ = this.children$4[index];
    this.children$4.splice(index, 0, child);
    child.parent = this;
    // also add to DOM:
    if (refChild) {
      containerElement.insertBefore(childElement, refChild.getElement());
    } else {
      containerElement.appendChild(childElement);
    }/*
    assert containerElement.childNodes.length === this.children$4.length;*/
  },

  /**
   * Indicates whether the security restrictions would cause any display objects to be omitted from the list returned by calling the <code>DisplayObjectContainer.getObjectsUnderPoint()</code> method with the specified <code>point</code> point. By default, content from one domain cannot access objects from another domain unless they are permitted to do so with a call to the <code>Security.allowDomain()</code> method. For more information, related to security, see the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.
   * <p>The <code>point</code> parameter is in the coordinate space of the Stage, which may differ from the coordinate space of the display object container (unless the display object container is the Stage). You can use the <code>globalToLocal()</code> and the <code>localToGlobal()</code> methods to convert points between these coordinate spaces.</p>
   * @param point The point under which to look.
   *
   * @return <code>true</code> if the point contains child display objects with security restrictions.
   *
   * @see flash.system.Security#allowDomain()
   * @see #getObjectsUnderPoint()
   * @see DisplayObject#globalToLocal()
   * @see DisplayObject#localToGlobal()
   *
   * @example The following code creates a display object container named <code>container</code>. The next block of code uses a Loader object to load a JPEG file named "test.jpg" from a remote file server. Note that the <code>checkPolicyFile</code> property of the LoaderContext object used as a parameter in the <code>load()</code> method is set to <code>false</code>. Once the file is loaded, the code calls the <code>loaded()</code> method, which in turn calls <code>container.areInaccessibleObjectsUnderPoint()</code>, which returns a value of <code>true</code> because the loaded content is assumed to be from an inaccessible domain:
   * <listing>
   * import flash.display.Sprite;
   * import flash.display.Loader;
   * import flash.system.LoaderContext;
   * import flash.net.URLRequest;
   * import flash.events.Event;
   * import flash.geom.Point;
   *
   * var container:Sprite = new Sprite();
   *
   * var urlReq:URLRequest = new URLRequest("http://localhost/RemoteFile.swf");
   * var ldr:Loader = new Loader();
   * var context:LoaderContext = new LoaderContext();
   * context.checkPolicyFile = false;
   * ldr.load(urlReq, context);
   *
   * ldr.contentLoaderInfo.addEventListener(Event.COMPLETE, loaded);
   * ldr.contentLoaderInfo.addEventListener(IOErrorEvent.IO_ERROR, urlNotFound);
   *
   * function loaded(event:Event):void {
   *     var pt:Point = new Point(1, 1);
   *     trace(container.areInaccessibleObjectsUnderPoint(pt)); // true
   * }
   *
   * function urlNotFound(event:Event):void {
   *     trace("The URL was not found.");
   * }
   * </listing>This example assumes that the SWF file produced by this code is loaded from a different domain than that of the JPEG file, and that the loaded JPEG file occupies the point (1, 1).
   */
  "public function areInaccessibleObjectsUnderPoint",function areInaccessibleObjectsUnderPoint(point/*:Point*/)/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Determines whether the specified display object is a child of the DisplayObjectContainer instance or the instance itself. The search includes the entire display list including this DisplayObjectContainer instance. Grandchildren, great-grandchildren, and so on each return <code>true</code>.
   * @param child The child object to test.
   *
   * @return <code>true</code> if the <code>child</code> object is a child of the DisplayObjectContainer or the container itself; otherwise <code>false</code>.
   *
   * @example The following example sets up a number of Sprite objects and adds some to the child list of others. (A Sprite object is a type of display object container.) The relationship between various objects is shown by calling the <code>contains()</code> method:
   * <listing>
   * import flash.display.Sprite;
   *
   * var sprite1:Sprite = new Sprite();
   * var sprite2:Sprite = new Sprite();
   * var sprite3:Sprite = new Sprite();
   * var sprite4:Sprite = new Sprite();
   *
   * sprite1.addChild(sprite2);
   * sprite2.addChild(sprite3);
   *
   * trace(sprite1.contains(sprite1)); // true
   * trace(sprite1.contains(sprite2)); // true
   * trace(sprite1.contains(sprite3)); // true
   * trace(sprite1.contains(sprite4)); // false
   * </listing>
   */
  "public function contains",function contains(child/*:DisplayObject*/)/*:Boolean*/ {
    return child === this || this.children$4.some(function flash$display$DisplayObjectContainer$353_44(someChild/*:DisplayObject*/)/*:Boolean*/ {
      var container/*:DisplayObjectContainer*/ =as( someChild,  flash.display.DisplayObjectContainer);
      return container ? container.contains(child) : someChild === child;
    });

  },

  /**
   * Returns the child display object instance that exists at the specified index.
   * @param index The index position of the child object.
   *
   * @return The child display object at the specified index position.
   *
   * @throws RangeError Throws if the index does not exist in the child list.
   * @throws SecurityError This child display object belongs to a sandbox to which you do not have access. You can avoid this situation by having the child movie call <code>Security.allowDomain()</code>.
   *
   * @see #getChildByName()
   *
   * @example The following example creates a display object container named <code>container</code> and then adds a three display objects to the child list of the <code>container</code> object. The calls to the <code>getChildAt()</code> method then reveal the positions of the child objects:
   * <listing>
   * import flash.display.Sprite;
   *
   * var container:Sprite = new Sprite();
   *
   * var sprite1:Sprite = new Sprite();
   * var sprite2:Sprite = new Sprite();
   * var sprite3:Sprite = new Sprite();
   *
   * container.addChild(sprite1);
   * container.addChild(sprite2);
   * container.addChildAt(sprite3, 0);
   *
   * trace(container.getChildAt(0) == sprite3); // true
   * trace(container.getChildAt(1) == sprite1); // true
   * trace(container.getChildAt(2) == sprite2); // true
   * </listing>
   */
  "public function getChildAt",function getChildAt(index/*:int*/)/*:DisplayObject*/ {
    return as( this.children$4[index],  flash.display.DisplayObject);
  },

  /**
   * Returns the child display object that exists with the specified name. If more that one child display object has the specified name, the method returns the first object in the child list.
   * <p>The <code>getChildAt()</code> method is faster than the <code>getChildByName()</code> method. The <code>getChildAt()</code> method accesses a child from a cached array, whereas the <code>getChildByName()</code> method has to traverse a linked list to access a child.</p>
   * @param name The name of the child to return.
   *
   * @return The child display object with the specified name.
   *
   * @throws SecurityError This child display object belongs to a sandbox to which you do not have access. You can avoid this situation by having the child movie call the <code>Security.allowDomain()</code> method.
   *
   * @see #getChildAt()
   * @see DisplayObject#name
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e40.html Advantages of the display list approach
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e04.html Improved depth management
   *
   * @example The following example creates a display object container named <code>container</code> and then adds two child display objects to the container. Then, the code calls the <code>getChildByName()</code> and <code>getChildIndex()</code> methods to return the index position of the child of the <code>container</code> object that has the <code>name "sprite1"</code>.
   * <listing>
   * import flash.display.Sprite;
   * import flash.display.DisplayObject;
   *
   * var container:Sprite = new Sprite();
   *
   * var sprite1:Sprite = new Sprite();
   * sprite1.name = "sprite1";
   * var sprite2:Sprite = new Sprite();
   * sprite2.name = "sprite2";
   *
   * container.addChild(sprite1);
   * container.addChild(sprite2);
   *
   * var target:DisplayObject = container.getChildByName("sprite1");
   * trace(container.getChildIndex(target)); // 0
   * </listing>
   */
  "public function getChildByName",function getChildByName(name/*:String*/)/*:DisplayObject*/ {
    for (var i/*:int*/ = 0; i < this.children$4.length; i++) {
      var child/*:DisplayObject*/ = this.children$4[i];
      if (child.name === name) {
        return child;
      }
    }
    return null;
  },

  /**
   * Returns the index position of a <code>child</code> DisplayObject instance.
   * @param child The DisplayObject instance to identify.
   *
   * @return The index position of the child display object to identify.
   *
   * @throws ArgumentError Throws if the child parameter is not a child of this object.
   *
   * @example The following example creates a display object container named <code>container</code> and then adds two child display objects to the container. Then, the code calls the <code>getChildByName()</code> and <code>getChildIndex()</code> methods to return the index position of the child of the <code>container</code> object that has the <code>name "sprite1"</code>.
   * <listing>
   * import flash.display.Sprite;
   * import flash.display.DisplayObject;
   *
   * var container:Sprite = new Sprite();
   *
   * var sprite1:Sprite = new Sprite();
   * sprite1.name = "sprite1";
   * var sprite2:Sprite = new Sprite();
   * sprite2.name = "sprite2";
   *
   * container.addChild(sprite1);
   * container.addChild(sprite2);
   *
   * var target:DisplayObject = container.getChildByName("sprite1");
   * trace(container.getChildIndex(target)); // 0
   * </listing>
   */
  "public function getChildIndex",function getChildIndex(child/*:DisplayObject*/)/*:int*/ {
    var index/*:int*/ = this.children$4.indexOf(child);
    if (index == -1) {
      throw new ArgumentError();
    }
    return index;
  },

  /**
   * Returns an array of objects that lie under the specified point and are children (or grandchildren, and so on) of this DisplayObjectContainer instance. Any child objects that are inaccessible for security reasons are omitted from the returned array. To determine whether this security restriction affects the returned array, call the <code>areInaccessibleObjectsUnderPoint()</code> method.
   * <p>The <code>point</code> parameter is in the coordinate space of the Stage, which may differ from the coordinate space of the display object container (unless the display object container is the Stage). You can use the <code>globalToLocal()</code> and the <code>localToGlobal()</code> methods to convert points between these coordinate spaces.</p>
   * @param point The point under which to look.
   *
   * @return An array of objects that lie under the specified point and are children (or grandchildren, and so on) of this DisplayObjectContainer instance.
   *
   * @see #areInaccessibleObjectsUnderPoint()
   * @see DisplayObject#globalToLocal()
   * @see DisplayObject#localToGlobal()
   *
   * @example The following example creates a display object container named <code>container</code> and then adds two overlapping child display objects to the container. Then the code calls the <code>getObjectsUnderPoint()</code> twice � first using a point that touches only one object, then using a point where the objects overlap � and the <code>length</code> of the return Array shows the number of objects at each point in the container:
   * <listing>
   * import flash.display.Sprite;
   * import flash.geom.Point;
   *
   * var container:Sprite = new Sprite();
   *
   * var square1:Sprite = new Sprite();
   * square1.graphics.beginFill(0xFFCC00);
   * square1.graphics.drawRect(0, 0, 40, 40);
   *
   * var square2:Sprite = new Sprite();
   * square2.graphics.beginFill(0x00CCFF);
   * square2.graphics.drawRect(20, 0, 30, 40);
   *
   * container.addChild(square1);
   * container.addChild(square2);
   *
   * var pt:Point = new Point(10, 20);
   * var objects:Array = container.getObjectsUnderPoint(pt);
   * trace(objects.length); // 1
   *
   * pt = new Point(35, 20);
   * objects = container.getObjectsUnderPoint(pt);
   * trace(objects.length);  // 2
   * </listing>
   */
  "public function getObjectsUnderPoint",function getObjectsUnderPoint(point/*:Point*/)/*:Array*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Removes the specified <code>child</code> DisplayObject instance from the child list of the DisplayObjectContainer instance. The <code>parent</code> property of the removed child is set to <code>null</code> , and the object is garbage collected if no other references to the child exist. The index positions of any display objects above the child in the DisplayObjectContainer are decreased by 1.
   * <p>The garbage collector reallocates unused memory space. When a variable or object is no longer actively referenced or stored somewhere, the garbage collector sweeps through and wipes out the memory space it used to occupy if no other references to it exist.</p>
   * @param child The DisplayObject instance to remove.
   *
   * @return The DisplayObject instance that you pass in the <code>child</code> parameter.
   *
   * @throws ArgumentError Throws if the child parameter is not a child of this object.
   *
   * @example The following example creates a display object container named <code>container</code> and then adds two child display objects to the container. An event listener is added to the <code>container</code> object, so that when the user clicks a child object of the container, the <code>removeChild()</code> method removes the child clicked from the child list of the container:
   * <listing>
   *
   * import flash.display.DisplayObject;
   * import flash.display.Sprite;
   * import flash.events.MouseEvent;
   *
   * var container:Sprite = new Sprite();
   * addChild(container);
   *
   * var circle1:Sprite = new Sprite();
   * circle1.graphics.beginFill(0xFFCC00);
   * circle1.graphics.drawCircle(40, 40, 40);
   *
   * var circle2:Sprite = new Sprite();
   * circle2.graphics.beginFill(0x00CCFF);
   * circle2.graphics.drawCircle(120, 40, 40);
   *
   * container.addChild(circle1);
   * container.addChild(circle2);
   *
   * container.addEventListener(MouseEvent.CLICK, clicked);
   *
   * function clicked(event:MouseEvent):void {
   *     container.removeChild(DisplayObject(event.target));
   * }
   * </listing>
   */
  "public function removeChild",function removeChild(child/*:DisplayObject*/)/*:DisplayObject*/ {
    return this.removeChildAt(this.getChildIndex(child));
  },

  /**
   * Removes a child DisplayObject from the specified <code>index</code> position in the child list of the DisplayObjectContainer. The <code>parent</code> property of the removed child is set to <code>null</code>, and the object is garbage collected if no other references to the child exist. The index positions of any display objects above the child in the DisplayObjectContainer are decreased by 1.
   * <p>The garbage collector reallocates unused memory space. When a variable or object is no longer actively referenced or stored somewhere, the garbage collector sweeps through and wipes out the memory space it used to occupy if no other references to it exist.</p>
   * @param index The child index of the DisplayObject to remove.
   *
   * @return The DisplayObject instance that was removed.
   *
   * @throws SecurityError This child display object belongs to a sandbox to which the calling object does not have access. You can avoid this situation by having the child movie call the <code>Security.allowDomain()</code> method.
   * @throws RangeError Throws if the index does not exist in the child list.
   *
   * @example The following example creates a display object container named <code>container</code> and then adds two child display objects to the container. The code then shows that when you call the <code>removeChildAt()</code> method to remove the child at the lowest index position (0), any other child object in the list moves down one position:
   * <listing>
   * import flash.display.Sprite;
   *
   * var container:Sprite = new Sprite();
   *
   * var sprite1:Sprite = new Sprite();
   * sprite1.name = "sprite1";
   * var sprite2:Sprite = new Sprite();
   * sprite2.name = "sprite2";
   *
   * container.addChild(sprite1);
   * container.addChild(sprite2);
   *
   * trace(container.numChildren) // 2
   * container.removeChildAt(0);
   * trace(container.numChildren) // 1
   * trace(container.getChildAt(0).name); // sprite2
   * </listing>
   */
  "public function removeChildAt",function removeChildAt(index/*:int*/)/*:DisplayObject*/ {
    var containerElement/*:HTMLElement*/ = this.getElement();/*
    assert containerElement.childNodes.length === this.children$4.length;*/
    var child/*:DisplayObject*/ = this.children$4.splice(index, 1)[0];
    child.parent = null;
    // if successful, remove in DOM, too:
    var childElement/*:Element*/ = child.getElement();
    containerElement.removeChild(childElement);/*
    assert!childElement.parentNode;
    assert containerElement.childNodes.length === this.children$4.length;*/
    return child;
  },

  /**
   * Changes the position of an existing child in the display object container. This affects the layering of child objects. For example, the following example shows three display objects, labeled a, b, and c, at index positions 0, 1, and 2, respectively:
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/DisplayObjectContainerSetChildIndex1.jpg" /></p>
   * <p>When you use the <code>setChildIndex()</code> method and specify an index position that is already occupied, the only positions that change are those in between the display object's former and new position. All others will stay the same. If a child is moved to an index LOWER than its current index, all children in between will INCREASE by 1 for their index reference. If a child is moved to an index HIGHER than its current index, all children in between will DECREASE by 1 for their index reference. For example, if the display object container in the previous example is named <code>container</code>, you can swap the position of the display objects labeled a and b by calling the following code:</p>
   * <listing>
   * container.setChildIndex(container.getChildAt(1), 0);</listing>
   * <p>This code results in the following arrangement of objects:</p>
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/DisplayObjectContainerSetChildIndex2.jpg" /></p>
   * @param child The child DisplayObject instance for which you want to change the index number.
   * @param index The resulting index number for the <code>child</code> display object.
   *
   * @throws RangeError Throws if the index does not exist in the child list.
   * @throws ArgumentError Throws if the child parameter is not a child of this object.
   *
   * @see #addChildAt()
   * @see #getChildIndex()
   *
   * @example The following example creates a display object container named <code>container</code> and then adds three slightly overlapping child display objects to the container. When the user clicks any of these objects, the <code>clicked()</code> method calls the <code>setChildIndex()</code> method to move the clicked object to the top-most position in the child list of the <code>container</code> object:
   * <listing>
   * import flash.display.Sprite;
   * import flash.events.MouseEvent;
   *
   * var container:Sprite = new Sprite();
   * addChild(container);
   *
   * var circle1:Sprite = new Sprite();
   * circle1.graphics.beginFill(0xFF0000);
   * circle1.graphics.drawCircle(40, 40, 40);
   * circle1.addEventListener(MouseEvent.CLICK, clicked);
   *
   * var circle2:Sprite = new Sprite();
   * circle2.graphics.beginFill(0x00FF00);
   * circle2.graphics.drawCircle(100, 40, 40);
   * circle2.addEventListener(MouseEvent.CLICK, clicked);
   *
   * var circle3:Sprite = new Sprite();
   * circle3.graphics.beginFill(0x0000FF);
   * circle3.graphics.drawCircle(70, 80, 40);
   * circle3.addEventListener(MouseEvent.CLICK, clicked);
   *
   * container.addChild(circle1);
   * container.addChild(circle2);
   * container.addChild(circle3);
   * addChild(container);
   *
   * function clicked(event:MouseEvent):void {
   *     var circle:Sprite = Sprite(event.target);
   *     var topPosition:uint = container.numChildren - 1;
   *     container.setChildIndex(circle, topPosition);
   * }
   * </listing>
   */
  "public function setChildIndex",function setChildIndex(child/*:DisplayObject*/, index/*:int*/)/*:void*/ {
    this.removeChild(child);
    this.addChildAt(child, index);
  },

  /**
   * Swaps the z-order (front-to-back order) of the two specified child objects. All other child objects in the display object container remain in the same index positions.
   * @param child1 The first child object.
   * @param child2 The second child object.
   *
   * @throws ArgumentError Throws if either child parameter is not a child of this object.
   *
   * @example The following example creates a display object container named <code>container</code>, then adds two child display objects to the container, and then shows the effect of a call to the <code>swapChildren()</code> method:
   * <listing>
   * import flash.display.Sprite;
   *
   * var container:Sprite = new Sprite();
   *
   * var sprite1:Sprite = new Sprite();
   * sprite1.name = "sprite1";
   * var sprite2:Sprite = new Sprite();
   * sprite2.name = "sprite2";
   *
   * container.addChild(sprite1);
   * container.addChild(sprite2);
   *
   * trace(container.getChildAt(0).name); // sprite1
   * trace(container.getChildAt(1).name); // sprite2
   *
   * container.swapChildren(sprite1, sprite2);
   *
   * trace(container.getChildAt(0).name); // sprite2
   * trace(container.getChildAt(1).name); // sprite1
   * </listing>
   */
  "public function swapChildren",function swapChildren(child1/*:DisplayObject*/, child2/*:DisplayObject*/)/*:void*/ {
    var child1Index/*:int*/ = this.children$4.indexOf(child1);
    var child2Index/*:int*/ = this.children$4.indexOf(child2);
    if (child1Index === -1 || child2Index === -1) {
      throw new ArgumentError;
    }
    this.swapChildrenAt(child1Index, child2Index);
  },

  /**
   * Swaps the z-order (front-to-back order) of the child objects at the two specified index positions in the child list. All other child objects in the display object container remain in the same index positions.
   * @param index1 The index position of the first child object.
   * @param index2 The index position of the second child object.
   *
   * @throws RangeError If either index does not exist in the child list.
   *
   * @example The following example creates a display object container named <code>container</code>, then adds three child display objects to the container, and then shows how a call to the <code>swapChildrenAt()</code> method rearranges the child list of the display object container:
   * <listing>
   * import flash.display.Sprite;
   *
   * var container:Sprite = new Sprite();
   *
   * var sprite1:Sprite = new Sprite();
   * sprite1.name = "sprite1";
   * var sprite2:Sprite = new Sprite();
   * sprite2.name = "sprite2";
   * var sprite3:Sprite = new Sprite();
   * sprite3.name = "sprite3";
   *
   * container.addChild(sprite1);
   * container.addChild(sprite2);
   * container.addChild(sprite3);
   *
   * trace(container.getChildAt(0).name); // sprite1
   * trace(container.getChildAt(1).name); // sprite2
   * trace(container.getChildAt(2).name); // sprite3
   *
   * container.swapChildrenAt(0, 2);
   *
   * trace(container.getChildAt(0).name); // sprite3
   * trace(container.getChildAt(1).name); // sprite2
   * trace(container.getChildAt(2).name); // sprite1
   * </listing>
   */
  "public function swapChildrenAt",function swapChildrenAt(index1/*:int*/, index2/*:int*/)/*:void*/ {
    if (index1 > index2) {
      this.swapChildrenAt(index2, index1);
    } else if (index1 < index2) {
      var containerElement/*:Element*/ = this.getElement();/*
      assert containerElement.childNodes.length === this.children$4.length;*/
      var child1/* : DisplayObject*/ = this.children$4[index1];
      var child2/* : DisplayObject*/ = this.children$4[index2];
      this.children$4.splice(index1, 1, child2);
      this.children$4.splice(index2, 1, child1);
      // also change in DOM, mind to insert left element first:
      var child1Element/*:Element*/ = child1.getElement();
      var child2Element/*:Element*/ = child2.getElement();
      var refElement/*:Element*/ =/* js.Element*/(child2Element.nextSibling); // since index1 < index2, refElement cannot be child1Element
      containerElement.insertBefore(child2Element, child1Element); // this removes child2Element at its old position, but we still have refElement
      if (refElement) {
        containerElement.insertBefore(child1Element, refElement);
      } else {
        containerElement.appendChild(child1Element);
      }/*
      assert containerElement.childNodes.length === this.children$4.length;*/
    }
  },

  /**
   * @inheritDoc
   */
  "override public function get height",function height$get()/*:Number*/ {
    return this.height$4 || this.children$4.length && (/*flash.display.DisplayObject*/(this.children$4[0]).y +/* flash.display.DisplayObject*/(this.children$4[0]).height); // TODO: find max y+height in children.
  },

  /**
   * @inheritDoc
   */
  "override public function get width",function width$get()/*:Number*/ {
    return this.width$4 || this.children$4.length && (/*flash.display.DisplayObject*/(this.children$4[0]).x +/* flash.display.DisplayObject*/(this.children$4[0]).width); // TODO: find max x+width in children.
  },

  // ************************** Jangaroo part **************************

  "override internal function updateScale",function updateScale(scaleX/*:Number*/, scaleY/*:Number*/)/*:void*/ {
    this.updateScale$4(scaleX, scaleY);
    this.children$4.forEach(function flash$display$DisplayObjectContainer$771_22(child/*:DisplayObject*/)/*:void*/ {
      child.updateScale(scaleX, scaleY);
    });
  },

  /**
   * @private
   */
  "override public function broadcastEvent",function broadcastEvent(event/*:Event*/)/*:Boolean*/ {
    if (this.dispatchEvent(event)) { // same as super.broadcastEvent(event), but more efficient
      this.children$4.every(function flash$display$DisplayObjectContainer$781_22(child/*:DisplayObject*/)/*:Boolean*/ {
        return child.broadcastEvent(event);
      });
      return true;
    }
    return false;
  },

  "private var",{ children/* : Array*/:null}/*<DisplayObject>*/,
];},[],["flash.display.InteractiveObject","Error","flash.events.Event","flash.display.DisplayObject","ArgumentError","js.Element"], "0.8.0", "0.8.2-SNAPSHOT"
);