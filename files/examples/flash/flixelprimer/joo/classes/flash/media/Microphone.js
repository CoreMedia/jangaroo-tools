joo.classLoader.prepare("package flash.media",/* {
import flash.events.EventDispatcher*/

/**
 * property ActivityEvent.type =
 * @eventType flash.events.ActivityEvent.ACTIVITY
 */
{Event:{name:"activity", type:"flash.events.ActivityEvent"}},
/**
 * property StatusEvent.type =
 * @eventType flash.events.StatusEvent.STATUS
 */
{Event:{name:"status", type:"flash.events.StatusEvent"}},

/**
 * Use the Microphone class to monitor or capture audio from a microphone.
 * <p>To get a reference to a Microphone instance, use the <code>Microphone.getMicrophone()</code> method.</p>
 * <p>You can send microphone audio to a Flash Media Server. You can send the audio stream directly to the device audio output for local playback. You can also capture the audio stream for local recording or processing.</p>
 * <p><b>Sending microphone audio to Flash Media Server</b></p>
 * <p>Use the NetConnection and NetStream classes to transmit the audio stream to Flash Media Server. Flash Media Server can send the audio to other servers and broadcast it to other clients running Flash Player or Adobe AIR.</p>
 * <p><b>Playing microphone audio</b></p>
 * <p>Call the Microphone <code>setLoopback()</code> method to route the microphone audio directly to the computer or device audio output. Uncontrolled audio feedback is an inherent danger and is likely to occur whenever the audio output can be picked up by the microphone input. The <code>setUseEchoSuppression()</code> method can reduce, but not eliminate, the risk of feedback amplification.</p>
 * <p><b>Capturing microphone audio</b></p>
 * <p>To capture microphone audio, listen for the <code>sampleData</code> events dispatched by a Microphone instance. The SampleDataEvent object dispatched for this event contains the audio data.</p>
 * <p>For information about capturing video, see the Camera class.</p>
 * <p><b>Runtime microphone support</b></p>
 * <p>The Microphone class is not supported in Flash Player running in a mobile browser.</p>
 * <p><i>AIR profile support:</i> The Microphone class is supported on desktop operating systems, but it is not supported on all mobile devices. It is not supported on AIR for TV devices. See <a href="http://help.adobe.com/en_US/air/build/WS144092a96ffef7cc16ddeea2126bb46b82f-8000.html">AIR Profile Support</a> for more information regarding API support across multiple profiles.</p>
 * <p>You can test for support at run time using the <code>Microphone.isSupported</code> property. Note that for AIR for TV devices, <code>Microphone.isSupported</code> is <code>true</code> but <code>Microphone.getMicrophone()</code> always returns <code>null</code>.</p>
 * <p><b>Privacy controls</b></p>
 * <p>Flash Player displays a Privacy dialog box that lets the user choose whether to allow or deny access to the microphone. Your application window size must be at least 215 x 138 pixels, the minimum size required to display the dialog box, or access is denied automatically.</p>
 * <p>Content running in the AIR application sandbox does not need permission to access the microphone and no dialog is displayed. AIR content running outside the application sandbox does require permission and the Privacy dialog is displayed.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/media/Microphone.html#includeExamplesSummary">View the examples</a></p>
 * @see Camera
 * @see http://coenraets.org/blog/air-for-android-samples/voice-notes-for-android/ Cristophe Coenraets: Voice Notes for Android
 * @see http://www.riagora.com/2010/08/air-android-and-the-microphone/ Michael Chaize: AIR, Android, and the Microphone
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7d27.html Basics of working with sound
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7d1d.html Capturing sound input
 *
 */
"public final class Microphone extends flash.events.EventDispatcher",2,function($$private){;return[ 
  /**
   * The amount of sound the microphone is detecting. Values range from 0 (no sound is detected) to 100 (very loud sound is detected). The value of this property can help you determine a good value to pass to the <code>Microphone.setSilenceLevel()</code> method.
   * <p>If the microphone is available but is not yet being used because <code>Microphone.getMicrophone()</code> has not been called, this property is set to -1.</p>
   * @see #getMicrophone()
   * @see #setSilenceLevel()
   * @see #gain
   *
   */
  "public function get activityLevel",function activityLevel$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The amount by which the microphone boosts the signal. Valid values are 0 to 100. The default value is 50.
   * @see #gain
   *
   */
  "public function get gain",function gain$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set gain",function gain$set(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The index of the microphone, as reflected in the array returned by <code>Microphone.names</code>.
   * @see #getMicrophone()
   * @see #names
   *
   */
  "public function get index",function index$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies whether the user has denied access to the microphone (<code>true</code>) or allowed access (<code>false</code>). When this value changes, a <code>status</code> event is dispatched. For more information, see <code>Microphone.getMicrophone()</code>.
   * @see #getMicrophone()
   * @see #event:status
   *
   */
  "public function get muted",function muted$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The name of the current sound capture device, as returned by the sound capture hardware.
   * @see #getMicrophone()
   * @see #names
   *
   */
  "public function get name",function name$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * An array of strings containing the names of all available sound capture devices. The names are returned without having to display the Flash Player Privacy Settings panel to the user. This array provides the zero-based index of each sound capture device and the number of sound capture devices on the system, through the <code>Microphone.names.length</code> property. For more information, see the Array class entry.
   * <p>Calling <code>Microphone.names</code> requires an extensive examination of the hardware, and it may take several seconds to build the array. In most cases, you can just use the default microphone.</p>
   * <p><b>Note:</b> To determine the name of the current microphone, use the <code>name</code> property.</p>
   * @see Array
   * @see #name
   * @see #getMicrophone()
   *
   */
  "public static function get names",function names$get()/*:Array*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The rate at which the microphone is capturing sound, in kHz. Acceptable values are 5, 8, 11, 22, and 44. The default value is 8 kHz if your sound capture device supports this value. Otherwise, the default value is the next available capture level above 8 kHz that your sound capture device supports, usually 11 kHz.
   * <p><b>Note:</b> The actual rate differs slightly from the <code>rate</code> value, as noted in the following table:</p>
   * <table>
   * <tr><th><code>rate</code> value</th><th>Actual frequency</th></tr>
   * <tr>
   * <td>44</td>
   * <td>44,100 Hz</td></tr>
   * <tr>
   * <td>22</td>
   * <td>22,050 Hz</td></tr>
   * <tr>
   * <td>11</td>
   * <td>11,025 Hz</td></tr>
   * <tr>
   * <td>8</td>
   * <td>8,000 Hz</td></tr>
   * <tr>
   * <td>5</td>
   * <td>5,512 Hz</td></tr></table>
   * @see #rate
   *
   */
  "public function get rate",function rate$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set rate",function rate$set(value/*:int*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The amount of sound required to activate the microphone and dispatch the <code>activity</code> event. The default value is 10.
   * @see #gain
   * @see #setSilenceLevel()
   *
   */
  "public function get silenceLevel",function silenceLevel$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The number of milliseconds between the time the microphone stops detecting sound and the time the <code>activity</code> event is dispatched. The default value is 2000 (2 seconds).
   * <p>To set this value, use the <code>Microphone.setSilenceLevel()</code> method.</p>
   * @see #setSilenceLevel()
   *
   */
  "public function get silenceTimeout",function silenceTimeout$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Controls the sound of this microphone object when it is in loopback mode.
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
   * Set to <code>true</code> if echo suppression is enabled; <code>false</code> otherwise. The default value is <code>false</code> unless the user has selected Reduce Echo in the Flash Player Microphone Settings panel.
   * @see #setUseEchoSuppression()
   *
   */
  "public function get useEchoSuppression",function useEchoSuppression$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns a reference to a Microphone object for capturing audio. To begin capturing the audio, you must attach the Microphone object to a NetStream object (see <code>NetStream.attachAudio()</code>).
   * <p>Multiple calls to <code>Microphone.getMicrophone()</code> reference the same microphone. Thus, if your code contains the lines <code>mic1 = Microphone.getMicrophone()</code> and <code>mic2 = Microphone.getMicrophone()</code> , both <code>mic1</code> and <code>mic2</code> reference the same (default) microphone.</p>
   * <p>If you want to get the default microphone, call <code>getMicrophone()</code> with -1 (the default value).</p>
   * <p>Use the <code>index</code> property to get the index value of the current Microphone object. You can then pass this value to other methods of the Microphone class.</p>
   * <p>In general, you should not pass a value for <code>index</code>. Simply call <code>air.Microphone.getMicrophone()</code> to return a reference to the default microphone. Using the Microphone Settings section in the Flash Player settings panel, the user can specify the default microphone the application should use. (The user access the Flash Player settings panel by right-clicking Flash Player content running in a web browser.) If you pass a value for <code>index</code>, you can reference a microphone other than the one the user chooses. You can use <code>index</code> in rare cases—for example, if your application is capturing audio from two microphones at the same time. Content running in Adobe AIR also uses the Flash Player setting for the default microphone.</p>
   * <p>When a SWF file tries to access the object returned by <code>Microphone.getMicrophone()</code> —for example, when you call <code>NetStream.attachAudio()</code>— Flash Player displays a Privacy dialog box that lets the user choose whether to allow or deny access to the microphone. (Make sure your Stage size is at least 215 x 138 pixels; this is the minimum size Flash Player requires to display the dialog box.)</p>
   * <p>When the user responds to this dialog box, a <code>status</code> event is dispatched that indicates the user's response. You can also check the <code>Microphone.muted</code> property to determine if the user has allowed or denied access to the microphone.</p>
   * <p>If <code>Microphone.getMicrophone()</code> returns <code>null</code>, either the microphone is in use by another application, or there are no microphones installed on the system. To determine whether any microphones are installed, use <code>Microphones.names.length</code>. To display the Flash Player Microphone Settings panel, which lets the user choose the microphone to be referenced by <code>Microphone.getMicrophone</code>, use <code>Security.showSettings()</code>.</p>
   * @param index The index value of the microphone.
   *
   * @return A reference to a Microphone object for capturing audio.
   * Events
   * <table>
   * <tr>
   * <td><code><b>status</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/StatusEvent.html"><code>StatusEvent</code></a></code> — Dispatched when a microphone reports its status. If the value of the <code>code</code> property is <code>"Microphone.Muted"</code>, the user has refused to allow the SWF file access to the user's microphone. If the value of the <code>code</code> property is <code>"Microphone.Unmuted"</code>, the user has allowed the SWF file access to the user's microphone.</td></tr></table>
   * @see #event:status
   * @see flash.net.NetStream#attachAudio()
   * @see flash.system.Security#showSettings()
   *
   * @example The following example shows how you can request access to the user's microphone using the static Microphone.getMicrophone() method and listening for the status event. Example provided by <a href="http://actionscriptexamples.com/2008/12/04/detecting-the-microphone-in-flash-using-actionscript-30/">ActionScriptExamples.com</a>.
   * <listing>
   * var mic:Microphone = Microphone.getMicrophone();
   * mic.setLoopBack();
   * mic.addEventListener(StatusEvent.STATUS, mic_status);
   *
   * var tf:TextField = new TextField();
   * tf.autoSize = TextFieldAutoSize.LEFT;
   * tf.text = "Detecting microphone...";
   * addChild(tf);
   *
   * function mic_status(evt:StatusEvent):void {
   *     tf.text = "Microphone is muted?: " + mic.muted;
   *     switch (evt.code) {
   *         case "Microphone.Unmuted":
   *             tf.appendText("\n" + "Microphone access was allowed.");
   *             break;
   *         case "Microphone.Muted":
   *             tf.appendText("\n" + "Microphone access was denied.");
   *             break;
   *     }
   * }
   * </listing>
   */
  "public static function getMicrophone",function getMicrophone(index/*:int = -1*/)/*:Microphone*/ {if(arguments.length<1){index = -1;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Routes audio captured by a microphone to the local speakers.
   * @param state <code>state:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/Boolean.html">Boolean</a></code> (default = <code>true</code>)
   *
   */
  "public function setLoopBack",function setLoopBack(state/*:Boolean = true*/)/*:void*/ {if(arguments.length<1){state = true;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Sets the minimum input level that should be considered sound and (optionally) the amount of silent time signifying that silence has actually begun.
   * <ul>
   * <li>To prevent the microphone from detecting sound at all, pass a value of 100 for <code>silenceLevel</code>; the <code>activity</code> event is never dispatched.</li>
   * <li>To determine the amount of sound the microphone is currently detecting, use <code>Microphone.activityLevel</code>.</li></ul>
   * <p>Activity detection is the ability to detect when audio levels suggest that a person is talking. When someone is not talking, bandwidth can be saved because there is no need to send the associated audio stream. This information can also be used for visual feedback so that users know they (or others) are silent.</p>
   * <p>Silence values correspond directly to activity values. Complete silence is an activity value of 0. Constant loud noise (as loud as can be registered based on the current gain setting) is an activity value of 100. After gain is appropriately adjusted, your activity value is less than your silence value when you're not talking; when you are talking, the activity value exceeds your silence value.</p>
   * <p>This method is similar to <code>Camera.setMotionLevel()</code>; both methods are used to specify when the <code>activity</code> event is dispatched. However, these methods have a significantly different impact on publishing streams:</p>
   * <ul>
   * <li><code>Camera.setMotionLevel()</code> is designed to detect motion and does not affect bandwidth usage. Even if a video stream does not detect motion, video is still sent.</li>
   * <li><code>Microphone.setSilenceLevel()</code> is designed to optimize bandwidth. When an audio stream is considered silent, no audio data is sent. Instead, a single message is sent, indicating that silence has started.</li></ul>
   * @param silenceLevel The amount of sound required to activate the microphone and dispatch the <code>activity</code> event. Acceptable values range from 0 to 100.
   * @param timeout The number of milliseconds that must elapse without activity before Flash Player or Adobe AIR considers sound to have stopped and dispatches the <code>dispatch</code> event. The default value is 2000 (2 seconds). (<b>Note</b>: The default value shown in the signature, -1, is an internal value that indicates to Flash Player or Adobe AIR to use 2000.)
   *
   * @see Camera#setMotionLevel()
   * @see #activityLevel
   * @see #event:activity
   * @see #gain
   * @see #silenceLevel
   * @see #silenceTimeout
   *
   */
  "public function setSilenceLevel",function setSilenceLevel(silenceLevel/*:Number*/, timeout/*:int = -1*/)/*:void*/ {if(arguments.length<2){timeout = -1;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies whether to use the echo suppression feature of the audio codec. The default value is <code>false</code> unless the user has selected Reduce Echo in the Flash Player Microphone Settings panel.
   * <p>Echo suppression is an effort to reduce the effects of audio feedback, which is caused when sound going out the speaker is picked up by the microphone on the same system. (This is different from echo cancellation, which completely removes the feedback.)</p>
   * <p>Generally, echo suppression is advisable when the sound being captured is played through speakers — instead of a headset —. If your SWF file allows users to specify the sound output device, you may want to call <code>Microphone.setUseEchoSuppression(true)</code> if they indicate they are using speakers and will be using the microphone as well.</p>
   * <p>Users can also adjust these settings in the Flash Player Microphone Settings panel.</p>
   * @param useEchoSuppression A Boolean value indicating whether echo suppression should be used (<code>true</code>) or not (<code>false</code>).
   *
   * @see #setUseEchoSuppression()
   * @see #useEchoSuppression
   *
   */
  "public function setUseEchoSuppression",function setUseEchoSuppression(useEchoSuppression/*:Boolean*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},["names","getMicrophone"],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.3"
);