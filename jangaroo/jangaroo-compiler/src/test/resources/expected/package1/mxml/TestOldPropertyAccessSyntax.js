/*package package1.mxml{
import net.jangaroo.ext.Exml;*/
Ext.define("package1.mxml.TestOldPropertyAccessSyntax", function(TestOldPropertyAccessSyntax) {/*public class TestOldPropertyAccessSyntax extends Object{

    import testPackage.PropertiesTest_properties;

    public static const BUNDLE:PropertiesTest_properties =*/function BUNDLE$static_(){TestOldPropertyAccessSyntax.BUNDLE=( test.package2.PropertiesTest_properties.INSTANCE);}/*;public*/function TestOldPropertyAccessSyntax$(config/*:TestOldPropertyAccessSyntax=null*/){if(arguments.length<=0)config=null;
    this.foo =net.jangaroo.ext.Exml.asString( TestOldPropertyAccessSyntax.BUNDLE.key + "\""); net.jangaroo.ext.Exml.apply(this,config);
}/*

    [Bindable]
    public var foo:String;}}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: TestOldPropertyAccessSyntax$,
      config: {foo: null},
      statics: {
        BUNDLE: undefined,
        __initStatics__: function() {
          BUNDLE$static_();
        }
      },
      uses: [
        "net.jangaroo.ext.Exml",
        "test.package2.PropertiesTest_properties"
      ]
    };
});
