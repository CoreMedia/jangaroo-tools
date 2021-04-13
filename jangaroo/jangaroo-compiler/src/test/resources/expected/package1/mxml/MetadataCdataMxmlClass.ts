import { cast, metadata } from "@jangaroo/joo/AS3";
import Exml from "../../net/jangaroo/ext/Exml";
import ConfigClass from "../ConfigClass";
interface MetadataCdataMxmlClass_ extends ConfigClass._ {
}



    /**
     * Let's have a class with two annotations.
     * @see {@link http://help.adobe.com/en_US/flex/using/WSd0ded3821e0d52fe1e63e3d11c2f44bc36-7ff2.html}
     */
    
/**
 * The class level comment for the component.
 * This tag supports all ASDoc tags,
 * and does not require a CDATA block.
 * @deprecated Use {@link use.this.please} instead.
 */
class MetadataCdataMxmlClass extends ConfigClass{constructor(readonly config:MetadataCdataMxmlClass._){
    super( Exml.apply(new MetadataCdataMxmlClass._({
}),config));
}}
metadata(MetadataCdataMxmlClass, ["ThisIsJustATest"]);

declare namespace MetadataCdataMxmlClass {
  export type _ = MetadataCdataMxmlClass_;
  export const _: { new(config?: _): _; };
}


export default MetadataCdataMxmlClass;
