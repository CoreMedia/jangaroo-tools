Ext.define("package1.mxml.pkg.PropertiesAccessBase", function(PropertiesAccessBase) {/*package package1.mxml.pkg {

public class PropertiesAccessBase {

  public*/ function PropertiesAccessBase$(config/*:PropertiesAccess = null*/) {if(arguments.length<=0)config=null;
    this.property_1 = config.property_1 += "_HI";
    this.property_2 = 123;
    this.property_3 = config.property_3 || "";
  }/*

  public var property_1:String;
  private var _property_2:String;

  public*/ function  set$property_2(value/*:**/)/*:void*/ {
    this._property_2$tL7B = String(value);
  }/*

  public*/ function  get$property_2()/*:String*/ {
    return this._property_2$tL7B;
  }/*

  public native function set property_3(value:String):void;

  public native function get property_3():String;
}
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: PropertiesAccessBase$,
      property_1: null,
      _property_2$tL7B: null,
      __accessors__: {property_2: {
        get: get$property_2,
        set: set$property_2
      }}
    };
});
