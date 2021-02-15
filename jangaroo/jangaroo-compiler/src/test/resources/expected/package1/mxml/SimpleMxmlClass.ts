import {_, bind, cast} from '@jangaroo/joo/AS3';
import SomeEvent from '../someOtherPackage/SomeEvent';
import Exml from '../../net/jangaroo/ext/Exml';
import ConfigClass from '../ConfigClass';
import SomeOtherClass from '../someOtherPackage/SomeOtherClass';
import int from '../../AS3/int_';

class SimpleMxmlClassConfigs {

    
     list:any = null;

    
     bar:string = null;

    
     computed:string = null;

    /**
     Some number.
     */
    
     num:int = 0;

    
     empty:int = 0;

    
     someFlag1:boolean = false;

    
     anotherFlag1:boolean = false;

    
     someFlag2:boolean = false;

    
     anotherFlag2:boolean = false;

    
     someFlag3:boolean = false;

    
     anotherFlag3:boolean = false;

    
     emptyObject:any = null;

    
     joe:any = null;

    
     otherByExpression:any = null;

    
     other:SomeOtherClass = null;

        
         no_config:SomeOtherClass = null;}
interface SimpleMxmlClass_ extends ConfigClass._, Partial<SimpleMxmlClassConfigs> {
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
        new SomeOtherClass(_<SomeOtherClass._>({ doodle: "non-bound", bla: this.getConfig("other").getConfig("bla")}))
      ],
      number: 12
    })),
    new ConfigClass(_<ConfigClass._>({
      ...Exml.prepend({items: [
        new SomeOtherClass(_<SomeOtherClass._>({ bla: 12})),
        this.setConfig("no_config" , new SomeOtherClass(_<SomeOtherClass._>({ bla: 13})))
      ]})
    }))
  ]}),
    listeners:{
             click: Exml.eventHandler( SomeEvent.CLICK,SomeEvent,bind(this,this.#$on_click_14_20))}
}),config);})());
  }

     #blub:any;

    // noinspection JSUnusedLocalSymbols
  private static static$5_bR = (() =>{
      if(1 < 0 && 0 > 1) {
        throw "plain wrong!";
      }
    })();
//@ts-expect-error 18022
 #$on_click_14_20 (event:SomeEvent) :void {
    var result = 'gotcha!';}
//@ts-expect-error 18022
 #$on_clickClack_55_41 (event:SomeEvent) :void {
    var test=0;}}
declare namespace SimpleMxmlClass {
  export type _ = SimpleMxmlClass_;
  export const _: { new(config?: _): _; };
}


export default SimpleMxmlClass;
