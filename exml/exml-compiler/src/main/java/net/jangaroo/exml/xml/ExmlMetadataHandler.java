package net.jangaroo.exml.xml;

import net.jangaroo.exml.ExmlConstants;
import net.jangaroo.exml.ExmlParseException;
import net.jangaroo.exml.model.ConfigAttribute;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.utils.CharacterRecordingHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Generates an internal representation of all metadata of the component described by the given EXML.
 */
public class ExmlMetadataHandler extends CharacterRecordingHandler {
  private ConfigClass configClass;
  private boolean expectsOptionalConfigDescription = false;
  private boolean expectsOptionalComponentDescription = false;

  public ExmlMetadataHandler(ConfigClass configClass) {
    this.configClass = configClass;
  }

  public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
    if (ExmlConstants.EXML_NAMESPACE_URI.equals(uri)) {
      if ("component".equals(localName)) {
        //prepare characterStack for optional component description
        expectsOptionalComponentDescription = true;
      } else if ("cfg".equals(localName)) {
        //handle config elements
        configClass.addCfg(new ConfigAttribute(atts.getValue("name"), atts.getValue("type"), null));
        expectsOptionalConfigDescription = true;
      } else if ("description".equals(localName)) {
        if (expectsOptionalConfigDescription || expectsOptionalComponentDescription) {
          // start recording characters of the description:
          startRecordingCharacters();
        }
      }
    } else if (configClass.getSuperClassName() == null && configClass.getSuperClassPackage() == null) {
      configClass.setSuperClassName(localName);
      String thePackage = ExmlConstants.parsePackageFromNamespace(uri);
      if (thePackage == null) {
        throw new ExmlParseException("namespace '" + uri + "' of superclass element in EXML file does not denote a config package");
      }
      configClass.setSuperClassPackage(thePackage);
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if (ExmlConstants.EXML_NAMESPACE_URI.equals(uri)) {
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
