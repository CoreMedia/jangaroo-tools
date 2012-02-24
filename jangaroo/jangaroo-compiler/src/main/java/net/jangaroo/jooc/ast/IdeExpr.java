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

package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;

import java.io.IOException;
import java.util.List;

/**
 * @author Andreas Gawecki
 */
public class IdeExpr extends Expr {

  private Ide ide;

  public IdeExpr(JooSymbol symIde) {
    this(new Ide(symIde));
  }

  public IdeExpr(Ide ide) {
    this.ide = ide;
  }

  public static IdeExpr fromPrefix(JooSymbol symPrefix, JooSymbol symDot, Ide ide) {
    return new IdeExpr(ide.qualify(symPrefix, symDot));
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), ide);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitIdeExpression(this);
  }

  @Override
  public void scope(final Scope scope) {
    getIde().scope(scope);
  }

  @Override
  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    getIde().analyze(this);
    getIde().analyzeAsExpr(parentNode, this);
    setType(getIde().resolveDeclaration());
  }

  public JooSymbol getSymbol() {
    return getIde().getSymbol();
  }

  @Override
  public boolean isRuntimeConstant() {
    String qualifiedNameStr = ide.getQualifiedNameStr();
    return qualifiedNameStr.equals("undefined") || qualifiedNameStr.equals("NaN");
  }

  @Override
  public boolean isCompileTimeConstant() {
    if (isRuntimeConstant()) {
      return true;
    }

    IdeDeclaration declaration = ide.getDeclaration(false);
    if (declaration != null) {
      return declaration.isDeclaringCompileTimeConstant();
    } else if (ide.isQualified()) {
      IdeDeclaration qualifierDeclaration = ide.getQualifier().getDeclaration(false);
      if (qualifierDeclaration instanceof ClassDeclaration) {
        ClassDeclaration classDeclaration = (ClassDeclaration) qualifierDeclaration;
        TypedIdeDeclaration staticMemberDeclaration = classDeclaration.getStaticMemberDeclaration(ide.getName());
        return staticMemberDeclaration.isDeclaringCompileTimeConstant();
      }
    }
    return false;
  }

  public final Ide getIde() {
    return ide;
  }

}
