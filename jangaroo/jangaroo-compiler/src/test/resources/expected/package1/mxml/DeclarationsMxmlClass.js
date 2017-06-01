Ext.define("package1.mxml.DeclarationsMxmlClass", function(DeclarationsMxmlClass) {/*package package1.mxml{
import package1.*;
import package1.someOtherPackage.*;
import net.jangaroo.ext.Exml;
public class DeclarationsMxmlClass extends SomeNativeClass{

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
    public var other:package1.someOtherPackage.SomeOtherClass;public*/function DeclarationsMxmlClass$(config/*:DeclarationsMxmlClass=null*/){package1.someOtherPackage.SomeNativeClass.prototype.constructor.call(this);if(arguments.length<=0)config=null; net.jangaroo.ext.Exml.apply(this,
  {
    bar: "BAR!",
    /**
     Some number.
     */
    num: 123,
    blub:
    { name: "Kuno"},
    list:[
      { name: "Joe"},
      new package1.ConfigClass({
        items:[
          new package1.someOtherPackage.SomeOtherClass({ bla: 123})
        ]
      })
    ],
    other:
    new package1.someOtherPackage.SomeOtherClass({ bla: 3,
                        blubb_config: 'blub config expression',
                        blubb_accessor: 'blub accessor expression'})
  });
}/*}}

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
        "net.jangaroo.ext.Exml",
        "package1.ConfigClass",
        "package1.someOtherPackage.SomeOtherClass"
      ]
    };
});
