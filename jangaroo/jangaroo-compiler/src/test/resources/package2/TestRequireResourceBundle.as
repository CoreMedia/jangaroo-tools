package package2 {
import mx.resources.IResourceManager;

[ResourceBundle("package2.ResourceBundle")]
public class TestRequireResourceBundle {

  public function testResourceManagerAccess(rm:IResourceManager) {
    var propertyValue:String = rm.getString("package2.ResourceBundle", "someProperty");
  }
}
}