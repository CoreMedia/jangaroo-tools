Ext.define("package1.mxml.StringToArrayCoersion", function(StringToArrayCoersion) {/*package package1.mxml{
import ext.*;
public class StringToArrayCoersion extends Panel{public*/function StringToArrayCoersion$(config/*:StringToArrayCoersion=null*/){this.super$3();if(arguments.length<=0)config=null;
    var config_$1/*: ext.Panel*/ =AS3.cast(ext.Panel,{});
    config_$1.items = ["just a joke"];
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "ext.Panel",
      constructor: StringToArrayCoersion$,
      super$3: function() {
        ext.Panel.prototype.constructor.apply(this, arguments);
      },
      requires: ["ext.Panel"]
    };
});
