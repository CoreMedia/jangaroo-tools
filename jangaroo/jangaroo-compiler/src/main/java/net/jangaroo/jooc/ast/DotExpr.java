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

import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.utils.AS3Type;

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
    getIde().setBound(ideExpr.getIde().isBound());
    scope(ideExpr.getIde().getScope());
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
    IdeDeclaration qualiferType = getArg().getType();
    if (qualiferType instanceof ClassDeclaration && !AS3Type.ANY.name.equals(qualiferType.getName())) {
      IdeDeclaration memberDeclaration = qualiferType.resolvePropertyDeclaration(getIde().getName());
      if (memberDeclaration == null) {
        if (!qualiferType.isDynamic()) {
          getIde().getScope().getCompiler().getLog().error(getIde().getIde(), "cannot resolve member '" + getIde().getName() + "'.");
        }
        return;
      }
      if (memberDeclaration.isStatic()) {
        throw JangarooParser.error(getIde().getIde(), "static member used in dynamic context");
      }
      if (memberDeclaration instanceof Typed) {
        TypeRelation typeRelation = ((Typed) memberDeclaration).getOptTypeRelation();
        if (typeRelation != null) {
          memberDeclaration = typeRelation.getType().getIde().getDeclaration();
        }
      }
      setType(memberDeclaration);
    }

  }

}
