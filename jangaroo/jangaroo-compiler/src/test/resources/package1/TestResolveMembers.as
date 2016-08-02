package package1 {
public class TestResolveMembers {

  public function TestResolveMembers() {
    // resolve Object methods on something typed by an interface:
    var someInterface:Interface;
    var str:String = someInterface.toString();
  }
}
}