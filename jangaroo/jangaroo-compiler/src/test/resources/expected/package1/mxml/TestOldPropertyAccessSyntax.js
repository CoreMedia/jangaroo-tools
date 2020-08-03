/*package package1.mxml{*/
Ext.define("package1.mxml.TestOldPropertyAccessSyntax", function(TestOldPropertyAccessSyntax) {/*public class TestOldPropertyAccessSyntax extends Object{

    import testPackage.PropertiesTest_properties;

    public static const BUNDLE:PropertiesTest_properties =*/function BUNDLE$static_(){TestOldPropertyAccessSyntax.BUNDLE=( testPackage.PropertiesTest_properties.INSTANCE);}/*;public*/function TestOldPropertyAccessSyntax$(config/*:TestOldPropertyAccessSyntax=null*/){if(arguments.length<=0)config=null;
    AS3.setBindable(this,"foo" , TestOldPropertyAccessSyntax.BUNDLE.key + "\"");
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
      uses: ["testPackage.PropertiesTest_properties"]
    };
});
