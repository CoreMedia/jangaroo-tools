import Config from "@jangaroo/runtime/AS3/Config";
import { cast } from "@jangaroo/runtime/AS3";
import Exml from "../../../net/jangaroo/ext/Exml";
import PropertiesAccessBase from "./PropertiesAccessBase";
interface PropertiesAccessConfig extends Config<PropertiesAccessBase> {
}


class PropertiesAccess extends PropertiesAccessBase{
  declare Config: PropertiesAccessConfig;constructor(config:Config<PropertiesAccess> =null){
    super( Exml.apply(Config<PropertiesAccess>({
        property_1: "egal",
        property_2: "egaler",
        property_3: "am egalsten"
}),config));
}}
export default PropertiesAccess;
