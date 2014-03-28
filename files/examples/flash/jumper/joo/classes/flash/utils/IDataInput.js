joo.classLoader.prepare("package flash.utils",/* {*/

/**
 * The IDataInput interface provides a set of methods for reading binary data. This interface is the I/O counterpart to the IDataOutput interface, which writes binary data.
 * <p>All IDataInput and IDataOutput operations are "bigEndian" by default (the most significant byte in the sequence is stored at the lowest or first storage address), and are nonblocking. If insufficient data is available, an <code>EOFError</code> exception is thrown. Use the <code>IDataInput.bytesAvailable</code> property to determine how much data is available to read.</p>
 * <p>Sign extension matters only when you read data, not when you write it. Therefore you do not need separate write methods to work with <code>IDataInput.readUnsignedByte()</code> and <code>IDataInput.readUnsignedShort()</code>. In other words:</p>
 * <ul>
 * <li>Use <code>IDataOutput.writeByte()</code> with <code>IDataInput.readUnsignedByte()</code> and <code>IDataInput.readByte()</code>.</li>
 * <li>Use <code>IDataOutput.writeShort()</code> with <code>IDataInput.readUnsignedShort()</code> and <code>IDataInput.readShort()</code>.</li></ul>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/utils/IDataInput.html#includeExamplesSummary">View the examples</a></p>
 * @see IDataOutput
 * @see #endian
 * @see flash.filesystem.FileStream
 * @see flash.net.Socket
 * @see flash.net.URLStream
 * @see ByteArray
 * @see flash.errors.EOFError
 *
 */
"public interface IDataInput",1,function($$private){;return[ /*
  /**
   * Returns the number of bytes of data available for reading in the input buffer. User code must call <code>bytesAvailable</code> to ensure that sufficient data is available before trying to read it with one of the read methods.
   * /
  function bytesAvailable():uint;*/,/*

  /**
   * The byte order for the data, either the <code>BIG_ENDIAN</code> or <code>LITTLE_ENDIAN</code> constant from the Endian class.
   * @see Endian
   *
   * /
  function endian():String;*/,/*

  /**
   * @private
   * /
  function endian(value:String):void;*/,/*

  /**
   * Used to determine whether the AMF3 or AMF0 format is used when writing or reading binary data using the <code>readObject()</code> method. The value is a constant from the ObjectEncoding class.
   * @see #readObject()
   * @see IDataOutput#writeObject()
   * @see flash.net.ObjectEncoding
   *
   * /
  function objectEncoding():uint;*/,/*

  /**
   * @private
   * /
  function objectEncoding(value:uint):void;*/,/*

  /**
   * Reads a Boolean value from the file stream, byte stream, or byte array. A single byte is read and <code>true</code> is returned if the byte is nonzero, <code>false</code> otherwise.
   * @return A Boolean value, <code>true</code> if the byte is nonzero, <code>false</code> otherwise.
   *
   * @throws flash.errors.EOFError There is not sufficient data available to read.
   *
   * /
  function readBoolean():Boolean;*/,/*

  /**
   * Reads a signed byte from the file stream, byte stream, or byte array.
   * @return The returned value is in the range -128 to 127.
   *
   * @throws flash.errors.EOFError There is not sufficient data available to read.
   *
   * /
  function readByte():int;*/,/*

  /**
   * Reads the number of data bytes, specified by the <code>length</code> parameter, from the file stream, byte stream, or byte array. The bytes are read into the ByteArray objected specified by the <code>bytes</code> parameter, starting at the position specified by <code>offset</code>.
   * @param bytes The <code>ByteArray</code> object to read data into.
   * @param offset The offset into the <code>bytes</code> parameter at which data read should begin.
   * @param length The number of bytes to read. The default value of 0 causes all available data to be read.
   *
   * @throws flash.errors.EOFError There is not sufficient data available to read.
   *
   * /
  function readBytes(bytes:ByteArray, offset:uint = 0, length:uint = 0):void;*/,/*

  /**
   * Reads an IEEE 754 double-precision floating point number from the file stream, byte stream, or byte array.
   * @return An IEEE 754 double-precision floating point number.
   *
   * @throws flash.errors.EOFError There is not sufficient data available to read.
   *
   * /
  function readDouble():Number;*/,/*

  /**
   * Reads an IEEE 754 single-precision floating point number from the file stream, byte stream, or byte array.
   * @return An IEEE 754 single-precision floating point number.
   *
   * @throws flash.errors.EOFError There is not sufficient data available to read.
   *
   * /
  function readFloat():Number;*/,/*

  /**
   * Reads a signed 32-bit integer from the file stream, byte stream, or byte array.
   * @return The returned value is in the range -2147483648 to 2147483647.
   *
   * @throws flash.errors.EOFError There is not sufficient data available to read.
   *
   * /
  function readInt():int;*/,/*

  /**
   * Reads a multibyte string of specified length from the file stream, byte stream, or byte array using the specified character set.
   * @param length The number of bytes from the byte stream to read.
   * @param charSet The string denoting the character set to use to interpret the bytes. Possible character set strings include <code>"shift-jis"</code>, <code>"cn-gb"</code>, <code>"iso-8859-1"</code>, and others. For a complete list, see <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/charset-codes.html">Supported Character Sets</a>.
   * <p><b>Note:</b> If the value for the <code>charSet</code> parameter is not recognized by the current system, then Adobe<sup>®</sup> Flash<sup>®</sup> Player or Adobe<sup>®</sup> AIR<sup>®</sup> uses the system's default code page as the character set. For example, a value for the <code>charSet</code> parameter, as in <code>myTest.readMultiByte(22, "iso-8859-01")</code>, that uses <code>01</code> instead of <code>1</code> might work on your development system, but not on another system. On the other system, Flash Player or the AIR runtime will use the system's default code page.</p>
   *
   * @return UTF-8 encoded string.
   *
   * @throws flash.errors.EOFError There is not sufficient data available to read.
   *
   * /
  function readMultiByte(length:uint, charSet:String):String;*/,/*

  /**
   * Reads an object from the file stream, byte stream, or byte array, encoded in AMF serialized format.
   * @return The deserialized object
   *
   * @throws flash.errors.EOFError There is not sufficient data available to read.
   *
   * @see #objectEncoding
   * @see flash.net.package#registerClassAlias()
   *
   * /
  function readObject():*;*/,/*

  /**
   * Reads a signed 16-bit integer from the file stream, byte stream, or byte array.
   * @return The returned value is in the range -32768 to 32767.
   *
   * @throws flash.errors.EOFError There is not sufficient data available to read.
   *
   * /
  function readShort():int;*/,/*

  /**
   * Reads an unsigned byte from the file stream, byte stream, or byte array.
   * @return The returned value is in the range 0 to 255.
   *
   * @throws flash.errors.EOFError There is not sufficient data available to read.
   *
   * /
  function readUnsignedByte():uint;*/,/*

  /**
   * Reads an unsigned 32-bit integer from the file stream, byte stream, or byte array.
   * @return The returned value is in the range 0 to 4294967295.
   *
   * @throws flash.errors.EOFError There is not sufficient data available to read.
   *
   * /
  function readUnsignedInt():uint;*/,/*

  /**
   * Reads an unsigned 16-bit integer from the file stream, byte stream, or byte array.
   * @return The returned value is in the range 0 to 65535.
   *
   * @throws flash.errors.EOFError There is not sufficient data available to read.
   *
   * /
  function readUnsignedShort():uint;*/,/*

  /**
   * Reads a UTF-8 string from the file stream, byte stream, or byte array. The string is assumed to be prefixed with an unsigned short indicating the length in bytes.
   * <p>This method is similar to the <code>readUTF()</code> method in the Java<sup>®</sup> IDataInput interface.</p>
   * @return A UTF-8 string produced by the byte representation of characters.
   *
   * @throws flash.errors.EOFError There is not sufficient data available to read.
   *
   * /
  function readUTF():String;*/,/*

  /**
   * Reads a sequence of UTF-8 bytes from the byte stream or byte array and returns a string.
   * @param length The number of bytes to read.
   *
   * @return A UTF-8 string produced by the byte representation of characters of the specified length.
   *
   * @throws flash.errors.EOFError There is not sufficient data available to read.
   *
   * /
  function readUTFBytes(length:uint):String;*/,
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);