Ext.define("package1.mxml.UndefinedTypeInBinding", function(UndefinedTypeInBinding) {/*package package1.mxml{
import package1.*;
import net.jangaroo.ext.Exml;
public class UndefinedTypeInBinding extends ConfigClass{public*/function UndefinedTypeInBinding$(config/*:UndefinedTypeInBinding=null*/){package1.ConfigClass.prototype.constructor.call(this);if(arguments.length<=0)config=null; net.jangaroo.ext.Exml.apply(this,{
        items: [function()/*:**/{
          return /* UndefinedType.mxml would raise compiler error, but is only scoped, so all is fine: */ AS3.cast(package1.mxml.UndefinedType,null);
        }]});
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.ConfigClass",
      constructor: UndefinedTypeInBinding$,
      requires: ["package1.ConfigClass"],
      uses: [
        "net.jangaroo.ext.Exml",
        "package1.mxml.UndefinedType"
      ]
    };
});
