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

  public function testMissingButNewLine():void {
    { 1
2 } 3
  }

  public function testMissingAfterRegexp():void {
    var regexp : RegExp = /at Function\/http:\/\/adobe\.com\/AS3\//
doSomeThing();
  }

  public function testMissingAfterReturn():void {
    return
    1 + 2
  }

  public function testMissingBeforeBlockEnd2():* {
    doSomeThing();
    return computeSomeThing()

   //keep the newlines please
  }
}
}