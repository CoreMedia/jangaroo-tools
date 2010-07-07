/**
 * API and documentation by Adobeｮ.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * The Object class is at the root of the ActionScript class hierarchy. Objects are created by constructors using the
 * new operator syntax, and can have properties assigned to them dynamically. Objects can also be created by assigning
 * an object literal, as in:
 * <pre>
 * var obj:Object = {a:"foo", b:"bar"}
 * </pre>
 * All classes that don't declare an explicit base class extend the built-in Object class.
 * <p>You can use the Object class to create associative arrays. At its core, an associative array is an instance of
 * the Object class, and each key-value pair is represented by a property and its value. Another reason to declare an
 * associative array using the Object data type is that you can then use an object literal to populate your associative
 * array (but only at the time you declare it). The following example creates an associative array using an object
 * literal, accesses items using both the dot operator and the array access operator, and then adds a new key-value
 * pair by creating a new property:
 * <pre>
 * var myAssocArray:Object = {fname:"John", lname:"Public"};
 * trace(myAssocArray.fname);     // John
 * trace(myAssocArray["lname"]);  // Public
 * myAssocArray.initial = "Q";
 * trace(myAssocArray.initial);   // Q
 * </pre>
 * ActionScript 3.0 has two types of inheritance: class inheritance and prototype inheritance:
 * <ul>
 * <li>Class inheritance - is the primary inheritance mechanism and supports inheritance of fixed properties. A fixed
 * property is a variable, constant or method declared as part of a class definition. Every class definition is now
 * represented by a special class object that stores information about the class.
 * <li>Prototype inheritance - is the only inheritance mechanism in previous versions of ActionScript and serves as an
 * alternate form of inheritance in ActionScript 3.0. Each class has an associated prototype object, and the properties
 * of the prototype object are shared by all instances of the class. When a class instance is created, it has a
 * reference to its class's prototype object, which serves as a link between the instance and its associated class
 * prototype object. At run time, when a property is not found on a class instance, the delegate, which is the class
 * prototype object, is checked for that property. If the prototype object does not contain the property, the process
 * continues with the prototype object's delegate checking in consecutively higher levels in the hierarchy until Flash
 * Player finds the property.
 * </ul>
 * Both class inheritance and prototype inheritance can exist simultaneously, as shown in the following example:
<pre>
 class A {
     var x = 1
     prototype.px = 2
 }
 dynamic class B extends A {
     var y = 3
     prototype.py = 4
 }
  
 var b = new B()
 b.x // 1 via class inheritance
 b.px // 2 via prototype inheritance from A.prototype
 b.y // 3
 b.py // 4 via prototype inheritance from B.prototype
  
 B.prototype.px = 5
 b.px // now 5 because B.prototype hides A.prototype
  
 b.px = 6
 b.px // now 6 because b hides B.prototype
</pre>
 * Using functions instead of classes, you can construct custom prototype inheritance trees. With classes, the
 * prototype inheritance tree mirrors the class inheritance tree. However, since the prototype objects are dynamic,
 * you can add and delete prototype-based properties at run time.
 * @see #prototype
 */
public class Object {

  /**
   * A reference to the class object or constructor function for a given object instance. If an object is an instance
   * of a class, the constructor property holds a reference to the class object. If an object is created with a
   * constructor function, the constructor property holds a reference to the constructor function. Do not confuse a
   * constructor function with a constructor method of a class. A constructor function is a Function object used to
   * create objects, and is an alternative to using the class keyword for defining classes.
   * <p>If you use the class keyword to define a class, the class's prototype object is assigned a property named
   * constructor that holds a reference to the class object. An instance of the class inherits this property from the
   * prototype object. For example, the following code creates a new class, A, and a class instance named myA:
   * <pre>
   * dynamic class A {}
   * trace(A.prototype.constructor);      // [class A]
   * trace(A.prototype.constructor == A); // true
   * var myA:A = new A();
   * trace(myA.constructor == A);         // true
   * </pre>
   * Advanced users may choose to use the function keyword instead of the class keyword to define a Function object
   * that can be used as a template for creating objects. Such a function is called a constructor function because you
   * can use it in conjunction with the new operator to create objects. If you use the function keyword to create a
   * constructor function, its prototype object is assigned a property named constructor that holds a reference to the
   * constructor function. If you then use the constructor function to create an object, the object inherits the
   * constructor property from the constructor function's prototype object. For example, the following code creates a
   * new constructor function, f, and an object named myF:
   * <pre>
   * function f() {}
   * trace(f.prototype.constructor);      // function Function() {}
   * trace(f.prototype.constructor == f); // true
   * var myF = new f();
   * trace(myF.constructor == f);         // true
   * </pre>
   * Note: The constructor property is writable, which means that user code can change its value with an assignment
   * statement. Changing the value of the constructor property is not recommended, but if you write code that depends
   * on the value of the constructor property, you should ensure that the value is not reset. The value can be changed
   * only when the property is accessed through the prototype object (for example, className.prototype.constructor).
   * <p>If you access the constructor property and compile in strict mode, you will get an error at compile time
   * because the constructor property depends on the protoype object, which is a runtime entity. If you use standard
   * mode or if the class description specifies "dynamic", the code runs without generating an error.
   *
   * @see Class
   * @see Function
   * @see prototype
   * @return Object A reference to the class object or constructor function for a given object instance.
   */
  public native function get constructor() : Object;

