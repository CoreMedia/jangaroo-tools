package net.jangaroo.jooc.mxml.ast;

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
 *
 * https://www.w3.org/TR/REC-xml/#sec-prolog-dtd
 */
public class XmlHeader extends NodeImplBase {
  private final Ide xmlIde;
  private final List<XmlAttribute> attributes;
  private final JooSymbol questionGt;
  private JooSymbol ltQuestion;

  public XmlHeader(JooSymbol ltQuestion, Ide xmlIde, List<XmlAttribute> attributes, JooSymbol questionGt) {
    this.ltQuestion = ltQuestion;
    this.xmlIde = xmlIde;
    this.attributes = attributes;
    this.questionGt = questionGt;
  }

  @Override
  public JooSymbol getSymbol() {
    return ltQuestion;
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
