/*
[PublicApi]
[Rename("test.package2.PropertiesTest_properties")]*/
/**
 * Some class comment in a properties file.
 * @see PropertiesTest_properties#INSTANCE
 */
Ext.define("test.package2.PropertiesTest_properties", {
  alternateClassName: "testPackage.PropertiesTest_properties",
  requires: [
    "testPackage.Icons_properties",
    "testPackage.icons.MoreIcons_properties"
  ],

/**
 * Documentation for 'key'.
 */
key: "The disk \"{1}\" contains {0}.",
  "keyWith\"+\"quotes": "gotcha!"
}, function() {
  Ext.apply(this.prototype, {
/**
 * Documentation for 'key2'.
 */
key2: testPackage.Icons_properties.INSTANCE.someKey,
key3: testPackage.icons.MoreIcons_properties.INSTANCE.someOtherKey,

/*
 * Documentation for 'keyWith"+"quotesAndReference'.
 */
  "keyWith\"+\"quotesAndReference": testPackage.icons.MoreIcons_properties.INSTANCE.someOtherKey
  });
  this.INSTANCE = new this();
});