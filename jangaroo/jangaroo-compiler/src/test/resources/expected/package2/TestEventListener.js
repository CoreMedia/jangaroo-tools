Ext.define("AS3.package2.TestEventListener", function(TestEventListener) {/*package package2 {
import ext.Panel;
import ext.events.PanelEvent;

public class TestEventListener {

  public*/ function TestEventListener$() {
    var panel/*:Panel*/ = AS3.cast(AS3.ext.Panel,{});
    panel.title = "not yet clicked.";
    AS3.addEventListener(panel, AS3.ext.events.PanelEvent,"FLOPS", function(event/*:PanelEvent*/)/*:void*/ {
      panel.title = "clicked!";
    } );
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: TestEventListener$,
      uses: [
        "AS3.ext.Panel",
        "AS3.ext.events.PanelEvent"
      ]
    };
});
