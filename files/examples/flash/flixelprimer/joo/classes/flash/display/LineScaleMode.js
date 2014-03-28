joo.classLoader.prepare("package flash.display",/* {*/


/**
 * The LineScaleMode class provides values for the <code>scaleMode</code> parameter in the <code>Graphics.lineStyle()</code> method.
 * @see Graphics#lineStyle()
 *
 */
"public final class LineScaleMode",1,function($$private){;return[ 
  /**
   * With this setting used as the <code>scaleMode</code> parameter of the <code>lineStyle()</code> method, the thickness of the line scales <i>only</i> vertically. For example, consider the following circles, drawn with a one-pixel line, and each with the <code>scaleMode</code> parameter set to <code>LineScaleMode.VERTICAL</code>. The circle on the left is scaled only vertically, and the circle on the right is scaled both vertically and horizontally.
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/LineScaleMode_VERTICAL.jpg" /></p>
   */
  "public static const",{ HORIZONTAL/*:String*/ : "horizontal"},
  /**
   * With this setting used as the <code>scaleMode</code> parameter of the <code>lineStyle()</code> method, the thickness of the line never scales.
   */
  "public static const",{ NONE/*:String*/ : "none"},
  /**
   * With this setting used as the <code>scaleMode</code> parameter of the <code>lineStyle()</code> method, the thickness of the line always scales when the object is scaled (the default).
   */
  "public static const",{ NORMAL/*:String*/ : "normal"},
  /**
   * With this setting used as the <code>scaleMode</code> parameter of the <code>lineStyle()</code> method, the thickness of the line scales <i>only</i> horizontally. For example, consider the following circles, drawn with a one-pixel line, and each with the <code>scaleMode</code> parameter set to <code>LineScaleMode.HORIZONTAL</code>. The circle on the left is scaled only horizontally, and the circle on the right is scaled both vertically and horizontally.
   * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/LineScaleMode_HORIZONTAL.jpg" /></p>
   */
  "public static const",{ VERTICAL/*:String*/ : "vertical"},
];},[],[], "0.8.0", "0.8.3"
);