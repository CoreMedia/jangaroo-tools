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

import {bind} from '@jangaroo/joo/AS3';


class TestBind {

  boundField:AnyFunction =bind( this,this.#getStatePrivate);

  constructor(state : string) {
    this.#state = state;
    var bound:AnyFunction =bind( this,this.#getStatePrivate);
  }

  
  //@ts-expect-error 18022
  #testCoerce(shouldBeString: any):void {}

  getState() : string {
    this.boundField.call( null);
    this.#testCoerce(String(1));
    this.#testCoerce("1");
    return this.#state;
  }

  
  chainable():this {
    if (this.getState()) {
      this.#testCoerce("nothing here");
      return this;
    }
    this.#testCoerce("nothing there");return this;
  }

  //@ts-expect-error 18022
  #getStatePrivate() : string {
    return this.#state;
  }

  #state : string = null;

}
export default TestBind;
