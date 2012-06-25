package {

/**
 * The RegExp class lets you work with regular expressions, which are patterns that you can use to perform searches in strings and to replace text in strings.
 * <p>You can create a new RegExp object by using the <code>new RegExp()</code> constructor or by assigning a RegExp literal to a variable:</p>
 * <listing>
 *  var pattern1:RegExp = new RegExp("test-\\d", "i");
 *      var pattern2:RegExp = /test-\d/i;
 *     </listing>
 * <p>For more information, see "Using Regular Expressions" in the <i>ActionScript 3.0 Developer's Guide</i>.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/./RegExp.html#includeExamplesSummary">View the examples</a></p>
 * @see String#match()
 * @see String#replace()
 * @see String#search()
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea9.html Regular expression syntax
 *
 */
[Native]
public dynamic class RegExp {
  /**
   * Specifies whether the dot character (.) in a regular expression pattern matches new-line characters. Use the <code>s</code> flag when constructing a regular expression to set <code>dotall = true</code>.
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea9.html Regular expression syntax
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea7.html Flags and properties
   *
   * @example The following example shows the effect of the <code>s</code> (<code>dotall</code>) flag on a regular expression:
   * <listing>
   * var str:String = "<p>Hello\n"
   *         + "again</p>"
   *         + "<p>Hello</p>";
   *
   * var pattern:RegExp = /<p>.*?<\/p>/;
   * trace(pattern.dotall) // false
   * trace(pattern.exec(str)); // <p>Hello</p>
   *
   * pattern = /<p>.*?<\/p>/s;
   * trace(pattern.dotall) // true
   * trace(pattern.exec(str));
   *   </listing>
   */
  public native function get dotall():Boolean;

  /**
   * Specifies whether to use extended mode for the regular expression. When a RegExp object is in extended mode, white space characters in the constructor string are ignored. This is done to allow more readable constructors.
   * <p>Use the <code>x</code> flag when constructing a regular expression to set <code>extended = true</code>.</p>
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea9.html Regular expression syntax
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea7.html Flags and properties
   *
   * @example The following example shows different ways to construct the same regular expression. In each, the regular expression is to match a phone number pattern of xxx-xxx-xxxx or (xxx) xxx-xxxx or (xxx)xxx-xxxx. The second regular expression uses the <code>x</code> flag, causing the white spaces in the string to be ignored.
   * <listing>
   * var rePhonePattern1:RegExp = /\d{3}-\d{3}-\d{4}|\(\d{3}\)\s?\d{3}-\d{4}/;
   * var str:String = "The phone number is (415)555-1212.";
   *
   * trace(rePhonePattern1.extended) // false
   * trace(rePhonePattern1.exec(str)); // (415)555-1212
   *
   * var rePhonePattern2:RegExp = / \d{3}-\d{3}-\d{4}  |   \( \d{3} \) \ ? \d{3}-\d{4}  /x;
   * trace(rePhonePattern2.extended) // true
   * trace(rePhonePattern2.exec(str)); // (415)555-1212
   * </listing>
   */
  public native function get extended():Boolean;

  /**
   * Specifies whether to use global matching for the regular expression. When <code>global == true</code>, the <code>lastIndex</code> property is set after a match is found. The next time a match is requested, the regular expression engine starts from the <code>lastIndex</code> position in the string. Use the <code>g</code> flag when constructing a regular expression to set <code>global</code> to <code>true</code>.
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea9.html Regular expression syntax
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea7.html Flags and properties
   *
   * @example The following example shows the effect setting the <code>g</code> (<code>global</code>) flag on the <code>exec()</code> method:
   * <listing>
   * var pattern:RegExp = /foo\d/;
   * var str:String = "foo1 foo2";
   * trace(pattern.global); // false
   * trace(pattern.exec(str)); // foo1
   * trace(pattern.lastIndex); // 0
   * trace(pattern.exec(str)); // foo1
   *
   * pattern = /foo\d/g;
   * trace(pattern.global); // true
   * trace(pattern.exec(str)); // foo1
   * trace(pattern.lastIndex); // 4
   * trace(pattern.exec(str)); // foo2
   * </listing>
   */
  public native function get global():Boolean;

  /**
   * Specifies whether the regular expression ignores case sensitivity. Use the <code>i</code> flag when constructing a regular expression to set <code>ignoreCase = true</code>.
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea9.html Regular expression syntax
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea7.html Flags and properties
   *
   * @example The following example shows the effect of setting the <code>i</code> (<code>ignoreCase</code>) flag:
   * <listing>
   * var pattern:RegExp = /bob/;
   * var str:String = "Bob bob";
   * trace(pattern.ignoreCase); // false
   * trace(pattern.exec(str)); // bob
   *
   * pattern = /bob/i;
   * trace(pattern.ignoreCase); // true
   * trace(pattern.exec(str)); // Bob
   * </listing>
   */
  public native function get ignoreCase():Boolean;

  /**
   * Specifies the index position in the string at which to start the next search. This property affects the <code>exec()</code> and <code>test()</code> methods of the RegExp class. However, the <code>match()</code>, <code>replace()</code>, and <code>search()</code> methods of the String class ignore the <code>lastIndex</code> property and start all searches from the beginning of the string.
   * <p>When the <code>exec()</code> or <code>test()</code> method finds a match and the <code>g</code> (<code>global</code>) flag is set to <code>true</code> for the regular expression, the method automatically sets the <code>lastIndex</code> property to the index position of the character <i>after</i> the last character in the matching substring of the last match. If the <code>g</code> (<code>global</code>) flag is set to <code>false</code>, the method does not set the <code>lastIndex</code>property.</p>
   * <p>You can set the <code>lastIndex</code> property to adjust the starting position in the string for regular expression matching.</p>
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea9.html Regular expression syntax
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea7.html Flags and properties
   *
   * @example The following example shows the effect of setting the <code>lastIndex</code> property, and it shows how it is updated after a call to the <code>exec()</code> method on a regular expression in which the <code>g</code> (<code>global</code>) flag is set:
   * <listing>
   * var pattern:RegExp = /\w\d/g;
   * var str:String = "a1 b2 c3 d4";
   * pattern.lastIndex = 2;
   * trace(pattern.exec(str)); // b2
   * trace(pattern.lastIndex); // 5
   * trace(pattern.exec(str)); // c3
   * trace(pattern.lastIndex); // 8
   * trace(pattern.exec(str)); // d4
   * trace(pattern.lastIndex); // 11
   * trace(pattern.exec(str)); // null
   * </listing>
   */
  public native function get lastIndex():Number;

  /**
   * @private
   */
  public native function set lastIndex(value:Number):void;

    /**
     * Specifies whether the <code>m</code> (<code>multiline</code>) flag is set. If it is set, the caret (<code>^</code>) and dollar sign (<code>$</code>) in a regular expression match before and after new lines. Use the <code>m</code> flag when constructing a regular expression to set <code>multiline = true</code>.
     * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea9.html Regular expression syntax
     * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea7.html Flags and properties
     *
     * @example The following example shows the effect setting the <code>m</code> (<code>multiline</code>) flag:
     * <listing>
     * var pattern:RegExp = /^bob/;
     * var str:String = "foo\n"
     *                 + "bob";
     * trace(pattern.multiline); // false
     * trace(pattern.exec(str)); // null
     *
     * pattern = /^bob/m;
     * trace(pattern.multiline); // true
     * trace(pattern.exec(str)); // bob
     * </listing>
     */
  public native function get multiline():Boolean;

  /**
   * Specifies the pattern portion of the regular expression.
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea9.html Regular expression syntax
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea7.html Flags and properties
   *
   * @example The following code outputs the <code>source</code> parameter for two regular expressions:
   * <listing>
   * var re1:RegExp = /aabb/gi;
   * trace (re1.source); // aabb
   *
   * var re2:RegExp = new RegExp("x+y*", "i");
   * trace(re2.source); // x+y*
   * </listing>
   */
  public native function get source():String;

  /**
   * Lets you construct a regular expression from two strings. One string defines the pattern of the regular expression, and the other defines the flags used in the regular expression.
   * @param re The pattern of the regular expression (also known as the <i>constructor string</i>). This is the main part of the regular expression (the part that goes within the "/" characters).
   * <p>Notes:</p>
   * <ul>
   * <li>Do not include the starting and trailing "/" characters; use these only when defining a regular expression literal without using the constructor. For example, the following two regular expressions are equivalent:
   * <listing>
   *  var re1:RegExp = new RegExp("bob", "i");
   *         var re2:RegExp = /bob/i;</listing></li>
   * <li>In a regular expression that is defined with the <code>RegExp()</code> constructor method, to use a metasequence that begins with the backslash (\) character, such as <code>\d</code> (which matches any digit), type the backslash character twice. For example, the following two regular expressions are equivalent:
   * <listing>
   *  var re1:RegExp = new RegExp("\\d+", "");
   *         var re2:RegExp = /\d/;</listing>
   * <p>In the first expression, you must type the backlash character twice in this case, because the first parameter of the <code>RegExp()</code> constructor method is a string, and in a string literal you must type a backslash character twice to have it recognized as a single backslash character.</p></li></ul>
   * @param flags The modifiers of the regular expression. These can include the following:
   * <ul>
   * <li><code>g</code> — When using the <code>replace()</code> method of the String class, specify this modifier to replace all matches, rather than only the first one. This modifier corresponds to the <code>global</code> property of the RegExp instance.</li>
   * <li><code>i</code> — The regular expression is evaluated <i>without</i> case sensitivity. This modifier corresponds to the <code>ignoreCase</code> property of the RegExp instance.</li>
   * <li><code>s</code> — The dot (<code>.</code>) character matches new-line characters. Note This modifier corresponds to the <code>dotall</code> property of the RegExp instance.</li>
   * <li><code>m</code> — The caret (<code>^</code>) character and dollar sign (<code>$</code>) match before <i>and</i> after new-line characters. This modifier corresponds to the <code>multiline</code> property of the RegExp instance.</li>
   * <li><code>x</code> — White space characters in the <code>re</code> string are ignored, so that you can write more readable constructors. This modifier corresponds to the <code>extended</code> property of the RegExp instance.</li></ul>
   * <p>All other characters in the <code>flags</code> string are ignored.</p>
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea9.html Regular expression syntax
   *
   */
  public native function RegExp(re:String = null, flags:String = null);

  /**
   * Performs a search for the regular expression on the given string <code>str</code>.
   * <p>If the <code>g</code> (<code>global</code>) flag is <i>not</i> set for the regular expression, then the search starts at the beginning of the string (at index position 0); the search ignores the <code>lastIndex</code> property of the regular expression.</p>
   * <p>If the <code>g</code> (<code>global</code>) flag <i>is</i> set for the regular expression, then the search starts at the index position specified by the <code>lastIndex</code> property of the regular expression. If the search matches a substring, the <code>lastIndex</code> property changes to match the position of the end of the match.</p>
   * @param str The string to search.
   *
   * @return If there is no match, <code>null</code>; otherwise, an object with the following properties:
   * <ul>
   * <li>An array, in which element 0 contains the complete matching substring, and other elements of the array (1 through <i>n</i>) contain substrings that match parenthetical groups in the regular expression</li>
   * <li><code>index</code> — The character position of the matched substring within the string</li>
   * <li><code>input</code> — The string (<code>str</code>)</li></ul>
   *
   * @see String#match()
   * @see String#search()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea8.html Methods for using regular expressions with strings
   *
   * @example When the <code>g</code> (<code>global</code>) flag is <i>not</i> set in the regular expression, then you can use <code>exec()</code> to find the first match in the string:
   * <listing>
   *          var myPattern:RegExp = /(\w*)sh(\w*)/ig;
   *          var str:String = "She sells seashells by the seashore";
   *          var result:Object = myPattern.exec(str);
   *          trace(result);
   *         </listing>
   * <p>The <code>result</code> object is set to the following:</p>
   * <ul>
   * <li><code>result[0]</code> is set to <code>"She"</code> (the complete match).</li>
   * <li><code>result[1]</code> is set to an empty string (the first matching parenthetical group).</li>
   * <li><code>result[2]</code> is set to <code>"e"</code> (the second matching parenthetical group).</li>
   * <li><code>result.index</code> is set to 0.</li>
   * <li><code>result.input</code> is set to the input string: <code>"She sells seashells by the seashore"</code>.</li></ul>
   * <p>In the following example, the <code>g</code> (<code>global</code>) flag <i>is</i> set in the regular expression, so you can use <code>exec()</code> repeatedly to find multiple matches:</p>
   * <listing>
   *          var myPattern:RegExp = /(\w*)sh(\w*)/ig;
   *          var str:String = "She sells seashells by the seashore";
   *          var result:Object = myPattern.exec(str);
   *
   *          while (result != null) {
   *              trace ( result.index, "\t", result);
   *              result = myPattern.exec(str);
   *          }
   *         </listing>
   * <p>This code results in the following output:</p>
   * <pre><code>           0      She,,e
   10     seashells,sea,ells
   27     seashore,sea,ore
   </code></pre>
   */
  public native function exec(str:String):Array;

  /**
   * Tests for the match of the regular expression in the given string <code>str</code>.
   * <p>If the <code>g</code> (<code>global</code>) flag is <i>not</i> set for the regular expression, then the search starts at the beginning of the string (at index position 0); the search ignores the <code>lastIndex</code> property of the regular expression.</p>
   * <p>If the <code>g</code> (<code>global</code>) flag <i>is</i> set for the regular expression, then the search starts at the index position specified by the <code>lastIndex</code> property of the regular expression. If the search matches a substring, the <code>lastIndex</code> property changes to match the position of the end of the match.</p>
   * @param str The string to test.
   *
   * @return If there is a match, <code>true</code>; otherwise, <code>false</code>.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea8.html Methods for using regular expressions with strings
   *
   * @example The following example shows the use of the <code>test()</code> method on a regular expression in which the <code>g</code> (<code>global</code>) flag is set:
   * <listing>
   * var re1:RegExp = /\w/g;
   * var str:String = "a b c";
   * trace (re1.lastIndex); // 0
   * trace (re1.test(str)); // true
   * trace (re1.lastIndex); // 1
   * trace (re1.test(str)); // true
   * trace (re1.lastIndex); // 3
   * trace (re1.test(str)); // true
   * trace (re1.lastIndex); // 5
   * trace (re1.test(str)); // false
   * </listing>
   */
  public native function test(str:String):Boolean;



  public native function get index():Number;


  public native static function get leftContext():String;


  public native static function get input():String;


  public native static function get lastParen():String;


  public native static function get lastMatch():String;


  public native static function get rightContext():String;


  public native function get $1():String;


  public native function get $2():String;


  public native function get $3():String;


  public native function get $4():String;


  public native function get $5():String;

}
}
