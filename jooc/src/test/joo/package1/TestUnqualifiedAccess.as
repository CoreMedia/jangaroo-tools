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

public class TestUnqualifiedAccess {

  public function TestUnqualifiedAccess(value :String) {
    protectedSlot = value;
  }

  private var privateSlot :String;
  protected var protectedSlot :String;
  public var publicSlot :String;

  public function setPrivateSlot(value :String) :void {
    privateSlot = value;
  }

  public function getPrivateSlot() :String {
    return privateSlot;
  }

  public function setProtectedSlot(value :String) :void {
    protectedSlot = value;
  }

  public function getProtectedSlot() :String {
    return protectedSlot;
  }

  public function setPublicSlot(value :String) :void {
    publicSlot = value;
  }

  public function getPublicSlot() :String {
    return publicSlot;
  }

}
}