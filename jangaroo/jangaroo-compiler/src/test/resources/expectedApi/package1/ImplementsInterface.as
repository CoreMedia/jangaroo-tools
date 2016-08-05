package package1 {
import ext.Panel;

[Uses("Object")]
[Uses("package1.Interface")]
[Uses("Vector$object")]
[Uses("ext.Panel")]
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
  public var bar:Vector.<Vector.<Panel>>;

  public function ImplementsInterface() {
    super();
  }

  public native function doSomething():String;

  public native function get property():String;
}
}