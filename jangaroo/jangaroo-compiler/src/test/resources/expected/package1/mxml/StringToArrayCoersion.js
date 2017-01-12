Ext.define("package1.mxml.StringToArrayCoersion", function(StringToArrayCoersion) {/*package package1.mxml{
import ext.*;
import net.jangaroo.ext.Exml;
public class StringToArrayCoersion extends Panel{override protected*/function initConfig(_config/*:Object*/)/*:void*/{
    var config/*:StringToArrayCoersion*/ =AS3.cast(StringToArrayCoersion,_config);
    var config_$1/*:StringToArrayCoersion*/ =AS3.cast(StringToArrayCoersion,{});
    var defaults_$1/*:StringToArrayCoersion*/ =AS3.cast(StringToArrayCoersion,{});
    config= net.jangaroo.ext.Exml.apply(defaults_$1,config);
    config_$1["items"] = ["just a joke"]; net.jangaroo.ext.Exml.apply(config_$1,config);ext.Panel.prototype.initConfig.call(this,config_$1);
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "ext.Panel",
      initConfig: initConfig,
      requires: ["ext.Panel"],
      uses: ["net.jangaroo.ext.Exml"]
    };
});
