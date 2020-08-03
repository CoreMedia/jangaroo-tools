/*package package2 {*/
Ext.define("package2.TestExtPrivate", function(TestExtPrivate) {/*public class TestExtPrivate {

  public*/ function canOverride()/*:void*/ {
    // This method can be overridden in subclasses.
  }/*

  [ExtPrivate]
  public*/ function canOnlyOverrideIfAnnotated(foo/*:String*/)/*:Boolean*/ {
    // This method can only be overridden in subclasses if annotated with [ExtPrivate].
  }/*
}*/function TestExtPrivate$() {}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      canOverride: canOverride,
      constructor: TestExtPrivate$,
      privates: {canOnlyOverrideIfAnnotated: canOnlyOverrideIfAnnotated}
    };
});
