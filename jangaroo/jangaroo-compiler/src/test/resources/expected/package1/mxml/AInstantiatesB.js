/*package package1.mxml{
import ext.*;
import package1.mxml.*;
import net.jangaroo.ext.Exml;*/
Ext.define("package1.mxml.AInstantiatesB", function(AInstantiatesB) {/*public class AInstantiatesB extends Panel{

    public*/function AInstantiatesB$(config/*:AInstantiatesB = null*/){if(arguments.length<=0)config=null;
    this.super$2Olv(net.jangaroo.ext.Exml.apply({
    items:[ new package1.mxml.BDeclaresA({
    someProperty: "yes"
    })]
    },config));
  }/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "ext.Panel",
      constructor: AInstantiatesB$,
      super$2Olv: function() {
        ext.Panel.prototype.constructor.apply(this, arguments);
      },
      requires: ["ext.Panel"],
      uses: [
        "net.jangaroo.ext.Exml",
        "package1.mxml.BDeclaresA"
      ]
    };
});
