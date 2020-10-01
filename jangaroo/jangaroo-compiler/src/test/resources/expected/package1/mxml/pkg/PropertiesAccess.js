/*package package1.mxml.pkg{
import package1.mxml.pkg.*;
import net.jangaroo.ext.Exml;*/
Ext.define("package1.mxml.pkg.PropertiesAccess", function(PropertiesAccess) {/*public class PropertiesAccess extends PropertiesAccessBase{public*/function PropertiesAccess$(config/*:PropertiesAccess=null*/){if(arguments.length<=0)config=null;
    this.super$drFQ(net.jangaroo.ext.Exml.apply(AS3.cast(PropertiesAccess,{
        property_1: "egal",
        property_2: "egaler",
        property_3: "am egalsten"
}),config));
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.mxml.pkg.PropertiesAccessBase",
      constructor: PropertiesAccess$,
      super$drFQ: function() {
        package1.mxml.pkg.PropertiesAccessBase.prototype.constructor.apply(this, arguments);
      },
      requires: ["package1.mxml.pkg.PropertiesAccessBase"],
      uses: ["net.jangaroo.ext.Exml"]
    };
});
