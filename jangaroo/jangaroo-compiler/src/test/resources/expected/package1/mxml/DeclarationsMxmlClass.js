Ext.define("package1.mxml.DeclarationsMxmlClass", function(DeclarationsMxmlClass) {/*package package1.mxml{
import package1.*;
import package1.someOtherPackage.*;
public class DeclarationsMxmlClass extends SomeNativeClass{public*/function DeclarationsMxmlClass$()/*:void*/{package1.someOtherPackage.SomeNativeClass.prototype.constructor.apply(this,arguments);
    AS3.setBindable(this,"bar" , "BAR!");
    AS3.setBindable(this,"num" , 123);
    AS3.setBindable(this,"empty" , null);
    AS3.setBindable(this,"blub" , {});
    AS3.getBindable(this,"blub")["name"] = "Kuno";
    var object_18_7_$1/*:Object*/ = {};
    object_18_7_$1["name"] = "Joe";
    var configClass_19_7_$1/*: package1.ConfigClass*/ =AS3.cast(package1.ConfigClass,{});
    var other_SomeOtherClass_21_11_$1/*: package1.someOtherPackage.SomeOtherClass*/ =AS3.cast(package1.someOtherPackage.SomeOtherClass,{});
    other_SomeOtherClass_21_11_$1["bla"] = 123;
    configClass_19_7_$1["items"] = [new package1.someOtherPackage.SomeOtherClass(other_SomeOtherClass_21_11_$1)];
    AS3.setBindable(this,"list" , [object_18_7_$1, new package1.ConfigClass(configClass_19_7_$1)]);
    var other_25_5_$1/*: package1.someOtherPackage.SomeOtherClass*/ =AS3.cast(package1.someOtherPackage.SomeOtherClass,{});
    other_25_5_$1["bla"] = 3;
    other_25_5_$1["blubb_config"] = 'blub config expression';
    other_25_5_$1["blubb_accessor"] = 'blub accessor expression';
    AS3.setBindable(this,"other" , new package1.someOtherPackage.SomeOtherClass(other_25_5_$1));
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
      extend: "package1.someOtherPackage.SomeNativeClass",
      constructor: DeclarationsMxmlClass$,
      config: {
        bar: null,
        num: 0,
        empty: 0,
        blub: null,
        list: null,
        other: null
      },
      requires: ["package1.someOtherPackage.SomeNativeClass"],
      uses: [
        "package1.ConfigClass",
        "package1.someOtherPackage.SomeOtherClass"
      ]
    };
});
