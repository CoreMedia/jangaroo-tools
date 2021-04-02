import { cast } from "@jangaroo/joo/AS3";
import Exml from "../../net/jangaroo/ext/Exml";
import Panel from "../../ext/Panel";
interface StringToArrayCoercion_ {
}


class StringToArrayCoercion<Cfg extends StringToArrayCoercion._ = StringToArrayCoercion._> extends Panel<Cfg>{constructor(config:StringToArrayCoercion._=null){
    super( Exml.apply(new StringToArrayCoercion._({
           items: ["just a joke"]
}),config));
}}
declare namespace StringToArrayCoercion {
  export type _ = StringToArrayCoercion_;
  export const _: { new(config?: _): _; };
}


export default StringToArrayCoercion;
