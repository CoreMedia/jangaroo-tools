Ext.define("AS3.package1.mxml.SimpleMxmlClass", function(SimpleMxmlClass) {/*package package1.mxml{
import package1.*;
import package1.someOtherPackage.*;
import net.jangaroo.ext.Exml;
import joo.addEventListener;
/**
  My config class subclass, authored in MXML.
* /

class SimpleMxmlClass extends ConfigClass implements package1.Interface{

    public static const xtype:String = "testNamespace.config.simpleMxmlClass";*/

    function SimpleMxmlClass$(config/*:SimpleMxmlClass = null*/){if(arguments.length<=0)config=null;
var config_$1/*:SimpleMxmlClass*/ =AS3.cast(SimpleMxmlClass,{});
var defaults_$1/*:SimpleMxmlClass*/ ={};
config= AS3.net.jangaroo.ext.Exml.apply(defaults_$1,config);

    this.blub$2 = {};
    this.blub$2.name = "Kuno";
    var object_31_7_$1/*:Object*/ = {};
    object_31_7_$1.name = "Joe";
    var config_$2/*:package1.ConfigClass*/ =AS3.cast( AS3.package1.ConfigClass,{});
    var config_$3/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    config_$3,"bla" , 123);
    config_$2.items = [new AS3.package1.someOtherPackage.SomeOtherClass(config_$3)];AS3.setBindable(
    defaults_$1,"list" , [object_31_7_$1, new AS3.package1.ConfigClass(config_$2)]);
    var config_$4/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    config_$4,"bla" , 3);
    config_$4["blubb_config"] = 'blub config expression';
    config_$4["blubb_accessor"] = 'blub accessor expression';AS3.setBindable(
    defaults_$1,"other" , new AS3.package1.someOtherPackage.SomeOtherClass(config_$4));
    config_$1.foo = "bar";
    config_$1.number = 1 + 1;
    joo.addEventListener(config_$1, "click",AS3.bind( this,"$on_config_$1_click$2"), AS3.package1.someOtherPackage.SomeEvent);
    var config_$5/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    config_$5,"bla" , 99);
    config_$5["knownUnknown"] = true;
    config_$1.defaultType = config_$5['xtype'];
    delete config_$5['xtype'];
    delete config_$5['xclass'];
    config_$1.defaults = config_$5;
    var config_$6/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    config_$6,"bla" , 23);
    var config_$7/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});
    joo.addEventListener(config_$7, "clack",AS3.bind( this,"$on_config_$7_clack$2"), AS3.package1.someOtherPackage.SomeEvent);AS3.setBindable(
    config_$7,"bla" , 1);
    var config_$8/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    config_$8,"bla" , 42);
    config_$8.number = 24;
    var config_$9/*:package1.ConfigClass*/ =AS3.cast( AS3.package1.ConfigClass,{});
    var config_$10/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});
    config_$10.doodle = "non-bound";AS3.setBindable(
    config_$10,"bla" ,AS3.getBindable(AS3.getBindable( this,"other"),"bla","bla_has_changed"));
    config_$9.items = [new AS3.package1.someOtherPackage.SomeOtherClass(config_$10)];
    config_$9.number = 12;
    var config_$11/*:package1.ConfigClass*/ =AS3.cast( AS3.package1.ConfigClass,{});
    var config_$12/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    config_$12,"bla" , 12);
    var config_$13/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    config_$13,"bla" , 13);AS3.setBindable(
    this,"no_config" , new AS3.package1.someOtherPackage.SomeOtherClass(config_$13));
    config_$11.items = [new AS3.package1.someOtherPackage.SomeOtherClass(config_$12),AS3.getBindable( this,"no_config")];
    config_$1.items = [new AS3.package1.someOtherPackage.SomeOtherClass(config_$6), new AS3.package1.someOtherPackage.SomeOtherClass(config_$7), new AS3.package1.someOtherPackage.SomeOtherClass(config_$8), new AS3.package1.ConfigClass(config_$9), new AS3.package1.ConfigClass(config_$11)]; AS3.net.jangaroo.ext.Exml.apply(config_$1,config);AS3.package1.ConfigClass.prototype.constructor.call(this,config_$1,config);}/*

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
private*/ function $on_config_$7_clack (event/*:package1.someOtherPackage.SomeEvent*/)/* :void*/ {

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
      $on_config_$7_clack$2: $on_config_$7_clack,
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
