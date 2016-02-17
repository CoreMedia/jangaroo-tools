Ext.define("AS3.package1.mxml.ScriptCdataMxmlClass", function(ScriptCdataMxmlClass) {/*package package1.mxml{
import package1.*;
import package1.ConfigClass;
import package1.mxml.SimpleInterface;
class ScriptCdataMxmlClass extends ConfigClass implements SimpleInterface{

    import package1.someOtherPackage.SomeOtherClass;

    private var field1:SomeOtherClass = null;
    protected var field2:Vector$object.<String> =*/function field2_(){this.field2=(/* new <String>*/["a", "b"]);}/*;
    public var field3:Vector$object.<int> =*/function field3_(){this.field3=(/* new <int>*/[1, 2, 3]);}/*;

    internal*/ function doIt(/*...values*/)/*:void*/ {var values=Array.prototype.slice.call(arguments);
      for (var v/*:Object*/ in values) {
        throw "cannot do it with " + v;
      }
    }/*}*/function ScriptCdataMxmlClass$() {AS3.package1.ConfigClass.prototype.constructor.call(this);field2_.call(this);field3_.call(this);}/*}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "AS3.package1.ConfigClass",
      mixins: ["AS3.package1.mxml.SimpleInterface"],
      field1$2: null,
      doIt: doIt,
      constructor: ScriptCdataMxmlClass$,
      requires: [
        "AS3.package1.ConfigClass",
        "AS3.package1.mxml.SimpleInterface"
      ]
    };
});
