/*package package1.mxml{
import package1.*;
import net.jangaroo.ext.Exml;
[ShortVersion]*/
Ext.define("package1.mxml.SimpleMetadataMxmlClass", function(SimpleMetadataMxmlClass) {/*public class SimpleMetadataMxmlClass extends ConfigClass{public*/function SimpleMetadataMxmlClass$(config/*:SimpleMetadataMxmlClass=null*/){if(arguments.length<=0)config=null;
    this.super$mXNi(net.jangaroo.ext.Exml.apply(AS3.cast(SimpleMetadataMxmlClass,{
}),config));
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.ConfigClass",
      metadata: {"": ["ShortVersion"]},
      constructor: SimpleMetadataMxmlClass$,
      super$mXNi: function() {
        package1.ConfigClass.prototype.constructor.apply(this, arguments);
      },
      requires: ["package1.ConfigClass"],
      uses: ["net.jangaroo.ext.Exml"]
    };
});
