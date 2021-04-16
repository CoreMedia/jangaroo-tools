import { bind } from "@jangaroo/runtime/AS3";


class TestHelperClasses {

  static readonly TEXT:string = "foo";
  static getText():string {
    var thc = new Helper("foo");
    var f:AnyFunction =bind( thc,thc.getText);
    return f();
  }

  static getConstantFromHelperClass():string {
    return Helper.CONST;
  }
}

class Helper {

  static readonly CONST:string = "FOO";
  #text:string = TestHelperClasses.TEXT;

  constructor(text:string) {
    this.#text = text;
  }

  getText():string {
    var f:AnyFunction =bind( this,this.#text_getter);
    f =bind( this,this.#text_getter);
    return f();
  }

  #text_getter():string {
    return this.#text;
  }
}
export default TestHelperClasses;
