package package1 {

public class SuperCallParameters extends ManyConstructorParameters {
  public function SuperCallParameters() {
    super("bar", -1, -4.2, true, {}, []);
  }

  override public function isEmpty(str:String):Boolean {
    return super.isEmpty(str);
  }
}
}