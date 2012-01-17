package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitor;
import net.jangaroo.jooc.ast.BinaryOpExpr;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.ConditionalExpr;
import net.jangaroo.jooc.ast.Declaration;
import net.jangaroo.jooc.ast.DotExpr;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IsExpr;
import net.jangaroo.jooc.ast.LiteralExpr;
import net.jangaroo.jooc.ast.Parameters;
import net.jangaroo.jooc.ast.PostfixOpExpr;
import net.jangaroo.jooc.ast.PredefinedTypeDeclaration;
import net.jangaroo.jooc.ast.PrefixOpExpr;

import java.io.IOException;

public abstract class CodeGeneratorBase implements AstVisitor {
  protected final JsWriter out;

  public CodeGeneratorBase(JsWriter out) {
    this.out = out;
  }

  protected void writeModifiers(JsWriter out, Declaration declaration) throws IOException {
    for (JooSymbol modifier : declaration.getSymModifiers()) {
      out.writeSymbol(modifier);
    }
  }

  @Override
  public final void visitLiteralExpr(LiteralExpr literalExpr) throws IOException {
    out.writeSymbol(literalExpr.getValue());
  }

  @Override
  public final void visitPostfixOpExpr(PostfixOpExpr postfixOpExpr) throws IOException {
    postfixOpExpr.getArg().visit(this);
    out.writeSymbol(postfixOpExpr.getOp());
  }

  @Override
  public final void visitDotExpr(DotExpr dotExpr) throws IOException {
    dotExpr.getArg().visit(this);
    Ide.writeMemberAccess(Ide.resolveMember(dotExpr.getArg().getType(), dotExpr.getIde()), dotExpr.getOp(), dotExpr.getIde(), true, out);
  }

  @Override
  public final void visitPrefixOpExpr(PrefixOpExpr prefixOpExpr) throws IOException {
    out.writeSymbol(prefixOpExpr.getOp());
    prefixOpExpr.getArg().visit(this);
  }

  @Override
  public final void visitBinaryOpExpr(BinaryOpExpr binaryOpExpr) throws IOException {
    binaryOpExpr.getArg1().visit(this);
    out.writeSymbol(binaryOpExpr.getOp());
    binaryOpExpr.getArg2().visit(this);
  }

  @Override
  public final void visitIsExpr(IsExpr isExpr) throws IOException {
    visitInfixOpExpr(isExpr);
  }

  @Override
  public final void visitConditionalExpr(ConditionalExpr conditionalExpr) throws IOException {
    conditionalExpr.getCond().visit(this);
    out.writeSymbol(conditionalExpr.getSymQuestion());
    conditionalExpr.getIfTrue().visit(this);
    out.writeSymbol(conditionalExpr.getSymColon());
    conditionalExpr.getIfFalse().visit(this);
  }

  @Override
  public final <T extends AstNode> void visitCommaSeparatedList(CommaSeparatedList<T> commaSeparatedList) throws IOException {
    visitIfNotNull(commaSeparatedList.getHead());
    if (commaSeparatedList.getSymComma() != null) {
      out.writeSymbol(commaSeparatedList.getSymComma());
      visitIfNotNull(commaSeparatedList.getTail());
    }
  }

  @Override
  public void visitParameters(Parameters parameters) throws IOException {
    visitCommaSeparatedList(parameters);
  }

  @Override
  public final void visitPredefinedTypeDeclaration(PredefinedTypeDeclaration predefinedTypeDeclaration) throws IOException {
    throw new IllegalStateException("there should be no code generation for predefined types");
  }

  protected void writeOptSymbol(JooSymbol symbol) throws IOException {
    if (symbol != null) {
      out.writeSymbol(symbol);
    }
  }

  protected void writeOptSymbol(JooSymbol optSymbol, String defaultToken) throws IOException {
    if (optSymbol != null) {
      out.writeSymbol(optSymbol);
    } else {
      out.writeToken(defaultToken);
    }
  }

  protected void writeSymbolReplacement(JooSymbol symbol, String replacementToken) throws IOException {
    if (symbol != null) {
      out.writeSymbolWhitespace(symbol);
    }
    out.writeToken(replacementToken);
  }

  protected void visitIfNotNull(AstNode args) throws IOException {
    if (args != null) {
      args.visit(this);
    }
  }

  protected void visitIfNotNull(AstNode args, String replacementToken) throws IOException {
    if (args != null) {
      args.visit(this);
    } else {
      out.writeToken(replacementToken);
    }
  }

  protected void visitAll(Iterable<? extends AstNode> nodes) throws IOException {
    for (AstNode node : nodes) {
      node.visit(this);
    }
  }

}
