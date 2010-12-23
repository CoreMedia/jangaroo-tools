(function(theGlobalObject){
  // define alias "js" for the top-level package, so that name-clashes in AS3 can be resolved:
  theGlobalObject.js = theGlobalObject;
  // defined here to avoid global name space pollution and unneccessary closures:
  function clone(object) {
    var empty = function(){ };
    empty.prototype =  object;
    return new empty();
  }
  function createGetQualified(create) {
    return (function(name) {
      var object = theGlobalObject;
      if (name) {
        var parts = name.split(".");
        for (var i=0; i<parts.length; ++i) {
          var subobject = object[parts[i]];
          try {
            if(String(subobject).indexOf("[JavaPackage")==0) {
              subobject =  null;
            }
          } catch(e) {
            // ignore
          }
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

  if (!theGlobalObject.joo) {
    theGlobalObject.joo = {};
  }
  joo.getOrCreatePackage = createGetQualified(true);
  joo.getQualifiedObject = createGetQualified(false);

  joo.is = function(object, type) {
      if (!type || object===undefined || object===null) {
        return false;
      }
      // constructor or instanceof may return false negatives:
      if (object.constructor === type || object instanceof type) {
        return true;
      }
      // special case meta-class Class:
      if (type === Class) {
        return !!object["$class"];
      }
      var classDeclaration = object.constructor["$class"];
      var typeDeclaration = type["$class"];
      if (classDeclaration && typeDeclaration) {
        return !!classDeclaration.Types.prototype[typeDeclaration.fullClassName];
      }
      return false;
    };

    joo.as = function (object, type) {
      return joo.is(object, type) ? object : null;
    };

    /*
    unsupported ActionScript features:
      - private non-static members
      - field initializers
      - typed catch clauses
      - dynamic class loading + resource bundles
      - all classes must reside within the joo package

     Caveat: static code blocks are executed immediately

     */
    joo.classLoader = {
      prepare: function(packageDef, classDef, inheritanceLevel, memberFactory, publicStaticMethodNames, dependencies, runtimeApiVersion, compilerVersion) {
        joo.runtimeApiVersion = runtimeApiVersion;
        joo.compilerVersion = compilerVersion;
        var classMatch = classDef.match(/^\s*((public|internal|final|dynamic)\s+)*class\s+([A-Za-z][a-zA-Z$_0-9]*)(\s+extends\s+([a-zA-Z$_0-9.]+))?(\s+implements\s+([a-zA-Z$_0-9.,\s]+))?\s*$/);
        var className = classMatch[3];
        var $extends = classMatch[5];
        var constructor;
        var publicConstructor = joo[className] = function() {
          constructor.apply(this, arguments);
        };
        var superConstructor;
        if ($extends) {
          superConstructor = joo.getQualifiedObject($extends);
          publicConstructor.prototype = clone(superConstructor.prototype);
        } else {
          superConstructor = Object;
        }
        publicConstructor.prototype["super$" + inheritanceLevel] = superConstructor;
        var privateStatics = {};
        var members = memberFactory(privateStatics);
        var staticInitializer;
        for (var i = 0; i < members.length; ++i) {
          var memberDeclaration = members[i];
          switch (typeof memberDeclaration) {
            case "function": staticInitializer = memberDeclaration; break;
            case "string":
              var isStatic = memberDeclaration.match(/\bstatic\b/);
              var isPrivate = memberDeclaration.match(/\bprivate\b/);
              var target = isStatic ? isPrivate ? privateStatics : publicConstructor : publicConstructor.prototype;
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
    };
})(this);
