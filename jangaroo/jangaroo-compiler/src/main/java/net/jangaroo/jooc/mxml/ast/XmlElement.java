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
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlElement extends NodeImplBase {

  public static final String CDATA_START_TOKEN = "<![CDATA[";
  public static final String CDATA_END_TOKEN = "]]>";
  private final List<XmlElement> elements = new LinkedList<>();

  private final XmlTag openingMxmlTag;
  private List children;
  private final XmlTag closingMxmlTag;

  private XmlElement parent;

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

  public String getPrefix() {
    return openingMxmlTag.getPrefix();
  }

  @Override
  public JooSymbol getSymbol() {
    return openingMxmlTag.getSymbol();
  }

  public JooSymbol getLastSymbol() {
    return closingMxmlTag == null ? openingMxmlTag.getSymGt() : closingMxmlTag.getSymbol();
  }

  @Override
  public List<? extends AstNode> getChildren() {
    //noinspection unchecked
    Iterable<? extends AstNode> filter = Iterables.filter(children, AstNode.class);
    return Lists.newLinkedList(filter);
  }

  List<JooSymbol> getTextNodes() {
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

  void removeFromParent() {
    int removeIndex = parent.children.indexOf(this);
    if (removeIndex > 0 && parent.children.get(removeIndex - 1) instanceof JooSymbol) {
      parent.children.remove(--removeIndex);
    }
    parent.children.remove(removeIndex);
    if (parent.getChildren().isEmpty()) {
      parent.removeFromParent();
    }
  }

  @Override
  public AstNode getParentNode() {
    return parent;
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

  public String getLocalName() {
    return openingMxmlTag.getLocalName();
  }

  public String getNamespaceURI() {
    return getNamespaceUri(getPrefix());
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
    XmlAttribute attribute = openingMxmlTag.getAttribute(namespaceUri, localName);
    return null != attribute ? (String) attribute.getValue().getJooValue() : "";
  }

  void prependTextNode(String text) {
    children = new ArrayList(children);
    JooSymbol firstSymbol = (JooSymbol) children.get(0);
    final String oldText = firstSymbol.getText();
    String pre = "";
    String post = oldText;
    if (oldText.startsWith(CDATA_START_TOKEN)) {
      pre = CDATA_START_TOKEN;
      post = oldText.substring(CDATA_START_TOKEN.length());
    }
    children.set(0, new JooSymbol(pre + text + post));
  }

  void appendTextNode(String text) {
    children = new ArrayList(children);
    int last = children.size() - 1;
    JooSymbol lastSymbol = (JooSymbol) children.get(last);
    final String oldText = lastSymbol.getText();
    String pre = oldText;
    String post = "";
    if (oldText.endsWith(CDATA_END_TOKEN)) {
      pre = oldText.substring(0, oldText.length() - CDATA_END_TOKEN.length());
      post = CDATA_END_TOKEN;
    }
    Matcher matcher = Pattern.compile("\\s*$").matcher(pre);
    if (matcher.find()) {
      post = pre.substring(matcher.start()) + post;
      pre = pre.substring(0, matcher.start());
    }
    children.set(last, new JooSymbol(pre + text + post));
  }
}
