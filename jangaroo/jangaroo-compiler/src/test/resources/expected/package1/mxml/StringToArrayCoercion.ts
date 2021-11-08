import Config from "@jangaroo/runtime/Config";
import Panel from "../../Ext/Panel";
import Exml from "../../net/jangaroo/ext/Exml";
interface StringToArrayCoercionConfig extends Config<Panel> {
}


class StringToArrayCoercion extends Panel{
  declare Config: StringToArrayCoercionConfig;constructor(config:Config<StringToArrayCoercion> =null){
    super( Exml.apply(Config(StringToArrayCoercion, {
           items: ["just a joke"]
}),config));
}}
export default StringToArrayCoercion;
