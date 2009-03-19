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

public class TestStaticInitializer {

  public function TestStaticInitializer() {
  }

  static public var s1 :String= "s" + "1";
  static public var s2 :String;

  {
    s2 = "s2/" + s1;
  }

  static public var s3 :String;

  {
    s3 = "s3/" + s2;
  }

  static private var f :Function;
  static public var fv :int;

  {
    f = function(x :int, y :int) : int {
      return x+y;
    };
  }
  // new code block so that f is not accidentially local to the code block 
  {
    fv = f(1,9);
  }

  static public function return1() : Number {
    return 1;
  }

  static public function return2() : Number {
    return 2;
  }
}
}