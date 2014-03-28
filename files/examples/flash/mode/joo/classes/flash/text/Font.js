joo.classLoader.prepare("package flash.text",/* {*/


/**
 * The Font class is used to manage embedded fonts in SWF files. Embedded fonts are represented as a subclass of the Font class. The Font class is currently useful only to find out information about embedded fonts; you cannot alter a font by using this class. You cannot use the Font class to load external fonts, or to create an instance of a Font object by itself. Use the Font class as an abstract base class.
 */
"public class Font",1,function($$private){;return[ 
  /**
   * The name of an embedded font.
   * @example The following example shows how you can use an embedded font with the Flash Professional ActionScript 3.0 CheckBox control by setting the textFormat and embedFonts styles. Example provided by <a href="http://actionscriptexamples.com/2008/11/27/using-embedded-fonts-with-the-checkbox-control-in-flash-with-actionscript-30/">ActionScriptExamples.com</a>.
   * <listing>
   * // Requires:
   * // - A CheckBox control UI component in Flash library.
   * // - An embedded font in Flash library with linkage class "MyFont" and Export for ActionScript checked.
   * //
   * import fl.controls.CheckBox;
   *
   * var embeddedFont:Font = new MyFont();
   *
   * var textFormat:TextFormat = new TextFormat();
   * textFormat.font = embeddedFont.fontName;
   * textFormat.size = 24;
   *
   * var checkBox:CheckBox = new CheckBox();
   * checkBox.setStyle("textFormat", textFormat);
   * checkBox.setStyle("embedFonts", true);
   * checkBox.label = "The quick brown fox jumps over the lazy dog.";
   * checkBox.textField.autoSize = TextFieldAutoSize.LEFT;
   * checkBox.move(10, 10);
   * checkBox.validateNow();
   * addChild(checkBox);
   * </listing>
   */
  "public function get fontName",function fontName$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The style of the font. This value can be any of the values defined in the FontStyle class.
   * @see FontStyle
   *
   */
  "public function get fontStyle",function fontStyle$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The type of the font. This value can be any of the constants defined in the FontType class.
   * @see FontType
   *
   */
  "public function get fontType",function fontType$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies whether to provide a list of the currently available embedded fonts.
   * @param enumerateDeviceFonts Indicates whether you want to limit the list to only the currently available embedded fonts. If this is set to <code>true</code> then a list of all fonts, both device fonts and embedded fonts, is returned. If this is set to <code>false</code> then only a list of embedded fonts is returned.
   *
   * @return A list of available fonts as an array of Font objects.
   *
   * @example This example first calls the static method <code>Font.enumerateFonts()</code> to get a list of all device and embedded fonts. Then it sorts the resulting Array of Font objects by the <code>fontName</code> property.
   * <p>Next the example shows how to call the <code>Font.enumerateFonts()</code> method with the <code>enumerateDeviceFonts</code> parameter set to false. The resulting Array only includes embedded Font objects. (If you run this code within an application that does not contain any embedded fonts, the <code>embeddedFonts</code> array will be empty.)</p>
   * <listing>
   *
   * import flash.text.Font;
   *
   * var allFonts:Array = Font.enumerateFonts(true);
   * allFonts.sortOn("fontName", Array.CASEINSENSITIVE);
   *
   * var embeddedFonts:Array = Font.enumerateFonts(false);
   * embeddedFonts.sortOn("fontName", Array.CASEINSENSITIVE);
   * </listing>
   */
  "public static function enumerateFonts",function enumerateFonts(enumerateDeviceFonts/*:Boolean = false*/)/*:Array*/ {switch(arguments.length){case 0:enumerateDeviceFonts = false;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies whether a provided string can be displayed using the currently assigned font.
   * @param str The string to test against the current font.
   *
   * @return A value of <code>true</code> if the specified string can be fully displayed using this font.
   *
   */
  "public function hasGlyphs",function hasGlyphs(str/*:String*/)/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Registers a font class in the global font list.
   * @param font The class you want to add to the global font list.
   *
   */
  "public static function registerFont",function registerFont(font/*:Class*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},["enumerateFonts","registerFont"],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);