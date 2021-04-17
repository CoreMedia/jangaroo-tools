import IResourceManager from "../mx/resources/IResourceManager";
import ResourceBundle_properties from "./ResourceBundle_properties";


class TestRequireResourceBundle {

  testResourceManagerAccess(rm:IResourceManager) {
    var propertyValue = ResourceBundle_properties.someProperty;
  }
}
export default TestRequireResourceBundle;
