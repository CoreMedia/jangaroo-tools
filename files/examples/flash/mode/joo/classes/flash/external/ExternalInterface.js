joo.classLoader.prepare("package flash.external",/* {*/



/**
 * The ExternalInterface class is an application programming interface that enables straightforward communication between ActionScript and the SWF containerâ€“ for example, an HTML page with JavaScript or a desktop application that uses Flash Player to display a SWF file.
 * <p>Using the ExternalInterface class, you can call an ActionScript function in the Flash runtime, using JavaScript in the HTML page. The ActionScript function can return a value, and JavaScript receives it immediately as the return value of the call.</p>
 * <p>This functionality replaces the <code>fscommand()</code> method.</p>
 * <p>Use the ExternalInterface class in the following combinations of browser and operating system:</p>
 * <table>
 * <tr><th>Browser</th><th>Operating System</th><th>Operating System</th></tr>
 * <tr>
 * <td>Internet Explorer 5.0 and later</td>
 * <td> Windows </td>
 * <td> </td></tr>
 * <tr>
 * <td>Netscape 8.0 and later</td>
 * <td> Windows </td>
 * <td> MacOS </td></tr>
 * <tr>
 * <td>Mozilla 1.7.5 and later</td>
 * <td> Windows </td>
 * <td> MacOS </td></tr>
 * <tr>
 * <td>Firefox 1.0 and later</td>
 * <td> Windows </td>
 * <td> MacOS </td></tr>
 * <tr>
 * <td>Safari 1.3 and later</td>
 * <td> </td>
 * <td> MacOS </td></tr></table>
 * <p>Flash Player for Linux version 9.0.31.0 and later supports the ExternalInterface class in the following browsers:</p>
 * <table>
 * <tr><th>Browser</th></tr>
 * <tr>
 * <td>Mozilla 1.7.x and later</td></tr>
 * <tr>
 * <td>Firefox 1.5.0.7 and later</td></tr>
 * <tr>
 * <td>SeaMonkey 1.0.5 and later</td></tr></table>
 * <p>The ExternalInterface class requires the user's web browser to support either ActiveX<sup>®</sup> or the NPRuntime API that is exposed by some browsers for plug-in scripting. Even if a browser and operating system combination are not listed above, they should support the ExternalInterface class if they support the NPRuntime API. See <a href="http://www.mozilla.org/projects/plugins/npruntime.html">http://www.mozilla.org/projects/plugins/npruntime.html</a>.</p>
 * <p><b>Note:</b> When embedding SWF files within an HTML page, make sure that the <code>id</code> attribute is set and the <code>id</code> and <code>name</code> attributes of the <code>object</code> and <code>embed</code> tags do not include the following characters:</p>
 * <pre> . - + * / \
 </pre>
 * <p><b>Note for Flash Player applications:</b> Flash Player version 9.0.115.0 and later allows the <code>.</code> (period) character within the <code>id</code> and <code>name</code> attributes.</p>
 * <p><b>Note for Flash Player applications:</b> In Flash Player 10 and later running in a browser, using this class programmatically to open a pop-up window may not be successful. Various browsers (and browser configurations) may block pop-up windows at any time; it is not possible to guarantee any pop-up window will appear. However, for the best chance of success, use this class to open a pop-up window only in code that executes as a direct result of a user action (for example, in an event handler for a mouse click or key-press event.)</p>
 * <p>From ActionScript, you can do the following on the HTML page:</p>
 * <ul>
 * <li>Call any JavaScript function.</li>
 * <li>Pass any number of arguments, with any names.</li>
 * <li>Pass various data types (Boolean, Number, String, and so on).</li>
 * <li>Receive a return value from the JavaScript function.</li></ul>
 * <p>From JavaScript on the HTML page, you can:</p>
 * <ul>
 * <li>Call an ActionScript function.</li>
 * <li>Pass arguments using standard function call notation.</li>
 * <li>Return a value to the JavaScript function.</li></ul>
 * <p><b>Note for Flash Player applications:</b> Flash Player does not currently support SWF files embedded within HTML forms.</p>
 * <p><b>Note for AIR applications:</b> In Adobe AIR, the ExternalInterface class can be used to communicate between JavaScript in an HTML page loaded in the HTMLLoader control and ActionScript in SWF content embedded in that HTML page.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/external/ExternalInterface.html#includeExamplesSummary">View the examples</a></p>
 * @see flash.system.package#fscommand()
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf69084-7f31.html Using the ExternalInterface API to access JavaScript
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf69084-7e92.html Accessing Flex from JavaScript
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf69084-7ea6.html About ExternalInterface API security in Flex
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cab.html External API requirements and advantages
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cb2.html Using the ExternalInterface class
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7c9b.html Controlling outbound URL access
 *
 */
"public final class ExternalInterface",1,function($$private){;return[ 
  /**
   * Indicates whether this player is in a container that offers an external interface. If the external interface is available, this property is <code>true</code>; otherwise, it is <code>false</code>.
   * <p><b>Note:</b> When using the External API with HTML, always check that the HTML has finished loading before you attempt to call any JavaScript methods.</p>
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cb2.html Using the ExternalInterface class
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ca9.html Getting information about the external container
   *
   * @example The following example uses the <code>available</code> property to determine whether the player is in a container that offers an external interface.
   * <listing>
   *      package {
   *        import flash.text.TextField;
   *        import flash.display.MovieClip;
   *        import flash.external.ExternalInterface;
   *
   *        public class extint_test extends MovieClip {
   *          public function extint_test() {
   *            var isAvailable:Boolean = ExternalInterface.available;
   *            var availTxt:TextField = new TextField();
   *            availTxt.text = isAvailable.toString();
   *            addChild(availTxt);
   *          }
   *        }
   *      }
   *     </listing>
   */
  "public static function get available",function available$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Indicates whether the external interface should attempt to pass ActionScript exceptions to the current browser and JavaScript exceptions to the player. You must explicitly set this property to <code>true</code> to catch JavaScript exceptions in ActionScript and to catch ActionScript exceptions in JavaScript.
   * @see #addCallBack()
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/statements.html#try..catch..finally try..catch..finally statement
   *
   * @example The following example creates an ActionScript function and registers it with the containing browser by using the <code>addCallback()</code> method. The new function throws an exception so that JavaScript code running in the browser can catch it. This example also contains a <code>try..catch</code> statement to catch any exceptions thrown by the browser when the <code>throwit()</code> function is called.
   * <listing>
   * package
   * {
   *     import flash.external.*
   *     import flash.net.*;
   *     import flash.display.*;
   *     import flash.system.System;
   *     public class ext_test extends Sprite {
   *     function ext_test():void {
   *         ExternalInterface.marshallExceptions = true;
   *         ExternalInterface.addCallback("g", g);
   *
   *         try {
   *         ExternalInterface.call("throwit");
   *         } catch(e:Error) {
   *         trace(e)
   *         }
   *     }
   *     function g() { throw new Error("exception from actionscript!!!!") }
   *     }
   * }
   * </listing>
   */
  "public static var",{ marshallExceptions/*:Boolean*/ : false},

  /**
   * Returns the <code>id</code> attribute of the <code>object</code> tag in Internet Explorer, or the <code>name</code> attribute of the <code>embed</code> tag in Netscape.
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cb2.html Using the ExternalInterface class
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ca9.html Getting information about the external container
   *
   */
  "public static function get objectID",function objectID$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Registers an ActionScript method as callable from the container. After a successful invocation of <code>addCallBack()</code>, the registered function in the player can be called by JavaScript or ActiveX code in the container.
   * <p><b>Note:</b> For <i>local</i> content running in a browser, calls to the <code>ExternalInterface.addCallback()</code> method work only if the SWF file and the containing web page are in the local-trusted security sandbox. For more information, see the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</p>
   * @param functionName The name by which the container can invoke the function.
   * @param closure The function closure to invoke. This could be a free-standing function, or it could be a method closure referencing a method of an object instance. By passing a method closure, you can direct the callback at a method of a particular object instance.
   * <p><b>Note:</b> Repeating <code>addCallback()</code> on an existing callback function with a <code>null</code> closure value removes the callback.</p>
   *
   * @throws Error The container does not support incoming calls. Incoming calls are supported only in Internet Explorer for Windows and browsers that use the NPRuntime API such as Mozilla 1.7.5 and later or Firefox 1.0 and later.
   * @throws SecurityError A callback with the specified name has already been added by ActionScript in a sandbox to which you do not have access; you cannot overwrite that callback. To work around this problem, rewrite the ActionScript that originally called the <code>addCallback()</code> method so that it also calls the <code>Security.allowDomain()</code> method.
   * @throws SecurityError The containing environment belongs to a security sandbox to which the calling code does not have access. To fix this problem, follow these steps: <ol>
   * <li>In the <code>object</code> tag for the SWF file in the containing HTML page, set the following parameter:
   * <p><code><param name="allowScriptAccess" value="always" /></code></p></li>
   * <li>In the SWF file, add the following ActionScript:
   * <p><code>flash.system.Security.allowDomain(<i>sourceDomain</i>)</code></p></li></ol>
   *
   * @see flash.system.Security#allowDomain()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cb2.html Using the ExternalInterface class
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ca7.html Calling ActionScript code from the container
   *
   */
  "public static function addCallback",function addCallback(functionName/*:String*/, closure/*:Function*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Calls a function exposed by the SWF container, passing zero or more arguments. If the function is not available, the call returns <code>null</code>; otherwise it returns the value provided by the function. Recursion is <i>not</i> permitted on Opera or Netscape browsers; on these browsers a recursive call produces a <code>null</code> response. (Recursion is supported on Internet Explorer and Firefox browsers.)
   * <p>If the container is an HTML page, this method invokes a JavaScript function in a <code>script</code> element.</p>
   * <p>If the container is another ActiveX container, this method dispatches the FlashCall ActiveX event with the specified name, and the container processes the event.</p>
   * <p>If the container is hosting the Netscape plug-in, you can either write custom support for the new NPRuntime interface or embed an HTML control and embed the player within the HTML control. If you embed an HTML control, you can communicate with the player through a JavaScript interface to the native container application.</p>
   * <p><b>Note:</b> For <i>local</i> content running in a browser, calls to the <code>ExternalInterface.call()</code> method are permitted only if the SWF file and the containing web page (if there is one) are in the local-trusted security sandbox. Also, you can prevent a SWF file from using this method by setting the <code>allowNetworking</code> parameter of the <code>object</code> and <code>embed</code> tags in the HTML page that contains the SWF content. For more information, see the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</p>
   * <p><b>Note for Flash Player applications:</b> In Flash Player 10 and Flash Player 9 Update 5, some web browsers restrict this method if a pop-up blocker is enabled. In this scenario, you can only call this method successfully in response to a user event (for example, in an event handler for a mouse click or keypress event).</p>
   * @param functionName The alphanumeric name of the function to call in the container. Using a non-alphanumeric function name causes a runtime error (error 2155). You can use a <code>try..catch</code> block to handle the error.
   * @param rest The arguments to pass to the function in the container. You can specify zero or more parameters, separating them with commas. They can be of any ActionScript data type. When the call is to a JavaScript function, the ActionScript types are automatically converted into JavaScript types; when the call is to some other ActiveX container, the parameters are encoded in the request message.
   *
   * @return The response received from the container. If the call failedâ€“ for example, if there is no such function in the container, the interface is not available, a recursion occurred (with a Netscape or Opera browser), or there is a security issueâ€“ <code>null</code> is returned and an error is thrown.
   *
   * @throws Error The container does not support outgoing calls. Outgoing calls are supported only in Internet Explorer for Windows and browsers that use the NPRuntime API such as Mozilla 1.7.5 and later or Firefox 1.0 and later.
   * @throws SecurityError The containing environment belongs to a security sandbox to which the calling code does not have access. To fix this problem, follow these steps: <ol>
   * <li>In the <code>object</code> tag for the SWF file in the containing HTML page, set the following parameter:
   * <p><code><param name="allowScriptAccess" value="always" /></code></p></li>
   * <li>In the SWF file, add the following ActionScript:
   * <p><code>flash.system.Security.allowDomain(<i>sourceDomain</i>)</code></p></li></ol>
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cb2.html Using the ExternalInterface class
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ca8.html Calling external code from ActionScript
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7c9b.html Controlling outbound URL access
   *
   * @example The following example shows how you can use the ExternalInterface class (flash.external.ExternalInterface) to send a string from Flash Player to the HTML container where it is displayed using the JavaScript alert() function. Example provided by <a href="http://actionscriptexamples.com/2008/02/28/using-the-externalinterface-class-in-actionscript-20-and-actionscript-30/">ActionScriptExamples.com</a>.
   * <listing>
   * //
   * // Requires:
   * //   - A Flash Professional Label component on the Stage with an instance name of "lbl".
   * //   - A Flash Professional Button component on the Stage with an instance name of "button".
   * //
   * var xmlResponse:String = "<invoke name=\"isReady\" returntype=\"xml\"><arguments><number>1</number><number>" + stage.stageWidth + "</number><number>" + stage.stageHeight + "</number></arguments></invoke>";
   *
   * lbl.text = "ExternalInterface.available: " + ExternalInterface.available;
   * lbl.width = 200;
   * button.enabled = ExternalInterface.available;
   * button.addEventListener(MouseEvent.CLICK, button_click);
   *
   * function button_click(evt:MouseEvent):void {
   *     ExternalInterface.call("alert", xmlResponse);
   * }
   * </listing>
   */
  "public static function call",function call(functionName/*:String, ...rest*/)/*:**/ {var rest=Array.prototype.slice.call(arguments,1);
    throw new Error('not implemented'); // TODO: implement!
  },
];},["available","objectID","addCallback","call"],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);