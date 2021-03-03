import {_, cast} from '@jangaroo/joo/AS3';
import button from '../ext/config/button';
import menuitem from '../ext/config/menuitem';
import allElements from '../exmlparser/config/allElements';
import someMixin from '../ext/config/someMixin';
import aplugin from '../ext/config/aplugin';
import uint from '../AS3/int_';
import int from '../AS3/int_';
import panel from '../ext/config/panel';
import Exml from '../net/jangaroo/ext/Exml';
import MessageBox from '../ext/MessageBox';
interface AllElements_ extends Partial<Pick<AllElements,
    "gear"
>> {
}


/**
 This is my <b>TestComponent</b>
 */
class AllElements<Cfg extends AllElements._ = AllElements._> extends panel<Cfg>{

    /**
     * This is my <b>constant</b>
     */
     static readonly SOME_CONSTANT:uint = allElements.SOME_CONSTANT;
    /**
     * This is another <b>constant</b>
     */
     static readonly ANOTHER_CONSTANT:string = allElements.ANOTHER_CONSTANT;
     static readonly CODE_CONSTANT:int = allElements.CODE_CONSTANT;

     #myProperty:string = null;
     #myVar:string = null;
     #myVar2:any = null;
     #myVar3:button = null;
     #myVar4:Array<any> = null;

    //@ts-expect-error 18022
     #__initialize__(config:AllElements._):void {
      this.#myVar = config.#myProperty + '_suffix';
      this.#myVar2 = {
        prop: config.#myProperty
      };
    }

     constructor(config:AllElements._ = null){
    super((()=>{this.#__initialize__(config);
  
    this.#myVar3 = new button(_<button._>({
            text: "Foo"}));
  
    this.#myVar4 =[
      { header: "a", sortable: false, menuDisabled: true},
      { header: "b", sortable: true, menuDisabled: false}
    ];
    return  Exml.apply(new AllElements._({
           title: "I am a panel",
           layout: Exml.asString( config.#myProperty),

    /* define some attributes through a typed mixin: */
    mixins:[
      cast(someMixin,{
        ...Exml.append({someList: [
          cast(button,{ text: "click me!", id: "myId",
            baseAction:
              new ext.Action(_<Action._>({
            }))
          })
        ]})
      })
    ],

    /* attribute*/
    layoutConfig:
      { bla: "blub", anchor: "test", border: "solid"
    },

    /* array with component
    items:{xtype:"testAll", ...}
     */
    items:[
      cast(button,{ text: "Save",
        handler: ():void => {
          MessageBox.alert('gotcha!');
        }
      }),
      {xtype: "editortreepanel"},
      {}
    ],


    /* array
    menu:[
      {...},
      {...}
    ]
     */
    menu:[
      cast(menuitem,{ text: "juhu1"}),
      cast(menuitem,{ text: "juhu2"}),
      cast(menuitem,{ text: "juhu3"})
    ],

    tools:[
      /*
      anonymous object in array:

      tools:[
        {id:"gear",
        handler:function(){} }
      ]
       */
      this.setConfig("gear" ,{ handler: (x) => ''+x})
    ],

    plugins:[
      cast(aplugin,{}),
      cast(aplugin,{})
    ]

}),config);})());
  }

      /*
      anonymous object in array:

      tools:[
        {id:"gear",
        handler:function(){} }
      ]
       */
      
       #gear:any = null;
  get gear():any { return this.#gear; }
  set gear(value:any) { this.#gear = value; }}
declare namespace AllElements {
  export type _ = AllElements_;
  export const _: { new(config?: _): _; };
}


export default AllElements;
