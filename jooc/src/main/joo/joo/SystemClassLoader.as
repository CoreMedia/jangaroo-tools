package joo {

import joo.*;

public class SystemClassLoader {

{
  joo.classLoader = new SystemClassLoader();
}

  public static const classDeclarationsByName : Object/*<String,SystemClassDeclaration>*/ = {};

  public var debug : Boolean = true;

  public function SystemClassLoader() {    
  }

  public function prepare(packageDef : String, directives : Array, classDef : String, memberFactory : Function,
                          publicStaticMethodNames : Array) : void {
    var cd : SystemClassDeclaration = this.createClassDeclaration(packageDef, directives, classDef, memberFactory, publicStaticMethodNames);
    classDeclarationsByName[cd.fullClassName] = cd;
  }

  internal function createClassDeclaration(packageDef : String, directives : Array, classDef : String, memberFactory : Function,
                          publicStaticMethodNames : Array) : SystemClassDeclaration {
    return new SystemClassDeclaration(packageDef, directives, classDef, memberFactory, publicStaticMethodNames).init()
            as SystemClassDeclaration;
  }

  public function getClassDeclaration(fullClassName : String) : NativeClassDeclaration {
    var cd : NativeClassDeclaration = classDeclarationsByName[fullClassName];
    if (!cd) {
      var constructor_ : Class = joo.getQualifiedObject(fullClassName);
      if (constructor_) {
        if (!constructor_.$class) {
          // create SystemClassDeclaration for native classes:
          cd = this.createNativeClassDeclaration(fullClassName, constructor_).init();
          classDeclarationsByName[fullClassName] = cd;
        } else {
          cd = constructor_.$class;
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

  protected function createNativeClassDeclaration(fullClassName : String, nativeClass : Class) : NativeClassDeclaration {
    return new NativeClassDeclaration(fullClassName, nativeClass);
  }

}
}