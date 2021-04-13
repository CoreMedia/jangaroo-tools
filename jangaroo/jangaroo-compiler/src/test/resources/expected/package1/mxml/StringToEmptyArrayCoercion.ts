import { cast } from "@jangaroo/joo/AS3";
import Exml from "../../net/jangaroo/ext/Exml";
import Panel from "../../ext/Panel";
interface StringToEmptyArrayCoercion_ {
}


class StringToEmptyArrayCoercion extends Panel{constructor(readonly config:StringToEmptyArrayCoercion._){
    super( Exml.apply(new StringToEmptyArrayCoercion._({
  items:[]
}),config));
}}
declare namespace StringToEmptyArrayCoercion {
  export type _ = StringToEmptyArrayCoercion_;
  export const _: { new(config?: _): _; };
}


export default StringToEmptyArrayCoercion;
