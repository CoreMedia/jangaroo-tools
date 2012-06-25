package package1 {

/**
 * This is an example of an API-only class ("native API stub").
 */
[Native]
public class SomeNativeClass {

  /**
   * Some constructor doc.
   */
  public function SomeNativeClass() {
  }

  // This comment must disappear!
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