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

  public function TestBind(state : String) {
    this.state = state;
  }

  public function getState() : String {
    return this.state;
  }

  public function getState1() : String {
    return this.state;
  }

  public function getState2() : String {
    return this.state;
  }

  public function getState3() : String {
    return this.state;
  }

  public function getState4() : String {
    return this.state;
  }

  // will be auto-bound!
  private function getStatePrivate() : String {
    return this.state;
  }

  public function testInvokeLocalVar() : * {
    var f : Function = this.getState1;
    return f();
  }

  public function testInvokeLocalVarUnqualified() : * {
    var f : Function = getState2;
    return f();
  }

  public function testInvokeParameter() : String {
    return this.invoke(this.getState3);
  }

  public function testInvokeParameterUnqualified() : String {
    return invoke(getState4);
  }

  public function testInvokeParameterUnqualifiedPrivate() : String {
    return invoke(getStatePrivate);
  }

  public function testLocalFunction() : String {
    return invoke(function() {
      return this.getState();
    });
  }

  public function testLocalFunctionUnqualified() : String {
    return invoke(function() {
      return getState();
    });
  }

  // negative tests>
  // This method is never actually called, but only analzyed by the compiler.
  // These expressions must *not* lead to testNotBound being bound!
  public function testDelete() : String {
    delete this.testNotBound;
    return typeof this.testNotBound;
  }

  // negative test: testNotBound must not be bound!
  public function testNotBound() : String {
    return this.getState();
  }

  // negative test: functions using "this" inside static code must not be bound:
  public static function testStaticNotBound() : String {
    return function() : Object {
      return this;
    };
  }

  private function invoke(f : Function) : * {
    return f();
  }

  private var state : String;
}
}