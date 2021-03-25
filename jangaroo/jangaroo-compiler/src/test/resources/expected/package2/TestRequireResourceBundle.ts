import ResourceBundle_properties from './ResourceBundle_properties';
import IResourceManager from '../mx/resources/IResourceManager';



class TestRequireResourceBundle {

  testResourceManagerAccess(rm:IResourceManager) {
    var propertyValue = ResourceBundle_properties.someProperty;
  }
}
export default TestRequireResourceBundle;
