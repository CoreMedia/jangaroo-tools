package package1{
import ext.mixin.Observable;

/**
 * click event documentation.
 * @eventType package1.someOtherPackage.ConfigClass_stranger_antonEvent.CLICK
 */
[Event(name="onClick", type="package1.someOtherPackage.ConfigClass_stranger_antonEvent")]

public class ConfigClass extends Observable {

  public function ConfigClass(config:ConfigClass = null) {
  }

  public var foo:String = "foo",
          number:int;

  public native function get items():Array;

  [DefaultProperty]
  [ExtConfig(create)]
  public native function set items(value:Array):void;

  public native function get defaultType():String;

  public native function set defaultType(value:String):void;

  public native function get defaults():*;

  [ExtConfig(extractXType="defaultType")]
  public native function set defaults(value:*):void;

  private var _title:String = "- empty -";

  [Bindable]
  public function get title():String {
    return _title;
  }

  [Bindable]
  public function set title(value:String):void {
    _title = value;
  }

  // Using [Bindable] on a normal method does not make sense,
  // but should be ignored:
  [Bindable]
  public function bogus(value:String):void {}
}
}