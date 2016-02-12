Ext.define("AS3.package1.mxml.SimpleMetadataMxmlClass", function(SimpleMetadataMxmlClass) {/*package package1.mxml{
import package1.*;
import package1.ConfigClass;
[ShortVersion]
class SimpleMetadataMxmlClass extends ConfigClass{}*/function SimpleMetadataMxmlClass$() {AS3.package1.ConfigClass.prototype.constructor.call(this);}/*}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "AS3.package1.ConfigClass",
      metadata: {"": ["ShortVersion"]},
      constructor: SimpleMetadataMxmlClass$,
      requires: ["AS3.package1.ConfigClass"]
    };
});
