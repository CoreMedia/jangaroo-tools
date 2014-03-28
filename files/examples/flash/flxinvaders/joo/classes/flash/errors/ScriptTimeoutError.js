joo.classLoader.prepare("package flash.errors",/* {*/


/**
 * The ScriptTimeoutError exception is thrown when the script timeout interval is reached. The script timeout interval is 15 seconds.
 * <p>Two ScriptTimeoutError exceptions are thrown. The first exception you can catch and exit cleanly. If there is no exception handler, the uncaught exception terminates execution. The second exception is thrown but cannot be caught by user code; it goes to the uncaught exception handler. It is uncatchable to prevent the player from hanging indefinitely.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/errors/ScriptTimeoutError.html#includeExamplesSummary">View the examples</a></p>
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ece.html Comparing the Error classes
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7eb1.html flash.error package Error classes
 *
 */
"public dynamic class ScriptTimeoutError extends Error",2,function($$private){;return[ 
  /**
   * Creates a new ScriptTimeoutError object.
   * @param message A string associated with the error object.
   *
   */
  "public function ScriptTimeoutError",function ScriptTimeoutError$(message/*:String = ""*/) {if(arguments.length<1){message = "";}
    this.super$2(message);
  },
];},[],["Error"], "0.8.0", "0.8.1"
);