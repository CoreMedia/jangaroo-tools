Ext.define("package1.mxml.ScriptCdataMxmlClass", function(ScriptCdataMxmlClass) {/*package package1.mxml{
import package1.*;
import package1.mxml.SimpleInterface;
public class ScriptCdataMxmlClass extends ConfigClass implements package1.mxml.SimpleInterface{

    import package1.someOtherPackage.SomeOtherClass;

    private var field1:SomeOtherClass = null;
    protected var field2:Vector.<String> =*/function field2_(){this.field2=(/* new <String>*/["a", "b"]);}/*;
    public var field3:Vector.<int> =*/function field3_(){this.field3=(/* new <int>*/[1, 2, 3]);}/*;

    public*/ function doIt(/*...values*/)/*:void*/ {var values=Array.prototype.slice.call(arguments);
      for (var v/*:Object*/ in values) {
        throw "cannot do it with " + v;
      }
    }/*public*/function ScriptCdataMxmlClass$(config/*:ScriptCdataMxmlClass=null*/){this.super$5omj();field2_.call(this);field3_.call(this);if(arguments.length<=0)config=null;
    var config_$1/*: package1.ConfigClass*/ =AS3.cast(package1.ConfigClass,{});
    config_$1.foo = "bar";
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.ConfigClass",
      mixins: ["package1.mxml.SimpleInterface"],
      field1$5omj: null,
      doIt: doIt,
      constructor: ScriptCdataMxmlClass$,
      super$5omj: function() {
        package1.ConfigClass.prototype.constructor.apply(this, arguments);
      },
      requires: [
        "package1.ConfigClass",
        "package1.mxml.SimpleInterface"
      ]
    };
});
