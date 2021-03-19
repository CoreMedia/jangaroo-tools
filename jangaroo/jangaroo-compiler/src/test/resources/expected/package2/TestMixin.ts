import Number from '../Number';
interface TestMixin_ extends Partial<Pick<TestMixin,
    "foo"
>> {
}


/**
 * This is a test mixin.
 */
 class TestMixin<Cfg extends TestMixin._ = TestMixin._> {
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
declare namespace TestMixin {
  export type _ = TestMixin_;
  export const _: { new(config?: _): _; };
}


export default TestMixin;
