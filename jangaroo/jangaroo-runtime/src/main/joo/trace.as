/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * Displays expressions, or writes to log files, while debugging. A single trace statement can support multiple arguments. If any argument in a trace statement includes a data type other than a String, the trace function invokes the associated <code>toString()</code> method for that data type. For example, if the argument is a Boolean value the trace function invokes <code>Boolean.toString()</code> and displays the return value.
 * @param msgs One or more (comma separated) expressions to evaluate. For multiple expressions, a space is inserted between each expression in the output.
 *
 * @example The following example uses the class <code>TraceExample</code> to show how the <code>trace()</code> method can be used to print a simple string. Generally, the message will be printed to a "Debug" console.
 * <listing>
 * package {
 *     import flash.display.Sprite;
 *
 *     public class TraceExample extends Sprite {
 *
 *         public function TraceExample() {
 *             trace("Hello World");
 *         }
 *     }
 * }
 * </listing>
 */
public native function trace(...msgs) : void;

}
