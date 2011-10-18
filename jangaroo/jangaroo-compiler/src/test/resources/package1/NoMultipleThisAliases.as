package package1{
public class NoMultipleThisAliases {
  public function NoMultipleThisAliases() {
    function foo1():void {
      method();
    }
    function foo2():void {
      method();
    }
  }

  private function method():void {}
}
}