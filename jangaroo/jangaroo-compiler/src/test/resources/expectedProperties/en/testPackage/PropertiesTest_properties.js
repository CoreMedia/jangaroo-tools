/**
 * some comment
 * [PublicApi]
 */
Ext.define("testPackage.PropertiesTest_properties", {

  requires: [
    "net.jangaroo.icons.SomeBundle_properties",
    "net.jangaroo.SomeOtherBundle_properties"
  ],
  "key": "The disk \"{1}\" contains {0}.",
  "keyWith\"+\"quotes": "gotcha!"
}, function() {
  this.prototype["key2"] =  net.jangaroo.icons.SomeBundle_properties.INSTANCE.someKey;
  this.prototype["key3"] =  net.jangaroo.SomeOtherBundle_properties.INSTANCE.someOtherKey;
  this.prototype["keyWith\"+\"quotesAndReference"] =  net.jangaroo.SomeOtherBundle_properties.INSTANCE.someOtherKey;

  testPackage.PropertiesTest_properties.INSTANCE = new testPackage.PropertiesTest_properties();
});