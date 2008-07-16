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

import package1.TestMethodCall;
import package1.package11.TestSubPackage;
import package2.TestStaticInitializer;

public class TestImport extends TestMethodCall {

  public function TestImport() {
  }

  public static function main() : int {
    var testImport : TestImport  = new TestImport();
    return TestStaticInitializer.s2 + "/" + (testImport.m(-19) + TestSubPackage.test());
  }

}
}