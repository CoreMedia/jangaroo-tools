package package2 {
import ext.Panel;
import ext.events.PanelEvent;

public class TestEventListener {

  public function TestEventListener() {
    var panel:Panel = Panel({});
    panel.title = "not yet clicked.";
    panel.addEventListener( PanelEvent.FLOPS, function(event:PanelEvent):void {
      panel.title = "clicked!";
    } );
  }
}
}