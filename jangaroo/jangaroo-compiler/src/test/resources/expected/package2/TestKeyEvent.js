/*package package2 {
import js.KeyEvent;*/

Ext.define("package2.TestKeyEvent", function(TestKeyEvent) {/*public class TestKeyEvent {
  public static*/ function someEventCallback$static(keyEvent/*: KeyEvent*/)/*:Boolean*/ {
    if (keyEvent.keyCode === KeyEvent.DOM_VK_ENTER) {
      return true;
    }
    return false;
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {statics: {someEventCallback: someEventCallback$static}};
});
