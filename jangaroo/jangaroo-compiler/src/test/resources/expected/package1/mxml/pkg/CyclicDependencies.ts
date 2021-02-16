import Exml from '../../../net/jangaroo/ext/Exml';
import CyclicDependencies_1 from './CyclicDependencies_1';

class CyclicDependenciesConfigs {

    
     cause_trouble:CyclicDependencies_1 = null;}
interface CyclicDependencies_ extends Partial<CyclicDependenciesConfigs> {
}


class CyclicDependencies<Cfg extends CyclicDependencies._ = CyclicDependencies._> extends Object<Cfg>{

     constructor(config:CyclicDependencies._ = null){
    super(); Exml.apply(this,config);
  }}
declare namespace CyclicDependencies {
  export type _ = CyclicDependencies_;
  export const _: { new(config?: _): _; };
}


export default CyclicDependencies;
