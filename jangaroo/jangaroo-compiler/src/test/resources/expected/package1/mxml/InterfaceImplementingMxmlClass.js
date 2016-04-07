Ext.define("AS3.package1.mxml.InterfaceImplementingMxmlClass", function(InterfaceImplementingMxmlClass) {/*package package1.mxml{
import package1.*;
import package1.mxml.YetAnotherInterface;
public class InterfaceImplementingMxmlClass extends ConfigClass implements package1.mxml.YetAnotherInterface{

    public native function createInstance(o:SimpleInterface):package1.mxml.SimpleClass;public*/function InterfaceImplementingMxmlClass$(config/*:InterfaceImplementingMxmlClass=null*/){AS3.package1.ConfigClass.prototype.constructor.call(this);if(arguments.length<=0)config=null;
    var config_$1/*:InterfaceImplementingMxmlClass*/ =AS3.cast(InterfaceImplementingMxmlClass,{});
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "AS3.package1.ConfigClass",
      mixins: ["AS3.package1.mxml.YetAnotherInterface"],
      constructor: InterfaceImplementingMxmlClass$,
      requires: [
        "AS3.package1.ConfigClass",
        "AS3.package1.mxml.YetAnotherInterface"
      ]
    };
});
