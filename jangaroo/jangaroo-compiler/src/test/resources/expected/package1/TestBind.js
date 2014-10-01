define("as3/package1/TestBind",["exports","as3-rt/AS3"], function($exports,AS3) { AS3.compilationUnit($exports, function($primaryDeclaration){/* /*
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

package package1 {

public class TestBind {

  public var boundField:Function =*/function boundField_(){this.boundField=(AS3.bind( this,"getStatePrivate$1"));}/*;

  public*/ function TestBind(state/* : String*/) {boundField_.call(this);
    this.state$1 = state;
    var bound/*:Function*/ =AS3.bind( this,"getStatePrivate$1");
  }/*

  public*/ function getState()/* : String*/ {
    return this.state$1;
  }/*

  private*/ function getStatePrivate()/* : String*/ {
    return this.state$1;
  }/*

  private var state : String;

}
}

============================================== Jangaroo part ==============================================*/
    $primaryDeclaration(AS3.class_({
      package_: "package1",
      class_: "TestBind",
      members: {
        constructor: TestBind,
        getState: getState,
        getStatePrivate$1: getStatePrivate,
        state$1: {
          value: null,
          writable: true
        }
      }
    }));
  });
});
