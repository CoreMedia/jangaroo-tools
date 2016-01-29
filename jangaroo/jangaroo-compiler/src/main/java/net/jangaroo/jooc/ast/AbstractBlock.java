package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.CodeGenerator;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBlock extends Statement {

  private JooSymbol lBrace;
  private List<Directive> directives;
  private JooSymbol rBrace;

  public AbstractBlock(JooSymbol rBrace, List<Directive> directives, JooSymbol lBrace) {
    this.rBrace = rBrace;
    this.directives = directives;
    this.lBrace = lBrace;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), directives);
  }

  @Override
  public void scope(final Scope scope) {
    scope(getDirectives(), scope);
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    analyze(this, getDirectives());
  }

  public JooSymbol getSymbol() {
    return getRBrace();
  }

  public JooSymbol getLBrace() {
    return lBrace;
  }

  public List<Directive> getDirectives() {
    return directives;
  }

  public JooSymbol getRBrace() {
    return rBrace;
  }

}
