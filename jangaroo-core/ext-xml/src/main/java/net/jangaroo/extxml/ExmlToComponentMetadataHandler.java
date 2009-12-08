package net.jangaroo.extxml;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.ArrayList;
import java.util.List;

import net.jangaroo.extxml.file.ExmlComponentSrcFileScanner;

/**
 * Generates an internal representation of all metadata of the component described by the given EXML.
 */
public class ExmlToComponentMetadataHandler extends CharacterRecordingHandler {

  private final ComponentSuite componentSuite;

  private Locator locator;

  private String componentDescription = "";
  private List<ConfigAttribute> cfgs = new ArrayList<ConfigAttribute>();
  private ComponentClass superComponentClass;

  private boolean expectsOptionalConfigDescription = false;
  private boolean expectsOptionalComponentDescription = false;


  public ExmlToComponentMetadataHandler(ComponentSuite componentSuite) {
    this.componentSuite = componentSuite;
  }

  public void setDocumentLocator(Locator locator) {
    this.locator = locator;
  }

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
        if(expectsOptionalConfigDescription || expectsOptionalComponentDescription) {
          // start recording characters of the description:
          startRecordingCharacters();
        }
      }
    } else if (superComponentClass == null){
      superComponentClass = componentSuite.getComponentClassByNamespaceAndLocalName(uri, localName);
      if (superComponentClass == null) {
        throw new SAXParseException(String.format("No component class for element name '%s' found in component suite '%s'!", localName, uri), locator);
      } else {
//        Log.getErrorHandler().info("Found super class "+superComponentClass.getFullClassName());
//        System.out.println("Found super class "+superComponentClass.getFullClassName());
        // TODO: we could stop the parsing here, but how is this done with a ContentHandler? Throw SAXException("Done")?
      }
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if (ExmlComponentSrcFileScanner.EXML_NAMESPACE_URI.equals(uri)) {
      if ("description".equals(localName)) {
        String characters = popRecordedCharacters();
        if (characters != null) {
          if(expectsOptionalConfigDescription) {
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

  public List<ConfigAttribute> getCfgs() {
    return cfgs;
  }

  public String getComponentDescription() {
    return componentDescription;
  }

  public String getSuperClassName() {
    return superComponentClass == null ? null : superComponentClass.getFullClassName();
  }

}
