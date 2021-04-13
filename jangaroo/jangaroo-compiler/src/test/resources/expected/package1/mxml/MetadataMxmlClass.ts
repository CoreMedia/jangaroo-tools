import { cast, metadata } from "@jangaroo/joo/AS3";
import Exml from "../../net/jangaroo/ext/Exml";
import ConfigClass from "../ConfigClass";
interface MetadataMxmlClass_ extends ConfigClass._ {
}

/**
 * @deprecated Use {@link use.this.please} instead.
 */
class MetadataMxmlClass extends ConfigClass{constructor(readonly config:MetadataMxmlClass._){
    super( Exml.apply(new MetadataMxmlClass._({
}),config));
}}
metadata(MetadataMxmlClass, ["ThisIsJustATest"]);

declare namespace MetadataMxmlClass {
  export type _ = MetadataMxmlClass_;
  export const _: { new(config?: _): _; };
}


export default MetadataMxmlClass;
