import { cast } from "@jangaroo/runtime/AS3";
import panel from "../../../ext/config/panel";
import Exml from "../../../net/jangaroo/ext/Exml";
import package1_mxml_SimpleMxmlClass from "../SimpleMxmlClass";
interface SimpleMxmlClass_ {
}


/**
 * Created by fwienber on 22.02.2021.
 */
class SimpleMxmlClass<Cfg extends SimpleMxmlClass._ = SimpleMxmlClass._> extends panel<Cfg>{

  static readonly xtype:string = "testNamespace.pkg.config.simpleMxmlClass";

  constructor(config:SimpleMxmlClass._ = null){
    super( Exml.apply(new SimpleMxmlClass._({
        title: Exml.asString( package1_mxml_SimpleMxmlClass.xtype)

}),config));
  }}
declare namespace SimpleMxmlClass {
  export type _ = SimpleMxmlClass_;
  export const _: { new(config?: _): _; };
}


export default SimpleMxmlClass;
