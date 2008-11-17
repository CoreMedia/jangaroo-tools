/*
 * Copyright 2008 CoreMedia AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 */

package net.jangaroo.test.integration;

/**
 * Some basic test cases for JangarooScript compiler and runtime correctness.
 *
 * @author Andreas Gawecki
 */

public class JooTest extends JooRuntimeTestCase {

  public JooTest(String name) {
    super(name);
  }


  public void testIdentityMethod() throws Exception {
    loadClass("package1.TestMethodCall");
    initClass("package1.TestMethodCall");
    expectNumber(43, "package1.TestMethodCall.s(43)");
    eval("obj = new package1.TestMethodCall();");
    expectNumber(43, "obj.m(43)");
  }

  public void testInheritance() throws Exception {
    loadClass("package1.TestInheritanceSuperClass");
    loadClass("package1.TestInheritanceSubClass");
    loadClass("package1.TestInheritanceSubSubClass");
    eval("obj1 = new package1.TestInheritanceSuperClass(1);");
    eval("obj2 = new package1.TestInheritanceSubClass(11, 2);");
    eval("obj3 = new package1.TestInheritanceSubSubClass(111, 22, 3);");
    expectNumber(1, "obj1.getSlot1()");
    expectNumber(11, "obj2.getSlot1()");
    expectNumber(111, "obj3.getSlot1()");
    expectNumber(2, "obj2.getSlot2()");
    expectNumber(22, "obj3.getSlot2()");
    expectNumber(3, "obj3.getSlot3()");
    expectNumber(1, "obj1.m()");
    expectNumber(12, "obj2.m()");
    expectNumber(113, "obj3.m()");
  }

  public void testStaticInitializer() throws Exception {
    loadClass("package2.TestStaticInitializer");
    initClass("package2.TestStaticInitializer");
    expectString("s1", "package2.TestStaticInitializer.s1");
    expectString("s2/s1", "package2.TestStaticInitializer.s2");
    expectString("s3/s2/s1", "package2.TestStaticInitializer.s3");
    expectNumber(10, "package2.TestStaticInitializer.fv");
    // must not access private fields
    expectString("undefined", "typeof package2.TestStaticInitializer.f");
  }

  public void testInternal() throws Exception {
    loadClass("package2.TestInternal");
    initClass("package2.TestInternal");
    expectString("internal", "new package2.TestInternal().returnInternal()");
  }

  public void testInitializeBeforeStaticMethod() throws Exception {
    loadClass("package2.TestStaticInitializer");
    expectNumber(1, "package2.TestStaticInitializer.return1()");
    expectNumber(2, "package2.TestStaticInitializer.return2()");
  }

  public void testLocalVariables() throws Exception {
    loadClass("package1.TestLocalVariables");
    eval("obj = new package1.TestLocalVariables();");
    expectNumber(200, "obj.m(10)");
    expectNumber(134, "obj.m2(10)");
  }

  public void testStatements() throws Exception {
    loadClass("package2.TestStatements");
    eval("obj = new package2.TestStatements;");
    expectNumber(200, "obj.testIf(true, 200, 300)");
    expectNumber(300, "obj.testIf(false, 200, 300)");
    expectNumber(200, "obj.testIfThenElse(true, 200, 300)");
    expectNumber(300, "obj.testIfThenElse(false, 200, 300)");
    expectNumber(15, "obj.testWhile(5)");
    expectNumber(10, "obj.testFor(5)");
    expectNumber(15, "obj.testDoWhile(5)");
    expectString("x, y, z", "obj.testForIn({ y: 2, x :1, z: 3})");
    expectNumber(11, "obj.testSwitch(1,1,11,2,22,33)");
    expectNumber(22, "obj.testSwitch(2,1,11,2,22,33)");
    expectNumber(33, "obj.testSwitch(3,1,11,2,22,33)");
    expectString("undefined", "typeof(obj.testReturnVoid())");
    expectNumber(42, "var o = { tobedeleted: 42 }; o.tobedeleted");
    expectNumber(42, "var o2 = { tobedeleted: 42 }; o2.tobedeleted");
    expectString("undefined", "obj.testDelete1(o); typeof(o.tobedeleted)");
    expectString("undefined", "obj.testDelete2(o2, 'tobedeleted'); typeof(o.tobedeleted)");
  }

