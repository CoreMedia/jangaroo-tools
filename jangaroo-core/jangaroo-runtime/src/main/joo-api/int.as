package {

/**
 * The int class lets you work with the data type representing a 32-bit signed integer. The range of values represented by the int class is -2,147,483,648 (-2^31) to 2,147,483,647 (2^31-1).
 * <p>The constant properties of the int class, <code>MAX_VALUE</code> and <code>MIN_VALUE</code>, are static, which means that you don't need an object to use them, so you don't need to use the constructor. The methods, however, are not static, which means that you do need an object to use them. You can create an int object by using the int class constructor or by declaring a variable of type int and assigning the variable a literal value.</p>
 * <p>The int data type is useful for loop counters and other situations where a floating point number is not needed, and is similar to the int data type in Java and C++. The default value of a variable typed as int is <code>0</code></p>
 * <p>If you are working with numbers that exceed <code>int.MAX_VALUE</code>, consider using Number.</p>
 * <p>The following example calls the <code>toString()</code> method of the int class, which returns the string <code>1234</code>:</p>
 * <listing>
 *  var myint:int = 1234;
 *  myint.toString();
 * </listing>
 * <p>The following example assigns the value of the <code>MIN_VALUE</code> property to a variable declared without the use of the constructor:</p>
 * <pre>
 * var smallest:int = int.MIN_VALUE;
 * </pre>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/./int.html#includeExamplesSummary">View the examples</a></p>
 * @see uint
 * @see Number
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f9c.html Data types
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f88.html Data type descriptions
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f87.html Type conversions
 *
 */
public final class int {
  /**
   * Constructor; creates a new int object. You must use the int constructor when using <code>int.toString()</code> and <code>int.valueOf()</code>. You do not use a constructor when using the properties of an int object. The <code>new int</code> constructor is primarily used as a placeholder. An int object is not the same as the <code>int()</code> function that converts a parameter to a primitive value.
   * When called as a function (not as constructor), converts a given numeric value to an integer value. Decimal values are truncated at the decimal point.
   *
   * @param num The numeric value of the int object being created or a value to be converted to a number. The default value is 0 if <code>value</code> is not provided.
   *
   * @return The new integer object or the converted integer value.
   *
   * @see #toString()
   * @see #valueOf()
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f9c.html Data types
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f88.html Data type descriptions
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f87.html Type conversions
   *
   * @example <a href="http://www.adobe.com/go/learn_as3_usingexamples_en">How to use this example</a>The following code constructs new int objects:
   * <pre>
   * var n1:int = new int(3.4);
   * var n2:int = new int(-10);
   * </pre>
   */
  public native function int(num:Object);

  /**
   * Returns a string representation of the number in exponential notation. The string contains one digit before the decimal point and up to 20 digits after the decimal point, as specified by the <code>fractionDigits</code> parameter.
   * @param fractionDigits An integer between 0 and 20, inclusive, that represents the desired number of decimal places.
   *
   * @return <code><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/String.html">String</a></code>
   *
   * @throws RangeError Throws an exception if the <code>fractionDigits</code> argument is outside the range 0 to 20.
   *
   * @example The following example shows how <code>toExponential(2)</code> returns a string in exponential notation.
   * <listing>
   * var num:Number = 315003;
   * trace(num.toExponential(2)); // 3.15e+5
   *
   * </listing>
   */
  public native function toExponential(fractionDigits:uint):String;

  /**
   * Returns a string representation of the number in fixed-point notation. Fixed-point notation means that the string will contain a specific number of digits after the decimal point, as specified in the <code>fractionDigits</code> parameter. The valid range for the <code>fractionDigits</code> parameter is from 0 to 20. Specifying a value outside this range throws an exception.
   * @param fractionDigits An integer between 0 and 20, inclusive, that represents the desired number of decimal places.
   *
   * @return <code><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/String.html">String</a></code>
   *
   * @throws RangeError Throws an exception if the <code>fractionDigits</code> argument is outside the range 0 to 20.
   *
   * @example The following example shows how <code>toFixed(3)</code> returns a string that rounds to three decimal places.
   * <listing>
   * var num:Number = 7.31343;
   * trace(num.toFixed(3)); // 7.313
   * </listing>
   * <div>The following example shows how <code>toFixed(2)</code> returns a string that adds trailing zeroes.
   * <listing>
   * var num:Number = 4;
   * trace(num.toFixed(2)); // 4.00
   * </listing></div>
   */
  public native function toFixed(fractionDigits:uint):String;

  /**
   * Returns a string representation of the number either in exponential notation or in fixed-point notation. The string will contain the number of digits specified in the <code>precision</code> parameter.
   * @param precision An integer between 1 and 21, inclusive, that represents the desired number of digits to represent in the resulting string.
   *
   * @return <code><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/String.html">String</a></code>
   *
   * @throws RangeError Throws an exception if the <code>precision</code> argument is outside the range 1 to 21.
   *
   * @example The following example shows how <code>toPrecision(3)</code> returns a string with only three digits. The string is in fixed-point notation because exponential notation is not required.
   * <listing>
   * var num:Number = 31.570;
   * trace(num.toPrecision(3)); // 31.6
   * </listing>
   * <div>The following example shows how <code>toPrecision(3)</code> returns a string with only three digits. The string is in exponential notation because the resulting number does not contain enough digits for fixed-point notation.
   * <listing>
   * var num:Number = 4000;
   * trace(num.toPrecision(3)); // 4.00e+3
   * </listing></div>
   */
  public native function toPrecision(precision:uint):String;

  /**
   * Returns the string representation of an <code>int</code> object.
   * @param radix Specifies the numeric base (from 2 to 36) to use for the number-to-string conversion. If you do not specify the <code>radix</code> parameter, the default value is 10.
   *
   * @return A string.
   *
   * @example <a href="http://www.adobe.com/go/learn_as3_usingexamples_en">How to use this example</a>The following example uses 2 and 8 for the <code>radix</code> parameter and returns a string that contains the corresponding representation of the number 9:
   * <pre>
   * var myint:int = new int(9);
   * trace(myint.toString(2)); // 1001
   * trace(myint.toString(8)); // 11
   * </pre>
   * <p>The following example results in a hexadecimal value.</p>
   * <pre>
   * var r:int = new int(250);
   * var g:int = new int(128);
   * var b:int = new int(114);
   * var rgb:String = "0x"+ r.toString(16)+g.toString(16)+b.toString(16);
   * trace(rgb); // 0xfa8072
   * </pre>
   */
  public native function toString(radix:uint):String;

  /**
   * Returns the primitive value of the specified int object.
   * @return An int value.
   *
   * @example <a href="http://www.adobe.com/go/learn_as3_usingexamples_en">How to use this example</a>The following example results in the primative value of the <code>numSocks</code> object.
   * <pre>
   * var numSocks:int = new int(2);
   * trace(numSocks.valueOf()); // 2
   * </pre>
   */
  public native function valueOf():int;

  /**
   * The largest representable 32-bit signed integer, which is 2,147,483,647.
   * @example <a href="http://www.adobe.com/go/learn_as3_usingexamples_en">How to use this example</a>The following ActionScript displays the largest and smallest representable int objects to the Output panel:
   * <pre>
   * trace("int.MIN_VALUE = "+int.MIN_VALUE);
   * trace("int.MAX_VALUE = "+int.MAX_VALUE);
   * </pre>
   * <p>This code displays the following values:</p>
   * <pre>
   * int.MIN_VALUE = -2147483648
   * int.MAX_VALUE = 2147483647
   * </pre>
   */
  public static native function get MAX_VALUE():uint;

  /**
   * The smallest representable 32-bit signed integer, which is -2,147,483,648.
   * @example <a href="http://www.adobe.com/go/learn_as3_usingexamples_en">How to use this example</a>The following ActionScript displays the largest and smallest representable int objects to the Output panel:
   * <pre>
   * trace("int.MIN_VALUE = "+int.MIN_VALUE);
   * trace("int.MAX_VALUE = "+int.MAX_VALUE);
   * </pre>
   * <p>This code displays the following values:</p>
   * <pre>
   * int.MIN_VALUE = -2147483648
   * int.MAX_VALUE = 2147483647
   * </pre>
   */
  public static native function get MIN_VALUE():uint;
}
}