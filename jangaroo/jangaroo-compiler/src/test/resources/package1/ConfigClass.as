package package1{

[Event(name="click", type="package1.someOtherPackage.SomeEvent")]

public class ConfigClass {

  public function ConfigClass() {
  }

  public var foo:String = "foo";

  public var number:int;

  [DefaultProperty]
  public var items:Array;

}
}