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

  public native function get defaultType():String;

  public native function set defaultType(value:String):void;

  private var _title:String = "- empty -";

  [Bindable]
  public function get title():String {
    return _title;
  }

  [Bindable]
  public function set title(value:String):void {
    _title = value;
  }
}
}