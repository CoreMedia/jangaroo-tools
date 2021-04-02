import { cast, metadata } from "@jangaroo/joo/AS3";
import Exml from "../../net/jangaroo/ext/Exml";
import ConfigClass from "../ConfigClass";
interface MetadataMxmlClass_ extends ConfigClass._ {
}



    @metadata("ThisIsJustATest")
@metadata("Deprecated", {replacement: "use.this.please"})
class MetadataMxmlClass<Cfg extends MetadataMxmlClass._ = MetadataMxmlClass._> extends ConfigClass<Cfg>{constructor(config:MetadataMxmlClass._=null){
    super( Exml.apply(new MetadataMxmlClass._({
}),config));
}}
declare namespace MetadataMxmlClass {
  export type _ = MetadataMxmlClass_;
  export const _: { new(config?: _): _; };
}


export default MetadataMxmlClass;
