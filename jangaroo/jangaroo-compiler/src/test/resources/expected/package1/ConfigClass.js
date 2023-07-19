/*package package1{
import ext.mixin.Observable;

/**
 * click event documentation.
 * @eventType package1.someOtherPackage.ConfigClass_stranger_antonEvent.CLICK
 * /
[Event(name="onClick", type="package1.someOtherPackage.ConfigClass_stranger_antonEvent")]*/

Ext.define("package1.ConfigClass", function(ConfigClass) {/*public class ConfigClass extends Observable {

  public*/ function ConfigClass$(config/*:ConfigClass = null*/) {if(arguments.length<=0)config=null;this.super$klfp();
  }/*

  public var foo:String = "foo",
          number:int;

  public native function get items():Array;

  [DefaultProperty]
  [ExtConfig(create)]
  public native function set items(value:Array):void;

  public native function get defaultType():String;

  public native function set defaultType(value:String):void;

  public native function get defaults():*;

  [ExtConfig(extractXType="defaultType")]
  public native function set defaults(value:*):void;

  private var _title:String = "- empty -";

  [Bindable]
  public*/ function get$title()/*:String*/ {
    return this._title$klfp;
  }/*

  [Bindable]
  public*/ function set$title(value/*:String*/)/*:void*/ {
    this._title$klfp = value;
  }/*

  // Using [Bindable] on a normal method does not make sense,
  // but should be ignored:
  [Bindable]
  public*/ function bogus(value/*:String*/)/*:void*/ {}/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "Ext.mixin.Observable",
      metadata: {items: ["DefaultProperty"]},
      constructor: ConfigClass$,
      super$klfp: function() {
        Ext.mixin.Observable.prototype.constructor.apply(this, arguments);
      },
      foo: "foo",
      number: 0,
      _title$klfp: "- empty -",
      getTitle: get$title,
      setTitle: set$title,
      bogus: bogus,
      config: {title: undefined},
      requires: ["Ext.mixin.Observable"]
    };
});
