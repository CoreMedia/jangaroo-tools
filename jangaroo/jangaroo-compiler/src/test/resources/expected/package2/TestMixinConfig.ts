
class TestMixinConfig {

  #doSomethingBeforeFoo() {
    // gotcha before foo!
  }

  protected doSomethingAfterFoo() {
    // gotcha after foo!
  }

  doSomethingBeforeBar() {
    // gotcha before bar!
  }

  doSomethingOnBar() {
    // gotcha on bar!
  }

  doSomethingOnBaz() {
    // gotcha on baz!
  }

  doSomethingOnMany() {
    // gotcha on many!
  }

  doSomethingOnBoth() {
    // gotcha on both!
  }

  static #extendClass(baseClass:Class, derivedClass:Class, classBody: any):void {
    // gotcha extended!
  }
}
export default TestMixinConfig;
