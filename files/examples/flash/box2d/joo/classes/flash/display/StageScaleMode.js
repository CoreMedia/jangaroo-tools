joo.classLoader.prepare("package flash.display",/* {*/


/**
 * The StageScaleMode class provides values for the <code>Stage.scaleMode</code> property.
 * @see Stage#scaleMode
 *
 */
"public final class StageScaleMode",1,function($$private){;return[ 
  /**
   * Specifies that the entire application be visible in the specified area without trying to preserve the original aspect ratio. Distortion can occur.
   */
  "public static const",{ EXACT_FIT/*:String*/ : "exactFit"},
  /**
   * Specifies that the entire application fill the specified area, without distortion but possibly with some cropping, while maintaining the original aspect ratio of the application.
   */
  "public static const",{ NO_BORDER/*:String*/ : "noBorder"},
  /**
   * Specifies that the size of the application be fixed, so that it remains unchanged even as the size of the player window changes. Cropping might occur if the player window is smaller than the content.
   */
  "public static const",{ NO_SCALE/*:String*/ : "noScale"},
  /**
   * Specifies that the entire application be visible in the specified area without distortion while maintaining the original aspect ratio of the application. Borders can appear on two sides of the application.
   */
  "public static const",{ SHOW_ALL/*:String*/ : "showAll"},
];},[],[], "0.8.0", "0.8.1"
);