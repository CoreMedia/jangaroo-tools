package mx.resources {
public interface IResourceManager {

  function getString(bundle:String, key:String):String;

  function addBundle(bundle:String, properties:Object):void;
}
}