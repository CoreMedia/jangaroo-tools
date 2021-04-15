import { cast, metadata } from "@jangaroo/runtime/AS3";
import Exml from "../../net/jangaroo/ext/Exml";
import ConfigClass from "../ConfigClass";
interface MetadataMxmlClass_ extends ConfigClass._ {
}

/**
 * @deprecated Use {@link use.this.please} instead.
 */
class MetadataMxmlClass<Cfg extends MetadataMxmlClass._ = MetadataMxmlClass._> extends ConfigClass<Cfg>{constructor(config:MetadataMxmlClass._=null){
    super( Exml.apply(new MetadataMxmlClass._({
}),config));
}}
metadata(MetadataMxmlClass, ["ThisIsJustATest"]);

declare namespace MetadataMxmlClass {
  export type _ = MetadataMxmlClass_;
  export const _: { new(config?: _): _; };
}


export default MetadataMxmlClass;
