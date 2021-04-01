import Exml from '../../../net/jangaroo/ext/Exml';
import CyclicDependencies_1 from './CyclicDependencies_1';
interface CyclicDependencies_ extends Partial<Pick<CyclicDependencies,
    "cause_trouble"
>> {
}


class CyclicDependencies<Cfg extends CyclicDependencies._ = CyclicDependencies._> extends Object<Cfg>{

  constructor(config:CyclicDependencies._ = null){
    super(); Exml.apply(this,config);
  }

  #cause_trouble:CyclicDependencies_1 = null;

  get cause_trouble():CyclicDependencies_1 { return this.#cause_trouble; }
  set cause_trouble(value:CyclicDependencies_1) { this.#cause_trouble = value; }}
declare namespace CyclicDependencies {
  export type _ = CyclicDependencies_;
  export const _: { new(config?: _): _; };
}


export default CyclicDependencies;
