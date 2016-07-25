package ext {
import ext.layout.ContainerLayout;
import ext.mixin.Observable;

[Event(name="onFlipFlop", type="ext.events.PanelEvent")]

public class Panel extends Observable {

  public function Panel(config:Object) {
  }

  [Bindable]
  public native function get title():String;

  [Bindable]
  public native function set title(value:String):void;

  [DefaultProperty]
  public native function get items():Array;

  public native function set items(value:Array):void;

  public native function get layout():ContainerLayout;

  public native function set layout(value:ContainerLayout):void;
}
}