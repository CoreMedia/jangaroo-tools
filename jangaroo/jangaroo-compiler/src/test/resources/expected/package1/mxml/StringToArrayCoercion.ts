import { cast } from "@jangaroo/runtime/AS3";
import Panel from "../../ext/Panel";
import Exml from "../../net/jangaroo/ext/Exml";
interface StringToArrayCoercion_ {
}


class StringToArrayCoercion extends Panel{
  declare readonly initialConfig: StringToArrayCoercion._;constructor(config:StringToArrayCoercion._=null){
    super( Exml.apply(StringToArrayCoercion._({
           items: ["just a joke"]
}),config));
}}
declare namespace StringToArrayCoercion {
  export type _ = StringToArrayCoercion_;
  export const _: (config?: _) => _;
}


export default StringToArrayCoercion;
