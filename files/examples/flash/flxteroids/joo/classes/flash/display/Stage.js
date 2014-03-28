joo.classLoader.prepare("package flash.display",/* {
import flash.events.Event
import flash.events.TimerEvent
import flash.geom.Rectangle
import flash.text.TextSnapshot
import flash.utils.Timer

import js.HTMLElement*/

/**
 * Dispatched when the Stage object enters, or leaves, full-screen mode. A change in full-screen mode can be initiated through ActionScript, or the user invoking a keyboard shortcut, or if the current focus leaves the full-screen window.
 * @eventType flash.events.FullScreenEvent.FULL_SCREEN
 */
{Event:{name:"fullScreen", type:"flash.events.FullScreenEvent"}},
/**
 * Dispatched by the Stage object when the pointer moves out of the stage area. If the mouse button is pressed, the event is not dispatched.
 * @eventType flash.events.Event.MOUSE_LEAVE
 */
{Event:{name:"mouseLeave", type:"flash.events.Event"}},
/**
 * Dispatched when the <code>scaleMode</code> property of the Stage object is set to <code>StageScaleMode.NO_SCALE</code> and the SWF file is resized.
 * @eventType flash.events.Event.RESIZE
 */
{Event:{name:"resize", type:"flash.events.Event"}},

/**
 * The Stage class represents the main drawing area.
 * <p>For SWF content running in the browser (in Flash<sup>®</sup> Player), the Stage represents the entire area where Flash content is shown. For content running in AIR on desktop operating systems, each NativeWindow object has a corresponding Stage object.</p>
 * <p>The Stage object is not globally accessible. You need to access it through the <code>stage</code> property of a DisplayObject instance.</p>
 * <p>The Stage class has several ancestor classes — DisplayObjectContainer, InteractiveObject, DisplayObject, and EventDispatcher — from which it inherits properties and methods. Many of these properties and methods are either inapplicable to Stage objects, or require security checks when called on a Stage object. The properties and methods that require security checks are documented as part of the Stage class.</p>
 * <p>In addition, the following inherited properties are inapplicable to Stage objects. If you try to set them, an IllegalOperationError is thrown. These properties may always be read, but since they cannot be set, they will always contain default values.</p>
 * <ul>
 * <li><code>accessibilityProperties</code></li>
 * <li><code>alpha</code></li>
 * <li><code>blendMode</code></li>
 * <li><code>cacheAsBitmap</code></li>
 * <li><code>contextMenu</code></li>
 * <li><code>filters</code></li>
 * <li><code>focusRect</code></li>
 * <li><code>loaderInfo</code></li>
 * <li><code>mask</code></li>
 * <li><code>mouseEnabled</code></li>
 * <li><code>name</code></li>
 * <li><code>opaqueBackground</code></li>
 * <li><code>rotation</code></li>
 * <li><code>scale9Grid</code></li>
 * <li><code>scaleX</code></li>
 * <li><code>scaleY</code></li>
 * <li><code>scrollRect</code></li>
 * <li><code>tabEnabled</code></li>
 * <li><code>tabIndex</code></li>
 * <li><code>transform</code></li>
 * <li><code>visible</code></li>
 * <li><code>x</code></li>
 * <li><code>y</code></li></ul>
 * <p>Some events that you might expect to be a part of the Stage class, such as <code>enterFrame</code>, <code>exitFrame</code>, <code>frameConstructed</code>, and <code>render</code>, cannot be Stage events because a reference to the Stage object cannot be guaranteed to exist in every situation where these events are used. Because these events cannot be dispatched by the Stage object, they are instead dispatched by every DisplayObject instance, which means that you can add an event listener to any DisplayObject instance to listen for these events. These events, which are part of the DisplayObject class, are called broadcast events to differentiate them from events that target a specific DisplayObject instance. Two other broadcast events, <code>activate</code> and <code>deactivate</code>, belong to DisplayObject's superclass, EventDispatcher. The <code>activate</code> and <code>deactivate</code> events behave similarly to the DisplayObject broadcast events, except that these two events are dispatched not only by all DisplayObject instances, but also by all EventDispatcher instances and instances of other EventDispatcher subclasses. For more information on broadcast events, see the DisplayObject class.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/display/Stage.html#includeExamplesSummary">View the examples</a></p>
 * @see DisplayObject
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e3c.html Core display classes
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e3d.html Working with display objects
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e31.html Setting Stage properties
 *
 */
"public class Stage extends flash.display.DisplayObjectContainer",5,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.display.StageScaleMode,flash.display.StageQuality,flash.display.StageAlign,flash.events.Event,flash.events.TimerEvent);}, 
  /**
   * A value from the StageAlign class that specifies the alignment of the stage in Flash Player or the browser. The following are valid values:
   * <table>
   * <tr><th>Value</th><th>Vertical Alignment</th><th>Horizontal</th></tr>
   * <tr>
   * <td><code>StageAlign.TOP</code></td>
   * <td>Top</td>
   * <td>Center</td></tr>
   * <tr>
   * <td><code>StageAlign.BOTTOM</code></td>
   * <td>Bottom</td>
   * <td>Center</td></tr>
   * <tr>
   * <td><code>StageAlign.LEFT</code></td>
   * <td>Center</td>
   * <td>Left</td></tr>
   * <tr>
   * <td><code>StageAlign.RIGHT</code></td>
   * <td>Center</td>
   * <td>Right</td></tr>
   * <tr>
   * <td><code>StageAlign.TOP_LEFT</code></td>
   * <td>Top</td>
   * <td>Left</td></tr>
   * <tr>
   * <td><code>StageAlign.TOP_RIGHT</code></td>
   * <td>Top</td>
   * <td>Right</td></tr>
   * <tr>
   * <td><code>StageAlign.BOTTOM_LEFT</code></td>
   * <td>Bottom</td>
   * <td>Left</td></tr>
   * <tr>
   * <td><code>StageAlign.BOTTOM_RIGHT</code></td>
   * <td>Bottom</td>
   * <td>Right</td></tr></table>
   * <p>The <code>align</code> property is only available to an object that is in the same security sandbox as the Stage owner (the main SWF file). To avoid this, the Stage owner can grant permission to the domain of the calling object by calling the <code>Security.allowDomain()</code> method or the <code>Security.alowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.</p>
   * @see StageAlign
   *
   */
  "public function get align",function align$get()/*:String*/ {
    return this._align$5;
  },

  /**
   * @private
   */
  "public function set align",function align$set(value/*:String*/)/*:void*/ {
    this._align$5 = value;
  },

  /**
   * A value from the StageDisplayState class that specifies which display state to use. The following are valid values:
   * <ul>
   * <li><code>StageDisplayState.FULL_SCREEN</code> Sets AIR application or Flash runtime to expand the stage over the user's entire screen, with keyboard input disabled.</li>
   * <li><code>StageDisplayState.FULL_SCREEN_INTERACTIVE</code> Sets the AIR application to expand the stage over the user's entire screen, with keyboard input allowed. (Not available for content running in Flash Player.)</li>
   * <li><code>StageDisplayState.NORMAL</code> Sets the Flash runtime back to the standard stage display mode.</li></ul>
   * <p>The scaling behavior of the movie in full-screen mode is determined by the <code>scaleMode</code> setting (set using the <code>Stage.scaleMode</code> property or the SWF file's <code>embed</code> tag settings in the HTML file). If the <code>scaleMode</code> property is set to <code>noScale</code> while the application transitions to full-screen mode, the Stage <code>width</code> and <code>height</code> properties are updated, and the Stage the <code>resize</code> event.</p>
   * <p>The following restrictions apply to SWF files that play within an HTML page (not those using the stand-alone Flash Player or not running in the AIR runtime):</p>
   * <ul>
   * <li>To enable full-screen mode, add the <code>allowFullScreen</code> parameter to the <code>object</code> and <code>embed</code> tags in the HTML page that includes the SWF file, with <code>allowFullScreen</code> set to <code>"true"</code>, as shown in the following example:
   * <listing>
   * <param name="allowFullScreen" value="true" />
   *           ...
   *     <embed src="example.swf" allowFullScreen="true" ... ></listing>
   * <p>An HTML page may also use a script to generate SWF-embedding tags. You need to alter the script so that it inserts the proper <code>allowFullScreen</code> settings. HTML pages generated by Flash Professional and Flash Builder use the <code>AC_FL_RunContent()</code> function to embed references to SWF files, and you need to add the <code>allowFullScreen</code> parameter settings, as in the following:</p>
   * <listing>
   * AC_FL_RunContent( ... "allowFullScreen", "true", ... )</listing></li>
   * <li>Full-screen mode is initiated in response to a mouse click or key press by the user; the movie cannot change <code>Stage.displayState</code> without user input. Flash runtimes restrict keyboard input in full-screen mode. Acceptable keys include keyboard shortcuts that terminate full-screen mode and non-printing keys such as arrows, space, Shift, and Tab keys. Keyboard shortcuts that terminate full-screen mode are: Escape (Windows, Linux, and Mac), Control+W (Windows), Command+W (Mac), and Alt+F4.
   * <p>A Flash runtime dialog box appears over the movie when users enter full-screen mode to inform the users they are in full-screen mode and that they can press the Escape key to end full-screen mode.</p></li>
   * <li>Starting with Flash Player 9.0.115.0, full-screen works the same in windowless mode as it does in window mode. If you set the Window Mode (<code>wmode</code> in the HTML) to Opaque Windowless (<code>opaque</code>) or Transparent Windowless (<code>transparent</code>), full-screen can be initiated, but the full-screen window will always be opaque.</li></ul>
   * <p>These restrictions are <i>not</i> present for SWF content running in the stand-alone Flash Player or in AIR. AIR supports an interactive full-screen mode which allows keyboard input.</p>
   * <p>For AIR content running in full-screen mode, the system screen saver and power saving options are disabled while video content is playing and until either the video stops or full-screen mode is exited.</p>
   * <p>On Linux, setting <code>displayState</code> to <code>StageDisplayState.FULL_SCREEN</code> or <code>StageDisplayState.FULL_SCREEN_INTERACTIVE</code> is an asynchronous operation.</p>
   * @throws SecurityError Calling the <code>displayState</code> property of a Stage object throws an exception for any caller that is not in the same security sandbox as the Stage owner (the main SWF file). To avoid this, the Stage owner can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>. Trying to set the <code>displayState</code> property while the settings dialog is displayed, without a user response, or if the <code>param</code> or <code>embed</code> HTML tag's <code>allowFullScreen</code> attribute is not set to <code>true</code> throws a security error.
   *
   * @see StageDisplayState
   * @see #scaleMode
   * @see flash.events.FullScreenEvent
   * @see flash.events.Event#RESIZE
   *
   * @example The following example creates an interactive demonstration of how to create a fullscreen experience by modifying the <code>displayState</code> property.
   * <p><b>Note</b>: Fullscreen can only be triggered in certain situations, such as if the user has clicked or pressed a key, due to security restrictions. When run in a browser, the allowFullScreen property must be set to true.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.display.Stage;
   *     import flash.events.*;
   *     import flash.net.NetConnection;
   *     import flash.net.NetStream;
   *     import flash.media.Video;
   *
   *     public class FullScreenExample extends Sprite
   *     {
   *         private var videoURL:String = "testVideo.flv";
   *         private var connection:NetConnection;
   *         private var stream:NetStream;
   *         private var video:Video;
   *
   *         public function FullScreenExample() {
   *             connection = new NetConnection();
   *             connection.addEventListener(NetStatusEvent.NET_STATUS, netStatusHandler);
   *             connection.addEventListener(SecurityErrorEvent.SECURITY_ERROR, securityErrorHandler);
   *             connection.connect(null);
   *
   *             loaderInfo.addEventListener(Event.INIT, createMouseListener);
   *         }
   *
   *         private function createMouseListener(event:Event):void {
   *             stage.addEventListener(MouseEvent.CLICK,toggleFullScreen);
   *         }
   *
   *         private function toggleFullScreen(event:MouseEvent):void {
   *             switch(stage.displayState) {
   *                 case "normal":
   *                     stage.displayState = "fullScreen";
   *                     break;
   *                 case "fullScreen":
   *                 default:
   *                     stage.displayState = "normal";
   *                     break;
   *             }
   *         }
   *
   *         // Video related:
   *         private function netStatusHandler(event:NetStatusEvent):void {
   *             switch (event.info.code) {
   *                 case "NetConnection.Connect.Success":
   *                     connectStream();
   *                     break;
   *                 case "NetStream.Play.StreamNotFound":
   *                     trace("Unable to locate video: " + videoURL);
   *                     break;
   *             }
   *         }
   *         private function connectStream():void {
   *             var stream:NetStream = new NetStream(connection);
   *             stream.addEventListener(NetStatusEvent.NET_STATUS, netStatusHandler);
   *             stream.addEventListener(AsyncErrorEvent.ASYNC_ERROR, asyncErrorHandler);
   *
   *             video = new Video(stage.stageWidth,stage.stageHeight);
   *             video.attachNetStream(stream);
   *             stream.play(videoURL);
   *             addChild(video);
   *         }
   *         private function securityErrorHandler(event:SecurityErrorEvent):void {
   *             trace("securityErrorHandler: " + event);
   *         }
   *         private function asyncErrorHandler(event:AsyncErrorEvent):void {
   *             // ignore AsyncErrorEvent events.
   *         }
   *     }
   * }
   * </listing>
   */
  "public function get displayState",function displayState$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set displayState",function displayState$set(value/*:String*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The interactive object with keyboard focus; or <code>null</code> if focus is not set or if the focused object belongs to a security sandbox to which the calling object does not have access.
   * @throws Error Throws an error if focus cannot be set to the target.
   *
   * @example The following sets the initial focus to the text field <code>myTF</code> so the user can start typing without having to click anything. If you test this code within the authoring tool interface, you can only have access to a few keys because the host (browser or tool) interprets most key presses first. To see this example work as intended, compile it and run the SWF file.
   * <listing>
   * var myTF:TextField = new TextField();
   * myTF.border =true;
   * myTF.type = TextFieldType.INPUT;
   *
   * addChild(myTF);
   * stage.focus= myTF;
   * </listing>
   */
  "public function get focus",function focus$get()/*:InteractiveObject*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set focus",function focus$set(value/*:InteractiveObject*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Gets and sets the frame rate of the stage. The frame rate is defined as frames per second. By default the rate is set to the frame rate of the first SWF file loaded. Valid range for the frame rate is from 0.01 to 1000 frames per second.
   * <p><b>Note:</b> An application might not be able to follow high frame rate settings, either because the target platform is not fast enough or the player is synchronized to the vertical blank timing of the display device (usually 60 Hz on LCD devices). In some cases, a target platform might also choose to lower the maximum frame rate if it anticipates high CPU usage.</p>
   * <p>For content running in Adobe AIR, setting the <code>frameRate</code> property of one Stage object changes the frame rate for all Stage objects (used by different NativeWindow objects).</p>
   * @throws SecurityError Calling the <code>frameRate</code> property of a Stage object throws an exception for any caller that is not in the same security sandbox as the Stage owner (the main SWF file). To avoid this, the Stage owner can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.
   *
   */
  "public function get frameRate",function frameRate$get()/*:Number*/ {
    return this._frameRate$5;
  },

  /**
   * @private
   */
  "public function set frameRate",function frameRate$set(value/*:Number*/)/*:void*/ {
    this._frameRate$5 = Number(value);
    if (this.frameTimer$5) {
      this.frameTimer$5.delay = 1000 / this._frameRate$5;
    }
  },

  /**
   * Returns the height of the monitor that will be used when going to full screen size, if that state is entered immediately. If the user has multiple monitors, the monitor that's used is the monitor that most of the stage is on at the time.
   * <p><b>Note</b>: If the user has the opportunity to move the browser from one monitor to another between retrieving the value and going to full screen size, the value could be incorrect. If you retrieve the value in an event handler that sets <code>Stage.displayState</code> to <code>StageDisplayState.FULL_SCREEN</code>, the value will be correct.</p>
   * <p>This is the pixel height of the monitor and is the same as the stage height would be if <code>Stage.align</code> is set to <code>StageAlign.TOP_LEFT</code> and <code>Stage.scaleMode</code> is set to <code>StageScaleMode.NO_SCALE</code>.</p>
   * @see #displayState
   * @see #fullScreenSourceRect
   * @see #fullScreenWidth
   * @see #scaleMode
   * @see StageDisplayState
   * @see flash.events.Event#RESIZE
   * @see flash.events.FullScreenEvent
   *
   * @example This example creates a green rectangle the size of the stage and places a red square on it that it activates as a button. Clicking the red square triggers the <code>enterFullScreen()</code> event handler, which sets the <code>fullScreenSourceRect</code> property and enters full screen mode. To set the <code>fullScreenSourceRect</code> property, the event handler starts with the location and dimensions of the red square. It then compares the aspect ratio (width divided by height) of the red square to the aspect ratio of the stage at full screen width and height so that it can expand the rectangle (<code>fullScreenSourceRect</code>) to match the screen's aspect ratio. The result is that the red square occupies the entire height of the monitor with the green background visible on each side. If the aspect ratio was not matched, the stage background color, which is white by default, would show on each side instead of the green background.
   * <p><b>Note</b>: Test this example in the browser. In the Flash Publish Settings dialog, on the HTML tab, select the template Flash Only - Allow Full Screen. Specify the Flash Player version 9.0.115.0, and make sure the Flash and HTML formats are selected on the Formats tab. Then publish and open the resulting HTML file in the browser.</p>
   * <listing>
   * import flash.display.Sprite;
   * import flash.display.Stage;
   * import flash.display.StageDisplayState;
   * import flash.events.MouseEvent;
   * import flash.geom.Rectangle;
   *
   * // cover the stage with a green rectangle
   * var greenRect:Sprite = new Sprite();
   * greenRect.graphics.beginFill(0x00FF00);
   * greenRect.graphics.drawRect(0, 0, stage.stageWidth, stage.stageHeight);
   * addChild(greenRect);
   *
   * // create red square on stage, turn it into a button for going to full screen
   * var redSquare:Sprite = new Sprite();
   * redSquare.graphics.beginFill(0xFF0000);
   * redSquare.graphics.drawRect(0, 0, 300, 300);
   * redSquare.x = 50;
   * redSquare.y = 50;
   * redSquare.addEventListener(MouseEvent.CLICK, enterFullScreen);
   * redSquare.buttonMode = true;
   * addChild(redSquare);
   *
   * function enterFullScreen(e:MouseEvent):void
   * {
   *     // we will go to full screen zoomed in on the red square
   *     var redSquare:Sprite = e.target as Sprite;
   *     var fullScreenRect:Rectangle = new Rectangle(redSquare.x, redSquare.y, redSquare.width, redSquare.height);
   *
   *     // calculate aspect ratio of the red square
   *     var rectAspectRatio:Number = fullScreenRect.width / fullScreenRect.height;
   *
   *     // calculate aspect ratio of the screen
   *     var screenAspectRatio:Number = stage.fullScreenWidth / stage.fullScreenHeight;
   *
   *     // change the fullScreenRect so that it covers the entire screen, keeping it centered on the redSquare
   *     // try commenting out this section to see what happens if you do not fix the aspect ratio.
   *     if (rectAspectRatio > screenAspectRatio) {
   *          var newHeight:Number = fullScreenRect.width / screenAspectRatio;
   *          fullScreenRect.y -= ((newHeight - fullScreenRect.height) / 2);
   *         fullScreenRect.height = newHeight;
   *     } else if (rectAspectRatio < screenAspectRatio) {
   *         var newWidth:Number = fullScreenRect.height * screenAspectRatio;
   *         fullScreenRect.x -= ((newWidth - fullScreenRect.width) / 2);
   *         fullScreenRect.width = newWidth;
   *     }
   *
   *     // go to full screen
   *     stage.fullScreenSourceRect = fullScreenRect;
   *     stage.displayState = StageDisplayState.FULL_SCREEN;
   * }
   * </listing>
   */
  "public function get fullScreenHeight",function fullScreenHeight$get()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Sets the Flash runtime to scale a specific region of the stage to full-screen mode. If available, the Flash runtime scales in hardware, which uses the graphics and video card on a user's computer, and generally displays content more quickly than software scaling.
   * <p>When this property is set to a valid rectangle and the <code>displayState</code> property is set to full-screen mode, the Flash runtime scales the specified area. The actual Stage size in pixels within ActionScript does not change. The Flash runtime enforces a minimum limit for the size of the rectangle to accommodate the standard "Press Esc to exit full-screen mode" message. This limit is usually around 260 by 30 pixels but can vary on platform and Flash runtime version.</p>
   * <p>This property can only be set when the Flash runtime is not in full-screen mode. To use this property correctly, set this property first, then set the <code>displayState</code> property to full-screen mode, as shown in the code examples.</p>
   * <p>To enable scaling, set the <code>fullScreenSourceRect</code> property to a rectangle object:</p>
   * <listing>
   *
   *      // valid, will enable hardware scaling
   *      stage.fullScreenSourceRect = new Rectangle(0,0,320,240);
   *     </listing>
   * <p>To disable scaling, set the <code>fullScreenSourceRect=null</code> in ActionScript 3.0, and <code>undefined</code> in ActionScript 2.0.</p>
   * <listing>
   *      stage.fullScreenSourceRect = null;
   *     </listing>
   * <p>The end user also can select within Flash Player Display Settings to turn off hardware scaling, which is enabled by default. For more information, see <a href="http://www.adobe.com/go/display_settings">www.adobe.com/go/display_settings</a>.</p>
   * @see StageDisplayState
   * @see #displayState
   * @see #scaleMode
   * @see flash.events.FullScreenEvent
   * @see flash.events.Event#RESIZE
   *
   * @example To take advantage of hardware scaling, you set the whole stage or part of the stage to full-screen mode. The following ActionScript 3.0 code sets the whole stage to full-screen mode:
   * <listing>
   *
   * import flash.geom.*;
   * {
   *   stage.fullScreenSourceRect = new Rectangle(0,0,320,240);
   *   stage.displayState = StageDisplayState.FULL_SCREEN;
   * }
   * </listing>
   * <div>In the following example, the user can switch between playing a video in full or normal screen mode by clicking on the stage. If the SWF for this example is running in Flash Player 9.0.115.0 or later, then it will use hardware acceleration to improve the full-screen scaling of the display.
   * <p>Before using the full-screen mode with hardware scaling, the following conditions must be met:</p><ol>
   * <li>Flash Player version 9.0.115.0 or later is needed, as well as an authoring tool that supports it.</li>
   * <li>HTML templates need to be modified to support full screen. The <code>allowFullScreen</code> attribute must be set to <code>true</code> for the <code>object</code> and <code>embed</code> tag. (The scripts that generate SWF-embedding tags must also allow for full screen.) For sample of files that can be used for Flash Builder, see the article, <a href="http://www.adobe.com/devnet/flashplayer/articles/full_screen_mode.html">Exploring full-screen mode in Flash Player 9</a>.</li>
   * <li>Your application must have permission and access to an FLV video file. In this example, it is assumed that Flash Video (FLV) file is in the same directory as the SWF file.</li>
   * <li>The user must allow access to full screen.</li>
   * <li>For additional information on hardware scaling, see the article <a href="http://www.adobe.com/go/hardware_scaling_en">Exploring Flash Player support for high-definition H.264 video and AAC audio</a> for Flash Player.</li></ol>
   * <p>An FLV file is loaded using NetConnection and NetStream objects. Since the FLV file is in the same directory as the SWF file and will connect via HTTP, the <code>NetConnection.connect()</code> method's parameter is set to <code>null</code>. The <code>connect</code> NetConnection object reports its status by dispatching a <code>netStatus</code> event which invokes the <code>netStatusHandler()</code> method. The <code>netStatusHandler()</code> method checks if the connection was successful and invokes <code>connectStream()</code> method, which creates a NetStream object that takes the NetConnection object as a parameter. It also creates a video object and attached the NetStream object to the video object. The video object then is added to the display list and the stream is set to play. Since the FLV video file does not contain metadata or cue point information, an <code>AsyncError</code> event will be dispatched. A listener must be set up to handle the event. Here the listener is set up and it ignores the event. Another listener for <code>netStatus</code> event is also set up for the NetStream object. It will display an error message if the stream was not found. (Note that <code>netStatusHandler()</code> could be used to handle any number of different status information reported for the stream or connection.)</p>
   * <p>When the properties and methods of a loaded SWF file are accessible, the <code>createMouseListener()</code> method is invoked. It sets up an event listener for when the mouse is clicked on the stage. The <code>toggleFullScreen()</code> method checks if the display state is in the full or normal screen mode. If it is normal, the size of the video object is set to the size of the video stream. The <code>fullScreenSourceRect</code> property is set to a rectangle matching the dimensions of the video object. Then the <code>Stage.displayMode</code> property is set to full screen, which causes the video in the source rectangle to expand to fill the full screen area. If system requirements are met, the machine's graphics hardware will be used to improve the performance of the full-screen video rendering and the display state is set to full-screen mode. In order to catch any security error that may occur while switching to the full-screen mode, a <code>try...catch</code> is used. (Note that the display state must be set to full-screen mode after the <code>fullScreenSourceRect</code> property is set.) Before switching to the normal-screen mode, the video object's width and height are set back to the saved original video object's width and height. Otherwise, the changes made to the video object for the full-screen mode will determine the width and height.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.display.StageDisplayState;
   *     import flash.media.Video;
   *     import flash.net.NetConnection;
   *     import flash.net.NetStream;
   *     import flash.events.NetStatusEvent;
   *     import flash.events.AsyncErrorEvent;
   *     import flash.events.SecurityErrorEvent;
   *     import flash.events.MouseEvent;
   *     import flash.events.Event;
   *     import flash.geom.Rectangle;
   *
   *     public class Stage_fullScreenSourceRectExample2 extends Sprite {
   *         private var videoURL:String = "testVideo1.flv";
   *         private var connection:NetConnection;
   *         private var stream:NetStream;
   *         private var myVideo:Video;
   *         private    var savedWidth:uint;
   *         private    var savedHeight:uint;
   *
   *         public function Stage_fullScreenSourceRectExample2() {
   *
   *             connection = new NetConnection();
   *              connection.addEventListener(NetStatusEvent.NET_STATUS, netStatusHandler);
   *             connection.addEventListener(SecurityErrorEvent.SECURITY_ERROR, securityErrorHandler);
   *             connection.connect(null);
   *
   *             loaderInfo.addEventListener(Event.INIT, createMouseListener);
   *         }
   *
   *         private function createMouseListener(event:Event):void {
   *             stage.addEventListener(MouseEvent.CLICK, toggleFullScreen);
   *         }
   *
   *         private function toggleFullScreen(event:MouseEvent):void {
   *
   *             if(stage.displayState == StageDisplayState.NORMAL) {
   *                 myVideo.width = myVideo.videoWidth;
   *                   myVideo.height = myVideo.videoHeight;
   *
   *                 try {
   *                     stage.fullScreenSourceRect = new Rectangle(myVideo.x, myVideo.y,
   *                                                            myVideo.width, myVideo.height);
   *                      stage.displayState = StageDisplayState.FULL_SCREEN;
   *
   *                  } catch (e:SecurityError) {
   *                      trace ("A security error occurred while switching to full screen: " + event);
   *                     myVideo.width = savedWidth;
   *                     myVideo.height = savedHeight;
   *                  }
   *
   *             }else {
   *                 myVideo.width = savedWidth;
   *                 myVideo.height = savedHeight;
   *                 stage.displayState = StageDisplayState.NORMAL;
   *             }
   *         }
   *
   *        private function netStatusHandler(event:NetStatusEvent):void {
   *             switch (event.info.code) {
   *                 case "NetConnection.Connect.Success":
   *                     connectStream();
   *                     break;
   *                 case "NetStream.Play.StreamNotFound":
   *                     trace ("Unable to locate video: " + videoURL);
   *                     break;
   *             }
   *         }
   *
   *        private function connectStream():void {
   *             var stream:NetStream = new NetStream(connection);
   *             stream.addEventListener(NetStatusEvent.NET_STATUS, netStatusHandler);
   *             stream.addEventListener(AsyncErrorEvent.ASYNC_ERROR, asyncErrorHandler);
   *
   *              myVideo = new Video();
   *             myVideo.attachNetStream(stream);
   *             stream.play(videoURL);
   *
   *             savedWidth = myVideo.width;
   *             savedHeight = myVideo.height;
   *
   *             addChild(myVideo);
   *         }
   *
   *         private function securityErrorHandler(event:SecurityErrorEvent):void {
   *             trace("securityErrorHandler: " + event);
   *         }
   *
   *         private function asyncErrorHandler(event:AsyncErrorEvent):void {
   *
   *         }
   *     }
   * }
   * </listing></div>
   */
  "public function get fullScreenSourceRect",function fullScreenSourceRect$get()/*:Rectangle*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set fullScreenSourceRect",function fullScreenSourceRect$set(value/*:Rectangle*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns the width of the monitor that will be used when going to full screen size, if that state is entered immediately. If the user has multiple monitors, the monitor that's used is the monitor that most of the stage is on at the time.
   * <p><b>Note</b>: If the user has the opportunity to move the browser from one monitor to another between retrieving the value and going to full screen size, the value could be incorrect. If you retrieve the value in an event handler that sets <code>Stage.displayState</code> to <code>StageDisplayState.FULL_SCREEN</code>, the value will be correct.</p>
   * <p>This is the pixel width of the monitor and is the same as the stage width would be if <code>Stage.align</code> is set to <code>StageAlign.TOP_LEFT</code> and <code>Stage.scaleMode</code> is set to <code>StageScaleMode.NO_SCALE</code>.</p>
   * @see #displayState
   * @see #fullScreenHeight
   * @see #fullScreenSourceRect
   * @see #scaleMode
   * @see StageDisplayState
   * @see flash.events.Event#RESIZE
   * @see flash.events.FullScreenEvent
   *
   * @example This example creates a green rectangle the size of the stage and places a red square on it that it activates as a button. Clicking the red square triggers the <code>enterFullScreen()</code> event handler, which sets the <code>fullScreenSourceRect</code> property and enters full screen mode. To set the <code>fullScreenSourceRect</code> property, the event handler starts with the location and dimensions of the red square. It then compares the aspect ratio (width divided by height) of the red square to the aspect ratio of the stage at full screen width and height so that it can expand the rectangle (<code>fullScreenSourceRect</code>) to match the screen's aspect ratio. The result is that the red square occupies the entire height of the monitor with the green background visible on each side. If the aspect ratio was not matched, the stage background color, which is white by default, would show on each side instead of the green background.
   * <p><b>Note</b>: Test this example in the browser. In the Flash Publish Settings dialog, on the HTML tab, select the template Flash Only - Allow Full Screen. Specify the Flash Player version 9.0.115.0, and make sure the Flash and HTML formats are selected on the Formats tab. Then publish and open the resulting HTML file in the browser.</p>
   * <listing>
   * import flash.display.Sprite;
   * import flash.display.Stage;
   * import flash.display.StageDisplayState;
   * import flash.events.MouseEvent;
   * import flash.geom.Rectangle;
   *
   * // cover the stage with a green rectangle
   * var greenRect:Sprite = new Sprite();
   * greenRect.graphics.beginFill(0x00FF00);
   * greenRect.graphics.drawRect(0, 0, stage.stageWidth, stage.stageHeight);
   * addChild(greenRect);
   *
   * // create red square on stage, turn it into a button for going to full screen
   * var redSquare:Sprite = new Sprite();
   * redSquare.graphics.beginFill(0xFF0000);
   * redSquare.graphics.drawRect(0, 0, 300, 300);
   * redSquare.x = 50;
   * redSquare.y = 50;
   * redSquare.addEventListener(MouseEvent.CLICK, enterFullScreen);
   * redSquare.buttonMode = true;
   * addChild(redSquare);
   *
   * function enterFullScreen(e:MouseEvent):void
   * {
   *     // we will go to full screen zoomed in on the red square
   *     var redSquare:Sprite = e.target as Sprite;
   *     var fullScreenRect:Rectangle = new Rectangle(redSquare.x, redSquare.y, redSquare.width, redSquare.height);
   *
   *     // calculate aspect ratio of the red square
   *     var rectAspectRatio:Number = fullScreenRect.width / fullScreenRect.height;
   *
   *     // calculate aspect ratio of the screen
   *     var screenAspectRatio:Number = stage.fullScreenWidth / stage.fullScreenHeight;
   *
   *     // change the fullScreenRect so that it covers the entire screen, keeping it centered on the redSquare
   *     // try commenting out this section to see what happens if you do not fix the aspect ratio.
   *     if (rectAspectRatio > screenAspectRatio) {
   *          var newHeight:Number = fullScreenRect.width / screenAspectRatio;
   *          fullScreenRect.y -= ((newHeight - fullScreenRect.height) / 2);
   *         fullScreenRect.height = newHeight;
   *     } else if (rectAspectRatio < screenAspectRatio) {
   *         var newWidth:Number = fullScreenRect.height * screenAspectRatio;
   *         fullScreenRect.x -= ((newWidth - fullScreenRect.width) / 2);
   *         fullScreenRect.width = newWidth;
   *     }
   *
   *     // go to full screen
   *     stage.fullScreenSourceRect = fullScreenRect;
   *     stage.displayState = StageDisplayState.FULL_SCREEN;
   * }
   * </listing>
   */
  "public function get fullScreenWidth",function fullScreenWidth$get()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Indicates the height of the display object, in pixels. The height is calculated based on the bounds of the content of the display object. When you set the <code>height</code> property, the <code>scaleY</code> property is adjusted accordingly, as shown in the following code:
   * <listing>
   *     var rect:Shape = new Shape();
   *     rect.graphics.beginFill(0xFF0000);
   *     rect.graphics.drawRect(0, 0, 100, 100);
   *     trace(rect.scaleY) // 1;
   *     rect.height = 200;
   *     trace(rect.scaleY) // 2;</listing>
   * <p>Except for TextField and Video objects, a display object with no content (such as an empty sprite) has a height of 0, even if you try to set <code>height</code> to a different value.</p>
   * @throws SecurityError Referencing the <code>height</code> property of a Stage object throws an exception for any caller that is not in the same security sandbox as the Stage owner (the main SWF file). To avoid this, the Stage owner can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.
   * @throws flash.errors.IllegalOperationError It is always illegal to set the <code>height</code> property of a Stage object, even if the calling object is the Stage owner (the main SWF file).
   *
   */
  "override public function get height",function height$get()/*:Number*/ {
    return this._stageHeight$5;
  },

  /**
   * @private
   */
  "override public function set height",function height$set(value/*:Number*/)/*:void*/ {
    this.stageHeight = $$int(value);
  },

  /**
   * Determines whether or not the children of the object are mouse, or user input device, enabled. If an object is enabled, a user can interact with it by using a mouse or user input device. The default is <code>true</code>.
   * <p>This property is useful when you create a button with an instance of the Sprite class (instead of using the SimpleButton class). When you use a Sprite instance to create a button, you can choose to decorate the button by using the <code>addChild()</code> method to add additional Sprite instances. This process can cause unexpected behavior with mouse events because the Sprite instances you add as children can become the target object of a mouse event when you expect the parent instance to be the target object. To ensure that the parent instance serves as the target objects for mouse events, you can set the <code>mouseChildren</code> property of the parent instance to <code>false</code>.</p>
   * <p>No event is dispatched by setting this property. You must use the <code>addEventListener()</code> method to create interactive functionality.</p>
   * @throws SecurityError Referencing the <code>mouseChildren</code> property of a Stage object throws an exception for any caller that is not in the same security sandbox as the Stage owner (the main SWF file). To avoid this, the Stage owner can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.
   *
   */
  "override public function get mouseChildren",function mouseChildren$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "override public function set mouseChildren",function mouseChildren$set(value/*:Boolean*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns the number of children of this object.
   * @throws SecurityError Referencing the <code>numChildren</code> property of a Stage object throws an exception for any caller that is not in the same security sandbox as the Stage owner (the main SWF file). To avoid this, the Stage owner can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.
   *
   */
  "override public function get numChildren",function numChildren$get()/*:int*/ {
    return this.numChildren$5;
  },

  /**
   * A value from the StageQuality class that specifies which rendering quality is used. The following are valid values:
   * <ul>
   * <li><code>StageQuality.LOW</code>—Low rendering quality. Graphics are not anti-aliased, and bitmaps are not smoothed, but runtimes still use mip-mapping. This setting is not supported in Adobe AIR.</li>
   * <li><code>StageQuality.MEDIUM</code>—Medium rendering quality. Graphics are anti-aliased using a 2 x 2 pixel grid, bitmap smoothing is dependent on the <code>Bitmap.smoothing</code> setting. Runtimes use mip-mapping. This setting is suitable for movies that do not contain text. This setting is not supported in Adobe AIR.</li>
   * <li><code>StageQuality.HIGH</code>—High rendering quality. Graphics are anti-aliased using a 4 x 4 pixel grid, and bitmap smoothing is dependent on the <code>Bitmap.smoothing</code> setting. Runtimes use mip-mapping. This is the default rendering quality setting that Flash Player uses.</li>
   * <li><code>StageQuality.BEST</code>—Very high rendering quality. Graphics are anti-aliased using a 4 x 4 pixel grid. If <code>Bitmap.smoothing</code> is <code>true</code> the runtime uses a high quality downscale algorithm that produces fewer artifacts (however, using <code>StageQuality.BEST</code> with <code>Bitmap.smoothing</code> set to <code>true</code> slows performance significantly and is not a recommended setting).</li></ul>
   * <p>Higher quality settings produce better rendering of scaled bitmaps. However, higher quality settings are computationally more expensive. In particular, when rendering scaled video, using higher quality settings can reduce the frame rate.</p>
   * <p>For content running in Adobe AIR, <code>quality</code> can be set to <code>StageQuality.BEST</code> or <code>StageQuality.HIGH</code> (and the default value is <code>StageQuality.HIGH</code>). Attempting to set it to another value has no effect (and the property remains unchanged).</p>
   * <p>For content running in Adobe AIR, setting the <code>quality</code> property of one Stage object changes the rendering quality for all Stage objects (used by different NativeWindow objects).</p><b><i>Note:</i></b> The operating system draws the device fonts, which are therefore unaffected by the <code>quality</code> property.
   * @throws SecurityError Calling the <code>quality</code> property of a Stage object throws an exception for any caller that is not in the same security sandbox as the Stage owner (the main SWF file). To avoid this, the Stage owner can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.
   *
   * @see StageQuality
   * @see Bitmap#smoothing
   *
   */
  "public function get quality",function quality$get()/*:String*/ {
    return this._quality$5;
  },

  /**
   * @private
   */
  "public function set quality",function quality$set(value/*:String*/)/*:void*/ {
    this._quality$5 = value;
  },

  /**
   * A value from the StageScaleMode class that specifies which scale mode to use. The following are valid values:
   * <ul>
   * <li><code>StageScaleMode.EXACT_FIT</code>—The entire application is visible in the specified area without trying to preserve the original aspect ratio. Distortion can occur, and the application may appear stretched or compressed.</li>
   * <li><code>StageScaleMode.SHOW_ALL</code>—The entire application is visible in the specified area without distortion while maintaining the original aspect ratio of the application. Borders can appear on two sides of the application.</li>
   * <li><code>StageScaleMode.NO_BORDER</code>—The entire application fills the specified area, without distortion but possibly with some cropping, while maintaining the original aspect ratio of the application.</li>
   * <li><code>StageScaleMode.NO_SCALE</code>—The entire application is fixed, so that it remains unchanged even as the size of the player window changes. Cropping might occur if the player window is smaller than the content.</li></ul>
   * @throws SecurityError Calling the <code>scaleMode</code> property of a Stage object throws an exception for any caller that is not in the same security sandbox as the Stage owner (the main SWF file). To avoid this, the Stage owner can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.
   *
   * @see StageScaleMode
   *
   */
  "public function get scaleMode",function scaleMode$get()/*:String*/ {
    return this._scaleMode$5;
  },

  /**
   * @private
   */
  "public function set scaleMode",function scaleMode$set(value/*:String*/)/*:void*/ {
    this._scaleMode$5 = value;
  },

  /**
   * Specifies whether to show or hide the default items in the Flash runtime context menu.
   * <p>If the <code>showDefaultContextMenu</code> property is set to <code>true</code> (the default), all context menu items appear. If the <code>showDefaultContextMenu</code> property is set to <code>false</code>, only the Settings and About... menu items appear.</p>
   * @throws SecurityError Calling the <code>showDefaultContextMenu</code> property of a Stage object throws an exception for any caller that is not in the same security sandbox as the Stage owner (the main SWF file). To avoid this, the Stage owner can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.
   *
   */
  "public native function get showDefaultContextMenu"/*():Boolean;*/,

  /**
   * @private
   */
  "public native function set showDefaultContextMenu"/*(value:Boolean):void;*/,

  /**
   * Specifies whether or not objects display a glowing border when they have focus.
   * @throws SecurityError Calling the <code>stageFocusRect</code> property of a Stage object throws an exception for any caller that is not in the same security sandbox as the Stage owner (the main SWF file). To avoid this, the Stage owner can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.
   *
   */
  "public native function get stageFocusRect"/*():Boolean;*/, // TODO: implement!

  /**
   * @private
   */
  "public native function set stageFocusRect"/*(value:Boolean):void;*/, // TODO: implement!

  /**
   * The current height, in pixels, of the Stage.
   * <p>If the value of the <code>Stage.scaleMode</code> property is set to <code>StageScaleMode.NO_SCALE</code> when the user resizes the window, the Stage content maintains its size while the <code>stageHeight</code> property changes to reflect the new height size of the screen area occupied by the SWF file. (In the other scale modes, the <code>stageHeight</code> property always reflects the original height of the SWF file.) You can add an event listener for the <code>resize</code> event and then use the <code>stageHeight</code> property of the Stage class to determine the actual pixel dimension of the resized Flash runtime window. The event listener allows you to control how the screen content adjusts when the user resizes the window.</p>
   * <p>Air for TV devices have slightly different behavior than desktop devices when you set the <code>stageHeight</code> property. If the <code>Stage.scaleMode</code> property is set to <code>StageScaleMode.NO_SCALE</code> and you set the <code>stageHeight</code> property, the stage height does not change until the next frame of the SWF.</p>
   * <p><b>Note:</b> In an HTML page hosting the SWF file, both the <code>object</code> and <code>embed</code> tags' <code>height</code> attributes must be set to a percentage (such as <code>100%</code>), not pixels. If the settings are generated by JavaScript code, the <code>height</code> parameter of the <code>AC_FL_RunContent()</code> method must be set to a percentage, too. This percentage is applied to the <code>stageHeight</code> value.</p>
   * @throws SecurityError Calling the <code>stageHeight</code> property of a Stage object throws an exception for any caller that is not in the same security sandbox as the Stage owner (the main SWF file). To avoid this, the Stage owner can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.
   *
   * @see StageScaleMode
   *
   */
  "public function get stageHeight",function stageHeight$get()/*:int*/ {
    return this._stageHeight$5;
  },

  /**
   * @private
   */
  "public function set stageHeight",function stageHeight$set(value/*:int*/)/*:void*/ {
    this._stageHeight$5 = value;
    this.getElement().style.height = value + "px";
  },

  /**
   * Specifies the current width, in pixels, of the Stage.
   * <p>If the value of the <code>Stage.scaleMode</code> property is set to <code>StageScaleMode.NO_SCALE</code> when the user resizes the window, the Stage content maintains its defined size while the <code>stageWidth</code> property changes to reflect the new width size of the screen area occupied by the SWF file. (In the other scale modes, the <code>stageWidth</code> property always reflects the original width of the SWF file.) You can add an event listener for the <code>resize</code> event and then use the <code>stageWidth</code> property of the Stage class to determine the actual pixel dimension of the resized Flash runtime window. The event listener allows you to control how the screen content adjusts when the user resizes the window.</p>
   * <p>Air for TV devices have slightly different behavior than desktop devices when you set the <code>stageWidth</code> property. If the <code>Stage.scaleMode</code> property is set to <code>StageScaleMode.NO_SCALE</code> and you set the <code>stageWidth</code> property, the stage width does not change until the next frame of the SWF.</p>
   * <p><b>Note:</b> In an HTML page hosting the SWF file, both the <code>object</code> and <code>embed</code> tags' <code>width</code> attributes must be set to a percentage (such as <code>100%</code>), not pixels. If the settings are generated by JavaScript code, the <code>width</code> parameter of the <code>AC_FL_RunContent()</code> method must be set to a percentage, too. This percentage is applied to the <code>stageWidth</code> value.</p>
   * @throws SecurityError Calling the <code>stageWidth</code> property of a Stage object throws an exception for any caller that is not in the same security sandbox as the Stage owner (the main SWF file). To avoid this, the Stage owner can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.
   *
   * @see StageScaleMode
   *
   */
  "public function get stageWidth",function stageWidth$get()/*:int*/ {
    return this._stageWidth$5;
  },

  /**
   * @private
   */
  "public function set stageWidth",function stageWidth$set(value/*:int*/)/*:void*/ {
    this._stageWidth$5 = value;
    this.getElement().style.width = value + "px";
  },

  /**
   * Determines whether the children of the object are tab enabled. Enables or disables tabbing for the children of the object. The default is <code>true</code>.
   * <p><b>Note:</b> Do not use the <code>tabChildren</code> property with Flex. Instead, use the <code>mx.core.UIComponent.hasFocusableChildren</code> property.</p>
   * @throws SecurityError Referencing the <code>tabChildren</code> property of a Stage object throws an exception for any caller that is not in the same security sandbox as the Stage owner (the main SWF file). To avoid this, the Stage owner can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.
   *
   */
  "override public function get tabChildren",function tabChildren$get()/*:Boolean*/ {
    return this.tabChildren$5;
  },

  /**
   * @private
   */
  "override public function set tabChildren",function tabChildren$set(value/*:Boolean*/)/*:void*/ {
    this.tabChildren$5 = value;
  },

  /**
   * Returns a TextSnapshot object for this DisplayObjectContainer instance.
   * @throws flash.errors.IllegalOperationError Referencing the <code>textSnapshot</code> property of a Stage object throws an exception because the Stage class does not implement this property. To avoid this, call the <code>textSnapshot</code> property of a display object container other than the Stage object.
   *
   */
  "override public function get textSnapshot",function textSnapshot$get()/*:TextSnapshot*/ {
    return this.textSnapshot$5;
  },

  /**
   * Indicates the width of the display object, in pixels. The width is calculated based on the bounds of the content of the display object. When you set the <code>width</code> property, the <code>scaleX</code> property is adjusted accordingly, as shown in the following code:
   * <listing>
   *     var rect:Shape = new Shape();
   *     rect.graphics.beginFill(0xFF0000);
   *     rect.graphics.drawRect(0, 0, 100, 100);
   *     trace(rect.scaleX) // 1;
   *     rect.width = 200;
   *     trace(rect.scaleX) // 2;</listing>
   * <p>Except for TextField and Video objects, a display object with no content (such as an empty sprite) has a width of 0, even if you try to set <code>width</code> to a different value.</p>
   * @throws SecurityError Referencing the <code>width</code> property of a Stage object throws an exception for any caller that is not in the same security sandbox as the Stage owner (the main SWF file). To avoid this, the Stage owner can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.
   * @throws flash.errors.IllegalOperationError It is always illegal to set the <code>width</code> property of a Stage object, even if you are the Stage owner.
   *
   */
  "override public function get width",function width$get()/*:Number*/ {
    return this._stageWidth$5;
  },

  /**
   * @private
   */
  "override public function set width",function width$set(value/*:Number*/)/*:void*/ {
    this.stageWidth = $$int(value);
  },

  /**
   * Adds a child DisplayObject instance to this DisplayObjectContainer instance. The child is added to the front (top) of all other children in this DisplayObjectContainer instance. (To add a child to a specific index position, use the <code>addChildAt()</code> method.)
   * <p>If you add a child object that already has a different display object container as a parent, the object is removed from the child list of the other display object container.</p>
   * <p><b>Note:</b> The command <code>stage.addChild()</code> can cause problems with a published SWF file, including security problems and conflicts with other loaded SWF files. There is only one Stage within a Flash runtime instance, no matter how many SWF files you load into the runtime. So, generally, objects should not be added to the Stage, directly, at all. The only object the Stage should contain is the root object. Create a DisplayObjectContainer to contain all of the items on the display list. Then, if necessary, add that DisplayObjectContainer instance to the Stage.</p>
   * @param child The DisplayObject instance to add as a child of this DisplayObjectContainer instance.
   *
   * @return The DisplayObject instance that you pass in the <code>child</code> parameter.
   *
   * @throws SecurityError Calling the <code>addChild()</code> method of a Stage object throws an exception for any caller that is not in the same security sandbox as the Stage owner (the main SWF file). To avoid this, the Stage owner can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e3d.html Working with display objects
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7dff.html Adding display objects to the display list
   *
   */
  "override public function addChild",function addChild(child/*:DisplayObject*/)/*:DisplayObject*/ {
    return this.addChild$5(child);
  },

  /**
   * Adds a child DisplayObject instance to this DisplayObjectContainer instance. The child is added at the index position specified. An index of 0 represents the back (bottom) of the display list for this DisplayObjectContainer object.
   * <p>For example, the following example shows three display objects, labeled a, b, and c, at index positions 0, 2, and 1, respectively:</p>
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/DisplayObjectContainer_layers.jpg" /></p>
   * <p>If you add a child object that already has a different display object container as a parent, the object is removed from the child list of the other display object container.</p>
   * @param child The DisplayObject instance to add as a child of this DisplayObjectContainer instance.
   * @param index The index position to which the child is added. If you specify a currently occupied index position, the child object that exists at that position and all higher positions are moved up one position in the child list.
   *
   * @return The DisplayObject instance that you pass in the <code>child</code> parameter.
   *
   * @throws SecurityError Calling the <code>addChildAt()</code> method of a Stage object throws an exception for any caller that is not in the same security sandbox as the Stage owner (the main SWF file). To avoid this, the Stage owner can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e3d.html Working with display objects
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7dff.html Adding display objects to the display list
   *
   */
  "override public function addChildAt",function addChildAt(child/*:DisplayObject*/, index/*:int*/)/*:DisplayObject*/ {
    return this.addChildAt$5(child, index);
  },

  /**
   * Registers an event listener object with an EventDispatcher object so that the listener receives notification of an event. You can register event listeners on all nodes in the display list for a specific type of event, phase, and priority.
   * <p>After you successfully register an event listener, you cannot change its priority through additional calls to <code>addEventListener()</code>. To change a listener's priority, you must first call <code>removeListener()</code>. Then you can register the listener again with the new priority level.</p>
   * <p>Keep in mind that after the listener is registered, subsequent calls to <code>addEventListener()</code> with a different <code>type</code> or <code>useCapture</code> value result in the creation of a separate listener registration. For example, if you first register a listener with <code>useCapture</code> set to <code>true</code>, it listens only during the capture phase. If you call <code>addEventListener()</code> again using the same listener object, but with <code>useCapture</code> set to <code>false</code>, you have two separate listeners: one that listens during the capture phase and another that listens during the target and bubbling phases.</p>
   * <p>You cannot register an event listener for only the target phase or the bubbling phase. Those phases are coupled during registration because bubbling applies only to the ancestors of the target node.</p>
   * <p>If you no longer need an event listener, remove it by calling <code>removeEventListener()</code>, or memory problems could result. Event listeners are not automatically removed from memory because the garbage collector does not remove the listener as long as the dispatching object exists (unless the <code>useWeakReference</code> parameter is set to <code>true</code>).</p>
   * <p>Copying an EventDispatcher instance does not copy the event listeners attached to it. (If your newly created node needs an event listener, you must attach the listener after creating the node.) However, if you move an EventDispatcher instance, the event listeners attached to it move along with it.</p>
   * <p>If the event listener is being registered on a node while an event is being processed on this node, the event listener is not triggered during the current phase but can be triggered during a later phase in the event flow, such as the bubbling phase.</p>
   * <p>If an event listener is removed from a node while an event is being processed on the node, it is still triggered by the current actions. After it is removed, the event listener is never invoked again (unless registered again for future processing).</p>
   * @param type The type of event.
   * @param listener The listener function that processes the event. This function must accept an Event object as its only parameter and must return nothing, as this example shows:
   * <listing>
   * function(evt:Event):void</listing>
   * <p>The function can have any name.</p>
   * @param useCapture Determines whether the listener works in the capture phase or the target and bubbling phases. If <code>useCapture</code> is set to <code>true</code>, the listener processes the event only during the capture phase and not in the target or bubbling phase. If <code>useCapture</code> is <code>false</code>, the listener processes the event only during the target or bubbling phase. To listen for the event in all three phases, call <code>addEventListener</code> twice, once with <code>useCapture</code> set to <code>true</code>, then again with <code>useCapture</code> set to <code>false</code>.
   * @param priority The priority level of the event listener. The priority is designated by a signed 32-bit integer. The higher the number, the higher the priority. All listeners with priority <i>n</i> are processed before listeners of priority <i>n</i>-1. If two or more listeners share the same priority, they are processed in the order in which they were added. The default priority is 0.
   * @param useWeakReference Determines whether the reference to the listener is strong or weak. A strong reference (the default) prevents your listener from being garbage-collected. A weak reference does not.
   * <p>Class-level member functions are not subject to garbage collection, so you can set <code>useWeakReference</code> to <code>true</code> for class-level member functions without subjecting them to garbage collection. If you set <code>useWeakReference</code> to <code>true</code> for a listener that is a nested inner function, the function will be garbage-collected and no longer persistent. If you create references to the inner function (save it in another variable) then it is not garbage-collected and stays persistent.</p>
   *
   * @throws SecurityError Calling the <code>addEventListener</code> method of a Stage object throws an exception for any caller that is not in the same security sandbox as the Stage owner (the main SWF file). To avoid this situation, the Stage owner can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e3d.html Working with display objects
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7dfb.html Handling events for display objects
   *
   */
  "override public function addEventListener",function addEventListener(type/*:String*/, listener/*:Function*/, useCapture/*:Boolean = false*/, priority/*:int = 0*/, useWeakReference/*:Boolean = false*/)/*:void*/ {if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){useCapture = false;}priority = 0;}useWeakReference = false;}
    this.addEventListener$5(type, listener, useCapture, priority, useWeakReference);
  },

  /**
   * Dispatches an event into the event flow. The event target is the EventDispatcher object upon which the <code>dispatchEvent()</code> method is called.
   * @param event The Event object that is dispatched into the event flow. If the event is being redispatched, a clone of the event is created automatically. After an event is dispatched, its <code>target</code> property cannot be changed, so you must create a new copy of the event for redispatching to work.
   *
   * @return A value of <code>true</code> if the event was successfully dispatched. A value of <code>false</code> indicates failure or that <code>preventDefault()</code> was called on the event.
   *
   * @throws SecurityError Calling the <code>dispatchEvent()</code> method of a Stage object throws an exception for any caller that is not in the same security sandbox as the Stage owner (the main SWF file). To avoid this, the Stage owner can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.
   *
   */
  "override public function dispatchEvent",function dispatchEvent(event/*:flash.events.Event*/)/*:Boolean*/ {
    return this.dispatchEvent$5(event);
  },

  /**
   * Checks whether the EventDispatcher object has any listeners registered for a specific type of event. This allows you to determine where an EventDispatcher object has altered handling of an event type in the event flow hierarchy. To determine whether a specific event type actually triggers an event listener, use <code>willTrigger()</code>.
   * <p>The difference between <code>hasEventListener()</code> and <code>willTrigger()</code> is that <code>hasEventListener()</code> examines only the object to which it belongs, whereas <code>willTrigger()</code> examines the entire event flow for the event specified by the <code>type</code> parameter.</p>
   * <p>When <code>hasEventListener()</code> is called from a LoaderInfo object, only the listeners that the caller can access are considered.</p>
   * @param type The type of event.
   *
   * @return A value of <code>true</code> if a listener of the specified type is registered; <code>false</code> otherwise.
   *
   * @throws SecurityError Calling the <code>hasEventListener()</code> method of a Stage object throws an exception for any caller that is not in the same security sandbox as the Stage owner (the main SWF file). To avoid this, the Stage owner can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.
   *
   */
  "override public function hasEventListener",function hasEventListener(type/*:String*/)/*:Boolean*/ {
    return this.hasEventListener$5(type);
  },

  /**
   * Calling the <code>invalidate()</code> method signals Flash runtimes to alert display objects on the next opportunity it has to render the display list (for example, when the playhead advances to a new frame). After you call the <code>invalidate()</code> method, when the display list is next rendered, the Flash runtime sends a <code>render</code> event to each display object that has registered to listen for the <code>render</code> event. You must call the <code>invalidate()</code> method each time you want the Flash runtime to send <code>render</code> events.
   * <p>The <code>render</code> event gives you an opportunity to make changes to the display list immediately before it is actually rendered. This lets you defer updates to the display list until the latest opportunity. This can increase performance by eliminating unnecessary screen updates.</p>
   * <p>The <code>render</code> event is dispatched only to display objects that are in the same security domain as the code that calls the <code>stage.invalidate()</code> method, or to display objects from a security domain that has been granted permission via the <code>Security.allowDomain()</code> method.</p>
   * @see flash.events.Event#RENDER
   *
   */
  "public function invalidate",function invalidate()/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Determines whether the <code>Stage.focus</code> property returns <code>null</code> for security reasons. In other words, <code>isFocusInaccessible</code> returns <code>true</code> if the object that has focus belongs to a security sandbox to which the SWF file does not have access.
   * @return <code>true</code> if the object that has focus belongs to a security sandbox to which the SWF file does not have access.
   *
   */
  "public function isFocusInaccessible",function isFocusInaccessible()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Removes a child DisplayObject from the specified <code>index</code> position in the child list of the DisplayObjectContainer. The <code>parent</code> property of the removed child is set to <code>null</code>, and the object is garbage collected if no other references to the child exist. The index positions of any display objects above the child in the DisplayObjectContainer are decreased by 1.
   * <p>The garbage collector reallocates unused memory space. When a variable or object is no longer actively referenced or stored somewhere, the garbage collector sweeps through and wipes out the memory space it used to occupy if no other references to it exist.</p>
   * @param index The child index of the DisplayObject to remove.
   *
   * @return The DisplayObject instance that was removed.
   *
   * @throws SecurityError Calling the <code>removeChildAt()</code> method of a Stage object throws an exception for any caller that is not in the same security sandbox as the object to be removed. To avoid this, the owner of that object can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.
   *
   */
  "override public function removeChildAt",function removeChildAt(index/*:int*/)/*:DisplayObject*/ {
    return this.removeChildAt$5(index);
  },

  /**
   * Changes the position of an existing child in the display object container. This affects the layering of child objects. For example, the following example shows three display objects, labeled a, b, and c, at index positions 0, 1, and 2, respectively:
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/DisplayObjectContainerSetChildIndex1.jpg" /></p>
   * <p>When you use the <code>setChildIndex()</code> method and specify an index position that is already occupied, the only positions that change are those in between the display object's former and new position. All others will stay the same. If a child is moved to an index LOWER than its current index, all children in between will INCREASE by 1 for their index reference. If a child is moved to an index HIGHER than its current index, all children in between will DECREASE by 1 for their index reference. For example, if the display object container in the previous example is named <code>container</code>, you can swap the position of the display objects labeled a and b by calling the following code:</p>
   * <listing>
   * container.setChildIndex(container.getChildAt(1), 0);</listing>
   * <p>This code results in the following arrangement of objects:</p>
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/DisplayObjectContainerSetChildIndex2.jpg" /></p>
   * @param child The child DisplayObject instance for which you want to change the index number.
   * @param index The resulting index number for the <code>child</code> display object.
   *
   * @throws SecurityError Calling the <code>setChildIndex()</code> method of a Stage object throws an exception for any caller that is not in the same security sandbox as the Stage owner (the main SWF file). To avoid this, the Stage owner can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.
   *
   */
  "override public function setChildIndex",function setChildIndex(child/*:DisplayObject*/, index/*:int*/)/*:void*/ {
    this.setChildIndex$5(child, index);
  },

  /**
   * Swaps the z-order (front-to-back order) of the two specified child objects. All other child objects in the display object container remain in the same index positions.
   * @param child1 The first child object.
   * @param child2 The second child object.
   *
   * @throws SecurityError Calling the <code>swapChildrenAt()</code> method of a Stage object throws an exception for any caller that is not in the same security sandbox as the owner of either of the objects to be swapped. To avoid this, the object owners can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.
   *
   */
  "override public function swapChildren",function swapChildren(child1/*:DisplayObject*/, child2/*:DisplayObject*/)/*:void*/ {
    this.swapChildren$5(child1, child2);
  },

  /**
   * Checks whether an event listener is registered with this EventDispatcher object or any of its ancestors for the specified event type. This method returns <code>true</code> if an event listener is triggered during any phase of the event flow when an event of the specified type is dispatched to this EventDispatcher object or any of its descendants.
   * <p>The difference between the <code>hasEventListener()</code> and the <code>willTrigger()</code> methods is that <code>hasEventListener()</code> examines only the object to which it belongs, whereas the <code>willTrigger()</code> method examines the entire event flow for the event specified by the <code>type</code> parameter.</p>
   * <p>When <code>willTrigger()</code> is called from a LoaderInfo object, only the listeners that the caller can access are considered.</p>
   * @param type The type of event.
   *
   * @return A value of <code>true</code> if a listener of the specified type will be triggered; <code>false</code> otherwise.
   *
   * @throws SecurityError Calling the <code>willTrigger()</code> method of a Stage object throws an exception for any caller that is not in the same security sandbox as the Stage owner (the main SWF file). To avoid this, the Stage owner can grant permission to the domain of the caller by calling the <code>Security.allowDomain()</code> method or the <code>Security.allowInsecureDomain()</code> method. For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.
   *
   */
  "override public function willTrigger",function willTrigger(type/*:String*/)/*:Boolean*/ {
    return this.willTrigger$5(type);
  },

  // ************************** Jangaroo part **************************

  /**
   * @inheritDoc
   */
  "override public function get stage",function stage$get()/*:Stage*/ {
    return this;
  },

  /**
   * @private
   */
  "public function Stage",function Stage$(id/* : String*/, properties/* : Object*/) {
    this.id$5 = id;
    if (properties) {
      for (var m/*:String*/ in properties) {
        this[m] = properties[m];
      }
    }
    this.super$5();this._quality$5=this._quality$5();this._scaleMode$5=this._scaleMode$5();this._align$5=this._align$5();
    this.frameTimer$5 = new flash.utils.Timer(1000/this._frameRate$5);
    this.frameTimer$5.addEventListener(flash.events.TimerEvent.TIMER, $$bound(this,"enterFrame$5"));
    this.frameTimer$5.start();
  },

  /**
   * @private
   */
  "public function set backgroundColor",function backgroundColor$set(value/*:**/)/*:void*/ {
    if (typeof value == 'string') {
      value = String(value).replace(/^#/, "0x");
    }
    this.getElement().style.backgroundColor = flash.display.Graphics.toRGBA($$uint(value));
  },

  /**
   * @private
   */
  "override protected function createElement",function createElement()/*:HTMLElement*/ {var this$=this;var this$=this;var this$=this;
    var element/* : HTMLElement*/ =/* js.HTMLElement*/(window.document.getElementById(this.id$5));
    element.style.position = "relative";
    element.style.overflow = "hidden";
    element.setAttribute("tabindex", "0");
    element.style.margin = "0";
    element.style.padding = "0";
    var width/* : Object*/ = element.getAttribute("width");
    if (!width) {
      width = this.width;
    }
    element.style.width = width+"px";
    var height/* : Object*/ = element.getAttribute("height");
    if (!height) {
      height = this.height;
    }
    element.style.height = height + "px";
    element.innerHTML = "";
    element.addEventListener('mousedown', function flash$display$Stage$1018_43()/*:void*/ {
      // TODO: check event.button property whether it was the "primary" mouse button!
      this$.buttonDown = true;
    }, true);
    element.addEventListener('mouseup', function flash$display$Stage$1022_41()/*:void*/ {
      // TODO: check event.button property whether it was the "primary" mouse button!
      this$.buttonDown = false;
    }, true);
    element.addEventListener('mousemove', function flash$display$Stage$1026_43(e/*:js.Event*/)/*:void*/ {
      this$._mouseX$5 = e.clientX;
      this$._mouseY$5 = e.clientY;
    }, true);
    return element;
  },

  /**
   * @inheritDoc
   */
  "override public function get mouseX",function mouseX$get()/*:Number*/ {
    return this._mouseX$5;
  },

  /**
   * @inheritDoc
   */
  "override public function get mouseY",function mouseY$get()/*:Number*/ {
    return this._mouseY$5;
  },

  "private function enterFrame",function enterFrame()/* : void*/ {
    this.broadcastEvent(new flash.events.Event(flash.events.Event.ENTER_FRAME, false, false));
  },

  "private var",{ _stageHeight/*:int*/:0},
  "private var",{ _stageWidth/*:int*/:0},
  "private var",{ _mouseX/*:int*/:0},
  "private var",{ _mouseY/*:int*/:0},
  "private var",{ id/* : String*/:null},
  "private var",{ _frameRate/* : Number*/ : 30},
  "private var",{ frameTimer/* : Timer*/:null},
  "private var",{ _quality/* : String*/ :function(){return( flash.display.StageQuality.HIGH);}},
  "private var",{ _scaleMode/* : String*/ :function(){return( flash.display.StageScaleMode.NO_SCALE);}},
  "private var",{ _align/* : String*/ :function(){return( flash.display.StageAlign.TOP_LEFT);}},
  "internal var",{ buttonDown/*:Boolean*/ : false},
];},[],["flash.display.DisplayObjectContainer","Error","Number","int","flash.utils.Timer","flash.events.TimerEvent","String","flash.display.Graphics","uint","js.HTMLElement","flash.events.Event","flash.display.StageQuality","flash.display.StageScaleMode","flash.display.StageAlign"], "0.8.0", "0.8.1"
);