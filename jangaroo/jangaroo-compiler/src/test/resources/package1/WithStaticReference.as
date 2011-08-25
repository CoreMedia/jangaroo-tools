package package1{
public class WithStaticReference {
  public static const BLA = "bla";
  public function WithStaticReference() {
    var bla = WithStaticReference.BLA;
    bla is WithStaticReference;
    new WithStaticReference();
  }
  public static function make():void {
    var bla = WithStaticReference.BLA;
    bla is WithStaticReference;
    new WithStaticReference();
  }
  public function make2():void {
    var bla = WithStaticReference.BLA;
    bla is WithStaticReference;
    new WithStaticReference();
  }
}
}