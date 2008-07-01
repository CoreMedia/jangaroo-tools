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
  function createMethod(object) {
    for (var name in object) {
      return setFunctionName(object[name], name);
    }
  }
  function createEmptyConstructor($prototype) {
    var $constructor = new Function();
    $constructor.prototype =  $prototype;
    return $constructor;
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
  var ClassDescription = (function() {
    var ClassDescription$static = {
      // static members:
      PENDING: 0,
      PREPARING: 1,
      PREPARED: 2,
      INITIALIZING: 3,
      INITIALIZED: 4,
      classDescriptions: {},
      pendingClassDescriptions: {},
      getClassDescription: function(fullClassName) {
          return this.classDescriptions[fullClassName];
      },
      waitForSuper: function(classDef) {
        var pendingCDs = this.pendingClassDescriptions[classDef.$extends];
        if (!pendingCDs) {
          pendingCDS = this.pendingClassDescriptions[classDef.$extends] = [];
        }
        pendingCDS.push(classDef);
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
      this.fullClassName = this.$package + "." + this.$class;
      ClassDescription$static.classDescriptions[this.fullClassName] = this;
      this.prepare();
    }
    with(ClassDescription$static) {
      // instance members:
      ClassDescription.prototype = {
        fullClassName: undefined,
        $extends: "joo.lang.JOObject",
        level: undefined,
        state: PENDING,
        superClassDescription: undefined,
        $constructor: undefined,
        Public: undefined,
        publicStatic: undefined,
        getStatic: undefined,
        /**
         * Prepares this class to be used by constructor, by accessing a static member, or as a super class.
         * The actual class loading is done when any of this three methods is called.
         */
        prepare: function() {
          if (this.state===PREPARING)
            throw new Error("cyclic usages between classes "+this.fullClassName+" and "+this.superClassDescription.fullClassName+".");
          if (this.state!==PENDING)
            return;
          if (this.fullClassName=="joo.lang.JOObject") {
            this.$extends = null;
            this.superClassDescription = null;
          } else {
            this.superClassDescription = getClassDescription(this.$extends);
            if (!this.superClassDescription || this.superClassDescription.state==PENDING) {
              // super class not yet loaded, stay pending and wait for super class:
              waitForSuper(this);
              return;
            }
          }
          this.state = PREPARING;
          // Only do the minimal setup to allow a preliminary, initializing public constructor and static getter,
          // and to allow subclasses to plug their constructor into this class.
          // create preliminary constructor and static getter that initialize before delegating to the real ones:
          this.level = this.superClassDescription ? this.superClassDescription.level + 1 : 0;
          var classDescription = this;
          this.$constructor = function() {
            classDescription.initialize();
            classDescription.$constructor.apply(this,arguments);
          };
          setFunctionName(this.$constructor, this.fullClassName);
          if (this.superClassDescription) {
            this.$constructor.prototype = new (this.superClassDescription.Public)();
          }
          // TODO: only if not final:
          this.Public = createEmptyConstructor(this.$constructor.prototype);
          // static part:
          this.getStatic = function() {
            classDescription.initialize();
            return classDescription.getStatic();
          }
          // TODO: only if public:
          this.$package = createPackage(this.$package);
          this.$package[this.$class] = this.$constructor;
          this.$package[this.$class+"_"] = this.getStatic;

          this.state = PREPARED;
          prepareSubclasses(this);
        },
        /**
         * Initializes this class by finishing the class setup and then invoking all static initializers.
         */
        initialize: function() {
          if (this.state!==PREPARED)
            return;
          this.state = INITIALIZING;
          // finish object structure setup of this class:
          // public part: avoid recursion!
          this.$package[this.$class+"_"] = this.getStatic = undefined;
          this.$package[this.$class] = this.$constructor = undefined;

          // private part of the object structure:
          var classPrefix = this.level; // + "$";
          var fieldsWithInitializer = [];
          var classDescription = this;
          if (this.superClassDescription) {
            var superName = classPrefix+"super";
            this.Public.prototype[superName] = function $super() {
              this[superName] = function() {throw new Error("may only call super() once in "+classDescription.fullClassName)};
              classDescription.superClassDescription.$constructor.apply(this,arguments);
              initFields(null, this, fieldsWithInitializer);
            };
          }
          // static part:
          this.$package[this.$class+"_"] = this.getStatic = function() {
            // overwrite, this time without initializing:
            return classDescription.publicStatic;
          };
          var publicStatic = this.publicStatic = {};
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
              $public: publicStatic,
              $protected: publicStatic,
              $private: privateStatic
            }
          };
          if (isFunction(this.$members)) {
            var memberDeclarations = this.$members(publicStatic, privateStatic);
            var i=0;
            while (i<memberDeclarations.length) {
              var memberKey = "$this"; // default: not static
              var visibility = "$public"; // default: public visibility
              var members = memberDeclarations[i++];
              if (members===undefined) {
                continue;
              }
              var memberType = "function";
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
	      var memberName;
	      if (memberType=="function") {
                if (typeof members=="object") {
                  members = createMethod(members);
                }
                memberName = members.getName();
                if (memberName=="") {
                  // found static code block; execute on initialization
                  targetMap.$static.fieldsWithInitializer.push(members);
                } else {
                  if (memberName==this.$class) {
                    this.$constructor = members;
                  } else if (memberKey=="$this") {
                    if (visibility=="$private") {
                      memberName = registerPrivateMember(privateStatic, classPrefix, memberName);
                      setFunctionName(members, memberName);
                    } else if (isFunction(target[memberName])) {
                      // Found overriding! Store super method as private method delegate for super access:
                      this.Public.prototype[registerPrivateMember(privateStatic, classPrefix, memberName)] = target[memberName];
                    }
                  }
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
                    var initFunctionName =  member.getName();
                    if (initFunctionName=="" || initFunctionName.indexOf('$')!=-1) {
                      targetFieldsWithInitializer.push(memberName);
                    }
                  }
                }
              }
            }
          }
          if (!this.$constructor) {
            this.$constructor = createDefaultConstructor(superName);
          }
          setFunctionName(this.$constructor, this.fullClassName);
          this.$constructor.prototype = this.Public.prototype;
          this.Public.prototype.getClass = createGetClass(this.$constructor);
          // TODO: constructor visibility!
          this.$package[this.$class] = this.$constructor;
          // init static fields with initializer:
          initFields(privateStatic, publicStatic, targetMap.$static.fieldsWithInitializer);
        }
      };
    }
    ClassDescription.$static = ClassDescription$static;
    return ClassDescription;
  })();
  function Package() { }
  theGlobalObject.joo = new Package();
  theGlobalObject.joo.Class = {};
  var loadedClasses = {};
  theGlobalObject.joo.Class.load = function(fullClassName) {
    if (!loadedClasses[fullClassName]) {
      loadedClasses[fullClassName] = true;
      var uri = document.location.href;
      uri = uri.substring(0, uri.lastIndexOf("/")+1);
      uri += fullClassName.replace(/\./g,"/")+".js";
      var script = document.createElement("script");
      script.src = uri;
      document.body.appendChild(script);
    }
  };
  theGlobalObject.joo.Class.run = function(fullClassName, args) {
    theGlobalObject.joo.Class.load(fullClassName);
    theGlobalObject.onload = function() {
      eval(fullClassName+"_()").main(args);
    }
  }
  theGlobalObject.joo.Class.prepare = function(packageDef /* import*, classDef, members */) {
    var classDef = arguments[arguments.length-2];
    var members = arguments[arguments.length-1];
    var classDesc = { $members: members };
    if (typeof packageDef!="string")
      throw new Error("package declaration must be a string.");
    var packageParts = packageDef.split(/\s+/);
    if (packageParts[0]!="package")
      throw new Error("package declaration must start with 'package'.");
    if (packageParts.length!=2) {
      throw new Error("package declaration must be followed by a package name.");
    }
    classDesc.$package = packageParts[1];

    if (typeof classDef!="string")
      throw new Error("class declaration must be a string.");
    var classParts = classDef.split(/\s+/);
    var i=0;
    if (classParts[i]=="public") {
      classDesc.visibility = classParts[i++];
    }
    if (classParts[i]=="abstract") {
      classDesc.$abstract = true;
      ++i;
    }
    if (classParts[i++]!="class")
      throw new Error("expected 'class' after class modifiers.");
    if (i==classParts.length) {
      throw new Error("expected class name after keyword 'class'.");
    }
    classDesc.$class = classParts[i++];
    if (i<classParts.length) {
      if (classParts[i++]!="extends")
        throw new Error("expected EOL or 'extends' after class name.");
      if (i==classParts.length)
        throw new Error("expected class name after 'extends'.");
      classDesc.$extends = classParts[i++];
    }
    if (i<classParts.length)
      throw new Error("unexpected token '"+classParts[i]+" after class declaration.");
    new ClassDescription(classDesc);
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
