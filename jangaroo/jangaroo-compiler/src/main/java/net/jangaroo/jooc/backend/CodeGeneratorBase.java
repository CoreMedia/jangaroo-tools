package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.SyntacticKeywords;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitor;
import net.jangaroo.jooc.ast.BinaryOpExpr;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.ConditionalExpr;
import net.jangaroo.jooc.ast.Declaration;
import net.jangaroo.jooc.ast.DotExpr;
import net.jangaroo.jooc.ast.GetterSetterPair;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeExpr;
import net.jangaroo.jooc.ast.IsExpr;
import net.jangaroo.jooc.ast.LiteralExpr;
import net.jangaroo.jooc.ast.PostfixOpExpr;
import net.jangaroo.jooc.ast.PredefinedTypeDeclaration;
import net.jangaroo.jooc.ast.PrefixOpExpr;

import java.io.IOException;

public abstract class CodeGeneratorBase implements AstVisitor {
  private final JsWriter out;

  public CodeGeneratorBase(JsWriter out) {
    this.out = out;
  }

  public JsWriter getOut() {
    return out;
  }

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

  @Override
  public void visitLiteralExpr(LiteralExpr literalExpr) throws IOException {
    out.writeSymbol(literalExpr.getValue());
  }

  @Override
  public void visitPostfixOpExpr(PostfixOpExpr postfixOpExpr) throws IOException {
    postfixOpExpr.getArg().visit(this);
    out.writeSymbol(postfixOpExpr.getOp());
  }

  @Override
  public void visitDotExpr(DotExpr dotExpr) throws IOException {
    dotExpr.getArg().visit(this);
    Ide.writeMemberAccess(Ide.resolveMember(dotExpr.getArg().getType(), dotExpr.getIde()), dotExpr.getOp(), dotExpr.getIde(), true, out);
  }

  @Override
  public void visitPrefixOpExpr(PrefixOpExpr prefixOpExpr) throws IOException {
    out.writeSymbol(prefixOpExpr.getOp());
    prefixOpExpr.getArg().visit(this);
  }

  @Override
  public void visitBinaryOpExpr(BinaryOpExpr binaryOpExpr) throws IOException {
    binaryOpExpr.getArg1().visit(this);
    out.writeSymbol(binaryOpExpr.getOp());
    binaryOpExpr.getArg2().visit(this);
  }

  @Override
  public void visitIsExpr(IsExpr isExpr) throws IOException {
    visitInfixOpExpr(isExpr);
  }

  @Override
  public void visitConditionalExpr(ConditionalExpr conditionalExpr) throws IOException {
    conditionalExpr.getCond().visit(this);
    out.writeSymbol(conditionalExpr.getSymQuestion());
    conditionalExpr.getIfTrue().visit(this);
    out.writeSymbol(conditionalExpr.getSymColon());
    conditionalExpr.getIfFalse().visit(this);
  }

  @Override
  public <T extends AstNode> void visitCommaSeparatedList(CommaSeparatedList<T> commaSeparatedList) throws IOException {
    if (commaSeparatedList.getHead() != null) {
      commaSeparatedList.getHead().visit(this);
    }
    if (commaSeparatedList.getSymComma() != null) {
      out.writeSymbol(commaSeparatedList.getSymComma());
      if (commaSeparatedList.getTail() != null) {
        commaSeparatedList.getTail().visit(this);
      }
    }
  }

  @Override
  public void visitGetterSetterPair(GetterSetterPair getterSetterPair) throws IOException {
    throw new IllegalStateException("GetterSetterPair#generateCode() should never be called!");
  }

  @Override
  public void visitPredefinedTypeDeclaration(PredefinedTypeDeclaration predefinedTypeDeclaration) throws IOException {
    throw new IllegalStateException("there should be no code generation for predefined types");
  }
}
