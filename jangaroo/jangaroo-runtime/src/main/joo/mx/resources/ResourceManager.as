package mx.resources {

public class ResourceManager {

  private static const instance:IResourceManager = new ResourceManagerImpl();

  public static function getInstance():IResourceManager {
    return instance;
  }

}
}