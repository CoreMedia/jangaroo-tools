Ext.define("package1.mxml.InterfaceImplementingMxmlClass", function(InterfaceImplementingMxmlClass) {/*package package1.mxml{
import package1.*;
import package1.mxml.YetAnotherInterface;
import net.jangaroo.ext.Exml;
public class InterfaceImplementingMxmlClass extends ConfigClass implements package1.mxml.YetAnotherInterface{

    public native function createInstance(o:SimpleInterface):package1.mxml.SimpleClass;

    [Bindable]
    public native function get someProperty():String;

    /** @private * /
    [Bindable]
    public native function set someProperty(value:String):void;override protected*/function initConfig(_config/*:Object*/)/*:void*/{
    var config/*:InterfaceImplementingMxmlClass*/ =AS3.cast(InterfaceImplementingMxmlClass,_config);
    var config_$1/*:InterfaceImplementingMxmlClass*/ =AS3.cast(InterfaceImplementingMxmlClass,{});
    var defaults_$1/*:InterfaceImplementingMxmlClass*/ =AS3.cast(InterfaceImplementingMxmlClass,{});
    config= net.jangaroo.ext.Exml.apply(defaults_$1,config); net.jangaroo.ext.Exml.apply(config_$1, config);package1.ConfigClass.prototype.initConfig.call(this,config_$1);
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.ConfigClass",
      mixins: ["package1.mxml.YetAnotherInterface"],
      initConfig: initConfig,
      requires: [
        "package1.ConfigClass",
        "package1.mxml.YetAnotherInterface"
      ],
      uses: ["net.jangaroo.ext.Exml"]
    };
});
