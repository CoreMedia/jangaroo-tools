Ext.define("package1.mxml.TestOldPropertyAccessSyntax", function(TestOldPropertyAccessSyntax) {/*package package1.mxml{
public class TestOldPropertyAccessSyntax extends Object{

    import testPackage.PropertiesTest_properties;

    public static const BUNDLE:PropertiesTest_properties =*/function BUNDLE$static_(){TestOldPropertyAccessSyntax.BUNDLE=( testPackage.PropertiesTest_properties.INSTANCE);}/*;public*/function TestOldPropertyAccessSyntax$()/*:void*/{
    this["foo"] = TestOldPropertyAccessSyntax.BUNDLE.key;
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: TestOldPropertyAccessSyntax$,
      statics: {
        BUNDLE: undefined,
        __initStatics__: function() {
          BUNDLE$static_();
        }
      },
      requires: ["testPackage.PropertiesTest_properties"]
    };
});
