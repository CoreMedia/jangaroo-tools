import {cast, metadata} from '@jangaroo/joo/AS3';
import Exml from '../../net/jangaroo/ext/Exml';
import ConfigClass from '../ConfigClass';
interface MetadataCdataMxmlClass_ extends ConfigClass._ {
}

/*

    /**
     * Let's have a class with two annotations.
     * @see {@link http://help.adobe.com/en_US/flex/using/WSd0ded3821e0d52fe1e63e3d11c2f44bc36-7ff2.html}
     * /
    @ThisIsJustATest
    @Deprecated ({replacement:'use.this.please'})*/
class MetadataCdataMxmlClass<Cfg extends MetadataCdataMxmlClass._ = MetadataCdataMxmlClass._> extends ConfigClass<Cfg>{constructor(config:MetadataCdataMxmlClass._=null){
    super( Exml.apply(new MetadataCdataMxmlClass._({
}),config));
}}
metadata(MetadataCdataMxmlClass, ["ThisIsJustATest"],
    "Deprecated", {replacement: 'use.this.please'}]);

declare namespace MetadataCdataMxmlClass {
  export type _ = MetadataCdataMxmlClass_;
  export const _: { new(config?: _): _; };
}


export default MetadataCdataMxmlClass;