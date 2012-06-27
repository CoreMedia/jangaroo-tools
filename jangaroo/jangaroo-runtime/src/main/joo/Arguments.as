package {
/**
 * An arguments object is used to store and access a function's arguments. Within a function's body, you can access its arguments object by using the local arguments variable.
 * <p>The arguments are stored as array elements: the first is accessed as <code>arguments[0]</code>, the second as <code>arguments[1]</code>, and so on. The <code>arguments.length</code> property indicates the number of arguments passed to the function. There may be a different number of arguments passed than the function declares.</p>
 * <p>Unlike previous versions of ActionScript, ActionScript 3.0 has no <code>arguments.caller</code> property. To get a reference to the function that called the current function, you must pass a reference to that function as an argument. An example of this technique can be found in the example for <code>arguments.callee</code>.</p>
 * <p>ActionScript 3.0 includes a new <code>...(rest)</code> keyword that is recommended instead of the arguments class.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/./arguments.html#includeExamplesSummary">View the examples</a></p>
 * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/statements.html#..._(rest)_parameter ...(rest)
 * @see Function
 *
 */
[Native]
public interface Arguments {
  /**
   * A reference to the currently executing function.
   * @example The following code shows how to get a reference to the function that calls the function named <code>secondFunction()</code>. The <code>firstFunction()</code> function has the Boolean argument of <code>true</code> to demonstrate that <code>secondFunction()</code> successfully calls <code>firstFunction()</code> and to prevent an infinite loop of each function calling the other.
   * <p>Because the <code>callSecond</code> parameter is <code>true</code>, <code>firstFunction()</code> calls <code>secondFunction()</code> and passes a reference to itself as the only argument. The function <code>secondFunction()</code> receives this argument and stores it using a parameter named <code>caller</code>, which is of data type Function. From within <code>secondFunction()</code>, the <code>caller</code> parameter is then used to call the <code>firstFunction</code> function, but this time with the <code>callSecond</code> argument set to <code>false</code>.</p>
   * <p>When execution returns to <code>firstFunction()</code>, the <code>trace()</code> statement is executed because <code>callSecond</code> is <code>false</code>.</p>
   * <listing>
   *   package {
   *     import flash.display.Sprite;
   *
   *     public class ArgumentsExample extends Sprite {
   *         private var count:int = 1;
   *
   *         public function ArgumentsExample() {
   *             firstFunction(true);
   *         }
   *
   *         public function firstFunction(callSecond:Boolean) {
   *             trace(count + ": firstFunction");
   *             if(callSecond) {
   *                 secondFunction(arguments.callee);
   *             }
   *             else {
   *                 trace("CALLS STOPPED");
   *             }
   *         }
   *
   *         public function secondFunction(caller:Function) {
   *             trace(count + ": secondFunction\n");
   *             count++;
   *             caller(false);
   *         }
   *     }
   * }
   * </listing>
   */
  function get callee():Function;
  /**
   * The number of arguments passed to the function. This may be more or less than the function declares.
   */
  function get length():uint;
}
}
