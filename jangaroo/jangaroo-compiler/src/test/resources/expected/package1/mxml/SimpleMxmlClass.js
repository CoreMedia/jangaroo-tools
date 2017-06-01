Ext.define("package1.mxml.SimpleMxmlClass", function(SimpleMxmlClass) {/*package package1.mxml{
import package1.*;
import package1.someOtherPackage.*;
import net.jangaroo.ext.Exml;
/**
  My config class subclass, authored in MXML.
 * /

public class SimpleMxmlClass extends ConfigClass{

    public static const xtype:String = "testNamespace.config.simpleMxmlClass";

    public*/function SimpleMxmlClass$(config/*:SimpleMxmlClass = null*/){if(arguments.length<=0)config=null;config = net.jangaroo.ext.Exml.apply(
  {
    bar: "FOO & BAR!",
    computed: 'B' + 'AR!',
    /**
     Some number.
     */
    num: 123,
    someFlag2: false,
    anotherFlag2: true,
    someFlag3: false,
    anotherFlag3: true,
    blub:
    { name: "Kuno"},
    joe: { name: "Joe" },
    list:[
      { name: "Joe"},
      new package1.ConfigClass({items:[
        new package1.someOtherPackage.SomeOtherClass({ bla: 123})
      ]})
    ],
    otherByExpression: { foo: 'bar'},
    other:
    new package1.someOtherPackage.SomeOtherClass({ bla: 3,
                          blubb_config: 'blub config expression',
                          blubb_accessor: 'blub accessor expression'})
  },config);config = net.jangaroo.ext.Exml.apply(AS3.cast(SimpleMxmlClass,{
             foo: "bar",
             number: 1 < 2  ? 1 + 1 : 3,listeners:{
             click: {scope:this,fn:function()/*:**/ {arguments=Array.prototype.slice.call(arguments);var event/*:package1.someOtherPackage.SomeEvent*/ =new package1.someOtherPackage.SomeEvent("onClick",arguments);var result/*:String*/ = 'gotcha!';}}},
  defaultType: package1.someOtherPackage.SomeOtherClass['prototype'].xtype,
  defaults:
    { bla: 99,
                          "known-unknown": true},
  items$at: net.jangaroo.ext.Exml.APPEND,
  items:[
    new package1.someOtherPackage.SomeOtherClass({ bla: 23}),
    new package1.someOtherPackage.SomeOtherClass({ bla: 1,listeners:{
      clickclack: {scope:this,fn:function()/*:**/ {arguments=Array.prototype.slice.call(arguments);var event/*:package1.someOtherPackage.SomeEvent*/ =new package1.someOtherPackage.SomeEvent("clickClack",arguments);
        var test=0; 
      }}}
    }),
    new package1.someOtherPackage.SomeOtherClass({ bla: 42, number: 24
    }),
    new package1.ConfigClass({
      items:[
        new package1.someOtherPackage.SomeOtherClass({ doodle: "non-bound", bla: AS3.getBindable(AS3.getBindable(this,"other"),"bla","bla_has_changed")})
      ],
      number: 12
    }),
    new package1.ConfigClass({
      items$at: net.jangaroo.ext.Exml.APPEND,
      items:[
        new package1.someOtherPackage.SomeOtherClass({ bla: 12}),
        AS3.setBindable(this,"no_config",new package1.someOtherPackage.SomeOtherClass({ bla: 13}))
      ]
    })
  ]}),config);
    package1.ConfigClass.prototype.constructor.call(this,config);
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

        [Bindable]
        public var no_config:package1.someOtherPackage.SomeOtherClass;}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.ConfigClass",
      alias: "widget.testNamespace.config.simpleMxmlClass",
      constructor: SimpleMxmlClass$,
      blub$3: undefined,
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
