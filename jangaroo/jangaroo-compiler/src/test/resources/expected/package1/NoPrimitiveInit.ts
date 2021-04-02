import SomeOtherClass from "./someOtherPackage/SomeOtherClass";
import int from "../AS3/int_";


class NoPrimitiveInit {
  constructor() {
  }

  #method(i:int):int {
    return SomeOtherClass.BLA + int.MAX_VALUE;
  }
}
export default NoPrimitiveInit;
