/* /*
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
 * /

package package1 {*/

Ext.define("package1.TestBind", function(TestBind) {/*public class TestBind {

  public var boundField:Function =*/function boundField_(){this.boundField=(AS3.bind( this,"getStatePrivate$JrNo"));}/*;

  public*/ function TestBind$(state/* : String*/) {this.super$JrNo();
    this.state$JrNo = state;
    var bound/*:Function*/ =AS3.bind( this,"getStatePrivate$JrNo");
    var bound2/*:Function*/ =AS3.bind( (this),"getState");
    var bound3/*:Function*/ = AS3.cast(TestBind,this).getState.call(this);
  }/*

  [Parameter("shouldBeString", coerceTo="String")]
  private*/ function testCoerce(shouldBeString/*: Object*/)/*:void*/ {}/*

  public*/ function getState()/* : String*/ {
    this.boundField.call( null);
    this.testCoerce$JrNo(String(1));
    this.testCoerce$JrNo("1");
    return this.state$JrNo;
  }/*

  [Return("this")]
  public*/ function chainable()/*: void*/ {
    if (this.getState()) {
      this.testCoerce$JrNo("nothing here");
      return this;
    }
    this.testCoerce$JrNo("nothing there");return this;
  }/*

  private*/ function getStatePrivate()/* : String*/ {
    return this.state$JrNo;
  }/*

  private var state : String;

}
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: TestBind$,
      super$JrNo: function() {
        boundField_.call(this);
      },
      testCoerce$JrNo: testCoerce,
      getState: getState,
      chainable: chainable,
      getStatePrivate$JrNo: getStatePrivate,
      state$JrNo: null
    };
});
