/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

public class FieldDeclaration extends AbstractVariableDeclaration {

  public FieldDeclaration(JscSymbol[] modifiers, JscSymbol symConstOrVar, Ide ide,
      TypeRelation optTypeRelation, Initializer optInitializer, JscSymbol symSemicolon) {
    super(modifiers,
            MODIFIERS_SCOPE|MODIFIER_STATIC,
            symConstOrVar, ide, optTypeRelation, optInitializer, symSemicolon);
  }

  public void analyze(AnalyzeContext context) {
    super.analyze(context);
    if (!isStatic() && optInitializer != null)
      Jscc.error(optInitializer, "instance field initializers not yet implemented");
  }

  public void generateCode(JsWriter out) throws IOException {
    if (!isStatic())
      out.beginComment();
    super.generateCode(out);
    if (!isStatic())
      out.endComment();
  }

  public void generateIdeCode(JsWriter out) throws IOException {
    out.beginCommentWriteSymbol(optSymConstOrVar);
    out.endComment();
    out.writeSymbolWhitespace(ide.ide);
    out.writeToken(getClassDeclaration().getPath());
    out.write('.');
    out.writeSymbolToken(ide.ide);
  }



}
