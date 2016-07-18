Ext.define("package1.mxml.pkg.PropertiesAccess", function(PropertiesAccess) {/*package package1.mxml.pkg{
import package1.mxml.pkg.*;
public class PropertiesAccess extends PropertiesAccessBase{public*/function PropertiesAccess$(config/*:PropertiesAccess=null*/){package1.mxml.pkg.PropertiesAccessBase.prototype.constructor.apply(this,arguments);if(arguments.length<=0)config=null;
    var config_$1/*:PropertiesAccess*/ =AS3.cast(PropertiesAccess,{});
    config_$1["property_1"] = "egal";
    config_$1["property_2"] = "egaler";
    config_$1["property_3"] = "am egalsten";
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.mxml.pkg.PropertiesAccessBase",
      constructor: PropertiesAccess$,
      requires: ["package1.mxml.pkg.PropertiesAccessBase"]
    };
});
