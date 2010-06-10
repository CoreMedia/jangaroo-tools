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

package package1 {

import package2.TestInclude;

public class TestTypeCast {

  public function TestTypeCast() {
  }

  public static function testAsCast(p : Object) : TestTypeCast {
    var r :Number = 99.7;
    var i :int = int(r);
    return p as TestTypeCast;
  }

  public static function testFunctionCast(p : Object) : TestTypeCast {
    p = TestInclude(p);
    p = package2.TestInclude(p);
    return TestTypeCast(p);
  }

}
}
