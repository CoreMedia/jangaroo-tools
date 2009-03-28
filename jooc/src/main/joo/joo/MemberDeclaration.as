package joo {
public class MemberDeclaration {

  public static const
          METHOD_TYPE_GET : String = "get",
          METHOD_TYPE_SET : String = "set",
          MEMBER_TYPE_VAR : String = "var",
          MEMBER_TYPE_CONST : String = "const",
          MEMBER_TYPE_FUNCTION : String = "function",
          NAMESPACE_PRIVATE : String = "private",
          NAMESPACE_INTERNAL : String = "internal",
          NAMESPACE_PROTECTED : String = "protected",
          NAMESPACE_PUBLIC : String = "public",
          STATIC : String = "static",
          FINAL : String = "final",
          NATIVE : String = "native",
          BOUND : String = "bound",
          OVERRIDE : String = "override";

  private static var SUPPORTS_GETTERS_SETTERS : Boolean;
{
  // no static initializers in system classes, use static block:
  SUPPORTS_GETTERS_SETTERS = "__defineGetter__" in Object.prototype;
}

  internal var
          _namespace : String = "internal",
          _static : Boolean = false,
          _final : Boolean = false,
          _native : Boolean = false,
          _bound : Boolean = false,
          _override : Boolean = false,
          memberType : String,
          getterOrSetter : String,
          memberName : String,
          slot : String,
          value : *,
          _cloneFactory : Class;

  public function MemberDeclaration(memberDeclarationStr : String) {
    var tokens : Array = memberDeclarationStr.split(/\s+/ as String) as Array;
    for (var j:int=0; j<tokens.length; ++j) {
      var token : String = tokens[j];
      if (!this.memberType) {
        switch(token) {
          case STATIC:
          case FINAL:
          case NATIVE:
          case BOUND:
          case OVERRIDE:
            this["_"+token] = true; break;
          case MEMBER_TYPE_VAR:
          case MEMBER_TYPE_CONST:
          case MEMBER_TYPE_FUNCTION:
            this.memberType = token; break;
          default:
            // "private", "public", "protected", "internal" or a custom namespace:
            this._namespace = token;
        }
      } else if (this.isMethod() && !this.getterOrSetter && (token==METHOD_TYPE_GET || token==METHOD_TYPE_SET)) {
        this.getterOrSetter = token; // detected getter or setter
      } else {
        this.memberName = token; // token following the member type is the member name
      }
    }
    if (!this.memberType) {
      throw new Error("Missing member type in declaration '" + memberDeclarationStr + "'.");
    }
  }

  public function getQualifiedName() : String {
    return this._namespace+"::"+this.memberName;
  }

  public function isPrivate() : Boolean {
    return this._namespace==NAMESPACE_PRIVATE;
  }

  public function isStatic() : Boolean {
    return this._static;
  }

  public function isFinal() : Boolean {
    return this._final;
  }

  public function isNative() : Boolean {
    return this._native;
  }

  public function isOverride() : Boolean {
    return this._override;
  }

  public function isBound() : Boolean {
    return this._bound;
  }

  public function isMethod() : Boolean {
    return this.memberType==MEMBER_TYPE_FUNCTION;
  }

  // public function retrieveMember(source : Object) : Function
  /* not needed if we take reflection seriously!
   retrieveMember: function joo$MemberDeclaration$getMember(source) {
   return this.getterOrSetter==METHOD_TYPE_GET ? source.__lookupGetter__(this.memberName)
   : this.getterOrSetter==METHOD_TYPE_SET ? source.__lookupSetter__(this.memberName)
   : source[this.memberName];
   },*/

  internal function setSlot(slot : String) : void {
    this.slot = slot;
  }

  internal function getSlot() : String {
    return this.slot || this.memberName;
  }

  internal function getNativeMember(publicConstructor : Class) : * {
    var target : * = this.isStatic() ? publicConstructor : publicConstructor.prototype;
    if (this.memberType==MEMBER_TYPE_FUNCTION && this.getterOrSetter) {
      // native variables are only declared as getter/setter functions, never implemented as such:
      this.memberType = MEMBER_TYPE_VAR;
      this.getterOrSetter = null;
    }
    var member : * = target[this.memberName];
    if (typeof member!="function") {
      var memberObject : Object = {};
      memberObject[this.memberName] = member;
      member = memberObject;
    }
    return member;
  }

  internal function retrieveMember(target : Object, fromSuper : Boolean) : * {
    var slot : String = this.getSlot();
    if (this.getterOrSetter) {
      if (SUPPORTS_GETTERS_SETTERS) {
        return target[this.getterOrSetter==METHOD_TYPE_GET ? "__lookupGetter__" : "__lookupSetter__"](slot);
      } else {
        slot = this.getterOrSetter+"$"+slot;
      }
    }
    return fromSuper || !target.hasOwnProperty || target.hasOwnProperty(slot) ? target[slot] : undefined;
  }

  internal function storeMember(target : Object) : void {
    // store only if not native:
    if (!this.isNative()) {
      var slot : String = this.getSlot();
      if (this.getterOrSetter) {
        if (SUPPORTS_GETTERS_SETTERS) {
          target[this.getterOrSetter==METHOD_TYPE_GET ? "__defineGetter__" : "__defineSetter__"](slot, this.value);
          return;
        } else {
          slot = this.getterOrSetter+"$"+slot;
        }
      }
      target[slot] = this.value;
    }
  }

  public function hasInitializer() : Boolean {
    return this.memberType!=MEMBER_TYPE_FUNCTION && typeof this.value=="function" && this.value.constructor!==RegExp;
  }

  public function initMember(target : Object) : void {
    var slot : String = this.getSlot();
    if (this.isBound()) {
      target[slot] = target[slot].bind(target);
    } else {
      target[slot] = target[slot]();
    }
  }

  public function _getCloneFactory() : Class {
    if (!this._cloneFactory) {
      this._cloneFactory = function() : void { };
      this._cloneFactory.prototype = this;
    }
    return this._cloneFactory;
  }

  public function clone(changedProperties : Object) : MemberDeclaration {
    var CloneFactory : Class = this._getCloneFactory();
    var clone : MemberDeclaration = new CloneFactory();
    for (var m:String in changedProperties) {
      clone[m] = changedProperties[m];
    }
    return clone;
  }

  public function toString() : String {
    var sb : Array = [this._namespace];
    if (this._static)
      sb.push(STATIC);
    if (this._override)
      sb.push(OVERRIDE);
    if (this._bound)
      sb.push(BOUND);
    sb.push(this.memberType);
    sb.push(this.memberName);
    return sb.join(" ");
  }

}
}
