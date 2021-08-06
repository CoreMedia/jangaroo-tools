import Config from "@jangaroo/runtime/AS3/Config";
import { asConfig } from "@jangaroo/runtime/AS3";
import int from "../../AS3/int_";
import Exml from "../../net/jangaroo/ext/Exml";
import ConfigClass from "../ConfigClass";
import SomeNativeClass from "../someOtherPackage/SomeNativeClass";
import SomeOtherClass from "../someOtherPackage/SomeOtherClass";
interface DeclarationsMxmlClassConfig extends Partial<Pick<DeclarationsMxmlClass,
  "bar" |
  "num" |
  "empty" |
  "blub" |
  "list" |
  "other"
>> {
}


class DeclarationsMxmlClass extends SomeNativeClass{
  declare Config: DeclarationsMxmlClassConfig;constructor(config:Config<DeclarationsMxmlClass> =null){
    super();
    this.bar = "BAR!";
    /**
     * Some number.
     */
    this.num = 123;
    this.blub ={ name: "Kuno"};
    this.list =[
      { name: "Joe"},
      new ConfigClass({
        items:[
          new SomeOtherClass({ bla: 123})
        ]
      })
    ];
    this.other = new SomeOtherClass({ bla: 3,
                        blubb_config: "blub config expression",
                        blubb_accessor: "blub accessor expression"}); Exml.apply(this,config);
}

  #bar:string = null;

  get bar():string { return this.#bar; }
  set bar(value:string) { this.#bar = value; }

  #num:int = 0;

    /**
     * Some number.
     */
  get num():int { return this.#num; }
  set num(value:int) { this.#num = value; }

  #empty:int = 0;

    /**
     * Empty declaration.
     */
  get empty():int { return this.#empty; }
  set empty(value:int) { this.#empty = value; }

  #blub:any = null;

  get blub():any { return this.#blub; }
  set blub(value:any) { this.#blub = value; }

  #list:Array<any> = null;

  get list():Array<any> { return this.#list; }
  set list(value:Array<any>) { this.#list = value; }

  #other:SomeOtherClass = null;

  get other():SomeOtherClass { return this.#other; }
  set other(value:SomeOtherClass) { this.#other = value; }}
export default DeclarationsMxmlClass;
