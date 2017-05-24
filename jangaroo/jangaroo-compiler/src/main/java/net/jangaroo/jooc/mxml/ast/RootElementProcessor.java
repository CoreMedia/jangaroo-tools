package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.mxml.MxmlComponentRegistry;
import net.jangaroo.jooc.mxml.MxmlUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class to consume MXML elements and attributes
 */
class RootElementProcessor {

  /**
   * http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf688f1-7ff1.html
   */
  private static final String IMPLEMENTS = "implements";

  private final List<XmlElement> declarations = new LinkedList<>();
  private final List<JooSymbol> imports = new LinkedList<>();
  private final List<JooSymbol> metadata = new LinkedList<>();
  private final List<JooSymbol> scripts = new LinkedList<>();

  private JooSymbol impl = null;

  void process(MxmlComponentRegistry mxmlComponentRegistry, XmlElement rootNode) {
    rootNode.resolveClass(mxmlComponentRegistry);
    processAttributes(rootNode);
    processElements(rootNode);
    findDeclarations(mxmlComponentRegistry, rootNode);
  }

  /**
   * consume built-in top level MXML elements
   */
  private void processElements(XmlElement rootNode) {
    for (XmlElement element : rootNode.getElements()) {
      if (MxmlUtils.isMxmlNamespace(element.getNamespaceURI())) {
        String name = element.getName();
        if (MxmlUtils.MXML_METADATA.equals(name)) {
          addAll(element.getTextNodes(), metadata);
        } else if (MxmlUtils.MXML_SCRIPT.equals(name)) {
          addAll(element.getTextNodes(), scripts);
        }
      }
    }
  }

  /**
   * consume XMLNS root node attributes
   */
  private void processAttributes(XmlElement rootNode) {
    List<XmlAttribute> rootNodeAttributes = rootNode.getAttributes();
    for (XmlAttribute xmlAttribute : rootNodeAttributes) {
      if (xmlAttribute.isNamespaceDefinition()) {
        imports.add(xmlAttribute.getValue());
      } else if (isImplements(xmlAttribute)) {
        impl = xmlAttribute.getValue();
      }
    }
  }

  private void findDeclarations(MxmlComponentRegistry mxmlComponentRegistry, XmlElement node) {
    JooSymbol idSymbol = node.getIdSymbol();
    if (idSymbol != null) {
      Ide.verifyIdentifier((String) idSymbol.getJooValue(), idSymbol);
      node.resolveClass(mxmlComponentRegistry);
      declarations.add(node);
    }
    node.getElements().forEach(subElement -> findDeclarations(mxmlComponentRegistry, subElement));
  }

  private static boolean isImplements(XmlAttribute xmlAttribute) {
    return IMPLEMENTS.equals(xmlAttribute.getLocalName()) && StringUtils.isBlank(xmlAttribute.getPrefix());
  }

  private void addAll(List<JooSymbol> textNodes, List<JooSymbol> target) {
    target.addAll(textNodes.stream().filter(symbol -> null != symbol && StringUtils.isNotBlank(symbol.getText()))
            .collect(Collectors.toList()));
  }

  List<XmlElement> getDeclarations() {
    return declarations;
  }

  List<JooSymbol> getImports() {
    return imports;
  }

  List<JooSymbol> getMetadata() {
    return metadata;
  }

  List<JooSymbol> getScripts() {
    return scripts;
  }

  @Nullable
  JooSymbol getImpl() {
    return impl;
  }

  static boolean alreadyProcessed(XmlAttribute attribute) {
    return attribute.isNamespaceDefinition() || isImplements(attribute);
  }
}
