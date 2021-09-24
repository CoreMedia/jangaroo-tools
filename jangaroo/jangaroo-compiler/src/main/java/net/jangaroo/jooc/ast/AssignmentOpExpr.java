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


/**
 * @author Andreas Gawecki
 */
public class AssignmentOpExpr extends BinaryOpExpr {
  private Scope scope;

  public AssignmentOpExpr(Expr arg1, JooSymbol op, Expr arg2) {
    super(arg1, op, arg2);
  }

  @Override
  public void scope(Scope scope) {
    super.scope(scope);
    this.scope = scope;
  }

  @Override
  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    ExpressionType rhsType = getArg2().getType();
    ExpressionType lhsType = getArg1().getType();
    if (lhsType == null) {
      lhsType = rhsType;
    } else if (rhsType != null && rhsType.isConfigType()) {
      if (lhsType.markAsConfigTypeIfPossible()) {
        scope.getCompilationUnit().addBuiltInIdentifierUsage("Config");
      }
    }
    setType(lhsType);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitAssignmentOpExpr(this);
  }

}
