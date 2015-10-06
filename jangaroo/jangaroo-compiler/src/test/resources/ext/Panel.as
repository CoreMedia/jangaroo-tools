package ext {

public class Panel {

  public function Panel(config:Object) {
  }

  [DefaultProperty]
  public native function get items():Array;

  public native function set items(value:Array):void;
}
}