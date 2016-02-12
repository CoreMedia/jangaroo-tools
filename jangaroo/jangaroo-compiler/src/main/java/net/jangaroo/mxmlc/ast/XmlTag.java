package net.jangaroo.mxmlc.ast;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitor;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.NamespacedIde;
import net.jangaroo.jooc.ast.NodeImplBase;
import net.jangaroo.jooc.mxml.MxmlToModelParser;
import net.jangaroo.jooc.mxml.MxmlUtils;
import org.w3c.dom.Node;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class XmlTag extends NodeImplBase {

  public static final String XMLNS = "xmlns";

  private static final List<String> TAGS = Arrays.asList(MxmlToModelParser.MXML_DECLARATIONS, MxmlToModelParser.MXML_METADATA, MxmlToModelParser.MXML_SCRIPT);

  private final Map<String, String> xmlNamespaces = new HashMap<String, String>();

  private final String defaultXmlNamespace;

  private final JooSymbol lt;
  private final Ide tagName;
  private final List<XmlAttribute> attributes;
  private final JooSymbol gt;

  private XmlElement xmlElement;

  public XmlTag(JooSymbol lt, Ide tagName, List<XmlAttribute> attributes, JooSymbol gt) {
    this.lt = lt;
    this.tagName = tagName;
    this.attributes = attributes;
    this.gt = gt;

    // extract namespace definitions
    String defaultNamespace = null;
    if(null != attributes) {
      for (XmlAttribute attribute : attributes) {
        String localName = attribute.getLocalName();
        String namespace = attribute.getPrefix();
        String jooValue = (String) attribute.getValue().getJooValue();
        if (XMLNS.equals(namespace)) {
          xmlNamespaces.put(localName, jooValue);
        } else if (null == namespace && XMLNS.equals(localName)) {
          defaultNamespace = jooValue;
        }
      }
    }
    defaultXmlNamespace = defaultNamespace;
  }

  @Override
  public JooSymbol getSymbol() {
    return lt;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return Collections.emptyList();
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
    builder.append(lt.getText()).append(tagName.getName());
    if (attributes != null) {
      for (XmlAttribute attribute : attributes) {
        builder.append(" ").append(attribute);
      }
    }
    builder.append(gt.getText());
    return builder.toString();
  }

  public String getName() {
    return tagName.getName();
  }

  public String getLocalName() {
    return tagName.getIde().getText();
  }

  /**
   * @see Node#getPrefix()
   */
  public String getPrefix() {
    if(tagName instanceof NamespacedIde) {
      return ((NamespacedIde)tagName).getNamespace().getName();
    }
    return null;
  }

  public XmlAttribute getAttribute(final String name) {
    return Iterables.getFirst(
            Iterables.filter(attributes, new Predicate<XmlAttribute>() {
              @Override
              public boolean apply(@Nullable XmlAttribute input) {
                return null != input && Objects.equals(name, input.getSymbol().getText());
              }
            }), null);
  }

  public List<XmlAttribute> getAttributes() {
    return attributes;
  }

  public String getNamespaceUri() {
    String prefix = getPrefix();
    return null != prefix ? xmlElement.getNamespaceUri(prefix) : defaultXmlNamespace;
  }

  String getNamespaceUri(@Nullable String prefix) {
    if(null != prefix) {
      return xmlNamespaces.get(prefix);
    }
    return defaultXmlNamespace;
  }

  public boolean isBuiltInTag() {
    return MxmlUtils.MXML_NAMESPACE_URI.equals(getNamespaceUri()) && TAGS.contains(getName());
  }

  void setElement(XmlElement xmlElement) {
    this.xmlElement = xmlElement;
  }
}
