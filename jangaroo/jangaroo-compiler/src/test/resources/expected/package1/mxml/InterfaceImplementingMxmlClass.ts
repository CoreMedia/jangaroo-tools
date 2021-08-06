import Config from "@jangaroo/runtime/AS3/Config";
import { cast, mixin } from "@jangaroo/runtime/AS3";
import Exml from "../../net/jangaroo/ext/Exml";
import ConfigClass from "../ConfigClass";
import SimpleClass from "./SimpleClass";
import SimpleInterface from "./SimpleInterface";
import YetAnotherInterface from "./YetAnotherInterface";
interface InterfaceImplementingMxmlClassConfig extends Config<ConfigClass>, Partial<Pick<InterfaceImplementingMxmlClass,
  "someProperty"
>> {
}


class InterfaceImplementingMxmlClass extends ConfigClass implements YetAnotherInterface{
  declare Config: InterfaceImplementingMxmlClassConfig;

  constructor(config:Config<InterfaceImplementingMxmlClass> = null){
    super( Exml.apply(Config(InterfaceImplementingMxmlClass),config));
  }
  #someProperty:string;


    /** This is some property. */
   get someProperty():string { return this.#someProperty; }

    /** @private */
   set someProperty(value:string) { this.#someProperty = value; }}
interface InterfaceImplementingMxmlClass{

  createInstance(o:SimpleInterface):SimpleClass;}

mixin(InterfaceImplementingMxmlClass, YetAnotherInterface);

export default InterfaceImplementingMxmlClass;
