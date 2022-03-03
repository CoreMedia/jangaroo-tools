package package1{
import ext.Panel;

public final class ImplementsInterface implements Interface {

  /**
   * Field with ASDoc.
   * Second line.
   */
  public var foo;
  
  /**
   * Annotated field with ASDoc.
   */
  [Bar]
  public var bar:Vector.<Vector.<Panel>>;

  public function ImplementsInterface() {
    // nothing really
  }

  public function doSomething():String {
    bar = new Vector.<Vector.<Panel>>();
    var panels:Vector.<Panel> = new Vector.<Panel>;
    panels.push(new Panel({}));
    bar.push(panels);
    bar[0][0].title = "Gotcha!";
  }

  public function get property():String {
    return "prefix" + foo;
  }

  public function set property(value:String):void {
    foo = value.substr("prefix".length);
  }
}
}