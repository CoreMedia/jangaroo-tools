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

//  public void analyze(AnalyzeContext context) {
//    super.analyze(context);
//    if (!isStatic() && optInitializer != null)
//      Jscc.error(optInitializer, "instance field initializers not yet implemented");
//  }

  public void generateCode(JsWriter out) throws IOException {
    writeRuntimeModifiers(out);
    out.write('{');
    generateIdeCode(out);
    if (optTypeRelation != null)
      optTypeRelation.generateCode(out);
    if (optInitializer != null) {
      out.writeSymbolWhitespace(optInitializer.symEq);
      out.write(':');
      boolean isCompileTimeConstant = optInitializer.value.isCompileTimeConstant();
      if (!isCompileTimeConstant) {
        out.write("function(){return ");
      }
      optInitializer.value.generateCode(out);
      if (!isCompileTimeConstant) {
        out.write(";}");
      }
    } else {
      out.write(": undefined");
    }
    out.write('}');
    Debug.assertTrue(optSymSemicolon != null, "optSymSemicolon != null");
    out.write(",");
  }

  public void generateIdeCode(JsWriter out) throws IOException {
    out.beginCommentWriteSymbol(optSymConstOrVar);
    out.endComment();
    out.writeSymbolWhitespace(ide.ide);
    out.writeSymbolToken(ide.ide);
  }



}
