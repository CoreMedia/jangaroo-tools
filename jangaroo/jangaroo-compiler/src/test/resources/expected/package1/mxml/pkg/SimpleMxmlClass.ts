import Config from "@jangaroo/runtime/AS3/Config";
import { cast } from "@jangaroo/runtime/AS3";
import panel from "../../../ext/config/panel";
import Exml from "../../../net/jangaroo/ext/Exml";
import package1_mxml_SimpleMxmlClass from "../SimpleMxmlClass";
interface SimpleMxmlClassConfig {
}


/**
 * Created by fwienber on 22.02.2021.
 */
class SimpleMxmlClass extends panel{
  declare Config: SimpleMxmlClassConfig;

  static readonly xtype:string = "testNamespace.pkg.config.simpleMxmlClass";

  constructor(config:Config<SimpleMxmlClass> = null){
    super( Exml.apply(Config(SimpleMxmlClass, {
        title:  package1_mxml_SimpleMxmlClass.xtype

}),config));
  }}
export default SimpleMxmlClass;
