Ext.define("package1.mxml.StringToEmptyArrayCoersion", function(StringToEmptyArrayCoersion) {/*package package1.mxml{
import ext.*;
import net.jangaroo.ext.Exml;
public class StringToEmptyArrayCoersion extends Panel{public*/function StringToEmptyArrayCoersion$(config/*:StringToEmptyArrayCoersion=null*/){ext.Panel.prototype.constructor.call(this);if(arguments.length<=0)config=null; net.jangaroo.ext.Exml.apply(this,{
  items:[]});
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "ext.Panel",
      constructor: StringToEmptyArrayCoersion$,
      requires: ["ext.Panel"],
      uses: ["net.jangaroo.ext.Exml"]
    };
});
