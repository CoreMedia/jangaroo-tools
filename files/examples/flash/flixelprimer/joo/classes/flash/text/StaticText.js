joo.classLoader.prepare("package flash.text",/* {
import flash.display.DisplayObject*/

/**
 * This class represents StaticText objects on the display list. You cannot create a StaticText object using ActionScript. Only the authoring tool can create a StaticText object. An attempt to create a new StaticText object generates an <code>ArgumentError</code>.
 * <p>To create a reference to an existing static text field in ActionScript 3.0, you can iterate over the items in the display list. For example, the following snippet checks to see if the display list contains a static text field and assigns the field to a variable:</p>
 * <listing>
 *  var i:uint;
 *  for (i = 0; i < this.numChildren; i++) {
 *      var displayitem:DisplayObject = this.getChildAt(i);
 *      if (displayitem instanceof StaticText) {
 *          trace("a static text field is item " + i + " on the display list");
 *          var myFieldLabel:StaticText = StaticText(displayitem);
 *          trace("and contains the text: " + myFieldLabel.text);
 *      }
 *  }
 * </listing>
 */
"public final class StaticText extends flash.display.DisplayObject",3,function($$private){;return[ 
  /**
   * Returns the current text of the static text field. The authoring tool may export multiple text field objects comprising the complete text. For example, for vertical text, the authoring tool will create one text field per character.
   */
  "public function get text",function text$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["flash.display.DisplayObject","Error"], "0.8.0", "0.8.3"
);