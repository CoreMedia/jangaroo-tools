package net.jangaroo.jooc.mxml;

import net.jangaroo.jooc.model.AnnotationModel;
import net.jangaroo.jooc.model.AnnotationPropertyModel;
import net.jangaroo.jooc.model.ClassModel;
import net.jangaroo.utils.AS3Type;
import net.jangaroo.utils.CompilerUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Some useful utility functions for EXML handling.
 */
public class MxmlUtils {

  private static final String MXML_NAMESPACE_URI = "http://ns.adobe.com/mxml/2009";
  public static final String RESOURCE_BUNDLE_ANNOTATION = "ResourceBundle";

  public static boolean isMxmlNamespace(String uri) {
    return MXML_NAMESPACE_URI.equals(uri);
  }

  public static boolean isBindingExpression(String attributeValue) {
    return attributeValue.startsWith("{") && attributeValue.endsWith("}");
  }

  public static String getBindingExpression(String attributeValue) {
    return attributeValue.substring(1, attributeValue.length() - 1);
  }

  public static void addImport(Set<String> imports, String importedClassName) {
    if (importedClassName != null && importedClassName.contains(".")) { // do not import top-level classes!
      imports.add(importedClassName);
    }
  }

  public static String parsePackageFromNamespace(String uri) {
    return uri.endsWith(".*") ? uri.substring(0, uri.length() -2) : uri.equals("*") || isMxmlNamespace(uri) ? "" : null;
  }

  public static Element findChildElement(Element element, String namespace, String nodeName) {
    for (Element child : getChildElements(element)) {
      if (namespace.equals(child.getNamespaceURI()) && nodeName.equals(child.getLocalName())) {
        return child;
      }
    }
    return null;
  }

  public static List<Element> getChildElements(Element element) {
    List<Element> result = new ArrayList<Element>();
    NodeList propertyChildNotes = element.getChildNodes();
    for (int j = 0; j < propertyChildNotes.getLength(); j++) {
      Node childNode = propertyChildNotes.item(j);
      if (childNode.getNodeType() == Node.ELEMENT_NODE) {
        result.add((Element) childNode);
      }
    }
    return result;
  }

  public static Object getAttributeValue(String attributeValue, String type) {
    if (!MxmlUtils.isBindingExpression(attributeValue)) {
      AS3Type as3Type = type == null ? AS3Type.ANY : AS3Type.typeByName(type);
      if (AS3Type.ANY.equals(as3Type)) {
        as3Type = CompilerUtils.guessType(attributeValue);
      }
      if (as3Type != null) {
        attributeValue = attributeValue.trim();
        switch (as3Type) {
          case BOOLEAN:
            return Boolean.parseBoolean(attributeValue);
          case NUMBER:
            return Double.parseDouble(attributeValue);
          case UINT:
          case INT:
            return Long.parseLong(attributeValue);
        }
      }
    }
    // code expression, Object or specific type. We don't care (for now).
    return attributeValue;
  }

  /**
   * Return a stringified representation of an object value.
   * This method can handle <code>Number</code>, <code>Boolean</code>,
   * <code>String</code> and strings containing a binding expression (curly braces).
   *
   * @param value        The value to be serialized.
   * @return a stringified representation of the object value
   */
  public static String valueToString(Object value) {
    if (value == null) {
      return "null";
    }
    if (value instanceof Number
        || value instanceof Boolean) {
      return value.toString();
    } else if (MxmlUtils.isBindingExpression(value.toString())) {
      return MxmlUtils.getBindingExpression(value.toString());
    }
    return CompilerUtils.quote(value.toString());

  }

  public static void addResourceBundleAnnotation(ClassModel classModel, String bundle) {
    // check if already present:
    List<AnnotationModel> annotations = classModel.getAnnotations(RESOURCE_BUNDLE_ANNOTATION);
    for (AnnotationModel annotation : annotations) {
      AnnotationPropertyModel propertyModel = annotation.getPropertiesByName().get("");
      if (propertyModel != null && bundle.equals(propertyModel.getStringValue())) {
        return; // found: bail out
      }
    }
    // not found: add a new annotation
    classModel.addAnnotation(new AnnotationModel(RESOURCE_BUNDLE_ANNOTATION, new AnnotationPropertyModel("", bundle)));
  }
}
