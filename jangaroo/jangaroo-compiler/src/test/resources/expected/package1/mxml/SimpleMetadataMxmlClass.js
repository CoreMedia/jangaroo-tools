Ext.define("AS3.package1.mxml.SimpleMetadataMxmlClass", function(SimpleMetadataMxmlClass) {/*package package1.mxml{
import package1.*;
[ShortVersion]
public class SimpleMetadataMxmlClass extends ConfigClass{public*/function SimpleMetadataMxmlClass$(config/*:SimpleMetadataMxmlClass=null*/){AS3.package1.ConfigClass.prototype.constructor.call(this);if(arguments.length<=0)config=null;
    var config_$1/*:SimpleMetadataMxmlClass*/ =AS3.cast(SimpleMetadataMxmlClass,{});
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "AS3.package1.ConfigClass",
      metadata: {"": ["ShortVersion"]},
      constructor: SimpleMetadataMxmlClass$,
      requires: ["AS3.package1.ConfigClass"]
    };
});
