package net.jangaroo.jooc.mxml.ast;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitor;
import net.jangaroo.jooc.ast.NodeImplBase;
import org.w3c.dom.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class XmlElement extends NodeImplBase {

  private final List<XmlElement> elements = new LinkedList<XmlElement>();

  private final XmlTag openingMxmlTag;
  private final List children;
  private final XmlTag closingMxmlTag;

  private XmlElement parent;

  public XmlElement(@Nonnull XmlTag openingMxmlTag, @Nullable List children, @Nonnull XmlTag closingMxmlTag) {
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
    return openingMxmlTag.getName();
  }

  public String getPrefix() {
    return openingMxmlTag.getPrefix();
  }

  @Override
  public JooSymbol getSymbol() {
    return openingMxmlTag.getSymbol();
  }

  @Override
  public List<? extends AstNode> getChildren() {
    //noinspection unchecked
    Iterable<? extends AstNode> filter = Iterables.filter(children, AstNode.class);
    return Lists.newLinkedList(filter);
  }

  public List<JooSymbol> getTextNodes() {
    //noinspection unchecked
    return Lists.newLinkedList(Iterables.filter(children, JooSymbol.class));
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
          child = ((JooSymbol)child).getJooValue();
        }
        builder.append(child);
      }
      builder.append(closingMxmlTag);
    }
    return builder.toString();
  }

  public List<XmlAttribute> getAttributes() {
    return openingMxmlTag.getAttributes();
  }

  /**
   * @see Element#getAttribute
   */
  public String getAttribute(String name) {
    XmlAttribute attribute = openingMxmlTag.getAttribute(name);
    return null != attribute ? (String) attribute.getValue().getJooValue() : "";
  }

  public String getLocalName() {
    return openingMxmlTag.getLocalName();
  }

  public String getNamespaceURI() {
    return getNamespaceUri(getPrefix());
  }

  public String getNamespaceUri(@Nullable String prefix) {
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
  public String getAttributeNS(String namespaceUri, String localName) {
    XmlAttribute attribute = openingMxmlTag.getAttribute(namespaceUri, localName);
    return null != attribute ? (String) attribute.getValue().getJooValue() : "";
  }
}
