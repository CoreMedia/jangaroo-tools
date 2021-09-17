import { as, cast, is, mixin } from "@jangaroo/runtime/AS3";
import int from "../AS3/int_";
import uint from "../AS3/uint_";
import SomeClass from "./SomeClass";
import TestInterface from "./mxml/pkg/TestInterface";


class TestTypeCast {

  constructor() {
  }

  static testAsCast(p : any) : TestTypeCast {
    var r = 99.7;
    var n = Number("99.8");
    var i = int(r);
    var b =is( p,  TestTypeCast);
    var notB :boolean = !is(p,  TestTypeCast);
    var castObjectToInterface = Object.assign(new (mixin(class {}, TestInterface)), { foo: "FOO" });
    var castObjectToNonExt = Object.assign(new SomeClass(), { bar: "BAR" });
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
