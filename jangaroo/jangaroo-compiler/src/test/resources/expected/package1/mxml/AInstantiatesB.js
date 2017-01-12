Ext.define("package1.mxml.AInstantiatesB", function(AInstantiatesB) {/*package package1.mxml{
import ext.*;
import package1.mxml.*;
import net.jangaroo.ext.Exml;
public class AInstantiatesB extends Panel{

    private var config:AInstantiatesB;

    public native function AInstantiatesB(config:AInstantiatesB = null);override protected*/function initConfig(_config/*:Object*/)/*:void*/{
    var config/*:AInstantiatesB*/ =AS3.cast(AInstantiatesB,_config);
    var config_$1/*:AInstantiatesB*/ =AS3.cast(AInstantiatesB,{});
    var defaults_$1/*:AInstantiatesB*/ =AS3.cast(AInstantiatesB,{});
    config= net.jangaroo.ext.Exml.apply(defaults_$1,config);
    var local_BDeclaresA_12_5_$1/*: package1.mxml.BDeclaresA*/ =AS3.cast(package1.mxml.BDeclaresA,{});
    local_BDeclaresA_12_5_$1["someProperty"] = "yes";
    config_$1["items"] = [new package1.mxml.BDeclaresA(local_BDeclaresA_12_5_$1)]; net.jangaroo.ext.Exml.apply(config_$1,config);ext.Panel.prototype.initConfig.call(this,config_$1);
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "ext.Panel",
      config$4: null,
      initConfig: initConfig,
      requires: ["ext.Panel"],
      uses: [
        "net.jangaroo.ext.Exml",
        "package1.mxml.BDeclaresA"
      ]
    };
});
