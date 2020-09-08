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
import net.jangaroo.jooc.sym;
import net.jangaroo.jooc.types.ExpressionType;

import java.io.IOException;
import java.util.List;

/**
 * @author Andreas Gawecki
 */
public class IdeExpr extends Expr {

  private Ide ide;
  private Expr normalizedExpr;
  private ClassDeclaration classDeclaration;

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
    Expr normalizedExpr = getNormalizedExpr();
    if (normalizedExpr != this) {
      normalizedExpr.visit(visitor);
    } else {
      visitor.visitIdeExpression(this);
    }
  }

  public Expr getNormalizedExpr() {
    if (normalizedExpr == null) {
      normalizedExpr = this;
      IdeDeclaration ideDeclaration = ide.getDeclaration(false);
      if ((ide instanceof QualifiedIde && ideDeclaration == null) ||  // qualified IDE without declaration => DotExpr
              (ideDeclaration != null && ideDeclaration.isClassMember())) {  // "this." or "<Class>." may have to be synthesized
        DotExpr dotExpr = null;
        if (ide instanceof QualifiedIde) {
          dotExpr = new DotExpr(new IdeExpr(ide.getQualifier()).getNormalizedExpr(), ((QualifiedIde)ide).getSymDot(), new Ide(ide.getIde()));
        } else if (!ideDeclaration.isStatic()) {
          // non-static class member: synthesize "this."
          JooSymbol ideSymbol = ide.getSymbol();
          Ide thisIde = new Ide(ideSymbol.replacingSymAndTextAndJooValue(sym.THIS, Ide.THIS, null));
          if (ide.isRewriteThis()) {
            thisIde.setRewriteThis(true);
            setThisDeclaration(thisIde);
          }
          dotExpr = new DotExpr(new IdeExpr(thisIde), synthesizeDotSymbol(ideSymbol), new Ide(ideSymbol.withoutWhitespace()));
        } else if (!ideDeclaration.isPrivate()) {
          // non-private static class member: synthesize "<Class>."
          JooSymbol ideSymbol = ide.getSymbol();
          ClassDeclaration classDeclaration = ideDeclaration.getClassDeclaration();
          Ide classIde = new Ide(ideSymbol.replacingSymAndTextAndJooValue(sym.IDE, classDeclaration.getName(), null));
          classIde.setDeclaration(classDeclaration); // must not be resolved again, as implicit imports through super class chain are not found in scope!
          dotExpr = new DotExpr(new IdeExpr(classIde), synthesizeDotSymbol(ideSymbol), new Ide(ideSymbol.withoutWhitespace()));
        }
        if (dotExpr != null) {
          normalizedExpr = dotExpr;
          dotExpr.setOriginal(this);
        }
      }
    }
    return normalizedExpr;
  }

  private void setThisDeclaration(Ide thisIde) {
    Scope scope = ide.getScope();
    while (scope != null && scope.getFunctionExpr() != null) {
      FunctionDeclaration functionDeclaration = scope.getFunctionExpr().getFunctionDeclaration();
      if (functionDeclaration != null && functionDeclaration.isClassMember()) {
        // found method that defines whose scope contains the "outer this" declaration:
        thisIde.setDeclaration(scope.lookupDeclaration(thisIde));
        break;
      }
      scope = scope.getParentScope();
    }
  }

  private static JooSymbol synthesizeDotSymbol(JooSymbol ideSymbol) {
    return ideSymbol.replacingSymAndTextAndJooValue(sym.DOT, ".", null).withoutWhitespace();
  }

  @Override
  public void scope(final Scope scope) {
    getIde().scope(scope);
    classDeclaration = scope.getClassDeclaration();
  }

  @Override
  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    Ide ide = getIde();
    ide.analyze(this);
    ide.analyzeAsExpr(parentNode, this);
    Expr normalizedExpr = getNormalizedExpr();
    if (normalizedExpr == this // normalized DotExpr has to check for itself!
            && !ide.isDeclared()) {
      ide.getScope().getCompiler().getLog().error(ide.getSymbol(), "undeclared identifier '" + ide.getName() + "'.");
    }
    ExpressionType type = normalizedExpr != this
            ? normalizedExpr.getType()
            : ide.getScope().getExpressionType(ide.getDeclaration(false));
    setType(type);
  }

  public JooSymbol getSymbol() {
    Expr normalizedExpr = getNormalizedExpr();
    if (normalizedExpr != this) {
      return normalizedExpr.getSymbol();
    }
    return getIde().getSymbol();
  }

  @Override
  public boolean isRuntimeConstant() {
    String qualifiedNameStr = ide.getQualifiedNameStr();
    return qualifiedNameStr.equals("undefined") || qualifiedNameStr.equals("NaN");
  }

  @Override
  public boolean isStandAloneConstant() {
    if (isRuntimeConstant()) {
      return true;
    }

    IdeDeclaration declaration = ide.getDeclaration(false);
    if (declaration instanceof VariableDeclaration) {
      VariableDeclaration variableDeclaration = (VariableDeclaration) declaration;
      // Stand-alone constants may be used in other stand-alone constants
      // of the same class.
      if (variableDeclaration.getClassDeclaration() == classDeclaration && variableDeclaration.isDeclaringStandAloneConstant()) {
        return true;
      }
    }
    return false;
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
