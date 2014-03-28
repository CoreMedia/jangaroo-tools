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
"public class NativeClassDeclaration",1,function($$private){var trace=joo.trace;return[function(){joo.classLoader.init(Error);}, 

  "internal static const",{ RESOURCE_BUNDLE_PATTERN/*:RegExp*/ : /_properties$/},

  "internal static function createEmptyConstructor",function createEmptyConstructor(prototype_/* : Object*/)/* : Function*/ {
    var emptyConstructor/* : Function*/ = function joo$NativeClassDeclaration$24_39()/* : void*/ {};
    emptyConstructor.prototype =  prototype_;
    return emptyConstructor;
  },

  "internal static const",{ STATE_LOADED/* : int*/ : 0},
  "internal static const",{ STATE_COMPLETING/* : int*/ : 1},
  "internal static const",{ STATE_COMPLETED/* : int*/ : 2},
  "internal static const",{ STATE_INITIALIZING/* : int*/ : 3},
  "internal static const",{ STATE_INITIALIZED/* : int*/ : 4},

  "public var",{
          fullClassName/* : String*/:null,
          constructor_/* : Function*/:null,
          publicConstructor/* : Function*/:null,
          state/*  : int*/ :function(){return( joo.NativeClassDeclaration.STATE_LOADED);},
          Public/* : Function*/:null,
          superClassDeclaration/* : NativeClassDeclaration*/:null,
          Types/*: Class*/:null,
          interfaces/* : Array*/:null},

  "public function NativeClassDeclaration",function NativeClassDeclaration$() {this.state=this.state();
  },

  "public function create",function create(fullClassName/* : String*/, publicConstructor/* : Function*/)/* : NativeClassDeclaration*/ {
    this.fullClassName = fullClassName;
    this.publicConstructor = publicConstructor;
    if (fullClassName != "Error") { // setting expando properties on Error and querying them on TypeError crashes IE9 beta :-(
      try {
        this.publicConstructor["$class"] = this;
      } catch (e/*:**/) {
        // ignore that expando properties do not work with certain native objects in certain browsers, e.g. IE7 / XMLHttpRequest
      }
    }
    return this;
  },

  "public function complete",function complete()/* : NativeClassDeclaration*/ {
    if (this.state < joo.NativeClassDeclaration.STATE_COMPLETING ) {
      this.state = joo.NativeClassDeclaration.STATE_COMPLETING;
      this.doComplete();
      this.state = joo.NativeClassDeclaration.STATE_COMPLETED;
    }
    return this;
  },

  // built-in Error constructor called as function unfortunately always creates a new Error object, so we have to emulate it:
  "private static const",{ ERROR_CONSTRUCTOR/*:Function*/ : function joo$NativeClassDeclaration$71_53(message/*:String*/)/*:void*/ {
    this.message = message || "";
  }},

  "internal function doComplete",function doComplete()/* : void*/ {
    this.interfaces = [];
    if (this.publicConstructor !== Error) {
      this.constructor_ = this.publicConstructor;
    } else {
      this.constructor_ = $$private.ERROR_CONSTRUCTOR;
      this.constructor_.prototype = this.publicConstructor.prototype;
    }
    this.Public = joo.NativeClassDeclaration.createEmptyConstructor(this.publicConstructor.prototype);
    this.initTypes();
  },

  "protected function initTypes",function initTypes()/*:void*/ {
    this.Types = function joo$NativeClassDeclaration$88_13()/*:void*/ {};
    var types/*:Object*/ = this.Types.prototype = this.superClassDeclaration ? new this.superClassDeclaration.Types() : {};
    types[this.fullClassName] = true;
    for (var i/*:int*/ = 0; i < this.interfaces.length; i++) {
      types[this.interfaces[i]] = true;
    }
  },

  "private static var",{ initializationDepth/*:String*/ : ""},

  "public function init",function init()/* : NativeClassDeclaration*/ {
    if (this.state < joo.NativeClassDeclaration.STATE_INITIALIZING ) {
      this.complete();
      this.state = joo.NativeClassDeclaration.STATE_INITIALIZING;
      if (joo.classLoader.debug) {
        trace("[INFO] Jangaroo Runtime: initializing class " + $$private.initializationDepth + this.fullClassName);
        $$private.initializationDepth += "  ";
      }
      this.doInit();
      if (joo.classLoader.debug) {
        $$private.initializationDepth = $$private.initializationDepth.substr(0, $$private.initializationDepth.length - 2);
      }
      this.state = joo.NativeClassDeclaration.STATE_INITIALIZED;
    } else if (this.state === joo.NativeClassDeclaration.STATE_INITIALIZING && !this.fullClassName.match(joo.NativeClassDeclaration.RESOURCE_BUNDLE_PATTERN)) {
      trace("[WARN] Jangaroo Runtime: cyclic static initializer dependency in " + this.fullClassName);
    }
    return this;
  },

  "internal function doInit",function doInit()/* : void*/ {
  },

  "public function getQualifiedName",function getQualifiedName()/* : String*/ {
    // AS uses namespace notation (::) to separate package and class name:
    return this.fullClassName.replace(/\.([^\.]+)^/, "::");
  },

  "public function toString",function toString()/* : String*/ {
    return this.fullClassName;
  },
];},["createEmptyConstructor"],["Error"], "0.8.0", "0.8.1"
);