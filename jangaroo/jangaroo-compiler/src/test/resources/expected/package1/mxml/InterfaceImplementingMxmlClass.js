Ext.define("AS3.package1.mxml.InterfaceImplementingMxmlClass", function(InterfaceImplementingMxmlClass) {/*package package1.mxml{
import package1.*;
import package1.SuperInterface;class InterfaceImplementingMxmlClass extends ConfigClass implements package1{}*/function InterfaceImplementingMxmlClass$() {AS3.package1.ConfigClass.prototype.constructor.call(this);}/*}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "AS3.package1.ConfigClass",
      mixins: ["AS3.package1.SuperInterface"],
      constructor: InterfaceImplementingMxmlClass$,
      requires: [
        "AS3.package1.ConfigClass",
        "AS3.package1.SuperInterface"
      ]
    };
});
