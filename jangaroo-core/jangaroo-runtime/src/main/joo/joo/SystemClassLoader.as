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

public class SystemClassLoader {

{
  classLoader = new SystemClassLoader();
}

  public static const classDeclarationsByName : Object/*<String,SystemClassDeclaration>*/ = {};

  public var debug : Boolean = false;

  public function SystemClassLoader() {    
  }

  public function prepare(...params) : SystemClassDeclaration {
    var packageDef : String = params[0];
    var metadata : Object = {};
    for (var i:int = 1; typeof params[i] == "object"; i++) {
      addToMetadata(metadata, params[i]);
    }
    var classDef : String = params[i++];
    var memberFactory : Function = params[i++];
    var publicStaticMethodNames : Array = params[i++];
    var dependencies : Array = params[i++];
    var runtimeApiVersion:String = params[i++];
    var compilerVersion:String = params[i++];
    var cd : SystemClassDeclaration = this.createClassDeclaration(packageDef, classDef, memberFactory, publicStaticMethodNames, dependencies);
    cd.metadata = metadata;
    if (runtimeApiVersion !== joo.runtimeApiVersion) {
      throw new Error("Runtime version " + joo.runtimeApiVersion + " and class version " + runtimeApiVersion
        + " of " + cd.fullClassName + " do not match. "
        + "Please recompile with the correct compiler version or replace jangaroo-runtime[-debug].js.");
    }
    classDeclarationsByName[cd.fullClassName] = cd;
    return cd;
  }

  static function addToMetadata(metadata:Object, annotation:*):void {
    for (var m:String in annotation) {
      metadata[m] = annotation[m];
    }
  }

  protected function createClassDeclaration(packageDef : String, classDef : String, memberFactory : Function,
                          publicStaticMethodNames : Array, dependencies : Array) : SystemClassDeclaration {
    return new SystemClassDeclaration(packageDef, classDef, memberFactory, publicStaticMethodNames).init()
            as SystemClassDeclaration;
  }

  public function getClassDeclaration(fullClassName : String) : NativeClassDeclaration {
    var cd : NativeClassDeclaration = classDeclarationsByName[fullClassName];
    if (!cd) {
      var constructor_ : Function = getQualifiedObject(fullClassName);
      if (constructor_) {
        if (!constructor_["$class"]) {
          // create SystemClassDeclaration for native classes:
          cd = this.createNativeClassDeclaration(fullClassName, constructor_).init();
          classDeclarationsByName[fullClassName] = cd;
        } else {
          cd = constructor_["$class"];
        }
      }
    }
    return cd;
  }

  /**
   * @param className
   * @return NativeClassDeclaration the class declaration with the given name.
   * @throws Error - ClassNotFound
   */
  public function getRequiredClassDeclaration(className : String) : NativeClassDeclaration {
    var cd : NativeClassDeclaration = this.getClassDeclaration(className);
    if (!cd) {
      throw new Error("Class not found: "+className);
    }
    return cd;
  }

  protected function createNativeClassDeclaration(fullClassName : String, nativeClass : Function) : NativeClassDeclaration {
    return new NativeClassDeclaration().create(fullClassName, nativeClass);
  }

  public function init(... classes) :Function {
    return null;
  }
}
}