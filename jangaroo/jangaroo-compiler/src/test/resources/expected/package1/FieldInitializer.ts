
 class FieldInitializer {
  readonly #const1:string = "foo";
  readonly #const2:any = "foo" + "bar";
  readonly #const3:Record<string,any> = {"foo": "bar"};

   foo():string {
    return this.#const1 + this.#const2 + this.#const3;
  }
}
export default FieldInitializer;
