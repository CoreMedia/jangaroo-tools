import KeyEvent from "@jangaroo/runtime/KeyEvent";


class TestKeyEvent {
  static someEventCallback(keyEvent: KeyboardEvent):boolean {
    if (keyEvent.keyCode === KeyEvent.DOM_VK_ENTER) {
      return true;
    }
    return false;
  }
}
export default TestKeyEvent;
