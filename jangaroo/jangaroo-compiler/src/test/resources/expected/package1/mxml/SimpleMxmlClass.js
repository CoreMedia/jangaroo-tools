/*package package1.mxml{
import package1.*;
import package1.someOtherPackage.*;
import net.jangaroo.ext.Exml;*/
/**
  My config class subclass, authored in MXML.
 */

Ext.define("package1.mxml.SimpleMxmlClass", function(SimpleMxmlClass) {/*public class SimpleMxmlClass extends ConfigClass{

    public static const xtype:String = "testNamespace.config.simpleMxmlClass";

    public*/function SimpleMxmlClass$(config/*:SimpleMxmlClass = null*/){if(arguments.length<=0)config=null;
    this.blub$5_bR ={ name: "Kuno"};
    config = net.jangaroo.ext.Exml.apply({
    bar: "FOO & BAR!",
    computed:net.jangaroo.ext.Exml.asString( 'B' + 'AR!'),
    /**
     Some number.
     */
    num: 123,
    someFlag2: false,
    anotherFlag2: true,
    someFlag3: false,
    anotherFlag3: true,
    joe: { name: "Joe"},
    list: [
      { name: "Joe"},
      new package1.ConfigClass({items:[
        new package1.someOtherPackage.SomeOtherClass({ bla: 123})
      ]})
    ],
    otherByExpression: { foo: 'bar'},
    other: new package1.someOtherPackage.SomeOtherClass({ bla: 3,
                          blubb_config: 'blub config expression',
                          blubb_accessor: 'blub accessor expression'})
    },config);
    this.super$5_bR( net.jangaroo.ext.Exml.apply(AS3.cast(SimpleMxmlClass,{
             foo: "bar",
             number: 1 < 2  ? 1 + 1 : 3,
  defaultType:
    package1.someOtherPackage.SomeOtherClass.xtype,
  defaults:{ bla: 99,
                          "known-unknown": true
  },items$at: net.jangaroo.ext.Exml.APPEND, 
  items:([
    new package1.someOtherPackage.SomeOtherClass({ bla: 23}),
    new package1.someOtherPackage.SomeOtherClass({ bla: 1,
    listeners:{ clickClack: net.jangaroo.ext.Exml.eventHandler( package1.someOtherPackage.SomeEvent.CLICK_CLACK,package1.someOtherPackage.SomeEvent,AS3.bind(this,"$on_clickClack_55_41$5_bR"))}}),
    new package1.someOtherPackage.SomeOtherClass({ bla: 42, number: 24
    }),
    new package1.ConfigClass({
      items:[
        new package1.someOtherPackage.SomeOtherClass({ doodle: "non-bound", bla: this.other.bla})
      ],
      number: 12
    }),
    new package1.ConfigClass({items$at: net.jangaroo.ext.Exml.PREPEND, 
      items:([
        new package1.someOtherPackage.SomeOtherClass({ bla: 12}),
        this.no_config = new package1.someOtherPackage.SomeOtherClass({ bla: 13})
      ])
    })
  ]),
    listeners:{
             click: net.jangaroo.ext.Exml.eventHandler( package1.someOtherPackage.SomeEvent.CLICK,package1.someOtherPackage.SomeEvent,AS3.bind(this,"$on_click_14_20$5_bR"))}
}),config));
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
      super$5_bR: function() {
        package1.ConfigClass.prototype.constructor.apply(this, arguments);
      },
      blub$5_bR: undefined,
      $on_click_14_20$5_bR: $on_click_14_20,
      $on_clickClack_55_41$5_bR: $on_clickClack_55_41,
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
