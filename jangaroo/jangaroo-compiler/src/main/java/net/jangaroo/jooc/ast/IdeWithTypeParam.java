package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.JsWriter;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA. User: fwienber Date: 27.02.11 Time: 17:08 To change this template use File | Settings |
 * File Templates.
 */
public class IdeWithTypeParam extends Ide {

  private JooSymbol originalIde;
  private JooSymbol symDotLt;
  private Type type;
  private JooSymbol symGt;

  public IdeWithTypeParam(JooSymbol ide, JooSymbol symDotLt, Type type, JooSymbol symGt) {
    super(withTypeParam(ide, type));
    this.originalIde = ide;
    this.symDotLt = symDotLt;
    this.type = type;
    this.symGt = symGt;
  }

  public JooSymbol getOriginalIde() {
    return originalIde;
  }

  public JooSymbol getSymDotLt() {
    return symDotLt;
  }

  public Type getType() {
    return type;
  }

  public JooSymbol getSymGt() {
    return symGt;
  }

  @SuppressWarnings({"UnusedParameters"})
  private static JooSymbol withTypeParam(JooSymbol ide, Type type) { // nosonar
    return new JooSymbol(ide.sym, ide.getFileName(), ide.getLine(), ide.getColumn(), ide.getWhitespace(),
            ide.getText() + "$object", // TODO: depending on type, use the right suffix
            ide.getJooValue());
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitIdeWithTypeParam(this);
  }

  @Override
  public void generateCodeAsExpr(JsWriter out) throws IOException {
    super.generateCodeAsExpr(out);
    out.beginComment();
    out.writeSymbol(symDotLt);
    out.writeSymbol(type.getIde().getIde());
    out.writeSymbol(symGt);
    out.endComment();
  }
}
