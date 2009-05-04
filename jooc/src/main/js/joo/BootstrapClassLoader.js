Function.prototype.bind = function(object) {
  var fn = this;
  return (function $boundMethod() {
    return arguments.length ? fn.apply(object,arguments) : fn.call(object); // call is faster at least in Firefox.
  });
};

(function(theGlobalObject){
  // defined here to avoid global name space pollution and unneccessary closures:
  function clone(object) {
    var empty = function(){ };
    empty.prototype =  object;
    return new empty();
  };
  function createGetQualified(create) {
    return (function(name) {
      var object = theGlobalObject;
      if (name) {
        var parts = name.split(".");
        for (var i=0; i<parts.length; ++i) {
          var subobject = object[parts[i]];
          if (!subobject) {
            if (create) {
              subobject = object[parts[i]] = {};
            } else {
              return null;
            }
          }
          object = subobject;
        }
      }
      return object;
    });
  }

  theGlobalObject.joo = {
    getOrCreatePackage: createGetQualified(true),
    getQualifiedObject: createGetQualified(false),
    classLoader: {
      prepare: function(packageDef, directives, classDef, memberFactory) {
        var classMatch = classDef.match(/^\s*((public|internal|final|dynamic)\s+)*class\s+([A-Za-z][a-zA-Z$_0-9]*)(\s+extends\s+([a-zA-Z$_0-9.]+))?(\s+implements\s+([a-zA-Z$_0-9.,\s]+))?\s*$/);
        var className = classMatch[3];
        var $extends = classMatch[5];
        var constructor;
        var publicConstructor = joo[className] = function() {
          constructor.apply(this, arguments);
        };
        var superConstructor;
        if ($extends) {
          superConstructor = joo[$extends];
          publicConstructor.prototype = clone(superConstructor.prototype);
        } else {
          superConstructor = Object;
          $extends = "Object";
        }
        publicConstructor.prototype[$extends] = superConstructor;
        var members = memberFactory(publicConstructor, {$super: $extends});
        var staticInitializer;
        for (var i = 0; i < members.length; ++i) {
          var memberDeclaration = members[i];
          switch (typeof memberDeclaration) {
            case "function": staticInitializer = memberDeclaration; break;
            case "string":
              var isStatic = memberDeclaration.match(/\bstatic\b/);
              var target = isStatic ? publicConstructor : publicConstructor.prototype;
              var member = members[++i];
              if (typeof member == "function") {
                var methodName = memberDeclaration.match(/function\s+([a-zA-Z$_0-9]+)/)[1];
                if (methodName == className) {
                  constructor = member;
                } else {
                  target[methodName] = member;
                }
              } else {
                for (var m in member) {
                  target[m] = member[m];
                }
              }
          }
        }
        if (staticInitializer) {
          staticInitializer();
        }
      },
      init: function() {
        // ignore
      }
    }
  };
})(this);