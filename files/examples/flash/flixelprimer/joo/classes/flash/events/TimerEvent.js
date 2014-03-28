joo.classLoader.prepare("package flash.events",/* {*/

/**
 * A Timer object dispatches a TimerEvent objects whenever the Timer object reaches the interval specified by the <code>Timer.delay</code> property.
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/TimerEvent.html#includeExamplesSummary">View the examples</a></p>
 * @see flash.utils.Timer
 *
 */
"public class TimerEvent extends flash.events.Event",2,function($$private){;return[ 
  /**
   * Creates an Event object with specific information relevant to <code>timer</code> events. Event objects are passed as parameters to event listeners.
   * @param type The type of the event. Event listeners can access this information through the inherited <code>type</code> property.
   * @param bubbles Determines whether the Event object bubbles. Event listeners can access this information through the inherited <code>bubbles</code> property.
   * @param cancelable Determines whether the Event object can be canceled. Event listeners can access this information through the inherited <code>cancelable</code> property.
   *
   */
  "public function TimerEvent",function TimerEvent$(type/*:String*/, bubbles/*:Boolean = false*/, cancelable/*:Boolean = false*/) {if(arguments.length<3){if(arguments.length<2){bubbles = false;}cancelable = false;}
    this.super$2(type, bubbles, cancelable);
  },

  /**
   * Creates a copy of the TimerEvent object and sets each property's value to match that of the original.
   * @return A new TimerEvent object with property values that match those of the original.
   *
   */
  "override public function clone",function clone()/*:Event*/ {
    return new flash.events.TimerEvent(this.type, this.bubbles, this.cancelable);
  },

  /**
   * Returns a string that contains all the properties of the TimerEvent object. The string is in the following format:
   * <p><code>[TimerEvent type=<i>value</i> bubbles=<i>value</i> cancelable=<i>value</i>]</code></p>
   * @return A string that contains all the properties of the TimerEvent object.
   *
   */
  "override public function toString",function toString()/*:String*/ {
    return this.formatToString("TimerEvent", "type", "bubbles", "cancelable");
  },

  /**
   * Instructs Flash Player or the AIR runtime to render after processing of this event completes, if the display list has been modified.
   * @example The following is an example for the <code>TimerEvent.updateAfterEvent()</code> method.
   * <listing>
   * function onTimer(event:TimerEvent):void {
   *     if (40 < my_mc.x && my_mc.x < 375) {
   *         my_mc.x-= 50;
   *     } else {
   *         my_mc.x=374;
   *     }
   *     event.updateAfterEvent();
   * }
   *
   * var moveTimer:Timer=new Timer(50,250);
   * moveTimer.addEventListener(TimerEvent.TIMER,onTimer);
   * moveTimer.start();
   * </listing>
   */
  "public function updateAfterEvent",function updateAfterEvent()/*:void*/ {
    // TODO
  },

  /**
   * Defines the value of the <code>type</code> property of a <code>timer</code> event object.
   * <p>This event has the following properties:</p>
   * <table>
   * <tr><th>Property</th><th>Value</th></tr>
   * <tr>
   * <td><code>bubbles</code></td>
   * <td><code>false</code></td></tr>
   * <tr>
   * <td><code>cancelable</code></td>
   * <td><code>false</code>; there is no default behavior to cancel.</td></tr>
   * <tr>
   * <td><code>currentTarget</code></td>
   * <td>The object that is actively processing the Event object with an event listener.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The Timer object that has reached its interval.</td></tr></table>
   * @see flash.utils.Timer#event:timer
   *
   */
  "public static const",{ TIMER/*:String*/ : "timer"},
  /**
   * Defines the value of the <code>type</code> property of a <code>timerComplete</code> event object.
   * <p>This event has the following properties:</p>
   * <table>
   * <tr><th>Property</th><th>Value</th></tr>
   * <tr>
   * <td><code>bubbles</code></td>
   * <td><code>false</code></td></tr>
   * <tr>
   * <td><code>cancelable</code></td>
   * <td><code>false</code>; there is no default behavior to cancel.</td></tr>
   * <tr>
   * <td><code>currentTarget</code></td>
   * <td>The object that is actively processing the Event object with an event listener.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The Timer object that has completed its requests.</td></tr></table>
   * @see flash.utils.Timer#event:timerComplete
   *
   */
  "public static const",{ TIMER_COMPLETE/*:String*/ : "timerComplete"},
];},[],["flash.events.Event"], "0.8.0", "0.8.3"
);