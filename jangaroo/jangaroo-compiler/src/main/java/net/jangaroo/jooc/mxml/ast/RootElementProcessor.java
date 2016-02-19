package net.jangaroo.jooc.mxml.ast;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import net.jangaroo.jooc.CompilerError;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.mxml.MxmlUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Helper class to consume MXML elements and attributes
 */
class RootElementProcessor {

  /**
   * http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf688f1-7ff1.html
   */
  private static final String IMPLEMENTS = "implements";

  private final List<XmlElement> declarations = new LinkedList<XmlElement>();
  private final List<JooSymbol> imports = new LinkedList<JooSymbol>();
  private final List<JooSymbol> metadata = new LinkedList<JooSymbol>();
  private final List<JooSymbol> scripts = new LinkedList<JooSymbol>();

  private JooSymbol impl = null;

  void process(XmlElement rootNode) {
    processAttributes(rootNode);
    processElements(rootNode);
  }

  /**
   * consume built-in top level MXML elements
   */
  private void processElements(XmlElement rootNode) {
    Iterator<XmlElement> it = rootNode.getElements().iterator();
    while (it.hasNext()) {
      XmlElement element = it.next();
      if (MxmlUtils.isMxmlNamespace(element.getNamespaceURI())) {
        it.remove();
        String name = element.getName();

        if (MxmlUtils.MXML_DECLARATIONS.equals(name)) {
          declarations.add(element);
        } else if (MxmlUtils.MXML_METADATA.equals(name)) {
          addAll(element.getTextNodes(), metadata);
        } else if (MxmlUtils.MXML_SCRIPT.equals(name)) {
          addAll(element.getTextNodes(), scripts);
        } else {
          throw new CompilerError(element.getSymbol(), "unsupported element");
        }
      }
    }
  }

  /**
   * consume XMLNS root node attributes
   */
  private void processAttributes(XmlElement rootNode) {
    List<XmlAttribute> rootNodeAttributes = rootNode.getAttributes();
    Iterator<XmlAttribute> it = rootNodeAttributes.iterator();
    while (it.hasNext()) {
      XmlAttribute xmlAttribute = it.next();

      if (xmlAttribute.isNamespaceDefinition()) {
        it.remove();
        imports.add(xmlAttribute.getValue());
      } else if (IMPLEMENTS.equals(xmlAttribute.getLocalName()) && StringUtils.isBlank(xmlAttribute.getPrefix())) {
        it.remove();
        impl = xmlAttribute.getValue();
      }
    }
  }

  private void addAll(List<JooSymbol> textNodes, List<JooSymbol> target) {
    target.addAll(Collections2.filter(textNodes, new Predicate<JooSymbol>() {
      @Override
      public boolean apply(@Nullable JooSymbol symbol) {
        return null != symbol && StringUtils.isNotBlank(symbol.getText());
      }
    }));
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
}
