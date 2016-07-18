Ext.define("package1.mxml.StringToEmptyArrayCoersion", function(StringToEmptyArrayCoersion) {/*package package1.mxml{
import ext.*;
public class StringToEmptyArrayCoersion extends Panel{public*/function StringToEmptyArrayCoersion$(config/*:StringToEmptyArrayCoersion=null*/){ext.Panel.prototype.constructor.apply(this,arguments);if(arguments.length<=0)config=null;
    var config_$1/*:StringToEmptyArrayCoersion*/ =AS3.cast(StringToEmptyArrayCoersion,{});
    config_$1["items"] = [];
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "ext.Panel",
      constructor: StringToEmptyArrayCoersion$,
      requires: ["ext.Panel"]
    };
});
