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
  // must use qualified name, otherwise a global property would be created:
  joo.classLoader = new SystemClassLoader();
}

  public static const classDeclarationsByName : Object/*<String,SystemClassDeclaration>*/ = {};

  public var debug : Boolean = false;

  public function SystemClassLoader() {    
  }

  public function prepare(packageDef : String, directives : Array, classDef : String, memberFactory : Function,
                          publicStaticMethodNames : Array, dependencies : Array) : void {
    var cd : SystemClassDeclaration = this.createClassDeclaration(packageDef, directives, classDef, memberFactory, publicStaticMethodNames, dependencies);
    classDeclarationsByName[cd.fullClassName] = cd;
  }

  protected function createClassDeclaration(packageDef : String, directives : Array, classDef : String, memberFactory : Function,
                          publicStaticMethodNames : Array, dependencies : Array) : SystemClassDeclaration {
    return new SystemClassDeclaration(packageDef, directives, classDef, memberFactory, publicStaticMethodNames).init()
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
   * @return
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