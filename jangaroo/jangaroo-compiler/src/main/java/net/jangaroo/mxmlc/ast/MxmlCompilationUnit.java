package net.jangaroo.mxmlc.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitor;
import net.jangaroo.jooc.ast.CompilationUnit;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * AST node for an MXML compilation unit, represented by its root node.
 */
public class MxmlCompilationUnit extends CompilationUnit {

  private XmlHeader optXmlHeader;
  private XmlElement rootNode;

  public MxmlCompilationUnit(XmlHeader optXmlHeader, XmlElement rootNode) {
    super(null, null, null, null, null, null);
    this.optXmlHeader = optXmlHeader;
    this.rootNode = rootNode;
  }

  @Override
  public JooSymbol getSymbol() {
    return rootNode.getSymbol();
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
}
