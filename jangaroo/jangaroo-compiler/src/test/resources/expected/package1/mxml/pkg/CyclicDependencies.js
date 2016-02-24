Ext.define("AS3.package1.mxml.pkg.CyclicDependencies", function(CyclicDependencies) {/*package package1.mxml.pkg{
import package1.mxml.pkg.*;
public class CyclicDependencies extends Object{

    private var config:CyclicDependencies;

    public*/function CyclicDependencies$(config/*:CyclicDependencies = null*/){if(arguments.length<=0)config=null;

    var config_$3/*:package1.mxml.pkg.CyclicDependencies_1*/ =AS3.cast( AS3.package1.mxml.pkg.CyclicDependencies_1,{});AS3.setBindable(
    this,"cause_trouble" , new AS3.package1.mxml.pkg.CyclicDependencies_1(config_$3));}/*

[Bindable]
public var cause_trouble:package1.mxml.pkg.CyclicDependencies_1;}}

============================================== Jangaroo part ==============================================*/
    return {
      config$1: null,
      constructor: CyclicDependencies$,
      config: {cause_trouble: null},
      uses: ["AS3.package1.mxml.pkg.CyclicDependencies_1"]
    };
});
