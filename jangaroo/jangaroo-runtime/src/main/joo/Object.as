/**
 * API and documentation by Adobe�.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * The Object class is at the root of the ActionScript class hierarchy. Objects are created by constructors using the <code>new</code> operator syntax, and can have properties assigned to them dynamically. Objects can also be created by assigning an object literal, as in:
 * <listing>
 * var obj:Object = {a:"foo", b:"bar"}</listing>
 * <p>All classes that don't declare an explicit base class extend the built-in Object class.</p>
 * <p>You can use the Object class to create <i>associative arrays</i>. At its core, an associative array is an instance of the Object class, and each key-value pair is represented by a property and its value. Another reason to declare an associative array using the Object data type is that you can then use an object literal to populate your associative array (but only at the time you declare it). The following example creates an associative array using an object literal, accesses items using both the dot operator and the array access operator, and then adds a new key-value pair by creating a new property:</p>
 * <listing>
 *  var myAssocArray:Object = {fname:"John", lname:"Public"};
 *  trace(myAssocArray.fname);     // John
 *  trace(myAssocArray["lname"]);  // Public
 *  myAssocArray.initial = "Q";
 *  trace(myAssocArray.initial);   // Q</listing>
 * <p>ActionScript 3.0 has two types of inheritance: class inheritance and prototype inheritance:</p>
 * <ul>
 * <li>Class inheritance - is the primary inheritance mechanism and supports inheritance of fixed properties. A fixed property is a variable, constant or method declared as part of a class definition. Every class definition is now represented by a special class object that stores information about the class.</li>
 * <li>Prototype inheritance - is the only inheritance mechanism in previous versions of ActionScript and serves as an alternate form of inheritance in ActionScript 3.0. Each class has an associated prototype object, and the properties of the prototype object are shared by all instances of the class. When a class instance is created, it has a reference to its class's prototype object, which serves as a link between the instance and its associated class prototype object. At run time, when a property is not found on a class instance, the delegate, which is the class prototype object, is checked for that property. If the prototype object does not contain the property, the process continues with the prototype object's delegate checking in consecutively higher levels in the hierarchy until the Flash runtime finds the property.</li></ul>
 * <p>Both class inheritance and prototype inheritance can exist simultaneously, as shown in the following example:</p>
 * <listing>
 *  class A {
 *      var x = 1
 *      prototype.px = 2
 *  }
 *  dynamic class B extends A {
 *      var y = 3
 *      prototype.py = 4
 *  }
 *
 *  var b = new B()
 *  b.x // 1 via class inheritance
 *  b.px // 2 via prototype inheritance from A.prototype
 *  b.y // 3
 *  b.py // 4 via prototype inheritance from B.prototype
 *
 *  B.prototype.px = 5
 *  b.px // now 5 because B.prototype hides A.prototype
 *
 *  b.px = 6
 *  b.px // now 6 because b hides B.prototype</listing>
 * <p>Using functions instead of classes, you can construct custom prototype inheritance trees. With classes, the prototype inheritance tree mirrors the class inheritance tree. However, since the prototype objects are dynamic, you can add and delete prototype-based properties at run time.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/./Object.html#includeExamplesSummary">View the examples</a></p>
 * @see #prototype
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f9c.html Data types
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f88.html Data type descriptions
 *
 */
