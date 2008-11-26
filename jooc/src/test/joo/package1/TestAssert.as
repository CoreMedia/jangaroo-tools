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

package package1 /*blubber*/ {

/**
* a comment
*/
public class TestAssert {

  public function TestAssert() {
  }

  static public function testAssert() :String {
    try {
      assert(1 < 2);
      try {
        assert(2 < 1);
        return "no exception thrown";
      } catch(ex) {
        return ex.message;
      }
    } catch(ex) {
      return ex.message;
    }
  }

}
}