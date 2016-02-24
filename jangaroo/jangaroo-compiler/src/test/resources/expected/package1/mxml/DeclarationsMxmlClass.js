Ext.define("AS3.package1.mxml.DeclarationsMxmlClass", function(DeclarationsMxmlClass) {/*package package1.mxml{
import package1.*;
import package1.someOtherPackage.*;
public class DeclarationsMxmlClass extends SomeNativeClass{public*/function DeclarationsMxmlClass$(config/*:DeclarationsMxmlClass=null*/){AS3.package1.someOtherPackage.SomeNativeClass.prototype.constructor.call(this);if(arguments.length<=0)config=null;AS3.setBindable(

    this,"bar" , "BAR!");AS3.setBindable(
    this,"num" , 123);AS3.setBindable(
    this,"empty" , null);AS3.setBindable(
    this,"blub" , {});AS3.getBindable(
    this,"blub").name = "Kuno";
    var object_18_7_$1/*:Object*/ = {};
    object_18_7_$1.name = "Joe";
    var config_$1/*:package1.ConfigClass*/ =AS3.cast( AS3.package1.ConfigClass,{});
    var config_$2/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    config_$2,"bla" , 123);
    config_$1.items = [new AS3.package1.someOtherPackage.SomeOtherClass(config_$2)];AS3.setBindable(
    this,"list" , [object_18_7_$1, new AS3.package1.ConfigClass(config_$1)]);
    var config_$3/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    config_$3,"bla" , 3);
    config_$3["blubb_config"] = 'blub config expression';
    config_$3["blubb_accessor"] = 'blub accessor expression';AS3.setBindable(
    this,"other" , new AS3.package1.someOtherPackage.SomeOtherClass(config_$3));
}/*

[Bindable]
public var bar:String;
/**
     Some number.
    * /
    [Bindable]
    public var num:int;
/**
     Empty declaration.
    * /
    [Bindable]
    public var empty:int;

    [Bindable]
    public var blub:Object;

    [Bindable]
    public var list:Array;
[Bindable]
public var other:package1.someOtherPackage.SomeOtherClass;}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "AS3.package1.someOtherPackage.SomeNativeClass",
      constructor: DeclarationsMxmlClass$,
      config: {
        bar: null,
        num: 0,
        empty: 0,
        blub: null,
        list: null,
        other: null
      },
      requires: ["AS3.package1.someOtherPackage.SomeNativeClass"],
      uses: [
        "AS3.package1.ConfigClass",
        "AS3.package1.someOtherPackage.SomeOtherClass"
      ]
    };
});
