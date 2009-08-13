/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * Displays expressions, or writes to log files, while debugging.
 * A single trace statement can support multiple arguments.
 * If any argument in a trace statement includes a data type other than a String,
 * the trace function invokes the associated toString() method for that data type.
 * For example, if the argument is a Boolean value the trace function invokes
 * Boolean.toString() and displays the return value.
 * @example
 * The following example uses the class TraceExample to show how the trace() method can be
 * used to print a simple string. Generally, the message will be printed to a "Debug" console.
<pre>
package {
    import flash.display.Sprite;

    public class TraceExample extends Sprite {

        public function TraceExample() {
            trace("Hello World");
        }
    }
}
</pre>
 * @param msgs One or more (comma separated) expressions to evaluate.
 *  For multiple expressions, a space is inserted between each expression in the output.
 */
public native function trace(...msgs) : void;

}