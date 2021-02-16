
 class TestStaticNonStaticConfusion {

   constructor() {
    this.#foo();
    this.#foo();
    TestStaticNonStaticConfusion.foo();
  }

   static foo():string {
    return "static foo";
  }

  //@ts-expect-error 18022
   #foo():string {
    return "foo";
  }
}
export default TestStaticNonStaticConfusion;
