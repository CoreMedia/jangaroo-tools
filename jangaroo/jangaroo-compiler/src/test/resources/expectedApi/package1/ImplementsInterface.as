package package1 {

[Uses("package1.Interface")]
public final class ImplementsInterface implements package1.Interface {
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
    super();
  }

  public native function doSomething():void;
}
}