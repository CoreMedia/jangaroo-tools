Ext.define("AS3.package1.SomeMxmlClass", function(SomeMxmlClass) {/*package package1 {
import package1.someOtherPackage.SomeOtherClass;
import package1.someOtherPackage.SomeEvent;
import joo.addEventListener;
import net.jangaroo.ext.Exml;

public class SomeMxmlClass extends package1.ConfigClass {
    import foo.Bar;
  
  public native function get bar():String;

  /**
   * @private
   * /
  public native function set bar(value:String):void;

  public native function get num():int;

  /**
   * @private
   * /
  public native function set num(value:int):void;

  public native function get blub():Object;

  /**
   * @private
   * /
  public native function set blub(value:Object):void;

  public native function get list():Array;

  /**
   * @private
   * /
  public native function set list(value:Array):void;

  public native function get other():package1.someOtherPackage.SomeOtherClass;

  /**
   * @private
   * /
  public native function set other(value:package1.someOtherPackage.SomeOtherClass):void;

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
    return AS3.getBindable( this.other,"get_bla","bla_has_changed");
  }/*

  public native function get no_config():package1.someOtherPackage.SomeOtherClass;

  /**
   * @private
   * /
  public native function set no_config(value:package1.someOtherPackage.SomeOtherClass):void;

  public*/ function SomeMxmlClass$(config/*:package1.SomeMxmlClass = null*/) {if(arguments.length<=0)config=null;
    
    var $_1/*:package1.ConfigClass*/ =AS3.cast( AS3.package1.ConfigClass,{});
    this.bar = "BAR!";
    this.num = 123;
    this.blub = {};
    this.blub.name = "Kuno";
    var $_2/*:Object*/ = {};
    $_2.name = "Joe";
    var $_3/*:Object*/ = {};
    $_3.name = "Felix";
    this.list = [$_2, $_3];
    var $_4/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});
    $_4.set_bla ( 3);
    $_4.blubb_accessor = this.$bind_other_blubbAccessor$2();
    $_4.blubb_config = 'blub config expression';
    this.other = new AS3.package1.someOtherPackage.SomeOtherClass($_4);
    joo.addEventListener($_1, "click",AS3.bind( this,"$on_$_1_click$2"), AS3.package1.someOtherPackage.SomeEvent);
    $_1.foo = "bar";
    $_1.number = 1 + 1;
    var $_5/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});
    $_5.set_bla ( 23);
    $_5.type = "someOtherType";
    var $_7/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});
    $_7.set_bla ( 1);
    joo.addEventListener($_7, "clack",AS3.bind( this,"$on_$_7_clack$2"), AS3.package1.someOtherPackage.SomeEvent);
    $_7.type = "someOtherType";
    var $_9/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});
    $_9.set_bla ( 42);
    $_9.type = "someOtherType";
    var $_11/*:package1.ConfigClass*/ =AS3.cast( AS3.package1.ConfigClass,{});
    var $_13/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});
    $_13.set_bla ( this.$bind_$_14_bla$2());
    $_13.doodle = "non-bound";
    var $_14/*:package1.someOtherPackage.SomeOtherClass*/ = new AS3.package1.someOtherPackage.SomeOtherClass($_13);
    $_11.items = [$_14];
    $_11.number = 12;
    var $_15/*:package1.ConfigClass*/ =AS3.cast( AS3.package1.ConfigClass,{});
    var $_17/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});
    $_17.set_bla ( 12);
    $_17.type = "someOtherType";
    var $_19/*:package1.someOtherPackage.SomeOtherClass*/ =AS3.cast( AS3.package1.someOtherPackage.SomeOtherClass,{});
    $_19.set_bla ( 13);
    this.no_config = new AS3.package1.someOtherPackage.SomeOtherClass($_19);
    $_15.items = [$_17, this.no_config];
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
      requires: ["AS3.package1.ConfigClass"],
      uses: [
        "AS3.package1.someOtherPackage.SomeOtherClass",
        "AS3.package1.someOtherPackage.SomeEvent",
        "AS3.net.jangaroo.ext.Exml"
      ]
    };
});
