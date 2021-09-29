import Config from "@jangaroo/runtime/AS3/Config";
import Events from "@jangaroo/ext-ts/Events";
import int from "../AS3/int_";
import Observable from "../Ext/mixin/Observable";
import IncludedClass from "./IncludedClass";
interface ConfigClassEvents extends Events<Observable> {

  /**
   * click event documentation.
   * @param source The config event source.
   * @param stranger
   */
  click(source: ConfigClass, stranger: IncludedClass):any;
}

interface ConfigClassConfig extends Partial<Pick<ConfigClass,
  "foo" |
  "number" |
  "items" |
  "defaultType" |
  "defaults" |
  "title"
>> {
  listeners?: ConfigClassEvents;
}



class ConfigClass extends Observable {
  declare Config: ConfigClassConfig;

  constructor(config:Config<ConfigClass> = null) {
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
export default ConfigClass;
