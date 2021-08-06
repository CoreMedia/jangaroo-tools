import Config from "@jangaroo/runtime/AS3/Config";
import { cast, metadata } from "@jangaroo/runtime/AS3";
import Exml from "../../net/jangaroo/ext/Exml";
import ConfigClass from "../ConfigClass";
interface MetadataMxmlClassConfig extends Config<ConfigClass> {
}

/**
 * @deprecated Use {@link use.this.please} instead.
 */
class MetadataMxmlClass extends ConfigClass{
  declare Config: MetadataMxmlClassConfig;constructor(config:Config<MetadataMxmlClass> =null){
    super( Exml.apply(Config(MetadataMxmlClass),config));
}}
metadata(MetadataMxmlClass, ["ThisIsJustATest"]);

export default MetadataMxmlClass;
