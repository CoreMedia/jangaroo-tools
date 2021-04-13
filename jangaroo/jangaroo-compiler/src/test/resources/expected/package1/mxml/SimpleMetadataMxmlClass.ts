import { cast, metadata } from "@jangaroo/joo/AS3";
import Exml from "../../net/jangaroo/ext/Exml";
import ConfigClass from "../ConfigClass";
interface SimpleMetadataMxmlClass_ extends ConfigClass._ {
}


class SimpleMetadataMxmlClass extends ConfigClass{constructor(readonly config:SimpleMetadataMxmlClass._){
    super( Exml.apply(new SimpleMetadataMxmlClass._({
}),config));
}}
metadata(SimpleMetadataMxmlClass, ["ShortVersion"]);

declare namespace SimpleMetadataMxmlClass {
  export type _ = SimpleMetadataMxmlClass_;
  export const _: { new(config?: _): _; };
}


export default SimpleMetadataMxmlClass;