  public void testExpressions() throws Exception {
    loadClass("package2.TestExpressions");
    eval("obj = new package2.TestExpressions;");
    expectNumber(200, "obj.testCond(true, 200, 300)");
    expectNumber(300, "obj.testCond(false, 200, 300)");
    expectNumber(1, "obj.antitestRegexpLiterals()");
    expectNumber(24, "obj.testParenExpr(11)");
    expectNumber(7, "obj.testBinOpExpr(12)");
    expectNumber(130, "(obj.testFunExpr(13))(10)");
    expectNumber(-2, "obj.testPrefixOpExpr(14)");
    expectNumber(30, "obj.testPostfixOpExpr(15)");
    String dq = "'¤\\\b\t\n\f\r\'/'\u00C6\u01Bfe\"'"; // "'¤\\\b\t\n\f\r\'/'\xc6\u01Bfe\"'"
    String sq = "\"¤\\\b\t\n\f\r\'/\"\u00C6\u01Bfe'\""; // '"¤\\\b\t\n\f\r\'/"\xc6\u01Bfe\'"'
    expectString(dq, "obj.testStringLiteralsDQ()");
    expectString(sq, "obj.testStringLiteralsSQ()");
    expectString("ActionScript <span class='heavy'>3.0</span>", "obj.testStringLiterals3()");
    expectString("<item id=\"155\">banana</item>", "obj.testStringLiterals4()");
    expectString(dq, "obj.testCharLiterals()");
    expectString("2,7,2,2,2", "obj.testRegexpLiterals()");
    expectNumber(123+456, "obj.testObjectLiterals()");
    expectString("1,2,3,4,5,6,7,8,9,0", "obj.testArrayLiterals()");
  }

  public void testImport() throws Exception {
    loadClass("package2.TestImport");
    loadClass("package1.TestMethodCall");
    loadClass("package1.package11.TestSubPackage");
    loadClass("package2.TestStaticInitializer");
    expectString("s2/s1"+"/"+(-19+42), "package2.TestImport.main()");
  }

  public void testSelfAwareness() throws Exception {
    runClass("package1.TestSelfAwareness");
  }

  public void testInitializers() throws Exception {
    doTestTwoSlots("package1.TestInitializers");
  }

  public void testImplicitSuper() throws Exception {
    loadClass("package1.TestInitializers");
    doTestTwoSlots("package1.TestImplicitSuper");
  }

  public void testExplicitSuper() throws Exception {
    loadClass("package1.TestInitializers");
    doTestTwoSlots("package1.TestExplicitSuper");
  }

  private void doTestTwoSlots(String className) throws Exception {
    loadClass(className);
    eval("obj = new "+className+"();");
    expectNumber(1, "obj.getSlot1()");
    expectNumber(2, "obj.getSlot2()");
  }

  /* TODO: the following does not work
  public void testUnqualifiedAccess() throws Exception {
    loadClass("package1.TestUnqualifiedAccess");
    eval("obj = new package1.TestUnqualifiedAccess(\"a\")");
    eval("new package1.TestUnqualifiedAccess(\"c\")");
    expectString("a", "obj.getPrivateSlot()");
    eval("obj.setPrivateSlot(\"b\")");
    expectString("b", "obj.getPrivateSlot()");
    expectString("a", "obj.getProtectedSlot()");
    eval("obj.setProtectedSlot(\"b\")");
    expectString("b", "obj.getProtectedSlot()");
    expectString("a", "obj.getPublicSlot()");
    eval("obj.setPublicSlot(\"b\")");
    expectString("b", "obj.getPublicSlot()");
  }
  */

  public void testNoSuper() throws Exception {
    loadClass("package1.TestNoSuper");
    try {
      eval("joo.Class.complete();");
    } catch (Exception e) {
      return;
    }
    fail("exception expected");
  }

  public void testYesSuper1() throws Exception {
    loadClass("package1.TestInheritanceSuperClass");
    loadClass("package1.TestInheritanceSubClass");
    loadClass("package1.TestInheritanceSubSubClass");
    eval("joo.Class.complete();");
  }

  public void testYesSuper2() throws Exception {
    loadClass("package1.TestInheritanceSubSubClass");
    loadClass("package1.TestInheritanceSubClass");
    loadClass("package1.TestInheritanceSuperClass");
    eval("joo.Class.complete();");
  }

  public static void main(String args[]) {
    junit.textui.TestRunner.run(JooTest.class);
  }

}
