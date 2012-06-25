/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * The Error class contains information about an error that occurred in a script. In developing ActionScript 3.0 applications, when you run your compiled code in the debugger version of a Flash runtime, a dialog box displays exceptions of type Error, or of a subclass, to help you troubleshoot the code. You create an Error object by using the <code>Error</code> constructor function. Typically, you throw a new Error object from within a <code>try</code> code block that is caught by a <code>catch</code> or <code>finally</code> code block.
 * <p>You can also create a subclass of the Error class and throw instances of that subclass.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/./Error.html#includeExamplesSummary">View the examples</a></p>
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ecc.html Working with the debugger versions of Flash runtimes
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ed0.html Creating custom error classes
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ecf.html Responding to error events and status
 *
 */
[Native]
public dynamic class Error {
  /**
   * Contains the reference number associated with the specific error message. For a custom Error object, this number is the value from the <code>id</code> parameter supplied in the constructor.
   */
  public function get errorID():int {
    throw new Error('not implemented'); // TODO: implement!
  }

  /**
   * Contains the message associated with the Error object. By default, the value of this property is "<code>Error</code>". You can specify a <code>message</code> property when you create an Error object by passing the error string to the <code>Error</code> constructor function.
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/statements.html#throw statements.html#throw
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/statements.html#try..catch..finally statements.html#try..catch..finally
   *
   */
  public var message :String;
  /**
   * Contains the name of the Error object. By default, the value of this property is "<code>Error</code>".
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/statements.html#throw statements.html#throw
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/statements.html#try..catch..finally statements.html#try..catch..finally
   *
   */
  public var name:String;

  /**
   * Creates a new Error object. If <code>message</code> is specified, its value is assigned to the object's <code>Error.message</code> property.
   * @param message A string associated with the Error object; this parameter is optional.
   * @param id A reference number to associate with the specific error message.
   *
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/statements.html#throw statements.html#throw
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/statements.html#try..catch..finally statements.html#try..catch..finally
   *
   * @example The following example creates a new Error object <code>err</code> and then, using the <code>Error()</code> constructor, assigns the string <code>"New Error Message"</code> to <code>err</code>.
   * <listing>
   * var err:Error = new Error();
   * trace(err.toString());    // Error
   *
   * err = new Error("New Error Message");
   * trace(err.toString());    // Error: New Error Message
   * </listing>
   */
  public native function Error(message : String = "", id:int = 0);

  /**
   * Returns the call stack for an error as a string at the time of the error's construction (for the debugger version of Flash Player and the AIR Debug Launcher (ADL) only; returns <code>null</code> if not using the debugger version of Flash Player or the ADL. As shown in the following example, the first line of the return value is the string representation of the exception object, followed by the stack trace elements:
   * <listing>
   *      TypeError: null cannot be converted to an object
   *          at com.xyz.OrderEntry.retrieveData(OrderEntry.as:995)
   *          at com.xyz.OrderEntry.init(OrderEntry.as:200)
   *          at com.xyz.OrderEntry.$construct(OrderEntry.as:148)
   *        </listing>
   * @return A string representation of the call stack.
   *
   */
  public function getStackTrace():String {
    throw new Error('not implemented'); // TODO: implement!
  }

  /**
   * Returns the string <code>"Error"</code> by default or the value contained in the <code>Error.message</code> property, if defined.
   * @return The error message.
   *
   * @see #message
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/statements.html#throw statements.html#throw
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/statements.html#try..catch..finally statements.html#try..catch..finally
   *
   * @example The following example creates a new Error object <code>err</code> and then, using the <code>Error()</code> constructor, assigns the string <code>"New Error Message"</code> to <code>err</code>. Finally, the <code>message</code> property is set to <code>"Another New Error Message"</code>, which overwrites <code>"New Error Message"</code>.
   * <listing>
   * var err:Error = new Error();
   * trace(err.toString());    // Error
   *
   * err = new Error("New Error Message");
   * trace(err.toString());    // Error: New Error Message
   *
   * err.message = "Another New Error Message";
   * trace(err.toString());    // Error: Another New Error Message
   * </listing>
   */
  public native function toString():String;
}
}
