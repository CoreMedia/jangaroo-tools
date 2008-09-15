// Copyright 2008 CoreMedia AG
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an "AS
// IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
// express or implied. See the License for the specific language
// governing permissions and limitations under the License.

// JangarooScript runtime support. Author: Frank Wienberg

Function.prototype.getName = typeof Function.prototype.name=="string"
? (function getName() { return this.name; })
: (function() {
  var nameRE = /function +([a-zA-Z\$_][a-zA-Z\$_0-9]*) *\(/;
  return (function getName() {
    if (!("name" in this)) {
      var matches = nameRE.exec(this.toString());
      var name = matches ? matches[1] : "";
      if (name=="anonymous")
        name = "";
      this.name = name;
    }
    return this.name;
  });
}());

Function.prototype.bind = function(object) {
  var fn = this;
  return function() {
    return fn.apply(object,arguments);
  };
};

(function(theGlobalObject) {
  function initFields(privateBean, publicBean, fieldNamesAndInitializers) {
    for (var i=0; i<fieldNamesAndInitializers.length; ++i) {
      var fieldNameOrInitializer = fieldNamesAndInitializers[i];
      if (typeof fieldNameOrInitializer == "function") {
        fieldNameOrInitializer();
      } else {
        //alert("init field: "+fieldNameOrInitializer);
        var bean = publicBean[fieldNameOrInitializer] ? publicBean : privateBean;
        bean[fieldNameOrInitializer] = bean[fieldNameOrInitializer]();
      }
    }
  }
  function createPackage(packageName) {
    var $package = theGlobalObject;
    if (packageName) {
      var packageParts = packageName.split(".");
      for (var i=0; i<packageParts.length; ++i) {
        var subpackage = $package[packageParts[i]];
        if (!subpackage) {
          subpackage = new Package();
          $package[packageParts[i]] = subpackage;
        }
        $package = subpackage;
      }
    }
    return $package;
  }
  function isFunction(object) {
    return typeof object=="function" && object.constructor!==RegExp;
  }
  function createEmptyConstructor($prototype) {
    var emptyConstructor = function(){};
    emptyConstructor.prototype =  $prototype;
    return emptyConstructor;
  };
  function createDefaultConstructor(superName) {
    return (function $DefaultConstructor() {
      this[superName].apply(this,arguments);
    });
  }
  function setFunctionName(theFunction, name) {
    theFunction.getName = function() { return name; };
    return theFunction;
  }
  function registerPrivateMember(privateStatic, classPrefix, memberName) {
    var privateMemberName = classPrefix+memberName;
    privateStatic["_"+memberName] = privateMemberName;
    return privateMemberName;
  }
  function createGetClass($constructor) {
    return (function Object$getClass() { return $constructor; });
  }
  function emptySuper() {}
  function getClassName(fullClassName) {
    var lastDotPos = fullClassName.lastIndexOf(".");
    return lastDotPos >=0 ? fullClassName.substring(lastDotPos+1) : fullClassName;
  }
  var ClassDescription = (function() {
    var ClassDescription$static = {
      // static members:
      PENDING: 0,
      PREPARING: 1,
      PREPARED: 2,
      INITIALIZING: 3,
      INITIALIZED: 4,
      classDescriptions: {},
      missingClassDescriptions: {},
      pendingClassDescriptions: {},
      loadingClasses: {},
      loadingClassesCount: 0,
      loadCheckTimer: undefined,
      registerClassDescription: function(classDescription) {
        if (classDescription.fullClassName in this.loadingClasses) {
          delete this.loadingClasses[classDescription.fullClassName];
          --this.loadingClassesCount;
        }
        this.classDescriptions[classDescription.fullClassName] = classDescription;
        for (var im in classDescription.$imports) {
          var importDecl = classDescription.$imports[im];
          // trigger loading imported classes:
          this.getClassDescription(importDecl);
        }
        delete this.missingClassDescriptions[this.fullClassName];
      },
      getClassDescription: function(fullClassName) {
        if (!fullClassName || fullClassName=="undefined") {
          fullClassName.debug();
        }
        var cd = this.classDescriptions[fullClassName];
        if (!cd) {
          this.missingClassDescriptions[fullClassName] = true;
        }
        return cd;
      },
      load: function(fullClassName) {
        if (joo.Class.debug && console) {
          console.debug("trying to load class "+fullClassName+"...");
        }
        if (!this.getClassDescription(fullClassName)) {
          if (this.loadingClasses[fullClassName]) {
            // class description is not there, but we already queued to load it:
            return false;
          }
          this.loadingClasses[fullClassName] = true;
          ++this.loadingClassesCount;
          var uri = theGlobalObject.document.location.href;
          uri = uri.substring(0, uri.lastIndexOf("/")+1);
          uri += fullClassName.replace(/\./g,"/")+".js";
          joo.Class.loadScript(uri);
          if (joo.Class.debug && console) {
            console.debug("scheduling load "+fullClassName);
          }
        }
        // class description is or will be loaded:
        return true;
      },
      doComplete: function() {
        var type = typeof this.oncomplete;
        if (type!="undefined") {
          if (joo.Class.debug && console) {
            console.debug("doComplete active. Still loading:");
            for (var loading in this.loadingClasses) {
              console.debug("  "+loading);
            }
            console.debug("End 'Still loading'.");
          }
          var missingCDsMap = this.missingClassDescriptions;
          for (var missingClassName in missingCDsMap) {
            this.load(missingClassName);
          }
          if (this.loadCheckTimer) {
            window.clearTimeout(this.loadCheckTimer);
            this.loadCheckTimer = undefined;
          }
          if (this.loadingClassesCount==0) {
            if (type=="function") {
              this.oncomplete(this.addImports(importedClasses, importedClasses));
            }
          } else if (this.classLoadTimeoutMS) {
            this.loadCheckTimer = theGlobalObject.setTimeout((function() {
              if (this.loadingClassesCount!=0) {
                var sb = [];
                for (var loading in this.loadingClasses) {
                  sb.push(loading);
                }
                throw new Error("The following classes were not loaded after "+joo.Class.classLoadTimeoutMS+" milliseconds: "+sb.join(", "));
              }
            }).bind(this), joo.Class.classLoadTimeoutMS);
          }
        }
      },
      dumpClasses: function() {
        var sb = [];
        for (var cd in this.classDescriptions) {
          sb.push(this.classDescriptions[cd].fullClassName,"\n");
        }
        return sb.join("");
      },
      addImports: function(importObject, imports) {
        for (var importName in imports) {
          var importedClassDesc = this.getClassDescription(imports[importName]);
          if (!importedClassDesc) {
            throw new Error("Class should have been loaded: "+imports[importName]);
          }
          importObject[importName] = importedClassDesc.initialize();
        }
        return importObject;
      },
      waitForSuper: function(classDef) {
        var pendingCDs = this.pendingClassDescriptions[classDef.$extends];
        if (!pendingCDs) {
          pendingCDs = this.pendingClassDescriptions[classDef.$extends] = [];
        }
        pendingCDs.push(classDef);
      },
      prepareSubclasses: function(classDef) {
        var pendingCDS = this.pendingClassDescriptions[classDef.fullClassName];
        if (pendingCDS) {
          delete this.pendingClassDescriptions[classDef.fullClassName];
          for (var c=0; c<pendingCDS.length; ++c) {
            pendingCDS[c].prepare();
          }
        }
      }
    };
    // constructor:
    function ClassDescription(classDef) {
      for (var m in classDef) {
        this[m] = classDef[m];
      }
      if (this.$imports[this.$extends]) {
        this.$extends = this.$imports[this.$extends];
      }
      this.fullClassName = this.$package ? (this.$package + "." + this.$class) : this.$class;
      if (joo.Class.debug && console) {
        console.debug("loaded class "+this.fullClassName);
      }
      ClassDescription$static.registerClassDescription(this);
      this.prepare();
      ClassDescription.$static.doComplete();
    }
    with(ClassDescription$static) {
      // instance members:
      ClassDescription.prototype = {
        fullClassName: undefined,
        $extends: undefined,
        level: 0,
        state: PENDING,
        superClassDescription: null,
        $constructor: undefined,
        Public: undefined,
        publicConstructor: undefined,
        createInitializingPublicStaticMethod: function(methodName) {
          var classDescription = this;
          this.publicConstructor[methodName] = function() {
            classDescription.initialize();
            return classDescription.publicConstructor[methodName].apply(null, arguments);
          };
        },
        /**
         * Prepares this class to be used by constructor, by accessing a static member, or as a super class.
         * The actual class loading is done when any of these three methods is called.
         */
        prepare: function() {
          if (this.state===PREPARING)
            throw new Error("cyclic usages between classes "+this.fullClassName+" and "+this.superClassDescription.fullClassName+".");
          if (this.state!==PENDING)
            return;
          if (this.$extends!="Object") {
            this.superClassDescription = getClassDescription(this.$extends);
            if (!this.superClassDescription || this.superClassDescription.state==PENDING) {
              // super class not yet loaded, stay pending and wait for super class:
              waitForSuper(this);
              return;
            }
            this.level = this.superClassDescription.level + 1;
          }
          this.state = PREPARING;
          // Only do the minimal setup to allow a preliminary, initializing public constructor and static getter,
          // and to allow subclasses to plug their constructor into this class.
          // create preliminary constructor and static getter that initialize before delegating to the real ones:
          var classDescription = this;
          this.$constructor = function() {
            classDescription.initialize();
            classDescription.$constructor.apply(this,arguments);
          };
          this.$package = createPackage(this.$package);
          var className = this.$class;
          this.$package[className] = this.publicConstructor = setFunctionName(function() {
            classDescription.$constructor.apply(this,arguments);
          }, this.fullClassName);
          // to initialize when calling the first public static method, wrap those methods:
          for (var i=0; i<this.$publicStaticMethods.length; ++i) {
            this.createInitializingPublicStaticMethod(this.$publicStaticMethods[i]);
          }
          if (this.superClassDescription) {
            this.publicConstructor.prototype = new (this.superClassDescription.Public)();
          }
          // TODO: only if not final:
          this.Public = createEmptyConstructor(this.publicConstructor.prototype);
          this.state = PREPARED;
          prepareSubclasses(this);
        },
        /**
         * Initializes this class by finishing the class setup and then invoking all static initializers.
         */
        initialize: function() {
          if (this.state!==PREPARED)
            return this.publicConstructor;
          this.state = INITIALIZING;
          // finish object structure setup of this class:
          // public part: avoid recursion!
          this.$constructor = undefined;

          // private part of the object structure:
          // assert this.$extends=="Object" || this.superClassDescription!=null;
          var classPrefix = this.level;
          var fieldsWithInitializer = [];
          var classDescription = this;
          var superName = classPrefix+"super";
          // static part:
          var publicConstructor = this.publicConstructor;
          var privateStatic = {_super: superName};

          if (this.superClassDescription) {
            // init super class:
            this.superClassDescription.initialize();
          }

          // evaluate $members, transfer members into the prepared objects:

          // Define a mapping to efficiently find the right prototype object to store a member,
          // depending on its modifiers.
          // Note: As long as "protected" is not implemented, treat it like "public".
          var targetMap = {
            $this: {
              fieldsWithInitializer: fieldsWithInitializer,
              $public: this.Public.prototype,
              $protected: this.Public.prototype,
              $private: this.Public.prototype
            },
            $static: {
              fieldsWithInitializer: [],
              $public: publicConstructor,
              $protected: publicConstructor,
              $private: privateStatic
            }
          };
          var memberDeclarations = this.$members(publicConstructor, privateStatic);
          var i=0;
          while (i<memberDeclarations.length) {
            var memberKey = "$this"; // default: not static
            var visibility = "$public"; // default: public visibility
            var members = memberDeclarations[i++];
            if (members===undefined) {
              continue;
            }
            var memberType = "function";
            var memberName = undefined;
            var modifiers;
            if (typeof members=="string") {
              modifiers = members.split(" ");
              for (var j=0; j<modifiers.length; ++j) {
                var modifier = modifiers[j];
                if (modifier=="static") {
                  memberKey = "$static";
                } else if (modifier=="private" || modifier=="public" || modifier=="protected") {
                  visibility = "$"+modifier;
                } else if (modifier=="var" || modifier=="const") {
                  memberType = modifier;
                } else if (modifier=="override") {
                  // so far: ignore. TODO: enable super-call!
                } else if (j==modifiers.length-1) {
                  // last "modifier" is the member name:
                  memberName = modifier;
                } else {
                  throw new Error("Unknown modifier '"+modifier+"'.");
                }
              }
              if (i>=memberDeclarations.length) {
                throw new Error("Member expected after modifiers "+modifiers.join(" "));
              }
              members = memberDeclarations[i++];
            } else {
              modifiers = [];
            }
            var target = targetMap[memberKey][visibility];
            //document.writeln("defining "+modifiers.join(" ")+" member(s):");
            if (memberType=="function") {
              if (!memberName) {
                // found static code block; execute on initialization
                targetMap.$static.fieldsWithInitializer.push(members);
              } else {
                if (memberName=="_"+this.$class) {
                  this.$constructor = members;
                  memberName = this.$class;
                } else if (memberKey=="$this") {
                  if (visibility=="$private") {
                    memberName = registerPrivateMember(privateStatic, classPrefix, memberName);
                    setFunctionName(members, memberName);
                  } else if (isFunction(target[memberName])) {
                    // Found overriding! Store super method as private method delegate for super access:
                    this.Public.prototype[registerPrivateMember(privateStatic, classPrefix, memberName)] = target[memberName];
                  }
                }
                setFunctionName(members, memberName);
                target[memberName] = members;
              }
            } else {
              var targetFieldsWithInitializer = targetMap[memberKey].fieldsWithInitializer;
              for (memberName in members) {
                var member = members[memberName];
                if (memberKey=="$this" && visibility=="$private") {
                  memberName = registerPrivateMember(privateStatic, classPrefix, memberName);
                }
                target[memberName] = member;
                if (isFunction(member)) {
                  targetFieldsWithInitializer.push(memberName);
                }
              }
            }
          }
          this.Public.prototype[superName] =
            classDescription.superClassDescription
            ? fieldsWithInitializer.length > 0
              ? (function $super() {
                   classDescription.superClassDescription.$constructor.apply(this,arguments);
                   initFields(null, this, fieldsWithInitializer);
                 })
              : classDescription.superClassDescription.$constructor
            : fieldsWithInitializer.length > 0
              ? (function $super() {
                   initFields(null, this, fieldsWithInitializer);
                 })
              : emptySuper;
          if (!this.$constructor) {
            this.$constructor = createDefaultConstructor(superName);
          }
          this.Public.prototype.getClass = createGetClass(publicConstructor);
          // TODO: constructor visibility!
          privateStatic[this.$class] = publicConstructor;
          // init imports:
          addImports(privateStatic, this.$imports);
          // init static fields with initializer:
          initFields(privateStatic, publicConstructor, targetMap.$static.fieldsWithInitializer);
          return publicConstructor;
        }
      };
    }
    ClassDescription.$static = ClassDescription$static;
    return ClassDescription;
  })();
  function Package() { }
  theGlobalObject.joo = new Package();
  var importedClasses = {};
  theGlobalObject.joo.Class = {
    debug: false,
    classLoadTimeoutMS: false,
    loadScript: function(uri) {
      var script = document.createElement("script");
      script.type = "text/javascript";
      theGlobalObject.document.body.appendChild(script);
      script.src = uri;
      return script;
    },
    load: function(fullClassName) {
      return ClassDescription.$static.load(fullClassName);
    },
    $import: function(importedFullClassName) {
      importedClasses[getClassName(importedFullClassName)] = importedFullClassName;
      this.load(importedFullClassName);
    },
    run: function(mainClass) {
      if (typeof mainClass=="string") {
        this.load(mainClass);
      }
      var args = [];
      for (var i=1; i<arguments.length; ++i) {
        args.push(arguments[i]);
      }
      this.complete(function() {
        if (typeof mainClass=="string") {
          mainClass = ClassDescription.$static.getClassDescription(mainClass).publicConstructor;
        }
        mainClass.main.apply(null,args);
      });
    },
    prepare: function(packageDef /* import*, classDef, publicStaticMethods, members */) {
      var classDef = arguments[arguments.length-3];
      var publicStaticMethods = arguments[arguments.length-2];
      var members = arguments[arguments.length-1];
      var imports = {};
      for (var im=1; im<arguments.length-3; ++im) {
        var importFullClassName = arguments[im].match(/^\s*import\s+([a-zA-Z$_0-9.]+)\s*$/)[1];
        imports[getClassName(importFullClassName)] = importFullClassName;
      }
      var classMatch = classDef.match(/^\s*((public|internal)\s+)?(abstract\s+)?class\s+([A-Za-z][a-zA-Z$_0-9]*)(\s+extends\s+([a-zA-Z$_0-9.]+))?\s*$/);
      if (!classMatch) {
        throw new Error("SyntaxError: \""+classDef+"\" does not match.");
      }
      new ClassDescription({
        $imports: imports,
        $publicStaticMethods: publicStaticMethods,
        $members: members,
        $package: typeof packageDef=="string" ? packageDef.split(/\s+/)[1] : "",
        visibility: classMatch[2],
        $abstract : !!classMatch[3],
        $class    : classMatch[4],
        $extends  : classMatch[6] || "Object"
      });
    },
    init: function(clazz) {
      return ClassDescription.$static.getClassDescription(clazz.getName()).initialize();
    },
    complete: function(oncomplete) {
      ClassDescription.$static.oncomplete = oncomplete || true;
      ClassDescription.$static.doComplete();
    },
    dumpClasses: function() {
      return ClassDescription.$static.dumpClasses();
    }
  };
})(this);
//  alert("runtime loaded!");
joo.typeOf = function typeOf(obj){
  if (obj==undefined) return false;
  var type = typeof obj;
  if (type == 'object' || type == 'function'){
    switch(obj.constructor){
      case Array: return 'array';
      case RegExp: return 'regexp';
    }
    if (typeof obj.length == 'number' && obj.callee) {
      return 'arguments';
    }
  }
  return type;
};
