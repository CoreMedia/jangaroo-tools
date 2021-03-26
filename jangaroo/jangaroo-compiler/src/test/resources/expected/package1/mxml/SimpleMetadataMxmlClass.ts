import {cast, metadata} from '@jangaroo/joo/AS3';
import Exml from '../../net/jangaroo/ext/Exml';
import ConfigClass from '../ConfigClass';
interface SimpleMetadataMxmlClass_ extends ConfigClass._ {
}

class SimpleMetadataMxmlClass<Cfg extends SimpleMetadataMxmlClass._ = SimpleMetadataMxmlClass._> extends ConfigClass<Cfg>{constructor(config:SimpleMetadataMxmlClass._=null){
    super( Exml.apply(new SimpleMetadataMxmlClass._({
}),config));
}}
metadata(SimpleMetadataMxmlClass, ["ShortVersion"]);

declare namespace SimpleMetadataMxmlClass {
  export type _ = SimpleMetadataMxmlClass_;
  export const _: { new(config?: _): _; };
}


export default SimpleMetadataMxmlClass;
