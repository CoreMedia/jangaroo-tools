package flash.utils {

[Native(global="setInterval")]
/**
 * Runs a function at a specified interval (in milliseconds).
 * <p>Instead of using the <code>setInterval()</code> method, consider creating a Timer object, with the specified interval, using 0 as the <code>repeatCount</code> parameter (which sets the timer to repeat indefinitely).</p>
 * <p>If you intend to use the <code>clearInterval()</code> method to cancel the <code>setInterval()</code> call, be sure to assign the <code>setInterval()</code> call to a variable (which the <code>clearInterval()</code> function will later reference). If you do not call the <code>clearInterval()</code> function to cancel the <code>setInterval()</code> call, the object containing the set timeout closure function will not be garbage collected.</p>
 * @param closure The name of the function to execute. Do not include quotation marks or parentheses, and do not specify parameters of the function to call. For example, use <code>functionName</code>, not <code>functionName()</code> or <code>functionName(param)</code>.
 * @param delay The interval, in milliseconds.
 * @param rest An optional list of arguments that are passed to the closure function.
 *
 * @return Unique numeric identifier for the timed process. Use this identifier to cancel the process, by calling the <code>clearInterval()</code> method.
 *
 * @see #clearInterval
 *
 * @example The following example uses the <code>setInterval()</code> method to create a timed interval, calling the <code>myRepeatingFunction()</code> method after regular intervals of one second.
 * <listing>
 * package {
 *     import flash.display.Sprite;
 *     import flash.utils.*;
 *
 *     public class SetIntervalExample extends Sprite {
 *         private var intervalDuration:Number = 1000; // duration between intervals, in milliseconds
 *
 *         public function SetIntervalExample() {
 *             var intervalId:uint = setInterval(myRepeatingFunction, intervalDuration, "Hello", "World");
 *         }
 *
 *         public function myRepeatingFunction():void {
 *             trace(arguments[0] + " " + arguments[1]);
 *         }
 *     }
 * }
 * </listing>
 */
public native function setInterval(closure:Function, delay:Number, ...rest):uint;
}
