/*package package1{
import ext.*;
import exmlparser.config.*;
import ext.mixin.*;
import ext.plugins.*;
import ext.events.PanelEvent;
import net.jangaroo.ext.Exml;*/
/**
 * This is my <b>TestComponent</b>
 */
Ext.define("package1.AllElements", function(AllElements) {/*public class AllElements extends Panel{

    /* add an extra import statement to the class * /

    import exmlparser.config.allElements;

    import ext.MessageBox;

    /**
     * This is my <b>constant</b>
     * /
    public static const SOME_CONSTANT:uint =*/function SOME_CONSTANT$static_(){AllElements.SOME_CONSTANT=( exmlparser.config.allElements.SOME_CONSTANT);}/*;
    /**
     * This is another <b>constant</b>
     * /
    public static const ANOTHER_CONSTANT:String =*/function ANOTHER_CONSTANT$static_(){AllElements.ANOTHER_CONSTANT=( exmlparser.config.allElements.ANOTHER_CONSTANT);}/*;
    public static const CODE_CONSTANT:int =*/function CODE_CONSTANT$static_(){AllElements.CODE_CONSTANT=( exmlparser.config.allElements.CODE_CONSTANT);}/*;

    private var myProperty:*;
    private var myVar:String;
    private var myVar2:Object;
    private var myVar3:Button;
    private var myVar4:Array;

    private*/ function __initialize__(config/*:AllElements*/)/*:void*/ {
      this.myVar$6tZL = config.myProperty$6tZL + '_suffix';
      this.myVar2$6tZL = {
        prop: config.myProperty$6tZL
      };
    }/*

    public*/function AllElements$(config/*:AllElements = null*/){if(arguments.length<=0)config=null;this.__initialize__$6tZL(config);
  
    this.myVar3$6tZL = new Ext.Button({
            text: "Foo"});
  
    this.myVar4$6tZL =[
      { header: "a", sortable: false, menuDisabled: true},
      { header: "b", sortable: true, menuDisabled: false}
    ];
    this.super$6tZL( net.jangaroo.ext.Exml.apply(AS3.cast(AllElements,{
           title: "I am a panel",
           layout: config.myProperty$6tZL,

    /* define some attributes through a typed mixin: */
    mixins:[
      AS3.cast(Ext.mixin.SomeMixin,{someList$at: net.jangaroo.ext.Exml.APPEND, 
        someList:([
          AS3.cast(Ext.Button,{ text: "click me!", id: "myId",
            baseAction:
              new Ext.Action({
            })
          })
        ])
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
      AS3.cast(Ext.Button,{ text: "Save",
        handler: function()/*:void*/ {
          Ext.MessageBox.alert('gotcha!');
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
      AS3.cast(Ext.MenuItem,{ text: "juhu1"}),
      AS3.cast(Ext.MenuItem,{ text: "juhu2"}),
      AS3.cast(Ext.MenuItem,{ text: "juhu3"})
    ],

    tools:[
      /*
      anonymous object in array:

      tools:[
        {id:"gear",
        handler:function(){} }
      ]
       */
      this.gear ={ handler: function gear(x){return ''+x;}}
    ],

    plugins:[
      AS3.cast(Ext.plugins.APlugin,{}),
      AS3.cast(Ext.plugins.APlugin,{})
    ],
    listeners:{
           flipflop:net.jangaroo.ext.Exml.eventHandler(Ext.events.PanelEvent.FLIPFLOP,Ext.events.PanelEvent,AS3.bind(this,"$on_flipflop_14_23$6tZL"))}

}),config));
  }/*

    private*/ function $on_flipflop_14_23 (event/*:ext.events.PanelEvent*/)/* :void*/ {
      this.myProperty$6tZL = 1;
    }/*

      /*
      anonymous object in array:

      tools:[
        {id:"gear",
        handler:function(){} }
      ]
       * /
      [Bindable]
      public var gear:Object;}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "Ext.Panel",
      myProperty$6tZL: undefined,
      myVar$6tZL: null,
      myVar2$6tZL: null,
      myVar3$6tZL: null,
      myVar4$6tZL: null,
      __initialize__$6tZL: __initialize__,
      constructor: AllElements$,
      super$6tZL: function() {
        Ext.Panel.prototype.constructor.apply(this, arguments);
      },
      $on_flipflop_14_23$6tZL: $on_flipflop_14_23,
      config: {gear: null},
      statics: {
        SOME_CONSTANT: undefined,
        ANOTHER_CONSTANT: undefined,
        CODE_CONSTANT: undefined,
        __initStatics__: function() {
          SOME_CONSTANT$static_();
          ANOTHER_CONSTANT$static_();
          CODE_CONSTANT$static_();
        }
      },
      requires: ["Ext.Panel"],
      uses: [
        "Ext.Action",
        "Ext.Button",
        "Ext.MenuItem",
        "Ext.events.PanelEvent",
        "Ext.mixin.SomeMixin",
        "Ext.plugins.APlugin",
        "Ext.window.MessageBox",
        "exmlparser.config.allElements",
        "net.jangaroo.ext.Exml"
      ]
    };
});
