Ext.define("package1.mxml.SimpleMetadataMxmlClass", function(SimpleMetadataMxmlClass) {/*package package1.mxml{
import package1.*;
[ShortVersion]
public class SimpleMetadataMxmlClass extends ConfigClass{public*/function SimpleMetadataMxmlClass$(config/*:SimpleMetadataMxmlClass=null*/){this.super$3();if(arguments.length<=0)config=null;
    var config_$1/*:SimpleMetadataMxmlClass*/ =AS3.cast(SimpleMetadataMxmlClass,{});
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.ConfigClass",
      metadata: {"": ["ShortVersion"]},
      constructor: SimpleMetadataMxmlClass$,
      super$3: function() {
        package1.ConfigClass.prototype.constructor.apply(this, arguments);
      },
      requires: ["package1.ConfigClass"]
    };
});
