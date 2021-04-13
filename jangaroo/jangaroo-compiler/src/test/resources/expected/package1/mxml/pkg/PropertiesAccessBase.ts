import PropertiesAccess from "./PropertiesAccess";
interface PropertiesAccessBase_ extends Partial<Pick<PropertiesAccessBase,
  "property_1" |
  "property_2" |
  "property_3"
>> {
}



class PropertiesAccessBase {

  constructor(readonly config:PropertiesAccess._) {
    this.property_1 = config.property_1 += "_HI";
    this.property_2 = 123;
    this.property_3 = config.property_3 || "";
  }

  property_1:string = null;
  #_property_2:string = null;

   set property_2(value:any) {
    this.#_property_2 = String(value);
  }

   get property_2():string {
    return this.#_property_2;
  }

  property_3:string;
}
declare namespace PropertiesAccessBase {
  export type _ = PropertiesAccessBase_;
  export const _: { new(config?: _): _; };
}


export default PropertiesAccessBase;
