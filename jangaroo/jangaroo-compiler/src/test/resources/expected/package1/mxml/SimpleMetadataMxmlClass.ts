import { cast, metadata } from "@jangaroo/runtime/AS3";
import Exml from "../../net/jangaroo/ext/Exml";
import ConfigClass from "../ConfigClass";
interface SimpleMetadataMxmlClass_ extends ConfigClass._ {
}


class SimpleMetadataMxmlClass extends ConfigClass{
  declare readonly initialConfig: SimpleMetadataMxmlClass._;constructor(config:SimpleMetadataMxmlClass._=null){
    super( Exml.apply(new SimpleMetadataMxmlClass._({
}),config));
}}
metadata(SimpleMetadataMxmlClass, ["ShortVersion"]);

declare namespace SimpleMetadataMxmlClass {
  export type _ = SimpleMetadataMxmlClass_;
  export const _: { new(config?: _): _; };
}


export default SimpleMetadataMxmlClass;
