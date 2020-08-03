/*package package1.mxml{
import ext.*;*/
Ext.define("package1.mxml.StringToEmptyArrayCoercion", function(StringToEmptyArrayCoercion) {/*public class StringToEmptyArrayCoercion extends Panel{public*/function StringToEmptyArrayCoercion$(config/*:StringToEmptyArrayCoercion=null*/){this.super$4x7N();if(arguments.length<=0)config=null;
    var config_$1/*: ext.Panel*/ =AS3.cast(ext.Panel,{});
    config_$1.items = [];
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "ext.Panel",
      constructor: StringToEmptyArrayCoercion$,
      super$4x7N: function() {
        ext.Panel.prototype.constructor.apply(this, arguments);
      },
      requires: ["ext.Panel"]
    };
});
