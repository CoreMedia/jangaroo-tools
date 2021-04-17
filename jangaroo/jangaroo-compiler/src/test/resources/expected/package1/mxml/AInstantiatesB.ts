import { _, cast } from "@jangaroo/runtime/AS3";
import Panel from "../../ext/Panel";
import Exml from "../../net/jangaroo/ext/Exml";
import BDeclaresA from "./BDeclaresA";
interface AInstantiatesB_ {
}


class AInstantiatesB extends Panel{
  declare readonly initialConfig: AInstantiatesB._;

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
