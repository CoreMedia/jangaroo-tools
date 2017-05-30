package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitor;
import net.jangaroo.jooc.mxml.MxmlComponentRegistry;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.utils.CompilerUtils;
import org.w3c.dom.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static net.jangaroo.jooc.mxml.MxmlParserHelper.parsePackageFromNamespace;

public class XmlElement extends XmlNode {

  private final List<XmlElement> elements = new LinkedList<>();

  private final XmlTag openingMxmlTag;
  private final List<Object> children;
  private final XmlTag closingMxmlTag;

  XmlElement parent;
  private String classQName;

  public XmlElement(@Nonnull XmlTag openingMxmlTag, @Nullable List children, @Nullable XmlTag closingMxmlTag) {
    this.openingMxmlTag = openingMxmlTag;
    this.children = children != null ? children : Collections.emptyList();
    this.closingMxmlTag = closingMxmlTag;
    initChildren();
    openingMxmlTag.setElement(this);
  }

  private void initChildren() {
    for (Object child : children) {
      if (child instanceof XmlElement) {
        XmlElement xmlElement = (XmlElement) child;
        elements.add(xmlElement);
        xmlElement.parent = this;
      }
    }
  }

  public String getName() {
    return openingMxmlTag.getLocalName();
  }

  @Override
  public String getPrefix() {
    return openingMxmlTag.getPrefix();
  }

  @Override
  public JooSymbol getSymbol() {
    return openingMxmlTag.getSymbol();
  }

  public JooSymbol getClosingSymbol() {
    return closingMxmlTag != null ? closingMxmlTag.getSymbol() : openingMxmlTag.getClosingSymbol();
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return children.stream()
            .filter((AstNode.class)::isInstance)
            .map(child -> (AstNode) child)
            .collect(Collectors.toList());
  }

  List<JooSymbol> getTextNodes() {
    return children.stream()
            .filter(JooSymbol.class::isInstance)
            .map(link -> (JooSymbol) link)
            .collect(Collectors.toList());
  }

  public List<XmlElement> getElements() {
    return elements;
  }

  @Override
  public void scope(Scope scope) {
    
  }

  @Override
  public void analyze(AstNode parentNode) {

  }

  @Override
  public AstNode getParentNode() {
    return null;
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(openingMxmlTag);
    if (null != children) {
      for (Object child : children) {
        if (child instanceof JooSymbol) {
          child = ((JooSymbol)child).getSourceCode();
        }
        builder.append(child);
      }
    }
    if(null != closingMxmlTag) {
      builder.append(closingMxmlTag);
    }
    return builder.toString();
  }

  public List<XmlAttribute> getAttributes() {
    return openingMxmlTag.getAttributes();
  }

  @Nullable
  XmlAttribute getAttribute(String name) {
    return openingMxmlTag.getAttribute(name);
  }

  @Nullable
  JooSymbol getIdSymbol() {
    return openingMxmlTag.getIdSymbol();
  }

  @Nullable
  public String getId() {
    JooSymbol idSymbol = getIdSymbol();
    return idSymbol == null ? null : (String) idSymbol.getJooValue();
  }

  public String getLocalName() {
    return openingMxmlTag.getLocalName();
  }

  public String getNamespaceURI() {
    return getNamespaceUri(getPrefix());
  }

  public boolean isBuiltInElement() {
    return MxmlUtils.isMxmlNamespace(getNamespaceURI()) && MxmlUtils.BUILT_IN_ELEMENT_NAMES.contains(getLocalName());
  }


  String getNamespaceUri(@Nullable String prefix) {
    String localResult = openingMxmlTag.getNamespaceUri(prefix);
    if(null != localResult) {
      return localResult;
    }
    if(null != parent) {
      return parent.getNamespaceUri(prefix);
    }
    return null;
  }

  /**
   * @see Element#getAttributeNS(String, String)
   */
  String getAttributeNS(String namespaceUri, String localName) {
    XmlAttribute attribute = getAttributeNodeNS(namespaceUri, localName);
    return null != attribute ? (String) attribute.getValue().getJooValue() : "";
  }

  /**
   * @see Element#getAttributeNodeNS(String, String)
   */
  XmlAttribute getAttributeNodeNS(String namespaceUri, String localName) {
    return openingMxmlTag.getAttribute(namespaceUri, localName);
  }

  String resolveClass(MxmlComponentRegistry mxmlComponentRegistry) {
    if (classQName == null) {
      String prefix = getPrefix();
      String name = getLocalName();
      String uri = getNamespaceURI();
      if (uri == null) {
        throw Jooc.error(getSymbol(),"Undeclared namespace URI for prefix '" + prefix + "'.");
      } else {
        String packageName = parsePackageFromNamespace(uri);
        if (packageName != null) {
          classQName = CompilerUtils.qName(packageName, name);
        } else {
          classQName = mxmlComponentRegistry.getClassName(uri, name);
        }
      }
      if (classQName == null) {
        if (getPrefix() != null) {
          name = getPrefix() + ":" + name;
        }
        throw Jooc.error(this, "Could not resolve class from MXML node <" + name + "/>");
      }
    }
    return classQName;
  }

  public String getClassQName() {
    return classQName;
  }
}
