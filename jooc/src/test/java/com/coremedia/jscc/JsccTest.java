/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

/**
 * Some basic test cases for JangarooScript compiler and runtime correctness.
 *
 * @author Andreas Gawecki
 */

public class JsccTest extends com.coremedia.jscc.test.JsccRuntimeTestCase {

  public JsccTest(String name) {
    super(name);
  }


  public void testIdentityMethod() throws Exception {
    loadClass("package1.TestMethodCall");
    expectInt(43, "package1.TestMethodCall_().s(43)");
    eval("obj = new package1.TestMethodCall();");
    expectInt(43, "obj.m(43)");
  }

  public void testInheritance() throws Exception {
    loadClass("package1.TestInheritanceSuperClass");
    loadClass("package1.TestInheritanceSubClass");
    loadClass("package1.TestInheritanceSubSubClass");
    eval("obj1 = new package1.TestInheritanceSuperClass(1);");
    eval("obj2 = new package1.TestInheritanceSubClass(11, 2);");
    eval("obj3 = new package1.TestInheritanceSubSubClass(111, 22, 3);");
    expectInt(1, "obj1.getSlot1()");
    expectInt(11, "obj2.getSlot1()");
    expectInt(111, "obj3.getSlot1()");
    expectInt(2, "obj2.getSlot2()");
    expectInt(22, "obj3.getSlot2()");
    expectInt(3, "obj3.getSlot3()");
    expectInt(1, "obj1.m()");
    expectDouble(12, "obj2.m()");
    expectDouble(113, "obj3.m()");
  }

  public void testStaticInitializer() throws Exception {
    loadClass("package2.TestStaticInitializer");
    expectString("s1", "package2.TestStaticInitializer_().s1");
    expectString("s2/s1", "package2.TestStaticInitializer_().s2");
    expectString("s3/s2/s1", "package2.TestStaticInitializer_().s3");
    expectDouble(10, "package2.TestStaticInitializer_().fv");
  }

  public void testLocalVariables() throws Exception {
    loadClass("package1.TestLocalVariables");
    eval("obj = new package1.TestLocalVariables();");
    expectDouble(200, "obj.m(10)");
    expectDouble(134, "obj.m2(10)");
  }

  public void testStatements() throws Exception {
    loadClass("package2.TestStatements");
    eval("obj = new package2.TestStatements;");
    expectInt(200, "obj.testIf(true, 200, 300)");
    expectInt(300, "obj.testIf(false, 200, 300)");
    expectInt(200, "obj.testIfThenElse(true, 200, 300)");
    expectInt(300, "obj.testIfThenElse(false, 200, 300)");
    expectDouble(15, "obj.testWhile(5)");
    expectDouble(10, "obj.testFor(5)");
    expectDouble(15, "obj.testDoWhile(5)");
    expectString("x, y, z", "obj.testForIn({ y: 2, x :1, z: 3})");
    expectInt(11, "obj.testSwitch(1,1,11,2,22,33)");
    expectInt(22, "obj.testSwitch(2,1,11,2,22,33)");
    expectInt(33, "obj.testSwitch(3,1,11,2,22,33)");
    expectString("undefined", "typeof(obj.testReturnVoid())");
    expectInt(42, "var o = { tobedeleted: 42 }; o.tobedeleted");
    expectInt(42, "var o2 = { tobedeleted: 42 }; o2.tobedeleted");
    expectString("undefined", "obj.testDelete1(o); typeof(o.tobedeleted)");
    expectString("undefined", "obj.testDelete2(o2, 'tobedeleted'); typeof(o.tobedeleted)");
  }

  public void testExpressions() throws Exception {
    loadClass("package2.TestExpressions");
    eval("obj = new package2.TestExpressions;");
    expectInt(200, "obj.testCond(true, 200, 300)");
    expectInt(300, "obj.testCond(false, 200, 300)");
    expectDouble(1, "obj.antitestRegexpLiterals()");
    expectDouble(24, "obj.testParenExpr(11)");
    expectDouble(7, "obj.testBinOpExpr(12)");
    expectDouble(130, "(obj.testFunExpr(13))(10)");
    expectDouble(-2, "obj.testPrefixOpExpr(14)");
    expectDouble(30, "obj.testPostfixOpExpr(15)");
    String s = "'¤\\\b\t\n\f\r\'/'\u00C6\u01bfe\"'";
    expectString(s, "obj.testStringLiterals()");
    expectString(s, "obj.testCharLiterals()");
    expectString("2,7,2,2,2", "obj.testRegexpLiterals()");
    expectDouble(123+456, "obj.testObjectLiterals()");
    expectString("1,2,3,4,5,6,7,8,9,0", "obj.testArrayLiterals()");
  }

  public static void main(String args[]) {
    junit.textui.TestRunner.run(JsccTest.class);
  }

}
