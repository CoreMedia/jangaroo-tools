Ext.define("package2.TestEventListener", function(TestEventListener) {/*package package2 {
import ext.Panel;
import ext.events.PanelEvent;

public class TestEventListener {

  public*/ function TestEventListener$() {
    var panel/*:Panel*/ = AS3.cast(ext.Panel,{});
    panel.title = "not yet clicked.";
    AS3.addEventListener(panel, ext.events.PanelEvent,"FLOPS", function(event/*:PanelEvent*/)/*:void*/ {
      panel.title = "clicked!";
    } );
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: TestEventListener$,
      uses: [
        "ext.Panel",
        "ext.events.PanelEvent"
      ]
    };
});
