Ext.define("AS3.package1.SomeMxmlClass", function(SomeMxmlClass) {/*package package1 {
import package1.someOtherPackage.SomeOtherClass;
import package1.someOtherPackage.SomeEvent;
import joo.addEventListener;
import net.jangaroo.ext.Exml;

public class SomeMxmlClass extends package1.ConfigClass {
    import foo.Bar;
  
  [Bindable]
  public var bar:String;

  [Bindable]
  public var num:int;

  [Bindable]
  public var blub:Object;

  [Bindable]
  public var list:Array;

  [Bindable]
  public var other:package1.someOtherPackage.SomeOtherClass;

  private*/ function $bind_other_blubbAccessor()/*:String*/ {
    return 'blub accessor expression';
  }/*

  private*/ function $on_$_1_click(event/*:package1.someOtherPackage.SomeEvent*/)/*:void*/ {
    var result/*:String*/ = 'gotcha!';
  }/*

  private*/ function $on_$_7_clack(event/*:package1.someOtherPackage.SomeEvent*/)/*:void*/ {
    var test=0;
  }/*

  private*/ function $bind_$_14_bla()/*:int*/ {
    return AS3.getBindable(AS3.getBindable( this,"other"),"bla","bla_has_changed");
  }/*

  [Bindable]
  public var no_config:package1.someOtherPackage.SomeOtherClass;

  public*/ function SomeMxmlClass$(config/*:package1.SomeMxmlClass = null*/) {if(arguments.length<=0)config=null;
    
    var $_1/*:package1.ConfigClass*/ =AS3.cast( AS3.package1.ConfigClass,{});AS3.setBindable(
    this,"bar" , "BAR!");AS3.setBindable(
    this,"num" , 123);AS3.setBindable(
    this,"blub" , {});AS3.getBindable(
    this,"blub").name = "Kuno";
    var $_2/*:Object*/ = {};
    $_2.name = "Joe";
    var $_3/*:Object*/ = {};
    $_3.name = "Felix";AS3.setBindable(
    this,"list" , [$_2, $_3]);
    var $_4/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    $_4,"bla" , 3);
    $_4.blubb_accessor = this.$bind_other_blubbAccessor$2();
    $_4.blubb_config = 'blub config expression';AS3.setBindable(
    this,"other" , new AS3.package1.someOtherPackage.SomeOtherClass($_4));
    joo.addEventListener($_1, "click",AS3.bind( this,"$on_$_1_click$2"), AS3.package1.someOtherPackage.SomeEvent);
    $_1.foo = "bar";
    $_1.number = 1 + 1;
    var $_5/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    $_5,"bla" , 23);
    var $_7/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    $_7,"bla" , 1);
    joo.addEventListener($_7, "clack",AS3.bind( this,"$on_$_7_clack$2"), AS3.package1.someOtherPackage.SomeEvent);
    var $_9/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    $_9,"bla" , 42);
    var $_11/*:package1.ConfigClass*/ =AS3.cast( AS3.package1.ConfigClass,{});
    var $_13/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    $_13,"bla" , this.$bind_$_14_bla$2());
    $_13.doodle = "non-bound";
    var $_14/*:package1.someOtherPackage.SomeOtherClass*/ = new AS3.package1.someOtherPackage.SomeOtherClass($_13);
    $_11.items = [$_14];
    $_11.number = 12;
    var $_15/*:package1.ConfigClass*/ =AS3.cast( AS3.package1.ConfigClass,{});
    var $_17/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    $_17,"bla" , 12);
    var $_19/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});AS3.setBindable(
    $_19,"bla" , 13);AS3.setBindable(
    this,"no_config" , new AS3.package1.someOtherPackage.SomeOtherClass($_19));
    $_15.items = [$_17,AS3.getBindable( this,"no_config")];
    $_1.items = [$_5, $_7, $_9, $_11, $_15];
    AS3.net.jangaroo.ext.Exml.apply($_1, config);
    this.callParent([$_1]);
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "AS3.package1.ConfigClass",
      $bind_other_blubbAccessor$2: $bind_other_blubbAccessor,
      $on_$_1_click$2: $on_$_1_click,
      $on_$_7_clack$2: $on_$_7_clack,
      $bind_$_14_bla$2: $bind_$_14_bla,
      constructor: SomeMxmlClass$,
      config: {
        bar: null,
        num: 0,
        blub: null,
        list: null,
        other: null,
        no_config: null
      },
      requires: ["AS3.package1.ConfigClass"],
      uses: [
        "AS3.package1.someOtherPackage.SomeOtherClass",
        "AS3.package1.someOtherPackage.SomeEvent",
        "AS3.net.jangaroo.ext.Exml"
      ]
    };
});
