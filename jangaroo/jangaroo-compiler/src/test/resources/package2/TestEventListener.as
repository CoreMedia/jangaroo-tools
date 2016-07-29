package package2 {
import ext.Panel;
import ext.events.PanelEvent;

public class TestEventListener {

  private var panel:Panel = Panel({});
  [ArrayElementType("ext.Panel")]
  private var panels:Array = [];

  public function TestEventListener() {
    panel.title = "not yet clicked.";
    panel.addEventListener( PanelEvent.FLOPS, function(event:PanelEvent):void {
      getThis().getPanel().title = "clicked!";
      panel.layout.getOwner().title = "clicked!";
      panels.push(panel);
      panels[0].title = "yes, clicked!";
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