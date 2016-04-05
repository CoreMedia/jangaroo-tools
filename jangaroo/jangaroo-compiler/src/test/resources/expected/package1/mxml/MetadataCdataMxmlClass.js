Ext.define("AS3.package1.mxml.MetadataCdataMxmlClass", function(MetadataCdataMxmlClass) {/*package package1.mxml{
import package1.*;

    /**
     * Let's have a class with two annotations.
     * @see {@link http://help.adobe.com/en_US/flex/using/WSd0ded3821e0d52fe1e63e3d11c2f44bc36-7ff2.html}
     * /
    [ThisIsJustATest]
    [Deprecated (replacement='use.this.please')]
public class MetadataCdataMxmlClass extends ConfigClass{public*/function MetadataCdataMxmlClass$(config/*:MetadataCdataMxmlClass=null*/){AS3.package1.ConfigClass.prototype.constructor.call(this);if(arguments.length<=0)config=null;
    var config_$1/*:MetadataCdataMxmlClass*/ =AS3.cast(MetadataCdataMxmlClass,{});
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
      constructor: MetadataCdataMxmlClass$,
      requires: ["AS3.package1.ConfigClass"]
    };
});
