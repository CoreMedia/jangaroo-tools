package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.AnalyzeContext;
import net.jangaroo.jooc.CodeGenerator;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.Scope;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBlock extends Statement {

  private JooSymbol lBrace;
  private List<Directive> directives;
  private JooSymbol rBrace;
  private List<CodeGenerator> blockStartCodeGenerators = new ArrayList<CodeGenerator>(3);

  public AbstractBlock(JooSymbol rBrace, List<Directive> directives, JooSymbol lBrace) {
    this.setRBrace(rBrace);
    this.setDirectives(directives);
    this.setLBrace(lBrace);
  }

  @Override
  public void scope(final Scope scope) {
    scope(getDirectives(), scope);
  }

  public void addBlockStartCodeGenerator(CodeGenerator blockStartCodeGenerator) {
    getBlockStartCodeGenerators().add(blockStartCodeGenerator);
  }

  public void generateJsCode(JsWriter out) throws IOException {
    throw new UnsupportedOperationException();
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    analyze(this, getDirectives(), context);
  }

  public JooSymbol getSymbol() {
     return getRBrace();
  }

  public JooSymbol getLBrace() {
    return lBrace;
  }

  public void setLBrace(JooSymbol lBrace) {
    this.lBrace = lBrace;
  }

  public List<Directive> getDirectives() {
    return directives;
  }

  public void setDirectives(List<Directive> directives) {
    this.directives = directives;
  }

  public JooSymbol getRBrace() {
    return rBrace;
  }

  public void setRBrace(JooSymbol rBrace) {
    this.rBrace = rBrace;
  }

  public List<CodeGenerator> getBlockStartCodeGenerators() {
    return blockStartCodeGenerators;
  }
}
