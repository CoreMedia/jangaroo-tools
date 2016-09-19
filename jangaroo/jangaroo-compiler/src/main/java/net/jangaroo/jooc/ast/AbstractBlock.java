package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.CodeGenerator;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractBlock extends Statement {

  private JooSymbol lBrace;
  private List<Directive> directives;
  private JooSymbol rBrace;
  private Scope scope;

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
    this.scope = scope;
    scope(getDirectives(), scope);
  }

  public Scope getScope() {
    return scope;
  }

  public void addDirective(Directive directive) {
    if (!(directives instanceof ArrayList)) {
      directives = new ArrayList<>(directives);
    }
    directives.add(directive);
    if (scope != null) {
      directive.scope(scope);
    }
  }

  public void addDirectives(Collection<? extends Directive> directives) {
    for (Directive directive : directives) {
      addDirective(directive);
    }
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
