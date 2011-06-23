package net.jangaroo.jooc;

import java.io.IOException;

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
  public void visit(AstVisitor visitor) {
    visitor.visitVectorLiteral(this);
  }

  @Override
  protected void generateJsCode(JsWriter out) throws IOException {
    out.beginComment();
    out.writeSymbol(symNew);
    out.writeSymbol(symLt);
    type.generateJsCode(out);
    out.writeSymbol(symGt);
    out.endComment();
    arrayLiteral.generateJsCode(out);
  }

  @Override
  public JooSymbol getSymbol() {
    return symNew;
  }

  @Override
  public void scope(Scope scope) {
    type.scope(scope);
    arrayLiteral.scope(scope);
  }
}
