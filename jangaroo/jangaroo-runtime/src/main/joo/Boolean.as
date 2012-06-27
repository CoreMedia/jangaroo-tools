package {


/**
 * A Boolean object is a data type that can have one of two values, either <code>true</code> or <code>false</code>, used for logical operations. Use the Boolean class to retrieve the primitive data type or string representation of a Boolean object.
 * <p>To create a Boolean object, you can use the constructor or the global function, or assign a literal value. It doesn't matter which technique you use; in ActionScript 3.0, all three techniques are equivalent. (This is different from JavaScript, where a Boolean object is distinct from the Boolean primitive type.)</p>
 * <p>The following lines of code are equivalent:</p>
 * <listing>
 * var flag:Boolean = true;
 * var flag:Boolean = new Boolean(true);
 * var flag:Boolean = Boolean(true);
 * </listing>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/./Boolean.html#includeExamplesSummary">View the examples</a></p>
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f9c.html Data types
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f88.html Data type descriptions
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f87.html Type conversions
 *
 */
[Native]
public final class Boolean {
  /**
   * Creates a Boolean object with the specified value. If you omit the <code>expression</code> parameter, the Boolean object is initialized with a value of <code>false</code>. If you specify a value for the <code>expression</code> parameter, the method evaluates it and returns the result as a Boolean value according to the rules in the global <code>Boolean()</code> function.
   * <p>When called as a function (not as constructor), it converts the <code>expression</code> parameter to a Boolean value and returns the value.</p>
   * <p>Unlike previous versions of ActionScript, the <code>Boolean()</code> function returns the same results as does the Boolean class constructor.</p>
   * <p>The result depends on the data type and value of the argument, as described in the following table:</p>
   * <table>
   * <tr><th>Input Value</th><th>Example</th><th>Return Value</th></tr>
   * <tr>
   * <td><code>0</code></td>
   * <td><code>Boolean(0)</code></td>
   * <td><code>false</code></td></tr>
   * <tr>
   * <td><code>NaN</code></td>
   * <td><code>Boolean(NaN)</code></td>
   * <td><code>false</code></td></tr>
   * <tr>
   * <td>Number (not <code>0</code> or <code>NaN</code>)</td>
   * <td><code>Boolean(4)</code></td>
   * <td><code>true</code></td></tr>
   * <tr>
   * <td>Empty string</td>
   * <td><code>Boolean("")</code></td>
   * <td><code>false</code></td></tr>
   * <tr>
   * <td>Non-empty string</td>
   * <td><code>Boolean("6")</code></td>
   * <td><code>true</code></td></tr>
   * <tr>
   * <td><code>null</code></td>
   * <td><code>Boolean(null)</code></td>
   * <td><code>false</code></td></tr>
   * <tr>
   * <td><code>undefined</code></td>
   * <td><code>Boolean(undefined)</code></td>
   * <td><code>false</code></td></tr>
   * <tr>
   * <td>Instance of Object class</td>
   * <td><code>Boolean(new Object())</code></td>
   * <td><code>true</code></td></tr>
   * <tr>
   * <td>No argument</td>
   * <td><code>Boolean()</code></td>
   * <td><code>false</code></td></tr></table>
   *
   * @param expression Any expression.
   *
   * @return A new Boolean or the result of the conversion to Boolean.
   *
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f9c.html Data types
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f88.html Data type descriptions
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f87.html Type conversions
   *
   * @example The following code creates a new Boolean object, initialized to a value of <code>false</code> called <code>myBoolean</code>:
   * <listing>
   *  var myBoolean:Boolean = new Boolean();
   * </listing>
   */
  public native function Boolean(expression:Object = false);

  /**
   * Returns the string representation (<code>"true"</code> or <code>"false"</code>) of the Boolean object. The output is not localized, and is <code>"true"</code> or <code>"false"</code> regardless of the system language.
   * @return The string <code>"true"</code> or <code>"false"</code>.
   *
   * @example This example creates a variable of type Boolean and then uses the <code>toString()</code> method to convert the value to a string for use in an array of strings:
   * <listing>
   *      var myStringArray:Array = new Array("yes", "could be");
   *      var myBool:Boolean = 0;
   *      myBool.toString();
   *      myStringArray.push(myBool);
   *      trace(myStringArray); // yes,could be,false
   *     </listing>
   */
  public native function toString():String;

  /**
   * Returns <code>true</code> if the value of the specified Boolean object is true; <code>false</code> otherwise.
   * @return A Boolean value.
   *
   * @example The following example shows how this method works, and also shows that the value of a new Boolean object is <code>false</code>:
   * <listing>
   *      var myBool:Boolean = new Boolean();
   *      trace(myBool.valueOf());   // false
   *      myBool = (6==3+3);
   *      trace(myBool.valueOf());   // true
   *     </listing>
   */
  public native function valueOf():Boolean;
}
}
