package package1{

[Event(name="click", type="package1.someOtherPackage.SomeEvent")]

public class ConfigClass {

  public function ConfigClass(config:ConfigClass = null) {
  }

  public var foo:String = "foo";

  public var number:int;

  public native function get items():Array;

  [DefaultProperty]
  [AllowConstructorParameters(false)]
  public native function set items(value:Array):void;

  public native function get defaults():*;

  [AllowConstructorParameters(suppressType)]
  public native function set defaults(value:*):void;
}
}