package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.mxml.MxmlComponentRegistry;
import net.jangaroo.jooc.mxml.MxmlUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class to consume MXML elements and attributes
 */
class RootElementProcessor {

  private XmlElement declarationsElement;
  private final List<XmlElement> references = new LinkedList<>();
  private final List<JooSymbol> imports = new LinkedList<>();
  private final List<JooSymbol> metadata = new LinkedList<>();
  private final List<JooSymbol> scripts = new LinkedList<>();

  private JooSymbol impl = null;

  void process(MxmlComponentRegistry mxmlComponentRegistry, XmlElement rootNode) {
    rootNode.resolveClass(mxmlComponentRegistry);
    processAttributes(rootNode);
    processElements(rootNode);
    findReferences(mxmlComponentRegistry, rootNode);
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
        } else if (MxmlUtils.MXML_DECLARATIONS.equals(name)) {
          declarationsElement = element;
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
      } else if (xmlAttribute.isImplements()) {
        impl = xmlAttribute.getValue();
      }
    }
  }

  private void findReferences(MxmlComponentRegistry mxmlComponentRegistry, XmlElement node) {
    node.resolveClass(mxmlComponentRegistry);
    JooSymbol idSymbol = node.getIdSymbol();
    if (idSymbol != null) {
      Ide.verifyIdentifier((String) idSymbol.getJooValue(), idSymbol);
      if (node.parent != declarationsElement) {
        references.add(node);
      }
    }
    node.getElements().forEach(subElement -> findReferences(mxmlComponentRegistry, subElement));
  }

  private void addAll(List<JooSymbol> textNodes, List<JooSymbol> target) {
    target.addAll(textNodes.stream().filter(symbol -> null != symbol && StringUtils.isNotBlank(symbol.getText()))
            .collect(Collectors.toList()));
  }

  XmlElement getDeclarationsElement() {
    return declarationsElement;
  }

  List<XmlElement> getDeclarations() {
    return declarationsElement == null ? Collections.emptyList() : declarationsElement.getElements();
  }

  List<XmlElement> getReferences() {
    return references;
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

}
