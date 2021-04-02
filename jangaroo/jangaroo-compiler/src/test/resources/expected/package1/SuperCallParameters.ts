import ManyConstructorParameters from "./ManyConstructorParameters";


class SuperCallParameters extends ManyConstructorParameters {
  constructor() {
    super("bar", -1, -4.2, true, {}, []);
  }

  isEmpty(str:string):boolean {
    return super.isEmpty(str);
  }
}
export default SuperCallParameters;
