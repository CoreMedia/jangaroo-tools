import PropertiesAccess from './PropertiesAccess';

class PropertiesAccessBaseProperties {

   property_1:string = null;

   property_2:string;

    property_3:string;}
interface PropertiesAccessBase_ extends Partial<PropertiesAccessBaseProperties> {
}



 class PropertiesAccessBase<Cfg extends PropertiesAccess._ = PropertiesAccess._> {

   constructor(config:PropertiesAccess._ = null) {
    this.property_1 = config.property_1 += "_HI";
    this.property_2 = 123;
    this.property_3 = config.property_3 || "";
  }
   #_property_2:string = null;

   set property_2(value:any) {
    this.#_property_2 = String(value);
  } get property_2():string {
    return this.#_property_2;
  }
}
interface PropertiesAccessBase<Cfg extends PropertiesAccess._ = PropertiesAccess._>extends PropertiesAccessBaseProperties{}

declare namespace PropertiesAccessBase {
  export type _ = PropertiesAccessBase_;
  export const _: { new(config?: _): _; };
}


export default PropertiesAccessBase;
