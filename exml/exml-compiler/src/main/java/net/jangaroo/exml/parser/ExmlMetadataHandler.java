package net.jangaroo.exml.parser;

import net.jangaroo.exml.api.ExmlcException;
import net.jangaroo.exml.compiler.Exmlc;
import net.jangaroo.exml.model.ConfigAttribute;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.exml.model.ConfigClassType;
import net.jangaroo.exml.model.Constant;
import net.jangaroo.utils.CharacterRecordingHandler;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Generates an internal representation of all metadata of the component described by the given EXML.
 */
public class ExmlMetadataHandler extends CharacterRecordingHandler {
  private static final Map<String,ConfigClassType> EXML_ROOT_NODE_TO_CONFIG_CLASS_TYPE
    = Collections.unmodifiableMap(new HashMap<String, ConfigClassType>() {
    {
      put(Exmlc.EXML_COMPONENT_NODE_NAME, ConfigClassType.XTYPE);
      put(Exmlc.EXML_PLUGIN_NODE_NAME, ConfigClassType.PTYPE);
      put(Exmlc.EXML_LAYOUT_NODE_NAME, ConfigClassType.TYPE);
      put(Exmlc.EXML_GRID_COLUMN_NODE_NAME, ConfigClassType.GCTYPE);
    }
  });

  private ConfigClass configClass;
  private Locator locator;

  private Deque<QName> elementPath = new LinkedList<QName>();

  public ExmlMetadataHandler(ConfigClass configClass) {
    this.configClass = configClass;
  }

  @Override
  public void setDocumentLocator(Locator locator) {
    this.locator = locator;
  }

  public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
    if (Exmlc.isExmlNamespace(uri)) {
      if (Exmlc.EXML_ROOT_NODE_NAMES.contains(localName)) {
        configClass.setType(EXML_ROOT_NODE_TO_CONFIG_CLASS_TYPE.get(localName));
      } else if (Exmlc.EXML_CFG_NODE_NAME.equals(localName)) {
        //handle config elements
        ConfigAttribute cfg = new ConfigAttribute(atts.getValue(Exmlc.EXML_CFG_NAME_ATTRIBUTE), atts.getValue(Exmlc.EXML_CFG_TYPE_ATTRIBUTE), null);
        if(!configClass.contains(cfg)) {
          configClass.addCfg(cfg);
        } else {
          throw new ExmlcException("Config '" + cfg.getName() + "' already defined.", locator.getLineNumber(), locator.getColumnNumber());
        }
      } else if (Exmlc.EXML_DESCRIPTION_NODE_NAME.equals(localName)) {
        if (isLastInPathExmlClass() || isLastInPathConfig() || isLastInPathConstant()) {
          // start recording characters of the description:
          startRecordingCharacters();
        }
      } else if (Exmlc.EXML_CONSTANT_NODE_NAME.equals(localName)) {
        Constant constant = new Constant(atts.getValue(Exmlc.EXML_CONSTANT_NAME_ATTRIBUTE), atts.getValue(Exmlc.EXML_CONSTANT_VALUE_ATTRIBUTE));
        if(!configClass.getConstants().contains(constant)) {
          configClass.addConstant(constant);
        } else {
          throw new ExmlcException("Constant '" + constant.getName() + "' already defined.", locator.getLineNumber(), locator.getColumnNumber());
        }
      }
    } else if (elementPath.size() == 1) {
      if (configClass.getSuperClassName() != null) {
        throw new ExmlcException("root node of EXML contained more than one component definition", locator.getLineNumber(), locator.getColumnNumber());
      }

      String thePackage = Exmlc.parsePackageFromNamespace(uri);
      if (thePackage == null) {
        throw new ExmlcException("namespace '" + uri + "' of superclass element in EXML file does not denote a config package", locator.getLineNumber(), locator.getColumnNumber());
      }
      configClass.setSuperClassName(thePackage + "." + localName);
    }
    elementPath.push(new QName(uri, localName));
  }

  private boolean isLastInPathExmlClass() {
    QName parent = elementPath.peek();
    return Exmlc.isExmlNamespace(parent.getNamespaceURI()) && Exmlc.EXML_ROOT_NODE_NAMES.contains(parent.getLocalPart());
  }

  private boolean isLastInPathConfig() {
    QName parent = elementPath.peek();
    return Exmlc.isExmlNamespace(parent.getNamespaceURI()) && Exmlc.EXML_CFG_NODE_NAME.equals(parent.getLocalPart());
  }

  private boolean isLastInPathConstant() {
    QName parent = elementPath.peek();
    return Exmlc.isExmlNamespace(parent.getNamespaceURI()) && Exmlc.EXML_CONSTANT_NODE_NAME.equals(parent.getLocalPart());
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if (Exmlc.isExmlNamespace(uri)) {
      elementPath.pop();
      if (Exmlc.EXML_DESCRIPTION_NODE_NAME.equals(localName)) {
        String characters = popRecordedCharacters();
        if (characters != null) {
          if (isLastInPathConfig()) {
            configClass.getCfgs().get(configClass.getCfgs().size() - 1).setDescription(characters.trim());
          } else if (isLastInPathExmlClass()) {
            configClass.setDescription(characters.trim());
          } else if (isLastInPathConstant()) {
            configClass.getConstants().get(configClass.getConstants().size() - 1).setDescription(characters.trim());
          }
        }
      }
    }
  }
}
