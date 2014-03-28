joo.classLoader.prepare(/*
 * Copyright 2009 CoreMedia AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 */

// JangarooScript runtime support. Author: Frank Wienberg

"package joo",/* {*/

"public class JooClassDeclaration extends joo.NativeClassDeclaration",2,function($$private){;return[function(){joo.classLoader.init(joo.MemberDeclaration,Object);}, 

  "protected var",{
          package_/* : Object*/:null,
          type/* : String*/ :function(){return( joo.MemberDeclaration.MEMBER_TYPE_CLASS);},
          namespace_/* : String*/ :function(){return( joo.MemberDeclaration.NAMESPACE_INTERNAL);},
          className/* : String*/:null,
          native_/* : Boolean*/ : false,
          extends_/* : String*/ : "Object",
          level/* : int*/ : -1,
          privateStatics/* : Object*/:null,
          memberDeclarations/* : **/:undefined /* Function, then Array */,
          memberDeclarationsByQualifiedName/* : Object*/:null,
          staticInitializers/* : Array*/:null/*<MemberDeclaration>*/,
          publicStaticMethodNames/* : Array*/:null,
          dependencies/* : Array*/:null},
  /**
   * The metadata (annotations) associated with this class.
   */
  "public var",{ metadata/* : Object*/ :function(){return( {});}},

  "private static const",{ DECLARATION_PATTERN_CLASS/*:RegExp*/ :
    /^\s*((public|internal|final|dynamic)\s+)*class\s+([A-Za-z][a-zA-Z$_0-9]*)(\s+extends\s+([a-zA-Z$_0-9.]+))?(\s+implements\s+([a-zA-Z$_0-9.,\s]+))?\s*$/},
  "private static const",{ DECLARATION_PATTERN_INTERFACE/*:RegExp*/ :
    /^\s*((public|internal)\s+)?interface\s+([A-Za-z][a-zA-Z$_0-9]*)(\s+extends\s+([a-zA-Z$_0-9.,\s]+))?\s*$/},
  "private static const",{ DECLARATION_PATTERN_NAMESPACE/*:RegExp*/ :
    /^\s*((public|internal)\s+)?namespace\s+([A-Za-z][a-zA-Z$_0-9]*)\s*$/},

  "public function JooClassDeclaration",function JooClassDeclaration$(packageDef/* : String*/, classDef/* : String*/, inheritanceLevel/* : int*/, memberDeclarations/* : Function*/,
          publicStaticMethodNames/* : Array*/, dependencies/* : Array*/) {this.super$2();this.namespace_=this.namespace_();this.type=this.type();this.metadata=this.metadata();
    var packageName/* : String*/ = packageDef.split(/\s+/)[1] || "";
    this.package_ = joo.getOrCreatePackage(packageName);
    var classMatch/* : Array*/ = classDef.match($$private.DECLARATION_PATTERN_CLASS);
    var interfaces/* : String*/;
    if (classMatch) {
      if (classMatch[5]) {
        this.extends_ = classMatch[5];
      }
      interfaces = classMatch[7];
    } else {
      classMatch = classDef.match($$private.DECLARATION_PATTERN_INTERFACE);
      if (classMatch) {
        this.type = joo.MemberDeclaration.MEMBER_TYPE_INTERFACE;
        interfaces = classMatch[5];
      } else {
        classMatch = classDef.match($$private.DECLARATION_PATTERN_NAMESPACE);
        if (classMatch) {
          this.type = joo.MemberDeclaration.MEMBER_TYPE_NAMESPACE;
        }
      }
    }
    if (!classMatch) {
      throw new Error("SyntaxError: \""+classDef+"\" does not match.");
    }
    this.level = inheritanceLevel;
    this.namespace_ = classMatch[2];
    this.className    = classMatch[3];
    var fullClassName/* : String*/ = this.className;
    if (packageName) {
      fullClassName = packageName+"."+this.className;
    }
    this.interfaces = interfaces ? interfaces.split(/\s*,\s*/) : [];
    this.memberDeclarations = memberDeclarations;
    this.publicStaticMethodNames = publicStaticMethodNames;
    this.dependencies = dependencies;
    this.privateStatics = {};
    this.publicConstructor = joo.getQualifiedObject(fullClassName);
    if (this.publicConstructor) {
      this.native_ = true;
    } else {
      this.package_[this.className] = this.publicConstructor = $$private.createInitializingConstructor(this);
      for (var i/*:int*/ = 0; i < publicStaticMethodNames.length; i++) {
        this.createInitializingStaticMethod(publicStaticMethodNames[i]);
      }
    }
    this.create(fullClassName, this.publicConstructor);
  },

  "public function isClass",function isClass()/* : Boolean*/ {
    return this.type === joo.MemberDeclaration.MEMBER_TYPE_CLASS;
  },

  "public function isInterface",function isInterface()/* : Boolean*/ {
    return this.type === joo.MemberDeclaration.MEMBER_TYPE_INTERFACE;
  },

  "public function isNamespace",function isNamespace()/* : Boolean*/ {
    return this.type === joo.MemberDeclaration.MEMBER_TYPE_NAMESPACE;
  },

  "public function isNative",function isNative()/* : Boolean*/ {
    return this.native_;
  },

  "internal override function doComplete",function doComplete()/* : void*/ {
    this.superClassDeclaration = joo.classLoader.getRequiredClassDeclaration(this.extends_);
    this.superClassDeclaration.complete();
    var Super/* : Function*/ = this.superClassDeclaration.Public;
    if (!this.native_) {
      this.publicConstructor.prototype = new Super();
      this.publicConstructor.prototype['constructor'] = this.publicConstructor;
      this.publicConstructor["superclass"] = Super.prototype; // Ext Core compatibility!
    }
    this.Public = joo.NativeClassDeclaration.createEmptyConstructor(this.publicConstructor.prototype);
    this.initTypes();
  },

  "internal function initMembers",function initMembers()/* : void*/ {
    this.staticInitializers = [];
    var memberDeclarations/*:Array*/ = this.memberDeclarations(this.privateStatics);
    this.memberDeclarations = [];
    this.memberDeclarationsByQualifiedName = {};
    this.constructor_ = this.isNative() ? this.publicConstructor : null;
    var metadata/*:Object*/ = {};
    for (var i/*:int*/ = 0; i < memberDeclarations.length; ++i) {
      var item/*:**/ = memberDeclarations[i];
      switch (typeof item) {
        case "function":
          this.staticInitializers.push(item);
          break;
        case "string":
          var memberDeclaration/*:MemberDeclaration*/ = joo.MemberDeclaration.create(item);
          if (memberDeclaration) {
            memberDeclaration.metadata = metadata;
            metadata = {};
            if (!memberDeclaration.isNative()) {
              if (++i >= memberDeclarations.length) {
                throw new Error(this + ": Member expected after modifiers '" + item + "'.");
              }
              var member/*:**/ = memberDeclarations[i];
            }
            switch (memberDeclaration.memberType) {
              case joo.MemberDeclaration.MEMBER_TYPE_FUNCTION:
                this.initMethod(memberDeclaration,/* Function*/(member));
                break;
              case joo.MemberDeclaration.MEMBER_TYPE_CLASS:
                //noinspection UnnecessaryLocalVariableJS
                var helperInheritanceLevel/*:int*/ = member;
                var helperMemberDeclarations/*:Function*/ = memberDeclarations[++i];
                var helperStatics/*:Array*/ = memberDeclarations[++i];
                var secondaryClass/*:NativeClassDeclaration*/ = joo.classLoader.prepare("package " + this.fullClassName, item,
                  helperInheritanceLevel, helperMemberDeclarations,
                  helperStatics, [], joo.runtimeApiVersion, joo.compilerVersion).complete();
                memberDeclaration._static = true;
                memberDeclaration.initSlot(this.level);
                this._storeMember(memberDeclaration, secondaryClass.publicConstructor);
                break;
              default:
                for (var memberName/*:String*/ in member) {
                  this._storeMember(this._createMemberDeclaration(memberDeclaration, {memberName: memberName}), member[memberName]);
                }
            }
          }
          break;
        case "object":
          joo.SystemClassLoader.addToMetadata(metadata, item);
      }
    }
    if (!this.isInterface() && !this.native_) {
      if (!this.superClassDeclaration.constructor_) {
        throw new Error("Class " + this.fullClassName + " extends " + this.superClassDeclaration.fullClassName + " whose constructor is not defined!");
      }
      this.Public.prototype["super$" + this.level] = this.superClassDeclaration.constructor_;
      if (!this.constructor_) {
        // no explicit constructor found
        // generate constructor invoking super() and initialize it from the "collecting" constructor:
        this._setConstructor($$private.createSuperConstructor(this.level));
      }
    }
  },

  "internal function _setConstructor",function _setConstructor(constructor_/*:Function*/)/*:void*/ {
    // replay all non-private static members collected so far for new constructor_ function:
    for (var i/*:int*/ = 0; i < this.memberDeclarations.length; i++) {
      var memberDeclaration/*:MemberDeclaration*/ = this.memberDeclarations[i];
      if (memberDeclaration.isStatic() && !memberDeclaration.isPrivate()) {
        memberDeclaration.storeMember(constructor_);
      }
    }
    constructor_['$class'] = this;
    if (this.superClassDeclaration) {
      constructor_['superclass'] = this.superClassDeclaration.Public.prototype; // Ext Core compatibility!
    }
    constructor_.prototype = this.Public.prototype;
    constructor_.prototype['constructor'] = constructor_;
    // replace initializing constructor by the real one:
    this.package_[this.className] = this.constructor_ = constructor_;
  },

  "private static function createSuperConstructor",function createSuperConstructor(level/*:int*/)/*:Function*/ {
    return function generatedConstructor$()/*:void*/ {
      this['super$' + level]();
    };
  },

  "internal function initMethod",function initMethod(memberDeclaration/* : MemberDeclaration*/, member/* : Function*/)/* : void*/ {
    if (memberDeclaration.memberName == this.className && !memberDeclaration.isStatic()) {
      if (memberDeclaration.getterOrSetter) {
        throw new Error(this+": Class name cannot be used for getter or setter: "+memberDeclaration);
      }
      if (!this.native_ && !memberDeclaration.isNative()) {
        this._setConstructor(member);
      }
    } else {
      memberDeclaration.initSlot(this.level);
      if (memberDeclaration.isNative()) {
        member = memberDeclaration.getNativeMember(this.publicConstructor);
      }
      if (memberDeclaration.isMethod()) {
        if (this.extends_!="Object") {
          var superMethod/* : Function*/ = memberDeclaration.retrieveMember(this.superClassDeclaration.Public.prototype);
        }
        var overrides/* : Boolean*/ = ! !superMethod
          && superMethod!==member
          && superMethod!==Object['prototype'][memberDeclaration.memberName];
        if (overrides !== memberDeclaration.isOverride()) {
          var msg/* : String*/ = overrides
                  ? "Method overrides without 'override' modifier"
                  : "Method with 'override' modifier does not override";
          throw new Error(this+": "+msg+": "+memberDeclaration);
        }
        if (overrides) {
          // found overriding: store super class' method as private member:
          this._storeMember(this._createMemberDeclaration(memberDeclaration, {_namespace: joo.MemberDeclaration.NAMESPACE_PRIVATE}), superMethod);
        }
      }
      this._storeMember(memberDeclaration, member);
    }
  },

  "internal function _createMemberDeclaration",function _createMemberDeclaration(memberDeclaration/* : MemberDeclaration*/, changedProperties/* : Object*/)/* : MemberDeclaration*/ {
    var newMemberDeclaration/* : MemberDeclaration*/ = memberDeclaration.clone(changedProperties);
    newMemberDeclaration.initSlot(this.level);
    return newMemberDeclaration;
  },

  "internal function _storeMember",function _storeMember(memberDeclaration/* : MemberDeclaration*/, value/* : Object*/)/* : void*/ {
    this.memberDeclarations.push(memberDeclaration);
    this.memberDeclarationsByQualifiedName[memberDeclaration.getQualifiedName()] = memberDeclaration;
    memberDeclaration.value = value;
    var _static/* : Boolean*/ = memberDeclaration.isStatic();
    var _private/* : Boolean*/ = memberDeclaration.isPrivate();

    if (_static && memberDeclaration.hasInitializer()) {
      this.staticInitializers.push(memberDeclaration);
    }
    this._processMetadata(memberDeclaration);
    var target/* : Object*/ = _static ? _private ? this.privateStatics : this.constructor_ : this.Public.prototype;

    if (target) {
      memberDeclaration.storeMember(target);
    }
    // if constructor_ is not yet set, static non-private members will be added later by _setConstructor().
  },

  "internal function _processMetadata",function _processMetadata(memberDeclaration/* : MemberDeclaration*/)/*:void*/ {
    var metadata/*:Object*/ = memberDeclaration.metadata;
    if (metadata) {
      for (var metaFunctionName/*:String*/ in metadata) {
        var metaFunction/*:Function*/ = joo.getQualifiedObject("joo.meta." + metaFunctionName);
        if (metaFunction) {
          metaFunction(this, memberDeclaration, metadata[metaFunctionName]);
        }
      }
    }
  },

  "internal override function doInit",function doInit()/* : void*/ {
    this.superClassDeclaration.init();
    for (var j/*:int*/ = 0; j < this.interfaces.length; j++) {
      this.interfaces[j] = joo.classLoader.getRequiredClassDeclaration(this.interfaces[j]).init();
    }
    this.initMembers();
    for (var i/*:int*/ =0; i<this.staticInitializers.length; ++i) {
      var staticInitializer/* : **/ = this.staticInitializers[i];
      if (typeof staticInitializer=="function") {
        staticInitializer();
      } else {
        var target/* : Object*/ = staticInitializer.isPrivate() ? this.privateStatics : this.constructor_;
        target[staticInitializer.slot] = target[staticInitializer.slot]();
      }
    }
  },

  "public function getMemberDeclaration",function getMemberDeclaration(namespace_/* : String*/, memberName/* : String*/)/* : MemberDeclaration*/ {
    var memberDeclaration/*:MemberDeclaration*/ = this.memberDeclarationsByQualifiedName[namespace_ + "::" + memberName];
    return !memberDeclaration && this.superClassDeclaration && this.superClassDeclaration["getMemberDeclaration"]
      ?/* joo.JooClassDeclaration*/(this.superClassDeclaration).getMemberDeclaration(namespace_, memberName)
      : memberDeclaration;

  },

  "public function getDependencies",function getDependencies()/* : Array*/ {
    return this.dependencies;
  },

  "private static function createInitializingConstructor",function createInitializingConstructor(classDeclaration/* : JooClassDeclaration*/)/* : Function*/ {
    // anonymous function has to be inside a static function, or jooc will replace "this" with "this$":
    return function joo$JooClassDeclaration$318_12()/* : void*/ {
      classDeclaration.init();
      // classDeclaration.constructor_ must have been set, at least to a default constructor:
      classDeclaration.constructor_.apply(this, arguments);
    };
  },

  "internal function createInitializingStaticMethod",function createInitializingStaticMethod(methodName/* : String*/)/* : void*/ {var this$=this;
    this.publicConstructor[methodName] = function joo$JooClassDeclaration$326_42()/* : **/ {
      this$.init();
      return this$.constructor_[methodName].apply(null, arguments);
    };
  },
];},[],["joo.NativeClassDeclaration","joo.MemberDeclaration","Error","Function","joo.SystemClassLoader","Object"], "0.8.0", "0.8.2-SNAPSHOT"
);