package net.jangaroo.mxmlc.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitor;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.NamespacedIde;
import net.jangaroo.jooc.ast.NodeImplBase;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * AST node for MXML's <code>&lt;?xml version="..."?></code>.
 */
public class XmlAttribute extends NodeImplBase {
  private Ide ide;
  private JooSymbol eq;
  private JooSymbol value;

  public XmlAttribute(Ide ide, JooSymbol eq, JooSymbol value) {
    this.ide = ide;
    this.eq = eq;
    this.value = value;
  }

  @Override
  public JooSymbol getSymbol() {
    return ide.getSymbol();
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
    String name = ide.getName();
    if (ide instanceof NamespacedIde) {
      name = ((NamespacedIde)ide).getNamespace().getName() + ((NamespacedIde)ide).getSymNamespaceSep().getText() + name;
    }
    return name + eq.getText() + value.getText();
  }
}
