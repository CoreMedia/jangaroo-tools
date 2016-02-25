Ext.define("AS3.package1.AllElements", function(AllElements) {/*package package1{
import ext.config.*;
import exmlparser.config.*;
import ext.*;
import net.jangaroo.ext.Exml;
/**
 This is my <b>TestComponent</b>
* /
public class AllElements extends Panel{

    /* add an extra import statement to the class * /

    import exmlparser.config.allElements;

    import ext.MessageBox;

    /**
     * This is my <b>constant</b>
     * /
    public static const SOME_CONSTANT:uint =*/function SOME_CONSTANT$static_(){AllElements.SOME_CONSTANT=( AS3.exmlparser.config.allElements.SOME_CONSTANT);}/*;
    /**
     * This is another <b>constant</b>
     * /
    public static const ANOTHER_CONSTANT:String =*/function ANOTHER_CONSTANT$static_(){AllElements.ANOTHER_CONSTANT=( AS3.exmlparser.config.allElements.ANOTHER_CONSTANT);}/*;
    public static const CODE_CONSTANT:int =*/function CODE_CONSTANT$static_(){AllElements.CODE_CONSTANT=( AS3.exmlparser.config.allElements.CODE_CONSTANT);}/*;

    private var myProperty:String;
    private var myVar:String;
    private var myVar2:Object;
    private var myVar3:button;
    private var myVar4:Array;

    private*/ function __initialize__(config/*:AllElements*/)/*:void*/ {
      this.myVar$2 = config.myProperty$2 + '_suffix';
      this.myVar2$2 = {
        prop: config.myProperty$2
      };
    }/*

    public*/function AllElements$(config/*:AllElements = null*/){if(arguments.length<=0)config=null;this. __initialize__$2(config);
var config_$1/*:AllElements*/ =AS3.cast(AllElements,{});
var defaults_$1/*:AllElements*/ ={};
config= AS3.net.jangaroo.ext.Exml.apply(defaults_$1,config);

    var config_$2/*:ext.config.button*/ =AS3.cast( AS3.ext.config.button,{});
    config_$2.text = "Foo";
    this.myVar3$2 = new AS3.ext.config.button(config_$2);
    var object_54_7_$1/*:Object*/ = {};
    object_54_7_$1.header = "a";
    object_54_7_$1.sortable = false;
    object_54_7_$1.menuDisabled = true;
    var object_55_7_$1/*:Object*/ = {};
    object_55_7_$1.header = "b";
    object_55_7_$1.sortable = true;
    object_55_7_$1.menuDisabled = false;
    this.myVar4$2 = [object_54_7_$1, object_55_7_$1];
    var config_$3/*:ext.config.panel*/ =AS3.cast( AS3.ext.config.panel,{});
    config_$3.title = "I am a panel";
    config_$3.layout = config.myLayout;
    var config_$4/*:ext.config.someMixin*/ =AS3.cast( AS3.ext.config.someMixin,{});
    var config_$5/*:ext.config.button*/ =AS3.cast( AS3.ext.config.button,{});
    config_$5.text = "click me!";
    config_$5["id"] = "myId";
    var config_$6/*:ext.Action*/ =AS3.cast( ext.Action,{});
    config_$6.action = "";
    config_$5.baseAction = new ext.Action(config_$6);
    config_$4.someList = [config_$5];
    config_$4.someList$at = AS3.net.jangaroo.ext.Exml.APPEND;
    config_$3.mixins = [config_$4];
    config_$3.defaults = "";
    var object_82_7_$1/*:Object*/ = {};
    object_82_7_$1.bla = "blub";
    object_82_7_$1.anchor = "test";
    object_82_7_$1.border = "solid";
    config_$3.layoutConfig = object_82_7_$1;
    var config_$7/*:ext.config.button*/ =AS3.cast( AS3.ext.config.button,{});
    config_$7.text = "Save";
    config_$7.handler = function()/*:void*/ {
          AS3.ext.MessageBox.alert('gotcha!');
        };
    var object_94_7_$1/*:Object*/ = {xtype: "editortreepanel"};
    var object_95_7_$1/*:Object*/ = {};
    config_$3.items = [config_$7, object_94_7_$1, object_95_7_$1];
    var config_$8/*:ext.config.menuitem*/ =AS3.cast( AS3.ext.config.menuitem,{});
    config_$8.text = "juhu1";
    var config_$9/*:ext.config.menuitem*/ =AS3.cast( AS3.ext.config.menuitem,{});
    config_$9.text = "juhu2";
    var config_$10/*:ext.config.menuitem*/ =AS3.cast( AS3.ext.config.menuitem,{});
    config_$10.text = "juhu3";
    config_$3.menu = [config_$8, config_$9, config_$10];AS3.setBindable(
    this,"gear" , {});AS3.getBindable(
    this,"gear").handler = function(x){return ''+x;};
    config_$3.tools = [AS3.getBindable(this,"gear")];
    var config_$11/*:ext.config.aplugin*/ =AS3.cast( AS3.ext.config.aplugin,{});
    var config_$12/*:ext.config.aplugin*/ =AS3.cast( AS3.ext.config.aplugin,{});
    config_$3.plugins = [config_$11, config_$12];
    config_$1.items = [config_$3]; AS3.net.jangaroo.ext.Exml.apply(config_$1,config);AS3.ext.Panel.prototype.constructor.call(this,config_$1);}/*

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
      extend: "AS3.ext.Panel",
      myProperty$2: null,
      myVar$2: null,
      myVar2$2: null,
      myVar3$2: null,
      myVar4$2: null,
      __initialize__$2: __initialize__,
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
        "AS3.exmlparser.config.allElements",
        "AS3.ext.Panel"
      ],
      uses: [
        "AS3.ext.MessageBox",
        "AS3.ext.config.aplugin",
        "AS3.ext.config.button",
        "AS3.ext.config.menuitem",
        "AS3.ext.config.panel",
        "AS3.ext.config.someMixin",
        "AS3.net.jangaroo.ext.Exml"
      ]
    };
});
