Ext.define("package1.mxml.AInstantiatesB", function(AInstantiatesB) {/*package package1.mxml{
import ext.*;
import package1.mxml.*;
import net.jangaroo.ext.Exml;
public class AInstantiatesB extends Panel{

    public*/function AInstantiatesB$(config/*:AInstantiatesB = null*/){if(arguments.length<=0)config=null;config = net.jangaroo.ext.Exml.apply(AS3.cast(AInstantiatesB,{

  items:[
    new package1.mxml.BDeclaresA({ someProperty: "yes"})
  ]}),config);
    ext.Panel.prototype.constructor.call(this,config);
  }/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "ext.Panel",
      constructor: AInstantiatesB$,
      requires: ["ext.Panel"],
      uses: [
        "net.jangaroo.ext.Exml",
        "package1.mxml.BDeclaresA"
      ]
    };
});
