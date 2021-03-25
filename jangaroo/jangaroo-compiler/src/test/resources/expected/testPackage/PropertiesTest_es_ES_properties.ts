import PropertiesTest_properties from './PropertiesTest_properties';
import MoreIcons_properties from './icons/MoreIcons_properties';

/**
 * Overrides of ResourceBundle "PropertiesTest" for Locale "es_ES".
 * @see PropertiesTest_properties#INSTANCE
 */
ResourceBundleUtil.override(PropertiesTest_properties, {
 key: "Hasta la vista",
 "keyWith\"+\"quotes": MoreIcons_properties.someOtherKey
});
