package package1 {
import package1.someOtherPackage.SomeOtherClass;

public class NoPrimitiveInit {
  public function NoPrimitiveInit() {
  }

  private function method(i:int):int {
    return SomeOtherClass.BLA + int.MAX_VALUE;
  }
}
}