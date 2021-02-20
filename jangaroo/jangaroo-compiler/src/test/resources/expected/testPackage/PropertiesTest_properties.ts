import Icons_properties from './Icons_properties';
import MoreIcons_properties from './icons/MoreIcons_properties';

/**
 * some comment
*/  /*
 * @see PropertiesTest_properties#INSTANCE
 */
 interface PropertiesTest_properties {

/**
 * Documentation for 'key'.
 */
 key: string;
/**
 * Documentation for 'key2'.
 */
 key2: string;
 key3: string;

}

/**
 * Singleton for the current user Locale's instance of ResourceBundle "PropertiesTest".
 * @see PropertiesTest_properties
 */
const PropertiesTest_properties: PropertiesTest_properties = {
  key: "The disk \"{1}\" contains {0}.",
  key2: Icons_properties.someKey,
  key3: MoreIcons_properties.someOtherKey,
  "keyWith\"+\"quotes": "gotcha!",

/*
 * Documentation for 'keyWith"+"quotesAndReference'.
 */
  "keyWith\"+\"quotesAndReference": MoreIcons_properties.someOtherKey
};

export default PropertiesTest_properties;
