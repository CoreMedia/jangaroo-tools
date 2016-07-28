package package1.mxml {
public interface YetAnotherInterface {
  function createInstance(obj:package1.mxml.SimpleInterface):SimpleClass;

  [Bindable]
  function get someProperty():String;

  [Bindable]
  function set someProperty(value:String):void;
}
}