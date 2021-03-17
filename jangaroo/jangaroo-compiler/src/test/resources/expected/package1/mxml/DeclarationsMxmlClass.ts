import {_, asConfig} from '@jangaroo/joo/AS3';
import SomeNativeClass from '../someOtherPackage/SomeNativeClass';
import Exml from '../../net/jangaroo/ext/Exml';
import ConfigClass from '../ConfigClass';
import SomeOtherClass from '../someOtherPackage/SomeOtherClass';
import int from '../../AS3/int_';
interface DeclarationsMxmlClass_ extends Partial<Pick<DeclarationsMxmlClass,
    "bar" |
    "num" |
    "empty" |
    "blub" |
    "list" |
    "other"
>> {
}


class DeclarationsMxmlClass<Cfg extends DeclarationsMxmlClass._ = DeclarationsMxmlClass._> extends SomeNativeClass<Cfg>{constructor(config:DeclarationsMxmlClass._=null){
    super();
    asConfig(this).bar = "BAR!";
    /**
     Some number.
     */
    asConfig(this).num = 123;
    asConfig(this).blub ={ name: "Kuno"};
    asConfig(this).list =[
      { name: "Joe"},
      new ConfigClass(_<ConfigClass._>({
        items:[
          new SomeOtherClass(_<SomeOtherClass._>({ bla: 123}))
        ]
      }))
    ];
    asConfig(this).other = new SomeOtherClass(_<SomeOtherClass._>({ bla: 3,
                        blubb_config: 'blub config expression',
                        blubb_accessor: 'blub accessor expression'})); Exml.apply(this,config);
}

    #bar:string = null;

    
    get bar():string { return this.#bar; }
    set bar(value:string) { this.#bar = value; }

    #num:int = 0;

    /**
     Some number.
     */
    
    get num():int { return this.#num; }
    set num(value:int) { this.#num = value; }

    #empty:int = 0;

    /**
     Empty declaration.
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
declare namespace DeclarationsMxmlClass {
  export type _ = DeclarationsMxmlClass_;
  export const _: { new(config?: _): _; };
}


export default DeclarationsMxmlClass;
