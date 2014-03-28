joo.classLoader.prepare("package flash.utils",/* {*/


/**
 * The Endian class contains values that denote the byte order used to represent multibyte numbers. The byte order is either bigEndian (most significant byte first) or littleEndian (least significant byte first).
 * <p>Content in Flash Player or Adobe<sup>®</sup> AIR™ can interface with a server by using the binary protocol of that server, directly. Some servers use the bigEndian byte order and some servers use the littleEndian byte order. Most servers on the Internet use the bigEndian byte order because "network byte order" is bigEndian. The littleEndian byte order is popular because the Intel x86 architecture uses it. Use the endian byte order that matches the protocol of the server that is sending or receiving data.</p>
 * @see ByteArray#endian
 * @see flash.filesystem.FileStream#endian
 * @see IDataInput#endian
 * @see IDataOutput#endian
 * @see flash.net.Socket#endian
 * @see flash.net.URLStream#endian
 *
 */
"public final class Endian",1,function($$private){;return[ 
  /**
   * Indicates the most significant byte of the multibyte number appears first in the sequence of bytes.
   * <p>The hexadecimal number 0x12345678 has 4 bytes (2 hexadecimal digits per byte). The most significant byte is 0x12. The least significant byte is 0x78. (For the equivalent decimal number, 305419896, the most significant digit is 3, and the least significant digit is 6).</p>
   * <p>A stream using the bigEndian byte order (the most significant byte first) writes:</p>
   * <pre>     12 34 56 78
   </pre>
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cfb.html Binary client sockets
   *
   */
  "public static const",{ BIG_ENDIAN/*:String*/ : "bigEndian"},
  /**
   * Indicates the least significant byte of the multibyte number appears first in the sequence of bytes.
   * <p>The hexadecimal number 0x12345678 has 4 bytes (2 hexadecimal digits per byte). The most significant byte is 0x12. The least significant byte is 0x78. (For the equivalent decimal number, 305419896, the most significant digit is 3, and the least significant digit is 6).</p>
   * <p>A stream using the littleEndian byte order (the least significant byte first) writes:</p>
   * <pre>     78 56 34 12
   </pre>
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cfb.html Binary client sockets
   *
   */
  "public static const",{ LITTLE_ENDIAN/*:String*/ : "littleEndian"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);