[Native]
public dynamic class Object {
  /**
   * A reference to the class object or constructor function for a given object instance. If an object is an instance of a class, the <code>constructor</code> property holds a reference to the class object. If an object is created with a constructor function, the <code>constructor</code> property holds a reference to the constructor function. Do not confuse a constructor function with a constructor method of a class. A constructor function is a Function object used to create objects, and is an alternative to using the <code>class</code> keyword for defining classes.
   * <p>If you use the <code>class</code> keyword to define a class, the class's prototype object is assigned a property named <code>constructor</code> that holds a reference to the class object. An instance of the class inherits this property from the prototype object. For example, the following code creates a new class, <code>A</code>, and a class instance named <code>myA</code>:</p>
   * <listing>
   *      dynamic class A {}
   *      trace(A.prototype.constructor);      // [class A]
   *      trace(A.prototype.constructor == A); // true
   *      var myA:A = new A();
   *      trace(myA.constructor == A);         // true</listing>
   * <p>Advanced users may choose to use the <code>function</code> keyword instead of the <code>class</code> keyword to define a Function object that can be used as a template for creating objects. Such a function is called a constructor function because you can use it in conjunction with the <code>new</code> operator to create objects. If you use the <code>function</code> keyword to create a constructor function, its prototype object is assigned a property named <code>constructor</code> that holds a reference to the constructor function. If you then use the constructor function to create an object, the object inherits the <code>constructor</code> property from the constructor function's prototype object. For example, the following code creates a new constructor function, <code>f</code>, and an object named <code>myF</code>:</p>
   * <listing>
   *      function f() {}
   *      trace(f.prototype.constructor);      // function Function() {}
   *      trace(f.prototype.constructor == f); // true
   *      var myF = new f();
   *      trace(myF.constructor == f);         // true</listing>
   * <p><b>Note</b>: The <code>constructor</code> property is writable, which means that user code can change its value with an assignment statement. Changing the value of the <code>constructor</code> property is not recommended, but if you write code that depends on the value of the <code>constructor</code> property, you should ensure that the value is not reset. The value can be changed only when the property is accessed through the prototype object (for example, <code>className.prototype.constructor</code>).</p>
   * <p>If you access the <code>constructor</code> property and compile in strict mode, you will get an error at compile time because the constructor property depends on the protoype object, which is a runtime entity. If you use standard mode or if the class description specifies "dynamic", the code runs without generating an error.</p>
   * @see Class
   * @see Function
   * @see #prototype
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f36.html Classes
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f30.html Methods
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f3f.html Advanced topics
   *
   */
  public native function get constructor() : Object;

  /**
   * Creates an Object object and stores a reference to the object's constructor method in the object's <code>constructor</code> property.
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f9c.html Data types
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f88.html Data type descriptions
   *
   */
  public native function Object();

  /**
   * Indicates whether an object has a specified property defined. This method returns <code>true</code> if the target object has a property that matches the string specified by the <code>name</code> parameter, and <code>false</code> otherwise. The following types of properties cause this method to return <code>true</code> for objects that are instances of a class (as opposed to class objects):
   * <ul>
   * <li>Fixed instance properties�variables, constants, or methods defined by the object's class that are not static;</li>
   * <li>Inherited fixed instance properties�variables, constants, or methods inherited by the object's class;</li>
   * <li>Dynamic properties�properties added to an object after it is instantiated (outside of its class definition). To add dynamic properties, the object's defining class must be declared with the <code>dynamic</code> keyword.</li></ul>
   * <p>The following types of properties cause this method to return <code>false</code> for objects that are instances of a class:</p>
   * <ul>
   * <li>Static properties�variables, constants, or methods defined with the static keyword in an object's defining class or any of its superclasses;</li>
   * <li>Prototype properties�properties defined on a prototype object that is part of the object's prototype chain. In ActionScript 3.0, the prototype chain is not used for class inheritance, but still exists as an alternative form of inheritance. For example, an instance of the Array class can access the <code>valueOf()</code> method because it exists on <code>Object.prototype</code>, which is part of the prototype chain for the Array class. Although you can use <code>valueOf()</code> on an instance of Array, the return value of <code>hasOwnProperty("valueOf")</code> for that instance is <code>false</code>.</li></ul>
   * <p>ActionScript 3.0 also has class objects, which are direct representations of class definitions. When called on class objects, the <code>hasOwnProperty()</code> method returns <code>true</code> only if a property is a static property defined on that class object. For example, if you create a subclass of Array named CustomArray, and define a static property in CustomArray named <code>foo</code>, a call to <code>CustomArray.hasOwnProperty("foo")</code> returns <code>true</code>. For the static property <code>DESCENDING</code> defined in the Array class, however, a call to <code>CustomArray.hasOwnProperty("DESCENDING")</code> returns <code>false</code>.</p>
   * <p><b>Note:</b> Methods of the Object class are dynamically created on Object's prototype. To redefine this method in a subclass of Object, do not use the <code>override</code> keyword. For example, A subclass of Object implements <code>function hasOwnProperty():Boolean</code> instead of using an override of the base class.</p>
   * @param name The property of the object.
   *
   * @return If the target object has the property specified by the <code>name</code> parameter this value is <code>true</code>, otherwise <code>false</code>.
   *
   */
  public native function hasOwnProperty(name:String):Boolean;

  /**
   * Indicates whether an instance of the Object class is in the prototype chain of the object specified as the parameter. This method returns <code>true</code> if the object is in the prototype chain of the object specified by the <code>theClass</code> parameter. The method returns <code>false</code> if the target object is absent from the prototype chain of the <code>theClass</code> object, and also if the <code>theClass</code> parameter is not an object.
   * <p><b>Note:</b> Methods of the Object class are dynamically created on Object's prototype. To redefine this method in a subclass of Object, do not use the <code>override</code> keyword. For example, A subclass of Object implements <code>function isPrototypeOf():Boolean</code> instead of using an override of the base class.</p>
   * @param theClass The class to which the specified object may refer.
   *
   * @return If the object is in the prototype chain of the object specified by the <code>theClass</code> parameter this value is <code>true</code>, otherwise <code>false</code>.
   *
   */
  public native function isPrototypeOf(theClass:Object):Boolean;

  /**
   * Indicates whether the specified property exists and is enumerable. If <code>true</code>, then the property exists and can be enumerated in a <code>for..in</code> loop. The property must exist on the target object because this method does not check the target object's prototype chain.
   * <p>Properties that you create are enumerable, but built-in properties are generally not enumerable.</p>
   * <p><b>Note:</b> Methods of the Object class are dynamically created on Object's prototype. To redefine this method in a subclass of Object, do not use the <code>override</code> keyword. For example, A subclass of Object implements <code>function propertyIsEnumerable():Boolean</code> instead of using an override of the base class.</p>
   * @param name The property of the object.
   *
   * @return If the property specified by the <code>name</code> parameter is enumerable this value is <code>true</code>, otherwise <code>false</code>.
   *
   */
  public native function propertyIsEnumerable(name:String):Boolean;

  /**
   * Returns the string representation of this object, formatted according to locale-specific conventions.
   * <p>The default implementation of this method does not perform locale-specific formatting and returns the same string as <code>toString()</code>. Subclasses should provided their own locale-aware implementation when appropriate.</p>
   * <p><b>Note:</b> Methods of the Object class are dynamically created on Object's prototype. To redefine this method in a subclass of Object, do not use the <code>override</code> keyword. For example, a subclass of Object implements <code>function toLocaleString():String</code> instead of using an override of the base class.</p>
   * @return A string representation of this object formatted according to local conventions.
   *
   * @see #toString()
   *
   */
  public native function toLocaleString():String;

  /**
   * Returns the string representation of the specified object.
   * <p><b>Note:</b> Methods of the Object class are dynamically created on Object's prototype. To redefine this method in a subclass of Object, do not use the <code>override</code> keyword. For example, a subclass of Object implements <code>function toString():String</code> instead of using an override of the base class.</p>
   * @return A string representation of the object.
   *
   */
  public native function toString():String;

  /**
   * Returns the primitive value of the specified object. If this object does not have a primitive value, the object itself is returned.
   * <p><b>Note:</b> Methods of the Object class are dynamically created on Object's prototype. To redefine this method in a subclass of Object, do not use the <code>override</code> keyword. For example, A subclass of Object implements <code>function valueOf():Object</code> instead of using an override of the base class.</p>
   * @return The primitive value of this object or the object itself.
   *
   * @see #toString()
   *
   */
  public native function valueOf() : Object;

  public native function toSource():Object;

  //public native function unwatch(prop:String):void;

  //public native function watch(prop:String, handler:Object):void;

  public static native function create(proto:Object, propertyDescriptors:Object = null):Object;

  public static native function defineProperties(object:Object, propertyDescriptors:Object):Object;

  public static native function defineProperty(object:Object, propertyName:String, propertyDescriptor:Object):Object;

  public static native function freeze(object:Object):Object;

  public static native function getOwnPropertyDescriptor(object:Object, propertyName:String):Object;

  public static native function getOwnPropertyNames(object:Object):Array;

  public static native function getPrototypeOf(value:Object):Object;

  public static native function isExtensible(object:Object):Boolean;

  public static native function isFrozen(object:Object):Boolean;

  public static native function isSealed(object:Object):Boolean;

  public static native function keys(object:Object):Array;

  public static native function preventExtensions(object:Object):Object;

  public static native function seal(object:Object):Object;

}
}
