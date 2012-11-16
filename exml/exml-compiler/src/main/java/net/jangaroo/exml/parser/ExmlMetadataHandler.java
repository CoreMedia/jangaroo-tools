package net.jangaroo.exml.parser;

import net.jangaroo.exml.api.ExmlcException;
import net.jangaroo.exml.compiler.Exmlc;
import net.jangaroo.exml.model.AnnotationAt;
import net.jangaroo.exml.model.ConfigAttribute;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.exml.model.ConfigClassType;
import net.jangaroo.exml.model.DescriptionHolder;
import net.jangaroo.exml.model.Declaration;
import net.jangaroo.exml.model.PublicApiMode;
import net.jangaroo.exml.utils.ExmlUtils;
import net.jangaroo.jooc.Jooc;
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
    if (ExmlUtils.isExmlNamespace(uri)) {
      if (Exmlc.EXML_ROOT_NODE_NAMES.contains(localName)) {
        configClass.setType(EXML_ROOT_NODE_TO_CONFIG_CLASS_TYPE.get(localName));

        for (int i = 0; i < atts.getLength(); i++) {
          //baseClass attribute has been specified, so the super class of the component is actually that
          if (Exmlc.EXML_PUBLIC_API_ATTRIBUTE.equals(atts.getLocalName(i))) {
            PublicApiMode publicApiMode = Exmlc.parsePublicApiMode(atts.getValue(i));
            if (publicApiMode != PublicApiMode.FALSE) {
              configClass.addAnnotation(Jooc.PUBLIC_API_INCLUSION_ANNOTATION_NAME);
            }
          }
        }
      } else if (Exmlc.EXML_ANNOTATION_NODE_NAME.equals(localName)) {
        AnnotationAt annotationAt = AnnotationAt.BOTH; // default for "at" is "both"
        for (int i = 0; i < atts.getLength(); i++) {
          if (Exmlc.EXML_ANNOTATION_AT_ATTRIBUTE.equals(atts.getLocalName(i))) {
            // found "at" attribute: parse it (might throw ExmlcException)
            annotationAt = Exmlc.parseAnnotationAtValue(atts.getValue(i));
            break;
          }
        }
        if (annotationAt != AnnotationAt.TARGET) {
          startRecordingCharacters();
        }
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
        Declaration constant = new Declaration(atts.getValue(Exmlc.EXML_DECLARATION_NAME_ATTRIBUTE), atts.getValue(Exmlc.EXML_DECLARATION_VALUE_ATTRIBUTE), atts.getValue(Exmlc.EXML_DECLARATION_TYPE_ATTRIBUTE));
        if(!configClass.getConstants().contains(constant)) {
          configClass.addConstant(constant);
        } else {
          throw new ExmlcException("Constant '" + constant.getName() + "' already defined.", locator.getLineNumber(), locator.getColumnNumber());
        }
      } else if (Exmlc.EXML_IMPORT_NODE_NAME.equals(localName)) {
        String importedClassName = atts.getValue(Exmlc.EXML_IMPORT_CLASS_ATTRIBUTE);
        if (importedClassName != null) {
          // TODO: check illegal values? Throw error when null?
          configClass.addImport(importedClassName);
        }
      }
    } else if (elementPath.size() == 1) {
      if (configClass.getSuperClassName() != null) {
        throw new ExmlcException("root node of EXML contained more than one component definition", locator.getLineNumber(), locator.getColumnNumber());
      }

      String thePackage = ExmlUtils.parsePackageFromNamespace(uri);
      if (thePackage == null) {
        throw new ExmlcException("namespace '" + uri + "' of superclass element in EXML file does not denote a config package", locator.getLineNumber(), locator.getColumnNumber());
      }
      configClass.setSuperClassName(thePackage + "." + localName);
    }
    elementPath.push(new QName(uri, localName));
  }

  private boolean isLastInPathExmlClass() {
    QName parent = elementPath.peek();
    return ExmlUtils.isExmlNamespace(parent.getNamespaceURI()) && Exmlc.EXML_ROOT_NODE_NAMES.contains(parent.getLocalPart());
  }

  private boolean isLastInPathConfig() {
    QName parent = elementPath.peek();
    return ExmlUtils.isExmlNamespace(parent.getNamespaceURI()) && Exmlc.EXML_CFG_NODE_NAME.equals(parent.getLocalPart());
  }

  private boolean isLastInPathConstant() {
    QName parent = elementPath.peek();
    return ExmlUtils.isExmlNamespace(parent.getNamespaceURI()) && Exmlc.EXML_CONSTANT_NODE_NAME.equals(parent.getLocalPart());
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    elementPath.pop();
    if (ExmlUtils.isExmlNamespace(uri)) {
      if (Exmlc.EXML_DESCRIPTION_NODE_NAME.equals(localName)) {
        String characters = popRecordedCharacters();
        if (characters != null) {
          DescriptionHolder descriptionHolder =
            isLastInPathConfig() ? configClass.getCfgs().get(configClass.getCfgs().size() - 1)
              : isLastInPathExmlClass() ? configClass
              : isLastInPathConstant() ? configClass.getConstants().get(configClass.getConstants().size() - 1)
              : null;
          if (descriptionHolder != null) {
            descriptionHolder.setDescription(characters);
          }
        }
      } else if (Exmlc.EXML_ANNOTATION_NODE_NAME.equals(localName)) {
        String characters = popRecordedCharacters();
        if (characters != null) {
          configClass.addAnnotation(characters);
        }
      }
      if (elementPath.isEmpty() && configClass.getSuperClassName() == null) {
        // if nothing else is specified, extend JavaScriptObject:
        configClass.setSuperClassName("joo.JavaScriptObject");
      }
    }
  }
}
