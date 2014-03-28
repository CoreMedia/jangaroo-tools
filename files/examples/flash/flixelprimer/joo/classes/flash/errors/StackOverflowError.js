joo.classLoader.prepare("package flash.errors",/* {*/


/**
 * ActionScript throws a StackOverflowError exception when the stack available to the script is exhausted. ActionScript uses a stack to store information about each method call made in a script, such as the local variables that the method uses. The amount of stack space available varies from system to system.
 * <p>A StackOverflowError exception might indicate that infinite recursion has occurred, in which case a termination case needs to be added to the function. It also might indicate that the recursive algorithm has a proper terminating condition but has exhausted the stack anyway. In this case, try to express the algorithm iteratively instead.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/errors/StackOverflowError.html#includeExamplesSummary">View the examples</a></p>
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ecf.html Responding to error events and status
 *
 */
"public dynamic class StackOverflowError extends Error",2,function($$private){;return[ 
  /**
   * Creates a new StackOverflowError object.
   * @param message A string associated with the error object.
   *
   */
  "public function StackOverflowError",function StackOverflowError$(message/*:String = ""*/) {if(arguments.length<1){message = "";}
    this.super$2(message);
  },
];},[],["Error"], "0.8.0", "0.8.3"
);