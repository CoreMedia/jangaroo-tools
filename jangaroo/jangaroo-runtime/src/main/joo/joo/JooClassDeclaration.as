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
  /**
   * The declaration of the class this class extends (its superclass declaration).
   */
  public native function get superClassDeclaration():NativeClassDeclaration;

  public native function getMemberDeclaration(namespace:String, memberName:String):MemberDeclaration;

  public function JooClassDeclaration(packageName:String, name:String, extends_:Class, implements_:Array,
                                      metadata:Object) {
    super(packageName, name);
  }

}
}