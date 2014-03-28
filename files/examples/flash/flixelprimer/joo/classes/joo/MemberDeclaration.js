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

"public class MemberDeclaration",1,function($$private){;return[function(){joo.classLoader.init(RegExp,Object);}, 

  "public static const",{
          METHOD_TYPE_GET/* : String*/ : "get",
          METHOD_TYPE_SET/* : String*/ : "set",
          MEMBER_TYPE_VAR/* : String*/ : "var",
          MEMBER_TYPE_CONST/* : String*/ : "const",
          MEMBER_TYPE_FUNCTION/* : String*/ : "function",
          MEMBER_TYPE_CLASS/* : String*/ : "class",
          MEMBER_TYPE_INTERFACE/* : String*/ : "interface",
          MEMBER_TYPE_NAMESPACE/* : String*/ : "namespace",
          NAMESPACE_PRIVATE/* : String*/ : "private",
          NAMESPACE_INTERNAL/* : String*/ : "internal",
          NAMESPACE_PROTECTED/* : String*/ : "protected",
          NAMESPACE_PUBLIC/* : String*/ : "public",
          STATIC/* : String*/ : "static",
          FINAL/* : String*/ : "final",
          NATIVE/* : String*/ : "native",
          OVERRIDE/* : String*/ : "override",
          VIRTUAL/* : String*/ : "virtual"},

  "private static var",{ SUPPORTS_GETTERS_SETTERS/* : Boolean*/:false},
  "private static var",{ SUPPORTS_PROPERTIES/* : Boolean*/:false},
  "private static var",{ DEFINE_METHOD/* : Object*/:null},
  "private static var",{ LOOKUP_METHOD/* : Object*/:null},function()

{
  // no static initializers in system classes, use static block:
  if ('getOwnPropertyDescriptor' in Object) {
    try {
      // To make sure we are not in IE8, which implements this method only partially,
      // just try getting a property of some Object and see if it fails:
      $$private.SUPPORTS_PROPERTIES = Object['getOwnPropertyDescriptor']({foo:1},"foo").value === 1;
    } catch (e/*:**/) {
      // ignore
    }
  }
  $$private.SUPPORTS_GETTERS_SETTERS = "__defineGetter__" in Object['prototype'];
  $$private.DEFINE_METHOD = {
    "get":  "__defineGetter__",
    "set": "__defineSetter__"
  };
  $$private.LOOKUP_METHOD = {
    "get": "__lookupGetter__",
    "set": "__lookupSetter__"
  };
},

  "public static function create",function create(memberDeclarationStr/* : String*/)/* : MemberDeclaration*/ {
    var tokens/* : Array*/ = memberDeclarationStr.split(/\s+/);
    // ignore imports:
    return tokens[0]=="import" ? null
           : new joo.MemberDeclaration(tokens);
  },

  "internal var",{
          _namespace/* : String*/ :function(){return( joo.MemberDeclaration.NAMESPACE_INTERNAL);},
          _static/* : Boolean*/ : false,
          _final/* : Boolean*/ : false,
          _native/* : Boolean*/ : false,
          _override/* : Boolean*/ : false,
          _cloneFactory/* : Function*/:null},
  "public var",{
          memberType/* : String*/:null,
          getterOrSetter/* : String*/:null,
          memberName/* : String*/:null,
          slot/* : String*/:null,
          value/* : **/:undefined},
  /**
   * The metadata (annotations) associated with this member.
   */
  "public var",{ metadata/* : Object*/ :function(){return( {});}},

  "public function MemberDeclaration",function MemberDeclaration$(tokens/* : Array*/) {this._namespace=this._namespace();this.metadata=this.metadata();
    for (var j/*:int*/ =0; j<tokens.length; ++j) {
      var token/* : String*/ = tokens[j];
      if (!this.memberType) {
        switch(token) {
          case joo.MemberDeclaration.STATIC:
          case joo.MemberDeclaration.FINAL:
          case joo.MemberDeclaration.NATIVE:
          case joo.MemberDeclaration.OVERRIDE:
            this["_"+token] = true; break;
          case joo.MemberDeclaration.MEMBER_TYPE_VAR:
          case joo.MemberDeclaration.MEMBER_TYPE_CONST:
          case joo.MemberDeclaration.MEMBER_TYPE_FUNCTION:
          case joo.MemberDeclaration.MEMBER_TYPE_CLASS:
            this.memberType = token; break;
          case joo.MemberDeclaration.VIRTUAL:
            break; // ignore, but do not consider a namespace
          default:
            // "private", "public", "protected", "internal" or a custom namespace:
            this._namespace = token;
        }
      } else {
        if (this.isMethod() && $$private.LOOKUP_METHOD[this.memberName]) {
          this.getterOrSetter = this.memberName; // detected getter or setter
        }
        this.memberName = token; // token following the member type is the member name
        if (this.memberType === joo.MemberDeclaration.MEMBER_TYPE_CLASS) {
          break; // let classLoader.prepare parse the rest!
        }
      }
    }
    if (!this.memberType) {
      throw new Error("Missing member type in declaration '" + tokens.join(" ") + "'.");
    }
  },

  "public function getQualifiedName",function getQualifiedName()/* : String*/ {
    return this._namespace+"::"+this.memberName;
  },

  "public function isPrivate",function isPrivate()/* : Boolean*/ {
    return this._namespace==joo.MemberDeclaration.NAMESPACE_PRIVATE;
  },

  "public function isStatic",function isStatic()/* : Boolean*/ {
    return this._static;
  },

  "public function isFinal",function isFinal()/* : Boolean*/ {
    return this._final;
  },

  "public function isNative",function isNative()/* : Boolean*/ {
    return this._native;
  },

  "public function isOverride",function isOverride()/* : Boolean*/ {
    return this._override;
  },

  "public function isMethod",function isMethod()/* : Boolean*/ {
    return this.memberType==joo.MemberDeclaration.MEMBER_TYPE_FUNCTION;
  },

  "internal function initSlot",function initSlot(level/* : int*/)/* : void*/ {
    this.slot = this.isPrivate() && !this.isStatic()
            ? this.memberName + "$" + level
            : this.memberName;
  },

  // public function retrieveMember(source : Object) : Function
  /* not needed if we take reflection seriously!
   retrieveMember: function joo$MemberDeclaration$getMember(source) {
   return this.getterOrSetter==METHOD_TYPE_GET ? source.__lookupGetter__(this.memberName)
   : this.getterOrSetter==METHOD_TYPE_SET ? source.__lookupSetter__(this.memberName)
   : source[this.memberName];
   },*/

  "public function getNativeMember",function getNativeMember(publicConstructor/* : Function*/)/* : **/ {
    var target/* : **/ = this.isStatic() ? publicConstructor : publicConstructor.prototype;
    if (this.memberType==joo.MemberDeclaration.MEMBER_TYPE_FUNCTION && this.getterOrSetter) {
      // native variables are only declared as getter/setter functions, never implemented as such:
      this.memberType = joo.MemberDeclaration.MEMBER_TYPE_VAR;
      this.getterOrSetter = null;
    }
    try {
      var member/* : **/ = target[this.memberName];
    } catch (e/* : **/) {
      // ignore Firefox' native member access exceptions.
    }
    if (typeof member!="function") {
      var memberObject/* : Object*/ = {};
      memberObject[this.memberName] = member;
      member = memberObject;
    }
    return member;
  },

  "public function hasOwnMember",function hasOwnMember(target/* : Object*/)/* : Boolean*/ {
    // fast path:
    if (!this.getterOrSetter && "hasOwnProperty" in target) {
      return target.hasOwnProperty(this.slot);
    }
    var value/* : **/ = this.retrieveMember(target);
    if (value!==undefined && target.constructor) {
      // is it really target's own member? Retrieve super's value:
      var superTarget/* : Object*/ = target.constructor.prototype;
      var superValue/* : **/ = this.retrieveMember(superTarget);
      if (value!==superValue) {
        return true;
      }
    }
    return false;
  },

  "public function retrieveMember",function retrieveMember(target/* : Object*/)/* : **/ {
    if (!target) {
      return undefined;
    }
    var slot/* : String*/ = this.slot;
    if (this.getterOrSetter) {
      if ($$private.SUPPORTS_PROPERTIES) {
        var propertyDescriptor/*:Object*/ = this._lookupPropertyDescriptor(target);
        return propertyDescriptor ? propertyDescriptor[this.getterOrSetter] : undefined;
      } else if ($$private.SUPPORTS_GETTERS_SETTERS) {
        return target[$$private.LOOKUP_METHOD[this.getterOrSetter]](slot);
      } else {
        slot = slot+"$"+this.getterOrSetter;
      }
    }
    try {
      return target[slot];
    } catch (e/*:**/) {
      // some IE oddities may throw error instead of returning undefined
      return undefined;
    }
  },

  "internal function _lookupPropertyDescriptor",function _lookupPropertyDescriptor(target/*:Object*/)/*:Object*/ {
    var slot/*:String*/ = this.slot;
    do {
      var propertyDescriptor/*:Object*/ = Object['getOwnPropertyDescriptor'](target, slot);
      if (propertyDescriptor) {
        return propertyDescriptor;
      }
      var oldTarget/*:Object*/ = target;
      target = target.constructor ? target.constructor['superclass'] || target.constructor.prototype : null;
    } while (target && target !== oldTarget);
    return undefined;
  },

  "public function storeMember",function storeMember(target/* : Object*/)/* : void*/ {
    // store only if not native and not already present:
    if (!this.isNative() && !this.hasOwnMember(target)) {
      var slot/* : String*/ = this.slot;
      if (this.getterOrSetter) {
        if ($$private.SUPPORTS_PROPERTIES) {
          // we have to redefine the property as a whole, so we look up the existing definition first:
          var propertyDescriptor/*:Object*/ = this._lookupPropertyDescriptor(target)
            || { configurable: true, enumerable: true };
          propertyDescriptor[this.getterOrSetter] = this.value;
          Object['defineProperty'](target, slot, propertyDescriptor);
          return;
        } else if ($$private.SUPPORTS_GETTERS_SETTERS) {
          // defining a getter or setter disables the counterpart setter/getter from the prototype,
          // so copy that setter/getter before, if "target" does not already define it:
          var oppositeMethodType/*:**/ = this.getterOrSetter==joo.MemberDeclaration.METHOD_TYPE_GET ? joo.MemberDeclaration.METHOD_TYPE_SET : joo.MemberDeclaration.METHOD_TYPE_GET;
          var counterpart/* : Function*/ = target[$$private.LOOKUP_METHOD[oppositeMethodType]](slot);
          // if counterpart is defined, check that it is not overridden (differs from prototype's counterpart):
          if (counterpart && counterpart===target.constructor.prototype[$$private.LOOKUP_METHOD[oppositeMethodType]](slot)) {
            // set the counterpart directly on target. This may be redundant, but we cannot find out.
            target[$$private.DEFINE_METHOD[oppositeMethodType]](slot, counterpart);
          }
          target[$$private.DEFINE_METHOD[this.getterOrSetter]](slot, this.value);
          return;
        } else {
          slot = slot+"$"+this.getterOrSetter;
        }
      }
      target[slot] = this.value;
    }
  },

  "public function hasInitializer",function hasInitializer()/* : Boolean*/ {
    return this.memberType!=joo.MemberDeclaration.MEMBER_TYPE_FUNCTION && this.memberType!=joo.MemberDeclaration.MEMBER_TYPE_CLASS && typeof this.value=="function" && this.value.constructor!==RegExp;
  },

  "public function _getCloneFactory",function _getCloneFactory()/* : Function*/ {
    if (!this._cloneFactory) {
      this._cloneFactory = function joo$MemberDeclaration$282_28()/* : void*/ { };
      this._cloneFactory.prototype = this;
    }
    return this._cloneFactory;
  },

  "public function clone",function clone(changedProperties/* : Object*/)/* : MemberDeclaration*/ {
    var CloneFactory/* : Function*/ = this._getCloneFactory();
    var clone/* : MemberDeclaration*/ =/* joo.MemberDeclaration*/(new CloneFactory());
    for (var m/*:String*/ in changedProperties) {
      clone[m] = changedProperties[m];
    }
    return clone;
  },

  "public function toString",function toString()/* : String*/ {
    var sb/* : Array*/ = [this._namespace];
    if (this._static) {
      sb.push(joo.MemberDeclaration.STATIC);
    }
    if (this._override) {
      sb.push(joo.MemberDeclaration.OVERRIDE);
    }
    sb.push(this.memberType);
    if (this.getterOrSetter) {
      sb.push(this.getterOrSetter);
    }
    sb.push(this.memberName);
    return sb.join(" ");
  },

];},["create"],["Object","Error","RegExp"], "0.8.0", "0.8.3"
);