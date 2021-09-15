import Config from "@jangaroo/runtime/AS3/Config";
import Ext from "../Ext";
interface TestExtApplyConfig {
}



class TestExtApply {
  declare Config: TestExtApplyConfig;
  constructor(config: Config<TestExtApply>) {
    Object.assign(config, this);
    Object.assign({ a: "target" }, { a: "defaults"}, { a: "apply" });
  }
}
export default TestExtApply;
