Ext.define("package1.mxml.StringToArrayCoersion", function(StringToArrayCoersion) {/*package package1.mxml{
import ext.*;
public class StringToArrayCoersion extends Panel{public*/function StringToArrayCoersion$(config/*:StringToArrayCoersion=null*/){ext.Panel.prototype.constructor.call(this);if(arguments.length<=0)config=null;
    var config_$1/*:StringToArrayCoersion*/ =AS3.cast(StringToArrayCoersion,{});
    config_$1.items = ["just a joke"];
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "ext.Panel",
      constructor: StringToArrayCoersion$,
      requires: ["ext.Panel"]
    };
});
