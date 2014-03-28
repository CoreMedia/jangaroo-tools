joo.classLoader.prepare("package flash.display",/* {*/


/**
 * The Scene class includes properties for identifying the name, labels, and number of frames in a scene. A Scene object instance is created in Flash Professional, not by writing ActionScript code. The MovieClip class includes a <code>currentScene</code> property, which is a Scene object that identifies the scene in which the playhead is located in the timeline of the MovieClip instance. The <code>scenes</code> property of the MovieClip class is an array of Scene objects. Also, the <code>gotoAndPlay()</code> and <code>gotoAndStop()</code> methods of the MovieClip class use Scene objects as parameters.
 * @see MovieClip#currentScene
 * @see MovieClip#scenes
 * @see MovieClip#gotoAndPlay()
 * @see MovieClip#gotoAndStop()
 *
 */
"public final class Scene",1,function($$private){;return[ 
  /**
   * An array of FrameLabel objects for the scene. Each FrameLabel object contains a <code>frame</code> property, which specifies the frame number corresponding to the label, and a <code>name</code> property.
   * @see FrameLabel
   *
   */
  "public function get labels",function labels$get()/*:Array*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The name of the scene.
   */
  "public function get name",function name$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The number of frames in the scene.
   */
  "public function get numFrames",function numFrames$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);