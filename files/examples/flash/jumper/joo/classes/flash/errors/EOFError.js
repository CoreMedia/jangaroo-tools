joo.classLoader.prepare("package flash.errors",/* {*/


/**
 * An EOFError exception is thrown when you attempt to read past the end of the available data. For example, an EOFError is thrown when one of the read methods in the IDataInput interface is called and there is insufficient data to satisfy the read request.
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/errors/EOFError.html#includeExamplesSummary">View the examples</a></p>
 * @see flash.utils.ByteArray
 * @see flash.utils.IDataInput
 * @see flash.net.Socket
 * @see flash.net.URLStream
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ecc.html Working with the debugger versions of Flash runtimes
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ece.html Comparing the Error classes
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7eb1.html flash.error package Error classes
 *
 */
"public dynamic class EOFError extends flash.errors.IOError",3,function($$private){;return[ 
  /**
   * Creates a new EOFError object.
   * @param message A string associated with the error object.
   *
   */
  "public function EOFError",function EOFError$(message/*:String = ""*/) {switch(arguments.length){case 0:message = "";}
    this.super$3(message);
  },
];},[],["flash.errors.IOError"], "0.8.0", "0.8.2-SNAPSHOT"
);