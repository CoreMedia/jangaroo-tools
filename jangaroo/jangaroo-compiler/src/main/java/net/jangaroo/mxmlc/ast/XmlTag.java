package net.jangaroo.mxmlc.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitor;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.NodeImplBase;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * AST node for MXML's <code>&lt;?xml version="..."?></code>.
 */
public class XmlTag extends NodeImplBase {

  private JooSymbol lt;
  private final Ide tagName;
  private final List attributes;
  private final JooSymbol gt;

  public XmlTag(JooSymbol lt, Ide tagName, List attributes, JooSymbol gt) {
    this.lt = lt;
    this.tagName = tagName;
    this.attributes = attributes;
    this.gt = gt;
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
      for (XmlAttribute attribute : ((List<XmlAttribute>)attributes)) {
        builder.append(" ").append(attribute);
      }
    }
    builder.append(gt.getText());
    return builder.toString();
  }
}
