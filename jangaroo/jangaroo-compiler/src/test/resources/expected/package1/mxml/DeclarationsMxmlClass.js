/*package package1.mxml{
import package1.*;
import package1.someOtherPackage.*;
import net.jangaroo.ext.Exml;*/
Ext.define("package1.mxml.DeclarationsMxmlClass", function(DeclarationsMxmlClass) {/*public class DeclarationsMxmlClass extends SomeNativeClass{public*/function DeclarationsMxmlClass$(config/*:DeclarationsMxmlClass=null*/){if(arguments.length<=0)config=null;this.super$GkLq();
    AS3.setBindable(this,"bar" , "BAR!");
    AS3.setBindable(this,"num" , 123);
    AS3.setBindable(this,"blub" ,{
    name: "Kuno"
    });
    AS3.setBindable(this,"list" ,[{
    name: "Joe"
    }, new package1.ConfigClass(AS3.cast(package1.ConfigClass,{
    items: [new package1.someOtherPackage.SomeOtherClass(AS3.cast(package1.someOtherPackage.SomeOtherClass,{
    bla: 123
    }))]
    }))]);
    AS3.setBindable(this,"other" , new package1.someOtherPackage.SomeOtherClass(AS3.cast(package1.someOtherPackage.SomeOtherClass,{
    bla: 3,
    blubb_config: 'blub config expression',
    blubb_accessor: 'blub accessor expression'
    })));net.jangaroo.ext.Exml.apply(this,config);
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
      super$GkLq: function() {
        package1.someOtherPackage.SomeNativeClass.prototype.constructor.apply(this, arguments);
      },
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
        "net.jangaroo.ext.Exml",
        "package1.ConfigClass",
        "package1.someOtherPackage.SomeOtherClass"
      ]
    };
});
