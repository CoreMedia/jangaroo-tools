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

public class TestInheritanceSubClass extends package1.TestInheritanceSuperClass {

  public function TestInheritanceSubClass(slot1Value :int, slot2Value :int) {
    super(slot1Value);
    slot2 = slot2Value;
  }

  protected var slot2 :int;

  public function setSlot2(value :int) :void {
    slot2 = value;
  }

  public function getSlot2() :int {
    return slot2;
  }

  override public function m() :int {
    return super.m()+1;
  }

}
}