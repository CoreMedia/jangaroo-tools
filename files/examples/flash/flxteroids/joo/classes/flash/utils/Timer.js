joo.classLoader.prepare("package flash.utils",/* {

import flash.events.EventDispatcher
import flash.events.TimerEvent*/

/**
 * Dispatched whenever a Timer object reaches an interval specified according to the <code>Timer.delay</code> property.
 * @eventType flash.events.TimerEvent.TIMER
 */
{Event:{name:"timer", type:"flash.events.TimerEvent"}},
/**
 * Dispatched whenever it has completed the number of requests set by <code>Timer.repeatCount</code>.
 * @eventType flash.events.TimerEvent.TIMER_COMPLETE
 */
{Event:{name:"timerComplete", type:"flash.events.TimerEvent"}},

/**
 * The Timer class is the interface to timers, which let you run code on a specified time sequence. Use the <code>start()</code> method to start a timer. Add an event listener for the <code>timer</code> event to set up code to be run on the timer interval.
 * <p>You can create Timer objects to run once or repeat at specified intervals to execute code on a schedule. Depending on the SWF file's framerate or the runtime environment (available memory and other factors), the runtime may dispatch events at slightly offset intervals. For example, if a SWF file is set to play at 10 frames per second (fps), which is 100 millisecond intervals, but your timer is set to fire an event at 80 milliseconds, the event will be dispatched close to the 100 millisecond interval. Memory-intensive scripts may also offset the events.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/utils/Timer.html#includeExamplesSummary">View the examples</a></p>
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f0f.html Controlling time intervals
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f07.html The Timer class
 *
 */
"public class Timer extends flash.events.EventDispatcher",2,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.events.TimerEvent);}, 
  /**
   * The total number of times the timer has fired since it started at zero. If the timer has been reset, only the fires since the reset are counted.
   */
  "public function get currentCount",function currentCount$get()/*:int*/ {
    return this._currentCount$2;
  },

  /**
   * The delay, in milliseconds, between timer events. If you set the delay interval while the timer is running, the timer will restart at the same <code>repeatCount</code> iteration.
   * <p><b>Note:</b> A <code>delay</code> lower than 20 milliseconds is not recommended. Timer frequency is limited to 60 frames per second, meaning a delay lower than 16.6 milliseconds causes runtime problems.</p>
   * @throws Error Throws an exception if the delay specified is negative or not a finite number.
   *
   */
  "public function get delay",function delay$get()/*:Number*/ {
    return this._delay$2;
  },

  /**
   * @private
   */
  "public function set delay",function delay$set(value/*:Number*/)/*:void*/ {
    this._delay$2 = value;
    if (this.timer$2) {
      this.stop();
      this.start();
    }
  },

  /**
   * The total number of times the timer is set to run. If the repeat count is set to 0, the timer continues forever or until the <code>stop()</code> method is invoked or the program stops. If the repeat count is nonzero, the timer runs the specified number of times. If <code>repeatCount</code> is set to a total that is the same or less then <code>currentCount</code> the timer stops and will not fire again.
   */
  "public function get repeatCount",function repeatCount$get()/*:int*/ {
    return this._repeatCount$2;
  },

  /**
   * @private
   */
  "public function set repeatCount",function repeatCount$set(value/*:int*/)/*:void*/ {
    this._repeatCount$2 = value;
    this.checkComplete$2();
  },

  /**
   * The timer's current state; <code>true</code> if the timer is running, otherwise <code>false</code>.
   */
  "public function get running",function running$get()/*:Boolean*/ {
    return this.timer$2 != null;
  },

  /**
   * Constructs a new Timer object with the specified <code>delay</code> and <code>repeatCount</code> states.
   * <p>The timer does not start automatically; you must call the <code>start()</code> method to start it.</p>
   * @param delay The delay between timer events, in milliseconds. A <code>delay</code> lower than 20 milliseconds is not recommended. Timer frequency is limited to 60 frames per second, meaning a delay lower than 16.6 milliseconds causes runtime problems.
   * @param repeatCount Specifies the number of repetitions. If zero, the timer repeats infinitely. If nonzero, the timer runs the specified number of times and then stops.
   *
   * @throws Error if the delay specified is negative or not a finite number
   *
   * @example In the following example, the user is given 90 seconds to enter a response in an input text field. Also, every 30 seconds, a status message lets the user know how many seconds are left.
   * <p>A Timer object is created that starts in 30 seconds (delay is set to 30000 milliseconds) and repeats three times, for a total of 90 seconds. (The timer stops after the third time.)</p>
   * <p>Two event listeners are added for the <code>myTimer</code> timer. The first is triggered by the <code>TimerEvent.TIMER</code> event, which occurs every time the timer is started. The <code>timerHandler()</code> method changes the text for the <code>statusTextField</code> text field to reflect the seconds remaining.</p>
   * <p><b>Note:</b> The Timer class keeps track of the number of times it has to start (<code>repeats</code>) by increasing the number in the <code>currentCount</code> property.)</p>
   * <p>After the timer is called for the last time, the <code>TimerEvent.TIMER_COMPLETE</code> event is dispatched and the <code>completeHandler()</code> method is called. The <code>completeHandler()</code> method changes the type of the <code>inputTextField</code> text field from <code>INPUT</code> to <code>DYNAMIC</code>, which means the user can no longer enter or change text.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldType;
   *     import flash.text.TextFieldAutoSize;
   *     import flash.utils.Timer;
   *     import flash.events.TimerEvent;
   *     import flash.events.Event;
   *
   *     public class Timer_constructorExample extends Sprite {
   *             private var statusTextField:TextField = new TextField();
   *             private var inputTextField:TextField = new TextField();
   *             private var delay:uint = 30000;
   *             private var repeat:uint = 3;
   *             private var myTimer:Timer = new Timer(delay, repeat);
   *
   *         public function Timer_constructorExample() {
   *             inputTextField.x = 10;
   *             inputTextField.y = 10;
   *             inputTextField.border = true;
   *             inputTextField.background = true;
   *             inputTextField.height = 200;
   *             inputTextField.width = 200;
   *             inputTextField.multiline = true;
   *             inputTextField.wordWrap = true;
   *             inputTextField.type = TextFieldType.INPUT;
   *
   *             statusTextField.x = 10;
   *             statusTextField.y = 220;
   *             statusTextField.background = true;
   *             statusTextField.autoSize = TextFieldAutoSize.LEFT;
   *
   *             myTimer.start();
   *             statusTextField.text = "You have " + ((delay * repeat) / 1000)
   *                                  + " seconds to enter your response.";
   *
   *             myTimer.addEventListener(TimerEvent.TIMER, timerHandler);
   *             myTimer.addEventListener(TimerEvent.TIMER_COMPLETE, completeHandler);
   *
   *             addChild(inputTextField);
   *             addChild(statusTextField);
   *         }
   *
   *         private function timerHandler(e:TimerEvent):void{
   *             repeat--;
   *             statusTextField.text = ((delay * repeat) / 1000) + " seconds left.";
   *         }
   *
   *         private function completeHandler(e:TimerEvent):void {
   *             statusTextField.text = "Times Up.";
   *             inputTextField.type = TextFieldType.DYNAMIC;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function Timer",function Timer$(delay/*:Number*/, repeatCount/*:int = 0*/) {if(arguments.length<2){repeatCount = 0;}this.super$2();
    this._delay$2 = delay;
    this._repeatCount$2 = repeatCount;
  },

  /**
   * Stops the timer, if it is running, and sets the <code>currentCount</code> property back to 0, like the reset button of a stopwatch. Then, when <code>start()</code> is called, the timer instance runs for the specified number of repetitions, as set by the <code>repeatCount</code> value.
   * @see #stop()
   *
   */
  "public function reset",function reset()/*:void*/ {
    this.stop();
    this._currentCount$2 = 0;
  },

  /**
   * Starts the timer, if it is not already running.
   */
  "public function start",function start()/*:void*/ {
    if (!this.timer$2) {
      this.timer$2 = window.setInterval($$bound(this,"tick$2"), this._delay$2);
    }
  },

  /**
   * Stops the timer. When <code>start()</code> is called after <code>stop()</code>, the timer instance runs for the <i>remaining</i> number of repetitions, as set by the <code>repeatCount</code> property.
   * @see #reset()
   *
   */
  "public function stop",function stop()/*:void*/ {
    if (this.timer$2) {
      window.clearInterval(this.timer$2);
      this.timer$2 = null;
    }
  },

  "private function tick",function tick()/*:void*/ {
    if (!this.timer$2) {
      // oops, a tick occurred although timer has been stopped:
      return;
    }
    ++this._currentCount$2;
    try {
      this.dispatchEvent(new flash.events.TimerEvent(flash.events.TimerEvent.TIMER));
    } finally {
      this.checkComplete$2();
    }
  },

  "private function checkComplete",function checkComplete()/*:void*/ {
    if (this._repeatCount$2 > 0 && this._currentCount$2 >= this._repeatCount$2) {
      this.stop();
      this.dispatchEvent(new flash.events.TimerEvent(flash.events.TimerEvent.TIMER_COMPLETE));
    }
  },

  "private var",{ timer/*:Object*/ : null},
  "private var",{ _delay/*:Number*/:NaN},
  "private var",{ _repeatCount/*:int*/:0},
  "private var",{ _currentCount/*:int*/ : 0},

];},[],["flash.events.EventDispatcher","flash.events.TimerEvent"], "0.8.0", "0.8.1"
);