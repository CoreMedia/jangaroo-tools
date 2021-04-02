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

import StaticAccessSuperSuperClass from "../package1/StaticAccessSuperSuperClass";
import StaticAccessSuperClass from "./StaticAccessSuperClass";


class TestStaticAccess extends StaticAccessSuperClass {

  static s1:string = "s1";
  static #s2:string = "s2";

  constructor() {
    super();
    StaticAccessSuperSuperClass.f0();
  }

  static get_s0():string {
    return StaticAccessSuperClass.s0;
  }
 
  static set_s0(_s0:string):void {
    StaticAccessSuperClass.s0 = _s0;
  }

  static get_s0_qualified():string {
    var s0 = "qualified error";
    return StaticAccessSuperClass.s0;
  }

  static set_s0_qualified(s0:string):void {
    StaticAccessSuperClass.s0 = s0;
  }

  static get_s0_fully_qualified():string {
    var s0 = "fully qualified error";
    return StaticAccessSuperClass.s0;
  }

  static set_s0_fully_qualified(s0:string):void {
    StaticAccessSuperClass.s0 = s0;
  }

  static get_s1():string {
      return TestStaticAccess.s1;
    }
  
  static set_s1(_s1:string):void {
    TestStaticAccess.s1 = _s1;
  }

  static get_s1_qualified():string {
    var s1 = "qualified error";
    return TestStaticAccess.s1;
  }

  static set_s1_qualified(s1:string):void {
    TestStaticAccess.s1 = s1;
  }

  static get_s1_fully_qualified():string {
    var s1 = "fully qualified error";
    return TestStaticAccess.s1;
  }

  static set_s1_fully_qualified(s1:string):void {
    TestStaticAccess.s1 = s1;
  }

  static get_s2():string {
    return TestStaticAccess.#s2;
  }

  static set_s2(_s2:string):void {
    TestStaticAccess.#s2 = _s2;
  }

  static get_s2_qualified():string {
    var s2 = "qualified error";
    return TestStaticAccess.#s2;
  }

  static set_s2_qualified(s2:string):void {
    TestStaticAccess.#s2 = s2;
  }

  static get_s2_fully_qualified():string {
    var s2 = "fully qualified error";
    return TestStaticAccess.#s2;
  }

  static set_s2_fully_qualified(s2:string):void {
    TestStaticAccess.#s2 = s2;
  }

  static #get_s2_private():string {
    return TestStaticAccess.#s2;
  }

  static get_s2_via_private_static_method():string {
    return TestStaticAccess.#get_s2_private();
  }

  static get_s2_via_private_static_method_qualified():string {
    return TestStaticAccess.#get_s2_private();
  }

  static get_s2_via_private_static_method_full_qualified():string {
    TestStaticAccess.#some_private_static = TestStaticAccess.#some_private_static + "foo";
    return TestStaticAccess.#get_s2_private();
  }

  static get #some_private_static():string {
    return "some_private_static";
  }

  static set #some_private_static(value:string) {
  }
}
export default TestStaticAccess;
