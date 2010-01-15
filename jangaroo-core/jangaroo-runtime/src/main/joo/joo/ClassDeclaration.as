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

public class ClassDeclaration extends joo.SystemClassDeclaration {

  private var importMap : ImportMap;

  public function ClassDeclaration(packageDef:String, directives : Array, classDef:String, memberDeclarations:Function,
          publicStaticMethods : Array) {
    super(packageDef, directives, classDef, memberDeclarations, publicStaticMethods);
  }

  public function getDependencies() : Array {
    var dependencies:Array = this.importMap.getImports();
    dependencies.push(this.importMap.findQualifiedName(this.extends_));
    return dependencies;
  }

  override protected function parseDirectives(packageName : String, directives : Array):void {
    // super.parseDirectives(packageName, directives); // we know it's empty!
    this.importMap = new ImportMap();
    this.importMap.addImport(packageName+".*");
    directives.forEach(this.parseDirective);
  }

  private function parseDirective(directive : String) : void {
    var importMatch : Array = directive.match(/^\s*import\s+(([a-zA-Z$_0-9]+\.)*(\*|[a-zA-Z$_0-9]+))\s*$/) as Array;
    if (importMatch) {
      this.importMap.addImport(importMatch[1]);
    }
    // else: TODO! use namespace, annotations, package-scope functions, namespace declarations...
  }

  override protected function doComplete():void {
    this.extends_ = this.importMap.findQualifiedName(this.extends_);
    super.doComplete();
    for (var i:int=0; i<this.interfaces.length; ++i) {
      this.interfaces[i] = this.importMap.findQualifiedName(this.interfaces[i]);
    }
    this.importMap.addToMap(this.privateStatics);
    createInitializingConstructor(this);
    this.publicStaticMethodNames.forEach(this.createInitializingStaticMethod);
  }

  private static function createInitializingConstructor(classDeclaration : ClassDeclaration) : void {
    // anonymous function has to be inside a static function, or jooc will add ".bind(this)":
    classDeclaration.constructor_ = function() : void {
      classDeclaration.init();
      assert(classDeclaration.constructor_!=null); // must have been set, at least to a default constructor!
      classDeclaration.constructor_.apply(this, arguments);
    };
  }

  private function createInitializingStaticMethod(methodName : String) : void {
    var classDeclaration : ClassDeclaration = this;
    classDeclaration.publicConstructor[methodName] = function() : * {
      //assert(!classDeclaration.inited);
      classDeclaration.init();
      return classDeclaration.publicConstructor[methodName].apply(null, arguments);
    };
  }

  private function deleteInitializingStaticMethod(methodName : String) : void {
    delete this.publicConstructor[methodName];
  }

  protected override function doInit():void {
    this.publicStaticMethodNames.forEach(this.deleteInitializingStaticMethod);
    super.doInit();
    this.interfaces.forEach(function(interface_ : String, i : uint, interfaces : Array) : void {
      interfaces[i] = classLoader.getRequiredClassDeclaration(interface_);
      interfaces[i].init();
    });
  }

  /**
   * Determines if the specified <code>Object</code> is assignment-compatible
   * with the object represented by this <code>ClassDefinition</code>.
   * The method returns <code>true</code> if the specified
   * <code>Object</code> argument is non-null and can be cast to the
   * reference type represented by this <code>Class</code> object without
   * raising a <code>ClassCastException.</code> It returns <code>false</code>
   * otherwise.
   */
  public override function isInstance(object : Object) : Boolean {
    return typeof object == "object" && object.constructor["$class"] ? this.isAssignableFrom(object.constructor["$class"]) : false;
  }

  /**
   * Determines if the class or interface represented by this
   * <code>ClassDefinition</code> object is either the same as, or is a super class or
   * super interface of, the class or interface represented by the specified
   * <code>ClassDefinition</code> parameter. It returns <code>true</code> if so;
   * otherwise it returns <code>false</code>.
   */
  protected function isAssignableFrom(cd : NativeClassDeclaration) : Boolean {
    do {
      if (this===cd) {
        return true;
      }
      // TODO: optimize: pre-calculate set of all implemented interfaces of a class!
      if (this.isInterface) {
        // I am an interface: search all implemented interfaces recursively:
        if (cd.interfaces.some(this.isAssignableFrom)) {
          return true;
        }
      }
      cd = cd.superClassDeclaration;
    } while(cd);
    return false;
  }

}
}