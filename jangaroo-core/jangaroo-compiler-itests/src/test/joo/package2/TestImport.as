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
import package1.package11.TestSubPackage;

public class TestImport extends TestMethodCall {

  private static const TEST_CLASS_INIT : String = TestStaticInitializer.s2;

  public function TestImport() {
    var ti :TestImplements = new TestImplements();
    var i :int = ti.implementMe("abc");
    assert(i == 3);
  }

  public static function main() : String {
    var testImport : TestImport  = new TestImport();

    var cti  :ClassToImport = new ClassToImport();
    cti.setX("foo");
    assert(cti.getX() === "foo");

    var tcti  :ToplevelClassToImport = new ToplevelClassToImport();
    tcti.setX("foo");
    assert(tcti.getX() === "foo");

    return TEST_CLASS_INIT + "/" + (testImport.m(-19) + TestSubPackage.test());
  }

}
}
