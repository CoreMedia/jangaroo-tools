package js{

[Native("KeyEvent")]
public class KeyEvent {
  public native function get keyCode() : Number;

  public static const DOM_VK_ENTER : Number = 14;
}
}
