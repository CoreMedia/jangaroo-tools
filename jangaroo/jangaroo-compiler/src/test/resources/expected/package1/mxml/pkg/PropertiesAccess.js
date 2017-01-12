Ext.define("package1.mxml.pkg.PropertiesAccess", function(PropertiesAccess) {/*package package1.mxml.pkg{
import package1.mxml.pkg.*;
import net.jangaroo.ext.Exml;
public class PropertiesAccess extends PropertiesAccessBase{override protected*/function initConfig(_config/*:Object*/)/*:void*/{
    var config/*:PropertiesAccess*/ =AS3.cast(PropertiesAccess,_config);
    var config_$1/*:PropertiesAccess*/ =AS3.cast(PropertiesAccess,{});
    var defaults_$1/*:PropertiesAccess*/ =AS3.cast(PropertiesAccess,{});
    config= net.jangaroo.ext.Exml.apply(defaults_$1,config);
    config_$1["property_1"] = "egal";
    config_$1["property_2"] = "egaler";
    config_$1["property_3"] = "am egalsten"; net.jangaroo.ext.Exml.apply(config_$1,config);package1.mxml.pkg.PropertiesAccessBase.prototype.initConfig.call(this,config_$1);
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.mxml.pkg.PropertiesAccessBase",
      initConfig: initConfig,
      requires: ["package1.mxml.pkg.PropertiesAccessBase"],
      uses: ["net.jangaroo.ext.Exml"]
    };
});
