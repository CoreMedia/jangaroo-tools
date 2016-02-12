Ext.define("AS3.package1.mxml.DeclarationsMxmlClass", function(DeclarationsMxmlClass) {/*package package1.mxml{
import package1.someOtherPackage.*;
import package1.someOtherPackage.SomeNativeClass;
class DeclarationsMxmlClass extends SomeNativeClass{}*/function DeclarationsMxmlClass$() {AS3.package1.someOtherPackage.SomeNativeClass.prototype.constructor.call(this);}/*}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "AS3.package1.someOtherPackage.SomeNativeClass",
      constructor: DeclarationsMxmlClass$,
      requires: ["AS3.package1.someOtherPackage.SomeNativeClass"]
    };
});
