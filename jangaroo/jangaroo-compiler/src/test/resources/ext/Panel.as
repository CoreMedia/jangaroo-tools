package ext {
import ext.mixin.Observable;

[Event(name="onFlipFlop", type="ext.events.PanelEvent")]

public class Panel extends Observable {

  public function Panel(config:Object) {
  }

  public native function get title():String;

  public native function set title(value:String):void;

  [DefaultProperty]
  public native function get items():Array;

  public native function set items(value:Array):void;
}
}