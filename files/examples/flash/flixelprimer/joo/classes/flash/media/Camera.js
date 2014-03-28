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
 * Use the Camera class to capture video from the client system's camera. Use the Video class to monitor the video locally. Use the NetConnection and NetStream classes to transmit the video to Flash Media Server. Flash Media Server can send the video stream to other servers and broadcast it to other clients running Flash Player.
 * <p>A Camera instance captures video in landscape aspect ratio. On devices that can change the screen orientation, such as mobile phones, a Video object attached to the camera will only show upright video in a landscape-aspect orientation. Thus, mobile apps should use a landscape orientation when displaying video and should not auto-rotate.</p>
 * <p>On Android, the camera does not capture video while an AIR app is not the active, foreground application.</p>
 * <p><b>Mobile Browser Support:</b> This class is not supported in mobile browsers.</p>
 * <p><i>AIR profile support:</i> This feature is supported on desktop operating systems, but it is not supported on all mobile devices. It is not supported on AIR for TV devices. See <a href="http://help.adobe.com/en_US/air/build/WS144092a96ffef7cc16ddeea2126bb46b82f-8000.html">AIR Profile Support</a> for more information regarding API support across multiple profiles.</p>
 * <p>You can test for support at run time using the <code>Camera.isSupported</code> property. Note that for AIR for TV devices, <code>Camera.isSupported</code> is <code>true</code> but <code>Camera.getCamera()</code> always returns <code>null</code>.</p>
 * <p>For information about capturing audio, see the Microphone class.</p>
 * <p><b>Important:</b> Flash Player displays a Privacy dialog box that lets the user choose whether to allow or deny access to the camera. Make sure your application window size is at least 215 x 138 pixels; this is the minimum size required to display the dialog box.</p>
 * <p>To create or reference a Camera object, use the <code>getCamera()</code> method.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/media/Camera.html#includeExamplesSummary">View the examples</a></p>
 * @see Microphone
 * @see http://coenraets.org/blog/2010/07/video-chat-for-android-in-30-lines-of-code/ Cristophe Coenraets: Video Chat for Android in 30 Lines of Code
 * @see http://www.riagora.com/2010/07/android-air-and-the-camera/ Michael Chaize: Android, AIR, and the Camera
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7d50.html Basics of video
 *
 */
