import Config from "@jangaroo/runtime/Config";
import { metadata } from "@jangaroo/runtime";
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
