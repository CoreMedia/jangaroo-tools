package package1 {

/**
 * This is an example of an API-only class ("native API stub").
 */
[Native(amd = "acme/native", global)]
public class SomeNativeClass {
  /**
   * Some constructor doc.
   */
  public function SomeNativeClass() {
    super();
  }

  /**
   * Some static method doc.
   */
  public static native function foo();

  /**
   * Some method doc.
   */
  public native function bar();

  [Native(accessor)]
  public native function get baz():String;

  [Native(accessor)]
  public native function set baz(value:String):void;
}
}