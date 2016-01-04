package package1{

[Event(name="click", type="package1.someOtherPackage.SomeEvent")]

public class ConfigClass {

  public function ConfigClass(config:ConfigClass = null) {
  }

  public var foo:String = "foo";

  public var number:int;

  public native function get items():Array;

  [DefaultProperty]
  [AllowConstructorParameters]
  public native function set items(value:Array):void;

}
}