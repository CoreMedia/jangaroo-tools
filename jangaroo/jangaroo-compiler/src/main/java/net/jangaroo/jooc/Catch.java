/*
 * Copyright 2008 CoreMedia AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 */

package net.jangaroo.jooc;

import java.io.IOException;
import java.util.List;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
class Catch extends KeywordStatement {

  private JooSymbol lParen;
  private Parameter param;
  private JooSymbol rParen;
  private BlockStatement block;
  private TryStatement parentNode;

  public Catch(JooSymbol symCatch, JooSymbol lParen, Parameter param, JooSymbol rParen, BlockStatement block) {
    super(symCatch);
    this.lParen = lParen;
    this.param = param;
    this.rParen = rParen;
    this.block = block;
  }

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitCatch(this);
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    List<Catch> catches = getParentTryStatement().catches;
    Catch firstCatch = catches.get(0);
    boolean isFirst = equals(firstCatch);
    boolean isLast = equals(catches.get(catches.size() - 1));
    TypeRelation typeRelation = param.optTypeRelation;
    boolean hasCondition = hasCondition();
    if (!hasCondition && !isLast) {
      throw Jooc.error(rParen, "Only last catch clause may be untyped.");
    }
    final JooSymbol errorVar = firstCatch.param.getIde().ide;
    final JooSymbol localErrorVar = param.getIde().ide;
    // in the following, always take care to write whitespace only once!
    out.writeSymbolWhitespace(symKeyword);
    if (isFirst) {
      out.writeSymbolToken(symKeyword); // "catch"
      // "(localErrorVar)":
      out.writeSymbol(lParen, !hasCondition);
      out.writeSymbol(errorVar, !hasCondition);
      if (!hasCondition && typeRelation != null) {
        // can only be ": *", add as comment:
        typeRelation.generateCode(out);
      }
      out.writeSymbol(rParen, !hasCondition);
      if (hasCondition || !isLast) {
        // a catch block always needs a brace, so generate one for conditions:
        out.writeToken("{");
      }
    } else {
      // transform catch(ide:Type){...} into else if is(e,Type)){var ide=e;...}
      out.writeToken("else");
    }
    if (hasCondition) {
      out.writeToken("if(is");
      out.writeSymbol(lParen);
      out.writeSymbolWhitespace(localErrorVar);
      out.writeSymbolToken(errorVar);
      out.writeSymbolWhitespace(typeRelation.symRelation);
      out.writeToken(",");
      Ide typeIde = ((IdeType)typeRelation.getType()).getIde();
      out.writeSymbolWhitespace(typeIde.getIde());
      out.writeToken(typeIde.getDeclaration().getQualifiedNameStr());
      out.writeSymbol(rParen);
      out.writeToken(")");
    }
    if (!localErrorVar.getText().equals(errorVar.getText())) {
      block.addBlockStartCodeGenerator(new VarCodeGenerator(localErrorVar, errorVar));
    }
    block.generateCode(out);
    if (isLast) {
      if (hasCondition) {
        out.writeToken("else throw");
        out.writeSymbolToken(errorVar);
        out.writeToken(";");
      }
      if (!(isFirst && !hasCondition)) {
        // last catch clause closes the JS catch block:
        out.writeToken("}");
      }
    }
  }

  private TryStatement getParentTryStatement() {
    return parentNode;
  }

  private boolean hasCondition() {
    TypeRelation typeRelation = param.optTypeRelation;
    return typeRelation != null && typeRelation.getType().getSymbol().sym != sym.MUL;
  }

  @Override
  public void scope(final Scope scope) {
    if (hasCondition()) {
      scope.getClassDeclaration().addBuiltInUsage("is");
    }
    withNewDeclarationScope(this, scope, new Scoped() {
      @Override
      public void run(final Scope scope) {
        param.scope(scope);
        block.scope(scope);
      }
    });
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    this.parentNode = (TryStatement) parentNode;
    param.analyze(this, context);
    TypeRelation typeRelation = param.optTypeRelation;
    if (typeRelation != null) {
      Type type = typeRelation.getType();
      if (type instanceof IdeType) {
        IdeType ideType = (IdeType) type;
        ideType.getIde().addExternalUsage(); // init will be done by is()!
      }
    }
    block.analyze(this, context);
  }

  private static class VarCodeGenerator implements CodeGenerator {
    private final JooSymbol localErrorVar;
    private final JooSymbol errorVar;

    public VarCodeGenerator(JooSymbol localErrorVar, JooSymbol errorVar) {
      this.localErrorVar = localErrorVar;
      this.errorVar = errorVar;
    }

    public void generateCode(JsWriter out) throws IOException {
      out.writeToken("var");
      out.writeSymbolToken(localErrorVar);
      out.writeToken("=");
      out.writeSymbolToken(errorVar);
      out.writeToken(";");
    }
  }
}
