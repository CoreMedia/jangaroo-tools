Ext.define("package1.mxml.pkg.CyclicDependencies", function(CyclicDependencies) {/*package package1.mxml.pkg{
import ext.*;
import package1.mxml.pkg.*;
public class CyclicDependencies extends Base{override protected*/function initConfig(_config/*:Object*/)/*:void*/{
    var config/*:CyclicDependencies*/ =AS3.cast(CyclicDependencies,_config);
    var cause_trouble_8_5_$1/*: package1.mxml.pkg.CyclicDependencies_1*/ =AS3.cast(package1.mxml.pkg.CyclicDependencies_1,{});
    AS3.setBindable(this,"cause_trouble" , new package1.mxml.pkg.CyclicDependencies_1(cause_trouble_8_5_$1));
}/*
[Bindable]
public var cause_trouble:package1.mxml.pkg.CyclicDependencies_1;}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "ext.Base",
      initConfig: initConfig,
      config: {cause_trouble: null},
      uses: ["package1.mxml.pkg.CyclicDependencies_1"]
    };
});
