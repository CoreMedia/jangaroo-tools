/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Andreas Gawecki
 */
public class ClassBody extends NodeImplBase {
  JooSymbol lBrace;

  public ArrayList getDeclararations() {
    return declararations;
  }

  ArrayList declararations;
  JooSymbol rBrace;

  ClassDeclaration classDeclaration;

  public ClassBody(JooSymbol lBrace, ArrayList declararations, JooSymbol rBrace) {
    this.lBrace = lBrace;
    this.declararations = declararations;
    this.rBrace = rBrace;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbolWhitespace(lBrace);
    generateCode(declararations, out);
    out.writeSymbolWhitespace(rBrace);
  }

  public void analyze(AnalyzeContext context) {
    analyze(declararations, context);
  }

  public JooSymbol getSymbol() {
    return lBrace;
  }

}
