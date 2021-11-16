
/**
 * Overrides of ResourceBundle "PropertiesTest" for Locale "es_ES".
 * @see PropertiesTest_properties#INSTANCE
 */
Ext.define("test.package2.PropertiesTest_es_ES_properties", {
  override: "test.package2.PropertiesTest_properties",
  requires: ["testPackage.icons.MoreIcons_properties"],
 key: "Hasta la vista"
}, function() {
  Ext.apply(this.prototype, {
 "keyWith\"+\"quotes": testPackage.icons.MoreIcons_properties.INSTANCE.someOtherKey
  });
});