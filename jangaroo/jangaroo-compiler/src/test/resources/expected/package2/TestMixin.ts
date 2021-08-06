interface TestMixinConfig extends Partial<Pick<TestMixin,
  "foo"
>> {
}


/**
 * This is a test mixin.
 */
class TestMixin {
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
