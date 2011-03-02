/*
 * Copyright 2010 CoreMedia AG
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

public class TestSemicolonInsertion {

  public function TestSemicolonInsertion() {
  }

  private function doSomeThing() :void {}
private function computeSomeThing() :* { return undefined }
  private var someVar : Number;
public static const SOME_CONSTANT : String = "some const"
  private var _anotherVar : Number // no semicolon please
private var _yetAnotherVar : Number
private const someConst : * = 99
  // keep the indentation please

  public function testMissingBeforeBlockEnd():void {
    doSomeThing();doSomeThing()}

  public function testMissingBeforeBlockEnd2():* {
    doSomeThing();
    return computeSomeThing()

    //keep the newlines please
  }

  public function testMissingButNewLine():void {
    { 1
2 } 3
  }

  public function testMissingAfterRegexp():void {
    var regexp : RegExp = /at Function\/http:\/\/adobe\.com\/AS3\//
doSomeThing();
  }

  public function testMissingAfterReturn():void {
    // wondering why this is red in IDEA? http://youtrack.jetbrains.net/issue/IDEA-57039
    return /* please keep this comment containing a line feed
*/    1 + 2
  }

  public function testMissingAfterContinue():int {
    var result :int = 0;
    var label :* = 99;
    label: for (var i:int = 1; i <= 2; i++) {
      for (var j:int = 10; j <= 20; j+=10) {
        if (i < 2) continue
          label;
        result += j;
      }
      result += i;
    }
    return result;
  }

  public function testMissingAfterBreak():int {
    var result :int = 0;
    var label :* = 99;
    label: for (var i:int = 1; i <= 2; i++) {
      for (var j:int = 10; j <= 20; j+=10) {
        if (i < 2) break
          label;
        result += j;
      }
      result += i;
    }
    return result;
  }

  public function testMissingBeforePostfixPlusPlus():* {
    var i :int = 13;
    return i
            ++ i;
  }


  public function testMissingBeforePostfixMinusMinus():* {
    var i :int = 31;
    return i
            -- i;
  }

  /*
   * Known compiler bug, produces SyntaxError:
   *
  public function testReturnPreIncrement(x:int):int {
    return ++x;
  }
  */

}
}