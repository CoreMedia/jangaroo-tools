joo.classLoader.prepare("package flash.media",/* {*/


/**
 * The SoundTransform class contains properties for volume and panning.
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/media/SoundTransform.html#includeExamplesSummary">View the examples</a></p>
 * @see flash.display.SimpleButton#soundTransform
 * @see flash.display.Sprite#soundTransform
 * @see Microphone#soundTransform
 * @see SoundChannel#soundTransform
 * @see SoundMixer#soundTransform
 * @see flash.net.NetStream#soundTransform
 *
 */
"public final class SoundTransform",1,function($$private){;return[ 
  /**
   * A value, from 0 (none) to 1 (all), specifying how much of the left input is played in the left speaker.
   */
  "public function get leftToLeft",function leftToLeft$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set leftToLeft",function leftToLeft$set(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * A value, from 0 (none) to 1 (all), specifying how much of the left input is played in the right speaker.
   */
  "public function get leftToRight",function leftToRight$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set leftToRight",function leftToRight$set(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The left-to-right panning of the sound, ranging from -1 (full pan left) to 1 (full pan right). A value of 0 represents no panning (balanced center between right and left).
   */
  "public function get pan",function pan$get()/*:Number*/ {
    return this._pan$1;
  },

  /**
   * @private
   */
  "public function set pan",function pan$set(value/*:Number*/)/*:void*/ {
    this._pan$1 = value;
    // TODO: can HTML5 Audio do this?
  },

  /**
   * A value, from 0 (none) to 1 (all), specifying how much of the right input is played in the left speaker.
   */
  "public function get rightToLeft",function rightToLeft$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set rightToLeft",function rightToLeft$set(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * A value, from 0 (none) to 1 (all), specifying how much of the right input is played in the right speaker.
   */
  "public function get rightToRight",function rightToRight$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set rightToRight",function rightToRight$set(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The volume, ranging from 0 (silent) to 1 (full volume).
   */
  "public function get volume",function volume$get()/*:Number*/ {
    return this._volume$1;
  },

  /**
   * @private
   */
  "public function set volume",function volume$set(value/*:Number*/)/*:void*/ {
    this._volume$1 = value;
    // TODO: can HTML5 Audio do this?
  },

  /**
   * Creates a SoundTransform object.
   * @param vol The volume, ranging from 0 (silent) to 1 (full volume).
   * @param panning The left-to-right panning of the sound, ranging from -1 (full pan left) to 1 (full pan right). A value of 0 represents no panning (center).
   *
   * @example In the following example, the sound plays only from the right channel, and the volume is set to 50 percent.
   * <p>In the constructor, the sound is loaded and is assigned to a sound channel (<code>channel</code>). A SoundTranform object (<code>transform</code>) is also created. Its first argument sets the volume at 50 percent (the range is 0.0 to 1.0). Its second argument sets the panning. In this example, panning is set to 1.0, which means the sound comes from the right speaker only. In order for these settings to take effect, the <code>transform</code> SoundTranform object is assigned to the sound channel's <code>souundTransform</code> property.</p>
   * <p><b>Note:</b> There is limited error handling written for this example.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.net.URLRequest;
   *     import flash.media.Sound;
   *     import flash.media.SoundChannel;
   *     import flash.media.SoundTransform;
   *     import flash.events.IOErrorEvent;
   *
   *     public class SoundTransform_constructorExample extends Sprite
   *     {
   *         public function SoundTransform_constructorExample() {
   *             var mySound:Sound = new Sound();
   *             var url:URLRequest = new URLRequest("mySound.mp3");
   *             var channel:SoundChannel;
   *             var transform:SoundTransform = new SoundTransform(0.5, 1.0);
   *
   *             mySound.load(url);
   *             channel = mySound.play();
   *             channel.soundTransform = transform;
   *
   *             mySound.addEventListener(IOErrorEvent.IO_ERROR, errorHandler);
   *         }
   *
   *         private function errorHandler(errorEvent:IOErrorEvent):void {
   *             trace("The sound could not be loaded: " + errorEvent.text);
   *         }
   *     }
   * }
   * </listing>
   */
  "public function SoundTransform",function SoundTransform$(vol/*:Number = 1*/, panning/*:Number = 0*/) {if(arguments.length<2){if(arguments.length<1){vol = 1;}panning = 0;}
    this.volume = vol;
    this.pan = panning;
  },

  "private var",{ _volume/*:Number*/:NaN},
  "private var",{ _pan/*:Number*/:NaN},
];},[],["Error"], "0.8.0", "0.8.3"
);