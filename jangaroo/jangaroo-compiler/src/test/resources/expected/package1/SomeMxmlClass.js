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

  private*/ function $on_$_1_click(event/*:package1.someOtherPackage.SomeEvent*/)/*:void*/ {
    var result/*:String*/ = 'gotcha!';
  }/*

  private*/ function $on_$_5_clack(event/*:package1.someOtherPackage.SomeEvent*/)/*:void*/ {
    var test=0;
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
    this.other = new AS3.package1.someOtherPackage.SomeOtherClass();
    this.other.set_bla ( 3);
    this.other.blubbConfig = 'blub config expression';
    joo.addEventListener($_1, "click",AS3.bind( this,"$on_$_1_click$2"), AS3.package1.someOtherPackage.SomeEvent);
    $_1.foo = "bar";
    $_1.number = 1 + 1;
    var $_4/*:package1.someOtherPackage.SomeOtherClass*/ = new AS3.package1.someOtherPackage.SomeOtherClass();
    $_4.set_bla ( 23);
    var $_5/*:package1.someOtherPackage.SomeOtherClass*/ = new AS3.package1.someOtherPackage.SomeOtherClass();
    $_5.set_bla ( 1);
    joo.addEventListener($_5, "clack",AS3.bind( this,"$on_$_5_clack$2"), AS3.package1.someOtherPackage.SomeEvent);
    var $_6/*:package1.someOtherPackage.SomeOtherClass*/ = new AS3.package1.someOtherPackage.SomeOtherClass();
    $_6.set_bla ( 42);
    var $_7/*:package1.ConfigClass*/ =AS3.cast( AS3.package1.ConfigClass,{});
    var $_9/*:package1.someOtherPackage.SomeOtherClass*/ = new AS3.package1.someOtherPackage.SomeOtherClass();
    $_9.doodle = "non-bound";
    $_7.items = [$_9];
    $_7.number = 12;
    var $_10/*:package1.ConfigClass*/ =AS3.cast( AS3.package1.ConfigClass,{});
    var $_12/*:package1.someOtherPackage.SomeOtherClass*/ = new AS3.package1.someOtherPackage.SomeOtherClass();
    $_12.set_bla ( 12);
    this.no_config = new AS3.package1.someOtherPackage.SomeOtherClass();
    this.no_config.set_bla ( 13);
    $_10.items = [$_12, this.no_config];
    $_1.items = [$_4, $_5, $_6, $_7, $_10];
    AS3.net.jangaroo.ext.Exml.apply($_1, config);
    this.callParent([$_1]);
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "AS3.package1.ConfigClass",
      $on_$_1_click$2: $on_$_1_click,
      $on_$_5_clack$2: $on_$_5_clack,
      constructor: SomeMxmlClass$,
      requires: ["AS3.package1.ConfigClass"],
      uses: [
        "AS3.package1.someOtherPackage.SomeOtherClass",
        "AS3.package1.someOtherPackage.SomeEvent",
        "AS3.net.jangaroo.ext.Exml"
      ]
    };
});
