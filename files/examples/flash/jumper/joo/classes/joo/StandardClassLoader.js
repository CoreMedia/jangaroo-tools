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

"public class StandardClassLoader extends joo.SystemClassLoader",2,function($$private){;return[ 

  "private static var",{ classDeclarations/* : Array*/ :function(){return( []);}},

  "private var",{ imports/* : Array*/:null},

  "public function StandardClassLoader",function StandardClassLoader$() {this.super$2();
    this.imports$2 = [];
  },

  "override protected function createClassDeclaration",function createClassDeclaration(packageDef/* : String*/, classDef/* : String*/, inheritanceLevel/* : int*/, memberFactory/* : Function*/,
                                                  publicStaticMethodNames/* : Array*/, dependencies/* : Array*/)/*:JooClassDeclaration*/ {
    var cd/* : JooClassDeclaration*/ = new joo.JooClassDeclaration(packageDef, classDef, inheritanceLevel, memberFactory, publicStaticMethodNames, dependencies);
    $$private.classDeclarations.push(cd); // remember all created classes for later initialization.
    return cd;
  },

  /**
   * Import the class given by its fully qualified class name (package plus name).
   * All imports are collected in a hash and can be used in the #complete() callback function.
   * @param fullClassName : String the fully qualified class name (package plus name) of the class to load and import.
   */
  "public function import_",function import_(fullClassName/* : String*/)/* : void*/ {    
    this.imports$2.push(fullClassName);
  },

  /**
   * Run the static main method of a class given by its fully qualified name (package plus name), handing through the
   * given arguments (args).
   * The main method is executed after all classes are completed (see #complete()).
   * @param mainClassName : String the fully qualified name (package plus name) or the constructor function
   *   of the class to run.
   * @param args the arguments to hand over to the main method of the given class.
   */
  "public function run",function run(mainClassName/* : String, ...args*/)/* : void*/ {var this$=this;var args=Array.prototype.slice.call(arguments,1);
    this.complete(function joo$StandardClassLoader$55_19()/* : void*/ {
      var mainClass/* : NativeClassDeclaration*/ = this$.getRequiredClassDeclaration(mainClassName).init();
      mainClass.constructor_["main"].apply(null,args);
    });
  },

  /**
   * Explicitly initialize the static members of the given class (constructor function).
   * If the class is not yet initialized, Initializers of static variables and static code blocks are called (once).
   * This is only necessary when you want to access constants of a class without importing the class,
   * or when you have loaded the class explicitly and want its static code to execute.
   * Explicit initializing is <i>not</i> necessary when you
   * - import the class or
   * - load the class and use the constructor or a static method of the class. This will trigger initialization
   *   automatically.
   * @param classes the classes (type Function) to initialize.
   * @return Function the last initialized class (constructor function). It only makes sense to use the return value
   *   if you use this method with exactly one parameter.
   */
  "public override function init",function init(/*... classes*/)/* : Function*/ {var classes=arguments;
    var clazz/* : Function*/;
    for (var i/*:int*/ =0; i<classes.length; ++i) {
      if ("$class" in classes[i]) {/*
        joo.NativeClassDeclaration*/((clazz = classes[i])["$class"]).init(); // cannot use "as NativeClassDeclaration", because Class.js only simulates that class :-(
      }
    }
    return clazz;
  },

  /**
   * Tell Jangaroo to load and initialize all required classes, then call the given function.
   * The function receives an import hash, which can be used in pure JavaScript in a 'with' statement
   * (Jangaroo does not support 'with', there, you would use import declarations!) like this:
   * <pre>
   * joo.classLoader.import_("com.custom.Foo");
   * joo.classLoader.complete(function(imports){with(imports){
   *   Foo.doSomething("bar");
   * }});
   * </pre>
   * @param onCompleteCallback : Function
   * @return void
   */
  "public function complete",function complete(onCompleteCallback/* : Function = undefined*/)/* : void*/ {
    this.completeAll();
    if (onCompleteCallback) {
      this.doCompleteCallbacks([onCompleteCallback]);
    }
  },

  "protected function completeAll",function completeAll()/* : void*/ {
    for (var i/*:int*/ = 0; i < $$private.classDeclarations.length; i++) {
      var classDeclaration/*:JooClassDeclaration*/ = $$private.classDeclarations[i];
      classDeclaration.complete();
      // init native class patches immediately:
      if (classDeclaration.isNative()) {
        classDeclaration.init();
      }
    }
  },

  "protected function doCompleteCallbacks",function doCompleteCallbacks(onCompleteCallbacks/* : Array*//*Function*/)/* : void*/ {
    if (onCompleteCallbacks.length) {
      var importMap/* : Object*/ = {};
      for (var j/*:int*/ = 0; j < this.imports$2.length; j++) {
        var fullClassName/*:String*/ = this.imports$2[j];
        var className/* : String*/ = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        importMap[className] = joo.classLoader.getRequiredClassDeclaration(fullClassName).init().constructor_;
      }
      for (var i/*:int*/ = 0; i < onCompleteCallbacks.length; ++i) {
        onCompleteCallbacks[i](importMap);
      }
    }
  },
];},[],["joo.SystemClassLoader","joo.JooClassDeclaration","joo.NativeClassDeclaration"], "0.8.0", "0.8.2-SNAPSHOT"
);