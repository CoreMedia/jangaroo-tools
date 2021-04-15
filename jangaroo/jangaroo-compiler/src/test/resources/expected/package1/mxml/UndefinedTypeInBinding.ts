import {cast} from '@jangaroo/runtime/AS3';
import Exml from '../../net/jangaroo/ext/Exml';
import TestComponentBase2 from './pkg2/TestComponentBase2';
interface UndefinedTypeInBinding_ extends TestComponentBase2._ {
}


class UndefinedTypeInBinding<Cfg extends UndefinedTypeInBinding._ = UndefinedTypeInBinding._> extends TestComponentBase2<Cfg>{constructor(config:UndefinedTypeInBinding._=null){
    super( Exml.apply(new UndefinedTypeInBinding._({
        property_1: /* UndefinedType will raise compiler error */():any => UndefinedType(null)
}),config));
}}
declare namespace UndefinedTypeInBinding {
  export type _ = UndefinedTypeInBinding_;
  export const _: { new(config?: _): _; };
}


export default UndefinedTypeInBinding;
