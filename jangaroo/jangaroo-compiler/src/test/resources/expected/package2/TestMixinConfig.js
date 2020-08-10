/*package package2 {*/
Ext.define("package2.TestMixinConfig", function(TestMixinConfig) {/*public class TestMixinConfig {

  [MixinHook(before="foo")]
  private*/ function doSomethingBeforeFoo() {
    // gotcha before foo!
  }/*

  [MixinHook(after="foo")]
  protected*/ function doSomethingAfterFoo() {
    // gotcha after foo!
  }/*

  [MixinHook(before="bar")]
  public*/ function doSomethingBeforeBar() {
    // gotcha before bar!
  }/*

  [MixinHook(on="bar")]
  public*/ function doSomethingOnBar() {
    // gotcha on bar!
  }/*

  [MixinHook("baz")]
  public*/ function doSomethingOnBaz() {
    // gotcha on baz!
  }/*

  [MixinHook(before="one", before="two", after="three", on="four", "five")]
  public*/ function doSomethingOnMany() {
    // gotcha on many!
  }/*

  [MixinHook(before="six")]
  [MixinHook(after="seven")]
  public*/ function doSomethingOnBoth() {
    // gotcha on both!
  }/*

  [MixinHook(extended)]
  private static*/ function extendClass$static(baseClass/*:Class*/, derivedClass/*:Class*/, classBody/*: Object*/)/*:void*/ {
    // gotcha extended!
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      mixinConfig: {
        before: {
          foo: "doSomethingBeforeFoo$jHtF",
          bar: "doSomethingBeforeBar",
          one: "doSomethingOnMany",
          two: "doSomethingOnMany",
          six: "doSomethingOnBoth"
        },
        after: {
          foo: "doSomethingAfterFoo",
          three: "doSomethingOnMany",
          seven: "doSomethingOnBoth"
        },
        on: {
          bar: "doSomethingOnBar",
          baz: "doSomethingOnBaz",
          four: "doSomethingOnMany",
          five: "doSomethingOnMany"
        },
        extended: extendClass$static
      },
      doSomethingBeforeFoo$jHtF: doSomethingBeforeFoo,
      doSomethingAfterFoo: doSomethingAfterFoo,
      doSomethingBeforeBar: doSomethingBeforeBar,
      doSomethingOnBar: doSomethingOnBar,
      doSomethingOnBaz: doSomethingOnBaz,
      doSomethingOnMany: doSomethingOnMany,
      doSomethingOnBoth: doSomethingOnBoth
    };
});
