package mx.resources {

public class ResourceManagerImpl implements IResourceManager {

  private var localeChain:Array;
  private const propertiesByLocaleByBundleName:Object = {};

  public function setLocaleChain(localeChain:Array):void {
    this.localeChain = localeChain;
  }

  public function addBundle(bundleName:String, propertiesByLocale:Object):void {
    propertiesByLocaleByBundleName[bundleName] = propertiesByLocale;
  }

  public function getString(bundleName:String, key:String):String {
    var propertiesByLocale:Object = propertiesByLocaleByBundleName[bundleName];
    for (var i:int = 0; i < localeChain.length; i++) {
      var properties:Object = propertiesByLocale[localeChain[i]];
      if (properties && key in properties) {
        return properties[key];
      }
    }
    return null;
  }

}
}