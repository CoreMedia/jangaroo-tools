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

public class ImportMap {

  private var importsByName : Object;
  private var importedPackages : Array;

  public function ImportMap() {
    this.importsByName = {};
    this.importedPackages = [""]; // always "import" top level package!
  }

  /**
   * Add a class to the list of imports.
   * If the unqualified class name if already contained in this ImportMap, the existing entry is
   * removed and the new entry is not added to avoid ambiguous imports (AS3 standard).
   * @param fullClassName the fully qualified class name of the class to be imported.
   */
  public function addImport(fullClassName : String) : void {
    var afterLastDotIndex : int = fullClassName.lastIndexOf(".")+1;
    var packageName : String = fullClassName.substring(0,afterLastDotIndex);
    var className : String = fullClassName.substring(afterLastDotIndex);
    if (className == "*") {
      this.importedPackages.push(packageName);
    } else {
      if (className in this.importsByName && this.importsByName[className]!=fullClassName) {
        delete this.importsByName[className]; // remove ambiguous import
      } else {
        this.importsByName[className] = fullClassName;
      }
    }
  }

  public function getImports() : Array/*<String>*/ {
    var imports : Array = [];
    for each (var im:String in this.importsByName) {
      imports.push(im);
    }
    return imports;
  }

  public function findQualifiedName(className : String) : String {
    if (className.indexOf(".") < 0) {
      // not already qualified:
      var fqn : String = this.importsByName[className];
      if (fqn) {
        return fqn;
      }
      // check candidates resulting of *-imports:
      var packages : Array = this.importedPackages;
      for (var i:int= packages.length-1; i >= 0; --i) {
        fqn = packages[i] + className;
        if (classLoader.getClassDeclaration(fqn)) {
          return fqn;
        }
      }
    }
    return className;
  }

  public function addToMap(map : Object) : Object {
    for (var im : String in this.importsByName) {
      const classDeclaration:NativeClassDeclaration = classLoader.getClassDeclaration(this.importsByName[im]);
      if (classDeclaration) {
        map[im] = classDeclaration.publicConstructor;
      } // else it may be an unused and thus not loaded import.
    }
    return map;
  }

  public function init() : void {
    for (var im : String in this.importsByName) {
      classLoader.getRequiredClassDeclaration(this.importsByName[im]).init();
    }
  }

}
}