Ext.define("package1.mxml.pkg.PropertiesAccess", function(PropertiesAccess) {/*package package1.mxml.pkg{
import package1.mxml.pkg.*;
import net.jangaroo.ext.Exml;
public class PropertiesAccess extends PropertiesAccessBase{public*/function PropertiesAccess$(config/*:PropertiesAccess=null*/){package1.mxml.pkg.PropertiesAccessBase.prototype.constructor.call(this);if(arguments.length<=0)config=null; net.jangaroo.ext.Exml.apply(this,{
        property_1: "egal",
        property_2: "egaler",
        property_3: "am egalsten"});
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.mxml.pkg.PropertiesAccessBase",
      constructor: PropertiesAccess$,
      requires: ["package1.mxml.pkg.PropertiesAccessBase"],
      uses: ["net.jangaroo.ext.Exml"]
    };
});
