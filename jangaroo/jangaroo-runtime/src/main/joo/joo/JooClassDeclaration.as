package joo {

[Native(amd)]
public class JooClassDeclaration extends NativeClassDeclaration {

  /**
   * The metadata (annotations) of this class.
   */
  public native function get metadata():Object;
  /**
   * The class this class extends (its superclass).
   */
  public native function get extends_():Class;
  /**
   * The list of interfaces this class implements.
   */
  public native function get implements_():Array;

  public function JooClassDeclaration(name:String, packageName:String, extends_:Class, implements_:Array,
                                      metadata:Object) {
    super(packageName, name);
  }

}
}