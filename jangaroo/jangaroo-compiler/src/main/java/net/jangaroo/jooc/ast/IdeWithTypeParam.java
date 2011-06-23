package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.ast.AstVisitor;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.Type;

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

  private static JooSymbol withTypeParam(JooSymbol ide, Type type) {
    return new JooSymbol(ide.sym, ide.getFileName(), ide.getLine(), ide.getColumn(), ide.getWhitespace(),
      ide.getText() + "$object", // TODO: depending on type, use the right suffix
      ide.getJooValue());
  }

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitIdeWithTypeParam(this);
  }

  @Override
  public void generateJsCode(JsWriter out) throws IOException {
    super.generateJsCode(out);
    writeTypeParamAsComment(out);
  }

  @Override
  public void generateCodeAsExpr(JsWriter out) throws IOException {
    super.generateCodeAsExpr(out);
    writeTypeParamAsComment(out);
  }

  @Override
  public void generateAsApiCode(JsWriter out) throws IOException {
    out.writeSymbol(originalIde);
    writeTypeParam(out);
  }

  private void writeTypeParamAsComment(JsWriter out) throws IOException {
    out.beginComment();
    writeTypeParam(out);
    out.endComment();
  }

  private void writeTypeParam(JsWriter out) throws IOException {
    out.writeSymbol(symDotLt);
    type.generateAsApiCode(out);
    out.writeSymbol(symGt);
  }

}
