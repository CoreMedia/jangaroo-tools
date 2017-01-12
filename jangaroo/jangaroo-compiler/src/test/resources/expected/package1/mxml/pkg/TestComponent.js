Ext.define("package1.mxml.pkg.TestComponent", function(TestComponent) {/*package package1.mxml.pkg{
import package1.mxml.pkg.*;
import net.jangaroo.ext.Exml;
public class TestComponent extends TestComponentBase{override protected*/function initConfig(_config/*:Object*/)/*:void*/{
    var config/*:TestComponent*/ =AS3.cast(TestComponent,_config);
    var config_$1/*:TestComponent*/ =AS3.cast(TestComponent,{});
    var defaults_$1/*:TestComponent*/ =AS3.cast(TestComponent,{});
    AS3.setBindable(defaults_$1,"property_1" , "withDefault");
    config= net.jangaroo.ext.Exml.apply(defaults_$1,config);
    config_$1["emptyText"] = '<div class=\'widget-content-list-empty\'>' + package1.mxml.pkg.TestComponentBase.DEFAULT + '</div>';
    config_$1["letters"] = [
              'a',
              'b',
              'c'
             ]; net.jangaroo.ext.Exml.apply(config_$1, config);package1.mxml.pkg.TestComponentBase.prototype.initConfig.call(this,config_$1);
}/*
[Bindable]
public var property_1:String;
[Bindable]
public var property_2:int;}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.mxml.pkg.TestComponentBase",
      initConfig: initConfig,
      config: {
        property_1: null,
        property_2: 0
      },
      requires: ["package1.mxml.pkg.TestComponentBase"],
      uses: ["net.jangaroo.ext.Exml"]
    };
});
