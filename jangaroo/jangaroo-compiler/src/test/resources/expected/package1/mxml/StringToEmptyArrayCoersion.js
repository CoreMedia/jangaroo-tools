Ext.define("package1.mxml.StringToEmptyArrayCoersion", function(StringToEmptyArrayCoersion) {/*package package1.mxml{
import ext.*;
import net.jangaroo.ext.Exml;
public class StringToEmptyArrayCoersion extends Panel{override protected*/function initConfig(_config/*:Object*/)/*:void*/{
    var config/*:StringToEmptyArrayCoersion*/ =AS3.cast(StringToEmptyArrayCoersion,_config);
    var config_$1/*:StringToEmptyArrayCoersion*/ =AS3.cast(StringToEmptyArrayCoersion,{});
    var defaults_$1/*:StringToEmptyArrayCoersion*/ =AS3.cast(StringToEmptyArrayCoersion,{});
    config= net.jangaroo.ext.Exml.apply(defaults_$1,config);
    config_$1["items"] = []; net.jangaroo.ext.Exml.apply(config_$1,config);ext.Panel.prototype.initConfig.call(this,config_$1);
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "ext.Panel",
      initConfig: initConfig,
      requires: ["ext.Panel"],
      uses: ["net.jangaroo.ext.Exml"]
    };
});
