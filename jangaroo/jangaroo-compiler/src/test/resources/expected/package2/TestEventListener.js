/*package package2 {
import ext.Panel;
import ext.events.PanelEvent;*/

Ext.define("package2.TestEventListener", function(TestEventListener) {/*public class TestEventListener {

  private var panel:Panel =*/function panel_(){this.panel$iwqQ=( AS3.cast(ext.Panel,{}));}/*;
  [ArrayElementType("ext.Panel")]
  private var panels:Array =*/function panels_(){this.panels$iwqQ=( []);}/*;

  [ArrayElementType("ext.Panel")]
  private*/ function getPanels()/*:Array*/ {
    return this.panels$iwqQ;
  }/*

  public*/ function TestEventListener$() {var _this=this;this.super$iwqQ();
    AS3.setBindable(this.panel$iwqQ,"title" , "not yet clicked.");
    AS3.addEventListener(panel, ext.events.PanelEvent,"FLOPS", function(event/*:PanelEvent*/)/*:void*/ {
      AS3.setBindable(_this.getThis().getPanel(),"title" , "clicked!");
      AS3.setBindable(_this.panel$iwqQ.layout.getOwner(),"title" , "clicked!");
      _this.panels$iwqQ.push(_this.panel$iwqQ);
      AS3.setBindable(_this.getPanels$iwqQ()[0],"title" , "yes, clicked!");
    } );
  }/*

  public*/ function getThis()/*:TestEventListener*/ {
    return this;
  }/*

  public*/ function getPanel()/*:Panel*/ {
    return this.panel$iwqQ;
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      getPanels$iwqQ: getPanels,
      constructor: TestEventListener$,
      super$iwqQ: function() {
        panel_.call(this);
        panels_.call(this);
      },
      getThis: getThis,
      getPanel: getPanel,
      uses: [
        "ext.Panel",
        "ext.events.PanelEvent"
      ]
    };
});
