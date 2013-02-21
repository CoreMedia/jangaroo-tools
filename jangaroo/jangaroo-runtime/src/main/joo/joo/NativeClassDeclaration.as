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

  internal static const RESOURCE_BUNDLE_PATTERN:RegExp = /_properties$/;

  internal static function createEmptyConstructor(prototype_ : Object) : Function {
    var emptyConstructor : Function = function() : void {};
    if (prototype_) {
      emptyConstructor.prototype = prototype_;
    }
    return emptyConstructor;
  }

  internal static const STATE_LOADED : int = 0;
  internal static const STATE_COMPLETING : int = 1;
  internal static const STATE_COMPLETED : int = 2;
  internal static const STATE_INITIALIZING : int = 3;
  internal static const STATE_MEMBERS_INITIALIZED : int = 4;
  internal static const STATE_INITIALIZED : int = 5;

  public var
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
    if (publicConstructor) {
      try {
        this.publicConstructor["$class"] = this;
      } catch (e:*) {
        // ignore that expando properties do not work with certain native objects in certain browsers, e.g. IE7 / XMLHttpRequest
      }
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

  internal function doComplete() : void {
    interfaces = [];
    constructor_ = Class(publicConstructor) === Error ? getQualifiedObject("joo.Error") : publicConstructor;
    Public = createEmptyConstructor(publicConstructor.prototype);
  }

  public function isInstance(obj:Object):Boolean {
    return obj instanceof publicConstructor;
  }

  private static var initializationDepth:String = "";

  public function init() : NativeClassDeclaration {
    if (state < STATE_INITIALIZING ) {
      complete();
      state = STATE_INITIALIZING;
      if (classLoader.debug) {
        trace("[DEBUG]", "Jangaroo Runtime: initializing class " + initializationDepth + fullClassName);
        initializationDepth += "  ";
      }
      doInit();
      if (classLoader.debug) {
        initializationDepth = initializationDepth.substr(0, initializationDepth.length - 2);
      }
      state = STATE_INITIALIZED;
    } else if (state < STATE_INITIALIZED && !fullClassName.match(RESOURCE_BUNDLE_PATTERN)) {
      trace("[WARN]", "Jangaroo Runtime: cyclic static initializer dependency in " + fullClassName);
    }
    return this;
  }

  internal function doInit() : void {
  }

  public function getQualifiedName() : String {
    // AS uses namespace notation (::) to separate package and class name,
    // so replace the last dot ('.') by a double-colon ('::'):
    return this.fullClassName.replace(/\.([^.]+)$/, "::$1");
  }

  public function toString() : String {
    return this.fullClassName;
  }
}
}
