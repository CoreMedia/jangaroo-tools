/*package package1.mxml{
import package1.*;
import package1.mxml.SimpleInterface;
import net.jangaroo.ext.Exml;*/
Ext.define("package1.mxml.ScriptCdataMxmlClass", function(ScriptCdataMxmlClass) {/*public class ScriptCdataMxmlClass extends ConfigClass implements package1.mxml.SimpleInterface{

    import package1.someOtherPackage.SomeOtherClass;

    private var field1:SomeOtherClass = null;
    protected var field2:Vector.<String> =*/function field2_(){this.field2=(/* new <String>*/["a", "b"]);}/*;
    public var field3:Vector.<int> =*/function field3_(){this.field3=(/* new <int>*/[1, 2, 3]);}/*;

    public*/ function doIt(/*...values*/)/*:void*/ {var values=Array.prototype.slice.call(arguments);
      for (var $1=0;$1</* in*/ values.length;++$1) {var v/*:Object*/ =String($1);
        throw "cannot do it with " + v;
      }
    }/*public*/function ScriptCdataMxmlClass$(config/*:ScriptCdataMxmlClass=null*/){if(arguments.length<=0)config=null;
    this.super$5omj(net.jangaroo.ext.Exml.apply( AS3.cast(package1.ConfigClass,{
    foo: "bar"
    }),config));
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
        field2_.call(this);
        field3_.call(this);
      },
      requires: [
        "package1.ConfigClass",
        "package1.mxml.SimpleInterface"
      ],
      uses: ["net.jangaroo.ext.Exml"]
    };
});
