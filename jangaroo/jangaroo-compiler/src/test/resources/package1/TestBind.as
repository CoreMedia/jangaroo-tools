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

public class TestBind {

  public var boundField:Function = getStatePrivate;

  public function TestBind(state : String) {
    this.state = state;
    var bound:Function = getStatePrivate;
    var bound2:Function = (this).getState;
    var bound3:Function = TestBind(this).getState.call(this);
  }

  [Parameter("shouldBeString", coerceTo="String")]
  private function testCoerce(shouldBeString: Object):void {}

  public function getState() : String {
    this.boundField.call();
    this.testCoerce(1);
    this.testCoerce("1");
    return state;
  }

  [Return("this")]
  public function chainable(): void {
    if (getState()) {
      this.testCoerce("nothing here");
      return;
    }
    this.testCoerce("nothing there");
  }

  private function getStatePrivate() : String {
    return state;
  }

  private var state : String;

}
}

