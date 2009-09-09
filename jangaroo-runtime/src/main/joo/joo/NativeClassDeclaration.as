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

// this makes jooc generate a with(joo) statement:
import joo.*;

public class NativeClassDeclaration {

  protected static function createEmptyConstructor(constructor_ : Function) : Function {
    var emptyConstructor : Function = function() : void {
      this.constructor = constructor_;
    };
    emptyConstructor.prototype =  constructor_.prototype;
    return emptyConstructor;
  }

  public var
          level : int = -1,
          fullClassName : String,
          constructor_ : Function,
          publicConstructor : Function,
          completed  : Boolean = false,
          inited  : Boolean = false,
          Public : Function,
          superClassDeclaration : NativeClassDeclaration,
          interfaces : Array;

  public function NativeClassDeclaration() {
  }

  public function create(fullClassName : String, publicConstructor : Function) : NativeClassDeclaration {
    this.fullClassName = fullClassName;
    this.publicConstructor = publicConstructor;
    this.publicConstructor["$class"] = this;
    return this;
  }

  public function complete() : NativeClassDeclaration {
    if (!this.completed) {
      this.completed = true;
      this.doComplete();
    }
    return this;
  }

  protected function doComplete() : void {
    this.interfaces = [];
    this.constructor_ = this.publicConstructor;
    this.Public = createEmptyConstructor(this.publicConstructor);
  }

  public function init() : NativeClassDeclaration {
    if (!this.inited) {
      this.inited = true;
      this.complete();
      this.doInit();
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