"public final class Camera extends flash.events.EventDispatcher",2,function($$private){;return[ 
  /**
   * The amount of motion the camera is detecting. Values range from 0 (no motion is being detected) to 100 (a large amount of motion is being detected). The value of this property can help you determine if you need to pass a setting to the <code>setMotionLevel()</code> method.
   * <p>If the camera is available but is not yet being used because the <code>Video.attachCamera()</code> method has not been called, this property is set to -1.</p>
   * <p>If you are streaming only uncompressed local video, this property is set only if you have assigned a function to the event handler. Otherwise, it is undefined.</p>
   * @see #motionLevel
   * @see #setMotionLevel()
   *
   */
  "public function get activityLevel",function activityLevel$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The maximum amount of bandwidth the current outgoing video feed can use, in bytes. A value of 0 means the feed can use as much bandwidth as needed to maintain the desired frame quality.
   * <p>To set this property, use the <code>setQuality()</code> method.</p>
   * @see #setQuality()
   *
   */
  "public function get bandwidth",function bandwidth$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The rate at which the camera is capturing data, in frames per second. This property cannot be set; however, you can use the <code>setMode()</code> method to set a related property—<code>fps</code>—which specifies the maximum frame rate at which you would like the camera to capture data.
   * @see #setMode()
   *
   */
  "public function get currentFPS",function currentFPS$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The maximum rate at which the camera can capture data, in frames per second. The maximum rate possible depends on the capabilities of the camera; this frame rate may not be achieved.
   * <ul>
   * <li>To set a desired value for this property, use the <code>setMode()</code> method.</li>
   * <li>To determine the rate at which the camera is currently capturing data, use the <code>currentFPS</code> property.</li></ul>
   * @see #currentFPS
   * @see #setMode()
   *
   */
  "public function get fps",function fps$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The current capture height, in pixels. To set a value for this property, use the <code>setMode()</code> method.
   * @see #width
   * @see #setMode()
   *
   */
  "public function get height",function height$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * A zero-based integer that specifies the index of the camera, as reflected in the array returned by the <code>names</code> property.
   * @see #names
   * @see #getCamera()
   *
   */
  "public function get index",function index$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The number of video frames transmitted in full (called <i>keyframes</i>) instead of being interpolated by the video compression algorithm. The default value is 15, which means that every 15th frame is a keyframe. A value of 1 means that every frame is a keyframe. The allowed values are 1 through 48.
   * @see #setKeyFrameInterval()
   *
   */
  "public function get keyFrameInterval",function keyFrameInterval$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Indicates whether a local view of what the camera is capturing is compressed and decompressed (<code>true</code>), as it would be for live transmission using Flash Media Server, or uncompressed (<code>false</code>). The default value is <code>false</code>.
   * <p>Although a compressed stream is useful for testing, such as when previewing video quality settings, it has a significant processing cost. The local view is compressed, edited for transmission as it would be over a live connection, and then decompressed for local viewing.</p>
   * <p>To set this value, use <code>Camera.setLoopback()</code>. To set the amount of compression used when this property is true, use <code>Camera.setQuality()</code>.</p>
   * @see #setLoopback()
   * @see #setQuality()
   *
   */
  "public function get loopback",function loopback$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The amount of motion required to invoke the <code>activity</code> event. Acceptable values range from 0 to 100. The default value is 50.
   * <p>Video can be displayed regardless of the value of the <code>motionLevel</code> property. For more information, see <code>setMotionLevel()</code>.</p>
   * @see #setMotionLevel()
   *
   */
  "public function get motionLevel",function motionLevel$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The number of milliseconds between the time the camera stops detecting motion and the time the <code>activity</code> event is invoked. The default value is 2000 (2 seconds).
   * <p>To set this value, use <code>setMotionLevel()</code>.</p>
   * @see #setMotionLevel()
   *
   */
  "public function get motionTimeout",function motionTimeout$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * A Boolean value indicating whether the user has denied access to the camera (<code>true</code>) or allowed access (<code>false</code>) in the Flash Player Privacy dialog box. When this value changes, the <code>status</code>event is dispatched.
   * @see #getCamera()
   * @see #event:status
   *
   */
  "public function get muted",function muted$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The name of the current camera, as returned by the camera hardware.
   * @see #names
   * @see #getCamera()
   *
   */
  "public function get name",function name$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * An array of strings indicating the names of all available cameras without displaying the Flash Player Privacy dialog box. This array behaves in the same way as any other ActionScript array, implicitly providing the zero-based index of each camera and the number of cameras on the system (by means of <code>names.length</code>). For more information, see the <code>names</code> Array class entry.
   * <p>Calling the <code>names</code> property requires an extensive examination of the hardware. In most cases, you can just use the default camera.</p>
   * <p>On Android, only one camera is supported, even if the device has more than one camera devices. The name of the camera is always, "Default."</p>
   * @see #getCamera()
   * @see #index
   * @see #name
   *
   */
  "public static function get names",function names$get()/*:Array*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The required level of picture quality, as determined by the amount of compression being applied to each video frame. Acceptable quality values range from 1 (lowest quality, maximum compression) to 100 (highest quality, no compression). The default value is 0, which means that picture quality can vary as needed to avoid exceeding available bandwidth.
   * <p>To set this property, use the <code>setQuality()</code> method.</p>
   * @see #setQuality()
   *
   */
  "public function get quality",function quality$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The current capture width, in pixels. To set a desired value for this property, use the <code>setMode()</code> method.
   * @see #setMode()
   *
   */
  "public function get width",function width$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns a reference to a Camera object for capturing video. To begin capturing the video, you must attach the Camera object to a Video object (see <code>Video.attachCamera()</code> ). To transmit video to Flash Media Server, call <code>NetStream.attachCamera()</code> to attach the Camera object to a NetStream object.
   * <p>Multiple calls to the <code>getCamera()</code> method reference the same camera driver. Thus, if your code contains code like <code>firstCam:Camera = getCamera()</code> and <code>secondCam:Camera = getCamera()</code>, both <code>firstCam</code> and <code>secondCam</code> reference the same camera, which is the user's default camera.</p>
   * <p>In general, you shouldn't pass a value for the <code>name</code> parameter; simply use <code>getCamera()</code> to return a reference to the default camera. By means of the Camera settings panel (discussed later in this section), the user can specify the default camera to use.</p>
   * <p>You can't use ActionScript to set a user's Allow or Deny permission setting for access to the camera, but you can display the Adobe Flash Player Settings camera setting dialog box where the user can set the camera permission. When a SWF file using the <code>attachCamera()</code> method tries to attach the camera returned by the <code>getCamera()</code> method to a Video or NetStream object, Flash Player displays a dialog box that lets the user choose to allow or deny access to the camera. (Make sure your application window size is at least 215 x 138 pixels; this is the minimum size Flash Player requires to display the dialog box.) When the user responds to the camera setting dialog box, Flash Player returns an information object in the <code>status</code> event that indicates the user's response: <code>Camera.muted</code> indicates the user denied access to a camera; <code>Camera.Unmuted</code> indicates the user allowed access to a camera. To determine whether the user has denied or allowed access to the camera without handling the <code>status</code> event, use the <code>muted</code> property.</p>
   * <p>In Flash Player, the user can specify permanent privacy settings for a particular domain by right-clicking (Windows and Linux) or Control-clicking (Macintosh) while a SWF file is playing, selecting Settings, opening the Privacy dialog, and selecting Remember. If the user selects Remember, Flash Player no longer asks the user whether to allow or deny SWF files from this domain access to your camera.</p>
   * <p><b>Note:</b> The <code>attachCamera()</code> method will not invoke the dialog box to Allow or Deny access to the camera if the user has denied access by selecting Remember in the Flash Player Settings dialog box. In this case, you can prompt the user to change the Allow or Deny setting by displaying the Flash Player Privacy panel for the user using <code>Security.showSettings(SecurityPanel.PRIVACY)</code>.</p>
   * <p>If <code>getCamera()</code> returns <code>null</code>, either the camera is in use by another application, or there are no cameras installed on the system. To determine whether any cameras are installed, use the <code>names.length</code> property. To display the Flash Player Camera Settings panel, which lets the user choose the camera to be referenced by <code>getCamera()</code>, use <code>Security.showSettings(SecurityPanel.CAMERA)</code>.</p>
   * <p>Scanning the hardware for cameras takes time. When the runtime finds at least one camera, the hardware is not scanned again for the lifetime of the player instance. However, if the runtime doesn't find any cameras, it will scan each time <code>getCamera</code> is called. This is helpful if the camera is present but is disabled; if your SWF file provides a Try Again button that calls <code>getCamera</code>, Flash Player can find the camera without the user having to restart the SWF file.</p>
   * @param name Specifies which camera to get, as determined from the array returned by the <code>names</code> property. For most applications, get the default camera by omitting this parameter. To specify a value for this parameter, use the string representation of the zero-based index position within the Camera.names array. For example, to specify the third camera in the array, use <code>Camera.getCamera("2")</code>.
   *
   * @return If the <code>name</code> parameter is not specified, this method returns a reference to the default camera or, if it is in use by another application, to the first available camera. (If there is more than one camera installed, the user may specify the default camera in the Flash Player Camera Settings panel.) If no cameras are available or installed, the method returns <code>null</code>.
   * Events
   * <table>
   * <tr>
   * <td><code><b>status</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/StatusEvent.html"><code>StatusEvent</code></a></code> — Dispatched when a camera reports its status. Before accessing a camera, Flash Player displays a Privacy dialog box to let users allow or deny access to their camera. If the value of the <code>code</code> property is <code>"Camera.muted"</code>, the user has refused to allow the SWF file access to the user's camera. If the value of the <code>code</code> property is <code>"Camera.Unmuted"</code>, the user has allowed the SWF file access to the user's camera.</td></tr></table>
   * @see #index
   * @see #muted
   * @see #names
   * @see #setMode()
   * @see #event:status
   * @see Video#attachCamera()
   *
   * @example In the following example, after the user allows access to the camera, the attached camera is used to capture video images. Information about the video stream, such as the current frames per second, is also displayed.
   * <p>The <code>Camera.getCamera()</code> method returns a reference to a camera object, or returns null if no camera is available or installed. The if statement checks whether the camera was found and whether the user allowed access to the camera. If the user denied access, the <code>muted</code> property is set to <code>true</code>.</p>
   * <p>Usually, when the <code>attachCamera()</code> method is invoked, a dialog box appears and prompts the user to allow or deny Flash Player access to the camera. However, if the user denied access and selected the <code>Remember</code> option, the dialog box does not appear and nothing displays. To make sure the user has the option to allow access to the camera, the <code>myTextField</code> text field instructs the user to click the text field to invoke the Flash Player Settings dialog box.</p>
   * <p>The <code>clickHandler()</code> method calls <code>Security.showSettings()</code> method, which displays the <code>PRIVACY</code> panel of the Settings dialog box. If the user allows access, the <code>StatusEvent.STATUS</code> event is dispatched and the value of the event's <code>code</code> property is set to <code>Camera.Unmuted</code>. (The camera object's <code>mute</code> property is also set to <code>false</code>.)</p>
   * <p>The <code>statusHandler()</code> method, added to listen to the status change of the user's setting, invokes the <code>connectCamera()</code> method, if the user allows access. The <code>connectCamera()</code> method instantiates a video object with the captured stream's width and height. To display the camera's captured video, the reference to the video stream is attached to the video object, and the video object is added to the display list.</p>
   * <p>A Timer object also is started. Every second, a Timer object's timer event is dispatched and the <code>timerHandler()</code> method is invoked. The <code>timerHandler()</code> method is displayed and updates a number of properties of the Camera object.</p>
   * <p><b>Note:</b> For this example, the only property that changes is the <code>currentFPS</code> property.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.media.Camera;
   *     import flash.media.Video;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldAutoSize;
   *     import flash.utils.Timer;
   *     import flash.events.TimerEvent;
   *     import flash.events.StatusEvent;
   *     import flash.events.MouseEvent;
   *     import flash.system.SecurityPanel;
   *     import flash.system.Security;
   *
   *     public class Camera_getCameraExample extends Sprite {
   *         private var myTextField:TextField;
   *         private var cam:Camera;
   *         private var t:Timer = new Timer(1000);
   *
   *         public function Camera_getCameraExample() {
   *             myTextField = new TextField();
   *             myTextField.x = 10;
   *             myTextField.y = 10;
   *             myTextField.background = true;
   *             myTextField.selectable = false;
   *             myTextField.autoSize = TextFieldAutoSize.LEFT;
   *
   *             if (Camera.isSupported)
   *             {
   *                 cam = Camera.getCamera();
   *
   *                  if (!cam) {
   *                     myTextField.text = "No camera is installed.";
   *
   *                 } else if (cam.muted) {
   *                     myTextField.text = "To enable the use of the camera,\n"
   *                                      + "please click on this text field.\n"
   *                                      + "When the Flash Player Settings dialog appears,\n"
   *                                      + "make sure to select the Allow radio button\n"
   *                                      + "to grant access to your camera.";
   *
   *                     myTextField.addEventListener(MouseEvent.CLICK, clickHandler);
   *
   *                 }else {
   *                     myTextField.text = "Connecting";
   *                     connectCamera();
   *                 }
   *
   *                 addChild(myTextField);
   *
   *                 t.addEventListener(TimerEvent.TIMER, timerHandler);
   *             }else {
   *                 myTextField.text = "The Camera class is not supported on this device.";
   *             }
   *         }
   *
   *         private function clickHandler(e:MouseEvent):void {
   *             Security.showSettings(SecurityPanel.PRIVACY);
   *
   *             cam.addEventListener(StatusEvent.STATUS, statusHandler);
   *
   *             myTextField.removeEventListener(MouseEvent.CLICK, clickHandler);
   *         }
   *
   *         private function statusHandler(event:StatusEvent):void {
   *
   *             if (event.code == "Camera.Unmuted") {
   *                 connectCamera();
   *                 cam.removeEventListener(StatusEvent.STATUS, statusHandler);
   *             }
   *         }
   *
   *         private function connectCamera():void {
   *                 var vid:Video = new Video(cam.width, cam.height);
   *                 vid.x = 10;
   *                 vid.y = 10;
   *                 vid.attachCamera(cam);
   *                 addChild(vid);
   *
   *                 t.start();
   *         }
   *
   *         private function timerHandler(event:TimerEvent):void {
   *             myTextField.y = cam.height + 20;
   *             myTextField.text = "";
   *             myTextField.appendText("bandwidth: " + cam.bandwidth + "\n");
   *             myTextField.appendText("currentFPS: " + Math.round(cam.currentFPS) + "\n");
   *             myTextField.appendText("fps: " + cam.fps + "\n");
   *             myTextField.appendText("keyFrameInterval: " + cam.keyFrameInterval + "\n");
   *         }
   *     }
   * }
   * </listing>
   */
  "public static function getCamera",function getCamera(name/*:String = null*/)/*:Camera*/ {if(arguments.length<1){name = null;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies which video frames are transmitted in full (called <i>keyframes</i>) instead of being interpolated by the video compression algorithm. This method is applicable only if you are transmitting video using Flash Media Server.
   * <p>The Flash Video compression algorithm compresses video by transmitting only what has changed since the last frame of the video; these portions are considered to be interpolated frames. Frames of a video can be interpolated according to the contents of the previous frame. A keyframe, however, is a video frame that is complete; it is not interpolated from prior frames.</p>
   * <p>To determine how to set a value for the <code>keyFrameInterval</code> parameter, consider both bandwidth use and video playback accessibility. For example, specifying a higher value for <code>keyFrameInterval</code> (sending keyframes less frequently) reduces bandwidth use. However, this may increase the amount of time required to position the playhead at a particular point in the video; more prior video frames may have to be interpolated before the video can resume.</p>
   * <p>Conversely, specifying a lower value for <code>keyFrameInterval</code> (sending keyframes more frequently) increases bandwidth use because entire video frames are transmitted more often, but may decrease the amount of time required to seek a particular video frame within a recorded video.</p>
   * @param keyFrameInterval A value that specifies which video frames are transmitted in full (as keyframes) instead of being interpolated by the video compression algorithm. A value of 1 means that every frame is a keyframe, a value of 3 means that every third frame is a keyframe, and so on. Acceptable values are 1 through 48.
   *
   * @see #keyFrameInterval
   *
   */
  "public function setKeyFrameInterval",function setKeyFrameInterval(keyFrameInterval/*:int*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies whether to use a compressed video stream for a local view of the camera. This method is applicable only if you are transmitting video using Flash Media Server; setting <code>compress</code> to <code>true</code> lets you see more precisely how the video will appear to users when they view it in real time.
   * <p>Although a compressed stream is useful for testing purposes, such as previewing video quality settings, it has a significant processing cost, because the local view is not simply compressed; it is compressed, edited for transmission as it would be over a live connection, and then decompressed for local viewing.</p>
   * <p>To set the amount of compression used when you set <code>compress</code> to <code>true</code>, use <code>Camera.setQuality()</code>.</p>
   * @param compress Specifies whether to use a compressed video stream (<code>true</code>) or an uncompressed stream (<code>false</code>) for a local view of what the camera is receiving.
   *
   * @see #setQuality()
   *
   */
  "public function setLoopback",function setLoopback(compress/*:Boolean = false*/)/*:void*/ {if(arguments.length<1){compress = false;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Sets the camera capture mode to the native mode that best meets the specified requirements. If the camera does not have a native mode that matches all the parameters you pass, Flash Player selects a capture mode that most closely synthesizes the requested mode. This manipulation may involve cropping the image and dropping frames.
   * <p>By default, Flash Player drops frames as needed to maintain image size. To minimize the number of dropped frames, even if this means reducing the size of the image, pass <code>false</code> for the <code>favorArea</code> parameter.</p>
   * <p>When choosing a native mode, Flash Player tries to maintain the requested aspect ratio whenever possible. For example, if you issue the command <code>myCam.setMode(400, 400, 30)</code>, and the maximum width and height values available on the camera are 320 and 288, Flash Player sets both the width and height at 288; by setting these properties to the same value, Flash Player maintains the 1:1 aspect ratio you requested.</p>
   * <p>To determine the values assigned to these properties after Flash Player selects the mode that most closely matches your requested values, use the <code>width</code>, <code>height</code>, and <code>fps</code> properties.</p>
   * <p>If you are using Flash Media Server, you can also capture single frames or create time-lapse photography. For more information, see <code>NetStream.attachCamera()</code>.</p>
   * @param width The requested capture width, in pixels. The default value is 160.
   * @param height The requested capture height, in pixels. The default value is 120.
   * @param fps The requested rate at which the camera should capture data, in frames per second. The default value is 15.
   * @param favorArea Specifies whether to manipulate the width, height, and frame rate if the camera does not have a native mode that meets the specified requirements. The default value is <code>true</code>, which means that maintaining capture size is favored; using this parameter selects the mode that most closely matches <code>width</code> and <code>height</code> values, even if doing so adversely affects performance by reducing the frame rate. To maximize frame rate at the expense of camera height and width, pass <code>false</code> for the <code>favorArea</code> parameter.
   *
   * @see #fps
   * @see #height
   * @see #width
   * @see flash.net.NetStream#attachCamera()
   *
   * @example In the following example, when a user clicks on the Stage, the video is resized and the frames per second capture rate is set to a new value.
   * <p>The Stage is set so it does not scale. The <code>Camera.getCamera()</code> method returns a reference to a camera object, or returns null if no camera is available or installed. If a camera exists, the <code>connectCamera()</code> method is called. The <code>connectCamera()</code> method instantiates a video object. To display the camera's captured video, the reference to the video stream is attached to the video object, and the video object is added to the display list. An event listener also is set for a <code>MouseEvent.CLICK</code> event. After the user clicks on the Stage, the <code>clickHandler()</code> method is invoked. The method checks the width of the captured video and sets the camera capture mode's width, height, and the frame per second request rate. In order for these setting to take effect, the video object must be removed and re-created. The video's width and height also must be set to the camera object's width and height.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.media.Camera;
   *     import flash.media.Video;
   *     import flash.events.MouseEvent;
   *     import flash.display.StageScaleMode;
   *
   *     public class Camera_setModeExample extends Sprite {
   *         private var cam:Camera;
   *         private var vid:Video;
   *
   *         public function Camera_setModeExample() {
   *             stage.scaleMode = StageScaleMode.NO_SCALE;
   *
   *             cam = Camera.getCamera();
   *
   *             if (!cam) {
   *                 trace("No camera is installed.");
   *             }else {
   *                 connectCamera();
   *             }
   *         }
   *
   *         private function connectCamera():void {
   *             vid = new Video();
   *             vid.width = cam.width;
   *             vid.height = cam.height;
   *             vid.attachCamera(cam);
   *             addChild(vid);
   *
   *             stage.addEventListener(MouseEvent.CLICK, clickHandler);
   *         }
   *
   *         private function clickHandler(e:MouseEvent):void {
   *
   *             switch (cam.width) {
   *                 case 160:
   *                 cam.setMode(320, 240, 10);
   *                 break;
   *                 case 320:
   *                 cam.setMode(640, 480, 5);
   *                 break;
   *                 default:
   *                 cam.setMode(160, 120, 15);
   *                 break;
   *             }
   *
   *             removeChild(vid);
   *             connectCamera();
   *         }
   *     }
   * }
   * </listing>
   */
  "public function setMode",function setMode(width/*:int*/, height/*:int*/, fps/*:Number*/, favorArea/*:Boolean = true*/)/*:void*/ {if(arguments.length<4){favorArea = true;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies how much motion is required to dispatch the <code>activity</code> event. Optionally sets the number of milliseconds that must elapse without activity before Flash Player considers motion to have stopped and dispatches the event.
   * <p><b>Note:</b> Video can be displayed regardless of the value of the <code>motionLevel</code> parameter. This parameter determines only when and under what circumstances the event is dispatched—not whether video is actually being captured or displayed.</p>
   * <p>To prevent the camera from detecting motion at all, pass a value of 100 for the <code>motionLevel</code> parameter; the <code>activity</code> event is never dispatched. (You would probably use this value only for testing purposes—for example, to temporarily disable any handlers that would normally be triggered when the event is dispatched.)</p>
   * <p>To determine the amount of motion the camera is currently detecting, use the <code>activityLevel</code> property. Motion sensitivity values correspond directly to activity values. Complete lack of motion is an activity value of 0. Constant motion is an activity value of 100. Your activity value is less than your motion sensitivity value when you're not moving; when you are moving, activity values frequently exceed your motion sensitivity value.</p>
   * <p>This method is similar in purpose to the <code>Microphone.setSilenceLevel()</code> method; both methods are used to specify when the <code>activity</code> event should be dispatched. However, these methods have a significantly different impact on publishing streams:</p>
   * <ul>
   * <li><code>Microphone.setSilenceLevel()</code> is designed to optimize bandwidth. When an audio stream is considered silent, no audio data is sent. Instead, a single message is sent, indicating that silence has started.</li>
   * <li><code>Camera.setMotionLevel()</code> is designed to detect motion and does not affect bandwidth usage. Even if a video stream does not detect motion, video is still sent.</li></ul>
   * @param motionLevel Specifies the amount of motion required to dispatch the <code>activity</code> event. Acceptable values range from 0 to 100. The default value is 50.
   * @param timeout Specifies how many milliseconds must elapse without activity before Flash Player considers activity to have stopped and dispatches the <code>activity</code> event. The default value is 2000 milliseconds (2 seconds).
   *
   * @see #motionLevel
   * @see #motionTimeout
   * @see Microphone#setSilenceLevel()
   *
   * @example In the following example, the user's camera is used as a monitor or a surveillance camera. The camera detects motion and a text field shows the activity level. (The example can be extended to sound an alarm or send a message through a web service to other applications.)
   * <p>The <code>Camera.getCamera()</code> method returns a reference to a camera object, or returns null if no camera is available or installed. The if statement checks whether a camera is available, and invokes the <code>connectCamera()</code> method when it is available. The <code>connectCamera()</code> method instantiates a video object with the captured stream's width and height. To display the camera's captured video, the reference to the video stream is attached to the video object, and the video object is added to the display list. (Usually, when the <code>attachCamera()</code> method is invoked, a dialog box appears and prompts the user to allow or deny Flash Player access to the camera. However, if the user denied access and selected the <code>Remember</code> option, the dialog box does not appear and nothing is displayed. To make sure the user has the option to allow access to the camera, use the <code>system.Security.showSettings()</code> method to invoke the Flash Player Settings dialog box.)</p>
   * <p>The <code>setMotionLevel()</code> method sets the level of activity (amount of motion), before the activity event is invoked, to five, for minimal motion. The time between when the camera stops detecting motion and when the activity event is invoked, is set to 1 second (1000 millisecond). After 1 second passes without activity or the level of activity reaches five, the <code>ActivityEvent.ACTIVITY</code> event is dispatched and the <code>activityHandler()</code> method is invoked. If the event was triggered by the level of activity, the <code>activating</code> property is set to <code>true</code> and a Timer object is started. Every second, a Timer objectÃ¢â‚¬™s timer event is dispatched and the <code>timerHandler()</code> method is invoked, which displays the current level of activity. (Although a level of five or larger triggers the timer, the displayed current level of activity might be a smaller number.)</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.media.Camera;
   *     import flash.media.Video;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldAutoSize;
   *     import flash.utils.Timer;
   *     import flash.events.TimerEvent;
   *     import flash.events.ActivityEvent;
   *
   *     public class Camera_setMotionLevelExample extends Sprite {
   *         private var myTextField:TextField;
   *         private var cam:Camera;
   *         private var t:Timer = new Timer(1000);
   *
   *         public function Camera_setMotionLevelExample() {
   *             myTextField = new TextField();
   *             myTextField.background = true;
   *             myTextField.selectable = false;
   *             myTextField.autoSize = TextFieldAutoSize.LEFT;
   *
   *             cam = Camera.getCamera();
   *
   *             if (!cam) {
   *                 myTextField.text = "No camera is installed.";
   *
   *             }else {
   *                 myTextField.text = "Waiting to connect.";
   *                 connectCamera();
   *             }
   *
   *             addChild(myTextField);
   *
   *             t.addEventListener(TimerEvent.TIMER, timerHandler);
   *         }
   *
   *         private function connectCamera():void {
   *                 var vid:Video = new Video(cam.width, cam.height);
   *                 vid.x = 10;
   *                 vid.y = 10;
   *                 vid.attachCamera(cam);
   *                 addChild(vid);
   *
   *                 cam.setMotionLevel(5, 1000);
   *                 cam.addEventListener(ActivityEvent.ACTIVITY, activityHandler);
   *         }
   *
   *         private function activityHandler(e:ActivityEvent):void {
   *             if (e.activating == true) {
   *                 t.start();
   *             } else {
   *                 myTextField.text = "Everything is quiet.";
   *                 t.stop();
   *             }
   *         }
   *
   *         private function timerHandler(event:TimerEvent):void {
   *              myTextField.x = 10;
   *              myTextField.y = cam.height + 20;
   *              myTextField.text = "There is some activity. Level: " + cam.activityLevel;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function setMotionLevel",function setMotionLevel(motionLevel/*:int*/, timeout/*:int = 2000*/)/*:void*/ {if(arguments.length<2){timeout = 2000;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Sets the maximum amount of bandwidth per second or the required picture quality of the current outgoing video feed. This method is generally applicable only if you are transmitting video using Flash Media Server.
   * <p>Use this method to specify which element of the outgoing video feed is more important to your application—bandwidth use or picture quality.</p>
   * <ul>
   * <li>To indicate that bandwidth use takes precedence, pass a value for <code>bandwidth</code> and 0 for <code>quality</code>. Flash Player transmits video at the highest quality possible within the specified bandwidth. If necessary, Flash Player reduces picture quality to avoid exceeding the specified bandwidth. In general, as motion increases, quality decreases.</li>
   * <li>To indicate that quality takes precedence, pass 0 for <code>bandwidth</code> and a numeric value for <code>quality</code>. Flash Player uses as much bandwidth as required to maintain the specified quality. If necessary, Flash Player reduces the frame rate to maintain picture quality. In general, as motion increases, bandwidth use also increases.</li>
   * <li>To specify that both bandwidth and quality are equally important, pass numeric values for both parameters. Flash Player transmits video that achieves the specified quality and that doesn't exceed the specified bandwidth. If necessary, Flash Player reduces the frame rate to maintain picture quality without exceeding the specified bandwidth.</li></ul>
   * @param bandwidth Specifies the maximum amount of bandwidth that the current outgoing video feed can use, in bytes per second. To specify that Flash Player video can use as much bandwidth as needed to maintain the value of <code>quality</code>, pass 0 for <code>bandwidth</code>. The default value is 16384.
   * @param quality An integer that specifies the required level of picture quality, as determined by the amount of compression being applied to each video frame. Acceptable values range from 1 (lowest quality, maximum compression) to 100 (highest quality, no compression). To specify that picture quality can vary as needed to avoid exceeding bandwidth, pass 0 for <code>quality</code>.
   *
   * @see #getCamera()
   * @see #quality
   *
   */
  "public function setQuality",function setQuality(bandwidth/*:int*/, quality/*:int*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},["names","getCamera"],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.3"
);