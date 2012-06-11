package package1{

public final class ImplementsInterface implements Interface {

  /**
   * Field with ASDoc.
   */
  public var foo;
  
  /**
   * Annotated field with ASDoc.
   */
  [Bar]
  public var bar:Vector.<Vector.<String>>;

  public function ImplementsInterface() {
    // nothing really
  }

  public function doSomething():void {
    // also nothing
  }

}
}