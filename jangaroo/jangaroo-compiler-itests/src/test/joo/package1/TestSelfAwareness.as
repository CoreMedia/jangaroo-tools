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
  public class TestSelfAwareness {
    public static var s : String;
    public var m : String;

    public static function main() : void {
      // Test that the class knows itself when accessing statics.
      package1.TestSelfAwareness.s = "a";
      if (TestSelfAwareness.s != "a") throw "failed to read unqualified";
      if (s != "a") throw "failed to read in scope";
      TestSelfAwareness.s = "b";
      if (package1.TestSelfAwareness.s != "b") throw "failed to read qualified";
      if (s != "b") throw "failed to read in scope";

      // Check that the constructor is accessible.
      var tsa : TestSelfAwareness = new TestSelfAwareness();
      tsa.m = "c";
      if (tsa.m != "c") throw "failed to read field";
    }
  }
}