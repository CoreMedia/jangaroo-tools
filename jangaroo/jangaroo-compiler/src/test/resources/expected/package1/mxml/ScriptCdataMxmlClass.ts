import Config from "@jangaroo/runtime/Config";
import { mixin } from "@jangaroo/runtime";
import int from "../../AS3/int_";
import Exml from "../../net/jangaroo/ext/Exml";
import ConfigClass from "../ConfigClass";
import SomeOtherClass from "../someOtherPackage/SomeOtherClass";
import SimpleInterface from "./SimpleInterface";
interface ScriptCdataMxmlClassConfig extends Config<ConfigClass>, Partial<Pick<ScriptCdataMxmlClass,
  "field3"
>> {
}


class ScriptCdataMxmlClass extends ConfigClass implements SimpleInterface{
  declare Config: ScriptCdataMxmlClassConfig;

  #field1:SomeOtherClass = null;
  protected field2:Array<string> =["a", "b"];
  field3:Array<int> =[1, 2, 3];

  doIt(...values):void {
      for (var v in values) {
        throw "cannot do it with " + v;
      }
    }constructor(config:Config<ScriptCdataMxmlClass> =null){
    super( Exml.apply(Config(ScriptCdataMxmlClass, {
             foo: "bar"
}),config));
}}
mixin(ScriptCdataMxmlClass, SimpleInterface);

export default ScriptCdataMxmlClass;
