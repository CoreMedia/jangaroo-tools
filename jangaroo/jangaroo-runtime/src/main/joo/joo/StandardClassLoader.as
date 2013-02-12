package joo {

/**
 * @deprecated use <code>joo.loadClasses()</code>.
 * @see joo.#loadClasses()
 */
public class StandardClassLoader {

  private var imports:Array;

  /**
   * @deprecated use <code>joo.loadClasses()</code>.
   * @see joo.#loadClasses()
   */
  public function StandardClassLoader() {
    imports = [];
  }

  /**
   * @deprecated use <code>joo.loadClasses()</code>.
   * Import the class given by its fully qualified class name (package plus name).
   * All imports are collected in a hash and can be used in the #complete() callback function.
   * @param fullClassName : String the fully qualified class name (package plus name) of the class to load and import.
   * @see joo.#loadClasses()
   */
  public function import_(fullClassName : String) : void {    
    imports.push(fullClassName);
  }

  /**
   * Run the static main method of a class given by its fully qualified name (package plus name), handing through the
   * given arguments (args).
   * The main method is executed after all classes are completed (see #complete()).
   * @param mainClassName : String the fully qualified name (package plus name) or the constructor function
   *   of the class to run.
   * @param args the arguments to hand over to the main method of the given class.
   */
  public function run(mainClassName : String, ...args) : void {
    loadClass(mainClassName, function(mainClass:Class):void {
      mainClass["main"].apply(null, args);
    });
  }

  //noinspection JSMethodCanBeStatic
  /**
   * @deprecated
   * Explicitly initializing the static members of the given class (constructor function) is no longer
   * needed in Jangaroo 3.
   * @param classes the classes (type Function/Class) to initialize.
   * @return the last initialized class (constructor function). It only makes sense to use the return value
   *   if you use this method with exactly one parameter.
   */
  public function init(... classes) : Function {
    return classes[classes.length - 1];
  }

  /**
   * @deprecated use <code>joo.loadClasses()</code>.
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
   * @see joo.#loadClasses()
   */
  public function complete(onCompleteCallback : Function = undefined) : void {
    var imports:Array = this.imports;
    this.imports = [];
    loadClasses(imports, function():void {
      if (onCompleteCallback) {
        var importMap:Object = {};
        for (var i:int = 0; i < imports.length; i++) {
          var qName:String = imports[i];
          var name:String = qName.match(/[^.]+$/)[0];
          importMap[name] = arguments[i];
        }
        onCompleteCallback(importMap);
      }
    });
  }

}
}