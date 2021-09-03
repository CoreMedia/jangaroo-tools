package ext.events {
import ext.Component;
import net.jangaroo.ext.FlExtEvent;

[Native("Ext.events.PanelEvent", require)]
public class PanelEvent extends FlExtEvent {

  public static const FLIPFLOP:String = "onFlipFlop";

  public static const __PARAMETER_SEQUENCE__:Array = ["source", "eOpts"];

  public function PanelEvent() {
  }

  public native function get source(): Component;
}
}