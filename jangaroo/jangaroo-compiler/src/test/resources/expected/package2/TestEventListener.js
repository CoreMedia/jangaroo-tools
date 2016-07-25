Ext.define("package2.TestEventListener", function(TestEventListener) {/*package package2 {
import ext.Panel;
import ext.events.PanelEvent;

public class TestEventListener {

  private var panel:Panel =*/function panel_(){this.panel$1=( AS3.cast(ext.Panel,{}));}/*;

  public*/ function TestEventListener$() {var this$=this;panel_.call(this);
    AS3.setBindable(this.panel$1,"title" , "not yet clicked.");
    AS3.addEventListener(panel, ext.events.PanelEvent,"FLOPS", function(event/*:PanelEvent*/)/*:void*/ {
      AS3.setBindable(this$.getThis().getPanel(),"title" , "clicked!");
    } );
  }/*

  public*/ function getThis()/*:TestEventListener*/ {
    return this;
  }/*

  public*/ function getPanel()/*:Panel*/ {
    return this.panel$1;
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: TestEventListener$,
      getThis: getThis,
      getPanel: getPanel,
      uses: [
        "ext.Panel",
        "ext.events.PanelEvent"
      ]
    };
});
