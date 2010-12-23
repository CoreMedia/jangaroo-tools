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

public class SystemClassDeclaration extends NativeClassDeclaration {

  protected static function createPublicConstructor(cd : NativeClassDeclaration) : Function {
    return function joo$SystemClassDeclaration$constructor() : void {
      this.constructor =  cd.publicConstructor;
      cd.constructor_.apply(this, arguments);
    };
  }

  private static function boundMethod(object:Object, methodName:String):Function {
    return object['$$b_'+methodName] ||
      (object['$$b_'+methodName] = function():* {
        return object[methodName].apply(object,arguments);
      });
  }

{
  // publish "boundMethod" as "joo.boundMethod" for use from JavaScript:
  var jooPackage:* = getQualifiedObject("joo");
  jooPackage["boundMethod"] = boundMethod;
}

  protected var
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
          staticInitializers : Array/*<MemberDeclaration>*/,
          publicStaticMethodNames : Array;
  /**
   * The metadata (annotations) associated with this class.
   */
  public var metadata : Object = {};

  private static const DECLARATION_PATTERN_CLASS:RegExp =
    /^\s*((public|internal|final|dynamic)\s+)*class\s+([A-Za-z][a-zA-Z$_0-9]*)(\s+extends\s+([a-zA-Z$_0-9.]+))?(\s+implements\s+([a-zA-Z$_0-9.,\s]+))?\s*$/;
  private static const DECLARATION_PATTERN_INTERFACE:RegExp =
    /^\s*((public|internal)\s+)?interface\s+([A-Za-z][a-zA-Z$_0-9]*)(\s+extends\s+([a-zA-Z$_0-9.,\s]+))?\s*$/;
  private static const DECLARATION_PATTERN_NAMESPACE:RegExp =
    /^\s*((public|internal)\s+)?namespace\s+([A-Za-z][a-zA-Z$_0-9]*)\s*$/;

  public function SystemClassDeclaration(packageDef : String, classDef : String, inheritanceLevel : int, memberDeclarations : Function,
          publicStaticMethodNames : Array) {
    var packageName : String = packageDef.split(/\s+/)[1] || "";
    this.package_ = getOrCreatePackage(packageName);
    var classMatch : Array = classDef.match(DECLARATION_PATTERN_CLASS);
    var interfaces : String;
    if (classMatch) {
      if (classMatch[5]) {
        this.extends_ = classMatch[5];
      }
      interfaces = classMatch[7];
    } else {
      classMatch = classDef.match(DECLARATION_PATTERN_INTERFACE);
      if (classMatch) {
        this.type = MemberDeclaration.MEMBER_TYPE_INTERFACE;
        interfaces = classMatch[5];
      } else {
        classMatch = classDef.match(DECLARATION_PATTERN_NAMESPACE);
        if (classMatch) {
          this.type = MemberDeclaration.MEMBER_TYPE_NAMESPACE;
        }
      }
    }
    if (!classMatch) {
      throw new Error("SyntaxError: \""+classDef+"\" does not match.");
    }
    this.level = inheritanceLevel;
    this.namespace_ = classMatch[2];
    this.className    = classMatch[3];
    var fullClassName : String = this.className;
    if (packageName) {
      fullClassName = packageName+"."+this.className;
    }
    this.interfaces = interfaces ? interfaces.split(/\s*,\s*/) : [];
    this.memberDeclarations = memberDeclarations;
    this.publicStaticMethodNames = publicStaticMethodNames;
    var publicConstructor : Function = getQualifiedObject(fullClassName);
    if (publicConstructor) {
      this.native_ = true;
    } else {
      publicConstructor = createPublicConstructor(this);
      this.package_[this.className] = publicConstructor;
    }
    this.create(fullClassName, publicConstructor);
    this.privateStatics = {};
  }

  public function isClass() : Boolean {
    return this.type === MemberDeclaration.MEMBER_TYPE_CLASS;
  }

  public function isInterface() : Boolean {
    return this.type === MemberDeclaration.MEMBER_TYPE_INTERFACE;
  }

  public function isNamespace() : Boolean {
    return this.type === MemberDeclaration.MEMBER_TYPE_NAMESPACE;
  }

  public function isNative() : Boolean {
    return this.native_;
  }

  protected override function doComplete() : void {
    this.superClassDeclaration = classLoader.getRequiredClassDeclaration(this.extends_);
    this.superClassDeclaration.complete();
    var Super : Function = this.superClassDeclaration.Public;
    if (!this.native_) {
      this.publicConstructor.prototype = new Super();
      this.publicConstructor["superclass"] = this.publicConstructor.prototype; // Ext Core compatibility!
    }
    this.Public = NativeClassDeclaration.createEmptyConstructor(this.publicConstructor);
    initTypes();
  }

