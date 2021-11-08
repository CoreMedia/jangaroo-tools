import Config from "@jangaroo/runtime/Config";
import Panel from "../../../Ext/Panel";
import Exml from "../../../net/jangaroo/ext/Exml";
import package1_mxml_SimpleMxmlClass from "../SimpleMxmlClass";
interface SimpleMxmlClassConfig extends Config<Panel> {
}


/**
 * Created by fwienber on 22.02.2021.
 */
class SimpleMxmlClass extends Panel{
  declare Config: SimpleMxmlClassConfig;

  static readonly xtype:string = "testNamespace.pkg.config.simpleMxmlClass";

  constructor(config:Config<SimpleMxmlClass> = null){
    super( Exml.apply(Config(SimpleMxmlClass, {
        title:  package1_mxml_SimpleMxmlClass.xtype

}),config));
  }}
export default SimpleMxmlClass;
