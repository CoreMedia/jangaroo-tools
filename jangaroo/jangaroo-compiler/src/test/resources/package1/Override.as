package package1 {

[Override]
public class Override extends OverrideBase {

  public function Override() {
    this.callParent();
    this["field1"] = "overriddenField1";
    this["field2"] = "overriddenField2";
  }

  override public function baseFun():String {
    return "overriddenBaseFun";
  }
}
}