import {cast, mixin} from '@jangaroo/joo/AS3';
import SimpleInterface from './SimpleInterface';
import Exml from '../../net/jangaroo/ext/Exml';
import ConfigClass from '../ConfigClass';
import SimpleClass from './SimpleClass';
import YetAnotherInterface from './YetAnotherInterface';

class InterfaceImplementingMxmlClassConfigs {

    

    /** @private */
    
      someProperty:string;}
interface InterfaceImplementingMxmlClass_ extends ConfigClass._, Partial<InterfaceImplementingMxmlClassConfigs> {
}


class InterfaceImplementingMxmlClass<Cfg extends InterfaceImplementingMxmlClass._ = InterfaceImplementingMxmlClass._> extends ConfigClass<Cfg> implements YetAnotherInterface{

     constructor(config:InterfaceImplementingMxmlClass._ = null){
    super( Exml.apply(new InterfaceImplementingMxmlClass._({
}),config));
  }}
interface InterfaceImplementingMxmlClass<Cfg extends InterfaceImplementingMxmlClass._ = InterfaceImplementingMxmlClass._>{

      createInstance(o:SimpleInterface):SimpleClass;}

mixin(InterfaceImplementingMxmlClass, YetAnotherInterface);

declare namespace InterfaceImplementingMxmlClass {
  export type _ = InterfaceImplementingMxmlClass_;
  export const _: { new(config?: _): _; };
}


export default InterfaceImplementingMxmlClass;
