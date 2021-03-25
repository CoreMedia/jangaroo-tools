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

import int from '../AS3/int_';


/**
* a comment
*/
class TestMethodCall /* blub ber *//*extends Object*/ {

  constructor() {
  }

  s:TestMethodCall = null;

  static s(x :int) :int {
    return x;
  }

  m(x :int) :int {
    return x;
  }

  protected prot(x :int) :int {
    return x+1;
  }

  //@ts-expect-error 18022
  #priv(x :int) :int {
    return x+2;
  }

  callm(x :int, t:TestMethodCall) :int {
    return t.m(x);
  }

  callmViaThis(x :int, t:TestMethodCall) :int {
    return this.m(x);
  }

  callmViaObject(x :int, t:TestMethodCall) :int {
    var o:Record<string,any> = { t: t };
    return o.t.m(x);
  }

  callmViaVar(x :int, t:TestMethodCall) :int {
    var o = t;
    return o.m(x);
  }

  callmViaField(x :int, t:TestMethodCall) :int {
    this.s = t;
    return this.s.m(x);
  }

  callmViaThisDotField(x :int, t:TestMethodCall) :int {
    this.s = t;
    return this.s.m(x);
  }

  callProt(x :int, t:TestMethodCall) :int {
    return t.prot(x);
  }

  callProtViaThis(x :int, t:TestMethodCall) :int {
    return this.prot(x);
  }

  callProtViaObject(x :int, t:TestMethodCall) :int {
    var o:Record<string,any> = { t: t };
    return o.t.prot(x);
  }

  callProtViaVar(x :int, t:TestMethodCall) :int {
    var o = t;
    return o.prot(x);
  }

  callProtViaField(x :int, t:TestMethodCall) :int {
    this.s = t;
    return this.s.prot(x);
  }

  callProtViaThisDotField(x :int, t:TestMethodCall) :int {
    this.s = t;
    return this.s.prot(x);
  }

  callPriv(x :int, t:TestMethodCall) :int {
    return t.#priv(x);
  }

  callPrivViaThis(x :int, t:TestMethodCall) :int {
    return this.#priv(x);
  }

  callPrivViaObject(x :int, t:TestMethodCall) :int {
    var o:Record<string,any> = { t: t };
    return o.t.priv(x);
  }

  callPrivViaVar(x :int, t:TestMethodCall) :int {
    var o = t;
    return o.#priv(x);
  }

  callPrivViaField(x :int, t:TestMethodCall) :int {
    this.s = t;
    return this.s.#priv(x);
  }

  callPrivViaThisDotField(x :int, t:TestMethodCall) :int {
    this.s = t;
    return this.s.#priv(x);
  }


}
export default TestMethodCall;
