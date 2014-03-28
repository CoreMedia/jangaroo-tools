joo.classLoader.prepare("package flash.errors",/* {*/


/**
 * The MemoryError exception is thrown when a memory allocation request fails.
 * <p>On a desktop machine, memory allocation failures are rare unless an allocation request is extremely large. For example, a 32-bit Windows program can access only 2GB of address space, so a request for 10 billion bytes is impossible.</p>
 * <p>By default, Flash Player does not impose a limit on how much memory an ActionScript program can allocate.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/errors/MemoryError.html#includeExamplesSummary">View the examples</a></p>
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ece.html Comparing the Error classes
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7eb1.html flash.error package Error classes
 *
 */
"public dynamic class MemoryError extends Error",2,function($$private){;return[ 
  /**
   * Creates a new MemoryError object.
   * @param message A string associated with the error object.
   *
   */
  "public function MemoryError",function MemoryError$(message/*:String = ""*/) {switch(arguments.length){case 0:message = "";}
    this.super$2(message);
  },
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);