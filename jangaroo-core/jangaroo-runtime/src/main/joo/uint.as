package {

/**
 * @see int
 * @see Number
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f9c.html Data types
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f88.html Data type descriptions
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f87.html Type conversions
 */
public final class uint {

  /**
   * @see #toString
   * @see #valueOf
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f9c.html Data types
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f88.html Data type descriptions
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f87.html Type conversions
   */
  public native function uint(num:Object);

  /**
   * Converts a given numeric value to an unsigned integer value. Decimal values are truncated at the decimal point.
   * <p>The following table describes the return value of <code>uint()</code> on various input types and values.</p>
   * <table>
   * <tr><th>Input Type/Value</th><th>Example</th><th>Return Value</th></tr>
   * <tr><td><code>undefined</code></td><td><code>uint(undefined)</code></td><td><code>0</code></td></tr>
   * <tr><td><code>null</code></td><td><code>uint(null)</code></td><td><code>0</code></td></tr>
   * <tr><td><code>0</code></td><td><code>uint(0)</code></td><td><code>0</code></td></tr>
   * <tr><td><code>NaN</code></td><td><code>uint(NaN)</code></td><td><code>0</code></td></tr>
   * <tr><td>Positive floating-point number</td><td><code>uint(5.31)</code></td><td>Truncated unsigned integer (e.g. <code>5</code>)</td></tr>
   * <tr><td>Negative floating-point number</td><td><code>uint(-5.78)</code></td><td>Truncates to integer then applies rule for negative integers</td></tr>
   * <tr><td>Negative integer</td><td><code>uint(-5)</code></td><td>Sum of <code>uint.MAX_VALUE</code> and the negative integer (for example, <code>uint.MAX_VALUE + (-5)</code>)</td></tr>
   * <tr><td><code>true</code></td><td><code>uint(true)</code></td><td><code>1</code></td></tr>
   * <tr><td><code>false</code></td><td><code>uint(false)</code></td><td><code>0</code></td></tr>
   * <tr><td>Empty String</td><td><code>uint("")</code></td><td><code>0</code></td></tr>
   * <tr><td>String that converts to Number</td><td><code>uint("5")</code></td><td>The number</td></tr>
   * <tr><td>String that does not convert to Number</td><td><code>uint("5a")</code></td><td><code>0</code></td></tr>
   * </table>
   *
   * @param value A value to be converted to an integer.
   * @return uint The converted integer value.
   *
   * @see int
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f9c.html Data types
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f88.html Data type descriptions
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f87.html Type conversions
   */
  //public native function uint(value:*):uint;

}
}