package flash.utils {

[Native("setTimeout")]
/**
 * Runs a specified function after a specified delay (in milliseconds).
 * <p>Instead of using this method, consider creating a Timer object, with the specified interval, using 1 as the <code>repeatCount</code> parameter (which sets the timer to run only once).</p>
 * <p>If you intend to use the <code>clearTimeout()</code> method to cancel the <code>setTimeout()</code> call, be sure to assign the <code>setTimeout()</code> call to a variable (which the <code>clearTimeout()</code> function will later reference). If you do not call the <code>clearTimeout()</code> function to cancel the <code>setTimeout()</code> call, the object containing the set timeout closure function will not be garbage collected.</p>
 * @param closure The name of the function to execute. Do not include quotation marks or parentheses, and do not specify parameters of the function to call. For example, use <code>functionName</code>, not <code>functionName()</code> or <code>functionName(param)</code>.
 * @param delay The delay, in milliseconds, until the function is executed.
 * @param rest An optional list of arguments that are passed to the closure function.
 *
 * @return Unique numeric identifier for the timed process. Use this identifier to cancel the process, by calling the <code>clearTimeout()</code> method.
 *
 * @see #clearTimeout
 *
 * @example
 * The following example uses the <code>setTimeout()</code> method to call another method following a specified delay period.
 * <listing>
 * package {
 *     import flash.display.Sprite;
 *     import flash.utils.*;
 *
 *     public class SetTimeoutExample extends Sprite {
 *         private var delay:Number = 1000; // delay before calling myDelayedFunction
 *
 *         public function SetTimeoutExample() {
 *             var intervalId:uint = setTimeout(myDelayedFunction, delay, "Hello", "World");
 *         }
 *
 *         public function myDelayedFunction():void {
 *             trace(arguments[0] + " " + arguments[1]);
 *         }
 *     }
 * }
 * </listing>
 */
public native function setTimeout(closure:Function, delay:Number, ...rest):uint;
}