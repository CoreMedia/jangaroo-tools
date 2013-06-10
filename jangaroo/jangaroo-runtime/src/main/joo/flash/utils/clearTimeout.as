package flash.utils {

[Native(global="clearTimeout")]
/**
 * Cancels a specified <code>setTimeout()</code> call.
 * @param id The ID of the <code>setTimeout()</code> call, which you set to a variable, as in the following:
 *
 * @see #setTimeout
 *
 * @example The following example uses the <code>setTimeout()</code> method to call another method following a specified delay period.
 * <p>A loop is created to count to one million. If the system can process this request faster than a second can expire, <code>clearTimeout()</code> will remove the <code>setTimeout()</code> request, and <code>myDelayedFunction()</code> will not be called.</p>
 * <listing>
 * package {
 *     import flash.display.Sprite;
 *     import flash.utils.*;
 *
 *     public class ClearTimeoutExample extends Sprite {
 *         private var delay:Number = 1000; // delay before calling myDelayedFunction
 *         private var intervalId:uint;
 *         private var count:uint = 1000000;
 *
 *         public function ClearTimeoutExample() {
 *             intervalId = setTimeout(myDelayedFunction, delay);
 *             startCounting();
 *         }
 *
 *         public function startCounting():void {
 *             var i:uint = 0;
 *             do {
 *                 if(i == count-1) {
 *                     clearTimeout(intervalId);
 *                     trace("Your computer can count to " + count + " in less than " + delay/1000 + " seconds.");
 *                 }
 *                 i++;
 *             } while(i &lt; count)
 *         }
 *
 *         public function myDelayedFunction():void {
 *             trace("Time expired.");
 *         }
 *     }
 * }
 * </listing>
 */
public native function clearTimeout(id:uint):void;
}