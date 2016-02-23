package package1.mxml.pkg {


public class TestComponentBase implements TestInterface {

  [ExtConfig]
  [Bindable]
  /**
   * @private
   */
  public native function set store(value:*):void;

  private var property_1:String;
  private var property_2:int;

  public function TestComponentBase(config:TestComponent = null) {
    this.property_1 = config.property_1 += "_HI";
    this.property_2 = config.property_2 || 0;
  }

  private var component:Object;

  public function init(component:Object):void {
    this.component = component;
  }
}
}