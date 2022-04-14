import ManyConstructorParameters from "./ManyConstructorParameters";


class SuperCallParameters extends ManyConstructorParameters {
  constructor() {
    // @ts-expect-error Ext JS semantics
    const this$ = this;
    const foo = this$.isEmpty("");
    var bar = "BAR";
    function innerUsingThis(): boolean {
      return this$.isEmpty("");
    }
    function innerDynamic(): boolean {
      return this.isEmpty("");
    }
    super("bar", -1, -4.2, true, {}, []);
    if (foo) {
      super.isEmpty("FOO");
    }
  }

  override isEmpty(str:string):boolean {
    return super.isEmpty(str);
  }
}
export default SuperCallParameters;
