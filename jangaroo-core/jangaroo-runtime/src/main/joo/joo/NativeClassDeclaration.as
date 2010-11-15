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

public class NativeClassDeclaration {

  internal static function createEmptyConstructor(constructor_ : Function) : Function {
    var emptyConstructor : Function = function() : void {
      this.constructor = constructor_;
    };
    emptyConstructor.prototype =  constructor_.prototype;
    return emptyConstructor;
  }

  internal static const STATE_LOADED : int = 0;
  internal static const STATE_COMPLETING : int = 1;
  internal static const STATE_COMPLETED : int = 2;
  internal static const STATE_INITIALIZING : int = 3;
  internal static const STATE_INITIALIZED : int = 4;

  public var
          level : int = -1,
          fullClassName : String,
          constructor_ : Function,
          publicConstructor : Function,
          state  : int = STATE_LOADED,
          Public : Function,
          superClassDeclaration : NativeClassDeclaration,
          interfaces : Array;

  public function NativeClassDeclaration() {
  }

  public function create(fullClassName : String, publicConstructor : Function) : NativeClassDeclaration {
    this.fullClassName = fullClassName;
    this.publicConstructor = publicConstructor;
    try {
      this.publicConstructor["$class"] = this;
    } catch (e:Error) {
      // ignore that expando properties do not work with certain native objects in certain browsers, e.g. IE7 / XMLHttpRequest
    }
    return this;
  }

  public function complete() : NativeClassDeclaration {
    if (state < STATE_COMPLETING ) {
      state = STATE_COMPLETING;
      this.doComplete();
      state = STATE_COMPLETED;
    }
    return this;
  }

  // built-in Error constructor called as function unfortunately always creates a new Error object, so we have to emulate it:
  private static const ERROR_CONSTRUCTOR:Function = function(message:String):void {
    this.message = message || "";
  };

  protected function doComplete() : void {
    this.interfaces = [];
    this.constructor_ = this.publicConstructor === Error ? ERROR_CONSTRUCTOR : this.publicConstructor;
    this.Public = createEmptyConstructor(this.publicConstructor);
  }

  private static var initializationDepth:String = "";

  public function init() : NativeClassDeclaration {
    if (state < STATE_INITIALIZING ) {
      complete();
      state = STATE_INITIALIZING;
      if (classLoader.debug) {
        trace("[INFO] Jangaroo Runtime: initializing class " + initializationDepth + fullClassName);
        initializationDepth += "  ";
      }
      doInit();
      if (classLoader.debug) {
        initializationDepth = initializationDepth.substr(0, initializationDepth.length - 2);
      }
      state = STATE_INITIALIZED;
    } else if (state === STATE_INITIALIZING) {
      trace("[WARN] Jangaroo Runtime: cyclic static initializer dependency in " + fullClassName);
    }
    return this;
  }

  protected function doInit() : void {
  }

  public function isInstance(object : Object) : Boolean {
    return object instanceof this.constructor_ || object && object.constructor===this.constructor_;
  }

  public function getQualifiedName() : String {
    // AS uses namespace notation (::) to separate package and class name:
    return this.fullClassName.replace(/\.([^\.]+)^/, "::");
  }

  public function toString() : String {
    return this.fullClassName;
  }
}
}
