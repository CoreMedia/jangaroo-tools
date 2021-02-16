import {cast} from '@jangaroo/joo/AS3';
import Exml from '../../../net/jangaroo/ext/Exml';
import PropertiesAccessBase from './PropertiesAccessBase';
interface PropertiesAccess_ extends PropertiesAccessBase._ {
}


class PropertiesAccess<Cfg extends PropertiesAccess._ = PropertiesAccess._> extends PropertiesAccessBase<Cfg>{constructor(config:PropertiesAccess._=null){
    super( Exml.apply(new PropertiesAccess._({
        property_1: "egal",
        property_2: "egaler",
        property_3: "am egalsten"
}),config));
}}
declare namespace PropertiesAccess {
  export type _ = PropertiesAccess_;
  export const _: { new(config?: _): _; };
}


export default PropertiesAccess;
