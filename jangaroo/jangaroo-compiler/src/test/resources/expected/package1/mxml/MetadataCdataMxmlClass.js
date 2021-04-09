/*package package1.mxml{
import package1.*;
import net.jangaroo.ext.Exml;

    /**
     * Let's have a class with two annotations.
     * @see {@link http://help.adobe.com/en_US/flex/using/WSd0ded3821e0d52fe1e63e3d11c2f44bc36-7ff2.html}
     * /
    [ThisIsJustATest]
    [Deprecated (replacement='use.this.please')]*/
/**
 * The class level comment for the component.
 * This tag supports all ASDoc tags,
 * and does not require a CDATA block.
 */
Ext.define("package1.mxml.MetadataCdataMxmlClass", function(MetadataCdataMxmlClass) {/*public class MetadataCdataMxmlClass extends ConfigClass{public*/function MetadataCdataMxmlClass$(config/*:MetadataCdataMxmlClass=null*/){if(arguments.length<=0)config=null;
    this.super$3$jH(net.jangaroo.ext.Exml.apply(AS3.cast(MetadataCdataMxmlClass,{
}),config));
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.ConfigClass",
      metadata: {"": ["ThisIsJustATest"]},
      constructor: MetadataCdataMxmlClass$,
      super$3$jH: function() {
        package1.ConfigClass.prototype.constructor.apply(this, arguments);
      },
      requires: ["package1.ConfigClass"],
      uses: ["net.jangaroo.ext.Exml"]
    };
});
