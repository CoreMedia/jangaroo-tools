package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitor;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.NamespacedIde;
import net.jangaroo.jooc.mxml.MxmlUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class XmlAttribute extends XmlNode {

  private static final String XMLNS = "xmlns";

  /**
   * http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf688f1-7ff1.html
   */
  private static final String IMPLEMENTS = "implements";

  private final Ide ide;
  private final JooSymbol eq;
  private final JooSymbol value;

  public XmlAttribute(Ide ide, JooSymbol eq, JooSymbol value) {
    this.ide = ide;
    this.eq = eq;
    this.value = value;
  }

  @Override
  public JooSymbol getSymbol() {
    return ide.getSymbol();
  }

  public JooSymbol getEq() {
    return eq;
  }

  public JooSymbol getValue() {
    return value;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return Collections.emptyList();
  }

  boolean isImplements() {
    return IMPLEMENTS.equals(getLocalName()) && StringUtils.isBlank(getPrefix());
  }

  boolean isId() {
    return MxmlUtils.MXML_ID_ATTRIBUTE.equals(getLocalName()) && StringUtils.isBlank(getPrefix());
  }

  @Override
  boolean isUntypedAccess() {
    String attributeNamespaceUri = parent.getNamespaceUri(getPrefix());
    return MxmlUtils.EXML_UNTYPED_NAMESPACE.equals(attributeNamespaceUri);
  }

  @Override
  boolean assignPropertyDeclarationOrEvent(XmlElement parentElement) {
    return isId() || isNamespaceDefinition() || isImplements() || super.assignPropertyDeclarationOrEvent(parentElement);
  }

  @Override
  public void analyze(AstNode parentNode) {
    XmlElement parentElement = (XmlElement) parentNode;
    if (parentElement.getType() != null && !isNamespaceDefinition() && !isImplements()) {
      assignPropertyDeclarationOrEvent(parentElement);
    }
  }

  @Override
  public AstNode getParentNode() {
    return null;
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    
  }

  public String getLocalName() {
    return ide.getIde().getText();
  }

  /**
   * @see Node#getPrefix()
   */
  public String getPrefix() {
    if(ide instanceof NamespacedIde) {
      return ((NamespacedIde) ide).getNamespace().getName();
    }
    return null;
  }

  @Override
  JooSymbol getPropertyValue() {
    return getValue();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (ide instanceof NamespacedIde) {
      builder.append(((NamespacedIde)ide).getNamespace().getSymbol().getSourceCode())
              .append(((NamespacedIde)ide).getSymNamespaceSep().getSourceCode());
    }
    builder.append(ide.getIde().getSourceCode()).append(eq.getSourceCode()).append(value.getSourceCode());
    return builder.toString();
  }

  boolean isNamespaceDefinition() {
    return isNamespacePrefixDefinition() || isDefaultNamespaceDefinition();
  }

  boolean isNamespacePrefixDefinition() {
    return XMLNS.equals(getPrefix());
  }

  boolean isDefaultNamespaceDefinition() {
    return getPrefix() == null && XMLNS.equals(getLocalName());
  }
}
