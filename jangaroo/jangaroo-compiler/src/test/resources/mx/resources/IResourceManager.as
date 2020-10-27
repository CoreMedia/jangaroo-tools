package mx.resources {

public interface IResourceManager {
    [Parameter("bundleName", coerceTo="PropertiesClass")]
    function getString(bundleName:String, resourceName:String,
                       parameters:Array = null,
                       locale:String = null):String;
}
}
