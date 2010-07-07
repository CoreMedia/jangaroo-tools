/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * A Class object is created for each class definition in a program.
 * Every Class object is an instance of the Class class.
 * The Class object contains the static properties and methods of the class.
 * The class object creates instances of the class when invoked using the new operator.
 * <p>Some methods return an object of type Class. Other methods may have a parameter of type Class.
 * <p>The class name is the reference to the Class object, as this example shows:
 * <pre>
 * class Foo {}
 * </pre>
 * The class Foo{} statement is the class definition that creates the Class object Foo.
 * Additionally, the statement new Foo() will create a new instance of class Foo, and the result will be of type Foo.
 * <p>Use the class statement to declare your classes. Class objects are useful for advanced techniques, such as
 * assigning classes to an existing instance object at runtime.
 * <p>Any static properties and methods of a class live on the class's Class object. Class, itself, declares prototype.
 */
public dynamic class Class {

  /**
    * A reference to the prototype object of a class or function object. The prototype property is automatically created
    * and attached to any class or function object that you create. If you create a class, the value of the prototype property is
    * shared by all instances of the class and is accessible only as a class property. Instances of your class cannot
    * directly access the prototype property
    * <p>A class's prototype object is a special instance of that class that provides a mechanism for sharing state
    * across all instances of a class. At run time, when a property is not found on a class instance, the delegate,
    * which is the class prototype object, is checked for that property. If the prototype object does not contain the
    * property, the process continues with the prototype object's delegate checking in consecutively higher levels in
    * the hierarchy until Flash Player or the Adobe Integrated Runtime finds the property.
    * <p>Note: In ActionScript 3.0, prototype inheritance is not the primary mechanism for inheritance. Class
    * inheritance, which drives the inheritance of fixed properties in class definitions, is the primary inheritance
    * mechanism in ActionScript 3.0.
    * @return Object A reference to the prototype object of a class or function object.
    */
   public native function get prototype() : Object;

   public native function set prototype(o:Object):void;
  
}

}