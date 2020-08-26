/*package package1.mxml.pkg{
import package1.mxml.pkg.*;
import net.jangaroo.ext.Exml;*/
Ext.define("package1.mxml.pkg.CyclicDependencies", function(CyclicDependencies) {/*public class CyclicDependencies extends Object{

    public*/function CyclicDependencies$(config/*:CyclicDependencies = null*/){if(arguments.length<=0)config=null;net.jangaroo.ext.Exml.apply(this,config);
  }/*

    [Bindable]
    public var cause_trouble:package1.mxml.pkg.CyclicDependencies_1;}}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: CyclicDependencies$,
      config: {cause_trouble: null},
      uses: ["net.jangaroo.ext.Exml"]
    };
});
