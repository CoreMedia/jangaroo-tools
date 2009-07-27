package joo {

import joo.*;

public class SystemClassDeclaration extends NativeClassDeclaration {

  internal static function createDefaultConstructor(superName : String) : Function {
    return (function $DefaultConstructor() : void {
      this[superName].apply(this,arguments);
    });
  }

  internal static function createPublicConstructor(cd : NativeClassDeclaration) : void {
    return function joo$SystemClassDeclaration$constructor() : void {
      this.constructor =  cd.publicConstructor;
      cd.constructor_.apply(this, arguments);
    };
  }

  private static function is_(object : Object, type : Function) : Boolean {
    if (!type || object===undefined || object===null) {
      return false;
    }
    if (type["$class"]) {
      return (type["$class"] as NativeClassDeclaration).isInstance(object);
    }
    // fallback:
    return object instanceof type || object.constructor===type;
  }

{
  joo["is"] = is_;
}

  internal var
          package_ : Object,
          isInterface : Boolean = false,
          namespace_ : String = "intern",
          className : String,
          native_ : Boolean = false,
          extends_ : String = "Object",
          privateStatics : Object,
          memberDeclarations : * /* Function, then Array */,
          memberDeclarationsByQualifiedName : Object,
          initializerNames : Array/*<String>*/, // names of slots that contain initializer functions
          staticInitializers : Array/*<MemberDeclaration>*/,
          boundMethodNames : Array/*<String>*/,  // names of slots that contain methods that need to be bound to "this"
          publicStaticMethodNames : Array;

  public function SystemClassDeclaration(packageDef : String, directives : Array, classDef : String, memberDeclarations : Function,
          publicStaticMethodNames : Array) {
    var packageName : String = packageDef.split(/\s+/ as String)[1] || "";
    this.parseDirectives(packageName, directives);
    var classMatch : Array = classDef.match(/^\s*((public|internal|final|dynamic)\s+)*class\s+([A-Za-z][a-zA-Z$_0-9]*)(\s+extends\s+([a-zA-Z$_0-9.]+))?(\s+implements\s+([a-zA-Z$_0-9.,\s]+))?\s*$/) as Array;
    var interfaces : String;
    if (classMatch) {
      if (classMatch[5]) {
        this.extends_ = classMatch[5];
      }
      interfaces = classMatch[7];
    } else {
      classMatch = classDef.match(/^\s*((public|internal)\s+)?interface\s+([A-Za-z][a-zA-Z$_0-9]*)(\s+extends\s+([a-zA-Z$_0-9.,\s]+))?\s*$/) as Array;
      this.isInterface = true;
      interfaces = classMatch[5];
    }
    if (!classMatch) {
      throw new Error("SyntaxError: \""+classDef+"\" does not match.");
    }
    this.namespace_ = classMatch[2];
    this.className    = classMatch[3];
    var fullClassName : String = this.className;
    if (packageName) {
      fullClassName = packageName+"."+this.className;
    }
    this.interfaces = interfaces ? interfaces.split(/\s*,\s*/ as String) as Array : [];
    this.memberDeclarations = memberDeclarations;
    this.publicStaticMethodNames = publicStaticMethodNames;
    var publicConstructor : Class = joo.getQualifiedObject(fullClassName);
    if (publicConstructor && String(publicConstructor).indexOf("[JavaPackage")!=0) {
      this.package_ = joo.getQualifiedObject(packageName);
      this.native_ = true;
    } else {
      this.package_ = joo.getOrCreatePackage(packageName);
      publicConstructor = createPublicConstructor(this);
      this.package_[this.className] = publicConstructor;
    }
    super(fullClassName, publicConstructor);
    this.privateStatics = { "Class": Class, "assert": joo["assert"], "is": is_, "trace": trace };
  }

  //noinspection JSUnusedLocalSymbols
  internal function parseDirectives(packageName : String, directives : Array) : void { }

  internal override function doComplete() : void {
    this.superClassDeclaration = joo.classLoader.getRequiredClassDeclaration(this.extends_);
    this.superClassDeclaration.complete();
    this.level = this.superClassDeclaration.level + 1;
    this.privateStatics.$super = this.level+"super";
    var Super : Class = this.superClassDeclaration.Public;
    if (!this.native_) {
      this.publicConstructor.prototype = new Super();
    }
    this.Public = NativeClassDeclaration.createEmptyConstructor(this.publicConstructor);
  }

  internal function initMembers() : void {
    this.initializerNames = [];
    this.staticInitializers = [];
    this.boundMethodNames = [];
    var memberDeclarations : Array = this.memberDeclarations(this.publicConstructor, this.privateStatics);
    this.memberDeclarations = [];
    this.memberDeclarationsByQualifiedName = {};
    this.constructor_ = null;
    for (var i:int=0; i<memberDeclarations.length; ++i) {
      var item : * = memberDeclarations[i];
      switch (typeof item) {
        case "undefined":
          continue;
        case "function":
          this.staticInitializers.push(item);
          break;
        case "string":
          var memberDeclaration : MemberDeclaration = MemberDeclaration.create(item);
          if (memberDeclaration) {
            if (!memberDeclaration.isNative()) {
              if (++i >= memberDeclarations.length) {
                throw new Error(this + ": Member expected after modifiers '" + item + "'.");
              }
              var member : Object = memberDeclarations[i];
            }
            if (memberDeclaration.memberType == "function") {
              this.initMethod(memberDeclaration, member as Function);
            } else {
              for (var memberName:String in member) {
                this._storeMember(this._createMemberDeclaration(memberDeclaration, {memberName: memberName}), member[memberName]);
              }
            }
          }
      }
    }
    var defaultConstructor : Function = this.native_ ? this.publicConstructor :
      this.publicConstructor.prototype[this.level+"super"] =
      this.initializerNames.length==0 ? this.superClassDeclaration.constructor_ : createSuperCall(this);
    if (!this.constructor_) {
      // create empty default constructor:
      this.constructor_ = defaultConstructor;
    }
    if (this.boundMethodNames.length>0) {
      this.constructor_ = createMethodBindingConstructor(this.constructor_, this.boundMethodNames);
    }
    this.privateStatics[this.className] = this.publicConstructor;
  }

  // must be defined static because otherwise, jooc will add .bind(this) to all function expressions!
  private static function createSuperCall(cd : SystemClassDeclaration) : Function {
    if (cd.extends_=="Object") {
      return function $super() : void {
        for (var i:int=0; i<cd.initializerNames.length; ++i) {
          var slot : String = cd.initializerNames[i] as String;
          this[slot] = this[slot]();
        }
      };
    }
    return function $super() : void {
      cd.superClassDeclaration.constructor_.apply(this,arguments);
      for (var i:int=0; i<cd.initializerNames.length; ++i) {
        var slot : String = cd.initializerNames[i] as String;
        this[slot] = this[slot]();
      }
    };
  }

  // must be defined static because otherwise, jooc will add .bind(this) to all function expressions!
  private static function createMethodBindingConstructor(constructor_ : Class, boundMethodNames : Array) : Function {
    return function $bindMethods() : void {
      for (var i:int=0; i<boundMethodNames.length; ++i) {
        var slot : String = boundMethodNames[i] as String;
        this[slot] = this[slot].bind(this);
      }
      constructor_.apply(this, arguments);
    };
  }

  internal function _initSlot(memberDeclaration : MemberDeclaration) : void {
    memberDeclaration.slot = memberDeclaration.isPrivate() && !memberDeclaration.isStatic()
            ? this.privateStatics["$"+memberDeclaration.memberName] = this.level + memberDeclaration.memberName
            : memberDeclaration.memberName;
  }

  internal function initMethod(memberDeclaration : MemberDeclaration, member : Function) : void {
    if (memberDeclaration.memberName == this.className && !memberDeclaration.isStatic()) {
      if (memberDeclaration.getterOrSetter) {
        throw new Error(this+": Class name cannot be used for getter or setter: "+memberDeclaration);
      }
      this.constructor_ = memberDeclaration.isNative() ? this.publicConstructor : member;
    } else {
      this._initSlot(memberDeclaration);
      if (memberDeclaration.isNative()) {
        member = memberDeclaration.getNativeMember(this.publicConstructor);
      }
      if (this.extends_!="Object") {
        var superMethod : Function = memberDeclaration.retrieveMember(this.superClassDeclaration.Public.prototype);
      }
      var overrides : Boolean = !!superMethod && superMethod!==Object.prototype[memberDeclaration.memberName];
      if (overrides !== memberDeclaration.isOverride()) {
        var msg : String = overrides
                ? "Method overrides without 'override' modifier"
                : "Method with 'override' modifier does not override";
        throw new Error(this+": "+msg+": "+memberDeclaration);
      }
      if (overrides) {
        // found overriding: store super class' method as private member:
        this._storeMember(this._createMemberDeclaration(memberDeclaration, {_namespace: MemberDeclaration.NAMESPACE_PRIVATE}), superMethod);
      }
      this._storeMember(memberDeclaration, member);
      if (memberDeclaration.isBound()) {
        this.boundMethodNames.push(memberDeclaration.slot);
      }
    }
  }

  internal function _createMemberDeclaration(memberDeclaration : MemberDeclaration, changedProperties : Object) : MemberDeclaration {
    var newMemberDeclaration : MemberDeclaration = memberDeclaration.clone(changedProperties);
    this._initSlot(newMemberDeclaration);
    return newMemberDeclaration;
  }

  internal function _storeMember(memberDeclaration : MemberDeclaration, value : Object) : void {
    this.memberDeclarations.push(memberDeclaration);
    this.memberDeclarationsByQualifiedName[memberDeclaration.getQualifiedName()] = memberDeclaration;
    memberDeclaration.value = value;
    var _static : Boolean = memberDeclaration.isStatic();
    var _private : Boolean = memberDeclaration.isPrivate();
    var target : Object = _static ? _private ? this.privateStatics : this.publicConstructor : this.publicConstructor.prototype;
    if (!memberDeclaration.hasOwnMember(target)) {
      memberDeclaration.storeMember(target);
      if (memberDeclaration.hasInitializer()) {
        if (_static) {
          this.staticInitializers.push(memberDeclaration);
        } else {
          this.initializerNames.push(memberDeclaration.slot);
        }
      }
    }
  }

  internal override function doInit() : void {
    this.superClassDeclaration.init();
    this.initMembers();
    for (var i:int=0; i<this.staticInitializers.length; ++i) {
      var staticInitializer : * = this.staticInitializers[i];
      if (typeof staticInitializer=="function") {
        staticInitializer();
      } else {
        var target : Object = staticInitializer.isPrivate() ? this.privateStatics : this.publicConstructor;
        target[staticInitializer.slot] = target[staticInitializer.slot]();
      }
    }
  }

  public function getMemberDeclaration(namespace_ : String, memberName : String) : MemberDeclaration {
    return this.memberDeclarationsByQualifiedName[namespace_+"::"+memberName];
  }
}
}
