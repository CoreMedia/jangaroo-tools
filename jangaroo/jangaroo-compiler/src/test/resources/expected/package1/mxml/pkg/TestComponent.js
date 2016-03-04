Ext.define("AS3.package1.mxml.pkg.TestComponent", function(TestComponent) {/*package package1.mxml.pkg{
import package1.mxml.pkg.*;
import net.jangaroo.ext.Exml;
public class TestComponent extends TestComponentBase{

    private var config:TestComponent;

    public*/function TestComponent$(config/*:TestComponent = null*/){if(arguments.length<=0)config=null;
var config_$1/*:TestComponent*/ =AS3.cast(TestComponent,{});
var defaults_$1/*:TestComponent*/ ={};
config= AS3.net.jangaroo.ext.Exml.apply(defaults_$1,config);

    AS3.setBindable(defaults_$1,"property_1" , "withDefault");
    config_$1.emptyText = '<div class=\'widget-content-list-empty\'>' + AS3.package1.mxml.pkg.TestComponentBase.DEFAULT + '</div>';
    config_$1.letters = [              'a',              'b',              'c'             ]; AS3.net.jangaroo.ext.Exml.apply(config_$1,config);AS3.package1.mxml.pkg.TestComponentBase.prototype.constructor.call(this,config_$1);}/*

[Bindable]
public var property_1:String;
[Bindable]
public var property_2:int;}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "AS3.package1.mxml.pkg.TestComponentBase",
      config$2: null,
      constructor: TestComponent$,
      config: {
        property_1: null,
        property_2: 0
      },
      requires: ["AS3.package1.mxml.pkg.TestComponentBase"],
      uses: ["AS3.net.jangaroo.ext.Exml"]
    };
});
