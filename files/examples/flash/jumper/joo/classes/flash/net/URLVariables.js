joo.classLoader.prepare("package flash.net",/* {*/

/**
 * The URLVariables class allows you to transfer variables between an application and a server. Use URLVariables objects with methods of the URLLoader class, with the <code>data</code> property of the URLRequest class, and with flash.net package functions.
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/net/URLVariables.html#includeExamplesSummary">View the examples</a></p>
 * @see URLLoader
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cfd.html Loading external data
 *
 */
"public dynamic class URLVariables",1,function($$private){;return[ 
  /**
   * Creates a new URLVariables object. You pass URLVariables objects to the <code>data</code> property of URLRequest objects.
   * <p>If you call the URLVariables constructor with a string, the <code>decode()</code> method is automatically called to convert the string to properties of the URLVariables object.</p>
   * @param source A URL-encoded string containing name/value pairs.
   *
   */
  "public function URLVariables",function URLVariables$(source/*:String = null*/) {switch(arguments.length){case 0:source = null;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Converts the variable string to properties of the specified URLVariables object.
   * <p>This method is used internally by the URLVariables events. Most users do not need to call this method directly.</p>
   * @param source A URL-encoded query string containing name/value pairs.
   *
   * @throws Error The source parameter must be a URL-encoded query string containing name/value pairs.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cfd.html Loading external data
   *
   * @example The following examples show how you can parse URL encoded strings. Example provided by <a href="http://actionscriptexamples.com/2008/02/27/decoding-url-encoded-strings-in-a-flash-application-using-the-urlvariables-class-in-actionscript-30/">ActionScriptExamples.com</a>.
   * <listing>
   * // The first method passes the string to be decoded to the URLVariables class constructor:
   * var urlVariables:URLVariables = new URLVariables("firstName=Tom&lastName=Jones");
   * lbl.text = urlVariables.lastName + "," + urlVariables.firstName;
   *
   * // The second method uses the decode() method to parse the URL encoded string:
   * var urlVariables:URLVariables = new URLVariables();
   * urlVariables.decode("firstName=Tom&lastName=Jones");
   * lbl.text = urlVariables.lastName + "," + urlVariables.firstName;
   * </listing>
   */
  "public function decode",function decode(source/*:String*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns a string containing all enumerable variables, in the MIME content encoding <i>application/x-www-form-urlencoded</i>.
   * @return A URL-encoded string containing name/value pairs.
   *
   */
  "public function toString",function toString()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);