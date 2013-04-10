define(["exports","as3-rt/AS3","as3/package1/ConfigClass","as3/package1/someOtherPackage/SomeOtherClass","as3/joo/addEventListener","as3/package1/someOtherPackage/SomeEvent","as3/mx/resources/ResourceManager","as3/joo/binding/Binding","bundle!Foo"], function($exports,AS3,ConfigClass,SomeOtherClass,addEventListener,SomeEvent,ResourceManager,Binding) { AS3.compilationUnit($exports, function($primaryDeclaration){/*package package1 {
import package1.someOtherPackage.SomeOtherClass;
import package1.someOtherPackage.SomeEvent;
import joo.addEventListener;
import mx.resources.ResourceManager;
import joo.binding.Binding;

[ResourceBundle("Foo")]
public class SomeMxmlClass extends package1.ConfigClass {
    import foo.Bar;
  
  public*/ function SomeMxmlClass() {Super.call(this);
    
    this.$bindings$2 = [];
    this.bar = "BAR!";
    this.num = 123;
    this.blub = {};
    this.blub.name = "Kuno";
    var $$1/*:Object*/ = {};
    $$1.name = "Joe";
    var $$2/*:Object*/ = {};
    $$2.name = "Felix";
    this.list = [$$1, $$2];
    var $$3/*:Object*/ = {};
    $$3.bla = 3;
    this.other = new (SomeOtherClass._||SomeOtherClass._$get())($$3);
    addEventListener(this, "click",AS3.bind( this,"$on_this_click$2"), (SomeEvent._||SomeEvent._$get()));
    this.foo = "bar";
    this.number = 1 + 1;
    var $$4/*:Object*/ = {};
    $$4.bla = (ResourceManager._||ResourceManager._$get()).getInstance().getString("Foo","bar");
    var $$6/*:Object*/ = {};
    $$6.bla = 1;
    addEventListener($$6, "clack",AS3.bind( this,"$on_$$6_clack$2"), (SomeEvent._||SomeEvent._$get()));
    var $$8/*:Object*/ = {};
    $$8.bla = (ResourceManager._||ResourceManager._$get()).getInstance().getString("Foo","baz");
    var $$10/*:package1.ConfigClass*/ = new (ConfigClass._||ConfigClass._$get())();
    $$10.number = 12;
    var $$11/*:Object*/ = {};
    $$11.bla = this.$bind_$$12_bla$2();
    $$11.doodle = "non-bound";
    var $$12/*:package1.someOtherPackage.SomeOtherClass*/ = new (SomeOtherClass._||SomeOtherClass._$get())($$11);
    this.$bindings$2.push(new Binding(AS3.bind(this,"$bind_$$12_bla$2"), function package1$SomeMxmlClass$43_60($value){
      $$12.set_bla ( $value);
    }));
    $$10.items = [$$12];
    this.items = [new (SomeOtherClass._||SomeOtherClass._$get())($$4), new (SomeOtherClass._||SomeOtherClass._$get())($$6), new (SomeOtherClass._||SomeOtherClass._$get())($$8), $$10];
    for/* each*/ (var $1 in this.$bindings$2){var $binding= this.$bindings$2[$1]; $binding.execute();}
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

  private*/ function $on_this_click(event/*:package1.someOtherPackage.SomeEvent*/)/*:void*/ {
    var result/*:String*/ = 'gotcha!';
  }/*

  private*/ function $on_$$6_clack(event/*:package1.someOtherPackage.SomeEvent*/)/*:void*/ {
    var test=0;
  }/*

  private*/ function $bind_$$12_bla()/*:int*/ {
    return Binding.get( this.other,"get_bla","bla_has_changed");
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    var Super=(ConfigClass._||ConfigClass._$get());
    $primaryDeclaration(AS3.class_({
      package_: "package1",
      metadata: {ResourceBundle: {$value: "Foo"}},
      class_: "SomeMxmlClass",
      extends_: Super,
      members: {
        constructor: SomeMxmlClass,
        $bindings$2: {
          value: null,
          writable: true
        },
        $on_this_click$2: $on_this_click,
        $on_$$6_clack$2: $on_$$6_clack,
        $bind_$$12_bla$2: $bind_$$12_bla
      }
    }));
  });
});
