import {_, cast} from '@jangaroo/joo/AS3';
import Exml from '../../net/jangaroo/ext/Exml';
import Panel from '../../ext/Panel';
import BDeclaresA from './BDeclaresA';
interface AInstantiatesB_ {
}


class AInstantiatesB<Cfg extends AInstantiatesB._ = AInstantiatesB._> extends Panel<Cfg>{

  constructor(config:AInstantiatesB._ = null){
    super( Exml.apply(new AInstantiatesB._({

  items:[
    new BDeclaresA(_<BDeclaresA._>({ someProperty: "yes"}))
  ]
}),config));
  }}
declare namespace AInstantiatesB {
  export type _ = AInstantiatesB_;
  export const _: { new(config?: _): _; };
}


export default AInstantiatesB;
