import { as, cast, is } from "@jangaroo/joo/AS3";
import uint from "../AS3/int_";
import int from "../AS3/int_";


class TestTypeCast {

  constructor() {
  }

  static testAsCast(p : any) : TestTypeCast {
    var r = 99.7;
    var n = Number("99.8");
    var i = int(r);
    var b =is( p,  TestTypeCast);
    var notB :boolean = !is(p,  TestTypeCast);
    return as( p,  TestTypeCast);
  }

  static testCastToUint(any:any):uint {
    return uint(any);
  }

  static testCastToClassVar(clazz:Class, value:any):any {
    return cast(clazz,value);
  }

}
export default TestTypeCast;
