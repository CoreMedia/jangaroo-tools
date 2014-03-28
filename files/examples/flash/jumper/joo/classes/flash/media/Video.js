joo.classLoader.prepare("package flash.media",/* {
import flash.display.DisplayObject
import flash.net.NetStream*/

/**
 * The Video class displays live or recorded video in an application without embedding the video in your SWF file. This class creates a Video object that plays either of the following kinds of video: recorded video files stored on a server or locally, or live video captured by the user. A Video object is a display object on the application's display list and represents the visual space in which the video runs in a user interface.
 * <p>When used with Flash Media Server, the Video object allows you to send live video captured by a user to the server and then broadcast it from the server to other users. Using these features, you can develop media applications such as a simple video player, a video player with multipoint publishing from one server to another, or a video sharing application for a user community.</p>
 * <p>Flash Player 9 and later supports publishing and playback of FLV files encoded with either the Sorenson Spark or On2 VP6 codec and also supports an alpha channel. The On2 VP6 video codec uses less bandwidth than older technologies and offers additional deblocking and deringing filters. See the flash.net.NetStream class for more information about video playback and supported formats.</p>
 * <p>Flash Player 9.0.115.0 and later supports mipmapping to optimize runtime rendering quality and performance. For video playback, Flash Player uses mipmapping optimization if you set the Video object's <code>smoothing</code> property to <code>true</code>.</p>
 * <p>As with other display objects on the display list, you can control various properties of Video objects. For example, you can move the Video object around on the Stage by using its <code>x</code> and <code>y</code> properties, you can change its size using its <code>height</code> and <code>width</code> properties, and so on.</p>
 * <p>To play a video stream, use <code>attachCamera()</code> or <code>attachNetStream()</code> to attach the video to the Video object. Then, add the Video object to the display list using <code>addChild()</code>.</p>
 * <p>If you are using Flash Professional, you can also place the Video object on the Stage rather than adding it with <code>addChild()</code>, like this:</p><ol>
 * <li>If the Library panel isn't visible, select Window > Library to display it.</li>
 * <li>Add an embedded Video object to the library by clicking the Options menu on the right side of the Library panel title bar and selecting New Video.</li>
 * <li>In the Video Properties dialog box, name the embedded Video object for use in the library and click OK.</li>
 * <li>Drag the Video object to the Stage and use the Property Inspector to give it a unique instance name, such as <code>my_video</code>. (Do not name it Video.)</li></ol>
 * <p>In AIR applications on the desktop, playing video in fullscreen mode disables any power and screen saving features (when allowed by the operating system).</p>
 * <p><b>Note:</b> The Video class is not a subclass of the InteractiveObject class, so it cannot dispatch mouse events. However, you can call the <code>addEventListener()</code> method on the display object container that contains the Video object.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/media/Video.html#includeExamplesSummary">View the examples</a></p>
 * @see #attachCamera()
 * @see #attachNetStream()
 * @see Camera#getCamera()
 * @see flash.net.NetConnection
 * @see flash.net.NetStream
 * @see flash.display.DisplayObjectContainer#addChild()
 * @see flash.display.Stage#addChild()
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e1a.html Working with Video
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e3c.html Core display classes
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7d5b.html Taking advantage of mipmapping
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7d50.html Basics of video
 *
 */
"public class Video extends flash.display.DisplayObject",3,function($$private){;return[ 
  /**
   * Indicates the type of filter applied to decoded video as part of post-processing. The default value is 0, which lets the video compressor apply a deblocking filter as needed.
   * <p>Compression of video can result in undesired artifacts. You can use the <code>deblocking</code> property to set filters that reduce blocking and, for video compressed using the On2 codec, ringing.</p>
   * <p><i>Blocking</i> refers to visible imperfections between the boundaries of the blocks that compose each video frame. <i>Ringing</i> refers to distorted edges around elements within a video image.</p>
   * <p>Two deblocking filters are available: one in the Sorenson codec and one in the On2 VP6 codec. In addition, a deringing filter is available when you use the On2 VP6 codec. To set a filter, use one of the following values:</p>
   * <ul>
   * <li>0—Lets the video compressor apply the deblocking filter as needed.</li>
   * <li>1—Does not use a deblocking filter.</li>
   * <li>2—Uses the Sorenson deblocking filter.</li>
   * <li>3—For On2 video only, uses the On2 deblocking filter but no deringing filter.</li>
   * <li>4—For On2 video only, uses the On2 deblocking and deringing filter.</li>
   * <li>5—For On2 video only, uses the On2 deblocking and a higher-performance On2 deringing filter.</li></ul>
   * <p>If a value greater than 2 is selected for video when you are using the Sorenson codec, the Sorenson decoder defaults to 2.</p>
   * <p>Using a deblocking filter has an effect on overall playback performance, and it is usually not necessary for high-bandwidth video. If a user's system is not powerful enough, the user may experience difficulties playing back video with a deblocking filter enabled.</p>
   */
  "public function get deblocking",function deblocking$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set deblocking",function deblocking$set(value/*:int*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies whether the video should be smoothed (interpolated) when it is scaled. For smoothing to work, the runtime must be in high-quality mode (the default). The default value is <code>false</code> (no smoothing).
   * <p>For video playback using Flash Player 9.0.115.0 and later versions, set this property to <code>true</code> to take advantage of mipmapping image optimization.</p>
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7d5b.html Taking advantage of mipmapping
   *
   */
  "public function get smoothing",function smoothing$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set smoothing",function smoothing$set(value/*:Boolean*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * An integer specifying the height of the video stream, in pixels. For live streams, this value is the same as the <code>Camera.height</code> property of the Camera object that is capturing the video stream. For recorded video files, this value is the height of the video.
   * <p>You may want to use this property, for example, to ensure that the user is seeing the video at the same size at which it was captured, regardless of the actual size of the Video object on the Stage.</p>
   * @see Camera#height
   *
   */
  "public function get videoHeight",function videoHeight$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * An integer specifying the width of the video stream, in pixels. For live streams, this value is the same as the <code>Camera.width</code> property of the Camera object that is capturing the video stream. For recorded video files, this value is the width of the video.
   * <p>You may want to use this property, for example, to ensure that the user is seeing the video at the same size at which it was captured, regardless of the actual size of the Video object on the Stage.</p>
   * @see Camera#width
   *
   */
  "public function get videoWidth",function videoWidth$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Creates a new Video instance. If no values for the <code>width</code> and <code>height</code> parameters are supplied, the default values are used. You can also set the width and height properties of the Video object after the initial construction, using <code>Video.width</code> and <code>Video.height</code>. When a new Video object is created, values of zero for width or height are not allowed; if you pass zero, the defaults will be applied.
   * <p>After creating the Video, call the <code>DisplayObjectContainer.addChild()</code> or <code>DisplayObjectContainer.addChildAt()</code> method to add the Video object to a parent DisplayObjectContainer object.</p>
   * @param width The width of the video, in pixels.
   * @param height The height of the video, in pixels.
   *
   * @example The following example shows how to load an external FLV file:
   * <listing>
   * var MyVideo:Video = new Video();
   * addChild(MyVideo);
   *
   * var MyNC:NetConnection = new NetConnection();
   * MyNC.connect(null);
   *
   * var MyNS:NetStream = new NetStream(MyNC);
   * MyNS.play("http://www.helpexamples.com/flash/video/clouds.flv");
   *
   * MyVideo.attachNetStream(MyNS);
   *
   * //the clouds.flv video has metadata we're not using, so create
   * //an error handler to ignore the message generated by the runtime
   * //about the metadata
   * MyNS.addEventListener(AsyncErrorEvent.ASYNC_ERROR, asyncErrorHandler);
   *
   * function asyncErrorHandler(event:AsyncErrorEvent):void
   * {
   *    //ignore metadata error message
   * }
   * </listing>
   */
  "public function Video",function Video$(width/*:int = 320*/, height/*:int = 240*/) {switch(arguments.length){case 0:width = 320;case 1:height = 240;}this.super$3();
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies a video stream from a camera to be displayed within the boundaries of the Video object in the application.
   * <p>Use this method to attach live video captured by the user to the Video object. You can play the live video locally on the same computer or device on which it is being captured, or you can send it to Flash Media Server and use the server to stream it to other users.</p>
   * @param camera A Camera object that is capturing video data. To drop the connection to the Video object, pass <code>null</code>.
   *
   * @see #attachNetStream()
   * @see Camera
   *
   * @example <a href="http://www.adobe.com/go/learn_as3_usingexamples_en">How to use this example</a>Please see the <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/media/Camera.html#getCamera()">Camera.getCamera()</a> method example for an illustration of how to use this method.
   */
  "public function attachCamera",function attachCamera(camera/*:Camera*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies a video stream to be displayed within the boundaries of the Video object in the application. The video stream is either a video file played with <code>NetStream.play()</code>, a Camera object, or <code>null</code>. If you use a video file, it can be stored on the local file system or on Flash Media Server. If the value of the <code>netStream</code> argument is <code>null</code>, the video is no longer played in the Video object.
   * <p>You do not need to use this method if a video file contains only audio; the audio portion of video files is played automatically when you call <code>NetStream.play()</code>. To control the audio associated with a video file, use the <code>soundTransform</code> property of the NetStream object that plays the video file.</p>
   * @param netStream A NetStream object. To drop the connection to the Video object, pass <code>null</code>.
   *
   * @see #attachCamera()
   * @see flash.net.NetStream#soundTransform
   * @see flash.net.NetStream#play()
   * @see SoundTransform
   *
   */
  "public function attachNetStream",function attachNetStream(netStream/*:NetStream*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Clears the image currently displayed in the Video object (not the video stream). This method is useful for handling the current image. For example, you can clear the last image or display standby information without hiding the Video object.
   * @see #attachCamera()
   *
   */
  "public function clear",function clear()/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["flash.display.DisplayObject","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);