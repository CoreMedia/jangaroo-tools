Ext.define("package1.Interface", function(Interface) {/*package package1 {

/**
 * Some ASDoc.
 * /
// this comment should vanish in the API!
public interface Interface extends SuperInterface {
  function doSomething():String;

  function get property():String;
}
}

============================================== Jangaroo part ==============================================*/
    return {mixins: ["package1.SuperInterface"]};
});
