package package1 {

public class ConfigSubclass extends ConfigClass {

  override public function get defaultType():String {
    return super.defaultType + "!";
  }

  [Bindable]
  override public function set title(value:String):void {
    super.title = value + "!";
  }
}
}