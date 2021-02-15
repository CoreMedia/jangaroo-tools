import Observable from '../ext/mixin/Observable';
import int from '../AS3/int_';

class ConfigClassProperties {

   foo:string = "foo";

   number:int = 0;/*

  @DefaultProperty*/
  

    items:Array<any>;

  

    defaults:any;}

class ConfigClassConfigs {

  

  
   title:string;}
interface ConfigClass_ extends Partial<ConfigClassProperties>, Partial<ConfigClassConfigs> {
}





 class ConfigClass<Cfg extends ConfigClass._ = ConfigClass._> extends Observable<Cfg> {

   constructor(config:ConfigClass._ = null) {
    super();
  }

   #_title:string = "- empty -";getTitle():string {
    return this.#_title;
  }
   setTitle(value:string) {
    this.#_title = value;
  }
}
interface ConfigClass<Cfg extends ConfigClass._ = ConfigClass._>extends ConfigClassProperties{}

declare namespace ConfigClass {
  export type _ = ConfigClass_;
  export const _: { new(config?: _): _; };
}


export default ConfigClass;
