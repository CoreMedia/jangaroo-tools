import SomeOtherClass from "./someOtherPackage/SomeOtherClass";
import int from "../AS3/int_";


class NoPrimitiveInit {
  constructor() {
  }

  //@ts-expect-error 18022
  #method(i:int):int {
    return SomeOtherClass.BLA + int.MAX_VALUE;
  }
}
export default NoPrimitiveInit;
