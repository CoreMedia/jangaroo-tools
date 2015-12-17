package package1 {
public class PrivateMemberAccess {

  public static const INSTANCE:PrivateMemberAccess = new PrivateMemberAccess();
  private var secret:String;

  public static function doSomething():String {
    return INSTANCE.secret;
  }
}
}