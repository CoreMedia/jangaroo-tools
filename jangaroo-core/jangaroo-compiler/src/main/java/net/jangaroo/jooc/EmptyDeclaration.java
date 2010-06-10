package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Frank Wienberg
 */
public class EmptyDeclaration extends Declaration {

  private JooSymbol symSemicolon;

  public EmptyDeclaration(JooSymbol symSemicolon) {
    super(new JooSymbol[0], 0);
    this.symSemicolon = symSemicolon;
  }

  @Override
  public void scope(final Scope scope) {
  }

  public JooSymbol getSymbol() {
    return symSemicolon;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbolWhitespace(symSemicolon);
  }
}
