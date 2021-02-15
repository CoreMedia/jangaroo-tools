/**
 * some comment
 * [PublicApi]
 */
Ext.define("testPackage.PropertiesTest_properties", {

  requires: [
    "testPackage.MoreIcons_properties",
    "testPackage.Icons_properties"
  ],
  "key": "The disk \"{1}\" contains {0}.",
  "keyWith\"+\"quotes": "gotcha!"
}, function() {
  this.prototype["key2"] =  testPackage.Icons_properties.INSTANCE.someKey;
  this.prototype["key3"] =  testPackage.MoreIcons_properties.INSTANCE.someOtherKey;
  this.prototype["keyWith\"+\"quotesAndReference"] =  testPackage.MoreIcons_properties.INSTANCE.someOtherKey;

  testPackage.PropertiesTest_properties.INSTANCE = new testPackage.PropertiesTest_properties();
});