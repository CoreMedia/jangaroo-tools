import { cast } from "@jangaroo/runtime/AS3";
import panel from "../../../ext/config/panel";
import Exml from "../../../net/jangaroo/ext/Exml";
import package1_mxml_SimpleMxmlClass from "../SimpleMxmlClass";
interface SimpleMxmlClass_ {
}


/**
 * Created by fwienber on 22.02.2021.
 */
class SimpleMxmlClass extends panel{
  declare readonly initialConfig: SimpleMxmlClass._;

  static readonly xtype:string = "testNamespace.pkg.config.simpleMxmlClass";

  constructor(config:SimpleMxmlClass._ = null){
    super( Exml.apply(SimpleMxmlClass._({
        title:  package1_mxml_SimpleMxmlClass.xtype

}),config));
  }}
declare namespace SimpleMxmlClass {
  export type _ = SimpleMxmlClass_;
  export const _: (config?: _) => _;
}


export default SimpleMxmlClass;
