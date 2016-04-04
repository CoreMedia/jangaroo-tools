package package1{

public final class ImplementsInterface implements Interface {

  /**
   * Field with ASDoc.
   * Second line.
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

  public function doSomething():String {
    // also nothing
  }

  public function get property():String {
    return "prefix" + foo;
  }
}
}