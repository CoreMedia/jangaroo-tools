package package1.mxml.pkg {

public class PropertiesAccessBase {

  public function PropertiesAccessBase(config:PropertiesAccess = null) {
    property_1 = config.property_1 += "_HI";
    property_2 = 123;
    property_3 = config.property_3 || "";
  }

  public var property_1:String;
  private var _property_2:String;

  public function set property_2(value:*):void {
    _property_2 = String(value);
  }

  public function get property_2():String {
    return _property_2;
  }

  public native function set property_3(value:String):void;

  public native function get property_3():String;
}
}