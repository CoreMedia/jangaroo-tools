package package2 {
import ext.Ext;

public class TestExtApply {
  function TestExtApply(config: TestExtApply) {
    Ext.apply(config, this);
    Ext.apply({ a: "target" }, { a: "apply" }, { a: "defaults"});
  }
}
}
