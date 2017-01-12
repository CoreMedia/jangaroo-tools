Ext.define("package1.mxml.TestOldPropertyAccessSyntax", function(TestOldPropertyAccessSyntax) {/*package package1.mxml{
import ext.*;
public class TestOldPropertyAccessSyntax extends Base{

    import testPackage.PropertiesTest_properties;

    public static const BUNDLE:PropertiesTest_properties =*/function BUNDLE$static_(){TestOldPropertyAccessSyntax.BUNDLE=( testPackage.PropertiesTest_properties.INSTANCE);}/*;override protected*/function initConfig(_config/*:Object*/)/*:void*/{
    var config/*:TestOldPropertyAccessSyntax*/ =AS3.cast(TestOldPropertyAccessSyntax,_config);
    this["foo"] = TestOldPropertyAccessSyntax.BUNDLE.key;
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "ext.Base",
      initConfig: initConfig,
      statics: {
        BUNDLE: undefined,
        __initStatics__: function() {
          BUNDLE$static_();
        }
      },
      requires: ["testPackage.PropertiesTest_properties"]
    };
});
