joo.classLoader.prepare("package flash.net",/* {
import flash.events.EventDispatcher*/

/**
 * property AsyncErrorEvent.type =
 * @eventType flash.events.AsyncErrorEvent.ASYNC_ERROR
 */
{Event:{name:"asyncError", type:"flash.events.AsyncErrorEvent"}},
/**
 * property IOErrorEvent.type =
 * @eventType flash.events.IOErrorEvent.IO_ERROR
 */
{Event:{name:"ioError", type:"flash.events.IOErrorEvent"}},
/**
 * property NetStatusEvent.type =
 * @eventType flash.events.NetStatusEvent.NET_STATUS
 */
{Event:{name:"netStatus", type:"flash.events.NetStatusEvent"}},
/**
 * property SecurityErrorEvent.type =
 * @eventType flash.events.SecurityErrorEvent.SECURITY_ERROR
 */
{Event:{name:"securityError", type:"flash.events.SecurityErrorEvent"}},

/**
 * The NetConnection class creates a two-way connection between a client and a server. The client can be a Flash Player or AIR application. The server can be a web server, Flash Media Server, an application server running Flash Remoting, or the <a href="http://labs.adobe.com/technologies/stratus/">Adobe Stratus</a> service. Call <code>NetConnection.connect()</code> to establish the connection. Use the NetStream class to send streams of media and data over the connection.
 * <p>For security information about loading content and data into Flash Player and AIR, see the following:</p>
 * <ul>
 * <li>To load content and data into Flash Player from a web server or from a local location, see <a href="http://www.adobe.com/go/devnet_security_en">Flash Player Developer Center: Security</a>.</li>
 * <li>To load content and data into Flash Player and AIR from Flash Media Server, see the <a href="http://www.adobe.com/support/flashmediaserver">Flash Media Server documentation</a>.</li>
 * <li>To load content and data into AIR, see the <a href="http://www.adobe.com/devnet/air/">Adobe AIR Developer Center</a>.</li></ul>
 * <p>To write callback methods for this class, extend the class and define the callback methods in the subclass, or assign the <code>client</code> property to an object and define the callback methods on that object.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/net/NetConnection.html#includeExamplesSummary">View the examples</a></p>
 * @see #client
 * @see NetStream
 * @see #connect()
 * @see Responder
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7c5a.html Flash Platform security overview
 *
 */
"public class NetConnection extends flash.events.EventDispatcher",2,function($$private){;return[ 
  /**
   * Indicates the object on which callback methods are invoked. The default is this NetConnection instance. If you set the <code>client</code> property to another object, callback methods will be invoked on that object.
   * @throws TypeError The <code>client</code> property must be set to a non-null object.
   *
   */
  "public function get client",function client$get()/*:Object*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set client",function client$set(value/*:Object*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Indicates whether the application is connected to a server through a persistent RTMP connection (<code>true</code>) or not (<code>false</code>). When connected through HTTP, this property is <code>false</code>, except when connected to Flash Remoting services on an application server, in which case it is <code>true</code>.
   */
  "public function get connected",function connected$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The proxy type used to make a successful connection to Flash Media Server. Possible values are: <code>"none"</code>, <code>"HTTP"</code>, <code>"HTTPS"</code>, or <code>"CONNECT"</code>.
   * <p>The value is <code>"none"</code> if the connection is not tunneled or is a native SSL connection.</p>
   * <p>The value is <code>"HTTP"</code> if the connection is tunneled over HTTP.</p>
   * <p>The value is <code>"HTTPS"</code> if the connection is tunneled over HTTPS,</p>
   * <p>The value is <code>"CONNECT"</code> if the connection is tunneled using the CONNECT method through a proxy server.</p>
   * @throws ArgumentError An attempt was made to access this property when the NetConnection instance was not connected.
   *
   */
  "public function get connectedProxyType",function connectedProxyType$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The default object encoding for NetConnection objects. When an object is written to or read from binary data, the <code>defaultObjectEncoding</code> property indicates which Action Message Format (AMF) version is used to serialize the data: the ActionScript 3.0 format (<code>ObjectEncoding.AMF3</code>) or the ActionScript 1.0 and ActionScript 2.0 format (<code>ObjectEncoding.AMF0</code>).
   * <p>The default value is <code>ObjectEncoding.AMF3</code>. Changing <code>NetConnection.defaultObjectEncoding</code> does not affect existing NetConnection instances; it affects only instances that are created subsequently.</p>
   * <p>To set an object's encoding separately (rather than setting object encoding for the entire application), set the <code>objectEncoding</code> property of the NetConnection object instead.</p>
   * <p>For more detailed information, see the description of the <code>objectEncoding</code> property.</p>
   * @see #objectEncoding
   * @see ObjectEncoding
   *
   */
  "public static function get defaultObjectEncoding",function defaultObjectEncoding$get()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public static function set defaultObjectEncoding",function defaultObjectEncoding$set(value/*:uint*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The object encoding for this NetConnection instance.
   * <p>When an object is written to or read from binary data, the <code>defaultObjectEncoding</code> property indicates which Action Message Format (AMF) version is used to serialize the data: the ActionScript 3.0 format (<code>ObjectEncoding.AMF3</code>) or the ActionScript 1.0 and ActionScript 2.0 format (<code>ObjectEncoding.AMF0</code>). Set the <code>objectEncoding</code> property to set an AMF version for a NetConnection instance.</p>
   * <p>It's important to understand this property if your application needs to communicate with servers released prior to Flash Player 9. The following three scenarios are possible:</p>
   * <ul>
   * <li>Connecting to a server that supports AMF3 (for example, Flex Data Services 2 or Flash Media Server 3). The default value of <code>defaultObjectEncoding</code> is <code>ObjectEncoding.AMF3</code>. All NetConnection instances created in this file use AMF3 serialization, so you don't need to set the <code>objectEncoding</code> property.</li>
   * <li>Connecting to a server that doesn't support AMF3 (for example, Flash Media Server 2). In this scenario, set the static <code>NetConnection.defaultObjectEncoding</code> property to <code>ObjectEncoding.AMF0</code>. All NetConnection instances created in this SWF file use AMF0 serialization. You don't need to set the <code>objectEncoding</code> property.</li>
   * <li>Connecting to multiple servers that use different encoding versions. Instead of using <code>defaultObjectEncoding</code>, set the object encoding on a per-connection basis using the <code>objectEncoding</code> property for each connection. Set it to <code>ObjectEncoding.AMF0</code> to connect to servers that use AMF0 encoding, such as Flash Media Server 2, and set it to <code>ObjectEncoding.AMF3</code> to connect to servers that use AMF3 encoding, such as Flex Data Services 2.</li></ul>
   * <p>Once a NetConnection instance is connected, its <code>objectEncoding</code> property is read-only.</p>
   * <p>If you use the wrong encoding to connect to a server, the NetConnection object dispatches the <code>netStatus</code> event. The <code>NetStatusEvent.info</code> property contains an information object with a <code>code</code> property value of <code>NetConnection.Connect.Failed</code>, and a description explaining that the object encoding is incorrect.</p>
   * @throws ReferenceError An attempt was made to set the value of the <code>objectEncoding</code> property while the NetConnection instance was connected.
   * @throws ArgumentError This property was set to a value other than <code>ObjectEncoding.AMF0</code> or <code>ObjectEncoding.AMF3</code>.
   *
   * @see #defaultObjectEncoding
   * @see ObjectEncoding
   *
   */
  "public function get objectEncoding",function objectEncoding$get()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set objectEncoding",function objectEncoding$set(value/*:uint*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Determines which fallback methods are tried if an initial connection attempt to Flash Media Server fails. Set the <code>proxyType</code> property before calling the <code>NetConnection.connect()</code> method.
   * <p>Acceptable values are <code>"none"</code>, <code>"HTTP"</code>, <code>"CONNECT"</code>, and <code>"best"</code>.The default value is <code>"none"</code>.</p>
   * <p>To use native SSL, set the property to <code>"best"</code>. If the player cannot make a direct connection to the server (over the default port of 443 or over another port that you specify) and a proxy server is in place, the player tries to use the CONNECT method. If that attempt fails, the player tunnels over HTTPS.</p>
   * <p>If the property is set to <code>"HTTP"</code> and a direct connection fails, HTTP tunneling is used. If the property is set to <code>"CONNECT"</code> and a direct connection fails, the <code>CONNECT</code> method of tunneling is used. If that fails, the connection does not fall back to HTTP tunneling.</p>
   * <p>This property is applicable only when using RTMP, RTMPS, or RTMPT. The <code>CONNECT</code> method is applicable only to users who are connected to the network by a proxy server.</p>
   */
  "public function get proxyType",function proxyType$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set proxyType",function proxyType$set(value/*:String*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The URI passed to the NetConnection.connect() method. If <code>NetConnection.connect()</code> hasn't been called or if no URI was passed, this property is <code>undefined</code>.
   */
  "public function get uri",function uri$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Indicates whether a secure connection was made using native Transport Layer Security (TLS) rather than HTTPS. This property is valid only when a NetConnection object is connected.
   * @throws ArgumentError An attempt was made to access this property when the NetConnection instance was not connected.
   *
   */
  "public function get usingTLS",function usingTLS$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Creates a NetConnection object. Call the <code>connect()</code> method to make a connection.
   * <p>If an application needs to communicate with servers released prior to Flash Player 9, set the NetConnection object's <code>objectEncoding</code> property.</p>
   * <p>The following code creates a NetConnection object:</p>
   * <pre>     var nc:NetConnection = new NetConnection();
   </pre>
   * @see #connect()
   * @see #objectEncoding
   *
   */
  "public function NetConnection",function NetConnection$() {this.super$2();
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Adds a context header to the Action Message Format (AMF) packet structure. This header is sent with every future AMF packet. If you call <code>NetConnection.addHeader()</code> using the same name, the new header replaces the existing header, and the new header persists for the duration of the NetConnection object. You can remove a header by calling <code>NetConnection.addHeader()</code> with the name of the header to remove an undefined object.
   * @param operation Identifies the header and the ActionScript object data associated with it.
   * @param mustUnderstand A value of <code>true</code> indicates that the server must understand and process this header before it handles any of the following headers or messages.
   * @param param Any ActionScript object.
   *
   */
  "public function addHeader",function addHeader(operation/*:String*/, mustUnderstand/*:Boolean = false*/, param/*:Object = null*/)/*:void*/ {if(arguments.length<3){if(arguments.length<2){mustUnderstand = false;}param = null;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Calls a command or method on Flash Media Server or on an application server running Flash Remoting. Before calling <code>NetConnection.call()</code> you must call <code>NetConnection.connect()</code> to connect to the server. You must create a server-side function to pass to this method.
   * <p>You cannot connect to commonly reserved ports. For a complete list of blocked ports, see "Restricting Networking APIs" in the <i>ActionScript 3.0 Developer's Guide</i>.</p>
   * @param command A method specified in the form <code>[objectPath/]method</code>. For example, the <code>someObject/doSomething</code> command tells the remote server to call the <code>clientObject.someObject.doSomething()</code> method, with all the optional <code>... arguments</code> parameters. If the object path is missing, <code>clientObject.doSomething()</code> is invoked on the remote server.
   * <p>With Flash Media Server, <code>command</code> is the name of a function defined in an application's server-side script. You do not need to use an object path before <code>command</code> if the server-side script is placed at the root level of the application directory.</p>
   * @param responder An optional object that is used to handle return values from the server. The Responder object can have two defined methods to handle the returned result: <code>result</code> and <code>status</code>. If an error is returned as the result, <code>status</code> is invoked; otherwise, <code>result</code> is invoked. The Responder object can process errors related to specific operations, while the NetConnection object responds to errors related to the connection status.
   * @param rest Optional arguments that can be of any ActionScript type, including a reference to another ActionScript object. These arguments are passed to the method specified in the <code>command</code> parameter when the method is executed on the remote application server.
   * Events
   * <table>
   * <tr>
   * <td><code><b>securityError</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/SecurityErrorEvent.html"><code>SecurityErrorEvent</code></a></code> — A call attempted to communicate with a server outside the caller's security sandbox. You can avoid this problem by using a policy file on the server.</td></tr></table>
   * @see Responder
   *
   */
  "public function call",function call(command/*:String*/, responder/*:Responder, ...rest*/)/*:void*/ {var rest=Array.prototype.slice.call(arguments,2);
    throw new Error('not implemented'); // TODO: implement!
},
/**
 * Closes the connection that was opened locally or to the server and dispatches a <code>netStatus</code> event with a <code>code</code> property of <code>NetConnection.Connect.Closed</code>.
 * <p>This method disconnects all NetStream objects running over the connection. Any queued data that has not been sent is discarded. (To terminate local or server streams without closing the connection, use <code>NetStream.close()</code>.) If you close the connection and then want to create a new one, you must create a new NetConnection object and call the <code>connect()</code> method again.</p>
 * <p>The <code>close()</code> method also disconnects all remote shared objects running over this connection. However, you don't need to recreate the shared object to reconnect. Instead, you can just call <code>SharedObject.connect()</code> to reestablish the connection to the shared object. Also, any data in the shared object that was queued when you issued <code>NetConnection.close()</code> is sent after you reestablish a connection to the shared object.</p>
 * <p>With Flash Media Server, the best development practice is to call <code>close()</code> when the client no longer needs the connection to the server. Calling <code>close()</code> is the fastest way to clean up unused connections. You can configure the server to close idle connections automatically as a back-up measure. For more information, see the <i><a href="http://www.adobe.com/support/documentation">Flash Media Server Configuration and Administration Guide</a></i>.</p>
 * @see NetStream
 * @see flash.events.NetStatusEvent#info
 *
 */
"public function close",function close()/*:void*/ {
  throw new Error('not implemented'); // TODO: implement!
},

/**
 * Creates a two-way connection to an application on Flash Media Server or to Flash Remoting, or creates a two-way network endpoint for RTMFP peer-to-peer group communication. To report its status or an error condition, a call to <code>NetConnection.connect()</code> dispatches a <code>netStatus</code> event.
 * <p>Call <code>NetConnection.connect()</code> to do the following:</p>
 * <ul>
 * <li>Pass "null" to play video and mp3 files from a local file system or from a web server.</li>
 * <li>Pass an "http" URL to connect to an application server running Flash Remoting. Use the NetServices class to call functions on and return results from application servers over a NetConnection object. For more information, see the <a href="http://www.adobe.com/support/documentation">Flash Remoting documentation</a>.</li>
 * <li>Pass an "rtmp/e/s" URL to connect to a Flash Media Server application.</li>
 * <li>Pass an "rtmfp" URL to create a two-way network endpoint for RTMFP client-server, peer-to-peer, and IP multicast communication.</li>
 * <li>Pass the string "rtmfp:" to create a serverless two-way network endpoint for RTMFP IP multicast communication.</li></ul>
 * <p>Consider the following security model:</p>
 * <ul>
 * <li>By default, Flash Player or AIR denies access between sandboxes. A website can enable access to a resource by using a URL policy file.</li>
 * <li>Your application can deny access to a resource on the server. In a Flash Media Server application, use Server-Side ActionScript code to deny access. See the <a href="http://www.adobe.com/go/learn_fms_docs_en">Flash Media Server documentation</a>.</li>
 * <li>You cannot call <code>NetConnection.connect()</code> if the calling file is in the local-with-file-system sandbox.</li>
 * <li>You cannot connect to commonly reserved ports. For a complete list of blocked ports, see "Restricting Networking APIs" in the <i>ActionScript 3.0 Developer's Guide</i>.</li>
 * <li>To prevent a SWF file from calling this method, set the <code>allowNetworking</code> parameter of the the <code>object</code> and <code>embed</code> tags in the HTML page that contains the SWF content.</li></ul>
 * <p>However, in Adobe AIR, content in the <code>application</code> security sandbox (content installed with the AIR application) are not restricted by these security limitations.</p>
 * <p>For more information about security, see the Adobe Flash Player Developer Center: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</p>
 * @param command Use one of the following values for the <code>command</code> parameter:
 * <ul>
 * <li>To play video and mp3 files from a local file system or from a web server, pass <code>null</code>.</li>
 * <li>To connect to an application server running Flash Remoting, pass a URL that uses the <code>http</code> protocol.</li>
 * <li>(Flash Player 10.1 or AIR 2 or later) To create a serverless network endpoint for RTMFP IP multicast communication, pass the string <code>"rtmfp:"</code>. Use this connection type to receive an IP multicast stream from a publisher without using a server. You can also use this connection type to use IP multicast to discover peers on the same local area network (LAN).</li>
 * <li>
 * <p>This connection type has the following limitations:</p>
 * <p>Only peers on the same LAN can discover each other.</p>
 * <p>Using IP multicast, Flash Player can receive streams, it cannot send them.</p>
 * <p>Flash Player and AIR can send and receive streams in a peer-to-peer group, but the peers must be discovered on the same LAN using IP multicast.</p>
 * <p>This technique cannot be used for one-to-one communication.</p></li>
 * <li>To connect to Flash Media Server, pass the URI of the application on the server. Use the following syntax (items in brackets are optional):
 * <p><code>protocol:[//host][:port]/appname[/instanceName]</code></p>
 * <p>Use one of the following protocols: <code>rtmp</code>, <code>rtmpe</code>, <code>rtmps</code>, <code>rtmpt</code>, <code>rtmpte</code>, or <code>rtmfp</code>. If the connection is successful, a <code>netStatus</code> event with a <code>code</code> property of <code>NetConnection.Connect.Success</code> is returned. See the <code>NetStatusEvent.info</code> property for a list of all event codes returned in response to calling <code>connect()</code>.</p>
 * <p>If the file is served from the same host where the server is installed, you can omit the <code>//host</code> parameter. If you omit the <code>/instanceName</code> parameter, Flash Player or AIR connects to the application's default instance.</p>
 * <p>(Flash Player 10.1 or AIR 2 or later)To create peer-to-peer applications, use the <code>rtmfp</code> protocol.</p></li></ul>
 * @param rest Optional parameters of any type passed to the application specified in <code>command</code>. With Flash Media Server, the additional arguments are passed to the <code>application.onConnect()</code> event handler in the application's server-side code. You must define and handle the arguments in <code>onConnect()</code>.
 *
 * @throws ArgumentError The URI passed to the <code>command</code> parameter is improperly formatted.
 * @throws flash.errors.IOError The connection failed. This can happen if you call <code>connect()</code> from within a <code>netStatus</code> event handler, which is not allowed.
 * @throws SecurityError Local-with-filesystem SWF files cannot communicate with the Internet. You can avoid this problem by reclassifying the SWF file as local-with-networking or trusted.
 * @throws SecurityError You cannot connect to commonly reserved ports. For a complete list of blocked ports, see "Restricting Networking APIs" in the <i>ActionScript 3.0 Developer's Guide</i>.
 *
 * @see NetStream
 * @see flash.events.NetStatusEvent#info
 *
 */
"public function connect",function connect(command/*:String, ...rest*/)/*:void*/ {var rest=Array.prototype.slice.call(arguments,1);
  throw new Error('not implemented'); // TODO: implement!
},
];},["defaultObjectEncoding"],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.1"
);