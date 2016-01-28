Ext.define("AS3.package1.SomeMxmlClass", function(SomeMxmlClass) {/*package package1 {
import package1.someOtherPackage.SomeOtherClass;
import package1.someOtherPackage.SomeEvent;
import joo.addEventListener;
import net.jangaroo.ext.Exml;

/**
 * My config class subclass, authored in MXML.
 * /
public class SomeMxmlClass extends package1.ConfigClass {
    public static const xtype:String = "testNamespace.config.soneMxmlClass";
    
    import foo.Bar;

    private var blub:*;

    [Bindable]
    public var list:Object;
  
  [Bindable]
  public var bar:String;

  [Bindable]
  /**
   * Some number.
   * /
  public var num:int;

  [Bindable]
  public var other:package1.someOtherPackage.SomeOtherClass;

  private*/ function $on_$_1_click(event/*:package1.someOtherPackage.SomeEvent*/)/*:void*/ {
    var result/*:String*/ = 'gotcha!';
  }/*

  private*/ function $on_$_12_clack(event/*:package1.someOtherPackage.SomeEvent*/)/*:void*/ {
    var test=0;
  }/*

  [Bindable]
  public var no_config:package1.someOtherPackage.SomeOtherClass;

  public*/ function SomeMxmlClass$(config/*:package1.SomeMxmlClass = null*/) {if(arguments.length<=0)config=null;
    
    var $_1/*:package1.SomeMxmlClass*/ =AS3.cast( SomeMxmlClass,{});AS3.setBindable(
    $_1,"bar" , "BAR!");AS3.setBindable(
    $_1,"num" , 123);
    this.blub$2 = {};
    this.blub$2.name = "Kuno";
    var $_2/*:Object*/ = {};
    $_2.name = "Joe";
    var $_3/*:package1.ConfigClass*/ =AS3.cast( AS3.package1.ConfigClass,{});
    var $_5/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    $_5,"bla" , 123);
    $_3.items = [new AS3.package1.someOtherPackage.SomeOtherClass($_5)];AS3.setBindable(
    $_1,"list" , [$_2, new AS3.package1.ConfigClass($_3)]);
    var $_7/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    $_7,"bla" , 3);
    $_7.blubb_accessor = 'blub accessor expression';
    $_7.blubb_config = 'blub config expression';AS3.setBindable(
    $_1,"other" , new AS3.package1.someOtherPackage.SomeOtherClass($_7));
    joo.addEventListener($_1, "click",AS3.bind( this,"$on_$_1_click$2"), AS3.package1.someOtherPackage.SomeEvent);
    $_1.foo = "bar";
    $_1.number = 1 + 1;
    var $_8/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    $_8,"bla" , 99);
    $_1.defaultType = $_8['xtype'];
    delete $_8['xtype'];
    delete $_8['xclass'];
    $_1.defaults = $_8;
    var $_10/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    $_10,"bla" , 23);
    var $_12/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    $_12,"bla" , 1);
    joo.addEventListener($_12, "clack",AS3.bind( this,"$on_$_12_clack$2"), AS3.package1.someOtherPackage.SomeEvent);
    var $_14/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    $_14,"bla" , 42);
    $_14.number = 24;
    var $_16/*:package1.ConfigClass*/ =AS3.cast( AS3.package1.ConfigClass,{});
    var $_18/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    $_18,"bla" ,AS3.getBindable(AS3.getBindable( this,"other"),"bla","bla_has_changed"));
    $_18.doodle = "non-bound";
    $_16.items = [new AS3.package1.someOtherPackage.SomeOtherClass($_18)];
    $_16.number = 12;
    var $_20/*:package1.ConfigClass*/ =AS3.cast( AS3.package1.ConfigClass,{});
    var $_22/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    $_22,"bla" , 12);
    var $_24/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    $_24,"bla" , 13);AS3.setBindable(
    this,"no_config" , new AS3.package1.someOtherPackage.SomeOtherClass($_24));
    $_20.items = [new AS3.package1.someOtherPackage.SomeOtherClass($_22),AS3.getBindable( this,"no_config")];
    $_1.items = [new AS3.package1.someOtherPackage.SomeOtherClass($_10), new AS3.package1.someOtherPackage.SomeOtherClass($_12), new AS3.package1.someOtherPackage.SomeOtherClass($_14), new AS3.package1.ConfigClass($_16), new AS3.package1.ConfigClass($_20)];
    $_1.items$at = AS3.net.jangaroo.ext.Exml.APPEND;
    AS3.net.jangaroo.ext.Exml.apply($_1, config);
    AS3.package1.ConfigClass.prototype.constructor.call(this,$_1);
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "AS3.package1.ConfigClass",
      alias: "widget.testNamespace.config.soneMxmlClass",
      blub$2: undefined,
      $on_$_1_click$2: $on_$_1_click,
      $on_$_12_clack$2: $on_$_12_clack,
      constructor: SomeMxmlClass$,
      config: {
        list: null,
        bar: null,
        num: 0,
        other: null,
        no_config: null
      },
      requires: ["AS3.package1.ConfigClass"],
      uses: [
        "AS3.net.jangaroo.ext.Exml",
        "AS3.package1.someOtherPackage.SomeEvent",
        "AS3.package1.someOtherPackage.SomeOtherClass"
      ]
    };
});
