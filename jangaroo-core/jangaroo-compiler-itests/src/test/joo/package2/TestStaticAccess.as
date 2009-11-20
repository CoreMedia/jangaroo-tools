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

public class TestStaticAccess {

  static public var s1 :String= "s1";
  static private var s2 :String = "s2";

  static public function get_s1() :String {
    return s1;
  }

  static public function set_s1(_s1 :String) :void {
    s1 = _s1;
  }

  static public function get_s1_qualified() :String {
    var s1 :String = "qualified error";
    return TestStaticAccess.s1;
  }

  static public function set_s1_qualified(s1 :String) :void {
    TestStaticAccess.s1 = s1;
  }

  static public function get_s1_fully_qualified() :String {
    var s1 :String = "fully qualified error";
    return package2.TestStaticAccess.s1;
  }

  static public function set_s1_fully_qualified(s1 :String) :void {
    package2.TestStaticAccess.s1 = s1;
  }

  static public function get_s2() :String {
    return s2;
  }

  static public function set_s2(_s2 :String) :void {
    s2 = _s2;
  }

  static public function get_s2_qualified() :String {
    var s2 :String = "qualified error";
    return TestStaticAccess.s2;
  }

  static public function set_s2_qualified(s2 :String) :void {
    TestStaticAccess.s2 = s2;
  }

  static public function get_s2_fully_qualified() :String {
    var s2 :String = "fully qualified error";
    return package2.TestStaticAccess.s2;
  }

  static public function set_s2_fully_qualified(s2 :String) :void {
    package2.TestStaticAccess.s2 = s2;
  }

}
}