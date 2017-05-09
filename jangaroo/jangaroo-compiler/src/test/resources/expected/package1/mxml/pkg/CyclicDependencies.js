Ext.define("package1.mxml.pkg.CyclicDependencies", function(CyclicDependencies) {/*package package1.mxml.pkg{
import package1.mxml.pkg.*;
public class CyclicDependencies extends Object{

    public*/function CyclicDependencies$(config/*:CyclicDependencies = null*/){if(arguments.length<=0)config=null;
    var cause_trouble_12_5_$1/*: package1.mxml.pkg.CyclicDependencies_1*/ =AS3.cast(package1.mxml.pkg.CyclicDependencies_1,{});
    AS3.setBindable(this,"cause_trouble" , new package1.mxml.pkg.CyclicDependencies_1(cause_trouble_12_5_$1));
  }/*

    [Bindable]
    public var cause_trouble:package1.mxml.pkg.CyclicDependencies_1;}}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: CyclicDependencies$,
      config: {cause_trouble: null},
      uses: ["package1.mxml.pkg.CyclicDependencies_1"]
    };
});
