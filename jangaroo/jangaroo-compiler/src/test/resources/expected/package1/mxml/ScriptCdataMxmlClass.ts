import {cast, mixin} from '@jangaroo/joo/AS3';
import SimpleInterface from './SimpleInterface';
import Exml from '../../net/jangaroo/ext/Exml';
import ConfigClass from '../ConfigClass';
import SomeOtherClass from '../someOtherPackage/SomeOtherClass';
import int from '../../AS3/int_';

class ScriptCdataMxmlClassProperties {
     field3:Array<int> =[1, 2, 3];}
interface ScriptCdataMxmlClass_ extends ConfigClass._, Partial<ScriptCdataMxmlClassProperties> {
}


class ScriptCdataMxmlClass<Cfg extends ScriptCdataMxmlClass._ = ScriptCdataMxmlClass._> extends ConfigClass<Cfg> implements SimpleInterface{

     #field1:SomeOtherClass = null;
    protected field2:Array<string> =["a", "b"];

     doIt(...values):void {
      for (var v in values) {
        throw "cannot do it with " + v;
      }
    }constructor(config:ScriptCdataMxmlClass._=null){
    super( Exml.apply(new ScriptCdataMxmlClass._({
             foo: "bar"
}),config));
}}
interface ScriptCdataMxmlClass<Cfg extends ScriptCdataMxmlClass._ = ScriptCdataMxmlClass._>extends ScriptCdataMxmlClassProperties{}

mixin(ScriptCdataMxmlClass, SimpleInterface);

declare namespace ScriptCdataMxmlClass {
  export type _ = ScriptCdataMxmlClass_;
  export const _: { new(config?: _): _; };
}


export default ScriptCdataMxmlClass;