Ext.define("package1.mxml.StringToArrayCoercion", function(StringToArrayCoercion) {/*package package1.mxml{
import ext.*;
public class StringToArrayCoercion extends Panel{public*/function StringToArrayCoercion$(config/*:StringToArrayCoercion=null*/){this.super$Sy5g();if(arguments.length<=0)config=null;
    var config_$1/*: ext.Panel*/ =AS3.cast(ext.Panel,{});
    config_$1.items = ["just a joke"];
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "ext.Panel",
      constructor: StringToArrayCoercion$,
      super$Sy5g: function() {
        ext.Panel.prototype.constructor.apply(this, arguments);
      },
      requires: ["ext.Panel"]
    };
});
