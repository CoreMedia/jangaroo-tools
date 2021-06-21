import { cast } from "@jangaroo/runtime/AS3";
import Exml from "../../../net/jangaroo/ext/Exml";
import PropertiesAccessBase from "./PropertiesAccessBase";
interface PropertiesAccess_ extends PropertiesAccessBase._ {
}


class PropertiesAccess extends PropertiesAccessBase{
  declare readonly initialConfig: PropertiesAccess._;constructor(config:PropertiesAccess._=null){
    super( Exml.apply(PropertiesAccess._({
        property_1: "egal",
        property_2: "egaler",
        property_3: "am egalsten"
}),config));
}}
declare namespace PropertiesAccess {
  export type _ = PropertiesAccess_;
  export const _: (config?: _) => _;
}


export default PropertiesAccess;
