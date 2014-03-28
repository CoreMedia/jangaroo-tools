joo.classLoader.prepare("package flash.errors",/* {*/


/**
 * The IllegalOperationError exception is thrown when a method is not implemented or the implementation doesn't cover the current usage.
 * <p>Examples of illegal operation error exceptions include:</p>
 * <ul>
 * <li>A base class, such as DisplayObjectContainer, provides more functionality than a Stage can support (such as masks)</li>
 * <li>Certain accessibility methods are called when the player is compiled without accessibility support</li>
 * <li>The mms.cfg setting prohibits a FileReference action</li>
 * <li>ActionScript tries to run a <code>FileReference.browse()</code> call when a browse dialog box is already open</li>
 * <li>ActionScript tries to use an unsupported protocol for a FileReference object (such as FTP)</li>
 * <li>Authoring-only features are invoked from a run-time player</li>
 * <li>An attempt is made to set the name of a Timeline-placed object</li></ul>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/errors/IllegalOperationError.html#includeExamplesSummary">View the examples</a></p>
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ece.html Comparing the Error classes
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7eb1.html flash.error package Error classes
 *
 */
"public dynamic class IllegalOperationError extends Error",2,function($$private){;return[ 
  /**
   * Creates a new IllegalOperationError object.
   * @param message A string associated with the error object.
   *
   */
  "public function IllegalOperationError",function IllegalOperationError$(message/*:String = ""*/) {if(arguments.length<1){message = "";}
    this.super$2(message);
  },
];},[],["Error"], "0.8.0", "0.8.3"
);