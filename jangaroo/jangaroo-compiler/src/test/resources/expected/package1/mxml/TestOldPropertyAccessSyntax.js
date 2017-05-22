Ext.define("package1.mxml.TestOldPropertyAccessSyntax", function(TestOldPropertyAccessSyntax) {/*package package1.mxml{
import net.jangaroo.ext.Exml;
public class TestOldPropertyAccessSyntax extends Object{

    import testPackage.PropertiesTest_properties;

    public static const BUNDLE:PropertiesTest_properties =*/function BUNDLE$static_(){TestOldPropertyAccessSyntax.BUNDLE=( testPackage.PropertiesTest_properties.INSTANCE);}/*;public*/function TestOldPropertyAccessSyntax$(config/*:TestOldPropertyAccessSyntax=null*/){if(arguments.length<=0)config=null; net.jangaroo.ext.Exml.apply(this,{
           foo: TestOldPropertyAccessSyntax.BUNDLE.key});
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
      requires: ["testPackage.PropertiesTest_properties"],
      uses: ["net.jangaroo.ext.Exml"]
    };
});
