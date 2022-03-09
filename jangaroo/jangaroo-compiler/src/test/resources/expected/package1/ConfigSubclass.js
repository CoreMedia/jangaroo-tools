/*package package1 {*/

Ext.define("package1.ConfigSubclass", function(ConfigSubclass) {/*public class ConfigSubclass extends ConfigClass {

  override public*/ function  get$defaultType()/*:String*/ {
    return package1.ConfigClass.prototype.defaultType + "!";
  }/*

  [Bindable]
  override public*/ function set$title(value/*:String*/)/*:void*/ {
    package1.ConfigClass.prototype.title = value + "!";
  }/*
}*/function ConfigSubclass$() {this.super$svnT();}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.ConfigClass",
      setTitle: set$title,
      super$svnT: function() {
        package1.ConfigClass.prototype.constructor.apply(this, arguments);
      },
      constructor: ConfigSubclass$,
      config: {title: undefined},
      __accessors__: {defaultType: {
        get: get$defaultType,
        set: super$.__lookupSetter__('defaultType')
      }},
      requires: ["package1.ConfigClass"]
    };
});
