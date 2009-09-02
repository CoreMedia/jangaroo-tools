/*
 * Copyright 2009 CoreMedia AG
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

public class TestNew {

  private var slot : Class = TestNew;

  public function TestNew() {
  }

  public function foo() : String {
    return "foo";
  }

  public function testNewCall() : String {
    return new this.slot().foo();
  }

  public function testNewCallNoThis() : String {
    return new slot().foo();
  }

  private static function getTestNew() : Class {
    return TestNew;
  }

  public static function testNewApplyExpr() : String {
    return (new (getTestNew())() as TestNew).foo();
  }

  private static var staticSlot : Class = TestNew;

  public static function testNewExpr() : String {
    return new staticSlot().foo();
  }

  public static function testNewStaticCall() : String {
    return new TestNew().foo();
  }

  public static function testNewFullyQualified() : String {
    return new package2.TestNew().foo();
  }

}
}