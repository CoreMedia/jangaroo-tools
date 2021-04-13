import Observable from "../ext/mixin/Observable";
import int from "../AS3/int_";
interface ConfigClass_ extends Partial<Pick<ConfigClass,
  "foo" |
  "number" |
  "items" |
  "defaults" |
  "title"
>> {
}



class ConfigClass extends Observable {

  constructor(readonly config:ConfigClass._) {
    super();
  }

  foo:string = "foo";
          number:int = 0;

  items:Array<any>;

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
