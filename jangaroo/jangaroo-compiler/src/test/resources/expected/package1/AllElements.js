Ext.define("package1.AllElements", function(AllElements) {/*package package1{
import ext.config.*;
import exmlparser.config.*;
import ext.*;
import net.jangaroo.ext.Exml;
/**
 This is my <b>TestComponent</b>
 * /
public class AllElements extends panel{

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

    private var myProperty:String;
    private var myVar:String;
    private var myVar2:Object;
    private var myVar3:button;
    private var myVar4:Array;

    private*/ function __initialize__(config/*:AllElements*/)/*:void*/ {
      this.myVar$4 = config.myProperty$4 + '_suffix';
      this.myVar2$4 = {
        prop: config.myProperty$4
      };
    }/*

    public*/function AllElements$(config/*:AllElements = null*/){if(arguments.length<=0)config=null;this.__initialize__$4(config);config = net.jangaroo.ext.Exml.apply(

  {
    config:
    new exmlparser.config.allElements({ myProperty: "My Property!",
      myPropertyWithDescription: false
    }),
  
    myVar3:
  
    new ext.config.button({
            text: "Foo"}),
  
    myVar4:[
      { header: "a", sortable: false, menuDisabled: true},
      { header: "b", sortable: true, menuDisabled: false}
    ]
  },config);config = net.jangaroo.ext.Exml.apply(AS3.cast(AllElements,{
           title: "I am a panel",
           layout: config.myProperty$4,
        someList$at: net.jangaroo.ext.Exml.APPEND,
        someList:[
          AS3.cast(ext.config.button,{ text: "click me!", id: "myId",
            baseAction:
              new ext.Action({})
          })
        ],

    /*
      empty attribute
     */
    defaults: null,

    /* attribute*/
    layoutConfig:
      { bla: "blub", anchor: "test", border: "solid"},

    /* array with component
    items:{xtype:"testAll", ...}
     */
    items:[
      AS3.cast(ext.config.button,{ text: "Save",
        handler: function()/*:void*/ {
          ext.MessageBox.alert('gotcha!');
        }
      }), {xtype: "editortreepanel"},
      {}
    ],


    /* array
    menu:[
      {...},
      {...}
    ]
     */
    menu:[
      AS3.cast(ext.config.menuitem,{ text: "juhu1"}),
      AS3.cast(ext.config.menuitem,{ text: "juhu2"}),
      AS3.cast(ext.config.menuitem,{ text: "juhu3"})
    ],

    tools:[
      /*
      anonymous object in array:

      tools:[
        {id:"gear",
        handler:function(){} }
      ]
       */
      AS3.setBindable(this,"gear",{ handler: function(x){return ''+x;}})
    ],

    plugins:[
      AS3.cast(ext.config.aplugin,{}),
      AS3.cast(ext.config.aplugin,{})
    ]}),config);
    ext.config.panel.prototype.constructor.call(this,config);
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
      extend: "ext.config.panel",
      myProperty$4: null,
      myVar$4: null,
      myVar2$4: null,
      myVar3$4: null,
      myVar4$4: null,
      __initialize__$4: __initialize__,
      constructor: AllElements$,
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
      requires: [
        "exmlparser.config.allElements",
        "ext.config.panel"
      ],
      uses: [
        "ext.MessageBox",
        "ext.config.aplugin",
        "ext.config.button",
        "ext.config.menuitem",
        "net.jangaroo.ext.Exml"
      ]
    };
});
