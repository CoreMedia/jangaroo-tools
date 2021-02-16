import MoreIcons_properties from "./MoreIcons_properties";
import Icons_properties from "./Icons_properties";

/**
 * some comment
 * [PublicApi]
 */
interface PropertiesTest_properties {
  key: string;
  key2: string;
  key3: string;
  "keyWith\"+\"quotes": string;
  "keyWith\"+\"quotesAndReference": string;
}

const PropertiesTest_properties: PropertiesTest_properties = {
  key: "The disk \"{1}\" contains {0}.",
  key2: Icons_properties.someKey,
  key3: MoreIcons_properties.someOtherKey,
  "keyWith\"+\"quotes": "gotcha!",
  "keyWith\"+\"quotesAndReference": MoreIcons_properties.someOtherKey,
};

export default PropertiesTest_properties;
