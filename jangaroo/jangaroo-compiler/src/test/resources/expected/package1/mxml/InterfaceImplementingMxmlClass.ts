import { cast, mixin } from "@jangaroo/runtime/AS3";
import Exml from "../../net/jangaroo/ext/Exml";
import ConfigClass from "../ConfigClass";
import SimpleClass from "./SimpleClass";
import SimpleInterface from "./SimpleInterface";
import YetAnotherInterface from "./YetAnotherInterface";
interface InterfaceImplementingMxmlClass_ extends ConfigClass._, Partial<Pick<InterfaceImplementingMxmlClass,
  "someProperty"
>> {
}


class InterfaceImplementingMxmlClass extends ConfigClass implements YetAnotherInterface{
  declare readonly initialConfig: InterfaceImplementingMxmlClass._;

  constructor(config:InterfaceImplementingMxmlClass._ = null){
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
