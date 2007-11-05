if (typeof RegExp.prototype.compile!="function") {
  RegExp.prototype.compile = function(regExp) {
    return this;
  };
}

Function.prototype.bind = function(object) {
  var fn = this;
  return function() {
    return fn.apply(object,arguments);
  };
}

Function.prototype.getName = typeof Function.prototype.name=="string"
? function getName() { return this.name; }
: function(nameRE) { return function getName() {
  if (typeof this.name=="undefined") {
    var matches = nameRE.exec(this.toString());
    name = matches ? matches[1] : "";
    if (name=="anonymous")
      name = "";
    this.name = name;
  }
  return this.name;
};}(new RegExp().compile("function +([a-zA-Z\\$_][a-zA-Z\\$_0-9]*) *\\("));

(function() {
  function initFields(privateBean, publicBean, fieldNames) {
    for (var i=0; i<fieldNames.length; ++i) {
      var fieldName = fieldNames[i];
      //alert("init field: "+fieldName);
      var bean = publicBean[fieldName] ? publicBean : privateBean;
      bean[fieldName] = bean[fieldName]();
    }
  }
  function createPackage(packageName) {
    var $package = window;
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
    var $constructor = new Function();
    $constructor.prototype =  $prototype;
    return $constructor;
  };
  function createDefaultConstructor(superName) {
    return function $DefaultConstructor() {
      this[superName].apply(this,arguments);
    };
  }
  function setFunctionName(theFunction, name) {
    theFunction.getName = function() { return name; };
  }
  function registerPrivateMember(privateStatic, classPrefix, memberName) {
    var privateMemberName = classPrefix+memberName;
    privateStatic["_"+memberName] = privateMemberName;
    return privateMemberName;
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
      getClassDescription: function getClassDescription(fullClassName) { with(this){
        var classDescription = classDescriptions[fullClassName];
        if (!classDescription)
          throw new Error("NoClassDefFound: "+fullClassName);
        return classDescription;
      }},
      prepareAll: function prepareAll() { with(this){
        for (var fullClassName in pendingClassDescriptions) {
          pendingClassDescriptions[fullClassName].prepare();
        }
      }}
    };
    // constructor:
    function ClassDescription(classDef) {
      for (var m in classDef) {
        this[m] = classDef[m];
      }
      with (this) {
        fullClassName = $package + "." + $class;
        ClassDescription$static.classDescriptions[fullClassName] = ClassDescription$static.pendingClassDescriptions[fullClassName] = this;
      }
    }
    with(ClassDescription$static) {
      // instance members:
      ClassDescription.prototype = {
        fullClassName: undefined,
        $extends: "jsc.lang.Object",
        level: undefined,
        state: PENDING,
        superClassDescription: undefined,
        $constructor: undefined,
        Public: undefined,
        Static: undefined,
        getStatic: undefined,
        /**
         * Prepares this class to be used by constructor, by accessing a static member, or as a super class.
         * The actual class loading is done when any of this three methods is called.
         * Called on load by prepareAll().
         */
        prepare: function() { with(this){
          if (state===PREPARING)
            throw new Error("cyclic usages between classes "+fullClassName+" and "+superClassDescription.fullClassName+".");
          if (state!==PENDING)
            return;
          state = PREPARING;
          // Only do the minimal setup to allow a preliminary, initializing public constructor and static getter,
          // and to allow subclasses to plug their constructor into this class.
          delete pendingClassDescriptions[fullClassName];
          superClassDescription = getClassDescription($extends);
          superClassDescription.prepare();
          // now, the superClassDescription contains the neccessary prototypes!
          // create preliminary constructor and static getter that initialize before delegating to the real ones:
          level = superClassDescription.level + 1;
          $constructor = function() {
            initialize();
            $constructor.apply(this,arguments);
          };
          setFunctionName($constructor, fullClassName);
          $constructor.prototype = new (superClassDescription.Public)();
          // TODO: only if not final:
          Public = createEmptyConstructor($constructor.prototype);
          // static part:
          Static = createEmptyConstructor(new (superClassDescription.Static)());
          getStatic = function() {
            initialize();
            return getStatic();
          }
          // TODO: only if public:
          $package = createPackage($package);
          $package[$class] = $constructor;
          $package[$class+"_"] = getStatic;

          state = PREPARED;
        }},
        /**
         * Initializes this class by finishing the class setup and then invoking all static initializers.
         */
        initialize: function() { with(this) {
          if (state!==PREPARED)
            return;
          state = INITIALIZING;
          // finish object structure setup of this class:
          // public part: avoid recursion!
          $package[$class+"_"] = getStatic = undefined;
          $package[$class] = $constructor = undefined;

          // private part of the object structure:
          var classPrefix = level; // + "$";
          var superName = classPrefix+"super";
          var fieldsWithInitializer = [];
          Public.prototype[superName] = function $super() {
            this[superName] = undefined; // only allow to call $super once!
            superClassDescription.$constructor.apply(this,arguments);
            initFields(null, this, fieldsWithInitializer);
          };
          // static part:
          $package[$class+"_"] = getStatic = function() {
            // overwrite, this time without initializing:
            return Static.prototype;
          };
          var privateStatic = new Static();
          privateStatic._super = superName;

          // init super class:
          superClassDescription.initialize();

          // evaluate $members, transfer members into the prepared objects:

          // Define a mapping to efficiently find the right prototype object to store a member,
          // depending on its modifiers.
          // Note: As long as "protected" is not implemented, treat it like "public".
          var targetMap = {
            $this: {
              fieldsWithInitializer: fieldsWithInitializer,
              $public: Public.prototype,
              $protected: Public.prototype,
              $private: Public.prototype
            },
            $static: {
              fieldsWithInitializer: [],
              $public: Static.prototype,
              $protected: Static.prototype,
              $private: privateStatic
            }
          };
          if (isFunction($members)) {
            var memberDeclarations = $members(privateStatic);
            var i=0;
            while (i<memberDeclarations.length) {
              var memberKey = "$this"; // default: not static
              var visibility = "$public"; // default: public visibility
              var members = memberDeclarations[i++];
              var modifiers = [];
              if (typeof members=="string") {
                modifiers = members.split(" ");
                for (var j=0; j<modifiers.length; ++j) {
                  var modifier = modifiers[j];
                  if (modifier=="static") {
                    memberKey = "$static";
                  } else if (modifier=="private" || modifier=="public" || modifier=="protected") {
                    visibility = "$"+modifier;
                  } else {
                    throw new Error("Unknown modifier '"+modifier+"'.");
                  }
                }
                if (i>=memberDeclarations.length) {
                  throw new Error("Member expected after modifiers "+modifiers.join(" "));
                }
                members = memberDeclarations[i++];
              }
              var target = targetMap[memberKey][visibility];
              //document.writeln("defining "+modifiers.join(" ")+" member(s):");
              if (isFunction(members)) {
                var memberName = members.getName();
                if (memberName==$class) {
                  $constructor = members;
                } else if (memberKey=="$this") {
                  if (visibility=="$private") {
                    memberName = registerPrivateMember(privateStatic, classPrefix, memberName);
                    setFunctionName(members, memberName);
                  } else if (isFunction(target[memberName])) {
                    // Found overriding! Store super method as private method delegate for super access:
                    Public.prototype[registerPrivateMember(privateStatic, classPrefix, memberName)] = target[memberName];
                  }
                }
                target[memberName] = members;
              } else {
                var targetFieldsWithInitializer = targetMap[memberKey].fieldsWithInitializer;
                for (var memberName in members) {
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
          }
          if (!$constructor) {
            $constructor = createDefaultConstructor(superName);
          }
          setFunctionName($constructor, fullClassName);
          $constructor.prototype = Public.prototype;
          Public.prototype.getClass = function Object$getClass() { return $constructor; };
          // TODO: constructor visibility!
          $package[$class] = $constructor;
          // init static fields with initializer:
          initFields(privateStatic, Static.prototype, targetMap.$static.fieldsWithInitializer);
        }}
      };
      classDescriptions["jsc.lang.Object"] = {
        $package: createPackage("jsc.lang"),
        $class: "Object",
        fullClassName: "jsc.lang.Object",
        $extends: undefined,
        level: 0,
        state: INITIALIZED,
        superClassDescription: undefined,
        $constructor: (function() {
          var hashCodeCnt = 0;
          return function Object$constructor() {
            var hashCode = hashCodeCnt++;
            this.hashCode = function() { return hashCode; };
          };
        })(),
        Public: new Function(),
        Static: new Function(),
        getStatic: new Function(),
        prepare: new Function(),
        initialize: new Function()
      };
    }
    ClassDescription.$static = ClassDescription$static;
    return ClassDescription;
  })();
  function Package() { }
  window.jsc = new Package();
  window.jsc.Class = {};
  window.jsc.Class.prepare = function(packageDef, classDef, members) {
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
  // TODO: use onDomReady!
  window.jsc.ClassLoader = ClassDescription.$static;
})();
//  alert("runtime loaded!");
