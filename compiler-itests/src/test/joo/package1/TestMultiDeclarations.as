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

public class TestMultiDeclarations {

  public var a, b;
  public var c=1, d : String, e:String="foo";
  public var f:int=2, g:Object = {toString: function():String{return "bar";}};

  public static const EXPECTED_RESULT : String = "undefined/undefined/1/undefined/foo/2/bar";

  public function TestMultiDeclarations() {
  }

  public function testFields() : String {
    return a+"/"+b+"/"+c+"/"+d+"/"+e+"/"+f+"/"+g;
  }

  public function testVariables() : String {
    var a, b;
    var c=1, d : String, e:String="foo";
    var f:int=2, g:Object = {toString: function():String{return "bar";}};
    return a+"/"+b+"/"+c+"/"+d+"/"+e+"/"+f+"/"+g;
  }

  public function testForLoops(arr : Array) : String {
    var result : String = "[";
    for (var i:int=0, l:int=arr.length; i<l; ++i) {
      if (i>0)
        result+=" ";
      result+=arr[i];
    }
    result += "]";
    return result;
  }

}
}