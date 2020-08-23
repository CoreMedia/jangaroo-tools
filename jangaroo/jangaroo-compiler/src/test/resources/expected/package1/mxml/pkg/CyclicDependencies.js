/*package package1.mxml.pkg{
import package1.mxml.pkg.*;
import net.jangaroo.ext.Exml;*/
Ext.define("package1.mxml.pkg.CyclicDependencies", function(CyclicDependencies) {/*public class CyclicDependencies extends Object{

    public*/function CyclicDependencies$(config/*:CyclicDependencies = null*/){if(arguments.length<=0)config=null;
    AS3.setBindable(this,"cause_trouble" , new package1.mxml.pkg.CyclicDependencies_1(AS3.cast(package1.mxml.pkg.CyclicDependencies_1,{
    })));net.jangaroo.ext.Exml.apply(this,config);
  }/*

    [Bindable]
    public var cause_trouble:package1.mxml.pkg.CyclicDependencies_1;}}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: CyclicDependencies$,
      config: {cause_trouble: null},
      uses: [
        "net.jangaroo.ext.Exml",
        "package1.mxml.pkg.CyclicDependencies_1"
      ]
    };
});
