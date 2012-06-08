package package1 {

public final class ImplementsInterface implements Interface {
  /**
   * Field with ASDoc.
   */
  public var foo;

  public function ImplementsInterface() {
    super();
  }

  public native function doSomething():void;
}
}