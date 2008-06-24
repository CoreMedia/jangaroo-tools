/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
abstract class AbstractVariableDeclaration extends MemberDeclaration {

  JscSymbol optSymConstOrVar;
  Initializer optInitializer;
  JscSymbol optSymSemicolon;

  protected AbstractVariableDeclaration(JscSymbol[] modifiers, int allowedModifiers, JscSymbol optSymConstOrVar, Ide ide,
      TypeRelation optTypeRelation, Initializer optInitializer, JscSymbol optSymSemicolon) {
    super(modifiers, allowedModifiers, ide, optTypeRelation);
    this.optSymConstOrVar = optSymConstOrVar;
    this.optInitializer = optInitializer;
    this.optSymSemicolon = optSymSemicolon;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.beginComment();
    writeModifiers(out);
    out.endComment();
    generateIdeCode(out);
    if (optTypeRelation != null)
      optTypeRelation.generateCode(out);
    if (optInitializer != null)
      optInitializer.generateCode(out);
    if (optSymSemicolon != null)
      out.writeSymbol(optSymSemicolon);
  }

  public abstract void generateIdeCode(JsWriter out) throws IOException;

  public boolean isConst() {
    return optSymConstOrVar != null && optSymConstOrVar.sym == sym.CONST;
  }

  public void analyze(AnalyzeContext context) {
    super.analyze(context);
    if (optInitializer == null && isConst())
      Jscc.error(optSymConstOrVar, "constant must be initialized");
    if (optInitializer != null)
      optInitializer.analyze(context);
  }

}
