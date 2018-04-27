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
      this.myVar$6tZL = config.myProperty$6tZL + '_suffix';
      this.myVar2$6tZL = {
        prop: config.myProperty$6tZL
      };
    }/*

    public*/function AllElements$(config/*:AllElements = null*/){if(arguments.length<=0)config=null;this.__initialize__$6tZL(config);
    var config_$1/*: ext.config.panel*/ =AS3.cast(ext.config.panel,{});
    var defaults_$1/*:AllElements*/ =AS3.cast(AllElements,{});
    var myVar3_54_5_$1/*: ext.config.button*/ =AS3.cast(ext.config.button,{});
    myVar3_54_5_$1.text = "Foo";
    this.myVar3$6tZL = new ext.config.button(myVar3_54_5_$1);
    var object_58_7_$1/*:Object*/ = {};
    object_58_7_$1.header = "a";
    object_58_7_$1.sortable = false;
    object_58_7_$1.menuDisabled = true;
    var object_59_7_$1/*:Object*/ = {};
    object_59_7_$1.header = "b";
    object_59_7_$1.sortable = true;
    object_59_7_$1.menuDisabled = false;
    this.myVar4$6tZL = [object_58_7_$1, object_59_7_$1];
    config = net.jangaroo.ext.Exml.apply(defaults_$1,config);
    config_$1.title = "I am a panel";
    config_$1.layout =net.jangaroo.ext.Exml.asString( config.myProperty$6tZL);
    var someMixin_65_7_$1/*: ext.config.someMixin*/ =AS3.cast(ext.config.someMixin,{});
    var button_67_11_$1/*: ext.config.button*/ =AS3.cast(ext.config.button,{});
    button_67_11_$1.text = "click me!";
    button_67_11_$1["id"] = "myId";
    var ext_Action_69_15_$1/*: ext.Action*/ =AS3.cast(ext.Action,{});
    button_67_11_$1.baseAction = new ext.Action(ext_Action_69_15_$1);
    someMixin_65_7_$1.someList = [button_67_11_$1];
    someMixin_65_7_$1["someList$at"] = net.jangaroo.ext.Exml.APPEND;
    config_$1.mixins = [someMixin_65_7_$1];
    config_$1.defaults = null;
    var object_83_7_$1/*:Object*/ = {};
    object_83_7_$1.bla = "blub";
    object_83_7_$1.anchor = "test";
    object_83_7_$1.border = "solid";
    config_$1.layoutConfig = object_83_7_$1;
    var button_90_7_$1/*: ext.config.button*/ =AS3.cast(ext.config.button,{});
    button_90_7_$1.text = "Save";
    button_90_7_$1.handler = function()/*:void*/ {
          ext.MessageBox.alert('gotcha!');
        };
    var object_95_7_$1/*:Object*/ = {xtype: "editortreepanel"};
    var object_96_7_$1/*:Object*/ = {};
    config_$1.items = [button_90_7_$1, object_95_7_$1, object_96_7_$1];
    var menuitem_107_7_$1/*: ext.config.menuitem*/ =AS3.cast(ext.config.menuitem,{});
    menuitem_107_7_$1.text = "juhu1";
    var menuitem_108_7_$1/*: ext.config.menuitem*/ =AS3.cast(ext.config.menuitem,{});
    menuitem_108_7_$1.text = "juhu2";
    var menuitem_109_7_$1/*: ext.config.menuitem*/ =AS3.cast(ext.config.menuitem,{});
    menuitem_109_7_$1.text = "juhu3";
    config_$1.menu = [menuitem_107_7_$1, menuitem_108_7_$1, menuitem_109_7_$1];
    AS3.setBindable(this,"gear" , {});
    AS3.getBindable(this,"gear").handler = function(x){return ''+x;};
    config_$1.tools = [AS3.getBindable(this,"gear")];
    var aplugin_125_7_$1/*: ext.config.aplugin*/ =AS3.cast(ext.config.aplugin,{});
    var aplugin_126_7_$1/*: ext.config.aplugin*/ =AS3.cast(ext.config.aplugin,{});
    config_$1.plugins = [aplugin_125_7_$1, aplugin_126_7_$1];
    net.jangaroo.ext.Exml.apply(config_$1,config);
    this.super$6tZL(config_$1);
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
