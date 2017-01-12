Ext.define("package1.mxml.ScriptCdataMxmlClass", function(ScriptCdataMxmlClass) {/*package package1.mxml{
import package1.*;
import package1.mxml.SimpleInterface;
import net.jangaroo.ext.Exml;
public class ScriptCdataMxmlClass extends ConfigClass implements package1.mxml.SimpleInterface{

    import package1.someOtherPackage.SomeOtherClass;

    private var field1:SomeOtherClass = null;
    protected var field2:Vector.<String> =*/function field2_(){this.field2=(/* new <String>*/["a", "b"]);}/*;
    public var field3:Vector.<int> =*/function field3_(){this.field3=(/* new <int>*/[1, 2, 3]);}/*;

    public*/ function doIt(/*...values*/)/*:void*/ {var values=Array.prototype.slice.call(arguments);
      for (var v/*:Object*/ in values) {
        throw "cannot do it with " + v;
      }
    }/*override protected*/function initConfig(_config/*:Object*/)/*:void*/{
    var config/*:ScriptCdataMxmlClass*/ =AS3.cast(ScriptCdataMxmlClass,_config);
    var config_$1/*:ScriptCdataMxmlClass*/ =AS3.cast(ScriptCdataMxmlClass,{});
    var defaults_$1/*:ScriptCdataMxmlClass*/ =AS3.cast(ScriptCdataMxmlClass,{});
    config= net.jangaroo.ext.Exml.apply(defaults_$1,config);
    config_$1["foo"] = "bar"; net.jangaroo.ext.Exml.apply(config_$1,config);package1.ConfigClass.prototype.initConfig.call(this,config_$1);
}/*}*/function ScriptCdataMxmlClass$() {package1.ConfigClass.prototype.constructor.apply(this,arguments);field2_.call(this);field3_.call(this);}/*}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.ConfigClass",
      mixins: ["package1.mxml.SimpleInterface"],
      field1$4: null,
      doIt: doIt,
      initConfig: initConfig,
      constructor: ScriptCdataMxmlClass$,
      requires: [
        "package1.ConfigClass",
        "package1.mxml.SimpleInterface"
      ],
      uses: ["net.jangaroo.ext.Exml"]
    };
});
