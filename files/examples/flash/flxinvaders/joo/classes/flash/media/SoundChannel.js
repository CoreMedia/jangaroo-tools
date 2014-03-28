joo.classLoader.prepare("package flash.media",/* {
import flash.events.EventDispatcher*/

/**
 * Dispatched when a sound has finished playing.
 * @eventType flash.events.Event.SOUND_COMPLETE
 */
{Event:{name:"soundComplete", type:"flash.events.Event"}},

/**
 * The SoundChannel class controls a sound in an application. Every sound is assigned to a sound channel, and the application can have multiple sound channels that are mixed together. The SoundChannel class contains a <code>stop()</code> method, properties for monitoring the amplitude (volume) of the channel, and a property for assigning a SoundTransform object to the channel.
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/media/SoundChannel.html#includeExamplesSummary">View the examples</a></p>
 * @see Sound
 * @see SoundTransform
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7d21.html Playing sounds
 *
 */
"public final class SoundChannel extends flash.events.EventDispatcher",2,function($$private){;return[ 
  /**
   * The current amplitude (volume) of the left channel, from 0 (silent) to 1 (full amplitude).
   */
  "public function get leftPeak",function leftPeak$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * When the sound is playing, the <code>position</code> property indicates in milliseconds the current point that is being played in the sound file. When the sound is stopped or paused, the <code>position</code> property indicates the last point that was played in the sound file.
   * <p>A common use case is to save the value of the <code>position</code> property when the sound is stopped. You can resume the sound later by restarting it from that saved position.</p>
   * <p>If the sound is looped, <code>position</code> is reset to 0 at the beginning of each loop.</p>
   */
  "public function get position",function position$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The current amplitude (volume) of the right channel, from 0 (silent) to 1 (full amplitude).
   */
  "public function get rightPeak",function rightPeak$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The SoundTransform object assigned to the sound channel. A SoundTransform object includes properties for setting volume, panning, left speaker assignment, and right speaker assignment.
   * @see SoundTransform
   *
   */
  "public function get soundTransform",function soundTransform$get()/*:SoundTransform*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set soundTransform",function soundTransform$set(value/*:SoundTransform*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Stops the sound playing in the channel.
   * @example In the following example, the user can pause and replay a sound file.
   * <p>In the constructor, the sound file is loaded. (This example assumes that the file is in the same directory as the SWF file.) A text field is used as a button for the user to play or pause the sound. When the user selects the <code>button</code> text field, the <code>clickHandler()</code> method is invoked.</p>
   * <p>In the <code>clickHandler()</code> method, the first time the user selects the text field, the sound is set to play and is assigned to a sound channel. Next, when the user selects the text field to pause, the sound stops playing. The sound channel's <code>position</code> property records the position of the sound at the time it was stopped. This property is used to resume the sound starting at that position, after the user selects the text field to start playing again. Each time the <code>Sound.play()</code> method is called, a new SoundChannel object is created and assigned to the <code>channel</code> variable. The Sound object must be assigned to a SoundChannel object in order to use the sound channel's <code>stop()</code> method to pause the sound.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.media.Sound;
   *     import flash.media.SoundChannel;
   *     import flash.net.URLLoader;
   *     import flash.net.URLRequest;
   *     import flash.text.TextField;
   *     import flash.events.MouseEvent;
   *     import flash.text.TextFieldAutoSize;
   *
   *     public class SoundChannel_stopExample extends Sprite {
   *         private var snd:Sound = new Sound();
   *         private var channel:SoundChannel = new SoundChannel();
   *         private var button:TextField = new TextField();
   *
   *         public function SoundChannel_stopExample() {
   *             var req:URLRequest = new URLRequest("MySound.mp3");
   *             snd.load(req);
   *
   *             button.x = 10;
   *             button.y = 10;
   *             button.text = "PLAY";
   *             button.border = true;
   *             button.background = true;
   *             button.selectable = false;
   *             button.autoSize = TextFieldAutoSize.CENTER;
   *
   *             button.addEventListener(MouseEvent.CLICK, clickHandler);
   *
   *             this.addChild(button);
   *         }
   *
   *         private function clickHandler(e:MouseEvent):void {
   *             var pausePosition:int = channel.position;
   *
   *             if(button.text == "PLAY") {
   *                 channel = snd.play(pausePosition);
   *                 button.text = "PAUSE";
   *             }
   *             else {
   *                 channel.stop();
   *                 button.text = "PLAY";
   *             }
   *         }
   *     }
   * }
   * </listing>
   */
  "public function stop",function stop()/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

];},[],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.1"
);