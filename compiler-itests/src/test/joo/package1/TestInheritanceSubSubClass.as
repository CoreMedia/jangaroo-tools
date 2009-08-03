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

public class TestInheritanceSubSubClass extends package1.TestInheritanceSubClass {

  public function TestInheritanceSubSubClass(slot1 :int, slot2 :int, slot3 :int) {
    super(slot1, slot2);
    this.slot3 = slot3;
  }

  protected var slot3;

  public function getSlot3() :int {
    return this.slot3;
  }

  override public function m() :int {
    return super.m()+1;
  }

}
}