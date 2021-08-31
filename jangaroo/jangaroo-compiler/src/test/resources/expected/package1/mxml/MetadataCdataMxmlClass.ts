import Config from "@jangaroo/runtime/AS3/Config";
import { metadata } from "@jangaroo/runtime/AS3";
import Exml from "../../net/jangaroo/ext/Exml";
import ConfigClass from "../ConfigClass";
interface MetadataCdataMxmlClassConfig extends Config<ConfigClass> {
}



    /**
     * Let's have a class with two annotations.
     * @see {@link http://help.adobe.com/en_US/flex/using/WSd0ded3821e0d52fe1e63e3d11c2f44bc36-7ff2.html}
     */
    
/**
 * The class level comment for the component.
 * This tag supports all ASDoc tags,
 * and does not require a CDATA block.
 * @deprecated Use {@link use.this.please} instead.
 */
class MetadataCdataMxmlClass extends ConfigClass{
  declare Config: MetadataCdataMxmlClassConfig;constructor(config:Config<MetadataCdataMxmlClass> =null){
    super( Exml.apply(Config(MetadataCdataMxmlClass),config));
}}
metadata(MetadataCdataMxmlClass, ["ThisIsJustATest"]);

export default MetadataCdataMxmlClass;
