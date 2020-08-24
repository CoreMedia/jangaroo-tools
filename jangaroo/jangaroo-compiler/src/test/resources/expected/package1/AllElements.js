/*package package1{
import ext.config.*;
import exmlparser.config.*;
import ext.*;
import net.jangaroo.ext.Exml;*/
/**
 This is my <b>TestComponent</b>
 */
Ext.define("package1.AllElements", function(AllElements) {/*public class AllElements extends panel{

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
      this.myVar$6tZL = config.myProperty$6tZL + '_suffix';
      this.myVar2$6tZL = {
        prop: config.myProperty$6tZL
      };
    }/*

    public*/function AllElements$(config/*:AllElements = null*/){if(arguments.length<=0)config=null;this.__initialize__$6tZL(config);
    this.myVar3$6tZL = new ext.config.button(AS3.cast(ext.config.button,{
    text: "Foo"
    }));
    this.myVar4$6tZL =[{
    header: "a",
    sortable: false,
    menuDisabled: true
    },{
    header: "b",
    sortable: true,
    menuDisabled: false
    }];
    this.super$6tZL( net.jangaroo.ext.Exml.apply( AS3.cast(ext.config.panel,{
    title: "I am a panel",
    layout:net.jangaroo.ext.Exml.asString( config.myProperty$6tZL),
    mixins:[ AS3.cast(ext.config.someMixin,{
    someList:[ AS3.cast(ext.config.button,{
    text: "click me!",
    id: "myId",
    baseAction: new ext.Action(AS3.cast(ext.Action,{
    }))
    })],
    someList$at:net.jangaroo.ext.Exml.APPEND
    })],
    layoutConfig:{
    bla: "blub",
    anchor: "test",
    border: "solid"
    },
    items:[ AS3.cast(ext.config.button,{
    text: "Save",
    handler: function()/*:void*/ {
          ext.MessageBox.alert('gotcha!');
        }
    }), {xtype: "editortreepanel"},{
    }],
    menu:[ AS3.cast(ext.config.menuitem,{
    text: "juhu1"
    }), AS3.cast(ext.config.menuitem,{
    text: "juhu2"
    }), AS3.cast(ext.config.menuitem,{
    text: "juhu3"
    })],
    tools:[
    AS3.setBindable(this,"gear" ,{
    handler: function(x){return ''+x;}
    })],
    plugins:[ AS3.cast(ext.config.aplugin,{
    }), AS3.cast(ext.config.aplugin,{
    })]
    }),config));
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
      myProperty$6tZL: null,
      myVar$6tZL: null,
      myVar2$6tZL: null,
      myVar3$6tZL: null,
      myVar4$6tZL: null,
      __initialize__$6tZL: __initialize__,
      constructor: AllElements$,
      super$6tZL: function() {
        ext.config.panel.prototype.constructor.apply(this, arguments);
      },
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
      requires: ["ext.config.panel"],
      uses: [
        "exmlparser.config.allElements",
        "ext.MessageBox",
        "ext.config.aplugin",
        "ext.config.button",
        "ext.config.menuitem",
        "ext.config.someMixin",
        "net.jangaroo.ext.Exml"
      ]
    };
});
