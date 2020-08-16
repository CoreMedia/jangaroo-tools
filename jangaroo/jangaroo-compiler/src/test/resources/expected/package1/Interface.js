/*package package1 {*/

/**
 * Some ASDoc.
 */
// this comment should vanish in the API!
Ext.define("package1.Interface", function(Interface) {/*public interface Interface extends SuperInterface {
  function doSomething():String;

  function get property():String;

  function set property(value:String):void;
}
}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.SuperInterface",
      requires: ["package1.SuperInterface"]
    };
});
