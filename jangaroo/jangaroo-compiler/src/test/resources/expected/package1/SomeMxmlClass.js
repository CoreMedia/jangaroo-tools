Ext.define("package1.SomeMxmlClass", function(SomeMxmlClass) {/*package package1 {
import package1.someOtherPackage.SomeOtherClass;
import joo.binding.Binding;
import package1.someOtherPackage.SomeEvent;
import joo.addEventListener;
import mx.resources.ResourceManager;

[ResourceBundle("Foo")]
public class SomeMxmlClass extends package1.ConfigClass {
    import foo.Bar;
  
  public*/ function SomeMxmlClass$() {var this$=this;this.callParent([]);
    
    this.$bindings$2 = [];
    this.bar = "BAR!";
    this.num = 123;
    this.blub = {};
    this.blub.name = "Kuno";
    var $_1/*:Object*/ = {};
    $_1.name = "Joe";
    var $_2/*:Object*/ = {};
    $_2.name = "Felix";
    this.list = [$_1, $_2];
    var $_3/*:Object*/ = {};
    $_3.bla = 3;
    $_3.blubb_accessor = this.$bind_other_blubbAccessor$2();
    $_3.blubb_config = 'blub config expression';
    this.other = new package1.someOtherPackage.SomeOtherClass($_3);
    this.$bindings$2.push(new joo.binding.Binding(AS3.bind(this,"$bind_other_blubbAccessor$2"), function($value){
      this$.other.setBlubbAccessor ( $value);
    }));
    joo.addEventListener(this, "click",AS3.bind( this,"$on_this_click$2"), package1.someOtherPackage.SomeEvent);
    this.foo = "bar";
    this.number = 1 + 1;
    var $_4/*:Object*/ = {};
    $_4.bla = mx.resources.ResourceManager.getInstance().getString("Foo","bar");
    $_4.type = "someOtherType";
    var $_6/*:Object*/ = {};
    $_6.bla = 1;
    joo.addEventListener($_6, "clack",AS3.bind( this,"$on_$_6_clack$2"), package1.someOtherPackage.SomeEvent);
    $_6.type = "someOtherType";
    var $_8/*:Object*/ = {};
    $_8.bla = mx.resources.ResourceManager.getInstance().getString("Foo","baz");
    $_8.type = "someOtherType";
    var $_10/*:package1.ConfigClass*/ = new package1.ConfigClass();
    $_10.number = 12;
    var $_11/*:Object*/ = {};
    $_11.bla = this.$bind_$_12_bla$2();
    $_11.doodle = "non-bound";
    var $_12/*:package1.someOtherPackage.SomeOtherClass*/ = new package1.someOtherPackage.SomeOtherClass($_11);
    this.$bindings$2.push(new joo.binding.Binding(AS3.bind(this,"$bind_$_12_bla$2"), function($value){
      $_12.set_bla ( $value);
    }));
    $_10.items = [$_12];
    var $_13/*:package1.ConfigClass*/ = new package1.ConfigClass();
    var $_14/*:Object*/ = {};
    $_14.bla = 12;
    $_14.type = "someOtherType";
    var $_16/*:Object*/ = {};
    $_16.bla = 13;
    this.no_config = new package1.someOtherPackage.SomeOtherClass($_16);
    $_13.items = [$_14, this.no_config];
    this.items = [$_4, $_6, $_8, $_10, $_13];
    for/* each*/ (var $1=0;$1</* in*/ this.$bindings$2.length;++$1){var $binding= this.$bindings$2[$1]; $binding.execute();}
  }/*

  private var $bindings:Array;

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

  private*/ function $on_this_click(event/*:package1.someOtherPackage.SomeEvent*/)/*:void*/ {
    var result/*:String*/ = 'gotcha!';
  }/*

  private*/ function $on_$_6_clack(event/*:package1.someOtherPackage.SomeEvent*/)/*:void*/ {
    var test=0;
  }/*

  private*/ function $bind_$_12_bla()/*:int*/ {
    return AS3.getBindable( this.other,"get_bla","bla_has_changed");
  }/*

  public native function get no_config():package1.someOtherPackage.SomeOtherClass;

  /**
   * @private
   * /
  public native function set no_config(value:package1.someOtherPackage.SomeOtherClass):void;
}
}

============================================== Jangaroo part ==============================================*/
    return {
      metadata: {"": [
        "ResourceBundle",
        [
          "",
          "Foo"
        ]
      ]},
      extend: "package1.ConfigClass",
      constructor: SomeMxmlClass$,
      $bindings$2: null,
      $bind_other_blubbAccessor$2: $bind_other_blubbAccessor,
      $on_this_click$2: $on_this_click,
      $on_$_6_clack$2: $on_$_6_clack,
      $bind_$_12_bla$2: $bind_$_12_bla,
      requires: [
        "package1.ConfigClass",
        "package1.someOtherPackage.SomeOtherClass",
        "joo.binding.Binding",
        "joo.addEventListener",
        "package1.someOtherPackage.SomeEvent",
        "mx.resources.ResourceManager"
      ]
    };
});
