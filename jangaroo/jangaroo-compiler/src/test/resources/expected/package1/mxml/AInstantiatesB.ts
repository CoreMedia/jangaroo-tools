import Config from "@jangaroo/runtime/AS3/Config";
import { cast } from "@jangaroo/runtime/AS3";
import Panel from "../../ext/Panel";
import Exml from "../../net/jangaroo/ext/Exml";
import BDeclaresA from "./BDeclaresA";
interface AInstantiatesBConfig extends Config<Panel> {
}


class AInstantiatesB extends Panel{
  declare Config: AInstantiatesBConfig;

  constructor(config:Config<AInstantiatesB> = null){
    super( Exml.apply(Config(AInstantiatesB, {

  items:[
    new BDeclaresA({ someProperty: "yes"})
  ]
}),config));
  }}
export default AInstantiatesB;
