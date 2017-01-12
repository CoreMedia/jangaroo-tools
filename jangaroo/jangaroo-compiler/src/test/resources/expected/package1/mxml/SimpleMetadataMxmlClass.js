Ext.define("package1.mxml.SimpleMetadataMxmlClass", function(SimpleMetadataMxmlClass) {/*package package1.mxml{
import package1.*;
import net.jangaroo.ext.Exml;
[ShortVersion]
public class SimpleMetadataMxmlClass extends ConfigClass{override protected*/function initConfig(_config/*:Object*/)/*:void*/{
    var config/*:SimpleMetadataMxmlClass*/ =AS3.cast(SimpleMetadataMxmlClass,_config);
    var config_$1/*:SimpleMetadataMxmlClass*/ =AS3.cast(SimpleMetadataMxmlClass,{});
    var defaults_$1/*:SimpleMetadataMxmlClass*/ =AS3.cast(SimpleMetadataMxmlClass,{});
    config= net.jangaroo.ext.Exml.apply(defaults_$1,config); net.jangaroo.ext.Exml.apply(config_$1,config);package1.ConfigClass.prototype.initConfig.call(this,config_$1);
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.ConfigClass",
      metadata: {"": ["ShortVersion"]},
      initConfig: initConfig,
      requires: ["package1.ConfigClass"],
      uses: ["net.jangaroo.ext.Exml"]
    };
});
