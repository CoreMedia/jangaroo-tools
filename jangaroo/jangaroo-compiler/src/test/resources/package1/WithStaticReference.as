package package1{
public class WithStaticReference {
  public static const BLA = "bla";
  public function WithStaticReference() {
    var bla = WithStaticReference.BLA;
    bla is WithStaticReference;
    this.make2();
  }
  public static function make():void {
    var bla = WithStaticReference.BLA;
    bla is WithStaticReference;
    new WithStaticReference();
  }
  private function make2():void {
    var bla = WithStaticReference.BLA;
    bla is WithStaticReference;
    new WithStaticReference();
  }
}
}