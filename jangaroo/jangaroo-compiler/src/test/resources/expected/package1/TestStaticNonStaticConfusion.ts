
class TestStaticNonStaticConfusion {

  constructor() {
    this.#foo();
    this.#foo();
    TestStaticNonStaticConfusion.foo();
  }

  static foo():string {
    return "static foo";
  }

  #foo():string {
    return "foo";
  }
}
export default TestStaticNonStaticConfusion;
