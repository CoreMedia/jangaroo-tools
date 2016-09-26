package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;

import java.io.IOException;
import java.util.Collections;

/**
 * @author Frank Wienberg
 */
public class EmptyDeclaration extends Declaration {

  private JooSymbol symSemicolon;

  public EmptyDeclaration(JooSymbol symSemicolon) {
    super(Collections.<Annotation>emptyList(), new JooSymbol[0]);
    this.symSemicolon = symSemicolon;
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitEmptyDeclaration(this);
  }

  @Override
  public void scope(final Scope scope) {
  }

  public JooSymbol getSymbol() {
    return symSemicolon;
  }

  public JooSymbol getSymSemicolon() {
    return symSemicolon;
  }
}
