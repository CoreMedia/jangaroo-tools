joo.classLoader.prepare("package flash.net",/* {
import flash.events.EventDispatcher
import flash.utils.ByteArray
import flash.utils.IDataInput
import flash.utils.IDataOutput*/

/**
 * Dispatched when the server closes the socket connection.
 * <p>The <code>close</code> event is dispatched only when the server closes the connection; it is not dispatched when you call the <code>Socket.close()</code> method.</p>
 * @eventType flash.events.Event.CLOSE
 */
{Event:{name:"close", type:"flash.events.Event"}},
/**
 * Dispatched when a network connection has been established.
 * @eventType flash.events.Event.CONNECT
 */
{Event:{name:"connect", type:"flash.events.Event"}},
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
 * property ProgressEvent.type =
 * @eventType flash.events.ProgressEvent.SOCKET_DATA
 */
{Event:{name:"socketData", type:"flash.events.ProgressEvent"}},

/**
 * The Socket class enables code to establish Transport Control Protocol (TCP) socket connections for sending and receiving binary data.
 * <p>The Socket class is useful for working with servers that use binary protocols.</p>
 * <p>To use the methods of the Socket class, first use the constructor, <code>new Socket</code>, to create a Socket object.</p>
 * <p>A socket transmits and receives data asynchronously.</p>
 * <p>On some operating systems, flush() is called automatically between execution frames, but on other operating systems, such as Windows, the data is never sent unless you call <code>flush()</code> explicitly. To ensure your application behaves reliably across all operating systems, it is a good practice to call the <code>flush()</code> method after writing each message (or related group of data) to the socket.</p>
 * <p>In Adobe AIR, Socket objects are also created when a listening ServerSocket receives a connection from an external process. The Socket representing the connection is dispatched in a ServerSocketConnectEvent. Your application is responsible for maintaining a reference to this Socket object. If you don't, the Socket object is eligible for garbage collection and may be destroyed by the runtime without warning.</p>
 * <p>SWF content running in the local-with-filesystem security sandbox cannot use sockets.</p>
 * <p><i>Socket policy files</i> on the target host specify the hosts from which SWF files can make socket connections, and the ports to which those connections can be made. The security requirements with regard to socket policy files have become more stringent in the last several releases of Flash Player. In all versions of Flash Player, Adobe recommends the use of a socket policy file; in some circumstances, a socket policy file is required. Therefore, if you are using Socket objects, make sure that the target host provides a socket policy file if necessary.</p>
 * <p>The following list summarizes the requirements for socket policy files in different versions of Flash Player:</p>
 * <ul>
 * <li>In Flash Player 9.0.124.0 and later, a socket policy file is required for any socket connection. That is, a socket policy file on the target host is required no matter what port you are connecting to, and is required even if you are connecting to a port on the same host that is serving the SWF file.</li>
 * <li>In Flash Player versions 9.0.115.0 and earlier, if you want to connect to a port number below 1024, or if you want to connect to a host other than the one serving the SWF file, a socket policy file on the target host is required.</li>
 * <li>In Flash Player 9.0.115.0, even if a socket policy file isn't required, a warning is displayed when using the Flash Debug Player if the target host doesn't serve a socket policy file.</li>
 * <li>In AIR, a socket policy file is not required for content running in the application security sandbox. Socket policy files are required for any socket connection established by content running outside the AIR application security sandbox.</li></ul>
 * <p>For more information related to security, see the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a></p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/net/Socket.html#includeExamplesSummary">View the examples</a></p>
 * @see ServerSocket
 * @see DatagramSocket
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cfb.html Binary client sockets
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7c63.html Connecting to sockets
 *
 */
"public class Socket extends flash.events.EventDispatcher implements flash.utils.IDataInput, flash.utils.IDataOutput",2,function($$private){;return[ 
  /**
   * The number of bytes of data available for reading in the input buffer.
   * <p>Your code must access <code>bytesAvailable</code> to ensure that sufficient data is available before trying to read it with one of the <code>read</code> methods.</p>
   */
  "public function get bytesAvailable",function bytesAvailable$get()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Indicates whether this Socket object is currently connected. A call to this property returns a value of <code>true</code> if the socket is currently connected, or <code>false</code> otherwise.
   */
  "public function get connected",function connected$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Indicates the byte order for the data. Possible values are constants from the flash.utils.Endian class, <code>Endian.BIG_ENDIAN</code> or <code>Endian.LITTLE_ENDIAN</code>.
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
   * Controls the version of AMF used when writing or reading an object.
   * @see ObjectEncoding
   * @see #readObject()
   * @see #writeObject()
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
   * Creates a new Socket object. If no parameters are specified, an initially disconnected socket is created. If parameters are specified, a connection is attempted to the specified host and port.
   * <p><b>Note:</b> It is strongly advised to use the constructor form <b>without parameters</b>, then add any event listeners, then call the <code>connect</code> method with <code>host</code> and <code>port</code> parameters. This sequence guarantees that all event listeners will work properly.</p>
   * @param host A fully qualified DNS domain name or an IP address. IPv4 addresses are specified in dot-decimal notation, such as <i>192.0.2.0</i>. In Flash Player 9.0.115.0 and AIR 1.0 and later, you can specify IPv6 addresses using hexadecimal-colon notation, such as <i>2001:db8:ccc3:ffff:0:444d:555e:666f</i>. You can also specify <code>null</code> to connect to the host server on which the SWF file resides. If the SWF file issuing this call is running in a web browser, <code>host</code> must be in the domain from which the SWF file originated.
   * @param port The TCP port number on the target host used to establish a connection. In Flash Player 9.0.124.0 and later, the target host must serve a socket policy file specifying that socket connections are permitted from the host serving the SWF file to the specified port. In earlier versions of Flash Player, a socket policy file is required only if you want to connect to a port number below 1024, or if you want to connect to a host other than the one serving the SWF file.
   * Events
   * <table>
   * <tr>
   * <td><code><b>connect</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/Event.html"><code>Event</code></a></code> — Dispatched when a network connection has been established.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>ioError</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/IOErrorEvent.html"><code>IOErrorEvent</code></a></code> — Dispatched when an input/output error occurs that causes the connection to fail.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>securityError</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/SecurityErrorEvent.html"><code>SecurityErrorEvent</code></a></code> — Dispatched if a call to <code>Socket.connect()</code> attempts to connect either to a server that doesn't serve a socket policy file, or to a server whose policy file doesn't grant the calling host access to the specified port. For more information on policy files, see "Website controls (policy files)" in the <i>ActionScript 3.0 Developer's Guide</i> and the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>. </td></tr></table>
   * @throws SecurityError This error occurs in SWF content for the following reasons:
   * <ul>
   * <li>Local-with-filesystem files cannot communicate with the Internet. You can work around this problem by reclassifying this SWF file as local-with-networking or trusted. This limitation is not set for AIR application content in the application security sandbox.</li>
   * <li>You cannot specify a socket port higher than 65535.</li></ul>
   *
   */
  "public function Socket",function Socket$(host/*:String = null*/, port/*:int = 0*/) {switch(arguments.length){case 0:host = null;case 1:port = 0;}this.super$2();
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Closes the socket. You cannot read or write any data after the <code>close()</code> method has been called.
   * <p>The <code>close</code> event is dispatched only when the server closes the connection; it is not dispatched when you call the <code>close()</code> method.</p>
   * <p>You can reuse the Socket object by calling the <code>connect()</code> method on it again.</p>
   * @throws flash.errors.IOError The socket could not be closed, or the socket was not open.
   *
   */
  "public function close",function close()/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Connects the socket to the specified host and port.
   * <p>If the connection fails immediately, either an event is dispatched or an exception is thrown: an error event is dispatched if a host was specified, and an exception is thrown if no host was specified. Otherwise, the status of the connection is reported by an event. If the socket is already connected, the existing connection is closed first.</p>
   * @param host The name or IP address of the host to connect to. If no host is specified, the host that is contacted is the host where the calling file resides. If you do not specify a host, use an event listener to determine whether the connection was successful.
   * @param port The port number to connect to.
   * Events
   * <table>
   * <tr>
   * <td><code><b>connect</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/Event.html"><code>Event</code></a></code> — Dispatched when a network connection has been established.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>ioError</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/IOErrorEvent.html"><code>IOErrorEvent</code></a></code> — Dispatched if a host is specified and an input/output error occurs that causes the connection to fail.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>securityError</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/SecurityErrorEvent.html"><code>SecurityErrorEvent</code></a></code> — Dispatched if a call to <code>Socket.connect()</code> attempts to connect either to a server that doesn't serve a socket policy file, or to a server whose policy file doesn't grant the calling host access to the specified port. For more information on policy files, see "Website controls (policy files)" in the <i>ActionScript 3.0 Developer's Guide</i> and the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</td></tr></table>
   * @throws flash.errors.IOError No host was specified and the connection failed.
   * @throws SecurityError This error occurs in SWF content for the following reasons:
   * <ul>
   * <li>Local untrusted SWF files may not communicate with the Internet. You can work around this limitation by reclassifying the file as local-with-networking or as trusted.</li>
   * <li>You cannot specify a socket port higher than 65535.</li>
   * <li>In the HTML page that contains the SWF content, the <code>allowNetworking</code> parameter of the <code>object</code> and <code>embed</code> tags is set to <code>"none"</code>.</li></ul>
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7c63.html Connecting to sockets
   *
   */
  "public function connect",function connect(host/*:String*/, port/*:int*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Flushes any accumulated data in the socket's output buffer.
   * <p>On some operating systems, flush() is called automatically between execution frames, but on other operating systems, such as Windows, the data is never sent unless you call <code>flush()</code> explicitly. To ensure your application behaves reliably across all operating systems, it is a good practice to call the <code>flush()</code> method after writing each message (or related group of data) to the socket.</p>
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   */
  "public function flush",function flush()/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads a Boolean value from the socket. After reading a single byte, the method returns <code>true</code> if the byte is nonzero, and <code>false</code> otherwise.
   * @return A value of <code>true</code> if the byte read is nonzero, otherwise <code>false</code>.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read.
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   */
  "public function readBoolean",function readBoolean()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads a signed byte from the socket.
   * @return A value from -128 to 127.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read.
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   */
  "public function readByte",function readByte()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads the number of data bytes specified by the length parameter from the socket. The bytes are read into the specified byte array, starting at the position indicated by <code>offset</code>.
   * @param bytes The ByteArray object to read data into.
   * @param offset The offset at which data reading should begin in the byte array.
   * @param length The number of bytes to read. The default value of 0 causes all available data to be read.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read.
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   */
  "public function readBytes",function readBytes(bytes/*:ByteArray*/, offset/*:uint = 0*/, length/*:uint = 0*/)/*:void*/ {switch(arguments.length){case 0:case 1:offset = 0;case 2:length = 0;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads an IEEE 754 double-precision floating-point number from the socket.
   * @return An IEEE 754 double-precision floating-point number.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read.
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   */
  "public function readDouble",function readDouble()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads an IEEE 754 single-precision floating-point number from the socket.
   * @return An IEEE 754 single-precision floating-point number.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read.
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   */
  "public function readFloat",function readFloat()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads a signed 32-bit integer from the socket.
   * @return A value from -2147483648 to 2147483647.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read.
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   */
  "public function readInt",function readInt()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads a multibyte string from the byte stream, using the specified character set.
   * @param length The number of bytes from the byte stream to read.
   * @param charSet The string denoting the character set to use to interpret the bytes. Possible character set strings include <code>"shift_jis"</code>, <code>"CN-GB"</code>, and <code>"iso-8859-1"</code>. For a complete list, see <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/charset-codes.html">Supported Character Sets</a>.
   * <p><b>Note:</b> If the value for the <code>charSet</code> parameter is not recognized by the current system, then the application uses the system's default code page as the character set. For example, a value for the <code>charSet</code> parameter, as in <code>myTest.readMultiByte(22, "iso-8859-01")</code> that uses <code>01</code> instead of <code>1</code> might work on your development machine, but not on another machine. On the other machine, the application will use the system's default code page.</p>
   *
   * @return A UTF-8 encoded string.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read.
   *
   */
  "public function readMultiByte",function readMultiByte(length/*:uint*/, charSet/*:String*/)/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads an object from the socket, encoded in AMF serialized format.
   * @return The deserialized object
   *
   * @throws flash.errors.EOFError There is insufficient data available to read.
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   * @see ObjectEncoding
   * @see flash.net.package#registerClassAlias()
   *
   */
  "public function readObject",function readObject()/*:**/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads a signed 16-bit integer from the socket.
   * @return A value from -32768 to 32767.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read.
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   */
  "public function readShort",function readShort()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads an unsigned byte from the socket.
   * @return A value from 0 to 255.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read.
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   */
  "public function readUnsignedByte",function readUnsignedByte()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads an unsigned 32-bit integer from the socket.
   * @return A value from 0 to 4294967295.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read.
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   */
  "public function readUnsignedInt",function readUnsignedInt()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads an unsigned 16-bit integer from the socket.
   * @return A value from 0 to 65535.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read.
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   */
  "public function readUnsignedShort",function readUnsignedShort()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads a UTF-8 string from the socket. The string is assumed to be prefixed with an unsigned short integer that indicates the length in bytes.
   * @return A UTF-8 string.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read.
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   */
  "public function readUTF",function readUTF()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Reads the number of UTF-8 data bytes specified by the <code>length</code> parameter from the socket, and returns a string.
   * @param length The number of bytes to read.
   *
   * @return A UTF-8 string.
   *
   * @throws flash.errors.EOFError There is insufficient data available to read.
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   */
  "public function readUTFBytes",function readUTFBytes(length/*:uint*/)/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Writes a Boolean value to the socket. This method writes a single byte, with either a value of 1 (<code>true</code>) or 0 (<code>false</code>).
   * @param value The value to write to the socket: 1 (<code>true</code>) or 0 (<code>false</code>).
   *
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   * @see #flush()
   *
   */
  "public function writeBoolean",function writeBoolean(value/*:Boolean*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Writes a byte to the socket.
   * @param value The value to write to the socket. The low 8 bits of the value are used; the high 24 bits are ignored.
   *
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   * @see #flush()
   *
   */
  "public function writeByte",function writeByte(value/*:int*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Writes a sequence of bytes from the specified byte array. The write operation starts at the position specified by <code>offset</code>.
   * <p>If you omit the <code>length</code> parameter the default length of 0 causes the method to write the entire buffer starting at <code>offset</code>.</p>
   * <p>If you also omit the <code>offset</code> parameter, the entire buffer is written.</p>
   * @param bytes The ByteArray object to write data from.
   * @param offset The zero-based offset into the <code>bytes</code> ByteArray object at which data writing should begin.
   * @param length The number of bytes to write. The default value of 0 causes the entire buffer to be written, starting at the value specified by the <code>offset</code> parameter.
   *
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   * @throws RangeError If <code>offset</code> is greater than the length of the ByteArray specified in <code>bytes</code> or if the amount of data specified to be written by <code>offset</code> plus <code>length</code> exceeds the data available.
   *
   * @see #flush()
   *
   */
  "public function writeBytes",function writeBytes(bytes/*:ByteArray*/, offset/*:uint = 0*/, length/*:uint = 0*/)/*:void*/ {switch(arguments.length){case 0:case 1:offset = 0;case 2:length = 0;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Writes an IEEE 754 double-precision floating-point number to the socket.
   * @param value The value to write to the socket.
   *
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   * @see #flush()
   *
   */
  "public function writeDouble",function writeDouble(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Writes an IEEE 754 single-precision floating-point number to the socket.
   * @param value The value to write to the socket.
   *
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   * @see #flush()
   *
   */
  "public function writeFloat",function writeFloat(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Writes a 32-bit signed integer to the socket.
   * @param value The value to write to the socket.
   *
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   * @see #flush()
   *
   */
  "public function writeInt",function writeInt(value/*:int*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Writes a multibyte string from the byte stream, using the specified character set.
   * @param value The string value to be written.
   * @param charSet The string denoting the character set to use to interpret the bytes. Possible character set strings include <code>"shift_jis"</code>, <code>"CN-GB"</code>, and <code>"iso-8859-1"</code>. For a complete list, see <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/charset-codes.html">Supported Character Sets</a>.
   *
   * @see #flush()
   *
   */
  "public function writeMultiByte",function writeMultiByte(value/*:String*/, charSet/*:String*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Write an object to the socket in AMF serialized format.
   * @param object The object to be serialized.
   *
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   * @see #flush()
   * @see ObjectEncoding
   * @see flash.net.package#registerClassAlias()
   *
   */
  "public function writeObject",function writeObject(object/*:**/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Writes a 16-bit integer to the socket. The bytes written are as follows:
   * <pre><code>(v >> 8) & 0xff v & 0xff</code></pre>
   * <p>The low 16 bits of the parameter are used; the high 16 bits are ignored.</p>
   * @param value The value to write to the socket.
   *
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   * @see #flush()
   *
   */
  "public function writeShort",function writeShort(value/*:int*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Writes a 32-bit unsigned integer to the socket.
   * @param value The value to write to the socket.
   *
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   * @see #flush()
   *
   */
  "public function writeUnsignedInt",function writeUnsignedInt(value/*:uint*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Writes the following data to the socket: a 16-bit unsigned integer, which indicates the length of the specified UTF-8 string in bytes, followed by the string itself.
   * <p>Before writing the string, the method calculates the number of bytes that are needed to represent all characters of the string.</p>
   * @param value The string to write to the socket.
   *
   * @throws RangeError The length is larger than 65535.
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   * @see #flush()
   *
   */
  "public function writeUTF",function writeUTF(value/*:String*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Writes a UTF-8 string to the socket.
   * @param value The string to write to the socket.
   *
   * @throws flash.errors.IOError An I/O error occurred on the socket, or the socket is not open.
   *
   * @see #flush()
   *
   */
  "public function writeUTFBytes",function writeUTFBytes(value/*:String*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["flash.events.EventDispatcher","flash.utils.IDataInput","flash.utils.IDataOutput","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);