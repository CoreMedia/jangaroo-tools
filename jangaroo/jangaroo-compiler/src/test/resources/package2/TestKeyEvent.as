package package2 {
import js.KeyEvent;

public class TestKeyEvent {
  public static function someEventCallback(keyEvent: KeyEvent):Boolean {
    if (keyEvent.keyCode === KeyEvent.DOM_VK_ENTER) {
      return true;
    }
    return false;
  }
}
}