import Config from "@jangaroo/runtime/Config";
import { asConfig, bind } from "@jangaroo/runtime";
import int from "../AS3/int_";
import uint from "../AS3/uint_";
import Action from "../Ext/Action";
import Button from "../Ext/Button";
import Component from "../Ext/Component";
import MenuItem from "../Ext/MenuItem";
import Panel from "../Ext/Panel";
import SomeMixin from "../Ext/mixin/SomeMixin";
import APlugin from "../Ext/plugins/APlugin";
import MessageBoxWindow from "../Ext/window/MessageBox";
import allElements from "../exmlparser/config/allElements";
import Exml from "../net/jangaroo/ext/Exml";
interface AllElementsConfig extends Config<Panel>, Partial<Pick<AllElements,
  "gear"
>> {
}


/**
 * This is my <b>TestComponent</b>
 */
class AllElements extends Panel{
  declare Config: AllElementsConfig;

    /**
     * This is my <b>constant</b>
     */
  static readonly SOME_CONSTANT:uint = allElements.SOME_CONSTANT;
    /**
     * This is another <b>constant</b>
     */
  static readonly ANOTHER_CONSTANT:string = allElements.ANOTHER_CONSTANT;
  static readonly CODE_CONSTANT:int = allElements.CODE_CONSTANT;

  #myProperty:any;
  #myVar:string = null;
  #myVar2:any = null;
  #myVar3:Button = null;
  #myVar4:Array<any> = null;

  #__initialize__(config:Config<AllElements>):void {
      this.#myVar = config.#myProperty + "_suffix";
      this.#myVar2 = {
        prop: config.#myProperty
      };
      const unused = new MessageBoxWindow();
    }

  constructor(config:Config<AllElements> = null){
    super((()=>{this.#__initialize__(config);
  
    this.#myVar3 = new Button({
            text: "Foo"});
  
    this.#myVar4 =[
      { header: "a", sortable: false, menuDisabled: true},
      { header: "b", sortable: true, menuDisabled: false}
    ];
    return  Exml.apply(Config(AllElements, {
           title: "I am a panel",
           layout: config.#myProperty,

    /* define some attributes through a typed mixin: */
    mixins:[
      Config(SomeMixin, {
        ...Exml.append({someList: [
          Config(Button, { text: "click me!", id: "myId",
            baseAction: new Action({
            })
          })
        ]})
      })
    ],

    /* attribute*/
    layoutConfig: { bla: "blub", anchor: "test", border: "solid"
    },

    /* array with component
    items:{xtype:"testAll", ...}
     */
    items:[
      Config(Button, { text: "Save",
        handler: ():void => 
          MessageBoxWindow.getInstance().alert("gotcha!")
        
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
      Config(MenuItem, { text: "juhu1"}),
      Config(MenuItem, { text: "juhu2"}),
      Config(MenuItem, { text: "juhu3"})
    ],

    tools:[
      /*
      anonymous object in array:

      tools:[
        {id:"gear",
        handler:function(){} }
      ]
       */
      this.gear ={ handler: (x) => ""+x}
    ],

    plugins:[
      Config(APlugin),
      Config(APlugin)
    ],
    listeners:{
           flipflop: bind(this,this.#$on_flipflop_14_23)}

}),config);})());
  }

  #$on_flipflop_14_23 (source: Component) :void {
      const event = { source };
      this.#myProperty = event.source;
    }

  #gear:any = null;

      /*
      anonymous object in array:

      tools:[
        {id:"gear",
        handler:function(){} }
      ]
       */
  get gear():any { return this.#gear; }
  set gear(value:any) { this.#gear = value; }}
export default AllElements;
