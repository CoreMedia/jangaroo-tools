/*package package1.mxml{
import ext.*;
import package1.mxml.*;
import net.jangaroo.ext.Exml;*/
Ext.define("package1.mxml.AInstantiatesB", function(AInstantiatesB) {/*public class AInstantiatesB extends Panel{

    public*/function AInstantiatesB$(config/*:AInstantiatesB = null*/){if(arguments.length<=0)config=null;
    this.super$2Olv(net.jangaroo.ext.Exml.apply(AS3.cast(AInstantiatesB,{

  items:[
    AS3.cast(package1.mxml.BDeclaresA,{ someProperty: "yes"})
  ]
}),config));
  }/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "Ext.Panel",
      constructor: AInstantiatesB$,
      super$2Olv: function() {
        Ext.Panel.prototype.constructor.apply(this, arguments);
      },
      requires: ["Ext.Panel"],
      uses: [
        "net.jangaroo.ext.Exml",
        "package1.mxml.BDeclaresA"
      ]
    };
});