  protected function initMembers() : void {
    this.staticInitializers = [];
    var memberDeclarations : Array = this.memberDeclarations(this.privateStatics);
    this.memberDeclarations = [];
    this.memberDeclarationsByQualifiedName = {};
    this.constructor_ = null;
    var metadata:Object = {};
    for (var i:int=0; i<memberDeclarations.length; ++i) {
      var item : * = memberDeclarations[i];
      switch (typeof item) {
        case "function":
          this.staticInitializers.push(item);
          break;
        case "string":
          var memberDeclaration : MemberDeclaration = MemberDeclaration.create(item);
          if (memberDeclaration) {
            memberDeclaration.metadata = metadata;
            metadata = {};
            if (!memberDeclaration.isNative()) {
              if (++i >= memberDeclarations.length) {
                throw new Error(this + ": Member expected after modifiers '" + item + "'.");
              }
              var member : * = memberDeclarations[i];
            }
            switch (memberDeclaration.memberType) {
              case MemberDeclaration.MEMBER_TYPE_FUNCTION:
                this.initMethod(memberDeclaration, Function(member));
                break;
              case MemberDeclaration.MEMBER_TYPE_CLASS:
                var helperInheritanceLevel:int = int(member);
                var helperMemberDeclarations:Function = memberDeclarations[++i];
                var helperStatics:Array = memberDeclarations[++i];
                var secondaryClass:NativeClassDeclaration = classLoader.prepare("package "+this.fullClassName, item,
                        helperInheritanceLevel, helperMemberDeclarations,
                        helperStatics, [], runtimeApiVersion, compilerVersion).complete();
                this.publicConstructor[memberDeclaration.memberName] = secondaryClass.publicConstructor;
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
    if (!this.isInterface()) {
      if (!this.native_) {
        this.publicConstructor.prototype["super$" + this.level] = this.superClassDeclaration.constructor_;
      }
      if (!this.constructor_) {
        // reuse native public constructor or super class constructor:
        this.constructor_ = this.native_ ? this.publicConstructor : this.superClassDeclaration.constructor_;
      }
    }
  }

  protected function initMethod(memberDeclaration : MemberDeclaration, member : Function) : void {
    if (memberDeclaration.memberName == this.className && !memberDeclaration.isStatic()) {
      if (memberDeclaration.getterOrSetter) {
        throw new Error(this+": Class name cannot be used for getter or setter: "+memberDeclaration);
      }
      this.constructor_ = memberDeclaration.isNative() ? this.publicConstructor : member;
    } else {
      memberDeclaration.initSlot(this.level);
      if (memberDeclaration.isNative()) {
        member = memberDeclaration.getNativeMember(this.publicConstructor);
      }
      if (memberDeclaration.isMethod()) {
        if (this.extends_!="Object") {
          var superMethod : Function = memberDeclaration.retrieveMember(this.superClassDeclaration.Public.prototype);
        }
        var overrides : Boolean = !!superMethod
          && superMethod!==member
          && superMethod!==Object['prototype'][memberDeclaration.memberName];
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
      }
      this._storeMember(memberDeclaration, member);
    }
  }

  protected function _createMemberDeclaration(memberDeclaration : MemberDeclaration, changedProperties : Object) : MemberDeclaration {
    var newMemberDeclaration : MemberDeclaration = memberDeclaration.clone(changedProperties);
    newMemberDeclaration.initSlot(this.level);
    return newMemberDeclaration;
  }

  protected function _storeMember(memberDeclaration : MemberDeclaration, value : Object) : void {
    this.memberDeclarations.push(memberDeclaration);
    this.memberDeclarationsByQualifiedName[memberDeclaration.getQualifiedName()] = memberDeclaration;
    memberDeclaration.value = value;
    var _static : Boolean = memberDeclaration.isStatic();
    var _private : Boolean = memberDeclaration.isPrivate();

    if (_static && memberDeclaration.hasInitializer()) {
      this.staticInitializers.push(memberDeclaration);
    }
    _processMetadata(memberDeclaration);
    var target : Object = _static ? _private ? this.privateStatics : this.publicConstructor : this.publicConstructor.prototype;

    // for compatibility with Prototype (which defines Node as an Object in IE):
    if (!target) {
      target = {};
    }
    
    memberDeclaration.storeMember(target);
  }

  protected function _processMetadata(memberDeclaration : MemberDeclaration):void {
    var metadata:Object = memberDeclaration.metadata;
    if (metadata) {
      for (var metaFunctionName:String in metadata) {
        var metaFunction:Function = getQualifiedObject("joo.meta." + metaFunctionName);
        if (metaFunction) {
          metaFunction(this, memberDeclaration, metadata[metaFunctionName]);
        }
      }
    }
  }

  protected override function doInit() : void {
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
    var memberDeclaration:MemberDeclaration = this.memberDeclarationsByQualifiedName[namespace_ + "::" + memberName];
    return !memberDeclaration && this.superClassDeclaration && this.superClassDeclaration["getMemberDeclaration"]
      ? (this.superClassDeclaration as SystemClassDeclaration).getMemberDeclaration(namespace_, memberName)
      : memberDeclaration;

  }
}
}
