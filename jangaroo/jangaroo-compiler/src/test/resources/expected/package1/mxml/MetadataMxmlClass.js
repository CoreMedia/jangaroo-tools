Ext.define("AS3.package1.mxml.MetadataMxmlClass", function(MetadataMxmlClass) {/*package package1.mxml{
import package1.*;
import package1.ConfigClass;

    [ThisIsJustATest]
    [Deprecated (replacement='use.this.please')]
class MetadataMxmlClass extends ConfigClass{}*/function MetadataMxmlClass$() {AS3.package1.ConfigClass.prototype.constructor.call(this);}/*}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "AS3.package1.ConfigClass",
      metadata: {"": [
        "ThisIsJustATest",
        "Deprecated",
        [
          "replacement",
          "use.this.please"
        ]
      ]},
      constructor: MetadataMxmlClass$,
      requires: ["AS3.package1.ConfigClass"]
    };
});