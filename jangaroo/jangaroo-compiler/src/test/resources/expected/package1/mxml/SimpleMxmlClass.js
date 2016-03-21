Ext.define("AS3.package1.mxml.SimpleMxmlClass", function(SimpleMxmlClass) {/*package package1.mxml{
import package1.*;
import package1.someOtherPackage.*;
import net.jangaroo.ext.Exml;
import joo.addEventListener;
/**
  My config class subclass, authored in MXML.
* /

public class SimpleMxmlClass extends ConfigClass implements package1.Interface{

    public static const xtype:String = "testNamespace.config.simpleMxmlClass";

    public*/function SimpleMxmlClass$(config/*:SimpleMxmlClass = null*/){if(arguments.length<=0)config=null;
var config_$1/*:SimpleMxmlClass*/ =AS3.cast(SimpleMxmlClass,{});
var defaults_$1/*:SimpleMxmlClass*/ ={};

    AS3.setBindable(defaults_$1,"bar" , "FOO & BAR!");
    AS3.setBindable(defaults_$1,"computed" , 'B' + 'AR!');
    AS3.setBindable(defaults_$1,"num" , 123);
    this.blub$2 = {};
    this.blub$2.name = "Kuno";
    var object_31_7_$1/*:Object*/ = {};
    object_31_7_$1.name = "Joe";
    var configClass_32_7_$1/*:package1.ConfigClass*/ = AS3.cast(AS3.package1.ConfigClass,{});
    var other$ns_SomeOtherClass_33_9_$1/*:package1.someOtherPackage.SomeOtherClass*/ = AS3.cast(AS3.package1.someOtherPackage.SomeOtherClass,{});
    AS3.setBindable(other$ns_SomeOtherClass_33_9_$1,"bla" , 123);
    configClass_32_7_$1.items = [new AS3.package1.someOtherPackage.SomeOtherClass(other$ns_SomeOtherClass_33_9_$1)];
    AS3.setBindable(defaults_$1,"list" , [object_31_7_$1, new AS3.package1.ConfigClass(configClass_32_7_$1)]);
    AS3.setBindable(defaults_$1,"otherByExpression" , { foo: 'bar'});
    var other_37_5_$1/*:package1.someOtherPackage.SomeOtherClass*/ = AS3.cast(AS3.package1.someOtherPackage.SomeOtherClass,{});
    AS3.setBindable(other_37_5_$1,"bla" , 3);
    other_37_5_$1["blubb_config"] = 'blub config expression';
    other_37_5_$1["blubb_accessor"] = 'blub accessor expression';
    AS3.setBindable(defaults_$1,"other" , new AS3.package1.someOtherPackage.SomeOtherClass(other_37_5_$1));
config= AS3.net.jangaroo.ext.Exml.apply(defaults_$1,config);

    config_$1.foo = "bar";
    config_$1.number = 1 < 2  ? 1 + 1 : 3;
    joo.addEventListener(config_$1, "click",AS3.bind( this,"$on_config_$1_click$2"), AS3.package1.someOtherPackage.SomeEvent);
    var other$ns_SomeOtherClass_42_5_$1/*:package1.someOtherPackage.SomeOtherClass*/ = AS3.cast(AS3.package1.someOtherPackage.SomeOtherClass,{});
    AS3.setBindable(other$ns_SomeOtherClass_42_5_$1,"bla" , 99);
    other$ns_SomeOtherClass_42_5_$1["known-unknown"] = true;
    config_$1.defaultType = other$ns_SomeOtherClass_42_5_$1['xtype'];
    delete other$ns_SomeOtherClass_42_5_$1['xtype'];
    delete other$ns_SomeOtherClass_42_5_$1['xclass'];
    config_$1.defaults = other$ns_SomeOtherClass_42_5_$1;
    var other$ns_SomeOtherClass_46_5_$1/*:package1.someOtherPackage.SomeOtherClass*/ = AS3.cast(AS3.package1.someOtherPackage.SomeOtherClass,{});
    AS3.setBindable(other$ns_SomeOtherClass_46_5_$1,"bla" , 23);
    var other$ns_SomeOtherClass_47_5_$1/*:package1.someOtherPackage.SomeOtherClass*/ = AS3.cast(AS3.package1.someOtherPackage.SomeOtherClass,{});
    joo.addEventListener(other$ns_SomeOtherClass_47_5_$1, "clack",AS3.bind( this,"$on_other$ns_SomeOtherClass_47_5_$1_clack$2"), AS3.package1.someOtherPackage.SomeEvent);
    AS3.setBindable(other$ns_SomeOtherClass_47_5_$1,"bla" , 1);
    var other$ns_SomeOtherClass_48_5_$1/*:package1.someOtherPackage.SomeOtherClass*/ = AS3.cast(AS3.package1.someOtherPackage.SomeOtherClass,{});
    AS3.setBindable(other$ns_SomeOtherClass_48_5_$1,"bla" , 42);
    other$ns_SomeOtherClass_48_5_$1.number = 24;
    var configClass_53_5_$1/*:package1.ConfigClass*/ = AS3.cast(AS3.package1.ConfigClass,{});
    var other$ns_SomeOtherClass_55_9_$1/*:package1.someOtherPackage.SomeOtherClass*/ = AS3.cast(AS3.package1.someOtherPackage.SomeOtherClass,{});
    other$ns_SomeOtherClass_55_9_$1.doodle = "non-bound";
    AS3.setBindable(other$ns_SomeOtherClass_55_9_$1,"bla" , AS3.getBindable(AS3.getBindable(this,"other"),"bla","bla_has_changed"));
    configClass_53_5_$1.items = [new AS3.package1.someOtherPackage.SomeOtherClass(other$ns_SomeOtherClass_55_9_$1)];
    configClass_53_5_$1.number = 12;
    var configClass_59_5_$1/*:package1.ConfigClass*/ = AS3.cast(AS3.package1.ConfigClass,{});
    var other$ns_SomeOtherClass_61_9_$1/*:package1.someOtherPackage.SomeOtherClass*/ = AS3.cast(AS3.package1.someOtherPackage.SomeOtherClass,{});
    AS3.setBindable(other$ns_SomeOtherClass_61_9_$1,"bla" , 12);
    var no_config_62_9_$1/*:package1.someOtherPackage.SomeOtherClass*/ = AS3.cast(AS3.package1.someOtherPackage.SomeOtherClass,{});
    AS3.setBindable(no_config_62_9_$1,"bla" , 13);
    AS3.setBindable(this,"no_config" , new AS3.package1.someOtherPackage.SomeOtherClass(no_config_62_9_$1));
    configClass_59_5_$1.items = [new AS3.package1.someOtherPackage.SomeOtherClass(other$ns_SomeOtherClass_61_9_$1), AS3.getBindable(this,"no_config")];
    configClass_59_5_$1.items$at = AS3.net.jangaroo.ext.Exml.APPEND;
    config_$1.items = [new AS3.package1.someOtherPackage.SomeOtherClass(other$ns_SomeOtherClass_46_5_$1), new AS3.package1.someOtherPackage.SomeOtherClass(other$ns_SomeOtherClass_47_5_$1), new AS3.package1.someOtherPackage.SomeOtherClass(other$ns_SomeOtherClass_48_5_$1), new AS3.package1.ConfigClass(configClass_53_5_$1), new AS3.package1.ConfigClass(configClass_59_5_$1)];
    config_$1.items$at = AS3.net.jangaroo.ext.Exml.APPEND; AS3.net.jangaroo.ext.Exml.apply(config_$1,config);AS3.package1.ConfigClass.prototype.constructor.call(this,config_$1);}/*

    private var blub:*;*/function static$0(){

    {
      if(1 < 0 && 0 > 1) {
        throw "plain wrong!";
      }
    }}/*

    [Bindable]
    public var list:Object;

[Bindable]
public var bar:String;
[Bindable]
public var computed:String;
/**
     Some number.
    * /
    [Bindable]
    public var num:int;
[Bindable]
public var empty:int;
[Bindable]
public var otherByExpression:Object;
[Bindable]
public var other:package1.someOtherPackage.SomeOtherClass;
private*/ function $on_config_$1_click (event/*:package1.someOtherPackage.SomeEvent*/)/* :void*/ {

    var result/*:String*/ = 'gotcha!';}/*
private*/ function $on_other$ns_SomeOtherClass_47_5_$1_clack (event/*:package1.someOtherPackage.SomeEvent*/)/* :void*/ {

    var test=0;}/*

        [Bindable]
        public var no_config:package1.someOtherPackage.SomeOtherClass;}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "AS3.package1.ConfigClass",
      mixins: ["AS3.package1.Interface"],
      alias: "widget.testNamespace.config.simpleMxmlClass",
      constructor: SimpleMxmlClass$,
      blub$2: undefined,
      $on_config_$1_click$2: $on_config_$1_click,
      $on_other$ns_SomeOtherClass_47_5_$1_clack$2: $on_other$ns_SomeOtherClass_47_5_$1_clack,
      config: {
        list: null,
        bar: null,
        computed: null,
        num: 0,
        empty: 0,
        otherByExpression: null,
        other: null,
        no_config: null
      },
      statics: {__initStatics__: function() {
          static$0();
        }},
      requires: [
        "AS3.package1.ConfigClass",
        "AS3.package1.Interface"
      ],
      uses: [
        "AS3.net.jangaroo.ext.Exml",
        "AS3.package1.someOtherPackage.SomeEvent",
        "AS3.package1.someOtherPackage.SomeOtherClass"
      ]
    };
});
