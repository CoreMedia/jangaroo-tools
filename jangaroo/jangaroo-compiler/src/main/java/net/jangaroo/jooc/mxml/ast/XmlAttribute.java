package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitor;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.NamespacedIde;
import net.jangaroo.jooc.ast.NodeImplBase;
import org.w3c.dom.Node;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class XmlAttribute extends NodeImplBase {

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

  public JooSymbol getValue() {
    return value;
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
    String namespacePrefix = getPrefix();
    return XmlTag.XMLNS.equals(namespacePrefix) || null == namespacePrefix && XmlTag.XMLNS.equals(getLocalName());
  }
}
