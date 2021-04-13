import { cast, mixin } from "@jangaroo/joo/AS3";
import SimpleInterface from "./SimpleInterface";
import Exml from "../../net/jangaroo/ext/Exml";
import ConfigClass from "../ConfigClass";
import SimpleClass from "./SimpleClass";
import YetAnotherInterface from "./YetAnotherInterface";
interface InterfaceImplementingMxmlClass_ extends ConfigClass._, Partial<Pick<InterfaceImplementingMxmlClass,
  "someProperty"
>> {
}


class InterfaceImplementingMxmlClass extends ConfigClass implements YetAnotherInterface{

  constructor(readonly config:InterfaceImplementingMxmlClass._){
    super( Exml.apply(new InterfaceImplementingMxmlClass._({
}),config));
  }
  #someProperty:string;


    /** This is some property. */
   get someProperty():string { return this.#someProperty; }

    /** @private */
   set someProperty(value:string) { this.#someProperty = value; }}
interface InterfaceImplementingMxmlClass{

  createInstance(o:SimpleInterface):SimpleClass;}

mixin(InterfaceImplementingMxmlClass, YetAnotherInterface);

declare namespace InterfaceImplementingMxmlClass {
  export type _ = InterfaceImplementingMxmlClass_;
  export const _: { new(config?: _): _; };
}


export default InterfaceImplementingMxmlClass;
