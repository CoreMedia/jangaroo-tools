/*
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

package joo {

public class JooClassDeclaration extends NativeClassDeclaration {

  public static const STATE_EVENT_AFTER_INIT_MEMBERS:String = 'afterInitMembers';
  private static var STATE_BY_EVENT:Object;
  STATE_BY_EVENT = {
    'afterInitMembers': STATE_MEMBERS_INITIALIZED
  };

  internal var
          package_ : Object,
          type : String = MemberDeclaration.MEMBER_TYPE_CLASS,
          namespace_ : String = MemberDeclaration.NAMESPACE_INTERNAL,
          className : String,
          native_ : Boolean = false,
          extends_ : String = "Object",
          level : int = -1,
          privateStatics : Object,
          memberDeclarations : * /* Function, then Array */,
          memberDeclarationsByQualifiedName : Object,
          staticInitializers : Array/*<MemberDeclaration|Function>*/,
          publicStaticMethodNames : Array,
          implementingClasses: Array/*Class*/,
          dependencies : Array,
          stateListeners: Object;
  /**
   * The metadata (annotations) associated with this class.
   */
  public var metadata : Object;

  private static const DECLARATION_PATTERN_CLASS:RegExp =
    /^\s*((public|internal|final|dynamic)\s+)*class\s+([a-zA-Z$_0-9]+)(\s+extends\s+([a-zA-Z$_0-9.]+))?(\s+implements\s+([a-zA-Z$_0-9.,\s]+))?\s*$/;
  private static const DECLARATION_PATTERN_INTERFACE:RegExp =
    /^\s*((public|internal)\s+)?interface\s+([a-zA-Z$_0-9]+)(\s+extends\s+([a-zA-Z$_0-9.,\s]+))?\s*$/;
  private static const DECLARATION_PATTERN_OTHER:RegExp =
    /^\s*((public|internal)\s+)?(const|var|function|namespace)\s+([a-zA-Z$_0-9]+)\s*$/;

  public function JooClassDeclaration(packageDef:String, metadata:Object, classDef:String, inheritanceLevel:int, memberDeclarations:Function, publicStaticMethodNames:Array, dependencies:Array) {
    this.stateListeners = {};
    this.metadata = metadata;
    var packageName : String = packageDef.split(/\s+/)[1] || "";
    this.package_ = getOrCreatePackage(packageName);
    var classMatch : Array = classDef.match(DECLARATION_PATTERN_CLASS);
    var interfaces : String;
    if (classMatch) {
      this.className = classMatch[3];
      if (classMatch[5]) {
        this.extends_ = classMatch[5];
      }
      interfaces = classMatch[7];
    } else {
      classMatch = classDef.match(DECLARATION_PATTERN_INTERFACE);
      if (classMatch) {
        this.className = classMatch[3];
        this.type = MemberDeclaration.MEMBER_TYPE_INTERFACE;
        interfaces = classMatch[5];
      } else {
        classMatch = classDef.match(DECLARATION_PATTERN_OTHER);
        if (classMatch) {
          this.className = classMatch[4];
          this.type = classMatch[3];
        }
      }
    }
    if (!classMatch) {
      throw new Error("SyntaxError: \""+classDef+"\" does not match.");
    }
    this.level = inheritanceLevel;
    this.namespace_ = classMatch[2];
    var fullClassName : String = this.className;
    if (packageName) {
      fullClassName = packageName+"."+this.className;
    }
    this.interfaces = interfaces ? interfaces.split(/\s*,\s*/) : [];
    this.memberDeclarations = memberDeclarations;
    this.publicStaticMethodNames = publicStaticMethodNames;
    this.dependencies = dependencies;
    this.privateStatics = {};
    this.publicConstructor = getQualifiedObject(fullClassName);
    if (publicConstructor) {
      this.native_ = true;
    } else if (isClass() || isInterface()) {
      this.package_[this.className] = this.publicConstructor = createInitializingConstructor(this);
      for (var i:int = 0; i < publicStaticMethodNames.length; i++) {
        createInitializingStaticMethod(publicStaticMethodNames[i]);
      }
    } else if (isFunction()) {
      this.package_[this.className] = createInitializingPackageMethod(this);
    } else if (isConst() || isVar()) {
      this.package_[this.className] = typeof this.memberDeclarations === "function" ?
        createInitializingPackageField(this) : this.memberDeclarations;
    }
    this.create(fullClassName, publicConstructor);
    this._processMetadata(); // for early annotation processing like adding dependencies
  }

  public function addStateListener(state:String, listener:Function):void {
    if (this.state >= STATE_BY_EVENT[state]) {
      // when already past this state, call back immediately:
      listener(this);
    } else {
      var stateListeners:Array = this.stateListeners[state];
      if (!stateListeners) {
        this.stateListeners[state] = stateListeners = [];
      }
      stateListeners.push(listener);
    }
  }

  public function removeStateListener(state:String, listener:Function):void {
    var stateListeners:Array = this.stateListeners[state];
    if (stateListeners) {
      var pos:int = stateListeners.indexOf(listener);
      if (pos !== -1) {
        stateListeners.splice(pos, 1);
      }
    }
  }

  public function isClass() : Boolean {
    return this.type === MemberDeclaration.MEMBER_TYPE_CLASS;
  }

  public function isInterface() : Boolean {
    return this.type === MemberDeclaration.MEMBER_TYPE_INTERFACE;
  }

  public function isFunction() : Boolean {
    return this.type === MemberDeclaration.MEMBER_TYPE_FUNCTION;
  }

  public function isConst() : Boolean {
    return this.type === MemberDeclaration.MEMBER_TYPE_CONST;
  }

  public function isVar() : Boolean {
    return this.type === MemberDeclaration.MEMBER_TYPE_VAR;
  }

  internal function addToInterfaces(clazz:Function):void {
    var scd:JooClassDeclaration = superClassDeclaration as JooClassDeclaration;
    if (scd) {
      scd.addToInterfaces(clazz);
    }
    for (var i:int = 0; i < interfaces.length; i++) {
      JooClassDeclaration(interfaces[i]).addImplementingClass(clazz);
    }
  }

  internal function addImplementingClass(clazz:Function):void {
    //trace("#### adding " + clazz + " to interface " + fullClassName + ":");
    var implementingClasses:Array = [];
    //trace("####   before: " + this.implementingClasses.join(", "));
    for (var i:int = 0; i < this.implementingClasses.length; i++) {
      var implementingClass:Function = this.implementingClasses[i];
      // do not add new clazz if it or a superclass is already in the set:
      if (clazz === implementingClass || clazz.prototype instanceof implementingClass) {
        //trace("####   " + implementingClass + " already present, nothing changed.");
        return; // class or superclass already added!
      }
      // remove all subclasses from the set (keep only non-subclasses):
      if (!(implementingClass.prototype instanceof clazz)) {
        implementingClasses.push(implementingClass);
      }
    }
    implementingClasses.push(clazz);
    this.implementingClasses = implementingClasses;
    //trace("####   after: " + this.implementingClasses.join(", "));
    addToInterfaces(clazz);
  }

  override public function isInstance(obj:Object):Boolean {
    return Public ? isInterface() ? implementingClasses.some(function(implementingClass:Function):Boolean {
      return obj instanceof implementingClass;
    }) : obj instanceof Public // cannot invoke super, since BootstrapClassLoader does not support super calls!
    : false; // class not even completed, cannot have instances!
  }

  public function isNamespace() : Boolean {
    return this.type === MemberDeclaration.MEMBER_TYPE_NAMESPACE;
  }

  public function isNative() : Boolean {
    return this.native_;
  }

  internal override function doComplete() : void {
    this.superClassDeclaration = classLoader.getRequiredClassDeclaration(this.extends_);
    this.superClassDeclaration.complete();
    var proto:Object = this.native_ ? publicConstructor.prototype : new (this.superClassDeclaration.Public)();
    this.Public = NativeClassDeclaration.createEmptyConstructor(proto);
  }

  internal function initMembers() : void {
    this.staticInitializers = [];
    var memberDeclarations:Array = this.memberDeclarations(this.privateStatics);
    this.memberDeclarations = [];
    this.memberDeclarationsByQualifiedName = {};
    this.constructor_ = isNative() ? publicConstructor : null;
    var metadata:Object = {};
    for (var i:int = 0; i < memberDeclarations.length; ++i) {
      var item:* = memberDeclarations[i];
      switch (typeof item) {
        case "function":
          this.staticInitializers.push(item);
          break;
        case "string":
          var memberDeclaration:MemberDeclaration = MemberDeclaration.create(item);
          if (memberDeclaration) {
            memberDeclaration.metadata = metadata;
            metadata = {};
            if (!memberDeclaration.isNative()) {
              if (++i >= memberDeclarations.length) {
                throw new Error(this + ": Member expected after modifiers '" + item + "'.");
              }
              var member:* = memberDeclarations[i];
            }
            switch (memberDeclaration.memberType) {
              case MemberDeclaration.MEMBER_TYPE_FUNCTION:
                this.initMethod(memberDeclaration, Function(member));
                break;
              case MemberDeclaration.MEMBER_TYPE_CLASS:
                //noinspection UnnecessaryLocalVariableJS
                var helperInheritanceLevel:int = member;
                var helperMemberDeclarations:Function = memberDeclarations[++i];
                var helperStatics:Array = memberDeclarations[++i];
                var secondaryClass:NativeClassDeclaration = classLoader.prepare("package " + this.fullClassName, item,
                  helperInheritanceLevel, helperMemberDeclarations,
                  helperStatics, [], runtimeApiVersion, compilerVersion).complete();

                // revert that the class has already set itself as a static member, because it considers the primary class its package.
                // otherwise, the static initializer that inits the secondary class may not be set by _storeMember()!
                delete getQualifiedObject(fullClassName)[memberDeclaration.memberName];

                memberDeclaration._static = true;
                memberDeclaration.initSlot(level);
                this._storeMember(memberDeclaration, createSecondaryClassInitializer(secondaryClass.publicConstructor));
                break;
              default:
                for (var memberName:String in member) {
                  this._storeMember(this._createMemberDeclaration(memberDeclaration, {memberName: memberName}), member[memberName]);
                }
            }
          }
          break;
        case "object":
          SystemClassLoader.addToMetadata(metadata, item);
      }
    }
    if (!isInterface() && !native_) {
      if (!superClassDeclaration.constructor_) {
        throw new Error("Class " + fullClassName + " extends " + superClassDeclaration.fullClassName + " whose constructor is not defined!");
      }
      // only add "super$..." for backwards compatibility, and never if we are a JavaScriptObject:
      if (!(Public.prototype instanceof JavaScriptObject)) {
        Public.prototype["super$" + level] = superClassDeclaration.constructor_;
      }
      if (!this.constructor_) {
        // no explicit constructor found
        // generate constructor invoking super() and initialize it from the "collecting" constructor:
        _setConstructor(createSuperConstructor(this));
      }
    }
  }

  private static function createSecondaryClassInitializer(secondaryClass:Function):Function {
    return function():Function {
      // init secondary class together with primary class:
      return classLoader.init(secondaryClass);
    };
  }

  //noinspection JSFieldCanBeLocalInspection
  private static var jooClasstoString:Function;
  jooClasstoString = function():String {
    return "[class " + this.$class.className + "]";
  };

  internal function _setConstructor(constructor_:Function):void {
    // replay all non-private static members collected so far for new constructor_ function:
    for (var i:int = 0; i < memberDeclarations.length; i++) {
      var memberDeclaration:MemberDeclaration = memberDeclarations[i];
      if (memberDeclaration.isStatic() && !memberDeclaration.isPrivate()) {
        memberDeclaration.storeMember(constructor_);
      }
    }
    constructor_['$class'] = this;
    if (superClassDeclaration) {
      constructor_['superclass'] = superClassDeclaration.Public.prototype; // Ext Core compatibility!
    }
    constructor_.prototype = Public.prototype;
    Object.defineProperty(Public.prototype, 'constructor', {
      value: constructor_,
      writable: true,
      configurable: true
    });
    constructor_.toString = jooClasstoString;
    // replace initializing constructor by the real one:
    package_[className] = this.constructor_ = constructor_;
  }

  private static function createSuperConstructor(classDeclaration:JooClassDeclaration):Function {
    return function generatedConstructor$():void {
      classDeclaration.constructor_["superclass"].constructor.call(this);
    };
  }

  internal function initMethod(memberDeclaration : MemberDeclaration, member : Function) : void {
    if (memberDeclaration.memberName == this.className && !memberDeclaration.isStatic()) {
      if (memberDeclaration.getterOrSetter) {
        throw new Error(this+": Class name cannot be used for getter or setter: "+memberDeclaration);
      }
      if (!native_ && !memberDeclaration.isNative()) {
        _setConstructor(member);
      }
    } else {
      memberDeclaration.initSlot(this.level);
      if (memberDeclaration.isNative()) {
        member = memberDeclaration.getNativeMember(this.publicConstructor);
      }
      if (memberDeclaration.isMethod()) {
        if (this.extends_!="Object") {
          var superMethod : Function = memberDeclaration.retrieveMember(superClassDeclaration.Public.prototype);
        }
        var overrides : Boolean = !!superMethod
          && superMethod!==member
          && superMethod!==Object['prototype'][memberDeclaration.memberName];
        if (overrides !== memberDeclaration.isOverride()) {
          var msg : String = overrides
                  ? "Method overrides without 'override' modifier"
                  : "Method with 'override' modifier does not override";
          trace("[WARN]", this+": "+msg+": "+memberDeclaration);
        } else if (overrides) {
          // found overriding: store super class' method as private member:
          this._storeMember(this._createMemberDeclaration(memberDeclaration, {_namespace: MemberDeclaration.NAMESPACE_PRIVATE}), superMethod);
        }
      }
      this._storeMember(memberDeclaration, member);
    }
  }

  internal function _createMemberDeclaration(memberDeclaration : MemberDeclaration, changedProperties : Object) : MemberDeclaration {
    var newMemberDeclaration : MemberDeclaration = memberDeclaration.clone(changedProperties);
    newMemberDeclaration.initSlot(this.level);
    return newMemberDeclaration;
  }

  internal function _storeMember(memberDeclaration : MemberDeclaration, value : Object) : void {
    this.memberDeclarations.push(memberDeclaration);
    this.memberDeclarationsByQualifiedName[memberDeclaration.getQualifiedName()] = memberDeclaration;
    memberDeclaration.value = value;
    var _static : Boolean = memberDeclaration.isStatic();
    var _private : Boolean = memberDeclaration.isPrivate();

    if (_static && memberDeclaration.hasInitializer()) {
      this.staticInitializers.push(memberDeclaration);
    }
    _processMetadata(memberDeclaration);
    var target : Object = _static ? _private ? privateStatics : constructor_ : Public.prototype;

    if (target) {
      memberDeclaration.storeMember(target);
    }
    // if constructor_ is not yet set, static non-private members will be added later by _setConstructor().
  }

  internal function _processMetadata(memberDeclaration : MemberDeclaration = null):void {
    var metaPackage:* = getQualifiedObject("joo.meta");
    if (metaPackage) {
      var metadata:Object = memberDeclaration ? memberDeclaration.metadata : this.metadata;
      if (metadata) {
        for (var metaFunctionName:String in metadata) {
          if (metaFunctionName in metaPackage) {
            metaPackage[metaFunctionName](this, memberDeclaration, metadata[metaFunctionName]);
          }
        }
      }
    }
  }

  internal override function doInit() : void {
    if (!isClass() && !isInterface()) {
      return;
    }
    this.superClassDeclaration.init();
    for (var j:int = 0; j < interfaces.length; j++) {
      interfaces[j] = classLoader.getRequiredClassDeclaration(interfaces[j]).init();
    }
    this.initMembers();
    if (isInterface()) {
      implementingClasses = [];
    } else {
      addToInterfaces(constructor_);
    }
    this.state = STATE_MEMBERS_INITIALIZED;
    fireStateEvent(STATE_EVENT_AFTER_INIT_MEMBERS);
    for (var i:int=0; i<this.staticInitializers.length; ++i) {
      var staticInitializer : * = this.staticInitializers[i];
      if (typeof staticInitializer=="function") {
        // static statements
        staticInitializer();
      } else {
        //noinspection UnnecessaryLocalVariableJS
        var memberDeclaration:MemberDeclaration = staticInitializer;
        // static variable initializer expression
        var target : Object = memberDeclaration.isPrivate() ? this.privateStatics : this.constructor_;
        target[memberDeclaration.slot] = target[memberDeclaration.slot]();
      }
    }
  }

  internal function fireStateEvent(event:String):void {
    var stateListeners:Array = this.stateListeners[event];
    if (stateListeners) {
      for (var i:int = 0; i < stateListeners.length; i++) {
        stateListeners[i](this);
      }
      delete this.stateListeners[event];
    }
  }

  public function getMemberDeclaration(namespace_ : String, memberName : String) : MemberDeclaration {
    var memberDeclaration:MemberDeclaration = this.memberDeclarationsByQualifiedName[namespace_ + "::" + memberName];
    return !memberDeclaration && this.superClassDeclaration && this.superClassDeclaration["getMemberDeclaration"]
      ? JooClassDeclaration(this.superClassDeclaration).getMemberDeclaration(namespace_, memberName)
      : memberDeclaration;

  }

  public function getDependencies() : Array {
    return this.dependencies;
  }

  private static function createInitializingConstructor(classDeclaration : JooClassDeclaration) : Function {
    return function() : Object {
      classDeclaration.init();
      // create an uninitialized Object with the correct prototype chain:
      var instance:Object = new classDeclaration.Public();
      // now, apply the constructor to that Object.
      // classDeclaration.constructor_ must have been set, at least to a default constructor:
      classDeclaration.constructor_.apply(instance, arguments);
      return instance;
    };
  }

  private static function createInitializingPackageMethod(classDeclaration : JooClassDeclaration) : Function {
    return function() : * {
        var fun:Function = classDeclaration.package_[classDeclaration.className] = classDeclaration.memberDeclarations();
        return fun.apply(null, arguments);
    };
  }

  private static function createInitializingPackageField(classDeclaration:JooClassDeclaration):Object {
    return {
      $class: {
        init: function():* {
          var value:* = classDeclaration.package_[classDeclaration.className] = classDeclaration.memberDeclarations();
          return {
            constructor_: value
          };
        }
      }
    };
  }

  internal function createInitializingStaticMethod(methodName : String) : void {
    this.publicConstructor[methodName] = function() : * {
      init();
      return constructor_[methodName].apply(null, arguments);
    };
  }
}
}
