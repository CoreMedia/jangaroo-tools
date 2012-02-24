package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;

import java.io.IOException;
import java.util.List;

/**
 * @author fwienber
 */
public class VectorLiteral extends Expr {

  private JooSymbol symNew;
  private JooSymbol symLt;
  private Type type;
  private JooSymbol symGt;
  private ArrayLiteral arrayLiteral;

  public VectorLiteral(JooSymbol symNew, JooSymbol symLt, Type type, JooSymbol symGt, ArrayLiteral arrayLiteral) {
    this.symNew = symNew;
    this.symLt = symLt;
    this.type = type;
    this.symGt = symGt;
    this.arrayLiteral = arrayLiteral;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), type, arrayLiteral);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitVectorLiteral(this);
  }

  @Override
  public JooSymbol getSymbol() {
    return symNew;
  }

  public JooSymbol getSymNew() {
    return symNew;
  }

  public JooSymbol getSymLt() {
    return symLt;
  }

  public Type getVectorType() {
    return type;
  }

  public JooSymbol getSymGt() {
    return symGt;
  }

  public ArrayLiteral getArrayLiteral() {
    return arrayLiteral;
  }

  @Override
  public void scope(Scope scope) {
    type.scope(scope);
    arrayLiteral.scope(scope);
  }
}
