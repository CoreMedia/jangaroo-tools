/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class FieldDeclaration extends AbstractVariableDeclaration {

  public FieldDeclaration(JooSymbol[] modifiers, JooSymbol symConstOrVar, Ide ide,
      TypeRelation optTypeRelation, Initializer optInitializer, JooSymbol symSemicolon) {
    super(modifiers,
            MODIFIERS_SCOPE|MODIFIER_STATIC,
            symConstOrVar, ide, optTypeRelation, optInitializer, symSemicolon);
  }

//  public void analyze(AnalyzeContext context) {
//    super.analyze(context);
//    if (!isStatic() && optInitializer != null)
//      Jooc.error(optInitializer, "instance field initializers not yet implemented");
//  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbolWhitespace(optSymConstOrVar);
    if (!writeRuntimeModifiersUnclosed(out)) {
      out.write("\"");
    }
    out.writeSymbolToken(optSymConstOrVar);
    out.write("\",");
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
    out.writeSymbolWhitespace(ide.ide);
    out.writeSymbolToken(ide.ide);
  }



}
