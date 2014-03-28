joo.classLoader.prepare("package",/* {*/

/**
 * The DefinitionError class represents an error that occurs when user code attempts to define an identifier that is already defined. This error commonly occurs in redefining classes, interfaces, and functions.
 */
"public dynamic class DefinitionError extends Error",2,function($$private){;return[ 
  /**
   * Creates a new DefinitionError object.
   * @param message A string associated with the error.
   */
  "public function DefinitionError",function DefinitionError$(message/*:String = ""*/) {if(arguments.length<1){message = "";}
    this.super$2(message);
  },
];},[],["Error"], "0.8.0", "0.8.3"
);