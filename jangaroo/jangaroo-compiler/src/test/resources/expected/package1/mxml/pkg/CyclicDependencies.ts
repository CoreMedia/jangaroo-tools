import Config from "@jangaroo/runtime/AS3/Config";
import Exml from "../../../net/jangaroo/ext/Exml";
import CyclicDependencies_1 from "./CyclicDependencies_1";
interface CyclicDependenciesConfig extends Partial<Pick<CyclicDependencies,
  "cause_trouble"
>> {
}


class CyclicDependencies extends Object{
  declare Config: CyclicDependenciesConfig;

  constructor(config:Config<CyclicDependencies> = null){
    super(); Exml.apply(this,config);
  }

  #cause_trouble:CyclicDependencies_1 = null;

  get cause_trouble():CyclicDependencies_1 { return this.#cause_trouble; }
  set cause_trouble(value:CyclicDependencies_1) { this.#cause_trouble = value; }}
export default CyclicDependencies;
