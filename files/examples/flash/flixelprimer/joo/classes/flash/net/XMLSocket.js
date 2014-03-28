joo.classLoader.prepare("package flash.net",/* {
import flash.events.EventDispatcher*/

/**
 * Dispatched when the server closes the socket connection. The <code>close</code> event is dispatched only when the server closes the connection; it is not dispatched when you call the <code>XMLSocket.close()</code> method.
 * @eventType flash.events.Event.CLOSE
 */
{Event:{name:"close", type:"flash.events.Event"}},
/**
 * Dispatched after a successful call to the <code>XMLSocket.connect()</code> method.
 * @eventType flash.events.Event.CONNECT
 */
{Event:{name:"connect", type:"flash.events.Event"}},
/**
 * property DataEvent.type =
 * @eventType flash.events.DataEvent.DATA
 */
{Event:{name:"data", type:"flash.events.DataEvent"}},
/**
 * property IOErrorEvent.type =
 * @eventType flash.events.IOErrorEvent.IO_ERROR
 */
{Event:{name:"ioError", type:"flash.events.IOErrorEvent"}},
/**
 * property SecurityErrorEvent.type =
 * @eventType flash.events.SecurityErrorEvent.SECURITY_ERROR
 */
{Event:{name:"securityError", type:"flash.events.SecurityErrorEvent"}},

/**
 * The XMLSocket class implements client sockets that let the Flash Player or AIR application communicate with a server computer identified by an IP address or domain name. The XMLSocket class is useful for client-server applications that require low latency, such as real-time chat systems. A traditional HTTP-based chat solution frequently polls the server and downloads new messages using an HTTP request. In contrast, an XMLSocket chat solution maintains an open connection to the server, which lets the server immediately send incoming messages without a request from the client. To use the XMLSocket class, the server computer must run a daemon that understands the protocol used by the XMLSocket class. The protocol is described in the following list:
 * <ul>
 * <li>XML messages are sent over a full-duplex TCP/IP stream socket connection.</li>
 * <li>Each XML message is a complete XML document, terminated by a zero (0) byte.</li>
 * <li>An unlimited number of XML messages can be sent and received over a single XMLSocket connection.</li></ul>
 * <p>Setting up a server to communicate with the XMLSocket object can be challenging. If your application does not require real-time interactivity, use the URLLoader class instead of the XMLSocket class.</p>
 * <p>To use the methods of the XMLSocket class, first use the constructor, <code>new XMLSocket</code>, to create an XMLSocket object.</p>
 * <p>SWF files in the local-with-filesystem sandbox may not use sockets.</p>
 * <p><i>Socket policy files</i> on the target host specify the hosts from which SWF files can make socket connections, and the ports to which those connections can be made. The security requirements with regard to socket policy files have become more stringent in the last several releases of Flash Player. In all versions of Flash Player, Adobe recommends the use of a socket policy file; in some circumstances, a socket policy file is required. Therefore, if you are using XMLSocket objects, make sure that the target host provides a socket policy file if necessary.</p>
 * <p>The following list summarizes the requirements for socket policy files in different versions of Flash Player:</p>
 * <ul>
 * <li>In Flash Player 9.0.124.0 and later, a socket policy file is required for any XMLSocket connection. That is, a socket policy file on the target host is required no matter what port you are connecting to, and is required even if you are connecting to a port on the same host that is serving the SWF file.</li>
 * <li>In Flash Player versions 9.0.115.0 and earlier, if you want to connect to a port number below 1024, or if you want to connect to a host other than the one serving the SWF file, a socket policy file on the target host is required.</li>
 * <li>In Flash Player 9.0.115.0, even if a socket policy file isn't required, a warning is displayed when using the Flash Debug Player if the target host doesn't serve a socket policy file.</li></ul>
 * <p>However, in Adobe AIR, content in the <code>application</code> security sandbox (content installed with the AIR application) are not restricted by these security limitations.</p>
 * <p>For more information related to security, see the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/net/XMLSocket.html#includeExamplesSummary">View the examples</a></p>
 * @see URLLoader#load()
 * @see URLLoader
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cfb.html Binary client sockets
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7c63.html Connecting to sockets
 *
 */
"public class XMLSocket extends flash.events.EventDispatcher",2,function($$private){;return[ 
  /**
   * Indicates whether this XMLSocket object is currently connected. You can also check whether the connection succeeded by registering for the <code>connect</code> event and <code>ioError</code> event.
   * @see #event:connect
   * @see #event:ioError
   *
   */
  "public function get connected",function connected$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Creates a new XMLSocket object. If no parameters are specified, an initially disconnected socket is created. If parameters are specified, a connection is attempted to the specified host and port.
   * <p><b>Note:</b> It is strongly advised to use the constructor form <b>without parameters</b>, then add any event listeners, then call the <code>connect</code> method with <code>host</code> and <code>port</code> parameters. This sequence guarantees that all event listeners will work properly.</p>
   * @param host A fully qualified DNS domain name or an IP address in the form <i>.222.333.444</i>. In Flash Player 9.0.115.0 and AIR 1.0 and later, you can specify IPv6 addresses, such as rtmp://[2001:db8:ccc3:ffff:0:444d:555e:666f]. You can also specify <code>null</code> to connect to the host server on which the SWF file resides. If the SWF file issuing this call is running in a web browser, <code>host</code> must be in the same domain as the SWF file.
   * @param port The TCP port number on the target host used to establish a connection. In Flash Player 9.0.124.0 and later, the target host must serve a socket policy file specifying that socket connections are permitted from the host serving the SWF file to the specified port. In earlier versions of Flash Player, a socket policy file is required only if you want to connect to a port number below 1024, or if you want to connect to a host other than the one serving the SWF file.
   *
   * @see #connect()
   *
   */
  "public function XMLSocket",function XMLSocket$(host/*:String = null*/, port/*:int = 0*/) {if(arguments.length<2){if(arguments.length<1){host = null;}port = 0;}this.super$2();
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Closes the connection specified by the XMLSocket object. The <code>close</code> event is dispatched only when the server closes the connection; it is not dispatched when you call the <code>close()</code> method.
   * @see #connect()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cfb.html Binary client sockets
   *
   */
  "public function close",function close()/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Establishes a connection to the specified Internet host using the specified TCP port.
   * <p>If you specify <code>null</code> for the <code>host</code> parameter, the host contacted is the one where the file calling <code>XMLSocket.connect()</code> resides. For example, if the calling file was downloaded from www.adobe.com, specifying <code>null</code> for the host parameter means you are connecting to www.adobe.com.</p>
   * <p>You can prevent a file from using this method by setting the <code>allowNetworking</code> parameter of the the <code>object</code> and <code>embed</code> tags in the HTML page that contains the SWF content.</p>
   * <p>For more information, see the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</p>
   * @param host A fully qualified DNS domain name or an IP address in the form <i>111.222.333.444</i>. You can also specify <code>null</code> to connect to the host server on which the SWF file resides. If the calling file is a SWF file running in a web browser, <code>host</code> must be in the same domain as the file.
   * @param port The TCP port number on the target host used to establish a connection. In Flash Player 9.0.124.0 and later, the target host must serve a socket policy file specifying that socket connections are permitted from the host serving the SWF file to the specified port. In earlier versions of Flash Player, a socket policy file is required only if you want to connect to a port number below 1024, or if you want to connect to a host other than the one serving the SWF file.
   * Events
   * <table>
   * <tr>
   * <td><code><b>securityError</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/SecurityErrorEvent.html"><code>SecurityErrorEvent</code></a></code> — A connect operation attempted to connect to a host outside the caller's security sandbox, or to a port that requires a socket policy file. Work around either problem by using a socket policy file on the target host.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>data</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/DataEvent.html"><code>DataEvent</code></a></code> — Dispatched when raw data has been received.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>connect</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/Event.html"><code>Event</code></a></code> — Dispatched when network connection has been established.</td></tr></table>
   * @throws SecurityError Local untrusted files may not communicate with the Internet. Work around this limitation by reclassifying the file as local-with-networking or trusted.
   * @throws SecurityError You may not specify a socket port higher than 65535.
   *
   * @see flash.events.Event#CONNECT
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cfb.html Binary client sockets
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7c63.html Connecting to sockets
   *
   */
  "public function connect",function connect(host/*:String*/, port/*:int*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Converts the XML object or data specified in the <code>object</code> parameter to a string and transmits it to the server, followed by a zero (0) byte. If <code>object</code> is an XML object, the string is the XML textual representation of the XML object. The send operation is asynchronous; it returns immediately, but the data may be transmitted at a later time. The <code>XMLSocket.send()</code> method does not return a value indicating whether the data was successfully transmitted.
   * <p>If you do not connect the XMLSocket object to the server using <code>XMLSocket.connect()</code>), the <code>XMLSocket.send()</code> operation fails.</p>
   * @param object An XML object or other data to transmit to the server.
   *
   * @throws flash.errors.IOError The XMLSocket object is not connected to the server.
   *
   * @see #connect()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cfb.html Binary client sockets
   *
   */
  "public function send",function send(object/*:**/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.3"
);