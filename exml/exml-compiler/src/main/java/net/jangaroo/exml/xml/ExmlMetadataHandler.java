package net.jangaroo.exml.xml;

import net.jangaroo.exml.model.ConfigAttribute;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.utils.CharacterRecordingHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Generates an internal representation of all metadata of the component described by the given EXML.
 */
public class ExmlMetadataHandler extends CharacterRecordingHandler {

  public static final String EXML_NAMESPACE_URI = "http://net.jangaroo.com/extxml/0.1";
  public static final String EXT_NAMESPACE_URI = "http://extjs.com/ext3";

  private ConfigClass configClass;
  private boolean expectsOptionalConfigDescription = false;
  private boolean expectsOptionalComponentDescription = false;

  public ExmlMetadataHandler(ConfigClass configClass) {
    this.configClass = configClass;
  }

  public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
    if (EXML_NAMESPACE_URI.equals(uri)) {
      if ("component".equals(localName)) {
        //prepare characterStack for optional component description
        expectsOptionalComponentDescription = true;
      } else if ("cfg".equals(localName)) {
        //handle config elements
        configClass.addCfg(new ConfigAttribute(atts.getValue("name"), atts.getValue("type")));
        expectsOptionalConfigDescription = true;
      } else if ("description".equals(localName)) {
        if (expectsOptionalConfigDescription || expectsOptionalComponentDescription) {
          // start recording characters of the description:
          startRecordingCharacters();
        }
      }
    } else if (configClass.getSuperClassName() == null && configClass.getSuperClassPackage() == null) {
      configClass.setSuperClassName(localName);
      if(EXT_NAMESPACE_URI.equals(uri)) {
        configClass.setSuperClassPackage("ext.type");
      } else {
        configClass.setSuperClassPackage(uri);
      }
      //throw new SAXParseException(String.format("Base component class with element name '%s' not found in component suite '%s'", localName, uri), locator);

    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if (EXML_NAMESPACE_URI.equals(uri)) {
      if ("description".equals(localName)) {
        String characters = popRecordedCharacters();
        if (characters != null) {
          if (expectsOptionalConfigDescription) {
            configClass.getCfgs().get(configClass.getCfgs().size() - 1).setDescription(characters.trim());
            expectsOptionalConfigDescription = false;
          } else if (expectsOptionalComponentDescription) {
            configClass.setDescription(characters.trim());
            expectsOptionalComponentDescription = false;
          }
        }
      }
    }
  }
}
