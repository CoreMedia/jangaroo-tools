Ext.define("package1.AllElements", function(AllElements) {/*package package1{
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
      this.myVar$3 = config.myProperty$3 + '_suffix';
      this.myVar2$3 = {
        prop: config.myProperty$3
      };
    }/*

    public*/function AllElements$(config/*:AllElements = null*/){if(arguments.length<=0)config=null;this.__initialize__$3(config);
    var config_$1/*:AllElements*/ =AS3.cast(AllElements,{});
    var defaults_$1/*:AllElements*/ =AS3.cast(AllElements,{});
    var myVar3_52_5_$1/*: ext.config.button*/ =AS3.cast(ext.config.button,{});
    myVar3_52_5_$1["text"] = "Foo";
    this.myVar3$3 = new ext.config.button(myVar3_52_5_$1);
    var object_56_7_$1/*:Object*/ = {};
    object_56_7_$1["header"] = "a";
    object_56_7_$1["sortable"] = false;
    object_56_7_$1["menuDisabled"] = true;
    var object_57_7_$1/*:Object*/ = {};
    object_57_7_$1["header"] = "b";
    object_57_7_$1["sortable"] = true;
    object_57_7_$1["menuDisabled"] = false;
    this.myVar4$3 = [object_56_7_$1, object_57_7_$1];
    config = net.jangaroo.ext.Exml.apply(defaults_$1,config);
    var panel_61_3_$1/*: ext.config.panel*/ =AS3.cast(ext.config.panel,{});
    panel_61_3_$1["title"] = "I am a panel";
    panel_61_3_$1["layout"] = config.myProperty$3;
    var someMixin_64_7_$1/*: ext.config.someMixin*/ =AS3.cast(ext.config.someMixin,{});
    var button_66_11_$1/*: ext.config.button*/ =AS3.cast(ext.config.button,{});
    button_66_11_$1["text"] = "click me!";
    button_66_11_$1["id"] = "myId";
    var ext_Action_68_15_$1/*: ext.Action*/ =AS3.cast(ext.Action,{});
    button_66_11_$1["baseAction"] = new ext.Action(ext_Action_68_15_$1);
    someMixin_64_7_$1["someList"] = [button_66_11_$1];
    someMixin_64_7_$1["someList$at"] = net.jangaroo.ext.Exml.APPEND;
    panel_61_3_$1["mixins"] = [someMixin_64_7_$1];
    panel_61_3_$1["defaults"] = "";
    var object_82_7_$1/*:Object*/ = {};
    object_82_7_$1["bla"] = "blub";
    object_82_7_$1["anchor"] = "test";
    object_82_7_$1["border"] = "solid";
    panel_61_3_$1["layoutConfig"] = object_82_7_$1;
    var button_89_7_$1/*: ext.config.button*/ =AS3.cast(ext.config.button,{});
    button_89_7_$1["text"] = "Save";
    button_89_7_$1["handler"] = function()/*:void*/ {
          ext.MessageBox.alert('gotcha!');
        };
    var object_94_7_$1/*:Object*/ = {xtype: "editortreepanel"};
    var object_95_7_$1/*:Object*/ = {};
    panel_61_3_$1["items"] = [button_89_7_$1, object_94_7_$1, object_95_7_$1];
    var menuitem_106_7_$1/*: ext.config.menuitem*/ =AS3.cast(ext.config.menuitem,{});
    menuitem_106_7_$1["text"] = "juhu1";
    var menuitem_107_7_$1/*: ext.config.menuitem*/ =AS3.cast(ext.config.menuitem,{});
    menuitem_107_7_$1["text"] = "juhu2";
    var menuitem_108_7_$1/*: ext.config.menuitem*/ =AS3.cast(ext.config.menuitem,{});
    menuitem_108_7_$1["text"] = "juhu3";
    panel_61_3_$1["menu"] = [menuitem_106_7_$1, menuitem_107_7_$1, menuitem_108_7_$1];
    AS3.setBindable(this,"gear" , {});
    AS3.getBindable(this,"gear")["handler"] = function(x){return ''+x;};
    panel_61_3_$1["tools"] = [AS3.getBindable(this,"gear")];
    var aplugin_124_7_$1/*: ext.config.aplugin*/ =AS3.cast(ext.config.aplugin,{});
    var aplugin_125_7_$1/*: ext.config.aplugin*/ =AS3.cast(ext.config.aplugin,{});
    panel_61_3_$1["plugins"] = [aplugin_124_7_$1, aplugin_125_7_$1];
    config_$1["items"] = [panel_61_3_$1];
    net.jangaroo.ext.Exml.apply(config_$1,config);
    ext.Panel.prototype.constructor.call(this,config_$1);
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
      extend: "ext.Panel",
      myProperty$3: null,
      myVar$3: null,
      myVar2$3: null,
      myVar3$3: null,
      myVar4$3: null,
      __initialize__$3: __initialize__,
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
        "ext.Panel"
      ],
      uses: [
        "ext.MessageBox",
        "ext.config.aplugin",
        "ext.config.button",
        "ext.config.menuitem",
        "ext.config.panel",
        "ext.config.someMixin",
        "net.jangaroo.ext.Exml"
      ]
    };
});
