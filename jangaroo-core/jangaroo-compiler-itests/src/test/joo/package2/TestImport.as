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

package package2 {

import package1.*;
import package1.ClassToImport;
import package1.ClassToImport2;
import package1.TestMethodCall;
import package1.package11.TestSubPackage;
import package3.ClassToImport; // IDEA takes this as disambiguating but flexsdk compc still complains ambiguity if ClassToImport would be used unqualified 

import packageInJar.TestClassInJar;

public class TestImport extends TestMethodCall {

  private static const TEST_CLASS_INIT : String = TestStaticInitializer.s2;

  public function TestImport() {
    //var xxxHelloWorld = function() { return {greet:function(){}}; };
    var ti :TestImplements = new TestImplements();
    var i :int = ti.implementMe("abc");
    assert(i == 3);
    package3.ClassToImport.m_package3(); // package prefix is required to resolve ambiguity with package1.ClassToImport
    TestClassInJar.m();
  }

  public function testVectorResultInApi():Vector.<String> {
    return new <String>["a", "b"];
  }

  public static function main(arguments :String) : String {
    var testImport : TestImport  = new TestImport();

    var cti  :package1.ClassToImport2 = new package1.ClassToImport2(); //todo remove package prefix?
    cti.m2_package1();

    var tcti  :ToplevelClassToImport = new ToplevelClassToImport();
    tcti.setX("foo");
    assert(tcti.getX() === "foo");

    return TEST_CLASS_INIT + "/" + (testImport.m(-19) + TestSubPackage.test());
  }

}
}
