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

public class TestInheritanceSuperClass {

  public function TestInheritanceSuperClass(value :int) {
    slot1 = value;
  }

  public var slot1 :int;

  public function setSlot1(value :int) :void {
    slot1 = value;
  }

  public function getSlot1() :int {
    return slot1;
  }

  public function m() :int {
    return getSlot1();
  }

}
}