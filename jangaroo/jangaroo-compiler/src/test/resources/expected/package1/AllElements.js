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
      this.myVar$4 = config.myProperty$4 + '_suffix';
      this.myVar2$4 = {
        prop: config.myProperty$4
      };
    }/*override protected*/function initConfig(_config/*:Object*/)/*:void*/{
    var config/*:AllElements*/ =AS3.cast(AllElements,_config);this.__initialize__$4( config);
    var config_$1/*:AllElements*/ =AS3.cast(AllElements,{});
    var defaults_$1/*:AllElements*/ =AS3.cast(AllElements,{});
    var myVar3_46_5_$1/*: ext.config.button*/ =AS3.cast(ext.config.button,{});
    myVar3_46_5_$1["text"] = "Foo";
    this.myVar3$4 = new ext.config.button(myVar3_46_5_$1);
    var object_50_7_$1/*:Object*/ = {};
    object_50_7_$1["header"] = "a";
    object_50_7_$1["sortable"] = false;
    object_50_7_$1["menuDisabled"] = true;
    var object_51_7_$1/*:Object*/ = {};
    object_51_7_$1["header"] = "b";
    object_51_7_$1["sortable"] = true;
    object_51_7_$1["menuDisabled"] = false;
    this.myVar4$4 = [object_50_7_$1, object_51_7_$1];
    config= net.jangaroo.ext.Exml.apply(defaults_$1,config);
    var panel_55_3_$1/*: ext.config.panel*/ =AS3.cast(ext.config.panel,{});
    panel_55_3_$1["title"] = "I am a panel";
    panel_55_3_$1["layout"] = config.myProperty$4;
    var someMixin_58_7_$1/*: ext.config.someMixin*/ =AS3.cast(ext.config.someMixin,{});
    var button_60_11_$1/*: ext.config.button*/ =AS3.cast(ext.config.button,{});
    button_60_11_$1["text"] = "click me!";
    button_60_11_$1["id"] = "myId";
    var ext_Action_62_15_$1/*: ext.Action*/ =AS3.cast(ext.Action,{});
    button_60_11_$1["baseAction"] = new ext.Action(ext_Action_62_15_$1);
    someMixin_58_7_$1["someList"] = [button_60_11_$1];
    someMixin_58_7_$1["someList$at"] = net.jangaroo.ext.Exml.APPEND;
    panel_55_3_$1["mixins"] = [someMixin_58_7_$1];
    panel_55_3_$1["defaults"] = "";
    var object_76_7_$1/*:Object*/ = {};
    object_76_7_$1["bla"] = "blub";
    object_76_7_$1["anchor"] = "test";
    object_76_7_$1["border"] = "solid";
    panel_55_3_$1["layoutConfig"] = object_76_7_$1;
    var button_83_7_$1/*: ext.config.button*/ =AS3.cast(ext.config.button,{});
    button_83_7_$1["text"] = "Save";
    button_83_7_$1["handler"] = function()/*:void*/ {
          ext.MessageBox.alert('gotcha!');
        };
    var object_88_7_$1/*:Object*/ = {xtype: "editortreepanel"};
    var object_89_7_$1/*:Object*/ = {};
    panel_55_3_$1["items"] = [button_83_7_$1, object_88_7_$1, object_89_7_$1];
    var menuitem_100_7_$1/*: ext.config.menuitem*/ =AS3.cast(ext.config.menuitem,{});
    menuitem_100_7_$1["text"] = "juhu1";
    var menuitem_101_7_$1/*: ext.config.menuitem*/ =AS3.cast(ext.config.menuitem,{});
    menuitem_101_7_$1["text"] = "juhu2";
    var menuitem_102_7_$1/*: ext.config.menuitem*/ =AS3.cast(ext.config.menuitem,{});
    menuitem_102_7_$1["text"] = "juhu3";
    panel_55_3_$1["menu"] = [menuitem_100_7_$1, menuitem_101_7_$1, menuitem_102_7_$1];
    AS3.setBindable(this,"gear" , {});
    AS3.getBindable(this,"gear")["handler"] = function(x){return ''+x;};
    panel_55_3_$1["tools"] = [AS3.getBindable(this,"gear")];
    var aplugin_118_7_$1/*: ext.config.aplugin*/ =AS3.cast(ext.config.aplugin,{});
    var aplugin_119_7_$1/*: ext.config.aplugin*/ =AS3.cast(ext.config.aplugin,{});
    panel_55_3_$1["plugins"] = [aplugin_118_7_$1, aplugin_119_7_$1];
    config_$1["items"] = [panel_55_3_$1]; net.jangaroo.ext.Exml.apply(config_$1,config);ext.Panel.prototype.initConfig.call(this,config_$1);
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
      myProperty$4: null,
      myVar$4: null,
      myVar2$4: null,
      myVar3$4: null,
      myVar4$4: null,
      __initialize__$4: __initialize__,
      initConfig: initConfig,
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
