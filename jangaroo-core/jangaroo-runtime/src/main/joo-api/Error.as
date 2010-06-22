/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * The Error class contains information about an error that occurred in a script.
 * In developing ActionScript 3.0 applications, when you run your compiled code, a dialog box displays exceptions of
 * type Error, or of a subclass, to help you troubleshoot the code. You create an Error object by using the Error
 * constructor function. Typically, you throw a new Error object from within a try code block that is caught by a
 * catch or finally code block.
 * <p>You can also create a subclass of the Error class and throw instances of that subclass.
 */
public class Error {

  public var name :String;
  public var message :String;

  /**
   * Creates a new Error object. If message is specified, its value is assigned to the object's Error.message property.
   * @example
   * The following example creates a new Error object err and then, using the Error() constructor, assigns the string
   * "New Error Message" to err.
   * <pre>
   * var err:Error = new Error();
   * trace(err.toString());    // Error
   * 
   * err = new Error("New Error Message");
   * trace(err.toString());    // Error: New Error Message
   * </pre>
   * @param message A string associated with the Error object; this parameter is optional.
   */
  public native function Error(message : String = "");

}

}