  public native function toLocaleString():Object;

  public native function toSource():Object;

  //public native function unwatch(prop:String):void;

  //public native function watch(prop:String, handler:Object):void;

  /**
   * Indicates whether an object has a specified property defined. This method returns true if the target object has a
   * property that matches the string specified by the name parameter, and false otherwise. The following types of
   * properties cause this method to return true for objects that are instances of a class (as opposed to class
   * objects):
   * <ul>
   * <li>Fixed instance properties要ariables, constants, or methods defined by the object's class that are not static;
   * <li>Inherited fixed instance properties要ariables, constants, or methods inherited by the object's class;
   * <li>Dynamic properties用roperties added to an object after it is instantiated (outside of its class definition). 
   * o add dynamic properties, the object's defining class must be declared with the dynamic keyword.
   * </ul>
   * The following types of properties cause this method to return false for objects that are instances of a class:
   * <ul>
   * <li>Static properties要ariables, constants, or methods defined with the static keyword in an object's defining
   * class or any of its superclasses;
   * <li>Prototype properties用roperties defined on a prototype object that is part of the object's prototype chain.
   * In ActionScript 3.0, the prototype chain is not used for class inheritance, but still exists as an alternative
   * form of inheritance. For example, an instance of the Array class can access the valueOf() method because it exists
   * on Object.prototype, which is part of the prototype chain for the Array class. Although you can use valueOf() on
   * an instance of Array, the return value of hasOwnProperty("valueOf") for that instance is false.
   * </ul>
   * ActionScript 3.0 also has class objects, which are direct representations of class definitions. When called on
   * class objects, the hasOwnProperty() method returns true only if a property is a static property defined on that
   * class object. For example, if you create a subclass of Array named CustomArray, and define a static property in
   * CustomArray named foo, a call to CustomArray.hasOwnProperty("foo") returns true. For the static property
   * DESCENDING defined in the Array class, however, a call to CustomArray.hasOwnProperty("DESCENDING") returns false.
   * <p>Note: Methods of the Object class are dynamically created on Object's prototype. To redefine this method in a
   * subclass of Object, do not use the override keyword. For example, A subclass of Object implements function
   * hasOwnProperty():Boolean instead of using an override of the base class. 
   * @param name The property of the object.
   * @return Boolean If the target object has the property specified by the name parameter this value is true,
   * otherwise false.
   */
  public native function hasOwnProperty(name : String) : Boolean;

  /**
   * Indicates whether an instance of the Object class is in the prototype chain of the object specified as the
   * parameter. This method returns true if the object is in the prototype chain of the object specified by the
   * theClass parameter. The method returns false if the target object is absent from the prototype chain of the
   * theClass object, and also if the theClass parameter is not an object.
   * <p>Note: Methods of the Object class are dynamically created on Object's prototype. To redefine this method in a
   * subclass of Object, do not use the override keyword. For example, A subclass of Object implements function
   * isPrototypeOf():Boolean instead of using an override of the base class.
   * @param theClass The class to which the specified object may refer.
   * @return Boolean If the object is in the prototype chain of the object specified by the theClass parameter this
   * value is true, otherwise false.
   */
  public native function isPrototypeOf(theClass : Object) : Boolean;

  /**
   * Indicates whether the specified property exists and is enumerable. If true, then the property exists and can be
   * enumerated in a for..in loop. The property must exist on the target object because this method does not check the
   * target object's prototype chain.
   * <p>Properties that you create are enumerable, but built-in properties are generally not enumerable.
   * <p>Note: Methods of the Object class are dynamically created on Object's prototype. To redefine this method in a
   * subclass of Object, do not use the override keyword. For example, A subclass of Object implements function
   * propertyIsEnumerable():Boolean instead of using an override of the base class.
   * @param name The property of the object.
   * @return Boolean If the property specified by the name parameter is enumerable this value is true, otherwise false.
   */
  public native function propertyIsEnumerable(name : String) : Boolean;

  /**
   * Returns the string representation of the specified object.
   * <p>Note: Methods of the Object class are dynamically created on Object's prototype. To redefine this method in a
   * subclass of Object, do not use the override keyword. For example, a subclass of Object implements function
   * toString():String instead of using an override of the base class.
   * @return String A string representation of the object. 
   */
  public native function toString() : String;

  /**
   * Returns the primitive value of the specified object. If this object does not have a primitive value, the object
   * itself is returned.
   * <p>Note: Methods of the Object class are dynamically created on Object's prototype. To redefine this method in a
   * subclass of Object, do not use the override keyword. For example, A subclass of Object implements function
   * valueOf():Object instead of using an override of the base class.
   * @return Object The primitive value of this object or the object itself.
   * @see toString()
   */
  public native function valueOf() : Object;

  
}

}

