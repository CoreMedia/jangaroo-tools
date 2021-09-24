import Config from "@jangaroo/runtime/AS3/Config";
import Events from "@jangaroo/ext-ts/Events";
import Base from "../Ext/Base";
interface TestMixinEvents extends Events<Base> {

/**
 * Test mixin event.
 */
  firedByMixin():any;
}

interface TestMixinConfig extends Config<Base>, Partial<Pick<TestMixin,
  "foo"
>> {
  listeners?: TestMixinEvents;
}


/**
 * This is a test mixin.
 */
class TestMixin extends Base {
  declare Config: TestMixinConfig;
  /**
   * An accessor.
   */
  
   get foo():number {
    return 42;
  }

  /**
   * The setter.
   */
  
   set foo(value:number) {
    // do nothing. 42 is perfect.
  }

  /**
   * This method is mixed in.
   * @param thing from the swamp
   * @return mud
   */
  mix(thing:string):string {
    return "Mixed " + thing + "!";
  }
}
export default TestMixin;
