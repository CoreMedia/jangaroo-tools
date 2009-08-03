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

public class TestInitializersWithDefaultConstructor {

  protected var slot1 : int = 1;
  protected var slot2 : int = slot1 + 1;
  protected var slot3 : Object = {
    nolabel: 1,
    alsonolabel: 2
  };

  public function getSlot1() :int {
    return this.slot1;
  }

  public function getSlot2() :int {
    return this.slot2;
  }

  public function getSlot3() :Object {
    return this.slot3;
  }

}
}