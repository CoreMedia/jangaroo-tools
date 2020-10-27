/*package package2 {
import mx.resources.IResourceManager;

[ResourceBundle("package2.ResourceBundle")]*/
Ext.define("package2.TestRequireResourceBundle", function(TestRequireResourceBundle) {/*public class TestRequireResourceBundle {

  public*/ function testResourceManagerAccess(rm/*:IResourceManager*/) {
    var propertyValue/*:String*/ = rm.getString(package2.ResourceBundle_properties, "someProperty");
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      testResourceManagerAccess: testResourceManagerAccess,
      requires: ["package2.ResourceBundle_properties"]
    };
});
