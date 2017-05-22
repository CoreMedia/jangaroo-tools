Ext.define("package1.mxml.StringToArrayCoersion", function(StringToArrayCoersion) {/*package package1.mxml{
import ext.*;
import net.jangaroo.ext.Exml;
public class StringToArrayCoersion extends Panel{public*/function StringToArrayCoersion$(config/*:StringToArrayCoersion=null*/){ext.Panel.prototype.constructor.call(this);if(arguments.length<=0)config=null; net.jangaroo.ext.Exml.apply(this,{
           items: ["just a joke"]});
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "ext.Panel",
      constructor: StringToArrayCoersion$,
      requires: ["ext.Panel"],
      uses: ["net.jangaroo.ext.Exml"]
    };
});
