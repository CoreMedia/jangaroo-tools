joo.classLoader.prepare("package flash.media",/* {
import flash.utils.ByteArray*/

/**
 * The SoundMixer class contains static properties and methods for global sound control in the application. The SoundMixer class controls embedded and streaming sounds in the application. it does not control dynamically created sounds (that is, sounds generated in response to a Sound object dispatching a <code>sampleData</code> event).
 */
"public final class SoundMixer",1,function($$private){;return[ 
  /**
   * The number of seconds to preload an embedded streaming sound into a buffer before it starts to stream. The data in a loaded sound, including its buffer time, cannot be accessed by a SWF file that is in a different domain unless you implement a cross-domain policy file. For more information about security and sound, see the Sound class description. The data in a loaded sound, including its buffer time, cannot be accessed by code in a file that is in a different domain unless you implement a cross-domain policy file. However, in the application sandbox in an AIR application, code can access data in sound files from any source. For more information about security and sound, see the Sound class description.
   * <p>The <code>SoundMixer.bufferTime</code> property only affects the buffer time for embedded streaming sounds in a SWF and is independent of dynamically created Sound objects (that is, Sound objects created in ActionScript). The value of <code>SoundMixer.bufferTime</code> cannot override or set the default of the buffer time specified in the SoundLoaderContext object that is passed to the <code>Sound.load()</code> method.</p>
   * @see Sound
   *
   */
  "public static function get bufferTime",function bufferTime$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public static function set bufferTime",function bufferTime$set(value/*:int*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The SoundTransform object that controls global sound properties. A SoundTransform object includes properties for setting volume, panning, left speaker assignment, and right speaker assignment. The SoundTransform object used in this property provides final sound settings that are applied to all sounds after any individual sound settings are applied.
   * @see SoundTransform
   *
   */
  "public static function get soundTransform",function soundTransform$get()/*:SoundTransform*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public static function set soundTransform",function soundTransform$set(value/*:SoundTransform*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Determines whether any sounds are not accessible due to security restrictions. For example, a sound loaded from a domain other than that of the content calling this method is not accessible if the server for the sound has no URL policy file that grants access to the domain of that domain. The sound can still be loaded and played, but low-level operations, such as getting ID3 metadata for the sound, cannot be performed on inaccessible sounds.
   * <p>For AIR application content in the application security sandbox, calling this method always returns <code>false</code>. All sounds, including those loaded from other domains, are accessible to content in the application security sandbox.</p>
   * @return The string representation of the boolean.
   *
   * @see #computeSpectrum()
   *
   */
  "public static function areSoundsInaccessible",function areSoundsInaccessible()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Takes a snapshot of the current sound wave and places it into the specified ByteArray object. The values are formatted as normalized floating-point values, in the range -1.0 to 1.0. The ByteArray object passed to the <code>outputArray</code> parameter is overwritten with the new values. The size of the ByteArray object created is fixed to 512 floating-point values, where the first 256 values represent the left channel, and the second 256 values represent the right channel.
   * <p><b>Note:</b> This method is subject to local file security restrictions and restrictions on cross-domain loading. If you are working with local files or sounds loaded from a server in a different domain than the calling content, you might need to address sandbox restrictions through a cross-domain policy file. For more information, see the Sound class description. In addition, this method cannot be used to extract data from RTMP streams, even when it is called by content that reside in the same domain as the RTMP server.</p>
   * <p>This method is supported over RTMP in Flash Player 9.0.115.0 and later and in Adobe AIR. You can control access to streams on Flash Media Server in a server-side script. For more information, see the <code>Client.audioSampleAccess</code> and <code>Client.videoSampleAccess</code> properties in <a href="http://www.adobe.com/go/documentation"><i>Server-Side ActionScript Language Reference for Adobe Flash Media Server</i></a>.</p>
   * @param outputArray A ByteArray object that holds the values associated with the sound. If any sounds are not available due to security restrictions (<code>areSoundsInaccessible == true</code>), the <code>outputArray</code> object is left unchanged. If all sounds are stopped, the <code>outputArray</code> object is filled with zeros.
   * @param FFTMode A Boolean value indicating whether a Fourier transformation is performed on the sound data first. Setting this parameter to <code>true</code> causes the method to return a frequency spectrum instead of the raw sound wave. In the frequency spectrum, low frequencies are represented on the left and high frequencies are on the right.
   * @param stretchFactor The resolution of the sound samples. If you set the <code>stretchFactor</code> value to 0, data is sampled at 44.1 KHz; with a value of 1, data is sampled at 22.05 KHz; with a value of 2, data is sampled 11.025 KHz; and so on.
   *
   * @see #areSoundsInaccessible()
   * @see flash.utils.ByteArray
   * @see Sound
   * @see SoundLoaderContext#checkPolicyFile
   *
   * @example In the following example, the <code>computeSpectrum()</code> method is used to produce a graphic representation of the sound wave data.
   * <p>In the constructor, a sound file is loaded and set to play. (There is no error handling in this example and it is assumed that the sound file is in the same directory as the SWF file.) The example listens for the <code>Event.ENTER_FRAME</code> event while the sound plays, repeatedly triggering the <code>onEnterFrame()</code> method to draw a graph of the sound data values. When the sound finishes playing the <code>onPlaybackComplete()</code> method stops the drawing process by removing the listener for the <code>Event.ENTER_FRAME</code> event.</p>
   * <p>In the <code>onEnterFrame()</code> method, the <code>computeSpectrum()</code> method stores the raw sound in the <code>bytes</code> byte array object. The data is sampled at 44.1 KHz. The byte array containing 512 bytes of data, each of which contains a floating-point value between -1 and 1. The first 256 values represent the left channel, and the second 256 values represent the right channel. The first for loop, reads the first 256 data values (the left stereo channel) and draws a line from each point to the next using the <code>Graphics.lineTo()</code> method. (The vector graphic display of the sound wave is written directly on to the class's sprite object.) The sound bytes are read as 32-bit floating-point number from the byte stream and multiplied by the plot height to allow for the vertical range of the graph. The width is set to twice the width of the channel length. The second for loop reads the next set of 256 values (the right stereo channel), and plots the lines in reverse order. The <code>g.lineTo(CHANNEL_LENGTH * 2, PLOT_HEIGHT);</code> and <code>g.lineTo(0, PLOT_HEIGHT);</code> methods draw the baseline for the waves. The resulting waveform plot produces a mirror-image effect.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.display.Graphics;
   *     import flash.events.Event;
   *     import flash.media.Sound;
   *     import flash.media.SoundChannel;
   *     import flash.media.SoundMixer;
   *     import flash.net.URLRequest;
   *     import flash.utils.ByteArray;
   *     import flash.text.TextField;
   *
   *     public class SoundMixer_computeSpectrumExample extends Sprite {
   *
   *         public function SoundMixer_computeSpectrumExample() {
   *             var snd:Sound = new Sound();
   *             var req:URLRequest = new URLRequest("Song1.mp3");
   *             snd.load(req);
   *
   *             var channel:SoundChannel;
   *             channel = snd.play();
   *             addEventListener(Event.ENTER_FRAME, onEnterFrame);
   *             channel.addEventListener(Event.SOUND_COMPLETE, onPlaybackComplete);
   *         }
   *
   *         private function onEnterFrame(event:Event):void {
   *             var bytes:ByteArray = new ByteArray();
   *             const PLOT_HEIGHT:int = 200;
   *             const CHANNEL_LENGTH:int = 256;
   *
   *             SoundMixer.computeSpectrum(bytes, false, 0);
   *
   *             var g:Graphics = this.graphics;
   *
   *             g.clear();
   *
   *             g.lineStyle(0, 0x6600CC);
   *             g.beginFill(0x6600CC);
   *             g.moveTo(0, PLOT_HEIGHT);
   *
   *             var n:Number = 0;
   *
   *             for (var i:int = 0; i < CHANNEL_LENGTH; i++) {
   *                 n = (bytes.readFloat() * PLOT_HEIGHT);
   *                 g.lineTo(i * 2, PLOT_HEIGHT - n);
   *             }
   *
   *             g.lineTo(CHANNEL_LENGTH * 2, PLOT_HEIGHT);
   *             g.endFill();
   *
   *             g.lineStyle(0, 0xCC0066);
   *             g.beginFill(0xCC0066, 0.5);
   *             g.moveTo(CHANNEL_LENGTH * 2, PLOT_HEIGHT);
   *
   *             for (i = CHANNEL_LENGTH; i > 0; i--) {
   *                 n = (bytes.readFloat() * PLOT_HEIGHT);
   *                 g.lineTo(i * 2, PLOT_HEIGHT - n);
   *             }
   *
   *             g.lineTo(0, PLOT_HEIGHT);
   *             g.endFill();
   *         }
   *
   *         private function onPlaybackComplete(event:Event):void {
   *             removeEventListener(Event.ENTER_FRAME, onEnterFrame);
   *         }
   *     }
   * }
   *
   * </listing>
   */
  "public static function computeSpectrum",function computeSpectrum(outputArray/*:ByteArray*/, FFTMode/*:Boolean = false*/, stretchFactor/*:int = 0*/)/*:void*/ {switch(arguments.length){case 0:case 1:FFTMode = false;case 2:stretchFactor = 0;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Stops all sounds currently playing.
   * <p>>In Flash Professional, this method does not stop the playhead. Sounds set to stream will resume playing as the playhead moves over the frames in which they are located.</p>
   * <p>When using this property, consider the following security model:</p>
   * <ul>
   * <li>By default, calling the <code>SoundMixer.stopAll()</code> method stops only sounds in the same security sandbox as the object that is calling the method. Any sounds whose playback was not started from the same sandbox as the calling object are not stopped.</li>
   * <li>When you load the sound, using the <code>load()</code> method of the Sound class, you can specify a <code>context</code> parameter, which is a SoundLoaderContext object. If you set the <code>checkPolicyFile</code> property of the SoundLoaderContext object to <code>true</code>, Flash Player or Adobe AIR checks for a cross-domain policy file on the server from which the sound is loaded. If the server has a cross-domain policy file, and the file permits the domain of the calling content, then the file can stop the loaded sound by using the <code>SoundMixer.stopAll()</code> method; otherwise it cannot.</li></ul>
   * <p>However, in Adobe AIR, content in the <code>application</code> security sandbox (content installed with the AIR application) are not restricted by these security limitations.</p>
   * <p>For more information related to security, see the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</p>
   * @example In the following example, the <code>stopAll()</code> method is used to mute two sounds that are playing at the same time.
   * <p>In the constructor, two different sound files are loaded and set to play. The first sound is loaded locally and is assigned to a sound channel. (It is assumed that the file is in the same directory as the SWF file.) The second file is loaded and streamed from the Adobe site. In order to use the <code>SoundMixer.stopAll()</code> method, all sound must be accessible. (A SoundLoaderContext object can be used to check for the cross-domain policy file.) Each sound also has an event listener that is invoked if an IO error occurred while loading the sound file. A <code>muteButton</code> text field is also created. It listens for a click event, which will invoke the <code>muteButtonClickHandler()</code> method.</p>
   * <p>In the <code>muteButtonClickHandler()</code> method, if the text field content is "MUTE," the <code>areSoundsInaccessible()</code> method checks if the sound mixer has access to the files. If the files are accessible, the <code>stopAll()</code> method stops the sounds. By selecting the text field again, the first sound begins playing and the text field's content changes to "MUTE" again. This time, the <code>stopAll()</code> method mutes the one sound that is running. Note that sound channel <code>stop()</code> method can also be used to stop a specific sound assigned to the channel. (To use the channel functionally, the sound needs to be reassigned to the channel each time the <code>play()</code> method is invoked.)</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.net.URLRequest;
   *     import flash.media.Sound;
   *     import flash.media.SoundLoaderContext;
   *     import flash.media.SoundChannel;
   *     import flash.media.SoundMixer;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldAutoSize;
   *     import flash.events.MouseEvent;
   *     import flash.events.IOErrorEvent;
   *
   *     public class SoundMixer_stopAllExample extends Sprite  {
   *         private var firstSound:Sound = new Sound();
   *         private var secondSound:Sound = new Sound();
   *         private var muteButton:TextField = new TextField();
   *         private var channel1:SoundChannel = new SoundChannel();
   *
   *         public function SoundMixer_stopAllExample() {
   *             firstSound.load(new URLRequest("mySound.mp3"));
   *             secondSound.load(new URLRequest("http://av.adobe.com/podcast/csbu_dev_podcast_epi_2.mp3"));
   *
   *             firstSound.addEventListener(IOErrorEvent.IO_ERROR, firstSoundErrorHandler);
   *             secondSound.addEventListener(IOErrorEvent.IO_ERROR, secondSoundErrorHandler);
   *
   *             channel1 = firstSound.play();
   *             secondSound.play();
   *
   *             muteButton.autoSize = TextFieldAutoSize.LEFT;
   *             muteButton.border = true;
   *             muteButton.background = true;
   *             muteButton.text = "MUTE";
   *
   *             muteButton.addEventListener(MouseEvent.CLICK, muteButtonClickHandler);
   *
   *             this.addChild(muteButton);
   *         }
   *
   *         private function muteButtonClickHandler(event:MouseEvent):void {
   *
   *             if(muteButton.text == "MUTE") {
   *
   *                 if(SoundMixer.areSoundsInaccessible() == false) {
   *                     SoundMixer.stopAll();
   *                     muteButton.text = "click to play only one of sound.";
   *                 }
   *                 else {
   *                     muteButton.text = "The sounds are not accessible.";
   *                 }
   *             }
   *            else {
   *                 firstSound.play();
   *                 muteButton.text = "MUTE";
   *            }
   *         }
   *
   *         private function firstSoundErrorHandler(errorEvent:IOErrorEvent):void {
   *             trace(errorEvent.text);
   *         }
   *
   *         private function secondSoundErrorHandler(errorEvent:IOErrorEvent):void {
   *             trace(errorEvent.text);
   *         }
   *     }
   * }
   * </listing>
   */
  "public static function stopAll",function stopAll()/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},["bufferTime","soundTransform","areSoundsInaccessible","computeSpectrum","stopAll"],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);