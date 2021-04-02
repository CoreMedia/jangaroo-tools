import { is } from "@jangaroo/joo/AS3";

class WithStaticReference {
  static readonly BLA = "bla";
  constructor() {
    var bla = WithStaticReference.BLA;is(
    bla,  WithStaticReference);
    this.#make2();
  }
  static make():void {
    var bla = WithStaticReference.BLA;is(
    bla,  WithStaticReference);
    new WithStaticReference();
  }
  #make2():void {
    var bla = WithStaticReference.BLA;is(
    bla,  WithStaticReference);
    new WithStaticReference();
  }
}
export default WithStaticReference;
