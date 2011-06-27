package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.SyntacticKeywords;
import net.jangaroo.jooc.ast.Declaration;
import net.jangaroo.jooc.ast.Ide;

import java.io.IOException;

public class CodeGeneratorBase {
  public void writeIde(JsWriter out, Ide ide) throws IOException {
    // take care of reserved words called as functions (Rhino does not like):
    if (SyntacticKeywords.RESERVED_WORDS.contains(ide.getIde().getText())) {
      out.writeToken("$$" + ide.getIde().getText());
    } else {
      out.writeSymbol(ide.getIde(), false);
    }
  }

  protected void writeModifiers(JsWriter out, Declaration declaration) throws IOException {
    for (JooSymbol modifier : declaration.getSymModifiers()) {
      out.writeSymbol(modifier);
    }
  }
}
