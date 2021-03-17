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
import net.jangaroo.jooc.types.ExpressionType;

import java.io.IOException;
import java.util.List;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class DotExpr extends PostfixOpExpr {

  private Ide ide;

  public DotExpr(Expr expr, JooSymbol symDot, Ide ide) {
    super(symDot, expr);
    this.ide = ide;
  }

  void setOriginal(IdeExpr ideExpr) {
    scope(ideExpr.getIde().getScope());
    getIde().setBound(ideExpr.getIde().isBound());
    analyze(ideExpr.getParentNode());
    if (getType() == null) {
      setType(ideExpr.getType());
    }
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), ide);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitDotExpr(this);
  }

  public Ide getIde() {
    return ide;
  }

  @Override
  public void scope(final Scope scope) {
    super.scope(scope);
    getIde().scope(scope);
  }

  @Override
  public void analyze(final AstNode parentNode) {
    super.analyze(parentNode);
    ide.analyze(this);
    ExpressionType qualifierExpressionType = getArg().getType();
    if (qualifierExpressionType != null) {
      IdeDeclaration memberDeclaration = qualifierExpressionType.resolvePropertyDeclaration(getIde().getName());
      if (memberDeclaration == null) {
        if (!qualifierExpressionType.getDeclaration().isDynamic()) {
          getIde().getScope().getCompiler().getLog().error(getIde().getIde(), "cannot resolve member '" + getIde().getName() + "'.");
        }
      } else {
        if (memberDeclaration instanceof PropertyDeclaration) {
          PropertyDeclaration propertyDeclaration = (PropertyDeclaration) memberDeclaration;
          // It makes a difference whether we read or write.
          // If we are the left-hand-side of an assignment, use setter, else, use getter:
          memberDeclaration = ide.isAssignmentLHS() ? propertyDeclaration.getSetter() : propertyDeclaration.getGetter();
        }
        ExpressionType type = memberDeclaration.getType();
        if (type != null) {
          type = type.getEvalType();
        }
        if (type == null) {
          type = getIde().getScope().getExpressionType(memberDeclaration);
        }
        setType(type);
        if (type != null && !type.isConfigType() && memberDeclaration instanceof TypedIdeDeclaration &&
                ((TypedIdeDeclaration) memberDeclaration).isBindable()) {
          getIde().getScope().getCompilationUnit().addBuiltInIdentifierUsage("asConfig");
        }
      }
    }
  }

}
