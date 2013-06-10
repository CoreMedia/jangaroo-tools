package flash.utils {

[Native(global="clearInterval")]
/**
 * Cancels a specified <code>setInterval()</code> call.
 * @param id The ID of the <code>setInterval()</code> call, which you set to a variable, as in the following:
 *
 * @see #setInterval
 *
 * @example The following example uses the <code>setInterval()</code> method to create a timed interval, calling the <code>myRepeatingFunction()</code> method after regular intervals of one second.
 * <p>Each call of the <code>myRepeatingFunction</code> method increments the <code>counter</code> property, and when it equals the <code>stopCount</code> property, the <code>clearInterval()</code> method is called with the property <code>intervalId</code> which is a reference id to the interval that was created earlier.</p>
 * <listing>
 * package {
 *     import flash.display.Sprite;
 *     import flash.utils.*;
 *
 *     public class ClearIntervalExample extends Sprite {
 *         private var intervalDuration:Number = 1000; // duration between intervals, in milliseconds
 *         private var intervalId:uint;
 *         private var counter:uint     = 0;
 *         private var stopCount:uint     = 3;
 *
 *         public function ClearIntervalExample() {
 *             intervalId = setInterval(myRepeatingFunction, intervalDuration, "Hello", "World");
 *         }
 *
 *         public function myRepeatingFunction():void {
 *             trace(arguments[0] + " " + arguments[1]);
 *
 *             counter++;
 *             if(counter == stopCount) {
 *                 trace("Clearing Interval");
 *                 clearInterval(intervalId);
 *             }
 *         }
 *     }
 * }
 * </listing>
 */
public native function clearInterval(id:uint):void
}