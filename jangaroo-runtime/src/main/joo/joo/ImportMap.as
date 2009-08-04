package joo {

import joo.*;

internal class ImportMap {

  private var importsByName : Object;
  private var importedPackages : Array;

  internal function ImportMap() {
    this.importsByName = {};
    this.importedPackages = [""]; // always "import" top level package!
  }

  /**
   * Add a class to the list of imports.
   * If the unqualified class name if already contained in this ImportMap, the existing entry is
   * removed and the new entry is not added to avoid ambigiouties (AS3 standard).
   * @param fullClassName the fully qualified class name of the class to be imported.
   */
  internal function addImport(fullClassName : String) : void {
    var afterLastDotIndex : int = fullClassName.lastIndexOf(".") + 1;
    var packageName : String = fullClassName.substring(0, afterLastDotIndex);
    var className : String = fullClassName.substring(afterLastDotIndex);
    if (className == "*") {
      this.importedPackages.push(packageName);
    } else {
      if (className in this.importsByName && this.importsByName[className] != fullClassName) {
        delete this.importsByName[className]; // remove ambigious import
      } else {
        this.importsByName[className] = fullClassName;
      }
    }
  }

  internal function getImports() : Array/*<String>*/ {
    var imports : Array = [];
    for each (var im:String in this.importsByName) {
      imports.push(im);
    }
    return imports;
  }

  internal function findQualifiedName(className : String) : String {
    if (className.indexOf(".") < 0) {
      // not already qualified:
      var fqn : String = this.importsByName[className];
      if (fqn) {
        return fqn;
      }
      // check candidates resulting of *-imports:
      var packages : Array = this.importedPackages;
      for (var i:int = packages.length - 1; i >= 0; --i) {
        fqn = packages[i] + className;
        if (joo.classLoader.getClassDeclaration(fqn)) {
          return fqn;
        }
      }
    }
    return className;
  }

  internal function addToMap(map : Object) : Object {
    for (var im : String in this.importsByName) {
      map[im] = (joo.classLoader as SystemClassLoader).getRequiredClassDeclaration(this.importsByName[im]).publicConstructor;
    }
    return map;
  }

  internal function init() : void {
    for (var im : String in this.importsByName) {
      (joo.classLoader as SystemClassLoader).getRequiredClassDeclaration(this.importsByName[im]).init();
    }
  }

}
}