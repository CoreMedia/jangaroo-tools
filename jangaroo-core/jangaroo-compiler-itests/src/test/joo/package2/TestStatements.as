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

public class TestStatements {

  public function TestStatements() {
  }

  public function testWhile(n:int):int {
    var result = 0;
    var i = n;
    while (i > 0)
      result += i--;
    return result;
  }

  public function testFor(n:int):int {
    var result = 0;
    for (var i:Number = 0; i < n; i++)
      result += i;
    return result;
  }

  public function testForIn(o:Object):String {
    var result = [];
    for (var slot:String in o)
      result.push(slot);
    result.sort();
    return result.join(", ");
  }

  public function testForIn2(o:Object):String {
    var result = [];
    var slot:String;
    for (slot in o)
      result.push(slot);
    result.sort();
    return result.join(", ");
  }

  public function testForEach(o:Object):String {
    var result = [];
    for each (var slot:String in o)
      result.push(slot);
    result.sort();
    return result.join(", ");
  }

  public function testForEach2(o:Object):String {
    var result = [];
    var slot:String;
    for each (slot in o)
      result.push(slot);
    result.sort();
    return result.join(", ");
  }

  public function testForEach3(o:Object):String {
    var result = [];
    var slot:String;
    var $1:String = "occupied";
    for each (slot in o) {
      result.push(slot);
      for each (var foo:String in o) {
        result.push(foo);
      }
    }
    result.sort();
    return result.join(", ");
  }

  public function testDoWhile(n:int):int {
    var result = 0;
    var i = n;
    do
      result += i--;
    while (i > 0);
    return result;
  }

  public function testIf(cond:Boolean, ifTrue:int, ifFalse:int):int {
    if (cond) return ifTrue;
    return ifFalse;
  }

  public function testIfThenElse(cond:Boolean, ifTrue:int, ifFalse:int):int {
    if (cond) return ifTrue;
    else return ifFalse;
  }

  public function testSwitch(x:int, x1:int, y1:int, x2:int, y2:int, y:int):int {
    var result;
    switch (x) {
      case x1: result = y1; break;
      default: result = y; break;
      case x2: result = y2; break;
    }
    return result;
  }

  public function testReturnVoid():void {
    if (true)
      return;
    return;
  }

  public function testDelete1(o:Object):void {
    delete o.tobedeleted;
  }

  public function testDelete2(o:Object, slot:String):void {
    delete o[slot];
  }

  public function testTryCatchFinally(e:Object):String {
    try {
      throw e;
    } catch(any) {
      // ignore
    }
    try {
      throw e;
    } catch(any:Object) {
      // ignore
    }
    try {
      throw e;
    } catch(any:*) {
      // ignore
    }
    try {
      throw e;
    } catch(e1:Error) {
      return "is an Error: " + e1.message;
    } catch(e2) {
      return "is not an Error: " + e2;
    } finally {
      this.cleanedUp = true;
    }
  }

  function testBreakDo():int {
    var i:int = 0;
    do {
      if (i == 3) {
        break;
      }
      i += 1;
    } while (i < 6);
    return i;
  }

  function testBreakWhile():int {
    var i:int = 0;
    while (i < 6) {
      if (i == 3) {
        break;
      }
      i += 1;
    }
    return i;
  }

  function testBreakFor():int {
    var i:int = 0;
    for (; i < 6; i++) {
      if (i == 3) {
        break;
      }
    }
    return i;
  }

  function testBreakLabeledWhile():int {
    var i:int = 0;
    label: while (true) {
      while (i < 6) {
        if (i == 3) {
          break label;
        }
        i += 1;
      }
      return -1;
    }
    return i;
  }

  function testBreakLabeledFor():int {
    var i:int = 0;
    label: for (var j = 0; j < 100; j++) {
      while (i < 6) {
        if (i == 3) break label;
        i += 1;
      }
      return -1;
    }
    return i;
  }

  function testBreakLabeledDo():int {
    var i:int = 0;
    label: do {
      while (i < 6) {
        if (i == 3) {
          break label;
        }
        i += 1;
      }
      return -1;
    } while (true);
    return i;
  }

  function testBreakLabeledBlock():int {
    var i:int = 0;
    label: {
      while (i < 6) {
        if (i == 3) break label;
        i += 1;
      }
      return -1;
    }
    return i;
  }

  function testBreakLabeledIf():int {
    var i:int = 0;
    label: if (i == 0) {
      i = 1;
      break label;
      i = 2;
    }
    return i;
  }

  function testBreakDuplicateLabel():int {
    var i:int = 0;
    label: {
      i = 1;
      break label;
      i = 2;
    }
    var j:int = 3;
    label: {
      j = 4;
      break label;
      j = 5;
      i = 6;
    }
    return 10*i+j;
  }

  public var cleanedUp:Boolean = false;
}
}