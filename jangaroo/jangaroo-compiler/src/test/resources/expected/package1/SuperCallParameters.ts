import ManyConstructorParameters from "./ManyConstructorParameters";


class SuperCallParameters extends ManyConstructorParameters {
  constructor() {
    var foo: boolean;
    super(...(():[any,any,any,any,any,any]=>{
    foo = this.isEmpty("");
    var bar = "BAR";
    function innerUsingThis(): boolean {
      return this$.isEmpty("");
    }
    function innerDynamic(): boolean {
      return this.isEmpty("");
    }
    return ["bar", -1, -4.2, true, {}, []];})());const this$=this;
    if (foo) {
      super.isEmpty("FOO");
    }
  }

  override isEmpty(str:string):boolean {
    return super.isEmpty(str);
  }
}
export default SuperCallParameters;
