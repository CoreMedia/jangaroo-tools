joo.classLoader.prepare("package flash.errors",/* {*/


/**
 * The IOError exception is thrown when some type of input or output failure occurs. For example, an IOError exception is thrown if a read/write operation is attempted on a socket that has not connected or that has become disconnected.
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/errors/IOError.html#includeExamplesSummary">View the examples</a></p>
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ecc.html Working with the debugger versions of Flash runtimes
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ece.html Comparing the Error classes
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7eb1.html flash.error package Error classes
 *
 */
"public dynamic class IOError extends Error",2,function($$private){;return[ 
  /**
   * Creates a new IOError object.
   * @param message A string associated with the error object.
   *
   */
  "public function IOError",function IOError$(message/*:String = ""*/) {if(arguments.length<1){message = "";}
    this.super$2(message);
  },
];},[],["Error"], "0.8.0", "0.8.1"
);