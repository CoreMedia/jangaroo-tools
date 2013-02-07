/**
 * API and documentation by Adobe�.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

[Native(amd)]
/**
 * A Class object is created for each class definition in a program. Every Class object is an instance of the Class class. The Class object contains the static properties and methods of the class. The class object creates instances of the class when invoked using the <code>new</code> operator.
 * <p>Some methods, such as <code>flash.net.getClassByAlias()</code>, return an object of type Class. Other methods may have a parameter of type Class, such as <code>flash.net.registerClassAlias()</code>.</p>
 * <p>The class name is the reference to the Class object, as this example shows:</p>
 * <pre>
 * class Foo {
 * }
 * </pre>
 * <p>The <code>class Foo{}</code> statement is the class definition that creates the Class object Foo. Additionally, the statement <code>new Foo()</code> will create a new instance of class Foo, and the result will be of type Foo.</p>
 * <p>Use the <code>class</code> statement to declare your classes. Class objects are useful for advanced techniques, such as assigning classes to an existing instance object at runtime, as shown in the "Examples" section below.</p>
 * <p>Any static properties and methods of a class live on the class's Class object. Class, itself, declares <code>prototype</code>.</p>
 * <p>Generally, you do not need to declare or create variables of type Class manually. However, in the following code, a class is assigned as a public Class property <code>circleClass</code>, and you can refer to this Class property as a property of the main Library class:</p>
 * <listing>
 *  package {
 *   import flash.display.Sprite;
 *   public class Library extends Sprite {
 *
 *       public var circleClass:Class = Circle;
 *       public function Library() {
 *       }
 *   }
 *  }
 *
 *  import flash.display.Shape;
 *  class Circle extends Shape {
 *   public function Circle(color:uint = 0xFFCC00, radius:Number = 10) {
 *       graphics.beginFill(color);
 *       graphics.drawCircle(radius, radius, radius);
 *   }
 *  }
 * </listing>
 * <p>Another SWF file can load the resulting Library.swf file and then instantiate objects of type Circle. The following example shows one way to get access to a child SWF file's assets. (Other techniques include using <code>flash.utils.getDefnitionByName()</code> or importing stub definitions of the child SWF file.)</p>
 * <listing>
 *  package {
 *   import flash.display.Sprite;
 *   import flash.display.Shape;
 *   import flash.display.Loader;
 *   import flash.net.URLRequest;
 *   import flash.events.Event;
 *   public class LibaryLoader extends Sprite {
 *       public function LibaryLoader() {
 *           var ldr:Loader = new Loader();
 *           var urlReq:URLRequest = new URLRequest("Library.swf");
 *           ldr.load(urlReq);
 *           ldr.contentLoaderInfo.addEventListener(Event.COMPLETE, loaded);
 *       }
 *       private function loaded(event:Event):void {
 *           var library:Object = event.target.content;
 *           var circle:Shape = new library.circleClass();
 *           addChild(circle);
 *       }
 *   }
 *  }
 * </listing>
 * <p>In ActionScript 3.0, you can create embedded classes for external assets (such as images, sounds, or fonts) that are compiled into SWF files. In earlier versions of ActionScript, you associated those assets using a linkage ID with the <code>MovieClip.attachMovie()</code> method. In ActionScript 3.0, each embedded asset is represented by a unique embedded asset class. Therefore, you can use the <code>new</code> operator to instantiate the asset's associated class and call methods and properties on that asset.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/./Class.html#includeExamplesSummary">View the examples</a></p>
 * @see Object#prototype
 * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#new
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f36.html Classes
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f3f.html Advanced topics
 *
 */
public dynamic class Class {

  /**
   * A reference to the prototype object of a class or function object. The <code>prototype</code> property is automatically created and attached to any class or function object that you create. This property is static in that it is specific to the class or function that you create. For example, if you create a class, the value of the <code>prototype</code> property is shared by all instances of the class and is accessible only as a class property. Instances of your class cannot directly access the <code>prototype</code> property.
   * <p>A class's prototype object is a special instance of that class that provides a mechanism for sharing state across all instances of a class. At run time, when a property is not found on a class instance, the delegate, which is the class prototype object, is checked for that property. If the prototype object does not contain the property, the process continues with the prototype object's delegate checking in consecutively higher levels in the hierarchy until the Flash runtime finds the property.</p>
   * <p><b>Note:</b> In ActionScript 3.0, prototype inheritance is not the primary mechanism for inheritance. Class inheritance, which drives the inheritance of fixed properties in class definitions, is the primary inheritance mechanism in ActionScript 3.0.</p>
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f3f.html Advanced topics
   *
   */
  public native function get prototype():Object;

  /**
   * @private
   */
  public native function set prototype(o:Object):void;

}

}
