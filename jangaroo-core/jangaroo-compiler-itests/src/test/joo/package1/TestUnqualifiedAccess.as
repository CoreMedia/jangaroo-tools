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
    privateSlot = protectedSlot = publicSlot = value;
  }

  private var privateSlot :String;
  protected var protectedSlot :String;
  public var publicSlot :String;

  public function testOtherPrivateAccess(other :TestUnqualifiedAccess) : String {
    return other.privateSlot;
  }

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

  public function testConstructorAccess() : Boolean {
    return this['constructor'] === TestUnqualifiedAccess;
  }

  // a local function must be found, or it will be assumed to be a member:
  public function testLocalFunction() : Boolean {
    function localFunction() : Boolean {
      return true;
    };
    return localFunction();
  }

  public function testForwardPrivateUnqualified(p : *) : * {
    return forwardPrivate(p);
  }

  private function forwardPrivate(p:*):* {
    return p;
  }

  public static const UNQUALIFIED_CLASS_EQUALS_QUALIFIED_CLASS : Boolean = TestUnqualifiedAccess === package1.TestUnqualifiedAccess;
  public static var SET_BY_STATIC_INITIALIZER : String;

{
  static var test : String = "o";
  SET_BY_STATIC_INITIALIZER = "f" + test + test;
}

}
}