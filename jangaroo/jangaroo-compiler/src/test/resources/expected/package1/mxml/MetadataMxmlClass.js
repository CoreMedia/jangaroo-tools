Ext.define("package1.mxml.MetadataMxmlClass", function(MetadataMxmlClass) {/*package package1.mxml{
import package1.*;
import net.jangaroo.ext.Exml;

    [ThisIsJustATest]
    [Deprecated (replacement='use.this.please')]
public class MetadataMxmlClass extends ConfigClass{override protected*/function initConfig(_config/*:Object*/)/*:void*/{
    var config/*:MetadataMxmlClass*/ =AS3.cast(MetadataMxmlClass,_config);
    var config_$1/*:MetadataMxmlClass*/ =AS3.cast(MetadataMxmlClass,{});
    var defaults_$1/*:MetadataMxmlClass*/ =AS3.cast(MetadataMxmlClass,{});
    config= net.jangaroo.ext.Exml.apply(defaults_$1,config); net.jangaroo.ext.Exml.apply(config_$1,config);package1.ConfigClass.prototype.initConfig.call(this,config_$1);
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.ConfigClass",
      metadata: {"": [
        "ThisIsJustATest",
        "Deprecated",
        [
          "replacement",
          "use.this.please"
        ]
      ]},
      initConfig: initConfig,
      requires: ["package1.ConfigClass"],
      uses: ["net.jangaroo.ext.Exml"]
    };
});
