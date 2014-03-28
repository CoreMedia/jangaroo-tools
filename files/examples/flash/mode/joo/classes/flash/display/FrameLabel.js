joo.classLoader.prepare("package flash.display",/* {*/


/**
 * The FrameLabel object contains properties that specify a frame number and the corresponding label name. The Scene class includes a <code>labels</code> property, which is an array of FrameLabel objects for the scene.
 * @see Scene#labels
 * @see MovieClip#currentLabel
 * @see MovieClip#currentScene
 * @see MovieClip#scenes
 * @see MovieClip#gotoAndPlay()
 * @see MovieClip#gotoAndStop()
 *
 */
"public final class FrameLabel",1,function($$private){;return[ 
  /**
   * The frame number containing the label.
   */
    "public function get frame",function frame$get()/*:int*/ {
      return this._frame$1;
    },

  /**
   * The name of the label.
   */
  "public function get name",function name$get()/*:String*/ {
    return this._name$1;
  },

  // ************************** Jangaroo part **************************

  /**
   * @private
   */
  "public function FrameLabel",function FrameLabel$() {
  },

  "private var",{ _frame/*:int*/:0},
  "private var",{ _name/*:String*/:null},

];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);