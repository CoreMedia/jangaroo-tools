package package1{
import ext.mixin.Observable;

[Event(name="click", type="package1.someOtherPackage.SomeEvent")]

public class ConfigClass extends Observable {

  public function ConfigClass(config:ConfigClass = null) {
  }

  public var foo:String = "foo";

  public var number:int;

  public native function get items():Array;

  [DefaultProperty]
  [ExtConfig(create)]
  public native function set items(value:Array):void;

  public native function get defaults():*;

  [ExtConfig(extractXType="defaultType")]
  public native function set defaults(value:*):void;

  public native function get title():String;

  [Bindable]
  public native function set title(value:String):void;
}
}