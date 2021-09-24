package package2 {
import ext.Panel;
import ext.events.PanelEvent;

public class TestEventListener {

  private var panel:Panel = Panel({});
  [ArrayElementType("ext.Panel")]
  private var panels:Array = [];

  [ArrayElementType("ext.Panel")]
  private function getPanels():Array {
    return panels;
  }

  public function TestEventListener() {
    panel.title = "not yet clicked.";
    panel.addEventListener( PanelEvent.FLIP_FLOP, function(event:PanelEvent):void {
      getThis().getPanel().title = "clicked!";
      panel.layout.getOwner().title = "clicked!";
      panels.push(panel);
      getPanels()[0].title = "yes, clicked!";
    } );
  }

  public function getThis():TestEventListener {
    return this;
  }

  public function getPanel():Panel {
    return this.panel;
  }
}
}