package net.jangaroo.mxmlc.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.ast.AstVisitor;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeDeclaration;

import java.io.IOException;

public class MxmlClassDeclaration extends IdeDeclaration {

  protected MxmlClassDeclaration(JooSymbol ide) {
    super(new Ide(ide));
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {

  }
}
