package package1 {

[Uses ("package1.ManyConstructorParameters")]
public class SuperCallParameters extends package1.ManyConstructorParameters {
  public function SuperCallParameters() {
    super(null, 0, NaN, false, null, undefined);
  }

  override public native function isEmpty(str:String):Boolean;
}
}