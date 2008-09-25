package joox.unit.framework {

import joox.unit.framework.ComparisonFailure;
import joox.unit.util.CallStack;
import joox.unit.framework.AssertionFailedError;

/**
 * A set of assert methods.
 */
public class Assert {

  /**
   * Static methods only.
   */
  public function Assert() {
  }

  /**
   * Asserts that two values are equal.
   * @param msg An optional error message.
   * @param expected The expected value.
   * @param actual The actual value.
   * @exception AssertionFailedError Thrown if the expected value is not the
   * actual one.
   */
  public static function assertEquals(msg : String, expected : Object, actual : Object) : void {
    if (arguments.length == 2) {
      actual = expected;
      expected = msg;
      msg = null;
    }
    if (expected instanceof RegExp && typeof actual == "string") {
      if (!actual.match(expected))
        fail("RegExp:<" + expected + "> did not match:<" + actual + ">");
    } else if (expected != actual) {
      if (typeof expected == "string" && typeof actual == "string")
        throw new ComparisonFailure(msg, expected, actual, new CallStack());
      else
        fail("Expected:<" + expected + ">, but was:<" + actual + ">",
          new CallStack(), msg);
    }
  }

  /**
   * Asserts that a condition is false.
   * @param msg An optional error message.
   * @param cond The condition to evaluate.
   * @exception AssertionFailedError Thrown if the evaluation was not false.
   */
  public static function assertFalse(msg : String, cond : String) : void {
    if (arguments.length == 1) {
      cond = msg;
      msg = null;
    }
    if (eval(cond))
      fail("Condition should have failed \"" + cond + "\"",
        new CallStack(), msg);
  }

  /**
   * Asserts that an object is not null.
   * @param msg An optional error message.
   * @param object The valid object.
   * @exception AssertionFailedError Thrown if the object is not null.
   */
  public static function assertNotNull(msg : String, object : Object) : void {
    if (arguments.length == 1) {
      object = msg;
      msg = null;
    }
    if (object === null)
      fail("Object was null.", new CallStack(), msg);
  }

  /**
   * Asserts that two values are not the same.
   * @param msg An optional error message.
   * @param expected The expected value.
   * @param actual The actual value.
   * @exception AssertionFailedError Thrown if the expected value is not the
   * actual one.
   */
  public static function assertNotSame(msg : String, expected : Object, actual : Object) : void {
    if (arguments.length == 2) {
      actual = expected;
      expected = msg;
      msg = null;
    }
    if (expected === actual)
      fail("Not the same expected:<" + expected + ">",
        new CallStack(), msg);
  }

  /**
   * Asserts that an object is not undefined.
   * @param msg An optional error message.
   * @param object The defined object.
   * @exception AssertionFailedError Thrown if the object is undefined.
   */
  public static function assertNotUndefined(msg : String, object : Object) : void {
    if (arguments.length == 1) {
      object = msg;
      msg = null;
    }
    if (object === undefined)
      fail("Object <" + object + "> was undefined."
        , new CallStack(), msg);
  }

  /**
   * Asserts that an object is null.
   * @param msg An optional error message.
   * @param object The null object.
   * @exception AssertionFailedError Thrown if the object is not null.
   */
  public static function assertNull(msg : String, object : Object) : void {
    if (arguments.length == 1) {
      object = msg;
      msg = null;
    }
    if (object !== null)
      fail("Object <" + object + "> was not null.",
           new CallStack(), msg);
  }

  /**
   * Asserts that two values are the same.
   * @param msg An optional error message.
   * @param expected The expected value.
   * @param actual The actual value.
   * @exception AssertionFailedError Thrown if the expected value is not the
   * actual one.
   */
  public static function assertSame(msg : String, expected : Object, actual : Object) : void {
    if (arguments.length == 2) {
      actual = expected;
      expected = msg;
      msg = null;
    }
    if (expected !== actual)
      fail("Same expected:<" + expected + ">, but was:<" + actual + ">",
        new CallStack(), msg);
  }

  /**
   * Asserts that a condition is true.
   * @param msg An optional error message.
   * @param cond The condition to evaluate.
   * @exception AssertionFailedError Thrown if the evaluation was not true.
   */
  public static function assertTrue(msg : String, cond : String) : void {
    if (arguments.length == 1) {
      cond = msg;
      msg = null;
    }
    if (!eval(cond))
      fail("Condition failed \"" + cond + "\"", new CallStack(), msg);
  }

  /**
   * Asserts that an object is undefined.
   * @param msg An optional error message.
   * @param object The undefined object.
   * @exception AssertionFailedError Thrown if the object is not undefined.
   */
  public static function assertUndefined(msg : String, object : Object) : void {
    if (arguments.length == 1) {
      object = msg;
      msg = null;
    }
    if (object !== undefined)
      fail("Object <" + object + "> was not undefined.",
           new CallStack(), msg);
  }

  /**
   * Fails a test with a give message.
   * @param msg The error message.
   * @param stack The call stack of the error.
   * @param usermsg The message part of the user.
   * @exception AssertionFailedError Is always thrown.
   */
  public static function fail(msg : String, stack : CallStack, usermsg : String) : void {
    var afe : AssertionFailedError = new AssertionFailedError(
      (usermsg ? usermsg + " " : "" ) + msg, stack);
    throw afe;
  }

}
}
