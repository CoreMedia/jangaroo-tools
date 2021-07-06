import int from "../AS3/int_";
import Observable from "../ext/mixin/Observable";
interface ConfigClass_ extends Partial<Pick<ConfigClass,
  "foo" |
  "number" |
  "items" |
  "defaultType" |
  "defaults" |
  "title"
>> {
}



class ConfigClass extends Observable {
  declare readonly initialConfig: ConfigClass._;

  constructor(config:ConfigClass._ = null) {
    super();
  }

  foo:string = "foo";
          number:int = 0;

  items:Array<any>;

  defaultType:string;

  defaults:any;

  #_title:string = "- empty -";

   get title():string {
    return this.#_title;
  }

   set title(value:string) {
    this.#_title = value;
  }
}
declare namespace ConfigClass {
  export type _ = ConfigClass_;
  export const _: { new(config?: _): _; };
}


export default ConfigClass;
