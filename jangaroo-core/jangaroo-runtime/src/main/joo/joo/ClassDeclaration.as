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

public class ClassDeclaration extends SystemClassDeclaration {

  private var dependencies : Array;

  public function ClassDeclaration(packageDef:String, classDef:String, memberDeclarations:Function,
          publicStaticMethods : Array, dependencies : Array) {
    super(packageDef, classDef, memberDeclarations, publicStaticMethods);
    this.dependencies = dependencies;
  }

  public function getDependencies() : Array {
    return this.dependencies;
  }

  override protected function doComplete():void {
    super.doComplete();
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