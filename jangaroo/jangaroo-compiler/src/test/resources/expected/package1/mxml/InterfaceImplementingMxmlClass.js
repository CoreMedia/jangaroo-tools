Ext.define("package1.mxml.InterfaceImplementingMxmlClass", function(InterfaceImplementingMxmlClass) {/*package package1.mxml{
import package1.*;
import package1.mxml.YetAnotherInterface;
public class InterfaceImplementingMxmlClass extends ConfigClass implements package1.mxml.YetAnotherInterface{

    public native function createInstance(o:SimpleInterface):package1.mxml.SimpleClass;public*/function InterfaceImplementingMxmlClass$(config/*:InterfaceImplementingMxmlClass=null*/){package1.ConfigClass.prototype.constructor.call(this);if(arguments.length<=0)config=null;
    var config_$1/*:InterfaceImplementingMxmlClass*/ =AS3.cast(InterfaceImplementingMxmlClass,{});
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.ConfigClass",
      mixins: ["package1.mxml.YetAnotherInterface"],
      constructor: InterfaceImplementingMxmlClass$,
      requires: [
        "package1.ConfigClass",
        "package1.mxml.YetAnotherInterface"
      ]
    };
});
