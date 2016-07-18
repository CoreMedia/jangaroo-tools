package package1 {

[Uses("Object")]
[Uses("NaN")]
[Uses("undefined")]
public class ParameterInitializers {
  public function ParameterInitializers(str = "foo", integer = 1, num2 = NaN, bool = false, obj = null, undef = undefined) {
    super();
  }
}
}