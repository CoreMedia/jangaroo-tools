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

[Event]
public class TestBind {

  public function TestBind(state : String) {
    this.state = state;
    //joo.bind(this,"getState");
  }

  //[Bound]
  public function getState() : String {
    return this.state;
  }

  //[Bound]
  private function getStatePrivate() : String {
    return this.state;
  }

  public function testInvokeLocalVar() : * {
    var f : Function = this.getState;
    return f();
  }

  public function testInvokeLocalVarUnqualified() : * {
    var f : Function = getState;
    return f();
  }

  public function testInvokeParameter() : String {
    return this.invoke(this.getState);
  }

  public function testInvokeParameterUnqualified() : String {
    return invoke(getState);
  }

  public function testInvokeParameterUnqualifiedPrivate() : String {
    return invoke(getStatePrivate);
  }

  public function testDelete() : Boolean {
    delete this.testDelete;
    return "testDelete" in this;
  }

  private function invoke(f : Function) : * {
    return f();
  }

  private var state : String;
}
}