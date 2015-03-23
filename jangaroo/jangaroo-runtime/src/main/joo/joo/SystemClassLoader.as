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

  public var debug:Boolean;

  //noinspection JSMethodCanBeStatic
  public function getClassDeclaration(fullClassName:String):NativeClassDeclaration {
    try {
      var constructor_:Function = getQualifiedObject(fullClassName) as Function;
      if (constructor_) {
        var classDeclaration:NativeClassDeclaration = constructor_["$class"];
        if (!classDeclaration) {
          classDeclaration = new NativeClassDeclaration(fullClassName, constructor_);
          try {
            constructor_["$class"] = classDeclaration;
          } catch (e:*) {
            // Some built-in constructors cannot cope with expando properties.
            // Ignore; NativeClassDeclaration will be recreated every time.
          }
        }
        return classDeclaration;
      }
    } catch (e:*) {
      return null;
    }
  }

  public function getRequiredClassDeclaration(className:String):NativeClassDeclaration {
    var cd:NativeClassDeclaration = this.getClassDeclaration(className);
    if (!cd) {
      throw new Error("Class not found: " + className);
    }
    return cd;
  }

  public function init(...classes):Function {
    return null;
  }
}
}
