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

}

}