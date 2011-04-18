package error {
public class TestPropertyAccessFromStaticCode {

  public function TestPropertyAccessFromStaticCode() {
  }

  //todo include this '{' once we can handle member declarations within blocks
  // {
    var propWithPackageInternalScope : String = "something";

    static function willRaiseException() :String {
      return propWithPackageInternalScope;
    }
  // }

}
}