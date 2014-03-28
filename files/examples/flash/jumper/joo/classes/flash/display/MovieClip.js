joo.classLoader.prepare("package flash.display",/* {*/


/**
 * The MovieClip class inherits from the following classes: Sprite, DisplayObjectContainer, InteractiveObject, DisplayObject, and EventDispatcher.
 * <p>Unlike the Sprite object, a MovieClip object has a timeline.</p>
 * <p>>In Flash Professional, the methods for the MovieClip class provide the same functionality as actions that target movie clips. Some additional methods do not have equivalent actions in the Actions toolbox in the Actions panel in the Flash authoring tool.</p>
 * <p>Children instances placed on the Stage in Flash Professional cannot be accessed by code from within the constructor of a parent instance since they have not been created at that point in code execution. Before accessing the child, the parent must instead either create the child instance by code or delay access to a callback function that listens for the child to dispatch its <code>Event.ADDED_TO_STAGE</code> event.</p>
 * <p>If you modify any of the following properties of a MovieClip object that contains a motion tween, the playhead is stopped in that MovieClip object: <code>alpha</code>, <code>blendMode</code>, <code>filters</code>, <code>height</code>, <code>opaqueBackground</code>, <code>rotation</code>, <code>scaleX</code>, <code>scaleY</code>, <code>scale9Grid</code>, <code>scrollRect</code>, <code>transform</code>, <code>visible</code>, <code>width</code>, <code>x</code>, or <code>y</code>. However, it does not stop the playhead in any child MovieClip objects of that MovieClip object.</p>
 * <p><b>Note:</b>Flash Lite 4 supports the MovieClip.opaqueBackground property only if FEATURE_BITMAPCACHE is defined. The default configuration of Flash Lite 4 does not define FEATURE_BITMAPCACHE. To enable the MovieClip.opaqueBackground property for a suitable device, define FEATURE_BITMAPCACHE in your project.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/display/MovieClip.html#includeExamplesSummary">View the examples</a></p>
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e3c.html Core display classes
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7d9c.html Basics of movie clips
 *
 */
"public dynamic class MovieClip extends flash.display.Sprite",6,function($$private){;return[ 
  /**
   * Specifies the number of the frame in which the playhead is located in the timeline of the MovieClip instance. If the movie clip has multiple scenes, this value is the frame number in the current scene.
   * @example The following code uses the <code>gotoAndStop()</code> method and the <code>currentFrame</code> property to direct the playhead of the <code>mc1</code> movie clip to advance five frames ahead of its current location and stop:
   * <listing>
   * mc1.gotoAndStop(mc1.currentFrame + 5);
   * </listing>
   */
  "public function get currentFrame",function currentFrame$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The current label in which the playhead is located in the timeline of the MovieClip instance. If the current frame has no label, <code>currentLabel</code> is set to the name of the previous frame that includes a label. If the current frame and previous frames do not include a label, <code>currentLabel</code> returns <code>null</code>.
   * @example The following code illustrates how to access the <code>currentLabel</code> property of a MovieClip object named <code>mc1</code>:
   * <listing>
   * trace(mc1.currentLabel);
   * </listing>
   */
  "public function get currentLabel",function currentLabel$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns an array of FrameLabel objects from the current scene. If the MovieClip instance does not use scenes, the array includes all frame labels from the entire MovieClip instance.
   * @see FrameLabel
   *
   * @example The following code illustrates how to use the <code>currentLabels</code> property of a MovieClip object named <code>mc1</code>:
   * <listing>
   * import flash.display.FrameLabel;
   *
   * var labels:Array = mc1.currentLabels;
   *
   * for (var i:uint = 0; i < labels.length; i++) {
   *     var label:FrameLabel = labels[i];
   *     trace("frame " + label.frame + ": " + label.name);
   * }
   * </listing>
   */
  "public function get currentLabels",function currentLabels$get()/*:Array*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The current scene in which the playhead is located in the timeline of the MovieClip instance.
   * @see Scene
   *
   * @example The following code illustrates how to use the <code>currentScene</code> property of a MovieClip object named <code>mc1</code>:
   * <listing>
   * import flash.display.Scene;
   *
   * var scene:Scene = mc1.currentScene;
   * trace(scene.name + ": " + scene.numFrames + " frames");
   * </listing>
   */
  "public function get currentScene",function currentScene$get()/*:Scene*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * A Boolean value that indicates whether a movie clip is enabled. The default value of <code>enabled</code> is <code>true</code>. If <code>enabled</code> is set to <code>false</code>, the movie clip's Over, Down, and Up frames are disabled. The movie clip continues to receive events (for example, <code>mouseDown</code>, <code>mouseUp</code>, <code>keyDown</code>, and <code>keyUp</code>).
   * <p>The <code>enabled</code> property governs only the button-like properties of a movie clip. You can change the <code>enabled</code> property at any time; the modified movie clip is immediately enabled or disabled. If <code>enabled</code> is set to <code>false</code>, the object is not included in automatic tab ordering.</p>
   * @example The following code illustrates how to use the <code>enabled</code> property to disable the button-like properties of a MovieClip object named <code>mc1</code>:
   * <listing>
   * mc1.enabled = false;
   * </listing>
   */
  "public function get enabled",function enabled$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set enabled",function enabled$set(value/*:Boolean*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The number of frames that are loaded from a streaming SWF file. You can use the <code>framesLoaded</code> property to determine whether the contents of a specific frame and all the frames before it loaded and are available locally in the browser. You can also use it to monitor the downloading of large SWF files. For example, you might want to display a message to users indicating that the SWF file is loading until a specified frame in the SWF file finishes loading.
   * <p>If the movie clip contains multiple scenes, the <code>framesLoaded</code> property returns the number of frames loaded for <i>all</i> scenes in the movie clip.</p>
   * @see Loader
   *
   * @example The following code illustrates how to use the <code>framesLoaded</code> property and the <code>totalFrames</code> property to determine if a streaming MovieClip object named <code>mc1</code> is fully loaded:
   * <listing>
   * if (mc1.framesLoaded == mc1.totalFrames) {
   *     trace("OK.");
   * }
   * </listing>
   */
  "public function get framesLoaded",function framesLoaded$get()/*:int*/ {
    return -1; // TODO: implement!
  },

  /**
   * An array of Scene objects, each listing the name, the number of frames, and the frame labels for a scene in the MovieClip instance.
   * @see Scene
   *
   * @example The following code illustrates how to use the <code>scenes</code> property of a MovieClip object named <code>mc1</code>:
   * <listing>
   * import flash.display.Scene;
   *
   * for (var i:uint = 0; i < mc1.scenes.length; i++) {
   *     var scene:Scene = mc1.scenes[i];
   *     trace("scene " + scene.name + ": " + scene.numFrames + " frames");
   * }
   * </listing>
   */
  "public function get scenes",function scenes$get()/*:Array*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The total number of frames in the MovieClip instance.
   * <p>If the movie clip contains multiple frames, the <code>totalFrames</code> property returns the total number of frames in <i>all</i> scenes in the movie clip.</p>
   * @example The following code illustrates the use of the <code>totalFrames</code> property of a MovieClip object named <code>mc1</code>:
   * <listing>
   *  trace(mc1.totalFrames);
   * </listing>
   */
  "public function get totalFrames",function totalFrames$get()/*:int*/ {
    return -1; // TODO: implement!
  },

  /**
   * Indicates whether other display objects that are SimpleButton or MovieClip objects can receive mouse release events or other user input release events. The <code>trackAsMenu</code> property lets you create menus. You can set the <code>trackAsMenu</code> property on any SimpleButton or MovieClip object. The default value of the <code>trackAsMenu</code> property is <code>false</code>.
   * <p>You can change the <code>trackAsMenu</code> property at any time; the modified movie clip immediately uses the new behavior.</p>
   * @example The following code illustrates how to use the <code>trackAsMenu</code> property to enable mouse release events for a MovieClip object named <code>mc1</code>:
   * <listing>
   *  mc1.trackAsMenu = true;
   * </listing>
   */
  "public function get trackAsMenu",function trackAsMenu$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set trackAsMenu",function trackAsMenu$set(value/*:Boolean*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Creates a new MovieClip instance. After creating the MovieClip, call the <code>addChild()</code> or <code>addChildAt()</code> method of a display object container that is onstage.
   */
  "public function MovieClip",function MovieClip$() {this.super$6();
    // TODO: implement!
  },

  /**
   * Starts playing the SWF file at the specified frame. This happens after all remaining actions in the frame have finished executing. To specify a scene as well as a frame, specify a value for the <code>scene</code> parameter.
   * @param frame A number representing the frame number, or a string representing the label of the frame, to which the playhead is sent. If you specify a number, it is relative to the scene you specify. If you do not specify a scene, the current scene determines the global frame number to play. If you do specify a scene, the playhead jumps to the frame number in the specified scene.
   * @param scene The name of the scene to play. This parameter is optional.
   *
   * @example The following code uses the <code>gotoAndPlay()</code> method to direct the playhead of the <code>mc1</code> movie clip to advance five frames ahead of its current location:
   * <listing>
   * mc1.gotoAndPlay(mc1.currentFrame + 5);
   * </listing>
   * <div>The following code uses the <code>gotoAndPlay()</code> method to direct the playhead of the <code>mc1</code> movie clip to the frame labeled <code>"intro"</code> in the scene named <code>"Scene 12"</code>:
   * <listing>
   * mc1.gotoAndPlay("intro", "Scene 12");
   * </listing></div>
   */
  "public function gotoAndPlay",function gotoAndPlay(frame/*:Object*/, scene/*:String = null*/)/*:void*/ {switch(arguments.length){case 0:case 1:scene = null;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Brings the playhead to the specified frame of the movie clip and stops it there. This happens after all remaining actions in the frame have finished executing. If you want to specify a scene in addition to a frame, specify a <code>scene</code> parameter.
   * @param frame A number representing the frame number, or a string representing the label of the frame, to which the playhead is sent. If you specify a number, it is relative to the scene you specify. If you do not specify a scene, the current scene determines the global frame number at which to go to and stop. If you do specify a scene, the playhead goes to the frame number in the specified scene and stops.
   * @param scene The name of the scene. This parameter is optional.
   *
   * @throws ArgumentError If the <code>scene</code> or <code>frame</code> specified are not found in this movie clip.
   *
   * @example The following code uses the <code>gotoAndStop()</code> method and the <code>currentFrame</code> property to direct the playhead of the <code>mc1</code> movie clip to advance five frames ahead of its current location and stop:
   * <listing>
   * mc1.gotoAndStop(mc1.currentFrame + 5);
   * </listing>
   * <div>The following code uses the <code>gotoAndStop()</code> to direct the playhead of the <code>mc1</code> movie clip to the frame labeled <code>"finale"</code> in the scene named <code>"Scene 12"</code> and stop the playhead:
   * <listing>
   * mc1.gotoAndStop("finale", "Scene 12");
   * </listing></div>
   */
  "public function gotoAndStop",function gotoAndStop(frame/*:Object*/, scene/*:String = null*/)/*:void*/ {switch(arguments.length){case 0:case 1:scene = null;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Sends the playhead to the next frame and stops it. This happens after all remaining actions in the frame have finished executing.
   * @see #prevFrame()
   *
   * @example In the following example, two SimpleButton objects control the timeline. The <code>prev</code> button moves the playhead to the previous frame, and the <code>nextBtn</code> button moves the playhead to the next frame:
   * <listing>
   * import flash.events.MouseEvent;
   *
   * mc1.stop();
   * prevBtn.addEventListener(MouseEvent.CLICK, goBack);
   * nextBtn.addEventListener(MouseEvent.CLICK, goForward);
   *
   * function goBack(event:MouseEvent):void {
   *     mc1.prevFrame();
   * }
   *
   * function goForward(event:MouseEvent):void {
   *     mc1.nextFrame();
   * }
   * </listing>
   */
  "public function nextFrame",function nextFrame()/*:void*/ {
    // TODO: implement!
  },

  /**
   * Moves the playhead to the next scene of the MovieClip instance. This happens after all remaining actions in the frame have finished executing.
   * @example In the following example, two SimpleButton objects control the timeline. The <code>prevBtn</code> button moves the playhead to the previous scene, and the <code>nextBtn</code> button moves the playhead to the next scene:
   * <listing>
   * import flash.events.MouseEvent;
   *
   * mc1.stop();
   * prevBtn.addEventListener(MouseEvent.CLICK, goBack);
   * nextBtn.addEventListener(MouseEvent.CLICK, goForward);
   *
   * function goBack(event:MouseEvent):void {
   *     mc1.prevScene();
   * }
   *
   * function goForward(event:MouseEvent):void {
   *     mc1.nextScene();
   * }
   * </listing>
   */
  "public function nextScene",function nextScene()/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Moves the playhead in the timeline of the movie clip.
   * @see #gotoAndPlay()
   *
   * @example The following code uses the <code>stop()</code> method to stop a movie clip named <code>mc1</code> and to resume playing when the user clicks the text field named <code>continueText</code>:
   * <listing>
   * import flash.text.TextField;
   * import flash.events.MouseEvent;
   *
   * var continueText:TextField = new TextField();
   * continueText.text = "Play movie...";
   * addChild(continueText);
   *
   * mc1.stop();
   * continueText.addEventListener(MouseEvent.CLICK, resumeMovie);
   *
   * function resumeMovie(event:MouseEvent):void {
   *     mc1.play();
   * }
   * </listing>
   */
  "public function play",function play()/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Sends the playhead to the previous frame and stops it. This happens after all remaining actions in the frame have finished executing.
   * @example In the following example, two SimpleButton objects control the timeline. The <code>prev</code> button moves the playhead to the previous frame, and the <code>nextBtn</code> button moves the playhead to the next frame:
   * <listing>
   * import flash.events.MouseEvent;
   *
   * mc1.stop();
   * prevBtn.addEventListener(MouseEvent.CLICK, goBack);
   * nextBtn.addEventListener(MouseEvent.CLICK, goForward);
   *
   * function goBack(event:MouseEvent):void {
   *     mc1.prevFrame();
   * }
   *
   * function goForward(event:MouseEvent):void {
   *     mc1.nextFrame();
   * }
   * </listing>
   */
  "public function prevFrame",function prevFrame()/*:void*/ {
    // TODO: implement!
  },

  /**
   * Moves the playhead to the previous scene of the MovieClip instance. This happens after all remaining actions in the frame have finished executing.
   * @example In the following example, two SimpleButton objects control the timeline. The <code>prevBtn</code> button moves the playhead to the previous scene, and the <code>nextBtn</code> button moves the playhead to the next scene:
   * <listing>
   * import flash.events.MouseEvent;
   *
   * mc1.stop();
   * prevBtn.addEventListener(MouseEvent.CLICK, goBack);
   * nextBtn.addEventListener(MouseEvent.CLICK, goForward);
   *
   * function goBack(event:MouseEvent):void {
   *     mc1.prevScene();
   * }
   *
   * function goForward(event:MouseEvent):void {
   *     mc1.nextScene();
   * }
   * </listing>
   */
  "public function prevScene",function prevScene()/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Stops the playhead in the movie clip.
   */
  "public function stop",function stop()/*:void*/ {
    // TODO: implement!
  },
];},[],["flash.display.Sprite","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);