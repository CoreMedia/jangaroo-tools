joo.classLoader.prepare("package flash.text",/* {*/

/**
 * TextSnapshot objects let you work with static text in a movie clip. You can use them, for example, to lay out text with greater precision than that allowed by dynamic text, but still access the text in a read-only way.
 * <p>You don't use a constructor to create a TextSnapshot object; it is returned by <code>flash.display.DisplayObjectContainer.textSnapshot</code> property.</p>
 * @see flash.display.DisplayObjectContainer#textSnapshot
 *
 */
"public class TextSnapshot",1,function($$private){;return[ 
  /**
   * The number of characters in a TextSnapshot object.
   * @see #getText()
   *
   */
  "public function get charCount",function charCount$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Searches the specified TextSnapshot object and returns the position of the first occurrence of <code>textToFind</code> found at or after <code>beginIndex</code>. If <code>textToFind</code> is not found, the method returns <code>-1</code>.
   * @param beginIndex Specifies the starting point to search for the specified text.
   * @param textToFind Specifies the text to search for. If you specify a string literal instead of a variable of type String, enclose the string in quotation marks.
   * @param caseSensitive Specifies whether the text must match the case of the string in <code>textToFind</code>.
   *
   * @return The zero-based index position of the first occurrence of the specified text, or -1.
   *
   * @see #getText()
   *
   */
  "public function findText",function findText(beginIndex/*:int*/, textToFind/*:String*/, caseSensitive/*:Boolean*/)/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns a Boolean value that specifies whether a TextSnapshot object contains selected text in the specified range.
   * <p>To search all characters, pass a value of <code>0</code> for <code>start</code>, and <code>charCount</code> (or any very large number) for <code>end</code>. To search a single character, pass the <code>end</code> parameter a value that is one greater than the <code>start</code> parameter.</p>
   * @param beginIndex Indicates the position of the first character to be examined. Valid values for <code>beginIndex</code> are <code>0</code> through <code>TextSnapshot.charCount - 1</code>. If <code>beginIndex</code> is a negative value, <code>0</code> is used.
   * @param endIndex A value that is one greater than the index of the last character to be examined. Valid values for <code>endIndex</code> are <code>0</code> through <code>charCount</code>. The character indexed by the <code>endIndex</code> parameter is not included in the extracted string. If this parameter is omitted, <code>charCount</code> is used. If this value is less than or equal to the value of <code>beginIndex</code>, <code>beginIndex + 1</code> is used.
   *
   * @return A Boolean value that indicates whether at least one character in the given range has been selected by the corresponding <code>setSelected()</code> method (<code>true</code>); otherwise, <code>false</code>.
   *
   * @see #charCount
   * @see #getText()
   * @see #getSelectedText()
   * @see #setSelected()
   *
   */
  "public function getSelected",function getSelected(beginIndex/*:int*/, endIndex/*:int*/)/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns a string that contains all the characters specified by the corresponding <code>setSelected()</code> method. If no characters are specified (by the <code>setSelected()</code> method), an empty string is returned.
   * <p>If you pass <code>true</code> for <code>includeLineEndings</code>, newline characters are inserted in the return string, and the return string might be longer than the input range. If <code>includeLineEndings</code> is <code>false</code> or omitted, the method returns the selected text without adding any characters.</p>
   * @param includeLineEndings An optional Boolean value that specifies whether newline characters are inserted into the returned string where appropriate. The default value is <code>false</code>.
   *
   * @return A string that contains all the characters specified by the corresponding <code>setSelected()</code> command.
   *
   * @see #getSelected()
   * @see #setSelected()
   *
   */
  "public function getSelectedText",function getSelectedText(includeLineEndings/*:Boolean = false*/)/*:String*/ {switch(arguments.length){case 0:includeLineEndings = false;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns a string that contains all the characters specified by the <code>beginIndex</code> and <code>endIndex</code> parameters. If no characters are selected, an empty string is returned.
   * <p>To return all characters, pass a value of <code>0</code> for <code>beginIndex</code> and <code>charCount</code> (or any very large number) for <code>endIndex</code>. To return a single character, pass a value of <code>beginIndex + 1</code> for <code>endIndex</code>.</p>
   * <p>If you pass a value of <code>true</code> for <code>includeLineEndings</code>, newline characters are inserted in the string returned where deemed appropriate. In this case, the return string might be longer than the input range. If <code>includeLineEndings</code> is <code>false</code> or omitted, the selected text is returned without any characters added.</p>
   * @param beginIndex Indicates the position of the first character to be included in the returned string. Valid values for <code>beginIndex</code> are<code>0</code> through <code>charCount - 1</code>. If <code>beginIndex</code> is a negative value, <code>0</code> is used.
   * @param endIndex A value that is one greater than the index of the last character to be examined. Valid values for <code>endIndex</code> are <code>0</code> through <code>charCount</code>. The character indexed by the <code>endIndex</code> parameter is not included in the extracted string. If this parameter is omitted, <code>charCount</code> is used. If this value is less than or equal to the value of <code>beginIndex</code>, <code>beginIndex + 1</code> is used.
   * @param includeLineEndings An optional Boolean value that specifies whether newline characters are inserted (<code>true</code>) or are not inserted (<code>false</code>) into the returned string. The default value is <code>false</code>.
   *
   * @return A string containing the characters in the specified range, or an empty string if no characters are found in the specified range.
   *
   * @see #charCount
   * @see #getSelectedText()
   *
   */
  "public function getText",function getText(beginIndex/*:int*/, endIndex/*:int*/, includeLineEndings/*:Boolean = false*/)/*:String*/ {switch(arguments.length){case 0:case 1:case 2:includeLineEndings = false;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns an array of objects that contains information about a run of text. Each object corresponds to one character in the range of characters specified by the two method parameters.
   * <p><b>Note:</b> Using the <code>getTextRunInfo()</code> method for a large range of text can return a large object. Adobe recommends limiting the text range defined by the <code>beginIndex</code> and <code>endIndex</code> parameters.</p>
   * @param beginIndex The index value of the first character in a range of characters in a TextSnapshot object.
   * @param endIndex The index value of the last character in a range of characters in a TextSnapshot object.
   *
   * @return An array of objects in which each object contains information about a specific character in the range of characters specified by the <code>beginIndex</code> and <code>endIndex</code> parameters. Each object contains the following eleven properties:
   * <ul>
   * <li><code>indexInRun</code>—A zero-based integer index of the character (relative to the entire string rather than the selected run of text).</li>
   * <li><code>selected</code>—A Boolean value that indicates whether the character is selected <code>true</code>; <code>false</code> otherwise.</li>
   * <li><code>font</code>—The name of the character's font.</li>
   * <li><code>color</code>—The combined alpha and color value of the character. The first two hexadecimal digits represent the alpha value, and the remaining digits represent the color value.</li>
   * <li><code>height</code>—The height of the character, in pixels.</li>
   * <li><code>matrix_a</code>, <code>matrix_b</code>, <code>matrix_c</code>, <code>matrix_d</code>, <code>matrix_tx</code>, and <code>matrix_ty</code>— The values of a matrix that define the geometric transformation on the character. Normal, upright text always has a matrix of the form <code>[1 0 0 1 x y]</code>, where <code>x</code> and <code>y</code> are the position of the character within the parent movie clip, regardless of the height of the text. The matrix is in the parent movie clip coordinate system, and does not include any transformations that may be on that movie clip itself (or its parent).</li>
   * <li><code>corner0x</code>, <code>corner0y</code>, <code>corner1x</code>, <code>corner1y</code>, <code>corner2x</code>, <code>corner2y</code>, <code>corner3x</code>, and <code>corner3y</code>—The corners of the bounding box of the character, based on the coordinate system of the parent movie clip. These values are only available if the font used by the character is embedded in the SWF file.</li></ul>
   *
   * @see flash.geom.Matrix
   *
   */
  "public function getTextRunInfo",function getTextRunInfo(beginIndex/*:int*/, endIndex/*:int*/)/*:Array*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Lets you determine which character within a TextSnapshot object is on or near the specified <code>x</code>, <code>y</code> coordinates of the movie clip containing the text in the TextSnapshot object.
   * <p>If you omit or pass a value of <code>0</code> for <code>maxDistance</code>, the location specified by the <code>x</code>, <code>y</code> coordinates must lie inside the bounding box of the TextSnapshot object.</p>
   * <p>This method works correctly only with fonts that include character metric information; however, by default, the Flash authoring tool does not include this information for static text fields. Therefore, the method might return <code>-1</code> instead of an index value. To ensure that an index value is returned, you can force the Flash authoring tool to include the character metric information for a font. To do this, add a dynamic text field that uses that font, select Character Options for that dynamic text field, and then specify that font outlines should be embedded for at least one character. (It doesn't matter which characters you specify, nor whether they are the characters used in the static text fields.)</p>
   * @param x A number that represents the <code>x</code> coordinate of the movie clip containing the text.
   * @param y A number that represents the <code>y</code> coordinate of the movie clip containing the text.
   * @param maxDistance An optional number that represents the maximum distance from <code>x</code>, <code>y</code> that can be searched for text. The distance is measured from the center point of each character. The default value is <code>0</code>.
   *
   * @return A number representing the index value of the character that is nearest to the specified <code>x</code>, <code>y</code> coordinate. Returns <code>-1</code> if no character is found, or if the font doesn't contain character metric information.
   *
   * @see flash.display.DisplayObject#x
   * @see flash.display.DisplayObject#y
   *
   */
  "public function hitTestTextNearPos",function hitTestTextNearPos(x/*:Number*/, y/*:Number*/, maxDistance/*:Number = 0*/)/*:Number*/ {switch(arguments.length){case 0:case 1:case 2:maxDistance = 0;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies the color to use when highlighting characters that have been selected with the <code>setSelected()</code> method. The color is always opaque; you can't specify a transparency value.
   * <p>This method works correctly only with fonts that include character metric information; however, by default, the Flash authoring tool does not include this information for static text fields. Therefore, the method might return <code>-1</code> instead of an index value. To ensure that an index value is returned, you can force the Flash authoring tool to include the character metric information for a font. To do this, add a dynamic text field that uses that font, select Character Options for that dynamic text field, and then specify that font outlines should be embedded for at least one character. (It doesn't matter which characters you specify, nor if they are the characters used in the static text fields.)</p>
   * @param hexColor The color used for the border placed around characters that have been selected by the corresponding <code>setSelected()</code> command, expressed in hexadecimal format (0x<i>RRGGBB</i>).
   *
   * @see #setSelected()
   *
   */
  "public function setSelectColor",function setSelectColor(hexColor/*:uint = 0xFFFF00*/)/*:void*/ {switch(arguments.length){case 0:hexColor = 0xFFFF00;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies a range of characters in a TextSnapshot object to be selected or deselected. Characters that are selected are drawn with a colored rectangle behind them, matching the bounding box of the character. The color of the bounding box is defined by <code>setSelectColor()</code>.
   * <p>To select or deselect all characters, pass a value of <code>0</code> for <code>beginIndex</code> and <code>charCount</code> (or any very large number) for <code>endIndex</code>. To specify a single character, pass a value of <code>start + 1</code> for <code>endIndex</code>.</p>
   * <p>Because characters are individually marked as selected, you can call this method multiple times to select multiple characters; that is, using this method does not deselect other characters that have been set by this method.</p>
   * <p>The colored rectangle that indicates a selection is displayed only for fonts that include character metric information; by default, Flash does not include this information for static text fields. In some cases, this behavior means that text that is selected won't appear to be selected onscreen. To ensure that all selected text appears to be selected, you can force the Flash authoring tool to include the character metric information for a font. To do this, add a dynamic text field that uses that font, select Character Options for that dynamic text field, and then specify that font outlines should be embedded for at least one character. It doesn't matter which characters you specify, nor even if they are the characters used in the static text fields in question.</p>
   * @param beginIndex Indicates the position of the first character to select. Valid values for <code>beginIndex</code> are <code>0</code> through <code>charCount - 1</code>. If <code>beginIndex</code> is a negative value, <code>0</code> is used.
   * @param endIndex An integer that is 1+ the index of the last character to be examined. Valid values for <code>end</code> are <code>0</code> through <code>charCount</code>. The character indexed by the <code>end</code> parameter is not included in the extracted string. If you omit this parameter, <code>TextSnapshot.charCount</code> is used. If the value of <code>beginIndex</code> is less than or equal to the value of <code>endIndex</code>, <code>beginIndex + 1</code> is used.
   * @param select A Boolean value that specifies whether the text should be selected (<code>true</code>) or deselected (<code>false</code>).
   *
   * @see #charCount
   * @see #setSelectColor()
   *
   */
  "public function setSelected",function setSelected(beginIndex/*:int*/, endIndex/*:int*/, select/*:Boolean*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);