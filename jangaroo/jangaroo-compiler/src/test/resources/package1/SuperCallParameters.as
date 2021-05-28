package package1 {

public class SuperCallParameters extends ManyConstructorParameters {
  public function SuperCallParameters() {
    const foo: Boolean = this.isEmpty("");
    var bar: String = "BAR";
    super("bar", -1, -4.2, true, {}, []);
    if (foo) {
      super.isEmpty("FOO");
    }
  }

  override public function isEmpty(str:String):Boolean {
    return super.isEmpty(str);
  }
}
}