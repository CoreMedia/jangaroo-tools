joo.classLoader.prepare("package flash.net",/* {
import flash.events.EventDispatcher
import flash.utils.ByteArray
import flash.utils.IDataInput*/

/**
 * Dispatched when data has loaded successfully.
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
 * Dispatched when a load operation starts.
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
 * The URLStream class provides low-level access to downloading URLs. Data is made available to application code immediately as it is downloaded, instead of waiting until the entire file is complete as with URLLoader. The URLStream class also lets you close a stream before it finishes downloading. The contents of the downloaded file are made available as raw binary data.
 * <p>The read operations in URLStream are nonblocking. This means that you must use the <code>bytesAvailable</code> property to determine whether sufficient data is available before reading it. An <code>EOFError</code> exception is thrown if insufficient data is available.</p>
 * <p>All binary data is encoded by default in big-endian format, with the most significant byte first.</p>
 * <p>The security rules that apply to URL downloading with the URLStream class are identical to the rules applied to URLLoader objects. Policy files may be downloaded as needed. Local file security rules are enforced, and security warnings are raised as needed.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/net/URLStream.html#includeExamplesSummary">View the examples</a></p>
 * @see URLLoader
 * @see URLRequest
 *
 */
"public class URLStream extends flash.events.EventDispatcher implements flash.utils.IDataInput",2,function($$private){;return[ 
  /**
   * Returns the number of bytes of data available for reading in the input buffer. Your code must call the <code>bytesAvailable</code> property to ensure that sufficient data is available before you try to read it with one of the <code>read</code> methods.
   */
  "public function get bytesAvailable",function bytesAvailable$get()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Indicates whether this URLStream object is currently connected. A call to this property returns a value of <code>true</code> if the URLStream object is connected, or <code>false</code> otherwise.
   */
  "public function get connected",function connected$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Indicates the byte order for the data. Possible values are <code>Endian.BIG_ENDIAN</code> or <code>Endian.LITTLE_ENDIAN</code>.
   * <p>The default value is <code>Endian.BIG_ENDIAN.</code></p>
   * @see flash.utils.Endian
   *
   */
  "public function get endian",function endian$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set endian",function endian$set(value/*:String*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Controls the version of Action Message Format (AMF) used when writing or reading an object.
   * @see #readObject()
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
   * Immediately closes the stream and cancels the download operation. No data can be read from the stream after the <code>close()</code> method is called.
   * @throws flash.errors.IOError The stream could not be closed, or the stream was not open.
   *
   */
  "public function close",function close()/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Begins downloading the URL specified in the <code>request</code> parameter.
   * <p><b>Note</b>: If a file being loaded contains non-ASCII characters (as found in many non-English languages), it is recommended that you save the file with UTF-8 or UTF-16 encoding, as opposed to a non-Unicode format like ASCII.</p>
   * <p>If the loading operation fails immediately, an IOError or SecurityError (including the local file security error) exception is thrown describing the failure. Otherwise, an <code>open</code> event is dispatched if the URL download starts downloading successfully, or an error event is dispatched if an error occurs.</p>
   * <p>By default, the calling SWF file and the URL you load must be in exactly the same domain. For example, a SWF file at www.adobe.com can load data only from sources that are also at www.adobe.com. To load data from a different domain, place a URL policy file on the server hosting the data.</p>
   * <p>In Flash Player, you cannot connect to commonly reserved ports. For a complete list of blocked ports, see "Restricting Networking APIs" in the <i>ActionScript 3.0 Developer's Guide</i>.</p>
   * <p>In Flash Player, you can prevent a SWF file from using this method by setting the <code>allowNetworking</code> parameter of the the <code>object</code> and <code>embed</code> tags in the HTML page that contains the SWF content.</p>
   * <p>In Flash Player 10 and later, and in AIR 1.5 and later, if you use a multipart Content-Type (for example "multipart/form-data") that contains an upload (indicated by a "filename" parameter in a "content-disposition" header within the POST body), the POST operation is subject to the security rules applied to uploads:</p>
   * <ul>
   * <li>The POST operation must be performed in response to a user-initiated action, such as a mouse click or key press.</li>
   * <li>If the POST operation is cross-domain (the POST target is not on the same server as the SWF file that is sending the POST request), the target server must provide a URL policy file that permits cross-domain access.</li></ul>
   * <p>Also, for any multipart Content-Type, the syntax must be valid (according to the RFC2046 standards). If the syntax appears to be invalid, the POST operation is subject to the security rules applied to uploads.</p>
   * <p>These rules also apply to AIR content in non-application sandboxes. However, in Adobe AIR, content in the application sandbox (content installed with the AIR application) are not restricted by these security limitations.</p>
   * <p>For more information related to security, see The Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</p>
   * <p>In AIR, a URLRequest object can register for the <code>httpResponse</code> status event. Unlike the <code>httpStatus</code> event, the <code>httpResponseStatus</code> event is delivered before any response data. Also, the <code>httpResponseStatus</code> event includes values for the <code>responseHeaders</code> and <code>responseURL</code> properties (which are undefined for an <code>httpStatus</code> event. Note that the <code>httpResponseStatus</code> event (if any) will be sent before (and in addition to) any <code>complete</code> or <code>error</code> event.</p>
   * <p>If there <i>is</i> an <code>httpResponseStatus</code> event listener, the body of the response message is <i>always</i> sent; and HTTP status code responses always results in a <code>complete</code> event. This is true in spite of whether the HTTP response status code indicates a success or an error.</p>
   * <p>In AIR, if there is <i>no</i> <code>httpResponseStatus</code> event listener, the behavior differs based on the SWF version:</p>
   * <ul>
   * <li>For SWF 9 content, the body of the HTTP response message is sent <i>only if</i> the HTTP response status code indicates success. Otherwise (if there is an error), no body is sent and the URLRequest object dispatches an IOError event.</li>
   * <li>For SWF 10 content, the body of the HTTP response message is <i>always</i> sent. If there is an error, the URLRequest object dispatches an IOError event.</li></ul>
   * @param request A URLRequest object specifying the URL to download. If the value of this parameter or the <code>URLRequest.url</code> property of the URLRequest object passed are <code>null</code>, the application throws a null pointer error.
   * Events
   * <table>
   * <tr>
   * <td><code><b>complete</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/Event.html"><code>Event</code></a></code> — Dispatched after data has loaded successfully. If there is a <code>httpResponseStatus</code> event listener, the URLRequest object also dispatches a <code>complete</code> event whether the HTTP response status code indicates a success <i>or</i> an error.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>httpStatus</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/HTTPStatusEvent.html"><code>HTTPStatusEvent</code></a></code> — If access is by HTTP and the current environment supports obtaining status codes, you may receive these events in addition to any <code>complete</code> or <code>error</code> event.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>httpResponseStatus</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/HTTPStatusEvent.html"><code>HTTPStatusEvent</code></a></code> — Dispatched if a call to the <code>load()</code> method attempts to access data over HTTP and Adobe AIR is able to detect and return the status code for the request.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>ioError</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/IOErrorEvent.html"><code>IOErrorEvent</code></a></code> — The load operation could not be completed.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>open</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/Event.html"><code>Event</code></a></code> — Dispatched when a load operation starts.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>securityError</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/SecurityErrorEvent.html"><code>SecurityErrorEvent</code></a></code> — A load operation attempted to retrieve data from a server outside the caller's security sandbox. This may be worked around using a policy file on the server.</td></tr></table>
   * @throws ArgumentError <code>URLRequest.requestHeader</code> objects may not contain certain prohibited HTTP request headers. For more information, see the URLRequestHeader class description.
   * @throws flash.errors.MemoryError This error can occur for the following reasons: <ol>
   * <li>Flash Player or Adobe AIR cannot convert the <code>URLRequest.data</code> parameter from UTF8 to MBCS. This error is applicable if the URLRequest object passed to <code>load()</code> is set to perform a <code>GET</code> operation and if <code>System.useCodePage</code> is set to <code>true</code>.</li>
   * <li>Flash Player or Adobe AIR cannot allocate memory for the <code>POST</code> data. This error is applicable if the URLRequest object passed to load is set to perform a <code>POST</code> operation.</li></ol>
   * @throws SecurityError Local untrusted SWF files may not communicate with the Internet. This may be worked around by reclassifying this SWF file as local-with-networking or trusted.
   * @throws SecurityError You are trying to connect to a commonly reserved port. For a complete list of blocked ports, see "Restricting Networking APIs" in the <i>ActionScript 3.0 Developer's Guide</i>.
   *
   */
  "public function load",function load(request/*:URLRequest*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads a Boolean value from the stream. A single byte is read, and <code>true</code> is returned if the byte is nonzero, <code>false</code> otherwise.
   * @return <code>True</code> is returned if the byte is nonzero, <code>false</code> otherwise.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read. If a local SWF file triggers a security warning, Flash Player prevents the URLStream data from being available to ActionScript. When this happens, the <code>bytesAvailable</code> property returns 0 even if data has been received, and any of the read methods throws an EOFError exception.
   * @throws flash.errors.IOError An I/O error occurred on the stream, or the stream is not open.
   *
   */
  "public function readBoolean",function readBoolean()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads a signed byte from the stream.
   * <p>The returned value is in the range -128...127.</p>
   * @return Value in the range -128...127.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read. If a local SWF file triggers a security warning, Flash Player prevents the URLStream data from being available to ActionScript. When this happens, the <code>bytesAvailable</code> property returns 0 even if data has been received, and any of the read methods throws an EOFError exception.
   * @throws flash.errors.IOError An I/O error occurred on the stream, or the stream is not open.
   *
   */
  "public function readByte",function readByte()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads <code>length</code> bytes of data from the stream. The bytes are read into the ByteArray object specified by <code>bytes</code>, starting <code>offset</code> bytes into the ByteArray object.
   * @param bytes The ByteArray object to read data into.
   * @param offset The offset into <code>bytes</code> at which data read should begin. Defaults to 0.
   * @param length The number of bytes to read. The default value of 0 will cause all available data to be read.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read. If a local SWF file triggers a security warning, Flash Player prevents the URLStream data from being available to ActionScript. When this happens, the <code>bytesAvailable</code> property returns 0 even if data has been received, and any of the read methods throws an EOFError exception.
   * @throws flash.errors.IOError An I/O error occurred on the stream, or the stream is not open.
   *
   */
  "public function readBytes",function readBytes(bytes/*:ByteArray*/, offset/*:uint = 0*/, length/*:uint = 0*/)/*:void*/ {switch(arguments.length){case 0:case 1:offset = 0;case 2:length = 0;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads an IEEE 754 double-precision floating-point number from the stream.
   * @return An IEEE 754 double-precision floating-point number from the stream.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read. If a local SWF file triggers a security warning, Flash Player prevents the URLStream data from being available to ActionScript. When this happens, the <code>bytesAvailable</code> property returns 0 even if data has been received, and any of the read methods throws an EOFError exception.
   * @throws flash.errors.IOError An I/O error occurred on the stream, or the stream is not open.
   *
   */
  "public function readDouble",function readDouble()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads an IEEE 754 single-precision floating-point number from the stream.
   * @return An IEEE 754 single-precision floating-point number from the stream.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read. If a local SWF file triggers a security warning, Flash Player prevents the URLStream data from being available to ActionScript. When this happens, the <code>bytesAvailable</code> property returns 0 even if data has been received, and any of the read methods throws an EOFError exception.
   * @throws flash.errors.IOError An I/O error occurred on the stream, or the stream is not open.
   *
   */
  "public function readFloat",function readFloat()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads a signed 32-bit integer from the stream.
   * <p>The returned value is in the range -2147483648...2147483647.</p>
   * @return Value in the range -2147483648...2147483647.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read. If a local SWF file triggers a security warning, Flash Player prevents the URLStream data from being available to ActionScript. When this happens, the <code>bytesAvailable</code> property returns 0 even if data has been received, and any of the read methods throws an EOFError exception.
   * @throws flash.errors.IOError An I/O error occurred on the stream, or the stream is not open.
   *
   */
  "public function readInt",function readInt()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads a multibyte string of specified length from the byte stream using the specified character set.
   * @param length The number of bytes from the byte stream to read.
   * @param charSet The string denoting the character set to use to interpret the bytes. Possible character set strings include <code>"shift_jis"</code>, <code>"CN-GB"</code>, <code>"iso-8859-1"</code>, and others. For a complete list, see <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/charset-codes.html">Supported Character Sets</a>.
   * <p><b>Note:</b> If the value for the <code>charSet</code> parameter is not recognized by the current system, the application uses the system's default code page as the character set. For example, a value for the <code>charSet</code> parameter, as in <code>myTest.readMultiByte(22, "iso-8859-01")</code> that uses <code>01</code> instead of <code>1</code> might work on your development machine, but not on another machine. On the other machine, the application will use the system's default code page.</p>
   *
   * @return UTF-8 encoded string.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read. If a local SWF file triggers a security warning, Flash Player prevents the URLStream data from being available to ActionScript. When this happens, the <code>bytesAvailable</code> property returns 0 even if data has been received, and any of the read methods throws an EOFError exception.
   *
   */
  "public function readMultiByte",function readMultiByte(length/*:uint*/, charSet/*:String*/)/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads an object from the socket, encoded in Action Message Format (AMF).
   * @return The deserialized object.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read. If a local SWF file triggers a security warning, Flash Player prevents the URLStream data from being available to ActionScript. When this happens, the <code>bytesAvailable</code> property returns 0 even if data has been received, and any of the read methods throws an EOFError exception.
   * @throws flash.errors.IOError An I/O error occurred on the stream, or the stream is not open.
   *
   * @see ObjectEncoding
   *
   */
  "public function readObject",function readObject()/*:**/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads a signed 16-bit integer from the stream.
   * <p>The returned value is in the range -32768...32767.</p>
   * @return Value in the range -32768...32767.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read. If a local SWF file triggers a security warning, Flash Player prevents the URLStream data from being available to ActionScript. When this happens, the <code>bytesAvailable</code> property returns 0 even if data has been received, and any of the read methods throws an EOFError exception.
   * @throws flash.errors.IOError An I/O error occurred on the stream, or the stream is not open.
   *
   */
  "public function readShort",function readShort()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads an unsigned byte from the stream.
   * <p>The returned value is in the range 0...255.</p>
   * @return Value in the range 0...255.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read. If a local SWF file triggers a security warning, Flash Player prevents the URLStream data from being available to ActionScript. When this happens, the <code>bytesAvailable</code> property returns 0 even if data has been received, and any of the read methods throws an EOFError exception.
   * @throws flash.errors.IOError An I/O error occurred on the stream, or the stream is not open.
   *
   */
  "public function readUnsignedByte",function readUnsignedByte()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads an unsigned 32-bit integer from the stream.
   * <p>The returned value is in the range 0...4294967295.</p>
   * @return Value in the range 0...4294967295.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read. If a local SWF file triggers a security warning, Flash Player prevents the URLStream data from being available to ActionScript. When this happens, the <code>bytesAvailable</code> property returns 0 even if data has been received, and any of the read methods throws an EOFError exception.
   * @throws flash.errors.IOError An I/O error occurred on the stream, or the stream is not open.
   *
   */
  "public function readUnsignedInt",function readUnsignedInt()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads an unsigned 16-bit integer from the stream.
   * <p>The returned value is in the range 0...65535.</p>
   * @return Value in the range 0...65535.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read. If a local SWF file triggers a security warning, Flash Player prevents the URLStream data from being available to ActionScript. When this happens, the <code>bytesAvailable</code> property returns 0 even if data has been received, and any of the read methods throws an EOFError exception.
   * @throws flash.errors.IOError An I/O error occurred on the stream, or the stream is not open.
   *
   */
  "public function readUnsignedShort",function readUnsignedShort()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads a UTF-8 string from the stream. The string is assumed to be prefixed with an unsigned short indicating the length in bytes.
   * @return A UTF-8 string.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read. If a local SWF file triggers a security warning, Flash Player prevents the URLStream data from being available to ActionScript. When this happens, the <code>bytesAvailable</code> property returns 0 even if data has been received, and any of the read methods throws an EOFError exception.
   * @throws flash.errors.IOError An I/O error occurred on the stream, or the stream is not open.
   *
   */
  "public function readUTF",function readUTF()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads a sequence of <code>length</code> UTF-8 bytes from the stream, and returns a string.
   * @param length A sequence of UTF-8 bytes.
   *
   * @return A UTF-8 string produced by the byte representation of characters of specified length.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read. If a local SWF file triggers a security warning, Flash Player prevents the URLStream data from being available to ActionScript. When this happens, the <code>bytesAvailable</code> property returns 0 even if data has been received, and any of the read methods throws an EOFError exception.
   * @throws flash.errors.IOError An I/O error occurred on the stream, or the stream is not open.
   *
   */
  "public function readUTFBytes",function readUTFBytes(length/*:uint*/)/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["flash.events.EventDispatcher","flash.utils.IDataInput","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);