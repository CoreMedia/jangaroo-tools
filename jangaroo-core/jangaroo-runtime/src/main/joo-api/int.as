package {

/**
 * The int class lets you work with the data type representing a 32-bit signed integer.
 * The range of values represented by the int class is -2,147,483,648 (-2^31) to 2,147,483,647 (2^31-1).
 * <p>The constant properties of the int class, <code>MAX_VALUE</code> and <code>MIN_VALUE</code>, are static, which
 * means that you don't need an object to use them, so you don't need to use the constructor. The methods, however, are
 * not static, which means that you do need an object to use them. You can create an int object by using the int class
 * constructor or by declaring a variable of type int and assigning the variable a literal value.</p>
 * <p>The int data type is useful for loop counters and other situations where a floating point number is not needed,
 * and is similar to the int data type in Java and C++. The default value of a variable typed as int is <code>0</code>.
 * </p>
 * <p>If you are working with numbers that exceed <code>int.MAX_VALUE</code>, consider using Number.</p>
 * <p>The following example calls the <code>toString()</code> method of the int class, which returns the string
 * <code>1234</code>:</p>
 * <pre>
 * var myint:int = 1234;
 * myint.toString();
 * </pre>
 * The following example assigns the value of the <code>MIN_VALUE</code> property to a variable declared without the
 * use of the constructor:
 * <pre>
 * var smallest:int = int.MIN_VALUE;
 * </pre>
 *
 * @see uint
 * @see Number
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f9c.html Data types
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f88.html Data type descriptions
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f87.html Type conversions
 */
public final class int {

  /**
   *
   * The largest representable 32-bit signed integer, which is 2,147,483,647.
   *
   * @example
   * The following ActionScript displays the largest and smallest representable int objects to the Output panel:
   * <pre>
   * trace("int.MIN_VALUE = "+int.MIN_VALUE);
   * trace("int.MAX_VALUE = "+int.MAX_VALUE);
   * </pre>
   * This code displays the following values:
   * <pre>
   * int.MIN_VALUE = -2147483648
   * int.MAX_VALUE = 2147483647
   * </pre>
   */
  public static native function get MAX_VALUE():int;

  /**
   *
   * The smallest representable 32-bit signed integer, which is -2,147,483,648.
   *
   * @example
   * The following ActionScript displays the largest and smallest representable int objects to the Output panel:
   * <pre>
   * trace("int.MIN_VALUE = "+int.MIN_VALUE);
   * trace("int.MAX_VALUE = "+int.MAX_VALUE);
   * </pre>
   * This code <span src="flashonly">displays</span> the following values:
   * <pre>
   * int.MIN_VALUE = -2147483648
   * int.MAX_VALUE = 2147483647
   * </pre>
   */
  public static native function get MIN_VALUE():int;

  /**
   * Constructor; creates a new int object. You must use the int constructor when using <code>int.toString()</code> and
   * <code>int.valueOf()</code>. You do not use a constructor when using the properties of an int object.
   * The <code>new int</code> constructor is primarily used as a placeholder. An int object is not the same as the
   * <code>int()</code> function that converts a parameter to a primitive value.
   *
   * @example
   * The following code constructs new int objects:
   * <pre>
   * var n1:int = new int(3.4);
   * var n2:int = new int(-10);
   * </pre>
   *
   * @param num Object The numeric value of the int object being created or a value to be converted to a&nbsp;number.
   *   The default value is 0 if <code>value</code> is not provided.
   *
   * @see #toString
   * @see #valueOf
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f9c.html Data types
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f88.html Data type descriptions
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f87.html Type conversions
   */
  public native function int(num:Object);

/**
 * Converts a given numeric value to an integer value. Decimal values are truncated at the decimal point.
 * @param value A value to be converted to an integer.
 * @return int The converted integer value.
 * @see uint
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f9c.html Data types
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f88.html Data type descriptions
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f87.html Type conversions
 */
//public native function int(value:*):int;

}
}