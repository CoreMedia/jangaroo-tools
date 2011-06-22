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

public class TestVector {

  public static function testConstructorAndSingleAssignment(s1:String, s2:String) : String {
    var vector:Vector.<String> = new Vector.<String>();
    vector[0] = s1;
    vector[1] = s2;
    return vector.join("|");
  }

  public static function testIntConstructorAndSingleAssignment(i1:int, i2:int) : String {
    var vector:Vector.<int> = new Vector.<int>();
    vector[0] = i1;
    vector[1] = i2;
    return vector.join("|");
  }

  public static function testConversion(s1:String, s2:String) : String {
    var vector:Vector.<String> = Vector.<String>(["foo", "bar"]);
    return vector.join("|");
  }

  public static function testLiteral(s1:String, s2:String) : String {
    var vector:Vector.<String> = new <String>[s1, s2];
    return vector.join("|");
  }

  public static function testForEach(s1:String, s2:String) : String {
    var vector:Vector.<String> = new <String>[s1, s2];
    var sb:Array = [];
    for each (var s:String in vector) {
      sb.push(s);
    }
    return sb.join("|");
  }

  public static function testNestedVector() : void {
    var nestedVector:Vector.<Vector.<String>> = Vector.<Vector.<String>>([["foo", "bar"], ["baz"]]);
  }
}
}