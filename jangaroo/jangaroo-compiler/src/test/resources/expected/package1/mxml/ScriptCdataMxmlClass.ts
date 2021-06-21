import { cast, mixin } from "@jangaroo/runtime/AS3";
import int from "../../AS3/int_";
import Exml from "../../net/jangaroo/ext/Exml";
import ConfigClass from "../ConfigClass";
import SomeOtherClass from "../someOtherPackage/SomeOtherClass";
import SimpleInterface from "./SimpleInterface";
interface ScriptCdataMxmlClass_ extends ConfigClass._, Partial<Pick<ScriptCdataMxmlClass,
  "field3"
>> {
}


class ScriptCdataMxmlClass extends ConfigClass implements SimpleInterface{
  declare readonly initialConfig: ScriptCdataMxmlClass._;

  #field1:SomeOtherClass = null;
  protected field2:Array<string> =["a", "b"];
  field3:Array<int> =[1, 2, 3];

  doIt(...values):void {
      for (var v in values) {
        throw "cannot do it with " + v;
      }
    }constructor(config:ScriptCdataMxmlClass._=null){
    super( Exml.apply(ScriptCdataMxmlClass._({
             foo: "bar"
}),config));
}}
mixin(ScriptCdataMxmlClass, SimpleInterface);

declare namespace ScriptCdataMxmlClass {
  export type _ = ScriptCdataMxmlClass_;
  export const _: (config?: _) => _;
}


export default ScriptCdataMxmlClass;
