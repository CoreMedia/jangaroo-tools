import Config from "@jangaroo/runtime/Config";
import { metadata } from "@jangaroo/runtime";
import Exml from "../../net/jangaroo/ext/Exml";
import ConfigClass from "../ConfigClass";
interface SimpleMetadataMxmlClassConfig extends Config<ConfigClass> {
}


class SimpleMetadataMxmlClass extends ConfigClass{
  declare Config: SimpleMetadataMxmlClassConfig;constructor(config:Config<SimpleMetadataMxmlClass> =null){
    super( Exml.apply(Config(SimpleMetadataMxmlClass),config));
}}
metadata(SimpleMetadataMxmlClass, ["ShortVersion"]);

export default SimpleMetadataMxmlClass;
