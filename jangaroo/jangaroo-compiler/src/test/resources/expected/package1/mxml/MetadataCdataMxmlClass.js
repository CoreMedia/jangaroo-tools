Ext.define("package1.mxml.MetadataCdataMxmlClass", function(MetadataCdataMxmlClass) {/*package package1.mxml{
import package1.*;
import net.jangaroo.ext.Exml;

    /**
     * Let's have a class with two annotations.
     * @see {@link http://help.adobe.com/en_US/flex/using/WSd0ded3821e0d52fe1e63e3d11c2f44bc36-7ff2.html}
     * /
    [ThisIsJustATest]
    [Deprecated (replacement='use.this.please')]
public class MetadataCdataMxmlClass extends ConfigClass{override protected*/function initConfig(_config/*:Object*/)/*:void*/{
    var config/*:MetadataCdataMxmlClass*/ =AS3.cast(MetadataCdataMxmlClass,_config);
    var config_$1/*:MetadataCdataMxmlClass*/ =AS3.cast(MetadataCdataMxmlClass,{});
    var defaults_$1/*:MetadataCdataMxmlClass*/ =AS3.cast(MetadataCdataMxmlClass,{});
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
