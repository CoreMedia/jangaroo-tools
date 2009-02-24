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

Class = function Class(casted) {
  return casted;
};
Class.$class = {
  isInstance: function(object) {
    return typeof object=="function";
  }
};
trace = function(msg) {
  if (window.console) {
    console.log("AS3: "+msg);
  } else {
    document.writeln("AS3: "+msg);
  }
};
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
  if (this.$boundTo===object) {
    return this;
  }
  var fn = this;
  var f = function $boundMethod() {
    return fn.apply(object,arguments);
  };
  f.$boundTo = object;
  return f;
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
          subpackage = new (Package)();
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
  function getMethod(object, methodType, methodName) {
    return methodType=="get" ? object.__lookupGetter__(methodName)
      : methodType=="set" ? object.__lookupSetter__(methodName)
      : isFunction(object[methodName]) ? object[methodName]
      : null;
  }
  function setMethod(object, methodType, methodName, method) {
    switch(methodType) {
      case "get": object.__defineGetter__(methodName, method); break;
      case "set": object.__defineSetter__(methodName, method); break;
      default: object[methodName] = method;
    }
  }
  function createEmptyConstructor($constructor) {
    var emptyConstructor = function(){ this.constructor = $constructor; };
    emptyConstructor.prototype =  $constructor.prototype;
    return emptyConstructor;
  };
  function createCast() {
    return function cast(object) {
      return object;
    };
  }
  function createDefaultConstructor(superName) {
    return (function $DefaultConstructor() {
      this[superName].apply(this,arguments);
    });
  }
  function createBoundMethodFactory(method) {
    return (function() {
      var self = this;
      return setFunctionName(function $boundMethod() {
        return method.apply(self, arguments);
      }, method.getName());
    });
  }
  function setFunctionName(theFunction, name) {
    theFunction.getName = function() { return name; };
    return theFunction;
  }
  function registerPrivateMember(privateStatic, classPrefix, memberName) {
    var privateMemberName = classPrefix+memberName;
    privateStatic["$"+memberName] = privateMemberName;
    return privateMemberName;
  }
  function createGetClass($constructor) {
    return (function Object$getClass() { return $constructor; });
  }
  function emptySuper() {}
  function addImport(imports, fullClassName) {
    imports.push(fullClassName);
    var lastDotPos = fullClassName.lastIndexOf(".");
    var importName = lastDotPos >=0 ? fullClassName.substring(lastDotPos+1) : fullClassName;
    var importsByName = imports.byName;
    if (importName in importsByName && importsByName[importName]!=fullClassName) {
      delete importsByName[importName]; // remove ambigious import
    } else {
      importsByName[importName] = fullClassName;
    }
  }
  function bind(object, methodName) {
    var member = object[methodName];
    if (typeof member!="function" || member.$boundTo===object) {
      return member;
    }
    return object[methodName]=member.bind(object);
  }
  function assert(cond, file, line, column) {
    if (!cond)
      throw new Error(file+"("+line+":"+column+"): assertion failed");
  }
  function isInstance(object) {
    return object instanceof this.publicConstructor || object && object.constructor===this.publicConstructor;
  }
  function is(object, type) {
    if (!type || object===undefined || object===null)
      return false;
    if (type.$class) {
      return type.$class.isInstance(object);
    }
    // fallback:
    return object instanceof type || object.constructor===type;
  }
  function getDefinitionByName(fullClassName) {
    var classDef = ClassDescription.$static.getClassDescription(fullClassName);
    if (classDef) {
      classDef.initialize();
      return classDef.publicConstructor;
    }
    return null;
  }
  var ClassDescription = (function() {
    function getNativeClass(fullClassName) {
      var parts = fullClassName.split(".");
      var nativeClass = theGlobalObject;
      for (var i=0; i<parts.length; ++i) {
        nativeClass = nativeClass[parts[i]];
        if (!nativeClass) {
          return undefined;
        }
      }
      return nativeClass;
    }
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
        for (var i=0; i<classDescription.$imports.length; ++i) {
          var importDecl = classDescription.$imports[i];
          // trigger loading imported classes:
          this.getClassDescription(importDecl);
        }
        delete this.missingClassDescriptions[this.fullClassName];
        return classDescription;
      },
      getClassDescription: function(fullClassName) {
        var cd = this.classDescriptions[fullClassName];
        if (!cd) {
          var constr = getNativeClass(fullClassName);
          var constrType = typeof constr;
          if (constrType=="function" || constrType=="object") {
            if (joo.Class.debug && theGlobalObject.console) {
              console.debug("found non-Jangaroo class "+fullClassName+"!");
            }
            return this.registerClassDescription({
              fullClassName: fullClassName,
              $imports: [],
              $implements: [],
              state: this.INITIALIZED,
              level: -1,
              Public: createEmptyConstructor(constr),
              initialize: emptySuper,
              $constructor: function() { constr.apply(this,arguments); },
              // TODO: can this be simplified to $constructor: constr ?
              publicConstructor: constr,
              isInstance: isInstance
            });
          }
          this.missingClassDescriptions[fullClassName] = true;
        }
        return cd;
      },
      load: function(fullClassName) {
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
          if (joo.Class.debug && theGlobalObject.console) {
            console.debug("scheduling load "+fullClassName);
          }
        }
        // class description is or will be loaded:
        return true;
      },
      doComplete: function() {
        if ("oncomplete" in this) {
          /*
          if (joo.Class.debug && theGlobalObject.console) {
            console.debug("doComplete active. Still loading:");
            for (var loading in this.loadingClasses) {
              console.debug("  "+loading);
            }
            console.debug("End 'Still loading'.");
          }
          */
          var missingCDsMap = this.missingClassDescriptions;
          for (var missingClassName in missingCDsMap) {
            this.load(missingClassName);
          }
          if (this.loadCheckTimer) {
            window.clearTimeout(this.loadCheckTimer);
            this.loadCheckTimer = undefined;
          }
          if (this.loadingClassesCount==0) {
            var oncomplete = this.oncomplete;
            if (typeof oncomplete=="function") {
              delete this.oncomplete; // only execute once! (problem in Opera only)
              this.initImports(importedClasses);
              oncomplete(importedClasses.byName);
            }
          } else if (joo.Class.classLoadTimeoutMS) {
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
      initImports: function(imports) {
        for (var i=0; i<imports.length; ++i) {
          var importedClassDesc = this.getClassDescription(imports[i]);
          if (!importedClassDesc) {
            throw new Error("Class should have been loaded: "+imports[i]);
          }
          importedClassDesc.initialize();
        }
        this.transformImports(imports);
      },
      transformImports: function(imports) {
        var importsByName = imports.byName;
        for (var im in importsByName) {
          if (typeof importsByName[im]=="string") {
            importsByName[im] = this.getClassDescription(importsByName[im]).publicConstructor;
          }
          //else throw new Error("Wrong type for "+im+"->"+importsByName[im]+" ("+typeof importsByName[im]+")!");
        }
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
      this._qualifyByImports(this, "$extends");
      for (var i=0; i<this.$implements.length; ++i) {
        this._qualifyByImports(this.$implements, i);
      }
      this.fullClassName = this.$package ? (this.$package + "." + this.$class) : this.$class;
      if (joo.Class.debug && theGlobalObject.console) {
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
        $interface: undefined,
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
        _qualifyByImports: function(bean, property) {
          var fqn = this.$imports.byName[bean[property]];
          if (fqn) {
            bean[property] = fqn;
          }
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
          this.$package[className] = this.publicConstructor = setFunctionName(
            this.$interface
              ? createCast()
              : function() {
                  this.constructor =  classDescription.publicConstructor;
                  classDescription.$constructor.apply(this,arguments);
                },
            this.fullClassName);
          this.publicConstructor.$class = this; // back-link for debugging only
          // to initialize when calling the first public static method, wrap those methods:
          for (var i=0; i<this.$publicStaticMethods.length; ++i) {
            this.createInitializingPublicStaticMethod(this.$publicStaticMethods[i]);
          }
          if (this.superClassDescription) {
            this.publicConstructor.prototype = new (this.superClassDescription.Public)();
          }
          // TODO: only if not final:
          this.Public = createEmptyConstructor(this.publicConstructor);
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
          var privateStatic = {$super: superName, assert: assert, is: is};

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
              $internal: this.Public.prototype,
              $private: this.Public.prototype
            },
            $static: {
              fieldsWithInitializer: [],
              $public: publicConstructor,
              $protected: publicConstructor,
              $internal: publicConstructor,
              $private: privateStatic
            }
          };
          var memberDeclarations = this.$members(publicConstructor, privateStatic);
          var i=0;
          while (i<memberDeclarations.length) {
            var memberKey = "$this"; // default: not static
            var visibility = "$internal"; // default: internal visibility
            var bound = false;
            var members = memberDeclarations[i++];
            if (members===undefined) {
              continue;
            }
            var memberType = "function";
            var methodType = "method";
            var memberName = undefined;
            var modifiers;
            if (typeof members=="string") {
              modifiers = members.split(/\s+/);
              for (var j=0; j<modifiers.length; ++j) {
                var modifier = modifiers[j];
                if (modifier=="static") {
                  memberKey = "$static";
                } else if (modifier=="private" || modifier=="public" || modifier=="protected" || modifier=="internal") {
                  visibility = "$"+modifier;
                } else if (modifier=="bound") {
                  bound = true;
                } else if (modifier=="var" || modifier=="const") {
                  memberType = modifier;
                } else if (modifier=="get" || modifier=="set") {
                  methodType = modifier;
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
                setFunctionName(members, methodType == "method" ? memberName : (methodType+"$"+memberName));
                if (memberName==this.$class) {
                  this.$constructor = members;
                } else {
                  if (memberKey=="$this") {
                    if (visibility=="$private") {
                      memberName = registerPrivateMember(privateStatic, classPrefix, memberName);
                    } else {
                      var overriddenMethod = getMethod(target, methodType, memberName);
                      if (overriddenMethod) {
                        // Found overriding! Store super method as private method delegate for super access:
                        setMethod(this.Public.prototype, methodType, registerPrivateMember(privateStatic, classPrefix, memberName), overriddenMethod);
                      }
                    }
                  }
                  if (bound) {
                    // replace method by a bound method factory...
                    members = createBoundMethodFactory(members);
                    // ...and add it to the "field" initializers, so that the factory is invoked in super call:
                    targetMap.$this.fieldsWithInitializer.push(memberName);
                  }
                  setMethod(target, methodType, memberName, members);
                }
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
          // transform imports from String to Class:
          transformImports(this.$imports);
          for (var im in this.$imports.byName) {
            privateStatic[im] = this.$imports.byName[im];
          }
          // init static fields with initializer:
          initFields(privateStatic, publicConstructor, targetMap.$static.fieldsWithInitializer);
          this.state = INITIALIZED;
          return this.publicConstructor;
        },
        /**
         * Determines if the specified <code>Object</code> is assignment-compatible
         * with the object represented by this <code>ClassDefinition</code>.
         * The method returns <code>true</code> if the specified
         * <code>Object</code> argument is non-null and can be cast to the
         * reference type represented by this <code>Class</code> object without
         * raising a <code>ClassCastException.</code> It returns <code>false</code>
         * otherwise.
         */
        isInstance: function(object) {
          if (object && typeof object.getClass=="function") {
            return this.isAssignableFrom(getClassDescription(object.getClass().getName()));
          }
          return false;
        },
        /**
         * Determines if the class or interface represented by this
         * <code>ClassDefinition</code> object is either the same as, or is a superclass or
         * superinterface of, the class or interface represented by the specified
         * <code>ClassDefinition</code> parameter. It returns <code>true</code> if so;
         * otherwise it returns <code>false</code>.
         */
        isAssignableFrom: function(classDef) {
          var cd = classDef;
          do {
            if (this===cd) {
              return true;
            }
            // TODO: optimize: pre-calculate set of all implemented interfaces of a class!
            if (this.$interface) {
              // I am an interface: search all implemented interfaces recursively:
              var interfaces = cd.$implements;
              for (var i=0; i<interfaces.length; ++i) {
                var interfaceDef = getClassDescription(interfaces[i]);
                if (this.isAssignableFrom(interfaceDef)) {
                  return true;
                }
              }
            }
            cd = cd.superClassDescription;
          } while(cd);
          return false;
        }
      };
    }
    ClassDescription.$static = ClassDescription$static;
    return ClassDescription;
  })();
  function Package() { }
  theGlobalObject.joo = new (Package)();
  var importedClasses = [];
  importedClasses.byName = {};
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
      addImport(importedClasses, importedFullClassName);
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
      var imports = [];
      imports.byName = {};
      imports.packages = [];
      for (var im=1; im<arguments.length-3; ++im) {
        var importMatch = arguments[im].match(/^\s*import\s+(([a-zA-Z$_0-9]+\.)*)(\*|[a-zA-Z$_0-9]+)\s*$/);
        var importPackageName = importMatch[1]; // including last dot
        var importClassName = importMatch[3];
        if (importClassName == "*") {
          imports.packages.push(importPackageName);
        } else {
          addImport(imports, importPackageName+importClassName);
        }
      }
      var packageName =  "";
      if (typeof packageDef=="string") {
        packageName = packageDef.split(/\s+/)[1];
        imports.packages.push(packageName+".");
      }
      // TODO: find a way to use imports.packages for resolving extends and implements!
      var $interface = false;
      var classMatch = classDef.match(/^\s*((public|internal)\s+)?class\s+([A-Za-z][a-zA-Z$_0-9]*)(\s+extends\s+([a-zA-Z$_0-9.]+))?(\s+implements\s+([a-zA-Z$_0-9.,\s]+))?\s*$/);
      var $extends = "Object";
      var interfaces;
      if (classMatch) {
        if (classMatch[5]) {
          $extends = classMatch[5];
        }
        interfaces = classMatch[7];
      } else {
        $interface = true;
        classMatch = classDef.match(/^\s*((public|internal)\s+)?interface\s+([A-Za-z][a-zA-Z$_0-9]*)(\s+extends\s+([a-zA-Z$_0-9.,\s]+))?\s*$/);
        interfaces = classMatch[5];
      }
      if (!classMatch) {
        throw new Error("SyntaxError: \""+classDef+"\" does not match.");
      }
      interfaces = interfaces ? interfaces.split(/\s*,\s*/) : [];
      new ClassDescription({
        $imports: imports,
        $publicStaticMethods: publicStaticMethods,
        $members: members,
        $package: packageName,
        visibility: classMatch[2],
        $interface : $interface,
        $class    : classMatch[3],
        $extends  : $extends,
        $implements : interfaces
      });
    },
    init: function(/*...classes*/) {
      var clazz;
      for (var i=0; i<arguments.length; ++i) {
        if (typeof arguments[i].getName=="function") {
          clazz = ClassDescription.$static.getClassDescription(arguments[i].getName()).initialize();
        }
      }
      return clazz;
    },
    complete: function(oncomplete) {
      ClassDescription.$static.oncomplete = oncomplete || true;
      ClassDescription.$static.doComplete();
    },
    dumpClasses: function() {
      return ClassDescription.$static.dumpClasses();
    }
  };
  theGlobalObject.joo.assert = assert;
  theGlobalObject.joo.is = is;
  theGlobalObject.joo.bind = bind;
  theGlobalObject.joo.getDefinitionByName = getDefinitionByName;
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
