import {_, asConfig, bind, cast} from '@jangaroo/joo/AS3';
import SomeEvent from '../someOtherPackage/SomeEvent';
import Exml from '../../net/jangaroo/ext/Exml';
import ConfigClass from '../ConfigClass';
import SomeOtherClass from '../someOtherPackage/SomeOtherClass';
import int from '../../AS3/int_';
interface SimpleMxmlClass_ extends ConfigClass._, Partial<Pick<SimpleMxmlClass,
    "list" |
    "bar" |
    "computed" |
    "num" |
    "empty" |
    "someFlag1" |
    "anotherFlag1" |
    "someFlag2" |
    "anotherFlag2" |
    "someFlag3" |
    "anotherFlag3" |
    "emptyObject" |
    "joe" |
    "otherByExpression" |
    "other" |
    "no_config"
>> {
}


/**
  My config class subclass, authored in MXML.
 */

class SimpleMxmlClass<Cfg extends SimpleMxmlClass._ = SimpleMxmlClass._> extends ConfigClass<Cfg>{

    static readonly xtype:string = "testNamespace.config.simpleMxmlClass";

    constructor(config:SimpleMxmlClass._ = null){
    super((()=>{
    this.#blub ={ name: "Kuno"};
    config = Exml.apply({
    bar: "FOO & BAR!",
    computed: Exml.asString( 'B' + 'AR!'),
    /**
     Some number.
     */
    num: 123,
    someFlag2: false,
    anotherFlag2: true,
    someFlag3: false,
    anotherFlag3: true,
    joe: { name: "Joe"},
    list: [
      { name: "Joe"},
      new ConfigClass(_<ConfigClass._>({items:[
        new SomeOtherClass(_<SomeOtherClass._>({ bla: 123}))
      ]}))
    ],
    otherByExpression: { foo: 'bar'},
    other: new SomeOtherClass(_<SomeOtherClass._>({ bla: 3,
                          blubb_config: 'blub config expression',
                          blubb_accessor: 'blub accessor expression'}))
    },config);
    return  Exml.apply(new SimpleMxmlClass._({
             foo: "bar",
             number: 1 < 2  ? 1 + 1 : 3,
  defaultType:
    SomeOtherClass.xtype,
  defaults:_<SomeOtherClass._&{"known-unknown"}>({ bla: 99,
                          "known-unknown": true
  }),
  ...Exml.append({items: [
    new SomeOtherClass(_<SomeOtherClass._>({ bla: 23})),
    new SomeOtherClass(_<SomeOtherClass._>({ bla: 1,
    listeners:{ clickClack: Exml.eventHandler( SomeEvent.CLICK_CLACK,SomeEvent,bind(this,this.#$on_clickClack_55_41))}})),
    new SomeOtherClass(_<SomeOtherClass._&ConfigClass._>({ bla: 42, number: 24
    })),
    new ConfigClass(_<ConfigClass._>({
      items:[
        new SomeOtherClass(_<SomeOtherClass._>({ doodle: "non-bound", bla: this.other.bla}))
      ],
      number: 12
    })),
    new ConfigClass(_<ConfigClass._>({
      ...Exml.prepend({items: [
        new SomeOtherClass(_<SomeOtherClass._>({ bla: 12})),
        this.no_config = new SomeOtherClass(_<SomeOtherClass._>({ bla: 13}))
      ]})
    }))
  ]}),
    listeners:{
             click: Exml.eventHandler( SomeEvent.CLICK,SomeEvent,bind(this,this.#$on_click_14_20))}
}),config);})());
  }

    #blub:any;

    //@ts-expect-error 18022
  static #static = (() =>{
      if(1 < 0 && 0 > 1) {
        throw "plain wrong!";
      }
    })();

    #list:any = null;

    
    get list():any { return this.#list; }
    set list(value:any) { this.#list = value; }

    #bar:string = null;

    
    get bar():string { return this.#bar; }
    set bar(value:string) { this.#bar = value; }

    #computed:string = null;

    
    get computed():string { return this.#computed; }
    set computed(value:string) { this.#computed = value; }

    #num:int = 0;

    /**
     Some number.
     */
    
    get num():int { return this.#num; }
    set num(value:int) { this.#num = value; }

    #empty:int = 0;

    
    get empty():int { return this.#empty; }
    set empty(value:int) { this.#empty = value; }

    #someFlag1:boolean = false;

    
    get someFlag1():boolean { return this.#someFlag1; }
    set someFlag1(value:boolean) { this.#someFlag1 = value; }

    #anotherFlag1:boolean = false;

    
    get anotherFlag1():boolean { return this.#anotherFlag1; }
    set anotherFlag1(value:boolean) { this.#anotherFlag1 = value; }

    #someFlag2:boolean = false;

    
    get someFlag2():boolean { return this.#someFlag2; }
    set someFlag2(value:boolean) { this.#someFlag2 = value; }

    #anotherFlag2:boolean = false;

    
    get anotherFlag2():boolean { return this.#anotherFlag2; }
    set anotherFlag2(value:boolean) { this.#anotherFlag2 = value; }

    #someFlag3:boolean = false;

    
    get someFlag3():boolean { return this.#someFlag3; }
    set someFlag3(value:boolean) { this.#someFlag3 = value; }

    #anotherFlag3:boolean = false;

    
    get anotherFlag3():boolean { return this.#anotherFlag3; }
    set anotherFlag3(value:boolean) { this.#anotherFlag3 = value; }

    #emptyObject:any = null;

    
    get emptyObject():any { return this.#emptyObject; }
    set emptyObject(value:any) { this.#emptyObject = value; }

    #joe:any = null;

    
    get joe():any { return this.#joe; }
    set joe(value:any) { this.#joe = value; }

    #otherByExpression:any = null;

    
    get otherByExpression():any { return this.#otherByExpression; }
    set otherByExpression(value:any) { this.#otherByExpression = value; }

    #other:SomeOtherClass = null;

    
    get other():SomeOtherClass { return this.#other; }
    set other(value:SomeOtherClass) { this.#other = value; }
//@ts-expect-error 18022
#$on_click_14_20 (event:SomeEvent) :void {
    var result = 'gotcha!';}
//@ts-expect-error 18022
#$on_clickClack_55_41 (event:SomeEvent) :void {
    var test=0;}

    #no_config:SomeOtherClass = null;

        
        get no_config():SomeOtherClass { return this.#no_config; }
    set no_config(value:SomeOtherClass) { this.#no_config = value; }}
declare namespace SimpleMxmlClass {
  export type _ = SimpleMxmlClass_;
  export const _: { new(config?: _): _; };
}


export default SimpleMxmlClass;
