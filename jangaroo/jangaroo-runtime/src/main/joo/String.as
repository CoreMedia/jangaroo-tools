package {

/**
 * The String class is a data type that represents a string of characters. The String class provides methods and properties that let you manipulate primitive string value types. You can convert the value of any object into a String data type object using the <code>String()</code> function.
 * <p>Because all string indexes are zero-based, the index of the last character for any string <code>x</code> is <code>x.length - 1</code>.</p>
 * <p>You can call any of the methods of the String class whether you use the constructor method <code>new String()</code> to create a new string variable or simply assign a string literal value. Unlike previous versions of ActionScript, it makes no difference whether you use the constructor, the global function, or simply assign a string literal value. The following lines of code are equivalent:</p>
 * <listing>
 *  var str:String = new String("foo");
 *  var str:String = "foo";
 *  var str:String = String("foo");</listing>
 * <p>When setting a string variable to <code>undefined</code>, the Flash runtimes coerce <code>undefined</code> to <code>null</code>. So, the statement:</p>
 * <pre> var s:String = undefined;</pre>sets the value to <code>null</code> instead of <code>undefined</code>. Use the <code>String()</code> function if you need to use <code>undefined</code>.
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/./String.html#includeExamplesSummary">View the examples</a></p>
 * @see #String()
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea9.html Regular expression syntax
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f9c.html Data types
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f88.html Data type descriptions
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f87.html Type conversions
 *
 */
[Native]
public class String {
  /**
   * An integer specifying the number of characters in the specified String object.
   * <p>Because all string indexes are zero-based, the index of the last character for any string <code>x</code> is <code>x.length - 1</code>.</p>
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ef0.html The length property
   *
   */
  public native function get length():Number;

  /**
   * Creates a new String object initialized to the specified string.
   * <p><b>Note:</b> Because string literals use less overhead than String objects and are generally easier to use, you should use string literals instead of the String class unless you have a good reason to use a String object rather than a string literal.</p>
   * @param val The initial value of the new String object.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea9.html Regular expression syntax
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f9c.html Data types
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f88.html Data type descriptions
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f87.html Type conversions
   *
   */
  public native function String(val:* = undefined);

  /**
   * Returns the character in the position specified by the <code>index</code> parameter. If <code>index</code> is not a number from 0 to <code>string.length - 1</code>, an empty string is returned.
   * <p>This method is similar to <code>String.charCodeAt()</code> except that the returned value is a character, not a 16-bit integer character code.</p>
   * @param index An integer specifying the position of a character in the string. The first character is indicated by <code>0</code>, and the last character is indicated by <code>my_str.length - 1</code>.
   *
   * @return The character at the specified index. Or an empty string if the specified index is outside the range of this string's indices.
   *
   * @see #charCodeAt()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7efe.html Working with characters in strings
   *
   */
  public native function charAt(index:Number = 0):String;

  /**
   * Returns the numeric Unicode character code of the character at the specified <code>index</code>. If <code>index</code> is not a number from 0 to <code>string.length - 1</code>, <code>NaN</code> is returned.
   * <p>This method is similar to <code>String.charAt()</code> except that the returned value is a 16-bit integer character code, not the actual character.</p>
   * @param index An integer that specifies the position of a character in the string. The first character is indicated by <code>0,</code> and the last character is indicated by <code>my_str.length - 1</code>.
   *
   * @return The Unicode character code of the character at the specified index. Or <code>NaN</code> if the index is outside the range of this string's indices.
   * <p>Unicode values are defined in the <a href="http://www.unicode.org/ucd/">Unicode Character Database</a> specification.</p>
   *
   * @see #charAt()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7efe.html Working with characters in strings
   *
   */
  public native function charCodeAt(index:Number = 0):Number;

  /**
   * Appends the supplied arguments to the end of the String object, converting them to strings if necessary, and returns the resulting string. The original value of the source String object remains unchanged.
   * @param args Zero or more values to be concatenated.
   *
   * @return A new string consisting of this string concatenated with the specified parameters.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ef9.html Concatenating strings
   *
   */
  public native function concat(...args):String;

  /**
   * Returns a string comprising the characters represented by the Unicode character codes in the parameters.
   * @param charCodes A series of decimal integers that represent Unicode values.
   * <p>Unicode values are defined in the <a href="http://www.unicode.org/ucd/">Unicode Character Database</a> specification.</p>
   *
   * @return The string value of the specified Unicode character codes.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7efe.html Working with characters in strings
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7fd0.html Functions
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f56.html Function parameters
   *
   */
  public native static function fromCharCode(... charCodes):String;

  /**
   * Searches the string and returns the position of the first occurrence of <code>val</code> found at or after <code>startIndex</code> within the calling string. This index is zero-based, meaning that the first character in a string is considered to be at index 0--not index 1. If <code>val</code> is not found, the method returns -1.
   * @param val The substring for which to search.
   * @param startIndex An optional integer specifying the starting index of the search.
   *
   * @return The index of the first occurrence of the specified substring or <code>-1</code>.
   *
   * @see #lastIndexOf()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f00.html Finding substrings and patterns in strings
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ef3.html Finding the character position of a matching substring
   *
   */
  public native function indexOf(val:String, startIndex:Number = undefined):int;

  /**
   * Searches the string from right to left and returns the index of the last occurrence of <code>val</code> found before <code>startIndex</code>. The index is zero-based, meaning that the first character is at index 0, and the last is at <code>string.length - 1</code>. If <code>val</code> is not found, the method returns <code>-1</code>.
   * @param val The string for which to search.
   * @param startIndex An optional integer specifying the starting index from which to search for <code>val</code>. The default is the maximum value allowed for an index. If <code>startIndex</code> is not specified, the search starts at the last item in the string.
   *
   * @return The position of the last occurrence of the specified substring or -1 if not found.
   *
   * @see #indexOf()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f00.html Finding substrings and patterns in strings
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ef3.html Finding the character position of a matching substring
   *
   */
  public native function lastIndexOf(val:String, startIndex:Number = 0x7FFFFFFF):int;

  /**
   * Compares the sort order of two or more strings and returns the result of the comparison as an integer. While this method is intended to handle the comparison in a locale-specific way, the ActionScript 3.0 implementation does not produce a different result from other string comparisons such as the equality (<code>==</code>) or inequality (<code>!=</code>) operators. If the strings are equivalent, the return value is 0. If the original string value precedes the string value specified by <code>other</code>, the return value is a negative integer, the absolute value of which represents the number of characters that separates the two string values. If the original string value comes after <code>other</code>, the return value is a positive integer, the absolute value of which represents the number of characters that separates the two string values.
   * @param other A string value to compare.
   * @param values Optional set of more strings to compare.
   *
   * @return The value 0 if the strings are equal. Otherwise, a negative integer if the original string precedes the string argument and a positive integer if the string argument precedes the original string. In both cases the absolute value of the number represents the difference between the two strings.
   *
   */
  public native function localeCompare(other:String, ...values):int;

  /**
   * Matches the specifed <code>pattern</code> against the string.
   * @param pattern The pattern to match, which can be any type of object, but it is typically either a string or a regular expression. If the <code>pattern</code> is not a regular expression or a string, then the method converts it to a string before executing.
   *
   * @return An array of strings consisting of all substrings in the string that match the specified <code>pattern</code>.
   * <p>If <code>pattern</code> is a regular expression, in order to return an array with more than one matching substring, the <code>g</code> (global) flag must be set in the regular expression:</p>
   * <ul>
   * <li>If the <code>g</code> (global) flag is <i>not</i> set, the return array will contain no more than one match, and the <code>lastIndex</code> property of the regular expression remains unchanged.</li>
   * <li>If the <code>g</code> (global) flag <i>is</i> set, the method starts the search at the beginning of the string (index position 0). If a matching substring is an empty string (which can occur with a regular expression such as <code>/x&lowast;/</code>), the method adds that empty string to the array of matches, and then continues searching at the next index position. The <code>lastIndex</code> property of the regular expression is set to 0 after the method completes.</li>
   * </ul>
   * <p>If no match is found,the method returns an empty array. If you pass no value (or an undefined value) as the <code>pattern</code> parameter, the method returns <code>null</code>.</p>
   *
   * @see RegExp
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f00.html Finding substrings and patterns in strings
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ef1.html Finding patterns in strings and replacing substrings
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea9.html Regular expression syntax
   *
   */
  public native function match(pattern:*):Array;

  /**
   * Matches the specifed <code>pattern</code> against the string and returns a new string in which the first match of <code>pattern</code> is replaced with the content specified by <code>repl</code>. The <code>pattern</code> parameter can be a string or a regular expression. The <code>repl</code> parameter can be a string or a function; if it is a function, the string returned by the function is inserted in place of the match. The original string is not modified.
   * <p>In the following example, only the first instance of "sh" (case-sensitive) is replaced:</p>
   * <listing>
   *     var myPattern:RegExp = /sh/;
   *     var str:String = "She sells seashells by the seashore.";
   *     trace(str.replace(myPattern, "sch"));
   *        // She sells seaschells by the seashore.</listing>
   * <p>In the following example, all instances of "sh" (case-sensitive) are replaced because the <code>g</code> (global) flag is set in the regular expression:</p>
   * <listing>
   *     var myPattern:RegExp = /sh/g;
   *     var str:String = "She sells seashells by the seashore.";
   *     trace(str.replace(myPattern, "sch"));
   *        // She sells seaschells by the seaschore.</listing>
   * <p>In the following example, all instance of "sh" are replaced because the <code>g</code> (global) flag is set in the regular expression and the matches are <i>not</i> case-sensitive because the <code>i</code> (ignoreCase) flag is set:</p>
   * <listing>
   *     var myPattern:RegExp = /sh/gi;
   *     var str:String = "She sells seashells by the seashore.";
   *     trace(str.replace(myPattern, "sch"));
   *        // sche sells seaschells by the seaschore.</listing>
   * @param pattern The pattern to match, which can be any type of object, but it is typically either a string or a regular expression. If you specify a <code>pattern</code> parameter that is any object other than a string or a regular expression, the <code>toString()</code> method is applied to the parameter and the <code>replace()</code> method executes using the resulting string as the <code>pattern</code>.
   * @param repl Typically, the string that is inserted in place of the matching content. However, you can also specify a function as this parameter. If you specify a function, the string returned by the function is inserted in place of the matching content.
   * <p>When you specify a string as the <code>repl</code> parameter and specify a regular expression as the <code>pattern</code> parameter, you can use the following special <i>$ replacement codes</i> in the <code>repl</code> string:</p>
   * <table>
   * <tr><th>$ Code</th><th>Replacement Text</th></tr>
   * <tr>
   * <td><code>$$</code> </td>
   * <td><code>$</code> </td></tr>
   * <tr>
   * <td><code>$&</code> </td>
   * <td>The matched substring.</td></tr>
   * <tr>
   * <td><code>$`</code> </td>
   * <td>The portion of the string that precedes the matched substring. Note that this code uses the straight left single quote character (`), not the straight single quote character (') or the left curly single quote character (‘).</td></tr>
   * <tr>
   * <td><code>$'</code> </td>
   * <td>The portion of string that follows the matched substring. Note that this code uses the straight single quote character (').</td></tr>
   * <tr>
   * <td><code>$</code><i>n</i> </td>
   * <td>The <i>n</i>th captured parenthetical group match, where <i>n</i> is a single digit 1-9 and <code>$</code><i>n</i> is not followed by a decimal digit.</td></tr>
   * <tr>
   * <td><code>$</code><i>nn</i> </td>
   * <td>The <i>nn</i>th captured parenthetical group match, where <i>nn</i> is a two-digit decimal number (01-99). If the <i>nn</i>th capture is undefined, the replacement text is an empty string.</td></tr></table>
   * <p>For example, the following shows the use of the <code>$2</code> and <code>$1</code> replacement codes, which represent the first and second capturing group matched:</p>
   * <listing>
   * var str:String = "flip-flop";
   *     var pattern:RegExp = /(\w+)-(\w+)/g;
   *     trace(str.replace(pattern, "$2-$1")); // flop-flip</listing>
   * <p>When you specify a function as the <code>repl</code>, the <code>replace()</code> method passes the following parameters to the function:</p>
   * <ul>
   * <li>The matching portion of the string.</li>
   * <li>Any captured parenthetical group matches are provided as the next arguments. The number of arguments passed this way will vary depending on the number of parenthetical matches. You can determine the number of parenthetical matches by checking <code>arguments.length - 3</code> within the function code.</li>
   * <li>The index position in the string where the match begins.</li>
   * <li>The complete string.</li></ul>
   * <p>For example, consider the following:</p>
   * <listing>
   *     var str1:String = "abc12 def34";
   *     var pattern:RegExp = /([a-z]+)([0-9]+)/;
   *     var str2:String = str1.replace(pattern, replFN);
   *     trace (str2);   // 12abc 34def
   *
   *     function replFN():String {
   *       return arguments[2] + arguments[1];
   *     }</listing>
   * <p>The call to the <code>replace()</code> method uses a function as the <code>repl</code> parameter. The regular expression (<code>/([a-z]([0-9]/g</code>) is matched twice. The first time, the pattern matches the substring <code>"abc12"</code>, and the following list of arguments is passed to the function:</p>
   * <listing>
   *     {"abc12", "abc", "12", 0, "abc12 def34"}</listing>
   * <p>The second time, the pattern matches the substring <code>"def23"</code>, and the following list of arguments is passed to the function:</p>
   * <listing>
   *     {"def34", "def", "34", 6, "abc123 def34"}</listing>
   *
   * @return The resulting string. Note that the source string remains unchanged.
   *
   * @see RegExp
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f00.html Finding substrings and patterns in strings
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ef1.html Finding patterns in strings and replacing substrings
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea9.html Regular expression syntax
   *
   */
  public native function replace(pattern:*, repl:Object, options:String = null):String;

  /**
   * Searches for the specifed <code>pattern</code> and returns the index of the first matching substring. If there is no matching substring, the method returns <code>-1</code>.
   * @param pattern The pattern to match, which can be any type of object but is typically either a string or a regular expression.. If the <code>pattern</code> is not a regular expression or a string, then the method converts it to a string before executing. Note that if you specify a regular expression, the method ignores the global flag ("g") of the regular expression, and it ignores the <code>lastIndex</code> property of the regular expression (and leaves it unmodified). If you pass an undefined value (or no value), the method returns <code>-1</code>.
   *
   * @return The index of the first matching substring, or <code>-1</code> if there is no match. Note that the string is zero-indexed; the first character of the string is at index 0, the last is at <code>string.length - 1</code>.
   *
   * @see RegExp
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f00.html Finding substrings and patterns in strings
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ef1.html Finding patterns in strings and replacing substrings
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea9.html Regular expression syntax
   *
   */
  public native function search(pattern:*):int;

  /**
   * Returns a string that includes the <code>startIndex</code> character and all characters up to, but not including, the <code>endIndex</code> character. The original String object is not modified. If the <code>endIndex</code> parameter is not specified, then the end of the substring is the end of the string. If the character indexed by <code>startIndex</code> is the same as or to the right of the character indexed by <code>endIndex</code>, the method returns an empty string.
   * @param startIndex The zero-based index of the starting point for the slice. If <code>startIndex</code> is a negative number, the slice is created from right-to-left, where -1 is the last character.
   * @param endIndex An integer that is one greater than the index of the ending point for the slice. The character indexed by the <code>endIndex</code> parameter is not included in the extracted string. If <code>endIndex</code> is a negative number, the ending point is determined by counting back from the end of the string, where -1 is the last character. The default is the maximum value allowed for an index. If this parameter is omitted, <code>String.length</code> is used.
   *
   * @return A substring based on the specified indices.
   *
   * @see #substr()
   * @see #substring()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f00.html Finding substrings and patterns in strings
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ef4.html Finding a substring by character position
   *
   */
  public native function slice(startIndex:Number = 0, endIndex:Number = 0x7fffffff):String;

  /**
   * Splits a String object into an array of substrings by dividing it wherever the specified <code>delimiter</code> parameter occurs.
   * <p>If the <code>delimiter</code> parameter is a regular expression, only the first match at a given position of the string is considered, even if backtracking could find a nonempty substring match at that position. For example:</p>
   * <listing>
   *      var str:String = "ab";
   *      var results:Array = str.split(/a*?/); // results == ["","b"]
   *
   *      results = str.split(/a&lowast;/); // results == ["","b"].)
   * </listing>
   * <p>If the <code>delimiter</code> parameter is a regular expression containing grouping parentheses, then each time the <code>delimiter</code> is matched, the results (including any undefined results) of the grouping parentheses are spliced into the output array. For example</p>
   * <listing>
   * var str:String = "Thi5 is a tricky-66 example.";
   * var re:RegExp = /(\d+)/;
   * var results:Array = str.split(re);
   *          // results == ["Thi","5"," is a tricky-","66"," example."]
   * </listing>
   * <p>If the <code>limit</code> parameter is specified, then the returned array will have no more than the specified number of elements.</p>
   * <p>If the <code>delimiter</code> is an empty string, an empty regular expression, or a regular expression that can match an empty string, each single character in the string is output as an element in the array.</p>
   * <p>If the <code>delimiter</code> parameter is undefined, the entire string is placed into the first element of the returned array.</p>
   * @param delimiter The pattern that specifies where to split this string. This can be any type of object but is typically either a string or a regular expression. If the <code> delimiter </code> is not a regular expression or string, then the method converts it to a string before executing.
   * @param limit The maximum number of items to place into the array. The default is the maximum value allowed.
   *
   * @return An array of substrings.
   *
   * @see Array#join()
   * @see RegExp
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f00.html Finding substrings and patterns in strings
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ef2.html Creating an array of substrings segmented by a delimiter
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ea9.html Regular expression syntax
   *
   */
  public native function split(delimiter:*, limit:Number = 0x7fffffff):Array;

  /**
   * Returns a substring consisting of the characters that start at the specified <code>startIndex</code> and with a length specified by <code>len</code>. The original string is unmodified.
   * @param startIndex An integer that specified the index of the first character to be used to create the substring. If <code>startIndex</code> is a negative number, the starting index is determined from the end of the string, where <code>-1</code> is the last character.
   * @param len The number of characters in the substring being created. The default value is the maximum value allowed. If <code>len</code> is not specified, the substring includes all the characters from <code>startIndex</code> to the end of the string.
   *
   * @return A substring based on the specified parameters.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f00.html Finding substrings and patterns in strings
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ef4.html Finding a substring by character position
   *
   */
  public native function substr(startIndex:Number = 0, len:Number = 0x7fffffff):String;

  /**
   * Returns a string consisting of the character specified by <code>startIndex</code> and all characters up to <code>endIndex - 1</code>. If <code>endIndex</code> is not specified, <code>String.length</code> is used. If the value of <code>startIndex</code> equals the value of <code>endIndex</code>, the method returns an empty string. If the value of <code>startIndex</code> is greater than the value of <code>endIndex</code>, the parameters are automatically swapped before the function executes. The original string is unmodified.
   * @param startIndex An integer specifying the index of the first character used to create the substring. Valid values for <code>startIndex</code> are <code>0</code> through <code>String.length</code>. If <code>startIndex</code> is a negative value, <code>0</code> is used.
   * @param endIndex An integer that is one greater than the index of the last character in the extracted substring. Valid values for <code>endIndex</code> are <code>0</code> through <code>String.length</code>. The character at <code>endIndex</code> is not included in the substring. The default is the maximum value allowed for an index. If this parameter is omitted, <code>String.length</code> is used. If this parameter is a negative value, <code>0</code> is used.
   *
   * @return A substring based on the specified parameters.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f00.html Finding substrings and patterns in strings
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ef4.html Finding a substring by character position
   *
   */
  public native function substring(startIndex:Number = 0, endIndex:Number = 0x7fffffff):String;

  /**
   * Returns a copy of this string, with all uppercase characters converted to lowercase. The original string is unmodified. While this method is intended to handle the conversion in a locale-specific way, the ActionScript 3.0 implementation does not produce a different result from the <code>toLowerCase()</code> method.
   * @return A copy of this string with all uppercase characters converted to lowercase.
   *
   * @see #toLowerCase()
   *
   */
  public native function toLocaleLowerCase():String;

  /**
   * Returns a copy of this string, with all lowercase characters converted to uppercase. The original string is unmodified. While this method is intended to handle the conversion in a locale-specific way, the ActionScript 3.0 implementation does not produce a different result from the <code>toUpperCase()</code> method.
   * @return A copy of this string with all lowercase characters converted to uppercase.
   *
   * @see #toUpperCase()
   *
   */
  public native function toLocaleUpperCase():String;

  /**
   * Returns a copy of this string, with all uppercase characters converted to lowercase. The original string is unmodified.
   * <p>This method converts all characters (not simply A-Z) for which Unicode lowercase equivalents exist:</p>
   * <listing>
   *      var str:String = " JOSÉ BARÇA";
   *      trace(str.toLowerCase()); // josé barça
   * </listing>
   * <p>These case mappings are defined in the <a href="http://www.unicode.org/ucd/">Unicode Character Database</a> specification.</p>
   * @return A copy of this string with all uppercase characters converted to lowercase.
   *
   * @see #toUpperCase()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7eff.html Converting strings between uppercase and lowercase
   *
   */
  public native function toLowerCase():String;

  /**
   * Returns a copy of this string, with all lowercase characters converted to uppercase. The original string is unmodified.
   * <p>This method converts all characters (not simply a-z) for which Unicode uppercase equivalents exist:</p>
   * <listing>
   *      var str:String = "José Barça";
   *      trace(str.toUpperCase()); // JOSÉ BARÇA
   * </listing>
   * <p>These case mappings are defined in the <a href="http://www.unicode.org/ucd/">Unicode Character Database</a> specification.</p>
   * @return A copy of this string with all lowercase characters converted to uppercase.
   *
   * @see #toLowerCase()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7eff.html Converting strings between uppercase and lowercase
   *
   */
  public native function toUpperCase():String;

  /**
   * Returns the primitive value of a String instance. This method is designed to convert a String object into a primitive string value. Because Flash runtimes automatically call <code>valueOf()</code> when necessary, you rarely need to call this method explicitly.
   * @return The value of the string.
   *
   */
  public native function valueOf():String;


  // ***** deprecated JavaScript String methods that produce HTML:
  public native function anchor(nameAttribute:String):String;
  public native function big():String;
  public native function blink():String;
  public native function bold():String;
  public native function fixed():String;
  public native function fontcolor(color:String):String;
  public native function fontsize(size:Number):String;
  public native function italics():String;
  public native function link(href:String):String;
  public native function small():String;
  public native function strike():String;
  public native function sub():String;
  public native function sup():String;

}
}
