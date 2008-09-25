package joox.unit.extensions {

import joox.unit.framework.TestCase;
import joox.unit.framework.Assert;

/**
 * A TestCase that expects an exception of class mClass to be thrown.
 * The other way to check that an expected exception is thrown is:
 * <pre>
 * try {
 *   this.shouldThrow();
 * } catch (ex) {
 *   if (ex instanceof SpecialException)
 *     return;
 *   else
 *      throw ex;
 * }
 * joox.unit.framework.Assert.fail("Expected SpecialException");
 * </pre>
 *
 * To use ExceptionTestCase, create a TestCase like:
 * <pre>
 * new joox.unit.extensions.ExceptionTestCase("testShouldThrow", SpecialException);
 * </pre>
 */
public class ExceptionTestCase extends TestCase {

  public var mClass : Function;

  /**
   * The constructor is initialized with the name of the test and the expected
   * class to be thrown.
   * @param name The name of the test case.
   * @param clazz The class to be thrown.
   */
  public function ExceptionTestCase(name : String, clazz : Function) {
    super( name );
    /**
     * Save the class.
     * @type Function
     */
    this.mClass = clazz;
  }

  /**
   * Execute the test method expecting that an exception of
   * class mClass or one of its subclasses will be thrown
   */
  override public function runTest() : void {
    try {
      super.runTest( );
    } catch (ex : Error) {
      if (ex instanceof this.mClass)
        return;
      else
        throw ex;
    }
    Assert.fail("Expected exception " + this.mClass);
  }

}
}
