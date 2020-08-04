/*package package1.mxml{
import package1.*;

    [ThisIsJustATest]
    [Deprecated (replacement='use.this.please')]*/
Ext.define("package1.mxml.MetadataMxmlClass", function(MetadataMxmlClass) {/*public class MetadataMxmlClass extends ConfigClass{public*/function MetadataMxmlClass$(config/*:MetadataMxmlClass=null*/){if(arguments.length<=0)config=null;this.super$$82_();
    var config_$1/*: package1.ConfigClass*/ =AS3.cast(package1.ConfigClass,{});
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
      super$$82_: function() {
        package1.ConfigClass.prototype.constructor.apply(this, arguments);
      },
      requires: ["package1.ConfigClass"]
    };
});
