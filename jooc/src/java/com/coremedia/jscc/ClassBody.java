/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;
import java.util.ArrayList;

public class ClassBody extends NodeImplBase {
  JscSymbol lBrace;

  public ArrayList getDeclararations() {
    return declararations;
  }

  ArrayList declararations;
  JscSymbol rBrace;

  ClassDeclaration classDeclaration;

  public ClassBody(JscSymbol lBrace, ArrayList declararations, JscSymbol rBrace) {
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

  public JscSymbol getSymbol() {
    return lBrace;
  }

}
