joo.classLoader.prepare("package flash.utils",/* {*/

/**
 * The IDataOutput interface provides a set of methods for writing binary data. This interface is the I/O counterpart to the IDataInput interface, which reads binary data. The IDataOutput interface is implemented by the FileStream, Socket and ByteArray classes.
 * <p>All IDataInput and IDataOutput operations are "bigEndian" by default (the most significant byte in the sequence is stored at the lowest or first storage address), and are nonblocking.</p>
 * <p>Sign extension matters only when you read data, not when you write it. Therefore, you do not need separate write methods to work with <code>IDataInput.readUnsignedByte()</code> and <code>IDataInput.readUnsignedShort()</code>. In other words:</p>
 * <ul>
 * <li>Use <code>IDataOutput.writeByte()</code> with <code>IDataInput.readUnsignedByte()</code> and <code>IDataInput.readByte()</code>.</li>
 * <li>Use <code>IDataOutput.writeShort()</code> with <code>IDataInput.readUnsignedShort()</code> and <code>IDataInput.readShort()</code>.</li></ul>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/utils/IDataOutput.html#includeExamplesSummary">View the examples</a></p>
 * @see IDataInput
 * @see #endian
 * @see flash.filesystem.FileStream
 * @see flash.net.Socket
 * @see flash.net.URLStream
 * @see ByteArray
 *
 */
"public interface IDataOutput",1,function($$private){;return[ /*
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
   * Used to determine whether the AMF3 or AMF0 format is used when writing or reading binary data using the <code>writeObject()</code> method. The value is a constant from the ObjectEncoding class.
   * @see IDataInput#readObject()
   * @see #writeObject()
   * @see flash.net.ObjectEncoding
   *
   * /
  function objectEncoding():uint;*/,/*

  /**
   * @private
   * /
  function objectEncoding(value:uint):void;*/,/*

  /**
   * Writes a Boolean value. A single byte is written according to the <code>value</code> parameter, either 1 if <code>true</code> or 0 if <code>false</code>.
   * @param value A Boolean value determining which byte is written. If the parameter is <code>true</code>, 1 is written; if <code>false</code>, 0 is written.
   *
   * /
  function writeBoolean(value:Boolean):void;*/,/*

  /**
   * Writes a byte. The low 8 bits of the parameter are used; the high 24 bits are ignored.
   * @param value A byte value as an integer.
   *
   * /
  function writeByte(value:int):void;*/,/*

  /**
   * Writes a sequence of bytes from the specified byte array, <code>bytes</code>, starting at the byte specified by <code>offset</code> (using a zero-based index) with a length specified by <code>length</code>, into the file stream, byte stream, or byte array.
   * <p>If the <code>length</code> parameter is omitted, the default length of 0 is used and the entire buffer starting at <code>offset</code> is written. If the <code>offset</code> parameter is also omitted, the entire buffer is written.</p>
   * <p>If the <code>offset</code> or <code>length</code> parameter is out of range, they are clamped to the beginning and end of the <code>bytes</code> array.</p>
   * @param bytes The byte array to write.
   * @param offset A zero-based index specifying the position into the array to begin writing.
   * @param length An unsigned integer specifying how far into the buffer to write.
   *
   * /
  function writeBytes(bytes:ByteArray, offset:uint = 0, length:uint = 0):void;*/,/*

  /**
   * Writes an IEEE 754 double-precision (64-bit) floating point number.
   * @param value A double-precision (64-bit) floating point number.
   *
   * /
  function writeDouble(value:Number):void;*/,/*

  /**
   * Writes an IEEE 754 single-precision (32-bit) floating point number.
   * @param value A single-precision (32-bit) floating point number.
   *
   * /
  function writeFloat(value:Number):void;*/,/*

  /**
   * Writes a 32-bit signed integer.
   * @param value A byte value as a signed integer.
   *
   * /
  function writeInt(value:int):void;*/,/*

  /**
   * Writes a multibyte string to the file stream, byte stream, or byte array, using the specified character set.
   * @param value The string value to be written.
   * @param charSet The string denoting the character set to use. Possible character set strings include <code>"shift-jis"</code>, <code>"cn-gb"</code>, <code>"iso-8859-1"</code>, and others. For a complete list, see <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/charset-codes.html">Supported Character Sets</a>.
   *
   * /
  function writeMultiByte(value:String, charSet:String):void;*/,/*

  /**
   * Writes an object to the file stream, byte stream, or byte array, in AMF serialized format.
   * @param object The object to be serialized.
   *
   * @see #objectEncoding
   * @see flash.net.package#registerClassAlias()
   *
   * /
  function writeObject(object:*):void;*/,/*

  /**
   * Writes a 16-bit integer. The low 16 bits of the parameter are used; the high 16 bits are ignored.
   * @param value A byte value as an integer.
   *
   * /
  function writeShort(value:int):void;*/,/*

  /**
   * Writes a 32-bit unsigned integer.
   * @param value A byte value as an unsigned integer.
   *
   * /
  function writeUnsignedInt(value:uint):void;*/,/*

  /**
   * Writes a UTF-8 string to the file stream, byte stream, or byte array. The length of the UTF-8 string in bytes is written first, as a 16-bit integer, followed by the bytes representing the characters of the string.
   * @param value The string value to be written.
   *
   * @throws RangeError If the length is larger than 65535.
   *
   * /
  function writeUTF(value:String):void;*/,/*

  /**
   * Writes a UTF-8 string. Similar to <code>writeUTF()</code>, but does not prefix the string with a 16-bit length word.
   * @param value The string value to be written.
   *
   * /
  function writeUTFBytes(value:String):void;*/,
];},[],[], "0.8.0", "0.8.1"
);