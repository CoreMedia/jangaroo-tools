import Config from "@jangaroo/runtime/AS3/Config";
import Ext from "../Ext";
interface TestExtApplyConfig {
}



class TestExtApply {
  declare Config: TestExtApplyConfig;
  constructor(config: Config<TestExtApply>) {
    Ext.apply(config, this);
    Ext.apply({ a: "target" }, { a: "apply" }, { a: "defaults"});
  }
}
export default TestExtApply;
