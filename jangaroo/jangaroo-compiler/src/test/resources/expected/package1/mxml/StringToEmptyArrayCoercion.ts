import {cast} from '@jangaroo/joo/AS3';
import Exml from '../../net/jangaroo/ext/Exml';
import Panel from '../../ext/Panel';
interface StringToEmptyArrayCoercion_ {
}


class StringToEmptyArrayCoercion<Cfg extends StringToEmptyArrayCoercion._ = StringToEmptyArrayCoercion._> extends Panel<Cfg>{constructor(config:StringToEmptyArrayCoercion._=null){
    super( Exml.apply(new StringToEmptyArrayCoercion._({
  items:[]
}),config));
}}
declare namespace StringToEmptyArrayCoercion {
  export type _ = StringToEmptyArrayCoercion_;
  export const _: { new(config?: _): _; };
}


export default StringToEmptyArrayCoercion;
