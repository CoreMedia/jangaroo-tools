package joo {

import joo.*;

public class NativeClassDeclaration {

  internal static function createEmptyConstructor(constructor_ : Class) : Class {
    var emptyConstructor : Class = function() : void {
      this.constructor = constructor_;
    };
    emptyConstructor.prototype =  constructor_.prototype;
    return emptyConstructor;
  }

  internal var
          level : int = -1,
          fullClassName : String,
          constructor_ : Class,
          publicConstructor : Class,
          completed  : Boolean = false,
          inited  : Boolean = false,
          Public : Class,
          superClassDeclaration : NativeClassDeclaration,
          interfaces : Array;

  public function NativeClassDeclaration(fullClassName : String, publicConstructor : Class) {
    this.fullClassName = fullClassName;
    this.publicConstructor = publicConstructor;
    this.publicConstructor.$class = this;
  }

  internal function complete() : NativeClassDeclaration {
    if (!this.completed) {
      this.completed = true;
      this.doComplete();
    }
    return this;
  }

  internal function doComplete() : void {
    this.interfaces = [];
    this.constructor_ = this.publicConstructor;
    this.Public = createEmptyConstructor(this.publicConstructor);
  }

  internal function init() : NativeClassDeclaration {
    if (!this.inited) {
      this.inited = true;
      this.complete();
      this.doInit();
    }
    return this;
  }

  internal function doInit() : void {
  }

  public function isInstance(object : Object) : Boolean {
    return object instanceof this.constructor_ || object && object.constructor===this.constructor_;
  }

  public function getQualifiedName() : String {
    // AS uses namespace notation (::) to separate package and class name:
    return this.fullClassName.replace(/\.([^\.]+)^/ as String,"::");
  }

  public function toString() : String {
    return this.fullClassName;
  }
}
}
