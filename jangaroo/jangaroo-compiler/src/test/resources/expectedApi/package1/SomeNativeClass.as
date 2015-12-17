package package1 {

/**
 * This is an example of an API-only class ("native API stub").
 */
[Native("SomeNativeClass")]
public class SomeNativeClass extends package1.SomeNativeSuperClass {
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
}
}