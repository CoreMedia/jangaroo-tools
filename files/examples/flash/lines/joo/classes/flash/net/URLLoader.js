joo.classLoader.prepare("package flash.net",/* {
import flash.events.Event
import flash.events.EventDispatcher

import js.XMLHttpRequest*/

/**
 * Dispatched after all the received data is decoded and placed in the data property of the URLLoader object. The received data may be accessed once this event has been dispatched.
 * @eventType flash.events.Event.COMPLETE
 */
{Event:{name:"complete", type:"flash.events.Event"}},
/**
 * property HTTPStatusEvent.type =
 * @eventType flash.events.HTTPStatusEvent.HTTP_STATUS
 */
{Event:{name:"httpStatus", type:"flash.events.HTTPStatusEvent"}},
/**
 * property IOErrorEvent.type =
 * @eventType flash.events.IOErrorEvent.IO_ERROR
 */
{Event:{name:"ioError", type:"flash.events.IOErrorEvent"}},
/**
 * Dispatched when the download operation commences following a call to the <code>URLLoader.load()</code> method.
 * @eventType flash.events.Event.OPEN
 */
{Event:{name:"open", type:"flash.events.Event"}},
/**
 * property ProgressEvent.type =
 * @eventType flash.events.ProgressEvent.PROGRESS
 */
{Event:{name:"progress", type:"flash.events.ProgressEvent"}},
/**
 * property SecurityErrorEvent.type =
 * @eventType flash.events.SecurityErrorEvent.SECURITY_ERROR
 */
{Event:{name:"securityError", type:"flash.events.SecurityErrorEvent"}},

/**
 * The URLLoader class downloads data from a URL as text, binary data, or URL-encoded variables. It is useful for downloading text files, XML, or other information to be used in a dynamic, data-driven application.
 * <p>A URLLoader object downloads all of the data from a URL before making it available to code in the applications. It sends out notifications about the progress of the download, which you can monitor through the <code>bytesLoaded</code> and <code>bytesTotal</code> properties, as well as through dispatched events.</p>
 * <p>When loading very large video files, such as FLV's, out-of-memory errors may occur.</p>
 * <p>When you use this class in Flash Player and in AIR application content in security sandboxes other than then application security sandbox, consider the following security model:</p>
 * <ul>
 * <li>A SWF file in the local-with-filesystem sandbox may not load data from, or provide data to, a resource that is in the network sandbox.</li>
 * <li>By default, the calling SWF file and the URL you load must be in exactly the same domain. For example, a SWF file at www.adobe.com can load data only from sources that are also at www.adobe.com. To load data from a different domain, place a URL policy file on the server hosting the data.</li></ul>
 * <p>For more information related to security, see the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/net/URLLoader.html#includeExamplesSummary">View the examples</a></p>
 * @see URLRequest
 * @see URLVariables
 * @see URLStream
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e6a.html Reading external XML documents
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cfd.html Loading external data
 *
 */
"public class URLLoader extends flash.events.EventDispatcher",2,function($$private){var is=joo.is,$$bound=joo.boundMethod,trace=joo.trace;return[function(){joo.classLoader.init(flash.events.Event,js.XMLHttpRequest);}, 
  /**
   * Indicates the number of bytes that have been loaded thus far during the load operation.
   */
  "public var",{ bytesLoaded/*:uint*/ : 0},
  /**
   * Indicates the total number of bytes in the downloaded data. This property contains 0 while the load operation is in progress and is populated when the operation is complete. Also, a missing Content-Length header will result in bytesTotal being indeterminate.
   */
  "public var",{ bytesTotal/*:uint*/ : 0},
  /**
   * The data received from the load operation. This property is populated only when the load operation is complete. The format of the data depends on the setting of the <code>dataFormat</code> property:
   * <p>If the <code>dataFormat</code> property is <code>URLLoaderDataFormat.TEXT</code>, the received data is a string containing the text of the loaded file.</p>
   * <p>If the <code>dataFormat</code> property is <code>URLLoaderDataFormat.BINARY</code>, the received data is a ByteArray object containing the raw binary data.</p>
   * <p>If the <code>dataFormat</code> property is <code>URLLoaderDataFormat.VARIABLES</code>, the received data is a URLVariables object containing the URL-encoded variables.</p>
   * @see URLLoaderDataFormat
   * @see #dataFormat
   *
   * @example The following example shows how you can load an external text file with URL encoded variables into an ActionScript 3.0 document using the URLLoader class and setting the dataFormat property to the URLLoaderDataFormat.VARIABLES constant ("variables"). Example provided by <a href="http://actionscriptexamples.com/2008/02/27/loading-url-encoded-variables-into-a-flash-application-using-the-urlloader-class-in-actionscript-30/">ActionScriptExamples.com</a>.
   * <listing>
   * //params.txt is a local file that includes: firstName=Tom&lastName=Jones
   * var lbl:TextField = new TextField();
   * var urlRequest:URLRequest = new URLRequest("params.txt");
   * var urlLoader:URLLoader = new URLLoader();
   * urlLoader.dataFormat = URLLoaderDataFormat.VARIABLES;
   * urlLoader.addEventListener(Event.COMPLETE, urlLoader_complete);
   * urlLoader.load(urlRequest);
   *
   * function urlLoader_complete(evt:Event):void {
   *     lbl.text = urlLoader.data.lastName + "," + urlLoader.data.firstName;
   *     addChild(lbl);
   * }
   * </listing>
   */
  "public var",{ data/*:**/:undefined},

  /**
   * Controls whether the downloaded data is received as text (<code>URLLoaderDataFormat.TEXT</code>), raw binary data (<code>URLLoaderDataFormat.BINARY</code>), or URL-encoded variables (<code>URLLoaderDataFormat.VARIABLES</code>).
   * <p>If the value of the <code>dataFormat</code> property is <code>URLLoaderDataFormat.TEXT</code>, the received data is a string containing the text of the loaded file.</p>
   * <p>If the value of the <code>dataFormat</code> property is <code>URLLoaderDataFormat.BINARY</code>, the received data is a ByteArray object containing the raw binary data.</p>
   * <p>If the value of the <code>dataFormat</code> property is <code>URLLoaderDataFormat.VARIABLES</code>, the received data is a URLVariables object containing the URL-encoded variables.</p>
   * <p>The default value is <code>URLLoaderDataFormat.TEXT.</code></p>
   * @see URLLoaderDataFormat
   *
   * @example The following example shows how you can load external text files. Use the URLRequest and URLLoader classes, and then listen for the complete event. Example provided by <a href="http://actionscriptexamples.com/2008/02/26/loading-text-files-using-the-urlloader-class-in-actionscript-30/">ActionScriptExamples.com</a>.
   * <listing>
   * var PATH:String = "lorem.txt";
   * var urlRequest:URLRequest = new URLRequest(PATH);
   * var urlLoader:URLLoader = new URLLoader();
   * urlLoader.dataFormat = URLLoaderDataFormat.TEXT; // default
   * urlLoader.addEventListener(Event.COMPLETE, urlLoader_complete);
   * urlLoader.load(urlRequest);
   *
   * function urlLoader_complete(evt:Event):void {
   *     textArea.text = urlLoader.data;
   * }
   * </listing>
   */
  "public var",{ dataFormat/*:String*/ : "text"},

  /**
   * Creates a URLLoader object.
   * @param request A URLRequest object specifying the URL to download. If this parameter is omitted, no load operation begins. If specified, the load operation begins immediately (see the <code>load</code> entry for more information).
   *
   * @see #load()
   *
   */
  "public function URLLoader",function URLLoader$(request/*:URLRequest = null*/) {if(arguments.length<1){request = null;}this.super$2();
    if (request) {
      this.load(request);
    }
  },

  /**
   * Registers an event listener object with an EventDispatcher object so that the listener receives notification of an event. You can register event listeners on all nodes in the display list for a specific type of event, phase, and priority.
   * <p>After you successfully register an event listener, you cannot change its priority through additional calls to <code>addEventListener()</code>. To change a listener's priority, you must first call <code>removeListener()</code>. Then you can register the listener again with the new priority level.</p>
   * <p>Keep in mind that after the listener is registered, subsequent calls to <code>addEventListener()</code> with a different <code>type</code> or <code>useCapture</code> value result in the creation of a separate listener registration. For example, if you first register a listener with <code>useCapture</code> set to <code>true</code>, it listens only during the capture phase. If you call <code>addEventListener()</code> again using the same listener object, but with <code>useCapture</code> set to <code>false</code>, you have two separate listeners: one that listens during the capture phase and another that listens during the target and bubbling phases.</p>
   * <p>You cannot register an event listener for only the target phase or the bubbling phase. Those phases are coupled during registration because bubbling applies only to the ancestors of the target node.</p>
   * <p>If you no longer need an event listener, remove it by calling <code>removeEventListener()</code>, or memory problems could result. Event listeners are not automatically removed from memory because the garbage collector does not remove the listener as long as the dispatching object exists (unless the <code>useWeakReference</code> parameter is set to <code>true</code>).</p>
   * <p>Copying an EventDispatcher instance does not copy the event listeners attached to it. (If your newly created node needs an event listener, you must attach the listener after creating the node.) However, if you move an EventDispatcher instance, the event listeners attached to it move along with it.</p>
   * <p>If the event listener is being registered on a node while an event is being processed on this node, the event listener is not triggered during the current phase but can be triggered during a later phase in the event flow, such as the bubbling phase.</p>
   * <p>If an event listener is removed from a node while an event is being processed on the node, it is still triggered by the current actions. After it is removed, the event listener is never invoked again (unless registered again for future processing).</p>
   * @param type The type of event.
   * @param listener The listener function that processes the event. This function must accept an Event object as its only parameter and must return nothing, as this example shows:
   * <listing>
   * function(evt:Event):void</listing>
   * <p>The function can have any name.</p>
   * @param useCapture Determines whether the listener works in the capture phase or the target and bubbling phases. If <code>useCapture</code> is set to <code>true</code>, the listener processes the event only during the capture phase and not in the target or bubbling phase. If <code>useCapture</code> is <code>false</code>, the listener processes the event only during the target or bubbling phase. To listen for the event in all three phases, call <code>addEventListener</code> twice, once with <code>useCapture</code> set to <code>true</code>, then again with <code>useCapture</code> set to <code>false</code>.
   * @param priority The priority level of the event listener. The priority is designated by a signed 32-bit integer. The higher the number, the higher the priority. All listeners with priority <i>n</i> are processed before listeners of priority <i>n</i>-1. If two or more listeners share the same priority, they are processed in the order in which they were added. The default priority is 0.
   * @param useWeakReference Determines whether the reference to the listener is strong or weak. A strong reference (the default) prevents your listener from being garbage-collected. A weak reference does not.
   * <p>Class-level member functions are not subject to garbage collection, so you can set <code>useWeakReference</code> to <code>true</code> for class-level member functions without subjecting them to garbage collection. If you set <code>useWeakReference</code> to <code>true</code> for a listener that is a nested inner function, the function will be garbage-collected and no longer persistent. If you create references to the inner function (save it in another variable) then it is not garbage-collected and stays persistent.</p>
   *
   */
  "override public function addEventListener",function addEventListener(type/*:String*/, listener/*:Function*/, useCapture/*:Boolean = false*/, priority/*:int = 0*/, useWeakReference/*:Boolean = false*/)/*:void*/ {if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){useCapture = false;}priority = 0;}useWeakReference = false;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Closes the load operation in progress. Any load operation in progress is immediately terminated. If no URL is currently being streamed, an invalid stream error is thrown.
   */
  "public function close",function close()/*:void*/ {
    this.xmlHttpRequest$2.abort();
  },

  /**
   * Sends and loads data from the specified URL. The data can be received as text, raw binary data, or URL-encoded variables, depending on the value you set for the <code>dataFormat</code> property. Note that the default value of the <code>dataFormat</code> property is text. If you want to send data to the specified URL, you can set the <code>data</code> property in the URLRequest object.
   * <p><b>Note:</b> If a file being loaded contains non-ASCII characters (as found in many non-English languages), it is recommended that you save the file with UTF-8 or UTF-16 encoding as opposed to a non-Unicode format like ASCII.</p>
   * <p>A SWF file in the local-with-filesystem sandbox may not load data from, or provide data to, a resource that is in the network sandbox.</p>
   * <p>By default, the calling SWF file and the URL you load must be in exactly the same domain. For example, a SWF file at www.adobe.com can load data only from sources that are also at www.adobe.com. To load data from a different domain, place a URL policy file on the server hosting the data.</p>
   * <p>You cannot connect to commonly reserved ports. For a complete list of blocked ports, see "Restricting Networking APIs" in the <i>ActionScript 3.0 Developer's Guide</i>.</p>
   * <p>In Flash Player 10 and later, if you use a multipart Content-Type (for example "multipart/form-data") that contains an upload (indicated by a "filename" parameter in a "content-disposition" header within the POST body), the POST operation is subject to the security rules applied to uploads:</p>
   * <ul>
   * <li>The POST operation must be performed in response to a user-initiated action, such as a mouse click or key press.</li>
   * <li>If the POST operation is cross-domain (the POST target is not on the same server as the SWF file that is sending the POST request), the target server must provide a URL policy file that permits cross-domain access.</li></ul>
   * <p>Also, for any multipart Content-Type, the syntax must be valid (according to the RFC2046 standards). If the syntax appears to be invalid, the POST operation is subject to the security rules applied to uploads.</p>
   * <p>For more information related to security, see the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</p>
   * @param request A URLRequest object specifying the URL to download.
   * Events
   * <table>
   * <tr>
   * <td><code><b>complete</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/Event.html"><code>Event</code></a></code> — Dispatched after data has loaded successfully.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>httpStatus</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/HTTPStatusEvent.html"><code>HTTPStatusEvent</code></a></code> — If access is over HTTP, and the current Flash Player environment supports obtaining status codes, you may receive these events in addition to any <code>complete</code> or <code>error</code> event.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>ioError</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/IOErrorEvent.html"><code>IOErrorEvent</code></a></code> — The load operation could not be completed.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>progress</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/ProgressEvent.html"><code>ProgressEvent</code></a></code> — Dispatched when data is received as the download operation progresses.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>securityError</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/SecurityErrorEvent.html"><code>SecurityErrorEvent</code></a></code> — A load operation attempted to retrieve data from a server outside the caller's security sandbox. This may be worked around using a policy file on the server.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>securityError</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/SecurityErrorEvent.html"><code>SecurityErrorEvent</code></a></code> — A load operation attempted to load a SWZ file (a Adobe platform component), but the certificate is invalid or the digest does not match the component.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>open</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/Event.html"><code>Event</code></a></code> — Dispatched when a load operation commences.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>httpResponseStatus</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/HTTPStatusEvent.html"><code>HTTPStatusEvent</code></a></code> — Dispatched if a call to the <code>load()</code> method attempts to access data over HTTP and Adobe AIR is able to detect and return the status code for the request.</td></tr></table>
   * @throws ArgumentError <code>URLRequest.requestHeader</code> objects may not contain certain prohibited HTTP request headers. For more information, see the URLRequestHeader class description.
   * @throws flash.errors.MemoryError This error can occur for the following reasons: 1) Flash Player or AIR cannot convert the <code>URLRequest.data</code> parameter from UTF8 to MBCS. This error is applicable if the URLRequest object passed to <code>load()</code> is set to perform a <code>GET</code> operation and if <code>System.useCodePage</code> is set to <code>true</code>. 2) Flash Player or AIR cannot allocate memory for the <code>POST</code> data. This error is applicable if the URLRequest object passed to <code>load</code> is set to perform a <code>POST</code> operation.
   * @throws SecurityError Local untrusted files may not communicate with the Internet. This may be worked around by reclassifying this file as local-with-networking or trusted.
   * @throws SecurityError You are trying to connect to a commonly reserved port. For a complete list of blocked ports, see "Restricting Networking APIs" in the <i>ActionScript 3.0 Developer's Guide</i>.
   * @throws TypeError The value of the request parameter or the <code>URLRequest.url</code> property of the URLRequest object passed are <code>null</code>.
   *
   * @see URLRequestHeader
   * @see URLRequest#requestHeaders
   * @see URLRequest#data
   * @see URLRequest#digest
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cfd.html Loading external data
   *
   * @example In the following example, an XML files is loaded and the content of its elements' first arguments are displayed in a text field.
   * <p>A <code>URLRequest</code> object is created to identify the location of the XML file, which for this example is in the same directory as the SWF file. The file is loaded in a <code>try...catch</code> block in order to catch any error that may occur. (Here we catch the <code>SecurityError</code> errors.) If an <code>IO_ERROR</code> event occurs, the <code>errorHandler()</code> method is invoked, which writes an error message in the <code>xmlTextField</code> text field. Once the XML file data is received and place in the data property of the <code>loader</code> URLLoader object, the <code>Event.COMPLETE</code> event is dispatched and the <code>loaderCompleteHandler()</code> method is invoked.</p>
   * <p>In the <code>loaderCompleteHandler()</code> method, a <code>try...catch</code> block is used to catch any parsing error that may occur while converting the loaded data from the file into an XML object. The <code>readNodes()</code> method then recursively goes through all the elements in the nodes of the XML document and appends the <code>xmlTextField</code> text field with a list of the first attributes of all the elements.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.events.Event;
   *     import flash.net.URLLoader;
   *     import flash.net.URLRequest;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldAutoSize;
   *     import flash.xml.*;
   *     import flash.events.IOErrorEvent;
   *
   *     public class URLLoader_loadExample extends Sprite {
   *         private var xmlTextField:TextField = new TextField();
   *         private var externalXML:XML;
   *         private var loader:URLLoader;
   *
   *         public function URLLoader_loadExample() {
   *             var request:URLRequest = new URLRequest("xmlFile.xml");
   *
   *             loader = new URLLoader();
   *
   *             try {
   *                 loader.load(request);
   *             }
   *             catch (error:SecurityError)
   *             {
   *                 trace("A SecurityError has occurred.");
   *             }
   *
   *              loader.addEventListener(IOErrorEvent.IO_ERROR, errorHandler);
   *             loader.addEventListener(Event.COMPLETE, loaderCompleteHandler);
   *
   *             xmlTextField.x = 10;
   *             xmlTextField.y = 10;
   *             xmlTextField.background = true;
   *             xmlTextField.autoSize = TextFieldAutoSize.LEFT;
   *
   *             addChild(xmlTextField);
   *         }
   *
   *         private function loaderCompleteHandler(event:Event):void {
   *
   *                 try {
   *                     externalXML = new XML(loader.data);
   *                     readNodes(externalXML);
   *                 } catch (e:TypeError) {
   *                     trace("Could not parse the XML file.");
   *                 }
   *         }
   *
   *         private function readNodes(node:XML):void {
   *
   *                 for each (var element:XML in node.elements()) {
   *                     xmlTextField.appendText(element.attributes()[0] + "\n");
   *
   *                     readNodes(element);
   *                 }
   *         }
   *
   *         private function errorHandler(e:IOErrorEvent):void {
   *             xmlTextField.text = "Had problem loading the XML File.";
   *         }
   *     }
   * }
   * </listing>
   */
  "public function load",function load(request/*:URLRequest*/)/*:void*/ {
    try {
      this.xmlHttpRequest$2 = new js.XMLHttpRequest();
    } catch(e){if(is(e,Error)) {
      throw new Error("Your browser does not support XMLHttpRequest: " + e.message);
    }else throw e;}
    this.xmlHttpRequest$2.onreadystatechange = $$bound(this,"readyStateChanged$2");
    this.xmlHttpRequest$2.open(request.method, request.url, true);
    this.xmlHttpRequest$2.send(null);
  },

  "private function readyStateChanged",function readyStateChanged()/*:void*/ {
    trace("URLLoader: " + this.xmlHttpRequest$2.readyState);
    if (this.xmlHttpRequest$2.readyState == js.XMLHttpRequest.DONE) {
      this.data = this.xmlHttpRequest$2.responseText;
    }
    var event/*:Event*/ = this.createEvent$2();
    if (event) {
      this.dispatchEvent(event);
    }
  },

  "private function createEvent",function createEvent()/*:Event*/ {
    switch (this.xmlHttpRequest$2.readyState) {
      case js.XMLHttpRequest.OPENED: return new flash.events.Event(flash.events.Event.OPEN, false, false);
      case js.XMLHttpRequest.DONE: return new flash.events.Event(flash.events.Event.COMPLETE, false, false);
    }
    return null;
  },

  "private var",{ xmlHttpRequest/*:XMLHttpRequest*/:null},
];},[],["flash.events.EventDispatcher","Error","js.XMLHttpRequest","flash.events.Event"], "0.8.0", "0.8.1"
);