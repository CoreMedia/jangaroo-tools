Ext.define("AS3.package1.mxml.MetadataMxmlClass", function(MetadataMxmlClass) {/*package package1.mxml{
import package1.*;

    [ThisIsJustATest]
    [Deprecated (replacement='use.this.please')]
public class MetadataMxmlClass extends ConfigClass{public*/function MetadataMxmlClass$(config/*:MetadataMxmlClass=null*/){AS3.package1.ConfigClass.prototype.constructor.call(this);if(arguments.length<=0)config=null;
var config_$1/*:MetadataMxmlClass*/ =AS3.cast(MetadataMxmlClass,{});
}/*}}

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
