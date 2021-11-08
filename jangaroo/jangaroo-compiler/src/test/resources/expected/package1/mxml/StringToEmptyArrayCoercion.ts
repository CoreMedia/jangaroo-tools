import Config from "@jangaroo/runtime/Config";
import Panel from "../../Ext/Panel";
import Exml from "../../net/jangaroo/ext/Exml";
interface StringToEmptyArrayCoercionConfig extends Config<Panel> {
}


class StringToEmptyArrayCoercion extends Panel{
  declare Config: StringToEmptyArrayCoercionConfig;constructor(config:Config<StringToEmptyArrayCoercion> =null){
    super( Exml.apply(Config(StringToEmptyArrayCoercion, {
  items:[]
}),config));
}}
export default StringToEmptyArrayCoercion;
