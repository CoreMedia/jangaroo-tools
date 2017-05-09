Ext.define("package1.mxml.InterfaceImplementingMxmlClass", function(InterfaceImplementingMxmlClass) {/*package package1.mxml{
import package1.*;
import package1.mxml.YetAnotherInterface;
import net.jangaroo.ext.Exml;
public class InterfaceImplementingMxmlClass extends ConfigClass implements package1.mxml.YetAnotherInterface{

    public*/function InterfaceImplementingMxmlClass$(config/*:InterfaceImplementingMxmlClass = null*/){if(arguments.length<=0)config=null;
    var config_$1/*:InterfaceImplementingMxmlClass*/ =AS3.cast(InterfaceImplementingMxmlClass,{});
    var defaults_$1/*:InterfaceImplementingMxmlClass*/ =AS3.cast(InterfaceImplementingMxmlClass,{});
    config = net.jangaroo.ext.Exml.apply(defaults_$1,config);
    net.jangaroo.ext.Exml.apply(config_$1,config);
    package1.ConfigClass.prototype.constructor.call(this,config_$1);
  }/*

    public native function createInstance(o:SimpleInterface):package1.mxml.SimpleClass;

    [Bindable]
    public native function get someProperty():String;

    /** @private * /
    [Bindable]
    public native function set someProperty(value:String):void;}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.ConfigClass",
      mixins: ["package1.mxml.YetAnotherInterface"],
      constructor: InterfaceImplementingMxmlClass$,
      requires: [
        "package1.ConfigClass",
        "package1.mxml.YetAnotherInterface"
      ],
      uses: ["net.jangaroo.ext.Exml"]
    };
});
