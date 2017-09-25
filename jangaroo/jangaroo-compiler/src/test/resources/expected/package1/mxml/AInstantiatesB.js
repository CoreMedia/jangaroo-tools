Ext.define("package1.mxml.AInstantiatesB", function(AInstantiatesB) {/*package package1.mxml{
import ext.*;
import package1.mxml.*;
import net.jangaroo.ext.Exml;
public class AInstantiatesB extends Panel{

    public*/function AInstantiatesB$(config/*:AInstantiatesB = null*/){if(arguments.length<=0)config=null;
    var config_$1/*: ext.Panel*/ =AS3.cast(ext.Panel,{});
    var defaults_$1/*:AInstantiatesB*/ =AS3.cast(AInstantiatesB,{});
    config = net.jangaroo.ext.Exml.apply(defaults_$1,config);
    var local_BDeclaresA_12_5_$1/*: package1.mxml.BDeclaresA*/ =AS3.cast(package1.mxml.BDeclaresA,{});
    AS3.setBindable(local_BDeclaresA_12_5_$1,"someProperty" , "yes");
    config_$1.items = [new package1.mxml.BDeclaresA(local_BDeclaresA_12_5_$1)];
    net.jangaroo.ext.Exml.apply(config_$1,config);
    this.super$3(config_$1);
  }/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "ext.Panel",
      constructor: AInstantiatesB$,
      super$3: function() {
        ext.Panel.prototype.constructor.apply(this, arguments);
      },
      requires: ["ext.Panel"],
      uses: [
        "net.jangaroo.ext.Exml",
        "package1.mxml.BDeclaresA"
      ]
    };
});
