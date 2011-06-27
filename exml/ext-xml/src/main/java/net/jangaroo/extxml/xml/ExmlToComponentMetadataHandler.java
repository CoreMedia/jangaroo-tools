package net.jangaroo.extxml.xml;

import net.jangaroo.extxml.file.ExmlComponentSrcFileScanner;
import net.jangaroo.extxml.model.ConfigAttribute;
import net.jangaroo.utils.CharacterRecordingHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates an internal representation of all metadata of the component described by the given EXML.
 */
public class ExmlToComponentMetadataHandler extends CharacterRecordingHandler {


  private String componentDescription = "";
  private List<ConfigAttribute> cfgs = new ArrayList<ConfigAttribute>();
  private String superClassLocalName;
  private String superClassNamespaceUri;
  private boolean expectsOptionalConfigDescription = false;
  private boolean expectsOptionalComponentDescription = false;


  public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
    if (ExmlComponentSrcFileScanner.EXML_NAMESPACE_URI.equals(uri)) {
      if ("component".equals(localName)) {
        //prepare characterStack for optional component description
        expectsOptionalComponentDescription = true;
      } else if ("cfg".equals(localName)) {
        //handle config elements
        cfgs.add(new ConfigAttribute(atts.getValue("name"), atts.getValue("type")));
        expectsOptionalConfigDescription = true;
      } else if ("description".equals(localName)) {
        if (expectsOptionalConfigDescription || expectsOptionalComponentDescription) {
          // start recording characters of the description:
          startRecordingCharacters();
        }
      }
    } else if (superClassLocalName == null && superClassNamespaceUri == null) {
      superClassLocalName = localName;
      superClassNamespaceUri = uri;
      //throw new SAXParseException(String.format("Base component class with element name '%s' not found in component suite '%s'", localName, uri), locator);

    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if (ExmlComponentSrcFileScanner.EXML_NAMESPACE_URI.equals(uri)) {
      if ("description".equals(localName)) {
        String characters = popRecordedCharacters();
        if (characters != null) {
          if (expectsOptionalConfigDescription) {
            cfgs.get(cfgs.size() - 1).setDescription(characters.trim());
            expectsOptionalConfigDescription = false;
          } else if (expectsOptionalComponentDescription) {
            componentDescription = characters.trim();
            expectsOptionalComponentDescription = false;
          }
        }
      }
    }
  }

  public String getSuperClassLocalName() {
    return superClassLocalName;
  }

  public String getSuperClassUri() {
    return superClassNamespaceUri;
  }


  public List<ConfigAttribute> getCfgs() {
    return cfgs;
  }

  public String getComponentDescription() {
    return componentDescription;
  }
}
