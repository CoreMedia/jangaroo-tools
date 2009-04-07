package joo {

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
    if (!type || object===undefined || object===null)
      return false;
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
          initializers : Array,
          staticInitializers : Array,
          boundMethods : Array,
          publicStaticMethodNames : Array;

  public function SystemClassDeclaration(packageDef : String, directives : Array, classDef : String, memberDeclarations : Function,
          publicStaticMethodNames : Array) {
    var packageName : String = packageDef.split(/\s+/ as String)[1] || "";
    this.package_ = joo.getOrCreatePackage(packageName);
    this.parseDirectives(directives);
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
    if (publicConstructor) {
      this.native_ = true;
    } else {
      publicConstructor = createPublicConstructor(this);
      this.package_[this.className] = publicConstructor;
    }
    super(fullClassName, publicConstructor);
    this.privateStatics = { "assert": joo["assert"], "is": is_ };
  }

  internal function parseDirectives(directives : Array) : void { }

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
    this.initializers = [];
    this.staticInitializers = [];
    this.boundMethods = [];
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
          var memberDeclaration : MemberDeclaration = new MemberDeclaration(item);
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
              this._storeMember(memberDeclaration.clone({memberName: memberName}), member[memberName]);
            }
          }
      }
    }
    var superCall : Function = this.publicConstructor.prototype[this.level+"super"] =
      this.initializers.length==0 && this.boundMethods.length==0 ? this.superClassDeclaration.constructor_ : createSuperCall(this);
    if (!this.constructor_) {
      // create empty default constructor:
      this.constructor_ = superCall;
    }
    this.privateStatics[this.className] = this.publicConstructor;
  }

  // must be defined static because otherwise, jooc will add .bind(this) to all function expressions!
  private static function createSuperCall(cd : SystemClassDeclaration) : Function {
    if (cd.extends_=="Object") {
      return function $super() : void {
        initObject(this, cd.boundMethods);
        initObject(this, cd.initializers);
      };
    }
    return function $super() : void {
      initObject(this, cd.boundMethods);
      cd.superClassDeclaration.constructor_.apply(this,arguments);
      initObject(this, cd.initializers);
    };
  }

  internal function initMethod(memberDeclaration : MemberDeclaration, member : Function) : void {
    if (memberDeclaration.memberName == this.className && !memberDeclaration.isStatic()) {
      if (memberDeclaration.getterOrSetter) {
        throw new Error(this+": Class name cannot be used for getter or setter: "+memberDeclaration);
      }
      this.constructor_ = memberDeclaration.isNative() ? this.publicConstructor : member;
    } else {
      if (memberDeclaration.isNative()) {
        member = memberDeclaration.getNativeMember(this.publicConstructor);
      }
      if (memberDeclaration.isBound()) {
        this.boundMethods.push(memberDeclaration);
      }
      if (this.extends_!="Object") {
        var superMethod : Function = memberDeclaration.retrieveMember(this.superClassDeclaration.Public.prototype,true);
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
        this._storeMember(memberDeclaration.clone({_namespace: MemberDeclaration.NAMESPACE_PRIVATE}), superMethod);
      }
      this._storeMember(memberDeclaration, member);
    }
  }

  internal function _storeMember(memberDeclaration : MemberDeclaration, value : Object) : void {
    this.memberDeclarations.push(memberDeclaration);
    this.memberDeclarationsByQualifiedName[memberDeclaration.getQualifiedName()] = memberDeclaration;
    memberDeclaration.value = value;
    var _static : Boolean = memberDeclaration.isStatic();
    var _private : Boolean = memberDeclaration.isPrivate();
    if (!_static && _private) {
      var slot : String = this.level + memberDeclaration.memberName;
      this.privateStatics["$"+memberDeclaration.memberName] = slot;
      memberDeclaration.setSlot(slot);
    }
    var target : Object = _static ? _private ? this.privateStatics : this.publicConstructor : this.publicConstructor.prototype;
    if (!memberDeclaration.retrieveMember(target,false)) {
      memberDeclaration.storeMember(target);
      if (memberDeclaration.hasInitializer()) {
        var initTarget : Object = _static ? this.staticInitializers : this.initializers;
        initTarget.push(memberDeclaration);
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
        staticInitializer.initMember(staticInitializer.isPrivate() ? this.privateStatics : this.publicConstructor);
      }
    }
  }

  private static function initObject(object : Object, memberDeclarations : Array) : void {
    for (var i:int=0; i<memberDeclarations.length; ++i) {
      (memberDeclarations[i] as MemberDeclaration).initMember(object);
    }
  }

  public function getMemberDeclaration(namespace_ : String, memberName : String) : MemberDeclaration {
    return this.memberDeclarationsByQualifiedName[namespace_+"::"+memberName];
  }
}
}
