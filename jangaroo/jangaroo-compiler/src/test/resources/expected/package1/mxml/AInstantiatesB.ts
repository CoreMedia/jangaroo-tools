import Config from "@jangaroo/runtime/Config";
import Panel from "../../Ext/Panel";
import Exml from "../../net/jangaroo/ext/Exml";
import BDeclaresA from "./BDeclaresA";
interface AInstantiatesBConfig extends Config<Panel> {
}


class AInstantiatesB extends Panel{
  declare Config: AInstantiatesBConfig;

  constructor(config:Config<AInstantiatesB> = null){
    super( Exml.apply(Config(AInstantiatesB, {

  items:[
    Config(BDeclaresA, { someProperty: "yes"})
  ]
}),config));
  }}
export default AInstantiatesB;
