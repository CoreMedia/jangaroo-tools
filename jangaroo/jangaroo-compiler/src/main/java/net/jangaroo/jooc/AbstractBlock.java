package net.jangaroo.jooc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBlock extends Statement {

  JooSymbol lBrace;
  List<Directive> directives;
  JooSymbol rBrace;
  List<CodeGenerator> blockStartCodeGenerators = new ArrayList<CodeGenerator>(3);

  public AbstractBlock(JooSymbol rBrace, List<Directive> directives, JooSymbol lBrace) {
    this.rBrace = rBrace;
    this.directives = directives;
    this.lBrace = lBrace;
  }

  @Override
  public void scope(final Scope scope) {
    scope(directives, scope);
  }

  public void addBlockStartCodeGenerator(CodeGenerator blockStartCodeGenerator) {
    blockStartCodeGenerators.add(blockStartCodeGenerator);
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    out.writeSymbol(lBrace);
    for (CodeGenerator codeGenerator : blockStartCodeGenerators) {
      codeGenerator.generateCode(out);
    }
    generateCode(directives, out);
    out.writeSymbol(rBrace);
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    analyze(this, directives, context);
  }

  public JooSymbol getSymbol() {
     return rBrace;
  }
}
