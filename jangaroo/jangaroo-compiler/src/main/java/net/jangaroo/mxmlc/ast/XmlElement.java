package net.jangaroo.mxmlc.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitor;
import net.jangaroo.jooc.ast.NodeImplBase;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * AST node for MXML's <code>&lt;?xml version="..."?></code>.
 */
public class XmlElement extends NodeImplBase {

  private final XmlTag openingMxmlTag;
  private final List children;
  private final XmlTag closingMxmlTag;

  public XmlElement(XmlTag openingMxmlTag, List children, XmlTag closingMxmlTag) {
    this.openingMxmlTag = openingMxmlTag;
    this.children = children;
    this.closingMxmlTag = closingMxmlTag;
  }

  @Override
  public JooSymbol getSymbol() {
    return openingMxmlTag.getSymbol();
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
    builder.append(openingMxmlTag);
    if (children != null) {
      for (Object child : children) {
        if (child instanceof JooSymbol) {
          child = ((JooSymbol)child).getText();
        }
        builder.append(child);
      }
      builder.append(closingMxmlTag);
    }
    return builder.toString();
  }
}
