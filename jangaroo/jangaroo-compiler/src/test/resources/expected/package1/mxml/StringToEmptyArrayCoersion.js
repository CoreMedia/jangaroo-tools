Ext.define("package1.mxml.StringToEmptyArrayCoersion", function(StringToEmptyArrayCoersion) {/*package package1.mxml{
import ext.*;
public class StringToEmptyArrayCoersion extends Panel{public*/function StringToEmptyArrayCoersion$(config/*:StringToEmptyArrayCoersion=null*/){this.super$3();if(arguments.length<=0)config=null;
    var config_$1/*:StringToEmptyArrayCoersion*/ =AS3.cast(StringToEmptyArrayCoersion,{});
    config_$1.items = [];
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "ext.Panel",
      constructor: StringToEmptyArrayCoersion$,
      super$3: function() {
        ext.Panel.prototype.constructor.apply(this, arguments);
      },
      requires: ["ext.Panel"]
    };
});
