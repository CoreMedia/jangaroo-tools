Ext.define("package1.mxml.MetadataMxmlClass", function(MetadataMxmlClass) {/*package package1.mxml{
import package1.*;

    [ThisIsJustATest]
    [Deprecated (replacement='use.this.please')]
public class MetadataMxmlClass extends ConfigClass{public*/function MetadataMxmlClass$(config/*:MetadataMxmlClass=null*/){package1.ConfigClass.prototype.constructor.apply(this,arguments);if(arguments.length<=0)config=null;
    var config_$1/*:MetadataMxmlClass*/ =AS3.cast(MetadataMxmlClass,{});
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.ConfigClass",
      metadata: {"": [
        "ThisIsJustATest",
        "Deprecated",
        [
          "replacement",
          "use.this.please"
        ]
      ]},
      constructor: MetadataMxmlClass$,
      requires: ["package1.ConfigClass"]
    };
});
