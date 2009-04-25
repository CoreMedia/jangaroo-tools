package joo {
public class DynamicClassLoader extends joo.ClassLoader {

  private static function isEmpty(object : Object) : Boolean {
    for (var m:String in object) {
      return false;
    }
    return true;
  }


  public var classLoadTimeoutMS : int = 0,
             urlPrefix : String = "";
  private var loadCheckTimer : *,
              onCompleteCallback : Function;

  /**
   * Keep record of all classes whose dependencies still have to be loaded.
   */
  private var pendingDependencies : Array/*<ClassDeclaration>*/ = [];
  /**
   * false => pending
   * true => loading
   */
  private var pendingClassState : Object/*<String,Boolean>*/ = {};

  override internal function createClassDeclaration(packageDef : String, directives : Array, classDef : String, memberFactory : Function,
                                                    publicStaticMethodNames : Array):SystemClassDeclaration {
    var cd : ClassDeclaration = super.createClassDeclaration(packageDef, directives, classDef, memberFactory, publicStaticMethodNames) as ClassDeclaration;
    this.pendingDependencies.push(cd);
    if (delete this.pendingClassState[cd.fullClassName]) {
      if (this.debug) {
        trace("prepared class " + cd.fullClassName + ", removed from pending classes.");
      }
      if (this.onCompleteCallback) {
        this.loadPendingDependencies();
        if (this.loadCheckTimer) {
          window.clearTimeout(this.loadCheckTimer);
          this.loadCheckTimer = undefined;
        }
        if (isEmpty(this.pendingClassState)) {
          var onCompleteCallback : Function = this.onCompleteCallback;
          this.onCompleteCallback = null;
          // "invoke later":
          window.setTimeout(function() {
            this.completeAll();
            this.doCompleteCallback(onCompleteCallback);
          }, 0);
        } else if (this.classLoadTimeoutMS) {
          this.loadCheckTimer = window.setTimeout(this.throwMissingClassesError, this.classLoadTimeoutMS);
        }
      }
    }
    return cd;
  }

  private function throwMissingClassesError() : void {
    var sb : Array = [];
    for (var loading:String in this.pendingClassState) {
      if (this.pendingClassState[loading]) {
        sb.push(loading);
      }
    }
    if (sb.length>0) {
      throw new Error("The following classes were not loaded after "+this.classLoadTimeoutMS+" milliseconds: "+sb.join(", "));
    }
  }

  /**
   * Import the class given by its fully qualified class name (package plus name).
   * All imports are collected in a hash and can be used in the #complete() callback function.
   * Additionally, the DynamicClassLoader tries to load the class from a URL if it is not present on #complete().
   * @param fullClassName : String the fully qualified class name (package plus name) of the class to load and import.
   */
  public override function import_(fullClassName : String) : void {
    super.import_(fullClassName);
    this.load(fullClassName);
  }

  override public function run(mainClassName : String, ...args):void {
    this.load(mainClassName);
    super.run.apply(this,arguments);
  }

  private function load(fullClassName : String) : void {
    if (!this.getClassDeclaration(fullClassName)) {
      if (!this.onCompleteCallback) {
        if (this.pendingClassState[fullClassName]===undefined) {
          // we are not yet in completion phase: just add to pending classes:
          this.pendingClassState[fullClassName] = false;
          if (this.debug) {
            trace("added to pending classes: "+fullClassName+".");
          }
        }
      } else {
        if (this.pendingClassState[fullClassName]!==true) {
          // trigger loading:
          this.pendingClassState[fullClassName] = true;
          var url : String = this.getUri(fullClassName);
          joo.classLoader.loadScript(url);
          if (this.debug) {
            trace("triggered loading class "+fullClassName+" from URL "+url+".");
          }
        }
      }
    }
  }

  protected function getBaseUri() : String {
    var baseUri : String = window.document.location.href;
    return baseUri.substring(0, baseUri.lastIndexOf("/")+1) + this.urlPrefix;
  }

  protected function getUri(fullClassName : String) : String {
    var baseUri : String = this.getBaseUri();
    return baseUri + fullClassName.replace(/\./g as String,"/") + ".js";
  }

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
  public override function complete(onCompleteCallback : Function = undefined) : void {
    this.onCompleteCallback = onCompleteCallback || defaultOnCompleteCallback;
    this.loadPendingDependencies();
    if (isEmpty(this.pendingClassState)) {
      // no deferred classes: do not behave any different than my superclass
      super.complete(onCompleteCallback);
    } else {
      for (var c:String in this.pendingClassState) {
        this.load(c);
      }
    }
  }

  private static function defaultOnCompleteCallback() : void {
    trace("All classes loaded!");
  }

  private function loadPendingDependencies():void {
    for (var j:int=0; j<this.pendingDependencies.length; ++j) {
      var dependencies : Array = (this.pendingDependencies[j] as ClassDeclaration).getDependencies();
      for (var i:int=0; i<dependencies.length; ++i) {
        this.load(dependencies[i]);
      }
    }
    this.pendingDependencies = [];
  }
}
}
