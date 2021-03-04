
 class FieldInitializer {
  readonly #const1:string = "foo";
  readonly #const2:any = "foo" + "bar";
  readonly #const3:Record<string,any> = {"foo": "bar"};

    #myConfigOption:string = "baz";

  
  get myConfigOption():string { return this.#myConfigOption; }
    set myConfigOption(value:string) { this.#myConfigOption = value; }

    #myConfigOption2:Record<string,any> = { a: 123 };

  
  get myConfigOption2():Record<string,any> { return this.#myConfigOption2; }
    set myConfigOption2(value:Record<string,any>) { this.#myConfigOption2 = value; }

   foo():string {
    return this.#const1 + this.#const2 + this.#const3;
  }
}
export default FieldInitializer;
