Ext.define("package1.mxml.SimpleMxmlClass", function(SimpleMxmlClass) {/*package package1.mxml{
import package1.*;
import package1.someOtherPackage.*;
import net.jangaroo.ext.Exml;
/**
  My config class subclass, authored in MXML.
 * /

public class SimpleMxmlClass extends ConfigClass{

    public static const xtype:String = "testNamespace.config.simpleMxmlClass";

    public*/function SimpleMxmlClass$(config/*:SimpleMxmlClass = null*/){if(arguments.length<=0)config=null;
    var config_$1/*:SimpleMxmlClass*/ =AS3.cast(SimpleMxmlClass,{});
    var defaults_$1/*:SimpleMxmlClass*/ =AS3.cast(SimpleMxmlClass,{});
    AS3.setBindable(defaults_$1,"bar" , "FOO & BAR!");
    AS3.setBindable(defaults_$1,"computed" , 'B' + 'AR!');
    AS3.setBindable(defaults_$1,"num" , 123);
    AS3.setBindable(defaults_$1,"someFlag2" , false);
    AS3.setBindable(defaults_$1,"anotherFlag2" , true);
    AS3.setBindable(defaults_$1,"someFlag3" , false);
    AS3.setBindable(defaults_$1,"anotherFlag3" , true);
    this.blub$3 = {};
    this.blub$3["name"] = "Kuno";
    AS3.setBindable(defaults_$1,"joe" , { name: "Joe" });
    var object_39_7_$1/*:Object*/ = {};
    object_39_7_$1["name"] = "Joe";
    var configClass_40_7_$1/*: package1.ConfigClass*/ =AS3.cast(package1.ConfigClass,{});
    var other$ns_SomeOtherClass_41_9_$1/*: package1.someOtherPackage.SomeOtherClass*/ =AS3.cast(package1.someOtherPackage.SomeOtherClass,{});
    other$ns_SomeOtherClass_41_9_$1["bla"] = 123;
    configClass_40_7_$1["items"] = [new package1.someOtherPackage.SomeOtherClass(other$ns_SomeOtherClass_41_9_$1)];
    AS3.setBindable(defaults_$1,"list" , [object_39_7_$1, new package1.ConfigClass(configClass_40_7_$1)]);
    AS3.setBindable(defaults_$1,"otherByExpression" , { foo: 'bar'});
    var other_45_5_$1/*: package1.someOtherPackage.SomeOtherClass*/ =AS3.cast(package1.someOtherPackage.SomeOtherClass,{});
    other_45_5_$1["bla"] = 3;
    other_45_5_$1["blubb_config"] = 'blub config expression';
    other_45_5_$1["blubb_accessor"] = 'blub accessor expression';
    AS3.setBindable(defaults_$1,"other" , new package1.someOtherPackage.SomeOtherClass(other_45_5_$1));
    config = net.jangaroo.ext.Exml.apply(defaults_$1,config);
    config_$1["foo"] = "bar";
    config_$1["number"] = 1 < 2  ? 1 + 1 : 3;
    AS3.addEventListener(config_$1,package1.someOtherPackage.SomeEvent,"CLICK",AS3.bind( this,"$on_click_14_20$3"));
    var other$ns_SomeOtherClass_50_5_$1/*: package1.someOtherPackage.SomeOtherClass*/ =AS3.cast(package1.someOtherPackage.SomeOtherClass,{});
    other$ns_SomeOtherClass_50_5_$1["bla"] = 99;
    other$ns_SomeOtherClass_50_5_$1["known-unknown"] = true;

    delete other$ns_SomeOtherClass_50_5_$1['xtype'];
    delete other$ns_SomeOtherClass_50_5_$1['xclass'];
    config_$1["defaults"] = other$ns_SomeOtherClass_50_5_$1;
    config_$1["defaultType"] = package1.someOtherPackage.SomeOtherClass['prototype'].xtype;
    var other$ns_SomeOtherClass_54_5_$1/*: package1.someOtherPackage.SomeOtherClass*/ =AS3.cast(package1.someOtherPackage.SomeOtherClass,{});
    other$ns_SomeOtherClass_54_5_$1["bla"] = 23;
    var other$ns_SomeOtherClass_55_5_$1/*: package1.someOtherPackage.SomeOtherClass*/ =AS3.cast(package1.someOtherPackage.SomeOtherClass,{});
    AS3.addEventListener(other$ns_SomeOtherClass_55_5_$1,package1.someOtherPackage.SomeEvent,"CLICK_CLACK",AS3.bind( this,"$on_clickClack_55_41$3"));
    other$ns_SomeOtherClass_55_5_$1["bla"] = 1;
    var other$ns_SomeOtherClass_56_5_$1/*: package1.someOtherPackage.SomeOtherClass*/ =AS3.cast(package1.someOtherPackage.SomeOtherClass,{});
    other$ns_SomeOtherClass_56_5_$1["bla"] = 42;
    other$ns_SomeOtherClass_56_5_$1["number"] = 24;
    var configClass_61_5_$1/*: package1.ConfigClass*/ =AS3.cast(package1.ConfigClass,{});
    var other$ns_SomeOtherClass_63_9_$1/*: package1.someOtherPackage.SomeOtherClass*/ =AS3.cast(package1.someOtherPackage.SomeOtherClass,{});
    other$ns_SomeOtherClass_63_9_$1["doodle"] = "non-bound";
    other$ns_SomeOtherClass_63_9_$1["bla"] = AS3.getBindable(AS3.getBindable(this,"other"),"bla","bla_has_changed");
    configClass_61_5_$1["items"] = [new package1.someOtherPackage.SomeOtherClass(other$ns_SomeOtherClass_63_9_$1)];
    configClass_61_5_$1["number"] = 12;
    var configClass_67_5_$1/*: package1.ConfigClass*/ =AS3.cast(package1.ConfigClass,{});
    var other$ns_SomeOtherClass_69_9_$1/*: package1.someOtherPackage.SomeOtherClass*/ =AS3.cast(package1.someOtherPackage.SomeOtherClass,{});
    other$ns_SomeOtherClass_69_9_$1["bla"] = 12;
    var no_config_70_9_$1/*: package1.someOtherPackage.SomeOtherClass*/ =AS3.cast(package1.someOtherPackage.SomeOtherClass,{});
    no_config_70_9_$1["bla"] = 13;
    AS3.setBindable(this,"no_config" , new package1.someOtherPackage.SomeOtherClass(no_config_70_9_$1));
    configClass_67_5_$1["items"] = [new package1.someOtherPackage.SomeOtherClass(other$ns_SomeOtherClass_69_9_$1), AS3.getBindable(this,"no_config")];
    configClass_67_5_$1["items$at"] = net.jangaroo.ext.Exml.APPEND;
    config_$1["items"] = [new package1.someOtherPackage.SomeOtherClass(other$ns_SomeOtherClass_54_5_$1), new package1.someOtherPackage.SomeOtherClass(other$ns_SomeOtherClass_55_5_$1), new package1.someOtherPackage.SomeOtherClass(other$ns_SomeOtherClass_56_5_$1), new package1.ConfigClass(configClass_61_5_$1), new package1.ConfigClass(configClass_67_5_$1)];
    config_$1["items$at"] = net.jangaroo.ext.Exml.APPEND;
    net.jangaroo.ext.Exml.apply(config_$1,config);
    package1.ConfigClass.prototype.constructor.call(this,config_$1);
  }/*

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
    public var someFlag1:Boolean;

    [Bindable]
    public var anotherFlag1:Boolean;

    [Bindable]
    public var someFlag2:Boolean;

    [Bindable]
    public var anotherFlag2:Boolean;

    [Bindable]
    public var someFlag3:Boolean;

    [Bindable]
    public var anotherFlag3:Boolean;

    [Bindable]
    public var emptyObject:Object;

    [Bindable]
    public var joe:Object;

    [Bindable]
    public var otherByExpression:Object;

    [Bindable]
    public var other:package1.someOtherPackage.SomeOtherClass;
private*/ function $on_click_14_20 (event/*:package1.someOtherPackage.SomeEvent*/)/* :void*/ {

    var result/*:String*/ = 'gotcha!';}/*
private*/ function $on_clickClack_55_41 (event/*:package1.someOtherPackage.SomeEvent*/)/* :void*/ {

    var test=0;}/*

        [Bindable]
        public var no_config:package1.someOtherPackage.SomeOtherClass;}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.ConfigClass",
      alias: "widget.testNamespace.config.simpleMxmlClass",
      constructor: SimpleMxmlClass$,
      blub$3: undefined,
      $on_click_14_20$3: $on_click_14_20,
      $on_clickClack_55_41$3: $on_clickClack_55_41,
      config: {
        list: null,
        bar: null,
        computed: null,
        num: 0,
        empty: 0,
        someFlag1: false,
        anotherFlag1: false,
        someFlag2: false,
        anotherFlag2: false,
        someFlag3: false,
        anotherFlag3: false,
        emptyObject: null,
        joe: null,
        otherByExpression: null,
        other: null,
        no_config: null
      },
      statics: {__initStatics__: function() {
          static$0();
        }},
      requires: ["package1.ConfigClass"],
      uses: [
        "net.jangaroo.ext.Exml",
        "package1.someOtherPackage.SomeEvent",
        "package1.someOtherPackage.SomeOtherClass"
      ]
    };
});
